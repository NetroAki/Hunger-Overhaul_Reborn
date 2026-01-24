# Module System

This document details all gameplay modules in Hunger Overhaul Reborn and their mechanics.

## Module Architecture

Each module is a self-contained system responsible for a specific gameplay mechanic. Modules are initialized during mod startup and register their own event handlers and configurations.

## Core Modules

### 1. FoodModule

**Purpose**: Manages all food-related modifications and effects.

**File**: `org.Netroaki.Main.modules.FoodModule`

**Features**:
- Food value modification and categorization
- Stack size adjustments based on nutrition
- Food tooltips in GUI
- Low hunger/health warnings
- Well Fed effect application

**Configuration Options**:
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

**Food Categories**:
| Category | Nutrition | Stack Size | Examples |
|----------|-----------|------------|----------|
| Raw Food | 1 | 32 | Carrot, raw beef, wheat |
| Cooked Food | 2 | 32 | Cooked beef, baked potato |
| Light Meal | 4 | 16 | Bread, cookies, stews |
| Average Meal | 6 | 8 | Rabbit stew, suspicious stew |
| Large Meal | 8 | 4 | Complex dishes |
| Feast | 10+ | 1 | High-value foods |

**Well Fed Effect Duration**:
- Feast (14+): 480 ticks (24 seconds)
- Large Meal (10-13): 240 ticks (12 seconds)
- Meal (7-9): 120 ticks (6 seconds)
- Light Meal (4-6): 40 ticks (2 seconds)
- Snacks (1-3): No effect

**Eating Duration Scaling**:
- Feast (14+): 64 ticks (3.2 seconds)
- Large Meal (10-13): 48 ticks (2.4 seconds)
- Meal (7-9): 32 ticks (1.6 seconds)
- Light Meal (4-6): 24 ticks (1.2 seconds)
- Snacks (1-3): 16 ticks (0.8 seconds)

**Methods**:
- `init()`: Registers tooltips and GUI warnings
- `getFoodTooltip(ItemStack)`: Returns food category tooltip
- `renderLowHungerWarning()`: Renders warning text on HUD

---

### 2. CropModule

**Purpose**: Controls crop growth mechanics and bone meal behavior.

**File**: `org.Netroaki.Main.modules.CropModule`

**Features**:
- Daylight-only growth requirement
- Growth speed reduction
- Difficulty-scaled bone meal effectiveness
- Serene Seasons integration
- Tall grass seed removal

**Configuration Options**:
```json
{
  "crops": {
    "cropsOnlyGrowInDaylight": true,
    "cropsTakeLongerToGrow": true,
    "cropGrowthMultiplier": 0.5,
    "cropsOnlyGiveSeeds": true,
    "difficultyScalingBoneMeal": true,
    "bonemealSuccessRateEasy": 1.0,
    "bonemealSuccessRateNormal": 0.5,
    "bonemealSuccessRateHard": 0.25,
    "removeTallGrassSeeds": true,
    "disableCropGrowthModifications": false,
    "autoDetectGrowthMods": true,
    "enableSereneSeasonsCompatibility": true,
    "respectSeasonalGrowth": true,
    "winterGrowthMultiplier": 0.3,
    "springGrowthMultiplier": 1.0,
    "summerGrowthMultiplier": 1.2,
    "autumnGrowthMultiplier": 0.6
  }
}
```

**Growth Mechanics**:

1. **Daylight Check** (if enabled):
   - Sky light level must be ≥ 9
   - Must be daytime
   - Cancels growth if check fails

2. **Season Check** (if Serene Seasons present):
   - Checks crop fertility in current season
   - Uses Serene Seasons' built-in fertility system
   - Cancels growth for infertile crops

3. **Difficulty Multiplier**:
   - Peaceful: 1.0x (no reduction)
   - Easy: 0.67x (33% reduction)
   - Normal: 0.5x (50% reduction)
   - Hard: 0.34x (66% reduction)

4. **Random Check**:
   - Base multiplier: 0.5 (50% chance)
   - Adjusted by difficulty
   - Adjusted by season (if enabled)
   - Final probability check

**Bone Meal Behavior**:
```java
// Success rates by difficulty
Peaceful/Easy: 100% (always works)
Normal: 50% (half the time)
Hard: 25% (quarter of the time)

// On failure:
- Consumes bone meal
- Plays sound and particles
- Does NOT grow crop
```

**Methods**:
- `init()`: Sets up crop growth handlers
- `onBoneMealUse()`: Handles bone meal interaction
- `onCropRandomTick()`: Additional crop tick logic

---

### 3. ToolModule

**Purpose**: Modifies tool behavior, particularly hoes.

**File**: `org.Netroaki.Main.modules.ToolModule`

**Features**:
- Hoe requires water nearby for tilling
- Seed drops from hoeing grass
- Increased hoe durability loss
- Modified/removed hoe recipes

**Configuration Options**:
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

**Hoe Mechanics**:

1. **Water Requirement**:
   - Checks 4-block radius for water
   - If no water found:
     - Converts grass block to dirt (not farmland)
     - 20% chance to drop seeds
     - Increases durability damage (4 points)
     - Does NOT create farmland

