# Configuration Guide

Complete reference for all configuration options in Hunger Overhaul Reborn.

## Configuration Files

Hunger Overhaul Reborn uses two configuration files:

### 1. Main Configuration: `hunger_overhaul_reborn.json`
- **Location**: `config/hunger_overhaul_reborn.json`
- **Format**: JSON (pretty-printed)
- **Auto-generated**: Yes, on first launch
- **Reload**: Requires game restart

### 2. Container Configuration: `hunger_overhaul_reborn.properties`
- **Location**: `config/hunger_overhaul_reborn.properties`
- **Format**: Java Properties
- **Auto-generated**: Yes, on first launch
- **Reload**: Requires game restart

## Main Configuration Reference

### Food Settings

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

#### Food Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `modifyFoodStats` | boolean | true | Master toggle for all food modifications |
| `modifyFoodValues` | boolean | true | Change nutrition values of food items |
| `showFoodTooltips` | boolean | true | Show food category in item tooltips |
| `enableWellFedEffect` | boolean | true | Apply Well Fed effect when eating |
| `modifyStackSizes` | boolean | true | Adjust stack sizes based on food value |
| `modifyEatingSpeed` | boolean | true | Change eating duration based on food value |
| `modFoodValueDivider` | double | 4.0 | Divider for modded food values (higher = more nerfed) |
| `enableLowHungerWarnings` | boolean | true | Show low hunger/health warnings on HUD |

**Food Value Divider**: Modded foods (from non-Minecraft mods) have their nutrition divided by this value. For example, with divider 4.0, a mod food with 8 nutrition becomes 2 nutrition.

### Crop Settings

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

#### Crop Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `cropsOnlyGrowInDaylight` | boolean | true | Crops require skylight ≥9 and daytime |
| `cropsTakeLongerToGrow` | boolean | true | Apply growth multiplier to slow growth |
| `cropGrowthMultiplier` | double | 0.5 | Base growth chance (0.5 = 50% chance) |
| `cropsOnlyGiveSeeds` | boolean | true | Breaking crops gives only seeds, not drops |
| `difficultyScalingBoneMeal` | boolean | true | Bone meal success based on difficulty |
| `bonemealSuccessRateEasy` | double | 1.0 | Bone meal chance on Peaceful/Easy (100%) |
| `bonemealSuccessRateNormal` | double | 0.5 | Bone meal chance on Normal (50%) |
| `bonemealSuccessRateHard` | double | 0.25 | Bone meal chance on Hard (25%) |
| `removeTallGrassSeeds` | boolean | true | Breaking tall grass gives no seeds |
| `disableCropGrowthModifications` | boolean | false | Disable all crop growth changes |
| `autoDetectGrowthMods` | boolean | true | Detect conflicting growth mods |
| `enableSereneSeasonsCompatibility` | boolean | true | Enable Serene Seasons integration |
| `respectSeasonalGrowth` | boolean | true | Use seasonal fertility checks |
| `winterGrowthMultiplier` | double | 0.3 | Growth multiplier in winter (30%) |
| `springGrowthMultiplier` | double | 1.0 | Growth multiplier in spring (100%) |
| `summerGrowthMultiplier` | double | 1.2 | Growth multiplier in summer (120%) |
| `autumnGrowthMultiplier` | double | 0.6 | Growth multiplier in autumn (60%) |

**Growth Multiplier Stacking**: The final growth chance is calculated as:
```
base * difficulty * season
```

For example, on Normal difficulty in winter:
```
0.5 (base) * 0.5 (normal) * 0.3 (winter) = 0.075 (7.5% chance)
```

### Animal Settings

```json
{
  "animals": {
    "eggTimeoutMultiplier": 4.0,
    "breedingTimeoutMultiplier": 4.0,
    "childDurationMultiplier": 4.0
  }
}
```

#### Animal Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `eggTimeoutMultiplier` | double | 4.0 | Multiplier for egg-laying cooldown |
| `breedingTimeoutMultiplier` | double | 4.0 | Multiplier for breeding cooldown |
| `childDurationMultiplier` | double | 4.0 | Multiplier for time as baby |

**Time Calculations**:
- Base breeding cooldown: 6000 ticks (5 min) → 24000 ticks (20 min) at 4.0x
- Base child duration: 24000 ticks (20 min) → 96000 ticks (80 min) at 4.0x
- Egg laying varies by animal, all multiplied by the factor

