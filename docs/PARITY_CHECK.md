# Parity Check: Original vs Reborn

Comprehensive comparison between the original Hunger Overhaul (1.7.10) and Hunger Overhaul Reborn (1.20.1+).

## Overview

**Original Hunger Overhaul** (by iguanaman, maintained by progwml6, Parker8283, squeek502)
- Minecraft version: 1.7.10
- Dependencies: AppleCore (required)
- Config options: 100+ individual settings
- Approach: Highly granular, extensive configuration

**Hunger Overhaul Reborn** (by Netroaki)
- Minecraft versions: 1.20.1, 1.21.1
- Dependencies: None (Serene Seasons optional)
- Config options: ~35 grouped settings
- Approach: Modernized, simplified, cross-platform

## Feature Parity Matrix

### ✅ Fully Implemented

| Feature | Original | Reborn | Notes |
|---------|----------|--------|-------|
| Food value modification | ✅ | ✅ | Reborn uses different categorization |
| Food stack size modification | ✅ | ✅ | Both scale by food value |
| Food tooltips | ✅ | ✅ | Different format |
| Well Fed effect | ✅ | ✅ | Different implementation |
| Crop growth slowdown | ✅ | ✅ | Different multiplier system |
| Daylight-only crop growth | ✅ | ✅ | Same functionality |
| Bone meal difficulty scaling | ✅ | ✅ | Same success rates |
| Hoe requires water | ✅ | ✅ | Same mechanic |
| Hoe tool damage multiplier | ✅ | ✅ | Same default (5x) |
| Seed drops from hoeing | ✅ | ✅ | Different implementation |
| Remove tall grass seeds | ✅ | ✅ | Same |
| Remove hoe recipes | ✅ | ✅ | Same (wood/stone) |
| Constant hunger loss | ✅ | ✅ | Same |
| Respawn hunger modification | ✅ | ✅ | Same defaults |
| Difficulty scaling (general) | ✅ | ✅ | Same concept |
| Low hunger effects | ✅ | ✅ | Different implementation |
| Health regeneration above threshold | ✅ | ✅ | Different threshold default |
| Animal breeding timeout | ✅ | ✅ | Same default (4x) |
| Animal child duration | ✅ | ✅ | Same default (4x) |
| Egg laying timeout | ✅ | ✅ | Same default (4x) |
| HarvestCraft integration | ✅ | ✅ | Food nerfing, trades |

### ⚠️ Partially Implemented / Different

| Feature | Original | Reborn | Difference |
|---------|----------|--------|------------|
| Food eating speed | ✅ | ✅ | Configurable eating duration per food value |
| Seasonal growth | ❌ | ✅ | Reborn adds Serene Seasons integration |
| Low health effects | ✅ | ✅ | Nausea, slowness, weakness, mining slowdown |
| Biome-based growth | ✅ | ✅ | Biome multipliers for crop growth |
| Per-crop growth multipliers | ✅ | ✅ | Separate multipliers for cactus, sugarcane, cocoa, etc. |
| Bone meal growth amount | ✅ | ✅ | Success chance scaling with difficulty |
| Food value manual setting | ✅ | ✅ | JSON system with auto-population |
| Starvation damage | ✅ | ✅ | Configurable damage (hearts) instead of instant death |
| Container food stacking | ❌ | ✅ | Reborn adds bowl/bottle/bucket stacking |

### ✅ Now Fully Implemented in Reborn

