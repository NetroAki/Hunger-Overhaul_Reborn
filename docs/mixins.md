# Mixin System

Documentation for all Mixin implementations in Hunger Overhaul Reborn.

## Overview

Hunger Overhaul Reborn uses the Mixin framework to modify vanilla Minecraft behavior at the bytecode level. Mixins allow for surgical modifications without directly patching classes, maintaining compatibility with other mods.

## Mixin Architecture

### Directory Structure

```
src/main/java/org/Netroaki/Main/mixin/
├── fabric/                    # Fabric-specific mixins
│   ├── AgeableBlockMixin.java      # Crop growth throttling
│   ├── BowlFoodItemMixin.java      # Bowl food stacking
│   ├── FoodItemMixin.java          # General food stacking
│   ├── HoneyBottleItemMixin.java   # Honey bottle stacking
│   ├── ItemsMixin.java             # Container stack size modification
│   ├── MilkBucketItemMixin.java    # Milk bucket stacking
│   ├── SuspiciousStewItemMixin.java # Suspicious stew stacking
│   └── HORMixinPlugin.java         # Mixin configuration plugin
└── forge/                     # Forge-specific mixins (same files except AgeableBlockMixin)
```

### Configuration Files

**Fabric**: `src/main/resources/hunger_overhaul_reborn-fabric.mixins.json`
**Forge**: `src/main/resources/hunger_overhaul_reborn-forge.mixins.json`

Both platforms use platform-specific mixin configs that point to their respective mixin classes.

## Core Mixins

### 1. AgeableBlockMixin (Fabric Only)

**Target**: `net.minecraft.world.level.block.CropBlock`
**Method**: `randomTick(BlockState, ServerLevel, BlockPos, RandomSource)`
**Injection Point**: `@At("HEAD")` with cancellable
**Platform**: Fabric only (Forge uses event system)

**Purpose**: Throttle crop growth based on daylight, season, and difficulty.

**Implementation**:
```java
@Mixin(CropBlock.class)
public class AgeableBlockMixin {
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void hor_onRandomTick(BlockState state, ServerLevel level,
                                   BlockPos pos, RandomSource random,
                                   CallbackInfo ci) {
        // 1. Check daylight requirement
        if (HungerOverhaulConfig.getInstance().crops.cropsOnlyGrowInDaylight) {
            int sky = level.getBrightness(LightLayer.SKY, pos);
            if (!level.isDay() || sky < 9) {
                ci.cancel(); // Block growth
                return;
            }
        }

        // 2. Check Serene Seasons fertility
        if (!SereneSeasonsAPI.shouldCropsGrow(level, pos, state)) {
            ci.cancel(); // Block growth for infertile crops
            return;
        }

        // 3. Apply probability gate
        double base = HungerOverhaulConfig.getInstance().crops.cropGrowthMultiplier;
        double adjusted = SereneSeasonsAPI.calculateGrowthMultiplier(level, base);

        if (random.nextDouble() > adjusted) {
            ci.cancel(); // Failed random check
            return;
        }

        // All checks passed - allow vanilla growth to proceed
    }
}
```

**Why HEAD injection?**
- Checks conditions before vanilla logic
- Can cancel entirely to prevent growth
- More efficient than RETURN injection
- Avoids complicated state restoration

**Cancellation behavior**:
- `ci.cancel()` stops method execution
- Vanilla `randomTick` never runs
- No growth occurs for that tick
- Random tick will try again later

---

### 2. FoodItemMixin

**Target**: `net.minecraft.world.item.Item`
**Method**: `finishUsingItem(ItemStack, Level, LivingEntity)`
**Injection Point**: `@At("HEAD")` with cancellable
**Platform**: Both Fabric and Forge

**Purpose**: Allow food items to stack properly when eaten (consuming 1 from stack instead of entire stack).

**Problem**: Vanilla Minecraft treats food items specially - when eaten from a stack, the entire stack is consumed or replaced with a crafting remainder.

