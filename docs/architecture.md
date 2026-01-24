# Architecture Overview

This document provides a high-level overview of Hunger Overhaul Reborn's architecture and design patterns.

## Project Structure

```
Hunger Overhaul Reborn/
├── src/main/
│   ├── java/org/Netroaki/Main/
│   │   ├── HOReborn.java                    # Main mod class
│   │   ├── compat/                           # Compatibility layers
│   │   │   └── CompatibilityLayer.java      # Multi-version support
│   │   ├── config/                           # Configuration system
│   │   │   ├── HungerOverhaulConfig.java    # Main JSON config
│   │   │   └── HORConfig.java               # Container food config
│   │   ├── effects/                          # Status effects
│   │   │   ├── WellFedEffect.java           # Beneficial effect
│   │   │   └── HungryEffect.java            # Harmful effect
│   │   ├── handlers/                         # Event handlers
│   │   │   ├── FoodEventHandler.java        # Food consumption/modification
│   │   │   ├── PlayerEventHandler.java      # Player tick events
│   │   │   └── WorldEventHandler.java       # World-level events
│   │   ├── modules/                          # Feature modules
│   │   │   ├── FoodModule.java              # Food system
│   │   │   ├── CropModule.java              # Crop growth
│   │   │   ├── ToolModule.java              # Tool mechanics
│   │   │   ├── AnimalModule.java            # Animal breeding
│   │   │   ├── HungerModule.java            # Hunger mechanics
│   │   │   ├── HealthModule.java            # Health mechanics
│   │   │   └── SereneSeasonsModule.java     # Serene Seasons integration
│   │   ├── mixin/                            # Mixin implementations
│   │   │   ├── fabric/                       # Fabric-specific mixins
│   │   │   └── forge/                        # Forge-specific mixins
│   │   ├── platforms/                        # Platform-specific code
│   │   │   ├── fabric/                       # Fabric implementation
│   │   │   └── forge/                        # Forge implementation
│   │   └── util/                             # Utility classes
│   │       ├── FoodUtil.java                # Food helper functions
│   │       ├── FoodCategorizer.java         # Food categorization
│   │       ├── SereneSeasonsAPI.java        # Serene Seasons wrapper
│   │       └── VersionDetector.java         # Version detection
│   └── resources/
│       ├── assets/                           # Client-side resources
│       ├── data/                             # Server-side data
│       └── *.json, *.toml                    # Metadata files
├── build.gradle.kts                          # Build configuration
├── gradle.properties                         # Project properties
└── stonecutter.gradle.kts                    # Multi-version build
```

## Architecture Patterns

### 1. Module-Based Architecture

The mod is organized into independent modules, each responsible for a specific gameplay system:

```
HOReborn (Main)
    │
    ├─→ FoodModule        (Food value/stack modifications)
    ├─→ CropModule        (Crop growth mechanics)
    ├─→ ToolModule        (Tool modifications)
    ├─→ AnimalModule      (Animal breeding/growth)
    ├─→ HungerModule      (Hunger mechanics)
    ├─→ HealthModule      (Health regeneration)
    └─→ SereneSeasonsModule (Season integration)
```

**Benefits:**
- Clear separation of concerns
- Easy to enable/disable features
- Maintainable and testable
- Each module can be developed independently

### 2. Event-Driven Architecture

The mod uses Architectury's event system for cross-platform compatibility:

```
Game Events → Architectury Events → Event Handlers → Modules → Game State Changes
```

**Event Flow:**
1. Minecraft triggers game event (e.g., player eats food)
2. Architectury translates to common event
3. Event handlers receive event
4. Handlers delegate to appropriate modules
5. Modules apply game logic
6. Changes reflected in game

**Key Event Handlers:**
- `FoodEventHandler`: Food consumption, modification
- `PlayerEventHandler`: Player tick, respawn, join
- `WorldEventHandler`: Block breaks, world ticks

### 3. Platform Abstraction Layer

Uses Architectury to abstract platform differences:

```
Common Code (Platform-agnostic)
        ↓
Architectury API Layer
        ↓
    ┌───┴───┐
 Fabric   Forge/NeoForge
```

**Platform-Specific Code:**
- `HORebornFabric`: Fabric entry point and events
- `HORebornForge`: Forge entry point and events
- `GrowthEventHandler`: Forge-specific crop growth handling
- Platform-specific mixins in separate directories

### 4. Mixin-Based Modifications

Uses Mixins to modify vanilla behavior with minimal invasiveness:

**Mixin Targets:**
- `CropBlock`: Crop growth throttling
- `Item`: Food consumption mechanics
- `BowlFoodItem`, `MilkBucketItem`, etc.: Container food stacking

**Mixin Philosophy:**
- Inject at method heads for pre-checks
- Cancel vanilla behavior when needed
- Apply custom logic
- Maintain compatibility with other mods

### 5. Configuration System

Dual configuration system for different needs:

**JSON Configuration** (`HungerOverhaulConfig`):
- Main mod settings
- Feature toggles
- Multipliers and rates
- Pretty-printed for readability

**Properties Configuration** (`HORConfig`):
- Container-specific stack sizes
- User-friendly format
- Legacy support

### 6. Multi-Version Support

Three-layer approach to version compatibility:

**Layer 1: Build Time** (Stonecutter)
- Manages version-specific source files
- Conditional compilation
- Version-specific dependencies

**Layer 2: Runtime Detection** (`VersionDetector`)
- Detects Minecraft version at runtime
- Provides version checks
- Validates supported versions

