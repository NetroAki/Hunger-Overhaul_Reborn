# Development Guide

Guide for setting up, building, and contributing to Hunger Overhaul Reborn.

## Prerequisites

### Required Software
- **Java JDK 17** (for 1.20.1) or **Java JDK 21** (for 1.21.1)
- **Git** for version control
- **IntelliJ IDEA** (recommended) or **Eclipse**

### Recommended Tools
- **Gradle 8.5+** (included via wrapper)
- **Architectury Loom** (included as plugin)
- **Stonecutter** (for multi-version management)

## Project Setup

### 1. Clone Repository

```bash
git clone https://github.com/Netroaki/Hunger-Overhaul-Reborn.git
cd Hunger-Overhaul-Reborn
```

### 2. Import into IDE

**IntelliJ IDEA**:
1. File → Open → Select project directory
2. Wait for Gradle sync to complete
3. Gradle should auto-detect the project

**Eclipse**:
1. File → Import → Existing Gradle Project
2. Select project directory
3. Wait for import to complete

### 3. Generate Run Configurations

```bash
# For Fabric
./gradlew genSources
./gradlew fabricRunClient

# For Forge
./gradlew genSources
./gradlew forgeRunClient
```

## Project Structure

```
Hunger Overhaul Reborn/
├── src/main/               # Main source code
│   ├── java/               # Java source files
│   └── resources/          # Resources (assets, data, configs)
├── versions/               # Version-specific files (Stonecutter)
│   ├── 1.20.1/            # 1.20.1-specific files
│   ├── 1.20.1-fabric/     # 1.20.1 Fabric-specific
│   ├── 1.20.1-forge/      # 1.20.1 Forge-specific
│   ├── 1.21.1/            # 1.21.1-specific files
│   ├── 1.21.1-fabric/     # 1.21.1 Fabric-specific
│   └── 1.21.1-forge/      # 1.21.1 NeoForge-specific
├── docs/                   # Documentation
├── build.gradle.kts        # Main build configuration
├── stonecutter.gradle.kts  # Multi-version build config
├── settings.gradle.kts     # Gradle settings
└── gradle.properties       # Project properties
```

## Building

### Build Single Version

```bash
# Build current active version
./gradlew build

# Build specific version/platform
./gradlew :1.20.1-fabric:build
./gradlew :1.20.1-forge:build
./gradlew :1.21.1-fabric:build
./gradlew :1.21.1-forge:build
```

### Build All Versions

```bash
# Build all platforms and versions
./gradlew chiseledBuild
```

Output JARs location: `build/libs/<version>/<platform>/`

### Build Universal JAR

```bash
# Create universal JAR (Forgix-based)
./gradlew buildUniversal
```

Output: `build/distributions/`

## Running

### Run Client

```bash
# Run Fabric client (1.20.1)
./gradlew :1.20.1-fabric:runClient

# Run Forge client (1.20.1)
./gradlew :1.20.1-forge:runClient

# Run NeoForge client (1.21.1)
./gradlew :1.21.1-forge:runClient
```

### Run Server

```bash
# Run Fabric server
./gradlew :1.20.1-fabric:runServer

# Run Forge server
./gradlew :1.20.1-forge:runServer
```

### Run from IDE

**IntelliJ IDEA**:
1. Open Gradle panel (right side)
2. Navigate to Tasks → fabric/forge → runClient
3. Double-click to run

Or use auto-generated run configurations in the toolbar.

## Multi-Version Development

### Stonecutter Workflow

Stonecutter manages multiple Minecraft versions from a single codebase:

**Active Version**: Set in `stonecutter.gradle.kts`
```kotlin
stonecutter {
    active = "1.20.1-fabric" // Current working version
}
```

**Switch Versions**:
```bash
# Switch to different version
./gradlew stonecutterSwitch --version=1.21.1-fabric
```

