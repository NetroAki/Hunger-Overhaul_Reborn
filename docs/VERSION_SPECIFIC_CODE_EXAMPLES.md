# Version-Specific Code Examples

## Overview

This document shows practical examples of how to handle version differences while maintaining code parity.

## Pattern 1: Runtime Version Detection

### Use Case: Different API calls between versions

```java
// In common/src/main/java/org/Netroaki/Main/util/FoodUtil.java
public class FoodUtil {

    public static boolean canPlayerEat(Player player, ItemStack food) {
        if (VersionDetector.is1_20_1()) {
            // 1.20.1 API
            return player.canEat(food.isEdible());
        } else if (VersionDetector.is1_21_1()) {
            // 1.21.1 API - hypothetical change
            return player.canConsume(food);
        }
        return false;
    }
}
```

## Pattern 2: Platform-Specific Overrides

### Use Case: Different event systems (Fabric vs Forge)

```java
// In common/src/main/java/org/Netroaki/Main/handlers/EventHandler.java
public abstract class EventHandler {

    public abstract void registerEvents();

    public static EventHandler create() {
        // Platform detection handled by CompatibilityLayer
        return CompatibilityLayer.createEventHandler();
    }
}
```

```java
// In fabric/src/main/java/org/Netroaki/Main/fabric/FabricEventHandler.java
@Environment(EnvType.CLIENT)
public class FabricEventHandler extends EventHandler {

    @Override
    public void registerEvents() {
        // Fabric-specific event registration
        HudRenderCallback.EVENT.register(HudRenderer::renderHudWarnings);
    }
}
```

```java
// In forge/src/main/java/org/Netroaki/Main/forge/ForgeEventHandler.java
public class ForgeEventHandler extends EventHandler {

    @Override
    public void registerEvents() {
        // Forge-specific event registration
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        HudRenderer.renderHudWarnings(event.getGuiGraphics(), event.getPartialTick());
    }
}
```

## Pattern 3: Version-Specific File Overrides

### Use Case: Different mixin targets between versions

```
common/src/main/java/org/Netroaki/Main/mixin/
├── FoodItemMixin.java (common implementation)

1.20.1/src/main/java/org/Netroaki/Main/mixin/
└── FoodItemMixin.java (1.20.1 specific overrides)

1.21.1/src/main/java/org/Netroaki/Main/mixin/
└── FoodItemMixin.java (1.21.1 specific overrides)
```

```java
// In 1.20.1/src/main/java/org/Netroaki/Main/mixin/FoodItemMixin.java
@Mixin(FoodItem.class)
public abstract class FoodItemMixin {

    @Shadow
    private FoodProperties foodProperties; // 1.20.1 field name

    @Inject(method = "getFoodProperties", at = @At("HEAD"), cancellable = true)
    private void modifyFoodProperties(CallbackInfoReturnable<FoodProperties> cir) {
        // 1.20.1 specific logic
        FoodProperties original = this.foodProperties;
        FoodProperties modified = FoodRegistry.modifyFoodProperties(original);
        cir.setReturnValue(modified);
    }
}
```

```java
// In 1.21.1/src/main/java/org/Netroaki/Main/mixin/FoodItemMixin.java
@Mixin(FoodItem.class)
public abstract class FoodItemMixin {

    @Shadow
    private FoodData foodData; // 1.21.1 field name change

    @Inject(method = "getFoodData", at = @At("HEAD"), cancellable = true)
    private void modifyFoodData(CallbackInfoReturnable<FoodData> cir) {
        // 1.21.1 specific logic
        FoodData original = this.foodData;
        FoodData modified = FoodRegistry.modifyFoodData(original);
        cir.setReturnValue(modified);
    }
}
```

## Pattern 4: Configuration with Version Checks

### Use Case: Version-specific default values

```java
// In common/src/main/java/org/Netroaki/Main/config/HungerOverhaulConfig.java
public static class GUISettings {
    // Common config options
    public boolean addGuiText = true;

    // Version-specific defaults
    public boolean effect_warnings = VersionDetector.is1_20_1() ? false : true;

    // Version-dependent features
    public boolean advanced_feature = VersionDetector.is1_21_1();
}
```

## Pattern 5: Build-Time Version Detection

### Use Case: Different dependencies per version

```kotlin
// In build.gradle.kts
dependencies {
    // Common dependencies for all versions
    modImplementation("com.example:common-lib:1.0.0")

    // Version-specific dependencies
    if (minecraft == "1.20.1") {
        modImplementation("com.example:legacy-lib:1.0.0")
    } else if (minecraft == "1.21.1") {
        modImplementation("com.example:modern-lib:2.0.0")
    }

    // Platform-specific dependencies
    if (loader == "fabric") {
        modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    } else if (loader == "forge") {
        forge("net.minecraftforge:forge:${property("deps.forge")}")
    }
}
```

## Pattern 6: Abstracted Version Differences

### Use Case: Different registry systems

```java
// In common/src/main/java/org/Netroaki/Main/util/RegistryUtil.java
public class RegistryUtil {

    public static <T> Registry<T> getRegistry(ResourceKey<Registry<T>> key) {
        if (VersionDetector.is1_20_1()) {
            return RegistryUtil1201.getRegistry(key);
        } else if (VersionDetector.is1_21_1()) {
            return RegistryUtil1211.getRegistry(key);
        }
        throw new UnsupportedOperationException("Unsupported version");
    }
}
```

```java
// In 1.20.1/src/main/java/org/Netroaki/Main/util/RegistryUtil1201.java
public class RegistryUtil1201 {
    public static <T> Registry<T> getRegistry(ResourceKey<Registry<T>> key) {
        return BuiltInRegistries.REGISTRY.get(key.location());
    }
}
```

```java
// In 1.21.1/src/main/java/org/Netroaki/Main/util/RegistryUtil1211.java
public class RegistryUtil1211 {
    public static <T> Registry<T> getRegistry(ResourceKey<Registry<T>> key) {
        return BuiltInRegistries.REGISTRY.getValue(key.location());
    }
}
```

## Best Practices

### 1. Minimize Version-Specific Code
- Keep 95%+ of code in `common/`
- Only create version-specific files when absolutely necessary
- Document why each override exists

### 2. Test Both Versions
- Always test changes on both versions
- Use automated tests where possible
- Maintain parity checklists

### 3. Version Detection Priority
1. Build-time detection (gradle.properties)
2. Runtime detection (VersionDetector)
3. Platform abstraction (CompatibilityLayer)

### 4. Error Handling
```java
public static void safeOperation() {
    try {
        if (VersionDetector.is1_21_1()) {
            // New API call
            newApiMethod();
        } else {
            // Fallback for older versions
            legacyApiMethod();
        }
    } catch (NoSuchMethodError e) {
        // Graceful fallback
        emergencyFallback();
    }
}
```

### 5. Documentation
- Comment version-specific code with reasons
- Update this document when adding new patterns
- Maintain changelog of version differences