**Layer 3: Compatibility Layer** (`CompatibilityLayer`)
- Version-specific implementations
- API version mapping
- Feature gating by version

## Core Systems

### 1. Initialization Flow

```
Mod Load
    ↓
HOReborn.init()
    ↓
├─→ CompatibilityLayer.initialize()    # Version detection
├─→ EFFECTS.register()                 # Register effects
├─→ ITEMS.register()                   # Register items
├─→ HungerOverhaulConfig.init()        # Load config
├─→ Event Handlers register            # Setup events
└─→ Modules initialize                 # Init all modules
    ↓
Game Ready
```

### 2. Food Modification Pipeline

```
Server Start
    ↓
FoodEventHandler.onServerStarting()
    ↓
├─→ getAllFoodItems()                  # Collect all food
├─→ Store original properties          # Backup for restoration
├─→ For each food item:
│   ├─→ FoodCategorizer.categorizeFood() # Determine category
│   ├─→ Apply nutrition changes        # Modify values
│   ├─→ Calculate stack size           # Based on nutrition
│   ├─→ Add Well Fed effect            # If enabled
│   └─→ FoodUtil.setFoodProperties()   # Apply changes
└─→ Log modifications                  # Debug info
```

### 3. Crop Growth System

**Fabric (Mixin-based):**
```
CropBlock.randomTick()
    ↓
AgeableBlockMixin.hor_onRandomTick() [HEAD injection]
    ↓
├─→ Check daylight requirement         # Cancel if night
├─→ SereneSeasonsAPI.shouldCropsGrow() # Check season fertility
├─→ Calculate growth multiplier        # Difficulty-adjusted
└─→ Random check                       # Probability gate
    ↓
    Pass → Continue vanilla growth
    Fail → Cancel growth
```

**Forge (Event-based):**
```
BlockEvent.CropGrowEvent.Pre
    ↓
GrowthEventHandler.onCropGrowPre()
    ↓
├─→ Check daylight requirement         # Deny if night
├─→ SereneSeasonsAPI.shouldCropsGrow() # Check season fertility
├─→ Calculate growth multiplier        # Difficulty-adjusted
└─→ Random check                       # Probability gate
    ↓
    Pass → Result.DEFAULT (allow)
    Fail → Result.DENY (cancel)
```

### 4. Player Tick System

```
Every Tick (20 times/second)
    ↓
PlayerEventHandler.onPlayerTick()
    ↓
├─→ Constant Hunger Loss               # Time-based depletion
├─→ Low Hunger Effects                 # Apply Hungry effect
├─→ Health Regeneration                # Above threshold
├─→ Well Fed Regeneration              # Short regen burst
└─→ Zero Hunger Death                  # Instant death
```

### 5. Serene Seasons Integration

Uses reflection for optional dependency:

```
Static Initialization
    ↓
SereneSeasonsAPI.initializeAPI()
    ↓
├─→ Try load Serene Seasons classes    # Reflection
├─→ Cache methods and enum values      # Performance
├─→ Set apiAvailable flag              # Success/failure
└─→ Log status                         # Debug info
    ↓
Runtime Usage
    ↓
SereneSeasonsAPI.getCurrentSeason()
    ↓
├─→ Check apiAvailable                 # Quick bail
├─→ Call SeasonHelper.getSeasonState() # Get season
├─→ Extract season + subseason         # Parse data
└─→ Return SeasonInfo                  # Wrapped data
    ↓
SereneSeasonsAPI.shouldCropsGrow()
    ↓
├─→ Get current season                 # Season info
├─→ Call ModFertility.isCropFertile()  # Fertility check
└─→ Return boolean                     # Can grow?
```

## Design Principles

### 1. Separation of Concerns
- Each module handles one system
- Handlers only route events
- Utils provide reusable functions
- Configuration is centralized

### 2. Platform Independence
- Common code uses Architectury APIs
- Platform-specific code isolated
- Conditional compilation where needed
- Runtime detection for features

### 3. Compatibility First
- Optional dependencies via reflection
- Graceful degradation
- Mixin injection points carefully chosen
- No direct ASM manipulation

### 4. Configuration-Driven
- All features configurable
- Sensible defaults
- Per-difficulty scaling
- Runtime reloadable where possible

### 5. Performance Conscious
- Caching of expensive operations
- Early bailouts in event handlers
- Minimal allocations in hot paths
- Efficient data structures

## Extension Points

For modders wanting to integrate:

1. **Food Categorization**: Extend `FoodCategorizer` for custom food tiers
2. **Growth Modifiers**: Hook into `SereneSeasonsAPI.calculateGrowthMultiplier()`
3. **Effects**: Add custom effects that interact with Well Fed/Hungry
4. **Config Integration**: Add config sections via `HungerOverhaulConfig`

See [API Reference](api-reference.md) for detailed integration guides.

## Common Patterns

### Conditional Feature Execution
```java
if (HungerOverhaulConfig.getInstance().feature.enabled) {
    // Feature logic
}
```

### Platform-Specific Code
```java
if (Platform.isModLoaded("modid")) {
    // Optional integration
}
```

### Event Result Pattern
```java
EventResult result = checkConditions();
if (result != EventResult.pass()) {
    return result; // Interrupt/modify
}
return EventResult.pass(); // Continue vanilla
```

### Reflection-Based Optional Dependencies
```java
try {
    Class<?> optionalClass = Class.forName("mod.Optional");
    // Use optional feature
} catch (Exception e) {
    // Feature not available
}
```