**Version-Specific Code**:
```java
// Syntax: /*? if <condition> {*/  code  /*?}*/

// Example: 1.20.1-specific code
/*? if <=1.20.1 {*/
import net.minecraft.world.level.Level;
/*?}*/

// Example: 1.21.1-specific code
/*? if >=1.21.1 {*/
import net.minecraft.world.level.World;
/*?}*/
```

### Version Compatibility

**VersionDetector Utility**:
```java
import org.Netroaki.Main.util.VersionDetector;

if (VersionDetector.is1_20_1()) {
    // 1.20.1-specific logic
} else if (VersionDetector.is1_21_1()) {
    // 1.21.1-specific logic
}
```

## Code Style

### Formatting

**IntelliJ IDEA**:
- Use default Java code style
- 4 spaces for indentation
- 120 character line limit

**Eclipse**:
- Import Java code style from `.editorconfig`

### Naming Conventions

- **Classes**: PascalCase (`FoodModule`, `HungerEventHandler`)
- **Methods**: camelCase (`onFoodConsumed`, `calculateGrowthRate`)
- **Constants**: UPPER_SNAKE_CASE (`BASE_HUNGER_RATE`, `MOD_ID`)
- **Packages**: lowercase (`org.netroaki.main.modules`)

### Documentation

**JavaDoc**:
```java
/**
 * Calculates the growth multiplier for crops based on difficulty and season.
 * 
 * @param level The world level
 * @param baseMultiplier The base growth multiplier from config
 * @return The adjusted growth multiplier (0.0 to 1.0)
 */
public static double calculateGrowthMultiplier(Level level, double baseMultiplier) {
    // Implementation
}
```

**Inline Comments**:
```java
// Check if crop should grow in current season
if (!SereneSeasonsAPI.shouldCropsGrow(level, pos, state)) {
    return EventResult.interrupt(false); // Cancel growth
}
```

## Testing

### Manual Testing

**Test Checklist**:
- [ ] Food consumption and stacking
- [ ] Crop growth with different difficulties
- [ ] Hoe mechanics (tilling, seed drops)
- [ ] Hunger effects at different levels
- [ ] Well Fed effect application
- [ ] Serene Seasons integration (if installed)
- [ ] Commands (`/ho_hunger`, `/ho_item_tags`)
- [ ] Configuration changes
- [ ] Compatibility with popular mods

### Debug Commands

```
# Set hunger level
/ho_hunger <value>              # Set hunger (0-20)
/ho_hunger <value> <saturation> # Set hunger and saturation

# Check item tags
/ho_item_tags                   # Show tags of held item

# Apply effects
/effect give @p hunger_overhaul_reborn:well_fed 60 0
/effect give @p hunger_overhaul_reborn:hungry 60 2
```

### Debug Logging

Enable debug logging in `log4j2.xml`:
```xml
<Logger level="debug" name="org.Netroaki.Main"/>
```

Or via launch argument:
```
-Dlog4j.logger.org.Netroaki.Main=DEBUG
```

## Contributing

### Pull Request Process

1. **Fork the repository**
2. **Create feature branch**: `git checkout -b feature/my-feature`
3. **Make changes** and test thoroughly
4. **Commit**: `git commit -m "Add feature: description"`
5. **Push**: `git push origin feature/my-feature`
6. **Create Pull Request** on GitHub

### PR Guidelines

**Title**: Clear, descriptive (e.g., "Add seasonal crop growth multipliers")

**Description**:
- What does this PR do?
- Why is this change needed?
- How has it been tested?
- Any breaking changes?

**Checklist**:
- [ ] Code follows project style
- [ ] All features tested (Fabric + Forge)
- [ ] Configuration options added (if applicable)
- [ ] Documentation updated
- [ ] No debug code left in
- [ ] Builds successfully

### Code Review

Maintainers will review for:
- Code quality and style
- Platform compatibility
- Performance impact
- Configuration appropriateness
- Documentation completeness

## Common Tasks

### Adding New Food Category

1. Update `FoodCategorizer`:
```java
SPECIFIC_CATEGORIES.put(Items.MY_FOOD, FoodCategory.LARGE_MEAL);
```

2. Test with `/ho_item_tags` command
3. Update documentation in `modules.md`

