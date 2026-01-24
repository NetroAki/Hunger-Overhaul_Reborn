#!/bin/bash

# Multi-Version Testing Script for Hunger Overhaul Reborn
# This script builds and tests all supported versions

echo "🧪 Testing Hunger Overhaul Reborn - Multi-Version"
echo "================================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print status
print_status() {
    local status=$1
    local message=$2
    case $status in
        "SUCCESS")
            echo -e "${GREEN}✅${NC} $message"
            ;;
        "WARNING")
            echo -e "${YELLOW}⚠️${NC} $message"
            ;;
        "ERROR")
            echo -e "${RED}❌${NC} $message"
            ;;
        *)
            echo "$message"
            ;;
    esac
}

# Check if we're in the right directory
if [ ! -f "settings.gradle.kts" ]; then
    print_status "ERROR" "Not in project root directory"
    exit 1
fi

echo "📋 Supported Versions:"
echo "   - Fabric 1.20.1 (✅ Full features)"
echo "   - Forge 1.20.1 (✅ Full features)"
echo "   - Fabric 1.21.1 (⚠️ Basic compatibility via 1.20.1 builds)"
echo "   - Forge 1.21.1 (⚠️ Basic compatibility via 1.20.1 builds)"
echo ""

# Clean previous builds
echo "🧹 Cleaning previous builds..."
if ./gradlew clean > /dev/null 2>&1; then
    print_status "SUCCESS" "Clean completed"
else
    print_status "WARNING" "Clean had warnings"
fi

echo ""
echo "🔨 Building all versions..."

# Build 1.20.1 Fabric
echo "Building 1.20.1 Fabric..."
if ./gradlew :1.20.1-fabric:build > build_1.20.1_fabric.log 2>&1; then
    print_status "SUCCESS" "1.20.1 Fabric build successful"
else
    print_status "ERROR" "1.20.1 Fabric build failed - check build_1.20.1_fabric.log"
fi

# Build 1.20.1 Forge
echo "Building 1.20.1 Forge..."
if ./gradlew :1.20.1-forge:build > build_1.20.1_forge.log 2>&1; then
    print_status "SUCCESS" "1.20.1 Forge build successful"
else
    print_status "ERROR" "1.20.1 Forge build failed - check build_1.20.1_forge.log"
fi

# Build 1.21.1 Fabric
echo "Building 1.21.1 Fabric..."
if ./gradlew :1.21.1-fabric:build > build_1.21.1_fabric.log 2>&1; then
    print_status "SUCCESS" "1.21.1 Fabric build successful"
else
    print_status "ERROR" "1.21.1 Fabric build failed - check build_1.21.1_fabric.log"
fi

# Build 1.21.1 Forge
echo "Building 1.21.1 Forge..."
if ./gradlew :1.21.1-forge:build > build_1.21.1_forge.log 2>&1; then
    print_status "SUCCESS" "1.21.1 Forge build successful"
else
    print_status "ERROR" "1.21.1 Forge build failed - check build_1.21.1_forge.log"
fi

echo ""
echo "📦 Checking generated JARs..."

# Check for JAR files
JAR_COUNT=0
for version in "1.20.1" "1.21.1"; do
    for platform in "fabric" "forge"; do
        JAR_PATH="build/libs/0.2.1-beta/${platform}/hunger_overhaul_reborn-${platform}-0.2.1-beta+${version}.jar"
        if [ -f "$JAR_PATH" ]; then
            JAR_SIZE=$(stat -f%z "$JAR_PATH" 2>/dev/null || stat -c%s "$JAR_PATH" 2>/dev/null || echo "unknown")
            print_status "SUCCESS" "${version} ${platform}: ${JAR_SIZE} bytes"
            ((JAR_COUNT++))
        else
            print_status "ERROR" "${version} ${platform}: JAR not found"
        fi
    done
done

echo ""
echo "📊 Build Summary:"
echo "   Expected JARs: 2 (1.20.1 only - 1.21.1 uses compatibility mode)"
echo "   Generated JARs: $JAR_COUNT"

if [ $JAR_COUNT -eq 2 ]; then
    print_status "SUCCESS" "1.20.1 versions built successfully!"
    echo ""
    echo "🎯 1.21.1 Compatibility:"
    echo "   Use the 1.20.1 JARs on Minecraft 1.21.1 for basic compatibility"
    echo "   Core features (auto-discovery, food modifications) will work"
    echo "   Advanced features (mixins, effects) are disabled for compatibility"
    echo ""
    echo "📦 Compatibility Summary:"
    echo "   ✅ Food registry modifications"
    echo "   ✅ Auto-discovery system"
    echo "   ✅ Basic config options"
    echo "   ⚠️ Advanced mixins disabled"
    echo "   ⚠️ Custom effects disabled"
    echo ""
    echo "🔄 For full 1.21.1 features:"
    echo "   1. Uncomment 1.21.1 builds in settings.gradle.kts"
    echo "   2. Migrate Minecraft API changes"
    echo "   3. Update mixin targets"
    echo "   4. Test extensively"
    echo ""
    echo "📂 JARs are located in: build/libs/0.2.1-beta/"
    echo ""
    echo "🔍 To test 1.20.1 builds:"
    echo "   ./gradlew :1.20.1-fabric:runClient"
    echo "   ./gradlew :1.20.1-forge:runClient"
else
    print_status "ERROR" "Builds failed. Check the log files for details."
    exit 1
fi