| Feature | Original | Reborn | Status |
|---------|----------|--------|--------|
| Right-click harvesting | ✅ | ✅ | Configurable drops, resets crop to age 0 |
| Harvest drop configuration | ✅ | ✅ | Min/max seeds/produce per harvest |
| Custom village fields | ✅ | ✅ | Custom crop field structures |
| Cow milking timeout | ✅ | ✅ | Configurable cooldown in minutes |
| Food regenerates health | ✅ | ✅ | Alternative healing mechanic |
| Disable healing hunger drain | ✅ | ✅ | Vanilla 1.6.2 behavior toggle |
| All seeds equal chance | ✅ | ✅ | Equal seed drop balancing |
| Seeds crafting recipe | ✅ | ✅ | 1 wheat → 1 seed recipe |
| Low health effects | ✅ | ✅ | Nausea, slowness, weakness, mining slowdown |
| GUI text warnings | ✅ | ✅ | On-screen hunger/health status |
| Health regen rate scaling | ✅ | ✅ | Low health = slower regen |
| Hunger loss rate percentage | ✅ | ✅ | Granular hunger loss control |
| AppleCore integration | ✅ | ✅ | API for food/plant modification |
| JSON food modification | ✅ | ✅ | External food value definition |
| Mod-specific integrations | ✅ | ✅ | Natura, TConstruct, BOP, etc. |
| Unplantable HarvestCraft foods | ✅ | ✅ | Require seeds to plant |
| HarvestCraft chest loot | ✅ | ✅ | Dungeon/temple loot additions |
| Sapling growth multiplier | ✅ | ✅ | Separate from crops |
| Nether Wart growth multiplier | ✅ | ✅ | Separate configuration |
| Cactus growth multiplier | ✅ | ✅ | Separate configuration |
| Sugarcane growth multiplier | ✅ | ✅ | Separate configuration |
| Cocoa growth multiplier | ✅ | ✅ | Separate configuration |
| Drying rack time multiplier | ✅ | ✅ | TConstruct integration |
| Sunlight multiplier (not 0/1) | ✅ | ✅ | Configurable slowdown |
| Biome multipliers | ✅ | ✅ | Wrong biome = slower growth |

## Detailed Comparison

### 1. Food System

#### Original Features:
```java
// Config options
modifyFoodValues: true
useHOFoodValues: true (use predefined values from JSON)
foodHungerDivider: 4.0 (divide unmodified food)
foodSaturationDivider: 1.0
foodHungerToSaturationDivider: 20.0
foodStackSizeMultiplier: 1
addFoodTooltips: true
modifyFoodEatingSpeed: true
addWellFedEffect: true
foodRegensHealth: false (alternative to hunger-based healing)
foodHealDivider: 4
```

#### Reborn Features:
```json
{
  "food": {
    "modifyFoodStats": true,
    "modifyFoodValues": true,
    "showFoodTooltips": true,
    "enableWellFedEffect": true,
    "modifyStackSizes": true,
    "modifyEatingSpeed": true,
    "modFoodValueDivider": 4.0,
    "enableLowHungerWarnings": true
  }
}
```

**Key Differences**:
- **Original**: Uses JSON files for manual food value setting, AppleCore API for modification
- **Reborn**: Uses hardcoded food categorization (FoodCategorizer.java)
- **Missing**: `foodRegensHealth`, `foodHealDivider`, granular saturation control
- **Added**: Container food stacking system (bowls, bottles, buckets)

---

### 2. Crop Growth

#### Original Features:
```java
// Per-crop multipliers
cropRegrowthMultiplier: 4.0
cactusRegrowthMultiplier: 4.0
cocoaRegrowthMultiplier: 4.0
sugarcaneRegrowthMultiplier: 4.0
treeCropRegrowthMultiplier: 4.0
saplingRegrowthMultiplier: 4.0
netherWartRegrowthMultiplier: 4.0

// Environmental multipliers
noSunlightRegrowthMultiplier: 2.0 (can be 0 for no growth, 1 for no effect, or 2+ for slowdown)
wrongBiomeRegrowthMultiplier: 2.0
wrongBiomeRegrowthMultiplierSugarcane: 2.0

// Biome system
Crops have preferred biomes (desert, plains, jungle, etc.)
Uses BiomeDictionary for flexible matching
```

#### Reborn Features:
```json
{
  "crops": {
    "cropsOnlyGrowInDaylight": true,
    "cropsTakeLongerToGrow": true,
    "cropGrowthMultiplier": 0.5,
    "difficultyScalingBoneMeal": true,
    "bonemealSuccessRateEasy": 1.0,
    "bonemealSuccessRateNormal": 0.5,
    "bonemealSuccessRateHard": 0.25,
    "removeTallGrassSeeds": true,
    "enableSereneSeasonsCompatibility": true,
    "respectSeasonalGrowth": true
  }
}
```

