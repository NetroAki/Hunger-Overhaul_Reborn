@echo off
echo Building Hunger Overhaul Reborn...

echo.
echo Building for Fabric...
call gradlew fabric:build

echo.
echo Building for Forge...
call gradlew forge:build

echo.
echo Build complete! Check the build/libs folders in fabric/ and forge/ directories.
pause
