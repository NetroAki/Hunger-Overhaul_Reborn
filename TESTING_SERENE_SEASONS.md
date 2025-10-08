# Testing Serene Seasons Compatibility

## Overview

This guide explains how to test the Serene Seasons compatibility in Hunger Overhaul Reborn.

## Setup for Testing

### 1. Download Serene Seasons

Download the appropriate Serene Seasons version for your mod loader:

- **Fabric**: Download from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/serene-seasons) or [Modrinth](https://modrinth.com/mod/serene-seasons)
- **Forge**: Download from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/serene-seasons) or [Modrinth](https://modrinth.com/mod/serene-seasons)

Make sure to get version 6.0.0+ for Minecraft 1.20.1.

### 2. Install Serene Seasons

1. Place the Serene Seasons JAR file in your `run/mods/` directory
2. Make sure you have the required dependencies:
   - **Fabric**: Fabric API
   - **Forge**: Forge (latest version)

### 3. Run the Game

1. Start Minecraft with both mods installed
2. Check the logs for compatibility messages:
   ```
   [INFO] Serene Seasons API initialized successfully
   [INFO] Serene Seasons compatibility: Active
   [INFO] Current season: SPRING
   ```

## Testing Scenarios

### 1. Basic Compatibility Test

1. Create a new world
2. Plant some crops (wheat, carrots, potatoes)
3. Observe crop growth behavior
4. Check that crops grow slower than vanilla but still grow

### 2. Seasonal Growth Test

1. Use Serene Seasons commands to change seasons:
   ```
   /season set spring
   /season set summer
   /season set autumn
   /season set winter
   ```
2. Observe how crop growth changes with seasons:
   - **Spring**: Normal growth
   - **Summer**: Slightly faster growth
   - **Autumn**: Slower growth
   - **Winter**: Very slow growth (may not grow at all)

### 3. Configuration Test

1. Open the mod configuration (Mod Menu or config file)
2. Toggle Serene Seasons compatibility settings:
   - `enableSereneSeasonsCompatibility`
   - `respectSeasonalGrowth`
   - Seasonal growth multipliers
3. Test that changes take effect immediately

### 4. Fallback Test

1. Remove Serene Seasons from the mods folder
2. Restart the game
3. Verify that Hunger Overhaul Reborn still works normally
4. Check logs for fallback messages

## Expected Behavior

### With Serene Seasons

- Crops grow based on both Serene Seasons and Hunger Overhaul logic
- Seasonal growth rates are applied
- Daylight-only growth is still enforced
- Bone meal scaling works with seasonal modifiers

### Without Serene Seasons

- Standard Hunger Overhaul crop growth behavior
- No seasonal effects
- All other features work normally

## Troubleshooting

### Compatibility Not Working

1. Check that Serene Seasons is properly installed
2. Verify both mods are compatible versions for 1.20.1
3. Check the logs for error messages
4. Ensure `enableSereneSeasonsCompatibility` is set to `true`

### Performance Issues

1. Serene Seasons can impact performance
2. Consider adjusting seasonal growth multipliers
3. Disable compatibility if not needed

### Unexpected Behavior

1. Check configuration settings
2. Verify Serene Seasons configuration
3. Test with default settings first

## Log Messages

### Successful Integration
```
[INFO] Serene Seasons API initialized successfully
[INFO] Serene Seasons compatibility: Active
[INFO] Current season: SPRING
```

### Fallback Mode
```
[DEBUG] Serene Seasons API not available: ClassNotFoundException
[INFO] Serene Seasons compatibility: Not available
```

### Configuration Changes
```
[INFO] Serene Seasons compatibility enabled
[INFO] Serene Seasons compatibility disabled
```

## Commands for Testing

### Serene Seasons Commands
- `/season get` - Get current season info
- `/season set <season>` - Set season (spring, summer, autumn, winter)
- `/season time <time>` - Set season time
- `/season info` - Get detailed season information

### Hunger Overhaul Commands
- Check if any custom commands are available
- Use configuration GUI if available

## Performance Considerations

- Serene Seasons can cause FPS drops on lower-spec systems
- The compatibility system has minimal performance impact
- Consider disabling seasonal effects if performance is poor
- Test with different modpack sizes

## Reporting Issues

If you encounter issues:

1. Check the logs for error messages
2. Note your Minecraft version and mod versions
3. Describe the expected vs actual behavior
4. Include configuration files if relevant
5. Report on the GitHub repository
