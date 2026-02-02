#!/bin/bash

# Clean and Build everything using the standard Gradle tasks
echo "Starting unified build..."
./gradlew clean build

# Create releases directory
mkdir -p releases
rm -rf releases/*

echo "Collecting artifacts..."

# Define Expected Paths based on standard Architectury/Stonecutter output
# Adjust these patterns if the build output location varies
cp versions/1.20.1-fabric/build/libs/hunger_overhaul_reborn-fabric-*1.20.1.jar releases/ 2>/dev/null
cp versions/1.20.1-forge/build/libs/hunger_overhaul_reborn-forge-*1.20.1.jar releases/ 2>/dev/null
cp versions/1.21.1-fabric/build/libs/hunger_overhaul_reborn-fabric-*1.21.1.jar releases/ 2>/dev/null
cp versions/1.21.1-forge/build/libs/hunger_overhaul_reborn-neoforge-*1.21.1.jar releases/ 2>/dev/null

echo "Build complete! Artifacts are in releases/"
ls -lh releases/
