# Hunger Overhaul Reborn

A modern rewrite of the original Hunger Overhaul mod that makes hunger more challenging and realistic.

## 🎯 Overview

Hunger Overhaul Reborn enhances the Minecraft hunger system with:
- **Food Categorization**: Automatic food value adjustments based on type
- **Realistic Hunger**: Different foods provide varying nutrition levels
- **Configurable Mechanics**: Extensive customization options
- **Multi-Version Support**: Compatible with Minecraft 1.20.1 and 1.21.1

## 📦 Downloads

### Minecraft 1.20.1 (Full Features)
| Platform | Download | Status |
|----------|----------|--------|
| **Fabric** | [hunger_overhaul_reborn-fabric-0.2.1-beta+1.20.1.jar](build/libs/0.2.1-beta/fabric/) | ✅ Recommended |
| **Forge** | [hunger_overhaul_reborn-forge-0.2.1-beta+1.20.1.jar](build/libs/0.2.1-beta/forge/) | ✅ Recommended |

### Minecraft 1.21.1 (Limited Compatibility)
| Platform | Download | Status |
|----------|----------|--------|
| **Fabric** | [hunger_overhaul_reborn-fabric-0.2.1-beta+1.21.1.jar](versions/1.21.1-fabric/build/libs/) | ⚠️ Core features only |
| **NeoForge** | [hunger_overhaul_reborn-neoforge-0.2.1-beta+1.21.1.jar](versions/1.21.1-forge/build/libs/) | ⚠️ Core features only |

## 🔧 Features

### ✅ Available on All Versions
- **Food Auto-Discovery**: Automatically scans and categorizes all loaded food items
- **Configurable Food Values**: Adjust hunger/saturation for different food types
- **Eating Speed Control**: Modify how quickly players can eat
- **Stack Size Adjustments**: Change item stack sizes for realism
- **HUD Warnings**: Optional visual indicators for hunger/health status

### ✅ 1.20.1 Only (Full Features)
- **Custom Effects**: Well Fed, Hungry, and Low Health status effects
- **Advanced Mixins**: Enhanced game mechanics integration
- **Complete API Compatibility**: Full Minecraft 1.20.1 feature support

### ⚠️ 1.21.1 Limitations
- **Core Features Only**: Food modifications and basic mechanics work
- **No Custom Effects**: Advanced effects disabled for compatibility
- **Limited Mixins**: Some advanced integrations disabled
- **Graceful Degradation**: Mod loads safely but with reduced functionality

## ⚙️ Configuration

The mod includes extensive configuration options in `config/hunger_overhaul_reborn.json`:

```json
{
  "effect_warnings": false,
  "food": {
    "enable_food_category_changes": true,
    "meat_hunger_modifier": 1.0,
    "vegetable_hunger_modifier": 0.8
  },
  "health": {
    "addLowHealthNausea": true,
    "addLowHealthMiningSlowdown": true
  }
}
```

### Key Settings
- `effect_warnings`: Enable/disable HUD warning overlays (default: false)
- `food.*_modifier`: Adjust hunger values for different food categories
- `health.*`: Configure low health debuffs and effects

## 🏗️ Building from Source

### Prerequisites
- Java 17+
- Gradle 8.0+

### Build Commands

```bash
# Build all versions
./gradlew build

# Build specific version
./gradlew :1.20.1-fabric:build
./gradlew :1.20.1-forge:build
./gradlew :1.21.1-fabric:build
./gradlew :1.21.1-forge:build

# Test builds
./gradlew test-versions.sh
```

### Multi-Version Architecture
This mod uses **Stonecutter** for multi-version management:
- **1.20.1**: Full feature set with custom effects and mixins
- **1.21.1**: Graceful degradation with core features only
- **Version Detection**: Runtime checks prevent incompatible features

## 🧪 Testing

Run the included test script to verify all builds:

```bash
./test-versions.sh
```

This will:
- Build all 4 variants
- Verify JAR generation
- Check file integrity
- Report compatibility status

## 🔍 Compatibility

### Minecraft Versions
| Version | Status | Features |
|---------|--------|----------|
| **1.20.1** | ✅ Full Support | All features available |
| **1.21.1** | ⚠️ Limited | Core features, graceful degradation |

### Mod Loaders
- **Fabric**: Full support on both versions
- **Forge/NeoForge**: Full support on both versions

### Other Mods
- Compatible with most food-adding mods
- Uses auto-discovery for seamless integration
- No known conflicts with major modpacks

## 📋 Changelog

### v0.2.1-beta
- ✅ Multi-version support (1.20.1 + 1.21.1)
- ✅ Stonecutter build system integration
- ✅ Graceful 1.21.1 compatibility
- ✅ Effect warnings system (configurable)
- ✅ Food auto-discovery and categorization
- ✅ Configurable eating speeds and stack sizes
- ✅ Version-aware effect registration

## 🤝 Contributing

### Development Setup
1. Clone the repository
2. Run `./gradlew build` to verify setup
3. Use `./gradlew :1.20.1-fabric:runClient` for testing
4. Make changes and test across versions

### Code Style
- Follow standard Minecraft Forge/Fabric conventions
- Include version detection for 1.21.1+ compatibility
- Test changes on both supported versions

## 📞 Support

### Known Issues
- **1.21.1 Effects**: Custom effects are disabled for compatibility
- **Mixin Warnings**: Some mixins show warnings but don't affect functionality
- **Performance**: Auto-discovery may add slight loading time

### Troubleshooting
1. **Mod not loading**: Check Minecraft and mod loader versions
2. **Effects not working**: Ensure you're using 1.20.1 for full features
3. **Config not applying**: Restart Minecraft after config changes

### Reporting Issues
Please include:
- Minecraft version and mod loader
- Full mod list (if applicable)
- Steps to reproduce
- Log files from `.minecraft/logs/`

## 📜 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Note**: For the best experience, use Minecraft 1.20.1 with either Fabric or Forge. The 1.21.1 versions provide basic compatibility but lack advanced features.