### Adding New Configuration Option

1. Add to `HungerOverhaulConfig`:
```java
public static class MySettings {
    public boolean myFeature = true;
    public double myMultiplier = 1.5;
}

public final MySettings mySettings = new MySettings();
```

2. Use in code:
```java
if (HungerOverhaulConfig.getInstance().mySettings.myFeature) {
    // Feature logic
}
```

3. Document in `configuration.md`

### Adding New Module

1. Create class in `modules` package:
```java
public class MyModule {
    public void init() {
        HOReborn.LOGGER.info("Initializing My Module");
        // Registration logic
    }
}
```

2. Add to `HOReborn`:
```java
public static final MyModule MY_MODULE = new MyModule();

public static void init() {
    // ... other initialization ...
    MY_MODULE.init();
}
```

3. Document in `modules.md`

### Adding Platform-Specific Feature

1. Create in appropriate platform directory:
```java
// platforms/fabric/MyFabricFeature.java
public class MyFabricFeature {
    public static void register() {
        // Fabric-specific registration
    }
}

// platforms/forge/MyForgeFeature.java
public class MyForgeFeature {
    public static void register() {
        // Forge-specific registration
    }
}
```

2. Call from platform entry point:
```java
// In HORebornFabric.onInitialize()
MyFabricFeature.register();

// In HORebornForge constructor
MyForgeFeature.register();
```

3. Document differences in `platform-support.md`

## Troubleshooting

### Build Fails

**Problem**: Gradle build errors
- **Check**: Java version matches requirements
- **Check**: Gradle cache isn't corrupted: `./gradlew clean`
- **Solution**: Invalidate caches and restart IDE

### Mixin Not Applying

**Problem**: Changes don't take effect
- **Check**: Mixin config JSON syntax
- **Check**: Target class/method signature
- **Solution**: Enable mixin debug: `-Dmixin.debug=true`

### Version Detection Fails

**Problem**: Wrong version detected
- **Check**: `version-info.properties` is correct
- **Check**: SharedConstants available
- **Solution**: Use explicit version detection in `VersionDetector`

### Platform-Specific Crash

**Problem**: Works on Fabric, crashes on Forge (or vice versa)
- **Check**: Platform-specific event signatures
- **Check**: Mixin compatibility
- **Solution**: Add platform detection guards

## Resources

### Documentation
- [Architectury Documentation](https://docs.architectury.dev/)
- [Fabric Wiki](https://fabricmc.net/wiki/)
- [Forge Documentation](https://docs.minecraftforge.net/)
- [NeoForge Documentation](https://docs.neoforged.net/)
- [Mixin Documentation](https://github.com/SpongePowered/Mixin/wiki)

### Tools
- [Architectury Discord](https://discord.gg/architectury)
- [Fabric Discord](https://discord.gg/v6v4pMv)
- [Forge Discord](https://discord.gg/forge)

### Internal Docs
- [Architecture Overview](architecture.md)
- [Module System](modules.md)
- [Mixin System](mixins.md)
- [Platform Support](platform-support.md)

## Release Process

### Versioning

Format: `MAJOR.MINOR.PATCH-stage`

Examples:
- `0.2.1-beta` - Beta release
- `1.0.0` - First stable release
- `1.0.1` - Patch release

### Pre-Release Checklist

- [ ] All tests pass
- [ ] Documentation updated
- [ ] RELEASE_NOTES written
- [ ] Version bumped in `gradle.properties`
- [ ] Changelog updated
- [ ] All platforms built and tested

### Release Steps

1. **Tag release**: `git tag v0.2.1-beta`
2. **Push tag**: `git push origin v0.2.1-beta`
3. **Build all**: `./gradlew chiseledBuild`
4. **Create GitHub release** with JARs attached
5. **Publish to CurseForge** (if applicable)
6. **Publish to Modrinth** (if applicable)

## License

This project is licensed under [LICENSE.txt](../LICENSE.txt).

When contributing, you agree that your contributions will be licensed under the same license.

