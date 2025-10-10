#!/bin/bash

# Multi-Version Build Script for Hunger Overhaul Reborn
# This script builds JARs that work with both Minecraft 1.20.1 and 1.21.1

echo "🚀 Building Multi-Version JARs for Hunger Overhaul Reborn"
echo "=================================================="

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Build all modules
echo "🔨 Building all modules..."
./gradlew build

# Create multi-version JARs
echo "📦 Creating multi-version JARs..."
./gradlew buildMultiVersionJars

# Copy JARs to distribution folder
echo "📁 Copying JARs to distribution folder..."
./gradlew copyMultiVersionJars

echo ""
echo "✅ Build complete!"
echo ""
echo "📋 Generated JARs:"
echo "   - hunger-overhaul-reborn-common-multiversion.jar"
echo "   - hunger-overhaul-reborn-fabric-multiversion.jar"
echo "   - hunger-overhaul-reborn-forge-multiversion.jar"
echo ""
echo "🎯 These JARs work with both Minecraft 1.20.1 and 1.21.1!"
echo "   The mod will automatically detect the Minecraft version at runtime."
echo ""
echo "📂 JARs are located in: build/distributions/"
echo ""
echo "🔍 To test:"
echo "   1. Copy the appropriate JAR to your mods folder"
echo "   2. Launch Minecraft (1.20.1 or 1.21.1)"
echo "   3. Check the logs for version detection messages"
echo ""