**Solution**:
```java
@Mixin(Item.class)
public class FoodItemMixin {
    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void onFinishUsingItem(ItemStack stack, Level level,
                                    LivingEntity entity,
                                    CallbackInfoReturnable<ItemStack> cir) {
        // Only process for food items
        if (!stack.isEdible()) return;

        // Only process for players in survival mode on server
        if (!(entity instanceof Player player)) return;
        if (level.isClientSide) return;
        if (player.getAbilities().instabuild) return;

        int originalCount = stack.getCount();

        // Only modify behavior if stack has more than 1 item
        if (originalCount > 1) {
            // Cancel vanilla method
            cir.cancel();

            // Shrink stack by 1
            stack.shrink(1);

            // Apply food effects manually
            if (stack.getItem().getFoodProperties() != null) {
                player.getFoodData().eat(stack.getItem(), stack);
            }

            // Handle crafting remainder (container item)
            Item craftingRemainder = stack.getItem().getCraftingRemainingItem();
            if (craftingRemainder != null) {
                ItemStack containerStack = new ItemStack(craftingRemainder);
                if (!player.getInventory().add(containerStack)) {
                    player.drop(containerStack, false);
                }
            }

            // Return the modified stack
            cir.setReturnValue(stack);
        }
        // For single items, let vanilla handle it normally
    }
}
```

**Key Points**:
- Only affects stacks with 2+ items
- Single items use vanilla behavior (for crafting remainder logic)
- Manually applies food effects via `FoodData.eat()`
- Handles container items (bowls, bottles, buckets)
- Returns modified stack to maintain inventory state

---

### 3. BowlFoodItemMixin

**Target**: `net.minecraft.world.item.BowlFoodItem`
**Method**: `finishUsingItem(ItemStack, Level, LivingEntity)`
**Injection Point**: `@At("HEAD")` with cancellable
**Platform**: Both Fabric and Forge

**Purpose**: Specialized handling for bowl food items (stews, soups).

**Why separate mixin?**
- BowlFoodItem has different crafting remainder behavior
- Needs explicit bowl return
- More specific targeting ensures compatibility

**Implementation**:
```java
@Mixin(BowlFoodItem.class)
public class BowlFoodItemMixin {
    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void onFinishUsingBowlFood(ItemStack stack, Level level,
                                       LivingEntity entity,
                                       CallbackInfoReturnable<ItemStack> cir) {
        if (!(entity instanceof Player player)) return;
        if (level.isClientSide) return;
        if (player.getAbilities().instabuild) return;

        int count = stack.getCount();

        // Only modify if stack has more than 1
        if (count > 1) {
            cir.cancel();

            // Apply food effects BEFORE shrinking
            // (ensures getFoodProperties works correctly)
            if (stack.getItem().getFoodProperties() != null) {
                player.getFoodData().eat(stack.getItem(), stack);
            }

            // Shrink by 1
            stack.shrink(1);

            // Give back a bowl
            ItemStack bowl = new ItemStack(Items.BOWL);
            if (!player.getInventory().add(bowl)) {
                player.drop(bowl, false);
            }

            // Return the original stack (now with count-1)
            cir.setReturnValue(stack);
        }
        // For single items, let vanilla handle it
    }
}
```

**Differences from FoodItemMixin**:
- Applies food effects BEFORE shrinking (order matters for vanilla compat)
- Explicitly returns `Items.BOWL` instead of checking crafting remainder
- Targeted at specific class (more maintainable)

---

### 4. HoneyBottleItemMixin

**Target**: `net.minecraft.world.item.HoneyBottleItem`
**Method**: `finishUsingItem(ItemStack, Level, LivingEntity)`
**Injection Point**: `@At("HEAD")` with cancellable
**Platform**: Both Fabric and Forge

**Purpose**: Allow honey bottles to stack and return glass bottles properly.

**Implementation**: Similar to BowlFoodItemMixin but returns `Items.GLASS_BOTTLE`.

**Special Considerations**:
- Honey bottles clear poison effects (vanilla behavior)
- Must preserve vanilla effect clearing
- Returns glass bottle instead of honey bottle

---

### 5. MilkBucketItemMixin

**Target**: `net.minecraft.world.item.MilkBucketItem`
**Method**: `finishUsingItem(ItemStack, Level, LivingEntity)`
**Injection Point**: `@At("HEAD")` with cancellable
**Platform**: Both Fabric and Forge

**Purpose**: Allow milk buckets to stack and return empty buckets.

**Unique Behavior**:
- Milk buckets clear ALL effects (not just food-related)
- Must preserve vanilla effect clearing
- Returns empty bucket (`Items.BUCKET`)

**Implementation Note**: Milk buckets are NOT food items (not edible) but have similar consumption mechanics, hence the separate mixin.

---

### 6. SuspiciousStewItemMixin

**Target**: `net.minecraft.world.item.SuspiciousStewItem`
**Method**: `finishUsingItem(ItemStack, Level, LivingEntity)`
**Injection Point**: `@At("HEAD")` with cancellable
**Platform**: Both Fabric and Forge

**Purpose**: Handle suspicious stew stacking with random effects.

