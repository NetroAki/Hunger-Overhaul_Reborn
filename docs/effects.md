# Effects System

Documentation for the custom status effects in Hunger Overhaul Reborn.

## Overview

Hunger Overhaul Reborn introduces two custom status effects:

1. **Well Fed** - A beneficial effect gained from eating food
2. **Hungry** - A harmful effect applied at low hunger levels

Both effects are registered using Architectury's registry system and work on both Fabric and Forge.

## Effect Registration

**Location**: `org.Netroaki.Main.HOReborn`

```java
public static final DeferredRegister<MobEffect> EFFECTS = 
    DeferredRegister.create(MOD_ID, Registries.MOB_EFFECT);

public static final RegistrySupplier<MobEffect> WELL_FED_EFFECT = 
    EFFECTS.register("well_fed", WellFedEffect::new);
    
public static final RegistrySupplier<MobEffect> HUNGRY_EFFECT = 
    EFFECTS.register("hungry", HungryEffect::new);
```

Registration happens during mod initialization in `HOReborn.init()`.

## Well Fed Effect

### Overview

**File**: `org.Netroaki.Main.effects.WellFedEffect`

A beneficial effect that provides temporary health regeneration after eating food.

### Properties

| Property | Value |
|----------|-------|
| **ID** | `hunger_overhaul_reborn:well_fed` |
| **Category** | Beneficial |
| **Color** | 0xFFD700 (Gold) |
| **Icon** | Bread item |
| **Translatable** | Yes |

### Mechanics

#### Application
- Applied automatically when eating food (if `enableWellFedEffect` is true)
- Duration scales with food value
- Amplifier increases with each consumption (max 3)
- Stacks duration, not amplifier initially

**Duration Calculation**:
```java
Food Value → Duration (ticks/seconds)
14+:  480 ticks (24 seconds)
10-13: 240 ticks (12 seconds)
7-9:  120 ticks (6 seconds)
4-6:   40 ticks (2 seconds)
1-3:    0 ticks (no effect)
```

**Amplifier Stacking**:
```java
Each food consumption:
- Adds duration to existing effect
- Increases amplifier by 1 (capped at 3)
- Resets on amplifier upgrade

Example:
1st food: Well Fed I (amplifier 0) - 240 ticks
2nd food: Well Fed II (amplifier 1) - 240 + 240 = 480 ticks
3rd food: Well Fed III (amplifier 2) - 480 + 240 = 720 ticks
4th food: Well Fed IV (amplifier 3) - 720 + 240 = 960 ticks (max)
```

#### Regeneration Pulse

When the effect is applied or amplifier increases, a **1-second regeneration pulse** occurs:

```java
Duration: 20 ticks (1 second)
Heal Timing: Every 10 ticks
Total Healing: 1.0F health (0.5 hearts × 2)

Timeline:
Tick 0:  Effect applied/upgraded
Tick 10: Heal 0.5 hearts
Tick 20: Heal 0.5 hearts (pulse complete)
```

**Implementation**:
```java
// In PlayerEventHandler.onPlayerTick()
MobEffectInstance wf = player.getEffect(WELL_FED_EFFECT);
if (wf != null) {
    int amp = wf.getAmplifier();
    Integer lastAmp = lastWellFedAmplifier.get(player.getUUID());
    
    if (lastAmp == null || lastAmp != amp) {
        // Amplifier changed - start pulse
        wellFedRegenTicksRemaining.put(player.getUUID(), 20);
        lastWellFedAmplifier.put(player.getUUID(), amp);
    }
    
    Integer ticks = wellFedRegenTicksRemaining.get(player.getUUID());
    if (ticks != null && ticks > 0) {
        if (ticks % 10 == 0 && player.getHealth() < player.getMaxHealth()) {
            player.heal(1.0F); // 0.5 hearts
        }
        wellFedRegenTicksRemaining.put(player.getUUID(), ticks - 1);
    }
}
```

### Visual Representation

**Effect Icon**: Shows bread icon with gold particles
**HUD Display**: Shows effect name and duration
**Particles**: Gold sparkles around player

### Configuration

```json
{
  "food": {
    "enableWellFedEffect": true
  }
}
```

### Code Reference

**Class**: `WellFedEffect`
```java
public class WellFedEffect extends MobEffect {
    public WellFedEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);
    }

    @Override
    public String getDescriptionId() {
        return "effect.hunger_overhaul_reborn.well_fed";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.BREAD);
    }
}
```

**Application**: `FoodEventHandler.onFoodConsumed()`
**Regeneration**: `PlayerEventHandler.onPlayerTick()`

---

## Hungry Effect

### Overview

**File**: `org.Netroaki.Main.effects.HungryEffect`

A harmful effect applied at low hunger levels that debuffs the player's capabilities.

### Properties

| Property | Value |
|----------|-------|
| **ID** | `hunger_overhaul_reborn:hungry` |
| **Category** | Harmful |
| **Color** | 0x8B4513 (Brown) |
| **Icon** | Custom texture |
| **Translatable** | Yes |
| **Attribute Modifiers** | Movement Speed, Attack Damage, Attack Speed |