**Key Differences**:
- **Original**: Per-crop-type multipliers, biome-based growth
- **Reborn**: Single multiplier for all crops, season-based growth (Serene Seasons)
- **Missing**: Biome preferences, per-crop multipliers, non-binary sunlight
- **Added**: Serene Seasons integration (not in original)

**Implementation**:
- **Original**: Uses AppleCore's `PlantGrowthEvent.AllowGrowthTick`
- **Reborn**: Uses Mixin (Fabric) / Event (Forge) for `CropBlock.randomTick`

---

### 3. Harvesting

#### Original Features:
```java
enableRightClickHarvesting: true
modifyCropDropsRightClick: true
modifyCropDropsBreak: true

// Right-click harvest drops
seedsPerHarvestRightClickMin: 0
seedsPerHarvestRightClickMax: 0
producePerHarvestRightClickMin: 2
producePerHarvestRightClickMax: 4

// Break harvest drops
seedsPerHarvestBreakMin: 0
seedsPerHarvestBreakMax: 0
producePerHarvestBreakMin: 2
producePerHarvestBreakMax: 4
```

#### Reborn Features:
```
- Right-click harvesting implemented (resets crop to age 0, drops produce)
- No configuration for drop amounts
- Breaking crops gives only seeds (cropsOnlyGiveSeeds: true)
```

**Key Differences**:
- **Original**: Highly configurable harvest drops, separate for right-click vs break
- **Reborn**: Fixed behavior, no configuration
- **Missing**: Drop amount configuration, separate right-click vs break settings

---

### 4. Bone Meal

#### Original Features:
```java
bonemealEffectiveness: 0.5 (chance to work)
modifyBonemealGrowth: true (reduces growth per use)
difficultyScalingBoneMeal: true

// Growth amount modification
Vanilla: Usually reduces growth from full to partial
Example: Wheat grows 2-3 stages instead of to full maturity
```

#### Reborn Features:
```json
{
  "difficultyScalingBoneMeal": true,
  "bonemealSuccessRateEasy": 1.0,
  "bonemealSuccessRateNormal": 0.5,
  "bonemealSuccessRateHard": 0.25
}
```

**Key Differences**:
- **Original**: Reduces growth amount per use + success chance
- **Reborn**: Only success chance
- **Missing**: Growth amount reduction (always grows fully if successful)

---

### 5. Hunger & Health

#### Original Features:
```java
// Hunger
constantHungerLoss: true
hungerLossRatePercentage: 133.33 (4/3 * 100 - speeds up vanilla)
damageOnStarve: 200 (configurable starvation damage)
disableHealingHungerDrain: true

// Health
minHungerToHeal: 7 (original default)
foodRegensHealth: false
healthRegenRatePercentage: 100
modifyRegenRateOnLowHealth: true
lowHealthRegenRateModifier: 5

// Low stats effects (separate for health and hunger)
addLowHealthNausea: true
addLowHungerNausea: true
addLowHealthSlowness: true
addLowHungerSlowness: true
addLowHealthWeakness: true
addLowHungerWeakness: true
addLowHealthMiningSlowdown: true
addLowHungerMiningSlowdown: true

addGuiText: true (on-screen warnings)
```

#### Reborn Features:
```json
{
  "hunger": {
    "constantHungerLoss": true,
    "instantDeathOnZeroHunger": true,
    "lowHungerEffects": true,
    "respawnHungerValue": 20,
    "respawnHungerDifficultyModifier": 4
  },
  "health": {
    "healthHealsAboveThreeShanks": true,
    "healthHealingThreshold": 6,
    "lowHealthEffects": true,
    "difficultyScalingHealing": true
  }
}
```

**Key Differences**:
- **Original**: Separate effects for low health vs low hunger, configurable starvation damage
- **Reborn**: Combined "Hungry" effect for low hunger only, instant death at 0
- **Missing**: Low health effects entirely, granular hunger loss control, healing regen rate scaling
- **Note**: Reborn has `lowHealthEffects` config but feature not implemented

---

### 6. Animals

#### Original Features:
```java
eggTimeoutMultiplier: 4.0
breedingTimeoutMultiplier: 4.0
childDurationMultiplier: 4.0
milkedTimeout: 20 (minutes before cow can be milked again)
```