**Special Considerations**:
- Suspicious stew has NBT data for effects
- Random effects must be preserved per-item
- Each stew in stack may have different effects
- Must handle NBT carefully

**Implementation**: Similar to BowlFoodItemMixin with additional NBT handling.

---

### 7. ItemsMixin

**Target**: `net.minecraft.world.item.Items`
**Method**: `<clinit>` (static initializer)
**Injection Point**: `@At("TAIL")` (after initialization)
**Platform**: Both Fabric and Forge

**Purpose**: Modify stack sizes of container food items after vanilla initialization.

**Why TAIL injection?**
- Items are already initialized by vanilla
- Need to modify after creation but before use
- Static initializer is the right time to change max stack size

**Implementation**:
```java
@Mixin(Items.class)
public class ItemsMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onItemsInit(CallbackInfo ci) {
        HORConfig config = HORConfig.getInstance();
        
        // Modify bowl food items
        setMaxStackSize(Items.MUSHROOM_STEW, config.getBowlStackSize());
        setMaxStackSize(Items.RABBIT_STEW, config.getBowlStackSize());
        setMaxStackSize(Items.BEETROOT_SOUP, config.getBowlStackSize());
        setMaxStackSize(Items.SUSPICIOUS_STEW, config.getBowlStackSize());
        
        // Modify bottle food items
        setMaxStackSize(Items.HONEY_BOTTLE, config.getBottleStackSize());
        
        // Modify bucket food items
        setMaxStackSize(Items.MILK_BUCKET, config.getBucketStackSize());
        
        // Handle modded items via registry scan (if needed)
    }
    
    private static void setMaxStackSize(Item item, int size) {
        try {
            Field maxStackSizeField = Item.class.getDeclaredField("maxStackSize");
            maxStackSizeField.setAccessible(true);
            maxStackSizeField.set(item, size);
        } catch (Exception e) {
            HOReborn.LOGGER.error("Failed to set stack size for " + item, e);
        }
    }
}
```

**Why reflection?**
- `maxStackSize` field is private/final
- Can't be changed via public API
- Reflection necessary for modification
- Uses access widener as fallback (see below)

---

### 8. HORMixinPlugin

**Target**: N/A (Configuration class)
**Platform**: Both Fabric and Forge (same implementation)

**Purpose**: Conditional mixin loading based on configuration or environment.

**Implementation**:
```java
@Slf4j
public class HORMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        // Initialize early config if needed
    }

    @Override
    public String getRefMapperConfig() {
        return null; // Use default
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Conditional mixin loading logic
        
        // Example: Disable crop mixins if growth modifications disabled
        if (mixinClassName.contains("AgeableBlockMixin")) {
            return !HungerOverhaulConfig.getInstance().crops.disableCropGrowthModifications;
        }
        
        return true; // Load by default
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // No-op
    }

    @Override
    public List<String> getMixins() {
        return null; // Use JSON config
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass,
                         String mixinClassName, IMixinInfo mixinInfo) {
        // No-op
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass,
                          String mixinClassName, IMixinInfo mixinInfo) {
        // No-op
    }
}
```

**Use Cases**:
- Conditional mixin loading based on config
- Compatibility checks with other mods
- Platform-specific mixin selection
- Debug logging for mixin application

---

## Mixin Configuration Files

### Fabric Configuration

**File**: `hunger_overhaul_reborn-fabric.mixins.json`

```json
{
  "required": true,
  "package": "org.Netroaki.Main.mixin.fabric",
  "compatibilityLevel": "JAVA_17",
  "plugin": "org.Netroaki.Main.mixin.fabric.HORMixinPlugin",
  "mixins": [
    "AgeableBlockMixin",
    "BowlFoodItemMixin",
    "FoodItemMixin",
    "HoneyBottleItemMixin",
    "ItemsMixin",
    "MilkBucketItemMixin",
    "SuspiciousStewItemMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}
```

### Forge Configuration

**File**: `hunger_overhaul_reborn-forge.mixins.json`

```json
{
  "required": true,
  "package": "org.Netroaki.Main.mixin.forge",
  "compatibilityLevel": "JAVA_17",
  "plugin": "org.Netroaki.Main.mixin.forge.HORMixinPlugin",
  "mixins": [
    "BowlFoodItemMixin",
    "FoodItemMixin",
    "HoneyBottleItemMixin",
    "ItemsMixin",
    "MilkBucketItemMixin",
    "SuspiciousStewItemMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}
```

**Note**: Forge config excludes `AgeableBlockMixin` because crop growth is handled via events instead.

