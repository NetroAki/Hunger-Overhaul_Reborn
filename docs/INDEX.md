# Documentation Index

Complete index of all documentation for Hunger Overhaul Reborn.

## Quick Start

New to the project? Start here:

1. **[README](README.md)** - Overview and quick links
2. **[Architecture Overview](architecture.md)** - Understanding the system design
3. **[Configuration Guide](configuration.md)** - Setting up the mod
4. **[Development Guide](development.md)** - Building and contributing

## Core Documentation

### Understanding the Mod

| Document | Description | Audience |
|----------|-------------|----------|
| [README](README.md) | Project overview, features, quick reference | Everyone |
| [Architecture Overview](architecture.md) | System design, patterns, core systems | Developers, Modpack Creators |
| [Module System](modules.md) | All gameplay modules and mechanics | Developers, Power Users |
| [Configuration Guide](configuration.md) | Complete configuration reference | Server Admins, Modpack Creators |

### Technical Details

| Document | Description | Audience |
|----------|-------------|----------|
| [Effects System](effects.md) | Well Fed and Hungry effects | Developers, Mod Integrators |
| [Mixin System](mixins.md) | Bytecode modifications and injection | Developers |
| [Platform Support](platform-support.md) | Fabric/Forge/NeoForge compatibility | Developers |
| [Event Handlers](event-handlers.md) | Event system and handlers | Developers |

### Development Resources

| Document | Description | Audience |
|----------|-------------|----------|
| [Development Guide](development.md) | Setup, building, contributing | Contributors |
| [API Reference](api-reference.md) | Public APIs for integration | Mod Developers |
| [Multi-Version Support](multi-version.md) | Version detection and compatibility | Developers |
| [Utilities](utilities.md) | Helper classes and functions | Developers |

### Data and Assets

| Document | Description | Audience |
|----------|-------------|----------|
| [Data Files](data-files.md) | Loot tables, recipes, tags | Modpack Creators, Developers |
| [Compatibility](compatibility.md) | Third-party mod integration | Everyone |
| [Parity Check](PARITY_CHECK.md) | Comparison with original Hunger Overhaul | Developers, Contributors |

## Documentation by Topic

### Gameplay Mechanics

