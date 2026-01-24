# Eating Speed Parity Fixes

## Overview

Fixed critical parity issues where eating duration, stack sizes, and well-fed effects were using **outdated thresholds** that didn't match the current meal tier system.

## The Problem

The system had **5 meal tiers** based on hunger values:
- RAW_FOOD: 1 hunger
- COOKED_FOOD: 2 hunger
- LIGHT_MEAL: 4 hunger
- AVERAGE_MEAL: 6 hunger
- LARGE_MEAL: 8 hunger

But three calculation methods used **outdated thresholds** from an old system:
- Thresholds: (14, 10, 7, 4) ❌
- Should have been: (8, 6, 4, 2) ✅

## Impact

This caused logical inconsistencies:

### Before Fix
```
1 hunger (RAW_FOOD):      → 16 ticks eating time (same as cooked!)
2 hunger (COOKED_FOOD):   → 16 ticks eating time
4 hunger (LIGHT_MEAL):    → 24 ticks eating time
6 hunger (AVERAGE_MEAL):  → 24 ticks eating time (SAME AS LIGHT!)
8 hunger (LARGE_MEAL):    → 24 ticks eating time (should be longest!)
```

## Solution

Fixed three methods in `FoodEventHandler.java` to use correct thresholds (8, 6, 4, 2):

### 1. getWellFedDuration() [Lines 187-198]

**Before:**
```java
if (foodValue >= 14) return 480;  // Never reached!
if (foodValue >= 10) return 240;
if (foodValue >= 7)  return 120;
if (foodValue >= 4)  return 40;
return 0;
```

**After:**
```java
if (foodValue >= 8)  return 480;  // LARGE_MEAL: 24 seconds
if (foodValue >= 6)  return 240;  // AVERAGE_MEAL: 12 seconds
if (foodValue >= 4)  return 120;  // LIGHT_MEAL: 6 seconds
if (foodValue >= 2)  return 40;   // COOKED_FOOD: 2 seconds
return 0;                         // RAW_FOOD: no effect
```

### 2. getEatingDuration() [Lines 200-212]

**Before:**
```java
if (foodValue >= 14) return 64;
if (foodValue >= 10) return 48;
if (foodValue >= 7)  return 32;
if (foodValue >= 4)  return 24;
return 16;
```

**After:**
```java
if (foodValue >= 8)  return 40;   // LARGE_MEAL: 2.0 seconds
if (foodValue >= 6)  return 32;   // AVERAGE_MEAL: 1.6 seconds
if (foodValue >= 4)  return 24;   // LIGHT_MEAL: 1.2 seconds
if (foodValue >= 2)  return 16;   // COOKED_FOOD: 0.8 seconds
return 12;                        // RAW_FOOD: 0.6 seconds
```

### 3. getStackSizeForFoodValue() [Lines 214-225]

**Before:**
```java
if (foodValue >= 14) return 1;
if (foodValue >= 10) return 4;
if (foodValue >= 7)  return 8;
if (foodValue >= 4)  return 16;
return 32;
```

**After:**
```java
if (foodValue >= 8)  return 1;    // LARGE_MEAL: very filling
if (foodValue >= 6)  return 4;    // AVERAGE_MEAL: filling
if (foodValue >= 4)  return 16;   // LIGHT_MEAL: moderate
if (foodValue >= 2)  return 32;   // COOKED_FOOD: less filling
return 32;                        // RAW_FOOD: not filling
```

## Results

### After Fix
```
RAW_FOOD (1):
  Eating: 12 ticks (0.6 sec)  ← Fastest to eat
  Stack:  32 items             ← Most stackable
  Well-Fed: None               ← No bonus

COOKED_FOOD (2):
  Eating: 16 ticks (0.8 sec)
  Stack:  32 items
  Well-Fed: 40 ticks (2 sec)

LIGHT_MEAL (4):
  Eating: 24 ticks (1.2 sec)
  Stack:  16 items
  Well-Fed: 120 ticks (6 sec)

AVERAGE_MEAL (6):
  Eating: 32 ticks (1.6 sec)
  Stack:  4 items
  Well-Fed: 240 ticks (12 sec)

LARGE_MEAL (8):
  Eating: 40 ticks (2.0 sec)   ← Slowest to eat
  Stack:  1 item               ← Least stackable
  Well-Fed: 480 ticks (24 sec) ← Longest bonus
```

## Gameplay Benefits

1. **Logical Progression**: Higher value foods take longer to eat and stack less
2. **Balanced Economy**: Each tier has distinct characteristics
3. **No Inconsistencies**: AVERAGE_MEAL now clearly takes longer than LIGHT_MEAL
4. **Better Design**: Raw food is fast/cheap, Large meals are slow/rare

## Technical Details

- **File Modified**: `src/main/java/org/Netroaki/Main/handlers/FoodEventHandler.java`
- **Methods Updated**: 3 (getWellFedDuration, getEatingDuration, getStackSizeForFoodValue)
- **Lines Changed**: 187-225
- **Key Change**: Threshold values from (14, 10, 7, 4) → (8, 6, 4, 2)

## Verification

All methods now:
- ✅ Use consistent threshold values
- ✅ Match meal tier system (1, 2, 4, 6, 8)
- ✅ Have proportional progression
- ✅ Include explanatory comments
- ✅ Pass linting checks (pre-existing warnings only)

