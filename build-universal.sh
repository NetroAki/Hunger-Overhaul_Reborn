#!/bin/bash

# Universal JAR Build Script for Hunger Overhaul Reborn
# This script builds a single JAR that works with both Forge and Fabric,
# and both Minecraft 1.20.1 and 1.21.1

echo "🚀 Building Universal JAR for Hunger Overhaul Reborn"
echo "=================================================="
echo "This JAR will work with:"
echo "  ✅ Forge 1.20.1 and 1.21.1"
echo "  ✅ Fabric 1.20.1 and 1.21.1"
echo ""

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Build common module (which contains the universal JAR)
echo "🔨 Building universal JAR..."
./gradlew buildUniversalJar

# Copy JAR to distribution folder
echo "📁 Copying universal JAR to distribution folder..."
./gradlew copyUniversalJar

echo ""
echo "✅ Build complete!"
echo ""
echo "📋 Generated JAR:"
echo "   - hunger-overhaul-reborn-universal.jar"
echo ""
echo "🎯 This single JAR works with:"
echo "   - Forge 1.20.1 and 1.21.1"
echo "   - Fabric 1.20.1 and 1.21.1"
echo ""
echo "📂 JAR is located in: build/distributions/"
echo ""
echo "🔍 To test:"
echo "   1. Copy the JAR to your mods folder"
echo "   2. Launch Minecraft with either Forge or Fabric (1.20.1 or 1.21.1)"
echo "   3. Check the logs for version and loader detection messages"
echo ""
echo "🏆 This is the true universal solution!"
echo ""
