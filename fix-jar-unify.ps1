# Unify universal JAR for Fabric/Forge:
# - Relocate version-prefixed class/resource folders to root
# - Remove prefixed mixin configs and refmaps
# - Create clean Fabric/Forge mixin configs
# - Fix fabric.mod.json to point to clean config and entrypoints
param(
    [string]$JarPath = "build\libs\hunger_overhaul_reborn-0.2.1-beta-universal-1.20.1.jar"
)

Write-Host "Unifying universal JAR: $JarPath"

$tempDir = "temp-jar-unify"
$zipCopy = "$JarPath.zip"
if (Test-Path $tempDir) { Remove-Item $tempDir -Recurse -Force }
if (Test-Path $zipCopy) { Remove-Item $zipCopy -Force }
New-Item -ItemType Directory -Path $tempDir | Out-Null

try {
    if (!(Test-Path $JarPath)) { throw "Jar not found: $JarPath" }

    Copy-Item $JarPath $zipCopy -Force
    Expand-Archive -Path $zipCopy -DestinationPath $tempDir -Force

    # Ensure root targets
    $rootOrg = Join-Path $tempDir 'org'
    $rootAssets = Join-Path $tempDir 'assets'
    $rootData = Join-Path $tempDir 'data'
    foreach ($d in @($rootOrg, $rootAssets, $rootData)) { if (!(Test-Path $d)) { New-Item -ItemType Directory -Path $d | Out-Null } }

    # Merge any nested org/assets/data trees into root
    $nested = Get-ChildItem -Path $tempDir -Directory -Recurse | Where-Object { $_.Name -in @('org', 'assets', 'data') -and $_.FullName -ne $rootOrg -and $_.FullName -ne $rootAssets -and $_.FullName -ne $rootData }
    foreach ($dir in $nested) {
        $target = switch ($dir.Name) { 'org' { $rootOrg } 'assets' { $rootAssets } 'data' { $rootData } }
        Write-Host "Merging $($dir.FullName) -> $target"
        Copy-Item -Path (Join-Path $dir.FullName '*') -Destination $target -Recurse -Force -ErrorAction SilentlyContinue
    }

    # Remove obvious version/loader prefix dirs if present
    Get-ChildItem -Path $tempDir -Directory | Where-Object { $_.Name -match '^(\d|1\.\d|fabric|forge)' } | ForEach-Object {
        try { Remove-Item $_.FullName -Recurse -Force -ErrorAction SilentlyContinue } catch {}
    }

    # Remove all existing HOR mixin configs/refmaps with prefixes
    Get-ChildItem -Path $tempDir -File -Recurse | Where-Object { $_.Name -match 'hunger_overhaul_reborn.*\.mixins\.json' -or $_.Name -match 'refmap.*\.json' } | ForEach-Object {
        if ($_.Name -notin @('hunger_overhaul_reborn-fabric.mixins.json', 'hunger_overhaul_reborn-forge.mixins.json')) {
            Write-Host "Removing mixin/refmap: $($_.FullName)"
            Remove-Item $_.FullName -Force -ErrorAction SilentlyContinue
        }
    }

    # Write clean Fabric mixin config
    $fabricMixin = @"
{
  "required": true,
  "package": "org.Netroaki.Main.mixin.fabric",
  "compatibilityLevel": "JAVA_17",
  "minVersion": "0.8.5",
  "plugin": "org.Netroaki.Main.mixin.fabric.HORMixinPlugin",
  "mixins": [
    "AgeableBlockMixin",
    "BowlFoodItemMixin",
    "FoodItemMixin",
    "HoneyBottleItemMixin",
    "ItemsMixin",
    "MilkBucketItemMixin",
    "SuspiciousStewItemMixin"
  ],
  "client": [],
  "injectors": { "defaultRequire": 1 }
}
"@
    Set-Content -Path (Join-Path $tempDir 'hunger_overhaul_reborn-fabric.mixins.json') -Value $fabricMixin -Encoding UTF8

    # Keep/refresh Forge mixin config if desired (optional)
    $forgeMixinPath = Join-Path $tempDir 'hunger_overhaul_reborn-forge.mixins.json'
    if (!(Test-Path $forgeMixinPath)) {
        $forgeMixin = @"
{
  "required": true,
  "package": "org.Netroaki.Main.mixin.forge",
  "compatibilityLevel": "JAVA_17",
  "minVersion": "0.8.5",
  "plugin": "org.Netroaki.Main.mixin.forge.HORMixinPlugin",
  "mixins": [
    "BowlFoodItemMixin",
    "FoodItemMixin",
    "HoneyBottleItemMixin",
    "ItemsMixin",
    "MilkBucketItemMixin",
    "SuspiciousStewItemMixin"
  ],
  "client": [],
  "injectors": { "defaultRequire": 1 }
}
"@
        Set-Content -Path $forgeMixinPath -Value $forgeMixin -Encoding UTF8
    }

    # Fix fabric.mod.json
    $fabricModPath = Join-Path $tempDir 'fabric.mod.json'
    if (Test-Path $fabricModPath) {
        $json = Get-Content $fabricModPath -Raw | ConvertFrom-Json
        $json.mixins = @('hunger_overhaul_reborn-fabric.mixins.json')
        if (-not $json.entrypoints) { $json | Add-Member -NotePropertyName 'entrypoints' -NotePropertyValue @{ } }
        $json.entrypoints.main = @('org.Netroaki.Main.platforms.fabric.HORebornFabric')
        $json.entrypoints.client = @('org.Netroaki.Main.platforms.fabric.client.HORebornFabricClient')
        $json | ConvertTo-Json -Depth 10 | Set-Content -Path $fabricModPath -Encoding UTF8
        Write-Host "fabric.mod.json updated"
    }
    else {
        Write-Warning "fabric.mod.json not found in JAR; Fabric may not detect the mod."
    }

    # Repack
    $zipOut = "$JarPath.tmp.zip"
    if (Test-Path $zipOut) { Remove-Item $zipOut -Force }
    Compress-Archive -Path (Join-Path $tempDir '*') -DestinationPath $zipOut -Force
    Move-Item -Path $zipOut -Destination $JarPath -Force

    # Verify critical entries
    Write-Host "Verifying:"
    & jar -tf $JarPath | Select-String 'fabric.mod.json', 'hunger_overhaul_reborn-fabric.mixins.json', 'org/Netroaki/Main/mixin/fabric/HORMixinPlugin.class', 'org/Netroaki/Main/mixin/fabric/AgeableBlockMixin.class'
    Write-Host "Unified JAR successfully."
}
finally {
    if (Test-Path $tempDir) { Remove-Item $tempDir -Recurse -Force }
    if (Test-Path $zipCopy) { Remove-Item $zipCopy -Force }
}


