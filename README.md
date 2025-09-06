# Hunger Overhaul Reborn

A comprehensive hunger and food mechanics overhaul mod for Minecraft 1.20.1 that makes hunger more challenging and realistic. This mod is a modern rewrite of the original Hunger Overhaul mod, designed for the current Minecraft ecosystem.

## Features

### Food System
- **Rebalanced food values**: Unprepared food gives half a hunger shank, cooked food gives one full shank, with larger meals providing more sustenance
- **Mod food nerfing**: Foods from other mods (like HarvestCraft) are reduced to 1/4 of their original value
- **Food tooltips**: Items show descriptions like "Light Meal", "Nourishing Meal", "Feast", etc.
- **Well Fed effect**: Eating larger meals provides a health regeneration boost
- **Stack size limits**: Larger meals can't be stacked as much (feasts are single items)
- **Eating speed**: Larger meals take longer to eat

### Crop System
- **Right-click harvesting**: Harvest crops without destroying them
- **Daylight-only growth**: Crops only grow during the day, not under torchlight
- **Slower growth**: Crops take much longer to grow, making resources scarcer
- **Seed-only drops**: Crops only give back the planted seeds, no extras
- **Bone meal scaling**: Bone meal effectiveness depends on difficulty setting

### Animal Modifications
- **Reduced egg laying**: Chickens lay eggs 4x slower
- **Longer breeding**: Animals have 4x longer breeding cooldowns
- **Slower maturation**: Baby animals take 4x longer to grow up

### Tool Changes
- **Modified hoe mechanics**: Using a hoe on grass without water nearby turns it to dirt and creates random seeds
- **Removed recipes**: Wood and stone hoes can't be crafted (requires iron)
- **Reduced durability**: Hoes have 1/5th of vanilla durability

### Hunger & Health System
- **Constant hunger loss**: Hunger decreases slowly over time, even when not doing anything
- **Difficulty-based respawn**: Respawn hunger depends on difficulty (Peaceful/Easy: 10, Normal: 8, Hard: 6)
- **Health healing**: Health regenerates when hunger is above 3 shanks (slower than vanilla)
- **Low health effects**: Slowness, mining slowdown, and weakness when health is low
- **Low hunger effects**: Similar effects when hunger is very low
- **Instant death**: Players die immediately when hunger reaches zero

### Mod Integration
- **HarvestCraft**: Village trading and chest loot integration
- **Natura**: Food value modifications
- **WeeeFlowers**: Food value modifications

## Dependencies

### Required
- **AppleCore**: Required for food mechanics
- **Architectury API**: For cross-platform compatibility

### Optional
- **HarvestCraft**: For enhanced food variety and integration
- **Natura**: For additional food items
- **WeeeFlowers**: For additional food items

## Installation

1. Install the required dependencies (AppleCore, Architectury API)
2. Download the appropriate version for your mod loader (Fabric or Forge)
3. Place the mod JAR in your mods folder
4. Start Minecraft

## Configuration

The mod uses AutoConfig for easy configuration. All settings can be modified in-game or through the config file:

- **Food settings**: Food value modifications, tooltips, Well Fed effect
- **Crop settings**: Growth rates, harvesting mechanics, bone meal effects
- **Animal settings**: Breeding times, egg laying rates, growth rates
- **Tool settings**: Hoe mechanics, recipe modifications
- **Hunger settings**: Loss rates, respawn values, effects
- **Health settings**: Healing rates, effect thresholds
- **Integration settings**: Mod compatibility options

## Modpack Policy

This mod is free to use in modpacks. Please credit the original authors and maintainers.

## Credits

- **Original mod**: Iguanaman
- **Maintainers**: ProgWML6, Alexbegt, Squeek502, Parker8283
- **Reborn version**: Netroaki

## License

This mod is licensed under the MIT License.

## Support

For issues, feature requests, or questions, please visit the GitHub repository or contact the maintainers.