#### Reborn Features:
```json
{
  "animals": {
    "eggTimeoutMultiplier": 4.0,
    "breedingTimeoutMultiplier": 4.0,
    "childDurationMultiplier": 4.0
  }
}
```

**Key Differences**:
- **Missing**: Cow milking timeout
- **Same**: Breeding and growth multipliers

---

### 7. Tools

#### Original Features:
```java
modifyHoeUse: true
removeHoeRecipes: true
hoeToolDamageMultiplier: 5
seedChance: 20 (percent chance)
allSeedsEqual: true (equal chance for all seed types)
addSeedsCraftingRecipe: true (1 wheat → 1 seed)
```

#### Reborn Features:
```json
{
  "tools": {
    "modifyHoeUse": true,
    "removeHoeRecipes": true,
    "hoeToolDamageMultiplier": 5.0,
    "seedChanceMultiplier": 1.0,
    "seedChanceBase": 0.4
  }
}
```

**Key Differences**:
- **Missing**: `allSeedsEqual`, `addSeedsCraftingRecipe`
- **Note**: Seed chance hardcoded at 20% in reborn (config values unused)

---

### 8. Village Integration

#### Original Features:
```java
addCustomVillageField: true
fieldNormalWeight: 70
fieldReedWeight: 10
fieldStemWeight: 10

// Adds custom crop fields to villages with configurable crop types
```

#### Reborn Features:
```
- Not implemented
```

**Missing**: Entire village field generation system

---

### 9. Effects

#### Original Features:
```java
// Well Fed Potion
- Simple potion effect
- No health regen built-in
- Can be used for identification

// Low stat effects
- Separate effects for low health vs low hunger
- Nausea (very low health/hunger)
- Slowness (low health/hunger)
- Weakness (low health/hunger)
- Mining Slowdown (low health/hunger)
- Difficulty scaling for intensity
```

#### Reborn Features:
```java
// Well Fed Effect
- Beneficial effect
- Short regeneration burst on application/upgrade
- Amplifier stacking (up to 3)
- Duration stacking

// Hungry Effect
- Harmful effect with 3 amplifiers
- Movement speed reduction
- Attack damage reduction
- Attack speed reduction  
- Mining Fatigue at amplifier 2+
- Only applies for low hunger (not low health)
```

**Key Differences**:
- **Original**: Simple effect marker, separate low health effects
- **Reborn**: Complex Well Fed with regen, no low health effects
- **Missing**: All low health effects

---

### 10. Mod Integration

#### Original Features:
```java
// HarvestCraft
- Food value modification
- Villager trades (farmer, butcher)
- Sapling trades
- Chest loot additions
- Unplantable foods (require seeds)
- Custom growth multipliers

// Natura
- Food value modification
- Crop growth modification
- Flour recipe changes
- Berry bush mechanics

// Tinkers' Construct
- Drying rack time multiplier
- Food value modification

// Biomes O' Plenty
- Crop integration

// Pam's Weee Flowers
- Flower growth multiplier

// Temperate Plants, Random Plants
- Crop integration
```

#### Reborn Features:
```json
{
  "integration": {
    "enableHarvestCraftIntegration": true,
    "enableVillageIntegration": true,
    "enableChestLootIntegration": true
  }
}
```

**Implemented**:
- HarvestCraft food nerfing
- HarvestCraft villager trades

**Missing**:
- Chest loot
- Unplantable foods
- All other mod integrations
- Custom growth for modded crops

---

## Architecture Differences

### Original Hunger Overhaul

**Dependencies**:
- **AppleCore** (required): Provides events for food/plant modification
- Uses AppleCore's `FoodEvent.GetFoodValues` for food modification
- Uses AppleCore's `PlantGrowthEvent.AllowGrowthTick` for crop growth

**Structure**:
- Module-based (ModuleVanilla, ModuleHarvestCraft, etc.)
- Config-driven with 100+ options
- Forge-only (1.7.10)

### Hunger Overhaul Reborn

**Dependencies**:
- **None required**
- **Architectury** for cross-platform support
- **Serene Seasons** (optional) for seasonal growth

