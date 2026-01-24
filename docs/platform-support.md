# Platform Support

Documentation for cross-platform compatibility in Hunger Overhaul Reborn.

## Overview

Hunger Overhaul Reborn supports three mod loaders through Architectury:
- **Fabric** (1.20.1, 1.21.1)
- **Forge** (1.20.1)
- **NeoForge** (1.21.1)

This document explains how platform-specific code is organized and how the mod maintains compatibility across platforms.

## Architecture

### Architectury Framework

Hunger Overhaul Reborn uses **Architectury** as the compatibility layer:

```
Common Code (Platform-agnostic)
        ↓
Architectury API Layer
        ↓
    ┌───┴───┐
 Fabric   Forge/NeoForge
```

**Benefits**:
- Single codebase for most features
- Platform-specific implementations isolated
- Event system abstraction
- Registry abstraction
- Version management

### Code Organization

```
src/main/java/org/Netroaki/Main/
├── HOReborn.java              # Common initialization
├── modules/                   # Platform-agnostic modules
├── handlers/                  # Platform-agnostic event handlers
├── config/                    # Platform-agnostic configuration
├── effects/                   # Platform-agnostic effects
├── util/                      # Platform-agnostic utilities
├── mixin/                     # Platform-specific mixins
│   ├── fabric/               # Fabric-only mixins
│   └── forge/                # Forge-only mixins
└── platforms/                 # Platform entry points
    ├── fabric/               # Fabric-specific code
    │   ├── HORebornFabric.java
    │   └── client/
    └── forge/                # Forge-specific code
        ├── HORebornForge.java
        └── GrowthEventHandler.java
```

## Platform Entry Points

### Fabric Entry Point

**File**: `org.Netroaki.Main.platforms.fabric.HORebornFabric`

**Implements**: `net.fabricmc.api.ModInitializer`

```java
public final class HORebornFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // 1. Initialize common code
        HOReborn.init();

        // 2. Register Fabric-specific events
        registerFabricEvents();

        // 3. Register commands
        registerCommands();

        // 4. Register villager trades
        registerVillagerTrades();
    }
}
```

**Key Fabric Features**:
- Uses Fabric API event system
- Mixin-based crop growth (AgeableBlockMixin)
- ItemTooltipCallback for food tooltips
- UseBlockCallback for hoe/bonemeal
- ServerTickEvents for Mining Fatigue application

### Forge Entry Point

**File**: `org.Netroaki.Main.platforms.forge.HORebornForge`

**Annotation**: `@Mod(HOReborn.MOD_ID)`

```java
@Mod(HOReborn.MOD_ID)
public final class HORebornForge {
    public HORebornForge() {
        // 1. Register mod event bus
        EventBuses.registerModEventBus(HOReborn.MOD_ID, 
            FMLJavaModLoadingContext.get().getModEventBus());

        // 2. Initialize common code
        HOReborn.init();

        // 3. Setup mod event bus
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetup);

        // 4. Register Forge event bus
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) { }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) { }

    // ... more event handlers ...
}
```

**Key Forge Features**:
- Uses Forge event system exclusively
- Event-based crop growth (BlockEvent.CropGrowEvent.Pre)
- Dedicated GrowthEventHandler class
- Break speed events for Mining Fatigue
- More granular event control

### NeoForge Support

**Status**: 1.21.1+ uses NeoForge instead of Forge

**Differences from Forge**:
- Package changes: `net.neoforged` instead of `net.minecraftforge`
- Updated dependency: `net.neoforged:neoforge` instead of `net.minecraftforge:forge`
- Compatible event system (mostly same API)
- Uses `mods.toml` instead of `META-INF/mods.toml`

**Build Configuration**: Handled automatically by Architectury and version detection.

## Platform-Specific Implementations

### Crop Growth

**Fabric** (Mixin-based):
```java
@Mixin(CropBlock.class)
public class AgeableBlockMixin {
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void hor_onRandomTick(BlockState state, ServerLevel level,
                                   BlockPos pos, RandomSource random,
                                   CallbackInfo ci) {
        // Check conditions
        if (!shouldGrow()) {
            ci.cancel(); // Prevent vanilla growth
            return;
        }
        // Allow vanilla growth to proceed
    }
}
```

**Forge** (Event-based):
```java
@Mod.EventBusSubscriber(modid = HOReborn.MOD_ID, bus = Bus.FORGE)
public class GrowthEventHandler {
    @SubscribeEvent
    public static void onCropGrowPre(BlockEvent.CropGrowEvent.Pre event) {
        // Check conditions
        if (!shouldGrow()) {
            event.setResult(Event.Result.DENY); // Prevent growth
            return;
        }
        // Growth proceeds with Result.DEFAULT
    }
}
```

