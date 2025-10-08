# Serene Seasons Compatibility

## Overview

Hunger Overhaul Reborn includes built-in compatibility with Serene Seasons, a popular mod that adds seasonal weather and crop growth mechanics to Minecraft. This compatibility ensures that both mods work together harmoniously without conflicts.

## How It Works

### Automatic Detection
- The mod automatically detects if Serene Seasons is installed
- If detected, it switches to compatibility mode instead of using the standard crop growth logic
- This prevents conflicts between the two mods' crop growth modifications

### Seasonal Growth Integration
When Serene Seasons is present, Hunger Overhaul Reborn respects the seasonal growth mechanics:

- **Spring**: Normal growth with slight boost (configurable)
- **Summer**: Best growth conditions (configurable boost)
- **Autumn**: Reduced growth rates (configurable)
- **Winter**: Significantly reduced growth (configurable)

### Layered Logic
The compatibility system applies both mods' logic in a layered approach:

1. **Serene Seasons Logic**: Determines if crops should grow based on current season and season strength
2. **Hunger Overhaul Logic**: Applies additional modifiers for daylight-only growth and general growth multipliers

## Configuration Options

### Serene Seasons Compatibility Settings

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

### Configuration Explanations

- **`enableSereneSeasonsCompatibility`**: Enable/disable the compatibility system
- **`respectSeasonalGrowth`**: Whether to respect Serene Seasons' seasonal growth mechanics
- **`winterGrowthMultiplier`**: Growth rate multiplier for winter (0.3 = 30% of normal)
- **`springGrowthMultiplier`**: Growth rate multiplier for spring (1.0 = normal)
- **`summerGrowthMultiplier`**: Growth rate multiplier for summer (1.2 = 120% of normal)
- **`autumnGrowthMultiplier`**: Growth rate multiplier for autumn (0.6 = 60% of normal)

## Fallback Behavior

If Serene Seasons is not detected or the compatibility system fails:
- The mod falls back to standard Hunger Overhaul crop growth logic
- All original features (daylight-only growth, growth multipliers, etc.) continue to work
- No functionality is lost

## Benefits

### For Players
- **No Conflicts**: Both mods work together without issues
- **Enhanced Gameplay**: Combines the best of both mods' features
- **Configurable**: Fine-tune how the mods interact
- **Automatic**: No manual setup required

### For Modpack Creators
- **Easy Integration**: Just include both mods in your modpack
- **Predictable Behavior**: Consistent crop growth mechanics
- **Configurable**: Adjust settings to fit your modpack's balance

## Technical Details

### Implementation
- Uses reflection to access Serene Seasons' API
- Graceful fallback if Serene Seasons is not available
- Event-driven architecture for clean integration
- No hard dependencies on Serene Seasons

### Performance
- Minimal performance impact
- Reflection calls are cached for efficiency
- Only active when Serene Seasons is present

## Troubleshooting

### Compatibility Not Working
1. Ensure Serene Seasons is properly installed
2. Check that `enableSereneSeasonsCompatibility` is set to `true`
3. Verify both mods are compatible versions for your Minecraft version

### Unexpected Growth Rates
1. Adjust the seasonal growth multipliers in the config
2. Set `respectSeasonalGrowth` to `false` to disable seasonal effects
3. Check Serene Seasons' own configuration

### Performance Issues
1. The compatibility system has minimal impact, but if issues occur:
2. Set `enableSereneSeasonsCompatibility` to `false` to disable it
3. The mod will fall back to standard behavior

## Version Compatibility

- **Minecraft**: 1.20.1
- **Serene Seasons**: 6.0.0+ (Fabric/Forge)
- **Hunger Overhaul Reborn**: 0.1.0-alpha+

## Support

If you encounter issues with Serene Seasons compatibility:
1. Check the logs for compatibility detection messages
2. Verify both mods are up to date
3. Report issues on the GitHub repository with:
   - Minecraft version
   - Mod versions
   - Log files
   - Configuration files
