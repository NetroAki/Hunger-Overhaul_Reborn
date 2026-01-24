@echo off
REM Batch script to copy the universal JAR to PrismLauncher mod directories
REM Run this from the project root directory

echo Copying universal JAR to PrismLauncher instances...
echo.

set "sourceJar=build\libs\hunger_overhaul_reborn-0.2.1-beta-universal.jar"

REM Check if source JAR exists
if not exist "%sourceJar%" (
    echo ERROR: Source JAR not found: %sourceJar%
    echo Make sure you're running this script from the project root directory.
    pause
    exit /b 1
)

set "successCount=0"
set "totalCount=4"

REM Copy to each destination
echo Copying to: C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.21.1 test client fabric\minecraft\mods
if exist "C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.21.1 test client fabric\minecraft\mods" (
    copy "%sourceJar%" "C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.21.1 test client fabric\minecraft\mods\" >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✓ Success
        set /a successCount+=1
    ) else (
        echo ✗ Failed
    )
) else (
    echo ✗ Directory does not exist
)

echo Copying to: C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.20.1(5)\minecraft\mods
if exist "C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.20.1(5)\minecraft\mods" (
    copy "%sourceJar%" "C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.20.1(5)\minecraft\mods\" >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✓ Success
        set /a successCount+=1
    ) else (
        echo ✗ Failed
    )
) else (
    echo ✗ Directory does not exist
)

echo Copying to: C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.20.1(6)\minecraft\mods
if exist "C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.20.1(6)\minecraft\mods" (
    copy "%sourceJar%" "C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.20.1(6)\minecraft\mods\" >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✓ Success
        set /a successCount+=1
    ) else (
        echo ✗ Failed
    )
) else (
    echo ✗ Directory does not exist
)

echo Copying to: C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.21.1(2)\minecraft\mods
if exist "C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.21.1(2)\minecraft\mods" (
    copy "%sourceJar%" "C:\Users\zacpa\AppData\Roaming\PrismLauncher\instances\1.21.1(2)\minecraft\mods\" >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✓ Success
        set /a successCount+=1
    ) else (
        echo ✗ Failed
    )
) else (
    echo ✗ Directory does not exist
)

echo.
echo Copy operation completed!
echo Successfully copied to %successCount% out of %totalCount% destinations.

if %successCount% equ %totalCount% (
    echo All copies successful! 🎉
) else if %successCount% gtr 0 (
    echo Some copies failed. Check the results above.
) else (
    echo No copies were successful.
)

echo.
pause
