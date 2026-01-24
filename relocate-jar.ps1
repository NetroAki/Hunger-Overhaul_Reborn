# Relocate prefixed class/resource folders inside the universal JAR to the root
param(
    [string]$JarPath = "build\libs\hunger_overhaul_reborn-0.2.1-beta-universal-1.20.1.jar"
)

Write-Host "Relocating class/resources in JAR: $JarPath"

$tempDir = "temp-jar-relocate"
if (Test-Path $tempDir) {
    Remove-Item $tempDir -Recurse -Force
}
New-Item -ItemType Directory -Path $tempDir | Out-Null

try {
    # Expand-Archive supports only .zip. Copy JAR to .zip first, then extract
    $zipCopy = "$JarPath.zip"
    if (Test-Path $zipCopy) { Remove-Item $zipCopy -Force }
    Copy-Item $JarPath $zipCopy -Force
    Expand-Archive -Path $zipCopy -DestinationPath $tempDir -Force

    # Ensure root targets exist
    $rootOrg = Join-Path $tempDir 'org'
    $rootAssets = Join-Path $tempDir 'assets'
    $rootData = Join-Path $tempDir 'data'
    if (!(Test-Path $rootOrg)) { New-Item -ItemType Directory -Path $rootOrg    | Out-Null }
    if (!(Test-Path $rootAssets)) { New-Item -ItemType Directory -Path $rootAssets | Out-Null }
    if (!(Test-Path $rootData)) { New-Item -ItemType Directory -Path $rootData   | Out-Null }

    # Find and merge any nested 'org', 'assets', 'data' directories into root
    $nestedDirs = Get-ChildItem -Path $tempDir -Directory -Recurse | Where-Object {
        $_.FullName -ne $rootOrg -and (
            $_.Name -eq 'org' -or $_.Name -eq 'assets' -or $_.Name -eq 'data'
        )
    }

    foreach ($dir in $nestedDirs) {
        $target = switch ($dir.Name) {
            'org' { $rootOrg }
            'assets' { $rootAssets }
            'data' { $rootData }
            default { $null }
        }
        if ($null -ne $target) {
            Write-Host "Merging $($dir.FullName) -> $target"
            Copy-Item -Path (Join-Path $dir.FullName '*') -Destination $target -Recurse -Force -ErrorAction SilentlyContinue
        }
    }

    # Remove any top-level version/loader directories if present
    Get-ChildItem -Path $tempDir -Directory | Where-Object { $_.Name -match '^(1|\d+)$' -or $_.Name -match 'fabric|forge' } | ForEach-Object {
        try { Remove-Item $_.FullName -Recurse -Force -ErrorAction SilentlyContinue } catch {}
    }

    # Repack JAR (Compress-Archive only supports .zip → then rename to .jar)
    $zipOut = "$JarPath.tmp.zip"
    if (Test-Path $zipOut) { Remove-Item $zipOut -Force }
    Compress-Archive -Path (Join-Path $tempDir '*') -DestinationPath $zipOut -Force
    if (Test-Path $JarPath) { Remove-Item $JarPath -Force }
    Move-Item $zipOut $JarPath -Force

    # Verify expected classes exist at root
    Write-Host "Verifying relocated classes:"
    & jar -tf $JarPath | Select-String "org/Netroaki/Main/mixin/fabric/HORMixinPlugin.class", "org/Netroaki/Main/mixin/fabric/AgeableBlockMixin.class", "org/Netroaki/Main/platforms/fabric/HORebornFabric.class"

    Write-Host "Relocation complete."
}
finally {
    if (Test-Path $tempDir) { Remove-Item $tempDir -Recurse -Force }
    if (Test-Path $zipCopy) { Remove-Item $zipCopy -Force }
}