**Food System**:
- [Module System - FoodModule](modules.md#1-foodmodule)
- [Effects - Well Fed](effects.md#well-fed-effect)
- [Configuration - Food Settings](configuration.md#food-settings)

**Crop Growth**:
- [Module System - CropModule](modules.md#2-cropmodule)
- [Module System - SereneSeasonsModule](modules.md#7-serene-seasons-module)
- [Mixins - AgeableBlockMixin](mixins.md#1-ageableblockmixin-fabric-only)
- [Configuration - Crop Settings](configuration.md#crop-settings)

**Hunger & Health**:
- [Module System - HungerModule](modules.md#5-hungermodule)
- [Module System - HealthModule](modules.md#6-healthmodule)
- [Effects - Hungry](effects.md#hungry-effect)
- [Configuration - Hunger Settings](configuration.md#hunger-settings)
- [Configuration - Health Settings](configuration.md#health-settings)

**Tools**:
- [Module System - ToolModule](modules.md#3-toolmodule)
- [Configuration - Tool Settings](configuration.md#tool-settings)
- [Data Files - Hoe Grass Loot](data-files.md#hoe-grass-loot-table)

**Animals**:
- [Module System - AnimalModule](modules.md#4-animalmodule)
- [Configuration - Animal Settings](configuration.md#animal-settings)

### Technical Implementation

**Architecture**:
- [Architecture - Design Patterns](architecture.md#architecture-patterns)
- [Architecture - Core Systems](architecture.md#core-systems)
- [Architecture - Initialization Flow](architecture.md#1-initialization-flow)

**Cross-Platform**:
- [Platform Support - Architectury Framework](platform-support.md#architectury-framework)
- [Platform Support - Platform Entry Points](platform-support.md#platform-entry-points)
- [Platform Support - Platform-Specific Implementations](platform-support.md#platform-specific-implementations)

**Code Modification**:
- [Mixins - Core Mixins](mixins.md#core-mixins)
- [Mixins - Configuration Files](mixins.md#mixin-configuration-files)
- [Mixins - Best Practices](mixins.md#mixin-best-practices)

**Events**:
- [Event Handlers - FoodEventHandler](event-handlers.md#foodeventhandler)
- [Event Handlers - PlayerEventHandler](event-handlers.md#playereventhandler)
- [Event Handlers - WorldEventHandler](event-handlers.md#worldeventhandler)

### Integration & Compatibility

**Mod Integration**:
- [Compatibility - Serene Seasons](compatibility.md#serene-seasons)
- [Compatibility - HarvestCraft](compatibility.md#harvestcraft)
- [API Reference - Extension Points](api-reference.md#extension-points)

**Multi-Version**:
- [Multi-Version - Stonecutter Workflow](multi-version.md#stonecutter-workflow)
- [Multi-Version - Version Detection](multi-version.md#version-detection)
- [Platform Support - NeoForge Migration](platform-support.md#migration-path-forge--neoforge)

## Documentation by Audience

### For Players

Start with these documents:
1. [README](README.md) - What the mod does
2. [Configuration Guide](configuration.md) - How to customize
3. [Compatibility](compatibility.md) - Works with other mods

### For Server Admins

Essential reading:
1. [Configuration Guide](configuration.md) - All settings explained
2. [Module System](modules.md) - Understanding features
3. [Compatibility](compatibility.md) - Mod interactions
4. [Data Files](data-files.md) - Customizing loot/recipes

### For Modpack Creators

Recommended reading:
1. [Configuration Guide](configuration.md) - Balancing settings
2. [Module System](modules.md) - Feature deep-dive
3. [Compatibility](compatibility.md) - Integration tips
4. [Data Files](data-files.md) - Custom content
5. [API Reference](api-reference.md) - Integration possibilities

### For Mod Developers (Integration)

Focus on these:
1. [API Reference](api-reference.md) - Public APIs
2. [Architecture Overview](architecture.md) - System design
3. [Effects System](effects.md) - Custom effects
4. [Compatibility](compatibility.md) - Integration patterns

### For Contributors

Must-read documentation:
1. [Development Guide](development.md) - Setup and workflow
2. [Architecture Overview](architecture.md) - System design
3. [Module System](modules.md) - Feature modules
4. [Mixin System](mixins.md) - Code modifications
5. [Platform Support](platform-support.md) - Cross-platform dev
6. [Event Handlers](event-handlers.md) - Event system

## Common Tasks

### Configuration Tasks

| Task | Documentation |
|------|---------------|
| Change food values | [Configuration - Food Settings](configuration.md#food-settings) |
| Adjust crop growth speed | [Configuration - Crop Settings](configuration.md#crop-settings) |
| Modify hunger depletion | [Configuration - Hunger Settings](configuration.md#hunger-settings) |
| Configure seasonal growth | [Configuration - Crop Settings](configuration.md#crop-settings) |
| Set difficulty scaling | [Configuration - Multiple sections](configuration.md) |

### Development Tasks

| Task | Documentation |
|------|---------------|
| Set up dev environment | [Development - Project Setup](development.md#project-setup) |
| Build the mod | [Development - Building](development.md#building) |
| Add new food category | [Development - Common Tasks](development.md#adding-new-food-category) |
| Create new module | [Development - Common Tasks](development.md#adding-new-module) |
| Add platform-specific feature | [Development - Common Tasks](development.md#adding-platform-specific-feature) |
| Write mixin | [Mixins - Core Mixins](mixins.md#core-mixins) |

### Troubleshooting

| Issue | Documentation |
|-------|---------------|
| Crops not growing | [Configuration - Troubleshooting](configuration.md#troubleshooting) |
| Food modifications not working | [Configuration - Troubleshooting](configuration.md#troubleshooting) |
| Build fails | [Development - Troubleshooting](development.md#troubleshooting) |
| Mixin not applying | [Mixins - Troubleshooting](mixins.md#troubleshooting) |
| Platform-specific crash | [Platform Support - Debugging](platform-support.md#debugging-platform-issues) |

## Code Reference

### Key Classes

| Class | Purpose | Documentation |
|-------|---------|---------------|
| `HOReborn` | Main mod class | [Architecture - Initialization](architecture.md#1-initialization-flow) |
| `HungerOverhaulConfig` | Configuration system | [Configuration Guide](configuration.md) |
| `FoodModule` | Food mechanics | [Module System - FoodModule](modules.md#1-foodmodule) |
| `CropModule` | Crop growth | [Module System - CropModule](modules.md#2-cropmodule) |
| `WellFedEffect` | Beneficial effect | [Effects - Well Fed](effects.md#well-fed-effect) |
| `HungryEffect` | Harmful effect | [Effects - Hungry](effects.md#hungry-effect) |
| `FoodCategorizer` | Food classification | [Utilities - FoodCategorizer](utilities.md#foodcategorizer) |
| `SereneSeasonsAPI` | Season integration | [Utilities - SereneSeasonsAPI](utilities.md#sereneseasons-api) |

### Important Files

| File | Purpose | Documentation |
|------|---------|---------------|
| `hunger_overhaul_reborn.json` | Main config | [Configuration Guide](configuration.md) |
| `hunger_overhaul_reborn.properties` | Container config | [Configuration Guide](configuration.md#container-configuration-reference) |
| `hunger_overhaul_reborn-fabric.mixins.json` | Fabric mixins | [Mixins - Configuration](mixins.md#fabric-configuration) |
| `hunger_overhaul_reborn-forge.mixins.json` | Forge mixins | [Mixins - Configuration](mixins.md#forge-configuration) |
| `build.gradle.kts` | Build configuration | [Development - Build Configuration](development.md#build-configuration) |

## External Resources

### Minecraft Modding

- [Architectury Documentation](https://docs.architectury.dev/)
- [Fabric Wiki](https://fabricmc.net/wiki/)
- [Forge Documentation](https://docs.minecraftforge.net/)
- [NeoForge Documentation](https://docs.neoforged.net/)
- [Mixin Documentation](https://github.com/SpongePowered/Mixin/wiki)

### Community

- [Architectury Discord](https://discord.gg/architectury)
- [Fabric Discord](https://discord.gg/v6v4pMv)
- [Forge Discord](https://discord.gg/forge)

## Contributing to Documentation

Found an error or want to improve the docs?

1. Documentation is in `docs/` directory
2. Written in Markdown format
3. Follow existing style and structure
4. Submit PR with changes
5. See [Development Guide](development.md#contributing) for PR process

### Documentation Standards

- **Clear structure** with headings and sections
- **Code examples** for technical concepts
- **Cross-references** to related docs
- **Audience awareness** (mark beginner/advanced)
- **Keep updated** with code changes

## Version Information

- **Last Updated**: 2025-01-13
- **Mod Version**: 0.2.1-beta
- **Minecraft Versions**: 1.20.1, 1.21.1
- **Platforms**: Fabric, Forge, NeoForge

## License

Documentation licensed under the same terms as the mod. See [LICENSE.txt](../LICENSE.txt).