### Tool Settings

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

#### Tool Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `modifyHoeUse` | boolean | true | Require water nearby for tilling |
| `removeHoeRecipes` | boolean | true | Remove vanilla hoe crafting recipes |
| `hoeToolDamageMultiplier` | double | 5.0 | Durability damage when tilling without water |
| `seedChanceMultiplier` | double | 1.0 | Multiplier for seed drop chance (unused) |
| `seedChanceBase` | double | 0.4 | Base chance for seeds from hoeing (unused) |

**Note**: `seedChanceMultiplier` and `seedChanceBase` are defined but not currently used. Seed chance is fixed at 20% in code.

### Hunger Settings

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

#### Hunger Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `constantHungerLoss` | boolean | true | Lose hunger over time (passive drain) |
| `constantHungerLossRate` | double | 0.001 | Rate of hunger loss (unused in code) |
| `modifyRespawnHunger` | boolean | true | Set custom hunger on respawn/join |
| `respawnHungerValue` | int | 20 | Base hunger on respawn (full bar) |
| `respawnHungerDifficultyModifier` | int | 4 | Hunger reduction per difficulty level |
| `difficultyScalingRespawnHunger` | boolean | true | Apply difficulty scaling to respawn |
| `difficultyScalingHungerLoss` | boolean | true | Scale hunger loss by difficulty |
| `harvestCraftHungerLossIncrease` | boolean | true | Increase hunger loss with HarvestCraft |
| `harvestCraftHungerLossMultiplier` | double | 1.33 | HarvestCraft hunger loss multiplier |
| `instantDeathOnZeroHunger` | boolean | true | Die instantly at 0 hunger |
| `lowHungerEffects` | boolean | true | Apply Hungry effect at low hunger |
| `lowHungerSlownessThreshold` | int | 6 | Hunger level for Hungry I |
| `lowHungerWeaknessThreshold` | int | 4 | Hunger level for Hungry II |

**Hunger Loss Calculation**:
```java
Base interval: 24000 ticks (20 minutes per point)

Difficulty multipliers:
- Peaceful/Easy: 0.75x
- Normal: 1.0x
- Hard: 1.5x

With HarvestCraft: Additional 1.33x

Final interval = base / (difficulty * harvestcraft)

Example (Normal, no HarvestCraft):
24000 / (1.0 * 1.0) = 24000 ticks = 20 minutes per hunger point
```

### Health Settings

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

#### Health Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `healthHealsAboveThreeShanks` | boolean | true | Enable custom health regeneration |
| `healthHealingThreshold` | int | 6 | Minimum hunger for healing (3 shanks) |
| `lowHealthEffects` | boolean | true | Apply effects at low health (unused) |
| `lowHealthEffectsThreshold` | int | 6 | Health threshold for effects (unused) |
| `difficultyScalingHealing` | boolean | true | Scale healing rate by difficulty |

**Healing Calculation**:
```java
Base interval: 120 ticks (6 seconds per half-heart)

Difficulty multipliers:
- Peaceful/Easy: 0.75x (faster healing)
- Normal: 1.0x
- Hard: 1.5x (slower healing)

Final interval = base / difficulty

Example (Normal):
120 / 1.0 = 120 ticks = 6 seconds per half-heart
```

### Integration Settings

```json
{
  "integration": {
    "enableHarvestCraftIntegration": true,
    "enableVillageIntegration": true,
    "enableChestLootIntegration": true
  }
}
```

#### Integration Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `enableHarvestCraftIntegration` | boolean | true | HarvestCraft food and trade integration |
| `enableVillageIntegration` | boolean | true | Add HarvestCraft foods to villager trades |
| `enableChestLootIntegration` | boolean | true | Modify chest loot tables (planned feature) |

## Container Configuration Reference

### File: `hunger_overhaul_reborn.properties`

```properties
# Container Food Stack Sizes
containerFood.bowl.stackSize=16
containerFood.bottle.stackSize=8
containerFood.bucket.stackSize=4
containerFood.default.stackSize=8
```

#### Container Options

| Property | Type | Default | Range | Description |
|----------|------|---------|-------|-------------|
| `containerFood.bowl.stackSize` | int | 16 | 1-64 | Stack size for bowl foods (stews) |
| `containerFood.bottle.stackSize` | int | 8 | 1-64 | Stack size for bottle foods (honey) |
| `containerFood.bucket.stackSize` | int | 4 | 1-64 | Stack size for bucket foods (milk) |
| `containerFood.default.stackSize` | int | 8 | 1-64 | Stack size for other container foods |