2. **Seed Drops**:
   - Uses loot table: `hunger_overhaul_reborn:gameplay/hoe_grass`
   - 20% base chance (fixed, not difficulty-scaled)
   - Drops from seed tags (forge:seeds, c:seeds)
   - Wheat seeds have higher weight (10 vs 1)

**Loot Table Structure**:
```json
{
  "rolls": 1,
  "entries": [
    {"type": "item", "name": "wheat_seeds", "weight": 10},
    {"type": "tag", "name": "forge:seeds", "weight": 1},
    {"type": "tag", "name": "c:seeds", "weight": 1}
  ]
}
```

**Methods**:
- `init()`: Registers hoe event handlers
- `onHoeUse()`: Handles hoe right-click on blocks
- `isWaterNearby()`: Checks for water in 4-block radius

---

### 4. AnimalModule

**Purpose**: Controls animal breeding and growth mechanics.

**File**: `org.Netroaki.Main.modules.AnimalModule`

**Features**:
- Extended breeding cooldowns
- Slower child growth
- Modified egg-laying rates

**Configuration Options**:
```json
{
  "animals": {
    "eggTimeoutMultiplier": 4.0,
    "breedingTimeoutMultiplier": 4.0,
    "childDurationMultiplier": 4.0
  }
}
```

**Mechanics**:

1. **Breeding Cooldown**:
   - Base: 6000 ticks (5 minutes)
   - Multiplied by `breedingTimeoutMultiplier`
   - Default: 24000 ticks (20 minutes)

2. **Child Growth**:
   - Base: -24000 ticks (20 minutes as baby)
   - Multiplied by `childDurationMultiplier`
   - Default: -96000 ticks (80 minutes as baby)

3. **Egg Laying**:
   - Multiplied by `eggTimeoutMultiplier`
   - Default: 4x longer between eggs

**Implementation Notes**:
- Forge: Uses `BabyEntitySpawnEvent` to modify ages directly
- Fabric: Requires entity tracking and age modification

**Methods**:
- `init()`: Registers animal event handlers
- `onServerStarting()`: Logs enabled features

---

### 5. HungerModule

**Purpose**: Manages hunger mechanics and effects.

**File**: `org.Netroaki.Main.modules.HungerModule`

**Features**:
- Constant hunger loss over time
- Instant death at zero hunger
- Low hunger status effects
- Difficulty-scaled hunger rates
- Modified respawn hunger

**Configuration Options**:
```json
{
  "hunger": {
    "constantHungerLoss": true,
    "constantHungerLossRate": 0.001,
    "modifyRespawnHunger": true,
    "respawnHungerValue": 20,
    "respawnHungerDifficultyModifier": 4,
    "difficultyScalingRespawnHunger": true,
    "difficultyScalingHungerLoss": true,
    "harvestCraftHungerLossIncrease": true,
    "harvestCraftHungerLossMultiplier": 1.33,
    "instantDeathOnZeroHunger": true,
    "lowHungerEffects": true,
    "lowHungerSlownessThreshold": 6,
    "lowHungerWeaknessThreshold": 4
  }
}
```

**Constant Hunger Loss**:
```java
Base Rate: 24000 ticks (20 minutes per hunger point)
Difficulty Multipliers:
  - Peaceful/Easy: 0.75x (slower)
  - Normal: 1.0x (base rate)
  - Hard: 1.5x (faster)
  
With HarvestCraft: Additional 1.33x multiplier

Calculation:
interval = BASE_RATE / (difficultySpeed * harvestCraftSpeed)
Every interval ticks: hunger -= 1
```

**Low Hunger Effects**:
| Hunger Level | Effect | Amplifier |
|--------------|--------|-----------|
| 6 or below | Hungry I | 0 (Slowness, Weakness) |
| 4 or below | Hungry II | 1 (More Slowness, Weakness) |
| 2 or below | Hungry III | 2 (Mining Fatigue added) |

**Respawn Hunger**:
```java
Base: 20 (full hunger bar)
Difficulty Modifier: -4 per level

Peaceful/Easy: 20 hunger
Normal: 16 hunger (20 - 4)
Hard: 12 hunger (20 - 8)
```

**Instant Death**:
- When hunger reaches 0
- Bypasses normal starvation damage
- Uses `Float.MAX_VALUE` damage
- Only if `instantDeathOnZeroHunger` enabled

**Methods**:
- `init()`: Logs enabled features
- Actual logic in `PlayerEventHandler`

---

### 6. HealthModule

**Purpose**: Controls health regeneration and health-based effects.

**File**: `org.Netroaki.Main.modules.HealthModule`

**Features**:
- Health regeneration above hunger threshold
- Difficulty-scaled healing speed
- Low health effects
- Integration with hunger system

**Configuration Options**:
```json
{
  "health": {
    "healthHealsAboveThreeShanks": true,
    "healthHealingThreshold": 6,
    "lowHealthEffects": true,
    "lowHealthEffectsThreshold": 6,
    "difficultyScalingHealing": true
  }
}
```