---

## Access Widener

**File**: `hunger_overhaul_reborn.accesswidener`

Access wideners allow accessing private/protected members without reflection:

```
accessWidener v2 named
accessible field net/minecraft/world/item/Item maxStackSize I
accessible field net/minecraft/world/item/Item foodProperties Lnet/minecraft/world/food/FoodProperties;
```

**Benefits**:
- Faster than reflection
- Compile-time checking
- Cleaner code
- Better IDE support

**Registered in**: `fabric.mod.json` and via Loom configuration

---

## Mixin Best Practices

### 1. Injection Point Selection

**@At("HEAD")**:
- Use for pre-checks and cancellation
- Good for gating behavior
- Example: Crop growth checks

**@At("TAIL")**:
- Use for post-processing
- Modify return values with `@ModifyVariable`
- Example: Items static init

**@At("RETURN")**:
- Use when need to modify return value
- Multiple return points need multiple injections
- Example: Calculating modified values

### 2. Cancellable vs Non-Cancellable

**Use `cancellable = true` when**:
- Need to prevent vanilla logic
- Implementing alternative behavior
- Gating functionality

**Don't use cancellable when**:
- Only observing behavior
- Adding side effects
- Modifying non-critical paths

### 3. Platform Differences

**Fabric-specific**:
- More aggressive mixin usage
- Less event system coverage
- Direct bytecode modification preferred

**Forge-specific**:
- Prefer events over mixins when possible
- Mixins for things without events
- More conservative mixin usage

### 4. Compatibility

**Good**:
- Targeted injections (specific methods)
- Early return on checks
- Graceful failure handling

**Bad**:
- Broad `@ModifyVariable` without specificity
- Assuming vanilla behavior
- No null checks

### 5. Debugging Mixins

**Enable Mixin Debug**:
```
-Dmixin.debug=true
-Dmixin.debug.verbose=true
-Dmixin.debug.export=true
```

**Export Transformed Classes**:
Classes will be exported to `.mixin.out/` for inspection

**Check Mixin Application**:
Look for `"Mixin class [class] applied successfully"` in logs

---

## Common Mixin Patterns

### Pattern 1: Conditional Cancellation
```java
@Inject(method = "targetMethod", at = @At("HEAD"), cancellable = true)
private void checkCondition(CallbackInfo ci) {
    if (!shouldProceed()) {
        ci.cancel();
        return;
    }
}
```

### Pattern 2: Modify Return Value
```java
@Inject(method = "targetMethod", at = @At("RETURN"), cancellable = true)
private void modifyReturn(CallbackInfoReturnable<Type> cir) {
    Type originalReturn = cir.getReturnValue();
    Type modified = transform(originalReturn);
    cir.setReturnValue(modified);
}
```

### Pattern 3: Capture Local Variable
```java
@Inject(method = "targetMethod", 
        at = @At(value = "INVOKE", target = "someMethod"),
        locals = LocalCapture.CAPTURE_FAILHARD)
private void captureLocal(CallbackInfo ci, LocalType local1, LocalType local2) {
    // Use captured locals
}
```

### Pattern 4: Modify Field Access
```java
@ModifyVariable(method = "targetMethod", at = @At("STORE"), ordinal = 0)
private int modifyStoredValue(int original) {
    return original * 2;
}
```

---

## Troubleshooting

### Mixin Not Applying

**Symptoms**: Changes don't take effect
**Causes**:
1. Mixin config not registered
2. Target class/method not found
3. Injection point incorrect
4. Mixin plugin returning false

**Solutions**:
1. Check mixin config JSON is correct
2. Verify target method signature matches
3. Use `@At("HEAD")` for testing
4. Enable mixin debug logging

### Mixin Conflict

**Symptoms**: Crash or unexpected behavior
**Causes**:
1. Multiple mods targeting same location
2. Incompatible injection points
3. Order-dependent mixins

**Solutions**:
1. Use `@ModifyArg` instead of `@Redirect`
2. Adjust mixin priority
3. Use mixin plugin for conditional loading

### ClassCastException

**Symptoms**: Cast errors at runtime
**Causes**:
1. Wrong type in injection signature
2. Obfuscation mismatch
3. Version incompatibility

**Solutions**:
1. Verify method signature
2. Check mappings are correct
3. Use version detection

### Performance Issues

**Symptoms**: TPS drops, lag
**Causes**:
1. Mixin in hot path
2. Expensive operations in injection
3. Too many mixin applications

**Solutions**:
1. Move logic outside injection
2. Cache expensive calculations
3. Use event system instead