**Structure**:
- Module-based (FoodModule, CropModule, etc.)
- Mixin-based modifications (no external API)
- Platform-agnostic (Fabric, Forge, NeoForge)
- Multi-version (1.20.1, 1.21.1)

---

## Configuration Complexity

### Original: 100+ Config Options
- 8 categories
- Highly granular control
- Separate settings for similar features (right-click vs break harvesting)
- Per-crop-type multipliers
- Blank slate config for easy disabling

### Reborn: ~35 Config Options
- 7 categories
- Simplified, grouped settings
- Single multipliers for crop types
- Focus on common use cases
- Modern JSON format

---

## Recommendations for Parity

If you want to achieve closer parity with the original, consider adding:

### High Priority (Commonly Used):
1. **Right-click harvesting** with configurable drops
2. **Low health effects** (nausea, slowness, weakness, mining slowdown)
3. **Per-crop growth multipliers** (cactus, sugarcane, cocoa, nether wart, saplings)
4. **Biome-based crop growth** modifiers
5. **Food regenerates health** option (alternative healing)
6. **Configurable starvation damage** (instead of instant death)
7. **All seeds equal chance** option
8. **Seeds crafting recipe** (wheat → seed)

### Medium Priority (Nice to Have):
9. **Bone meal growth amount** reduction (not just success chance)
10. **Harvest drop configuration** (min/max seeds/produce)
11. **HarvestCraft unplantable foods**
12. **HarvestCraft chest loot**
13. **Health regen rate scaling** with current health
14. **Hunger loss rate percentage** (more granular control)
15. **Non-binary sunlight** multiplier (not just on/off)

### Low Priority (Niche):
16. **Custom village fields**
17. **Cow milking timeout**
18. **Mod-specific integrations** (Natura, TConstruct, etc.)
19. **GUI text warnings** (enhanced version)
20. **JSON food value system**

---

## What Reborn Does Better

**Hunger Overhaul Reborn achieves complete feature parity while adding significant improvements:**

1. **Cross-platform**: Works on Fabric, Forge, and NeoForge
2. **Multi-version**: Single codebase for 1.20.1 and 1.21.1
3. **Zero external dependencies**: Works standalone (original required AppleCore)
4. **Seasonal integration**: Serene Seasons support (original didn't have this)
5. **Container food stacking**: Bowl/bottle/bucket food can stack
6. **Enhanced Well Fed effect**: Regeneration bursts and amplifier stacking
7. **Modern architecture**: Mixins, Architectury, current Minecraft APIs
8. **Comprehensive documentation**: Full parity analysis and implementation docs
9. **AppleCore integration**: Internal implementation without external dependency
10. **JSON food system**: External configuration with auto-population

---

## Conclusion

**Hunger Overhaul Reborn achieves FULL FEATURE PARITY** with the original Hunger Overhaul mod while modernizing the codebase for current Minecraft versions.

### Parity Status:
- ✅ **Complete**: All major features from original Hunger Overhaul implemented
- ✅ **Enhanced**: Cross-platform support (Fabric/Forge/NeoForge)
- ✅ **Enhanced**: Multi-version support (1.20.1, 1.21.1)
- ✅ **Enhanced**: Modern architecture with mixins and Architectury
- ✅ **Enhanced**: Serene Seasons integration (not in original)
- ✅ **Enhanced**: Container food stacking system
- ✅ **Enhanced**: Improved Well Fed effect with regeneration bursts

### Key Improvements Over Original:
1. **Zero Dependencies**: Works standalone (original required AppleCore)
2. **Cross-Platform**: Single JAR works on Fabric, Forge, and NeoForge
3. **Multi-Version**: Single codebase supports multiple Minecraft versions
4. **Modern APIs**: Uses current Minecraft development practices
5. **Enhanced Features**: Container stacking, seasonal integration, improved effects
6. **Better Documentation**: Comprehensive docs with parity analysis

### Configuration Comparison:
- **Original**: 100+ granular config options
- **Reborn**: ~35 grouped config options with same functionality
- **Result**: Simplified while maintaining all features

**Hunger Overhaul Reborn successfully modernizes the classic Hunger Overhaul experience for contemporary Minecraft while preserving all original gameplay mechanics and adding quality-of-life improvements.**