**Healing Mechanics**:

1. **Healing Requirement**:
   - Hunger must be ≥ `healthHealingThreshold` (default: 6)
   - Player health must be below max

2. **Healing Rate**:
   ```java
   Base Interval: 120 ticks (6 seconds per heart)
   Difficulty Multipliers:
     - Peaceful/Easy: 0.75x (faster healing)
     - Normal: 1.0x (base rate)
     - Hard: 1.5x (slower healing)
   
   Calculation:
   healInterval = BASE_INTERVAL / healSpeed
   Every healInterval ticks: health += 1.0F (half heart)
   ```

3. **Well Fed Bonus Healing**:
   - When Well Fed effect is applied or upgraded
   - 1-second burst (20 ticks)
   - Heals 1 heart at ticks 20 and 10
   - Total: 1 heart over 1 second

**Methods**:
- `init()`: Logs enabled features
- Actual logic in `PlayerEventHandler`

---

### 7. SereneSeasonsModule

**Purpose**: Integrates with Serene Seasons mod for seasonal crop growth.

**File**: `org.Netroaki.Main.modules.SereneSeasonsModule`

**Features**:
- Seasonal fertility checks
- Season-based growth multipliers
- Dynamic season detection
- Graceful degradation without Serene Seasons

**Configuration Options**:
```json
{
  "crops": {
    "enableSereneSeasonsCompatibility": true,
    "respectSeasonalGrowth": true,
    "winterGrowthMultiplier": 0.3,
    "springGrowthMultiplier": 1.0,
    "summerGrowthMultiplier": 1.2,
    "autumnGrowthMultiplier": 0.6
  }
}
```

**Season Detection**:
- Uses reflection to avoid compile-time dependency
- Calls `sereneseasons.api.season.SeasonHelper.getSeasonState()`
- Extracts season and sub-season information
- Falls back to no-op if Serene Seasons not present

**Fertility System**:
```java
// Uses Serene Seasons' built-in fertility
ModFertility.isCropFertile(blockKey, level, pos)

// Crops have different fertile seasons:
Winter: Wheat, Carrot, Potato
Spring: Most crops
Summer: Melons, Pumpkins
Autumn: Root vegetables

// Infertile crops do NOT grow at all
```

**Growth Multiplier Stacking**:
```java
// Base multiplier from config
base = cropGrowthMultiplier (0.5)

// Difficulty multiplier
difficulty = getDifficultyMultiplier(level)
  Peaceful: 1.0
  Easy: 0.67
  Normal: 0.5
  Hard: 0.34

// Final multiplier
final = base * difficulty

// Example (Normal difficulty):
0.5 * 0.5 = 0.25 (25% growth chance)
```

**Methods**:
- `init()`: Registers season event handlers
- `onCropRandomTick()`: Applies seasonal logic to growth
- `isSereneSeasonsLoaded()`: Checks if mod is present
- `setCompatibilityEnabled()`: Enable/disable integration

## Module Initialization Order

```java
HOReborn.init():
1. CompatibilityLayer.initialize()  // Version detection
2. EFFECTS.register()                // Register effects
3. ITEMS.register()                  // Register items
4. HungerOverhaulConfig.init()       // Load configuration
5. FoodEventHandler.register()       // Register food handlers
6. PlayerEventHandler.register()     // Register player handlers
7. WorldEventHandler.register()      // Register world handlers
8. FOOD_MODULE.init()                // Food system
9. CROP_MODULE.init()                // Crop growth
10. ANIMAL_MODULE.init()             // Animals
11. TOOL_MODULE.init()               // Tools
12. HUNGER_MODULE.init()             // Hunger
13. HEALTH_MODULE.init()             // Health
14. SERENE_SEASONS_MODULE.init()     // Seasons
```

## Module Dependencies

```
FoodModule
    └─→ FoodEventHandler
    └─→ FoodUtil
    └─→ FoodCategorizer

CropModule
    └─→ WorldEventHandler
    └─→ SereneSeasonsModule (optional)
    └─→ SereneSeasonsAPI (optional)

ToolModule
    └─→ WorldEventHandler
    └─→ Loot tables

AnimalModule
    └─→ Platform-specific event handlers

HungerModule
    └─→ PlayerEventHandler
    └─→ Effects (Hungry)

HealthModule
    └─→ PlayerEventHandler
    └─→ Effects (Well Fed)

SereneSeasonsModule
    └─→ SereneSeasonsAPI (optional)
    └─→ CropModule
```

## Adding New Modules

To add a new gameplay module:

1. Create class in `org.Netroaki.Main.modules` package
2. Add `init()` method
3. Register any event handlers
4. Add configuration section in `HungerOverhaulConfig`
5. Initialize in `HOReborn.init()`
6. Document in this file

Example skeleton:
```java
package org.Netroaki.Main.modules;

import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

public class NewModule {
    public void init() {
        HOReborn.LOGGER.info("Initializing New Module");
        
        // Register events
        // Setup handlers
        
        if (HungerOverhaulConfig.getInstance().newFeature.enabled) {
            // Feature-specific initialization
        }
    }
}
```