### Mechanics

#### Application

Automatically applied when hunger drops below threshold:

| Hunger Level | Effect | Amplifier |
|--------------|--------|-----------|
| ≤6 | Hungry I | 0 |
| ≤4 | Hungry II | 1 |
| ≤2 | Hungry III | 2 |
| >6 | None | - |

**Duration**: Infinite (Integer.MAX_VALUE) - reapplied every tick while conditions met

**Implementation**:
```java
// In PlayerEventHandler.onPlayerTick()
int foodLevel = player.getFoodData().getFoodLevel();

int desiredAmplifier = -1;
if (foodLevel <= 2) desiredAmplifier = 2;
else if (foodLevel <= 4) desiredAmplifier = 1;
else if (foodLevel <= 6) desiredAmplifier = 0;

if (desiredAmplifier >= 0) {
    MobEffectInstance current = player.getEffect(HUNGRY_EFFECT);
    if (current == null || current.getAmplifier() != desiredAmplifier) {
        player.removeEffect(HUNGRY_EFFECT);
        player.addEffect(new MobEffectInstance(
            HUNGRY_EFFECT, 
            Integer.MAX_VALUE,
            desiredAmplifier,
            false, // ambient
            true,  // visible
            true   // show icon
        ));
    }
} else {
    player.removeEffect(HUNGRY_EFFECT);
}
```

#### Attribute Modifiers

The Hungry effect applies three attribute modifiers:

**1. Movement Speed** (UUID: `7107DE5E-7CE8-4030-940E-514C1F160890`)
- Operation: MULTIPLY_TOTAL
- Value: -0.15 (15% slower)
- Scales with amplifier

**2. Attack Damage** (UUID: `7107DE5E-7CE8-4030-940E-514C1F160891`)
- Operation: ADDITION
- Value: -2.0 (2 less damage)
- Scales with amplifier

**3. Attack Speed** (UUID: `7107DE5E-7CE8-4030-940E-514C1F160892`)
- Operation: MULTIPLY_TOTAL
- Value: -0.10 (10% slower attacks)
- Scales with amplifier

**Amplifier Scaling**:
```java
// Attribute modifiers are multiplied by (amplifier + 1)

Hungry I (amplifier 0):
- Movement: -15% × 1 = -15%
- Attack Damage: -2.0 × 1 = -2.0
- Attack Speed: -10% × 1 = -10%

Hungry II (amplifier 1):
- Movement: -15% × 2 = -30%
- Attack Damage: -2.0 × 2 = -4.0
- Attack Speed: -10% × 2 = -20%

Hungry III (amplifier 2):
- Movement: -15% × 3 = -45%
- Attack Damage: -2.0 × 3 = -6.0
- Attack Speed: -10% × 3 = -30%
```

#### Mining Fatigue (Amplifier 2+)

At **Hungry III** (amplifier 2), an additional **Mining Fatigue** effect is applied:

**Fabric Implementation**:
```java
// In HORebornFabric.registerFabricEvents()
ServerTickEvents.END_SERVER_TICK.register(server -> {
    for (ServerPlayer player : server.getPlayerList().getPlayers()) {
        var hungry = HOReborn.HUNGRY_EFFECT.get();
        var inst = player.getEffect(hungry);
        
        if (inst != null && inst.getAmplifier() >= 2) {
            // Apply hidden Mining Fatigue I
            player.addEffect(new MobEffectInstance(
                MobEffects.DIG_SLOWDOWN, // Mining Fatigue
                20, // 1 second duration (refreshed every tick)
                0,  // Amplifier I
                false, // Not ambient
                false, // Not visible
                false  // No icon
            ));
        } else {
            player.removeEffect(MobEffects.DIG_SLOWDOWN);
        }
    }
});
```

**Forge Implementation**:
```java
// In HORebornForge.onBreakSpeed()
@SubscribeEvent
public void onBreakSpeed(BreakSpeed event) {
    var effect = HOReborn.HUNGRY_EFFECT.get();
    if (event.getEntity() != null && event.getEntity().hasEffect(effect)) {
        var inst = event.getEntity().getEffect(effect);
        if (inst != null && inst.getAmplifier() >= 2) {
            // Reduce mining speed by 50%
            event.setNewSpeed(event.getNewSpeed() * 0.5f);
        }
    }
}
```

### Visual Representation

**Effect Icon**: Custom brown texture (hungry.png)
**HUD Display**: Shows effect name and amplifier
**Particles**: Brown particles around player

### Configuration

```json
{
  "hunger": {
    "lowHungerEffects": true,
    "lowHungerSlownessThreshold": 6,
    "lowHungerWeaknessThreshold": 4
  }
}
```

**Note**: Thresholds are defined but currently hardcoded in implementation (6, 4, 2).

### Code Reference

