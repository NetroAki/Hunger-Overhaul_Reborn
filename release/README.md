# Hunger Overhaul Reborn v0.2.1-beta Release

## 📦 Available Versions

### ✅ Built and Ready
- **Fabric 1.20.1**: `hunger_overhaul_reborn-fabric-0.2.1-beta+1.20.1.jar`
- **Forge 1.20.1**: `hunger_overhaul_reborn-forge-0.2.1-beta+1.20.1.jar`

### ⚠️ Basic Compatibility (Use 1.20.1 JARs)
- **Fabric 1.21.1**: Use 1.20.1 JAR with compatibility mode
- **Forge 1.21.1**: Use 1.20.1 JAR with compatibility mode

#### 1.21.1 Compatibility Notes
- Download and use the **1.20.1 JARs** on Minecraft 1.21.1
- Core features work (food modifications, auto-discovery)
- Advanced features disabled (mixins, custom effects)
- See `docs/1.21.1_COMPATIBILITY.md` for details

## 🚀 Installation

1. Download the appropriate JAR file for your mod loader and Minecraft version
2. Place the JAR file in your `.minecraft/mods/` folder
3. Launch Minecraft
4. Enjoy the enhanced food mechanics!

## ✨ Features

- **Auto-Categorization**: Automatically detects and categorizes food items from any mod
- **Realistic Food Values**: Balanced hunger/saturation values across all food types
- **Enhanced Eating Mechanics**: More dramatic eating speeds and stack sizes
- **Survival Challenges**: Constant hunger drain, instant death on zero hunger
- **HUD Warnings**: Configurable warnings for low hunger/health (default: off)
- **Cross-Platform**: Full feature parity between Fabric and Forge

## 🔧 Configuration

The mod includes comprehensive configuration options. Key settings:
- `effect_warnings = false` - Toggle HUD warnings for hunger/health/status effects
- `addGuiText = true` - Master toggle for all GUI text
- Various difficulty and balance options

## 📋 Changelog v0.2.1-beta

- ✅ Full auto-categorization system for unsupported mods
- ✅ Assembly Required mod support
- ✅ Fixed eating speed parity between Fabric/Forge
- ✅ Consolidated HUD warnings under single config option
- ✅ Removed redundant well-fed timer (Minecraft handles this natively)
- ✅ Balanced food values across all integration modules
- ✅ Enhanced tooltips and food information display

## 🐛 Known Issues

- 1.21.1 full builds require API migration (use compatibility mode instead)
- On 1.21.1: Advanced mixins and custom effects are disabled for compatibility
- Some mixin warnings in logs (non-critical)

## 🤝 Support

For issues, questions, or contributions, please visit the project repository.