**Container Detection**:
- Bowl: Item ID contains "bowl"
- Bottle: Item ID contains "bottle" or "potion"
- Bucket: Item ID contains "bucket"
- Default: All other container foods

**Affected Items**:
- Bowls: Mushroom stew, rabbit stew, beetroot soup, suspicious stew
- Bottles: Honey bottle
- Buckets: Milk bucket
- Default: Other mod-added container foods

## Configuration Tips

### Recommended Presets

#### Hardcore Survival
```json
{
  "food": {
    "modFoodValueDivider": 6.0
  },
  "crops": {
    "cropGrowthMultiplier": 0.33,
    "bonemealSuccessRateNormal": 0.33,
    "bonemealSuccessRateHard": 0.1
  },
  "hunger": {
    "harvestCraftHungerLossMultiplier": 1.5,
    "respawnHungerValue": 16
  },
  "animals": {
    "breedingTimeoutMultiplier": 6.0,
    "childDurationMultiplier": 6.0
  }
}
```

#### Balanced Challenge
```json
{
  "food": {
    "modFoodValueDivider": 4.0
  },
  "crops": {
    "cropGrowthMultiplier": 0.5,
    "bonemealSuccessRateNormal": 0.5,
    "bonemealSuccessRateHard": 0.25
  },
  "hunger": {
    "harvestCraftHungerLossMultiplier": 1.33,
    "respawnHungerValue": 20
  },
  "animals": {
    "breedingTimeoutMultiplier": 4.0,
    "childDurationMultiplier": 4.0
  }
}
```

#### Light Touch (Vanilla+)
```json
{
  "food": {
    "modFoodValueDivider": 2.0,
    "modifyStackSizes": false
  },
  "crops": {
    "cropGrowthMultiplier": 0.75,
    "cropsOnlyGrowInDaylight": false
  },
  "hunger": {
    "constantHungerLoss": false,
    "instantDeathOnZeroHunger": false
  },
  "animals": {
    "breedingTimeoutMultiplier": 2.0,
    "childDurationMultiplier": 2.0
  }
}
```

### Compatibility Notes

**Serene Seasons**:
- Automatically detected
- `enableSereneSeasonsCompatibility` must be true
- `respectSeasonalGrowth` controls fertility checks
- Season multipliers stack with difficulty multipliers

**HarvestCraft**:
- Automatically detected
- Foods automatically nerfed by `modFoodValueDivider`
- Adds villager trades if `enableVillageIntegration` is true
- Increases hunger loss if `harvestCraftHungerLossIncrease` is true

**Crop Growth Mods**:
- Most growth-modifying mods are compatible
- May need to adjust `cropGrowthMultiplier` for balance
- Set `disableCropGrowthModifications` if conflicts occur

### Performance Tuning

For better performance:
- Disable `showFoodTooltips` if using many mods with tooltips
- Disable `constantHungerLoss` to reduce player tick overhead
- Set `autoDetectGrowthMods` to false if not needed

### Troubleshooting

**Problem**: Crops grow too slowly
- **Solution**: Increase `cropGrowthMultiplier` (try 0.75 or 1.0)
- **Solution**: Disable `difficultyScalingBoneMeal` for reliable bone meal

**Problem**: Hunger depletes too fast
- **Solution**: Decrease `harvestCraftHungerLossMultiplier`
- **Solution**: Disable `constantHungerLoss`
- **Solution**: Increase `respawnHungerValue`

**Problem**: Food modifications not working
- **Solution**: Ensure `modifyFoodStats` is true
- **Solution**: Check that server is restarted after config changes
- **Solution**: Verify config file is valid JSON

**Problem**: Serene Seasons not working
- **Solution**: Ensure Serene Seasons is installed
- **Solution**: Enable `enableSereneSeasonsCompatibility`
- **Solution**: Check logs for API initialization messages

## Configuration Loading

Configuration is loaded during mod initialization:

1. Check if config file exists
2. If not, create default configuration
3. Load configuration from JSON
4. Validate values (clamp to valid ranges)
5. Apply configuration to modules
6. Save if any values were adjusted

**Note**: Changes require a game restart to take effect. Some modules cache configuration values at startup for performance.