**Class**: `HungryEffect`
```java
public class HungryEffect extends MobEffect {
    private static final UUID MOVEMENT_SPEED_UUID = 
        UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890");
    private static final UUID ATTACK_DAMAGE_UUID = 
        UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160891");
    private static final UUID ATTACK_SPEED_UUID = 
        UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160892");

    public HungryEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B4513);
        
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
            MOVEMENT_SPEED_UUID.toString(), -0.15D, 
            AttributeModifier.Operation.MULTIPLY_TOTAL);
            
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
            ATTACK_DAMAGE_UUID.toString(), -2.0D,
            AttributeModifier.Operation.ADDITION);
            
        this.addAttributeModifier(Attributes.ATTACK_SPEED,
            ATTACK_SPEED_UUID.toString(), -0.10D,
            AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // No tick effects - behavior driven by attributes and events
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }

    @Override
    public String getDescriptionId() {
        return "effect.hunger_overhaul_reborn.hungry";
    }
}
```

**Application**: `PlayerEventHandler.onPlayerTick()`
**Mining Fatigue**: Platform-specific implementations

---

## Effect Interactions

### Well Fed + Hungry

Both effects can coexist on a player:

```java
// Possible scenario:
Player has Hungry I (hunger ≤6)
Player eats large meal
Player gains Well Fed II

Result:
- Hungry I still active (movement/attack debuffs)
- Well Fed II provides regeneration pulse
- Once hunger restored >6, Hungry removed
- Well Fed continues until duration expires
```

### Effect Priority

1. **Hungry** takes priority for attribute modifications (always applied at low hunger)
2. **Well Fed** provides independent regeneration (not affected by Hungry)
3. **Mining Fatigue** only applies at Hungry III

### Removing Effects

**Commands**:
```
/effect clear @p hunger_overhaul_reborn:well_fed
/effect clear @p hunger_overhaul_reborn:hungry
```

**Milk Bucket**:
- Removes both Well Fed and Hungry
- Follows vanilla behavior
- Can be used to clear negative effects

**Death**:
- All effects cleared on death
- Not reapplied on respawn
- Hungry will reapply if hunger is low

## Localization

**File**: `assets/hunger_overhaul_reborn/lang/en_us.json`

```json
{
  "effect.hunger_overhaul_reborn.well_fed": "Well Fed",
  "effect.hunger_overhaul_reborn.hungry": "Hungry"
}
```

Add translations in other language files as needed.

## Effect Textures

**Location**: `assets/hunger_overhaul_reborn/textures/mob_effect/`

- `well_fed.png` - Golden/yellow food icon
- `hungry.png` - Brown/dark empty plate icon

**Format**: 18×18 PNG with transparency
**Registered**: Via `assets/hunger_overhaul_reborn/atlases/mob_effects.json`

## Testing Effects

### Debug Commands

Use the built-in command to set hunger for testing:

```
/ho_hunger 6     # Set hunger to 6 (triggers Hungry I)
/ho_hunger 4 0   # Set hunger to 4, saturation to 0 (Hungry II)
/ho_hunger 2     # Set hunger to 2 (Hungry III + Mining Fatigue)
```

### Effect Commands

```
# Apply Well Fed manually
/effect give @p hunger_overhaul_reborn:well_fed 60 0

# Apply Hungry manually
/effect give @p hunger_overhaul_reborn:hungry 60 2

# Remove effects
/effect clear @p hunger_overhaul_reborn:well_fed
/effect clear @p hunger_overhaul_reborn:hungry
```

### Testing Scenarios

1. **Well Fed Stacking**:
   - Set hunger to 20
   - Eat 4 large meals in succession
   - Verify amplifier increases to 3
   - Verify healing pulses occur

2. **Hungry Progression**:
   - Set hunger to 7 (no effect)
   - Set hunger to 6 (Hungry I)
   - Set hunger to 4 (Hungry II)
   - Set hunger to 2 (Hungry III + Mining Fatigue)
   - Verify attribute changes at each level

3. **Mining Fatigue**:
   - Get Hungry III
   - Try to mine blocks
   - Verify mining is significantly slower

## Performance Considerations

### Well Fed
- Tracks UUID-based state for regeneration pulses
- Cleans up state when effect expires
- Only heals if below max health (skip check)

### Hungry
- Checks hunger level every player tick
- Only updates effect if amplifier changed (avoids spam)
- Efficient removal when hunger restored

### Mining Fatigue
- **Fabric**: Applied every server tick (slight overhead)
- **Forge**: Only checked on break speed event (more efficient)
- Hidden effect (no visual spam)

## Future Enhancements

Potential improvements:

1. **Configurable thresholds** for Hungry amplifiers
2. **Visual indicators** for effect strength
3. **Sound effects** when effects applied/removed
4. **Particle customization** per amplifier
5. **Additional benefits** for high Well Fed amplifiers
6. **Hunger restoration** bonus from Well Fed
7. **Custom tooltips** explaining effect mechanics

