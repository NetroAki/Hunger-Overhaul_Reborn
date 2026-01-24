# Hunger Overhaul Reborn - Documentation

Welcome to the comprehensive documentation for Hunger Overhaul Reborn! This documentation covers the entire mod architecture, systems, and implementation details.

## Overview

Hunger Overhaul Reborn is a modern rewrite of the original Hunger Overhaul mod for Minecraft 1.20.1+ that makes hunger more challenging and realistic. The mod is built using Architectury, allowing it to run on both Fabric and Forge (including NeoForge for 1.21.1+).

## What This Mod Does

Hunger Overhaul Reborn dramatically changes how food and farming work in Minecraft:

- **Food System Overhaul**: Reduces food values, modifies stack sizes based on food value, adds food categories, and introduces new food-related effects
- **Crop Growth Changes**: Makes crops grow slower based on difficulty, adds seasonal growth support (Serene Seasons integration), requires daylight for growth, and modifies bone meal effectiveness
- **Tool Changes**: Modifies hoe mechanics to require water nearby for tilling, adds seed drops from hoeing grass, increases tool damage, and removes/modifies vanilla hoe recipes
- **Hunger & Health**: Implements constant hunger loss, difficulty-scaled respawn hunger, low hunger effects (slowness, weakness, mining fatigue), and modified health regeneration
- **Animal Changes**: Increases breeding cooldowns, extends child growth duration, and modifies egg-laying timeouts
- **Effects System**: Introduces "Well Fed" (beneficial) and "Hungry" (harmful) status effects

## Documentation Structure

### Core Documentation

1. **[Architecture Overview](architecture.md)** - High-level system architecture and design patterns
2. **[Module System](modules.md)** - Detailed breakdown of all gameplay modules
3. **[Configuration Guide](configuration.md)** - Complete configuration reference
4. **[Event Handlers](event-handlers.md)** - Event system and handler implementations
5. **[Effects System](effects.md)** - Status effects and their mechanics

### Technical Documentation

6. **[Mixin System](mixins.md)** - Mixin implementations and bytecode modifications
7. **[Platform Support](platform-support.md)** - Fabric/Forge/NeoForge compatibility layer
8. **[Multi-Version Support](multi-version.md)** - Version detection and compatibility
9. **[Compatibility](compatibility.md)** - Third-party mod integrations
10. **[Utilities](utilities.md)** - Helper classes and utility functions

### Developer Resources

11. **[Building & Development](development.md)** - Setup, building, and contributing
12. **[API Reference](api-reference.md)** - Public APIs for integration
13. **[Data Files](data-files.md)** - Loot tables, recipes, and data generation

## Quick Links

- **Main Class**: `org.Netroaki.Main.HOReborn`
- **Mod ID**: `hunger_overhaul_reborn`
- **Current Version**: 0.2.1-beta
- **Supported Minecraft Versions**: 1.20.1, 1.21.1
- **Supported Loaders**: Fabric, Forge, NeoForge

## Key Features at a Glance

### Food Categories
- **Raw Food** (1 nutrition) - Raw crops and meat
- **Cooked Food** (2 nutrition) - Cooked items
- **Light Meal** (4 nutrition) - Bread, cookies, simple foods
- **Average Meal** (6 nutrition) - Stews and complex dishes
- **Large Meal** (8 nutrition) - High-value foods
- **Feast** (10+ nutrition) - Most valuable foods

### Stack Sizes by Food Value
- Feast (14+): 1 item per stack
- Large Meal (10-13): 4 items per stack
- Nourishing Meal (7-9): 8 items per stack
- Light Meal (4-6): 16 items per stack
- Snacks (1-3): 32 items per stack

### Difficulty Scaling
- **Peaceful/Easy**: 75% hunger speed, 100% bone meal success
- **Normal**: 100% hunger speed, 50% bone meal success, 50% crop growth multiplier
- **Hard**: 150% hunger speed, 25% bone meal success, 34% crop growth multiplier

## Integration Support

- **Serene Seasons**: Full seasonal crop growth support with fertility checks
- **HarvestCraft**: Food value adjustments and villager trading integration
- **Modded Foods**: Automatic detection and nerfing of modded food items

## Technical Highlights

- **Cross-platform**: Single codebase for Fabric, Forge, and NeoForge
- **Multi-version**: Supports both 1.20.1 and 1.21.1 with version detection
- **Mixin-based**: Minimal invasive changes using Mixin framework
- **JSON Configuration**: Easy-to-edit configuration files
- **Event-driven**: Uses Architectury events for cross-platform compatibility

## Getting Started

For users:
1. Install the mod in your mods folder
2. Launch the game
3. Edit `config/hunger_overhaul_reborn.json` to customize settings
4. Restart the game for changes to take effect

For developers:
1. See [Building & Development](development.md)
2. Review [Architecture Overview](architecture.md)
3. Check [API Reference](api-reference.md) for integration points

## Relationship to Original

This is a **modernized rewrite** of the original Hunger Overhaul mod. See [Parity Check](PARITY_CHECK.md) for a detailed comparison of features between the original and reborn versions.

**Key differences**:
- Cross-platform (Fabric/Forge/NeoForge vs Forge-only)
- Multi-version support (1.20.1, 1.21.1)
- Simplified configuration (~35 vs 100+ options)
- No external dependencies (vs required AppleCore)
- Added Serene Seasons integration
- Streamlined feature set focused on core mechanics

## Support & Contribution

- **Issues**: Report bugs and request features on the GitHub repository
- **Development**: See [Building & Development](development.md) for contribution guidelines
- **API**: Check [API Reference](api-reference.md) for integration with other mods
- **Original Comparison**: See [Parity Check](PARITY_CHECK.md) for feature comparison