**Why Different?**
- Forge has dedicated crop growth event
- Fabric lacks this event, requires mixin
- Same logic, different delivery mechanism

### Food Consumption

**Fabric**:
```java
UseItemCallback.EVENT.register((player, world, hand) -> {
    ItemStack stack = player.getItemInHand(hand);
    // Handle food consumption
    return InteractionResultHolder.pass(stack);
});
```

**Forge**:
```java
@SubscribeEvent
public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
    if (event.getEntity() instanceof Player player) {
        FoodEventHandler.onFoodConsumed(player, event.getItem());
    }
}
```

### Mining Fatigue (Hungry III)

**Fabric** (Tick-based):
```java
ServerTickEvents.END_SERVER_TICK.register(server -> {
    for (ServerPlayer player : server.getPlayerList().getPlayers()) {
        var inst = player.getEffect(HUNGRY_EFFECT);
        if (inst != null && inst.getAmplifier() >= 2) {
            // Apply hidden Mining Fatigue every tick
            player.addEffect(new MobEffectInstance(
                MobEffects.DIG_SLOWDOWN, 20, 0, false, false, false
            ));
        } else {
            player.removeEffect(MobEffects.DIG_SLOWDOWN);
        }
    }
});
```

**Forge** (Event-based):
```java
@SubscribeEvent
public void onBreakSpeed(BreakSpeed event) {
    var inst = event.getEntity().getEffect(HUNGRY_EFFECT);
    if (inst != null && inst.getAmplifier() >= 2) {
        // Reduce mining speed by 50%
        event.setNewSpeed(event.getNewSpeed() * 0.5f);
    }
}
```

**Performance**: Forge approach is more efficient (only fires when breaking blocks).

### Tooltips

**Fabric**:
```java
ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
    Component tooltip = FoodModule.getFoodTooltip(stack);
    if (tooltip != null) {
        lines.add(tooltip);
    }
});
```

**Forge**:
```java
@SubscribeEvent
public void onItemTooltip(ItemTooltipEvent event) {
    Component tooltip = FoodModule.getFoodTooltip(event.getItemStack());
    if (tooltip != null) {
        event.getToolTip().add(tooltip);
    }
}
```

### Commands

**Fabric** (Brigadier Registration):
```java
CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
    LiteralArgumentBuilder<CommandSourceStack> cmd = 
        Commands.literal("ho_hunger")
            .requires(src -> src.hasPermission(2))
            .then(Commands.argument("value", IntegerArgumentType.integer(0, 20))
                .executes(ctx -> {
                    // Command logic
                    return 1;
                }));
    dispatcher.register(cmd);
});
```

**Forge** (Event Registration):
```java
@SubscribeEvent
public void onRegisterCommands(RegisterCommandsEvent event) {
    LiteralArgumentBuilder<CommandSourceStack> cmd = 
        Commands.literal("ho_hunger")
            .requires(src -> src.hasPermission(2))
            .then(Commands.argument("value", IntegerArgumentType.integer(0, 20))
                .executes(ctx -> {
                    // Command logic
                    return 1;
                }));
    event.getDispatcher().register(cmd);
}
```

**Note**: Command logic is identical, only registration differs.

## Architectury API Usage

### Event System

**Common Pattern**:
```java
// Using Architectury events (cross-platform)
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;

// Registration
PlayerEvent.PLAYER_RESPAWN.register((player, conqueredEnd) -> {
    // Handle respawn
});

TickEvent.PLAYER_PRE.register((player) -> {
    // Handle player tick
});

BlockEvent.BREAK.register((level, pos, state, player, xp) -> {
    return EventResult.pass(); // or interrupt(true/false)
});
```

### Registry System

**Common Pattern**:
```java
// Using Architectury registries (cross-platform)
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

public static final DeferredRegister<MobEffect> EFFECTS = 
    DeferredRegister.create(MOD_ID, Registries.MOB_EFFECT);

public static final RegistrySupplier<MobEffect> WELL_FED_EFFECT = 
    EFFECTS.register("well_fed", WellFedEffect::new);

// In init()
EFFECTS.register();
```

### Platform Detection

**Common Pattern**:
```java
import dev.architectury.platform.Platform;

if (Platform.isModLoaded("serene_seasons")) {
    // Serene Seasons integration
}

if (Platform.isFabric()) {
    // Fabric-specific code
}

if (Platform.isForge()) {
    // Forge-specific code
}

Path configDir = Platform.getConfigFolder();
```

## Build Configuration

### Gradle Setup

**Root build.gradle.kts**:
```kotlin
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
}

architectury.common(stonecutter.tree.branches.mapNotNull {
    if (stonecutter.current.project !in it) null
    else it.prop("loom.platform")
})
```

