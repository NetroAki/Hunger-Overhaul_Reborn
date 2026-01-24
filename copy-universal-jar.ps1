# PowerShell script to copy the universal JAR to PrismLauncher mod directories
# Run this from the project root directory

$sourceJar = "build\libs\hunger_overhaul_reborn-0.2.1-beta-universal-1.20.1.jar"
$destinations = @(
    "C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.20.1(5)\minecraft\mods",
    "C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.20.1(6)\minecraft\mods"
)

# Check if source JAR exists
if (-not (Test-Path $sourceJar)) {
    Write-Error "Source JAR not found: $sourceJar"
    Write-Host "Make sure you're running this script from the project root directory."
    exit 1
}

Write-Host "Copying universal JAR to PrismLauncher instances..."
Write-Host "Source: $sourceJar"
Write-Host ""

$successCount = 0
$totalCount = $destinations.Count

foreach ($dest in $destinations) {
    try {
        # Check if destination directory exists
        if (-not (Test-Path $dest)) {
            Write-Warning "Destination directory does not exist: $dest"
            continue
        }
        
        # Copy the JAR
        Copy-Item -Path $sourceJar -Destination $dest -Force
        Write-Host "✓ Copied to: $dest" -ForegroundColor Green
        $successCount++
    }
    catch {
        Write-Error "Failed to copy to $dest`: $($_.Exception.Message)"
    }
}

Write-Host ""
Write-Host "Copy operation completed!"
Write-Host "Successfully copied to $successCount out of $totalCount destinations." -ForegroundColor Cyan

if ($successCount -eq $totalCount) {
    Write-Host "All copies successful! 🎉" -ForegroundColor Green
}
elseif ($successCount -gt 0) {
    Write-Host "Some copies failed. Check the errors above." -ForegroundColor Yellow
}
else {
    Write-Host "No copies were successful." -ForegroundColor Red
}
