#!/bin/bash

echo "Building Hunger Overhaul Reborn..."

echo
echo "Building for Fabric..."
./gradlew fabric:build

echo
echo "Building for Forge..."
./gradlew forge:build

echo
echo "Build complete! Check the build/libs folders in fabric/ and forge/ directories."