### Version-Specific Dependencies

**For Fabric**:
```kotlin
if (loader == "fabric") {
    modImplementation("net.fabricmc:fabric-loader:${deps.fabric_loader}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${deps.fabric_version}")
    modImplementation("dev.architectury:architectury-fabric:${deps.architectury_version}")
}
```

**For Forge**:
```kotlin
if (loader == "forge") {
    "forge"("net.minecraftforge:forge:$minecraft-${deps.forge_loader}")
    modImplementation("dev.architectury:architectury-forge:${deps.architectury_version}")
}
```

**For NeoForge**:
```kotlin
if (loader == "neoforge") {
    "neoForge"("net.neoforged:neoforge:${deps.neoforge_loader}")
    modImplementation("dev.architectury:architectury-neoforge:${deps.architectury_version}")
}
```

### Source Sets

**Excluding Platform Code**:
```kotlin
sourceSets {
    main {
        java {
            srcDir(rootProject.file("src/main/java"))
            if (loader == "fabric") {
                exclude("**/platforms/forge/**")
                exclude("**/mixin/forge/**")
            } else if (loader == "forge" || loader == "neoforge") {
                exclude("**/platforms/fabric/**")
                exclude("**/mixin/fabric/**")
            }
        }
    }
}
```

## Compatibility Considerations

### When to Use Platform-Specific Code

**Use platform-specific when**:
- Feature only available on one platform (e.g., Forge events)
- Performance optimization for specific platform
- Workarounds for platform limitations
- Unavoidable API differences

**Use common/Architectury when**:
- Feature works on all platforms
- Logic is platform-independent
- Registry operations
- Basic event handling

### Testing Cross-Platform

**Testing Strategy**:
1. Test on Fabric first (more restrictive)
2. Test on Forge second (different event model)
3. Test on NeoForge (1.21.1 differences)
4. Test with mods that also use Architectury
5. Test with platform-specific mods

**Common Issues**:
- Event firing order differences
- Mixin application conflicts
- Registry timing differences
- Client/server synchronization

### Migration Path: Forge → NeoForge

**For 1.21.1**, Forge transitions to NeoForge:

**Changes Required**:
1. Update package imports
2. Update dependency declarations
3. Update mods.toml format
4. Test event compatibility

**Architectury Handles**:
- Build configuration
- Dependency resolution
- Most API compatibility

**Manual Handling**:
- Version-specific build properties
- Platform detection (if needed)
- NeoForge-specific features

## Performance Considerations

### Platform Performance Characteristics

**Fabric**:
- Generally lighter weight
- Faster startup (fewer transformers)
- Mixin-heavy approach
- Better for client-side performance

**Forge**:
- More event overhead
- Heavier mod loading
- Better server-side optimization
- More granular event control

**NeoForge**:
- Similar to Forge
- Ongoing optimizations
- Cleaner API in some areas

### Optimization Strategies

**General**:
- Cache Architectury lookups
- Minimize event handler work
- Use early returns
- Batch operations when possible

**Fabric-Specific**:
- Limit mixin injection points
- Use `@At("HEAD")` for early exits
- Avoid expensive mixin operations

**Forge-Specific**:
- Use event priorities appropriately
- Cancel events early when possible
- Leverage event result caching

## Debugging Platform Issues

### Logs

**Fabric**:
```
[HungerOverhaulReborn/INFO] Initializing Hunger Overhaul Reborn
[HungerOverhaulReborn/INFO] Running on Minecraft 1.20.1
[HungerOverhaulReborn/INFO] Fabric-specific events registered
```

**Forge**:
```
[HungerOverhaulReborn/INFO] Initializing Hunger Overhaul Reborn
[HungerOverhaulReborn/INFO] Running on Minecraft 1.20.1
[HungerOverhaulReborn/INFO] Forge event bus registered
```

### Common Problems

**Problem**: Events not firing
- **Check**: Platform-specific event registration
- **Check**: Event bus subscription
- **Solution**: Verify event name and signature

**Problem**: Mixin conflicts
- **Check**: Mixin config for platform
- **Check**: Mixin priority
- **Solution**: Adjust injection points or use mixin plugin

**Problem**: Registry issues
- **Check**: Architectury registry timing
- **Check**: Platform-specific registry calls
- **Solution**: Use DeferredRegister consistently

## Future Platform Support

**Potential Additions**:
- Quilt (Fabric fork) - Likely compatible as-is
- Future Minecraft versions - Requires version detection updates
- Rift (discontinued) - Not planned

**Maintenance Strategy**:
- Keep common code platform-agnostic
- Document platform-specific workarounds
- Test on all supported platforms before release
- Monitor Architectury updates for API changes

