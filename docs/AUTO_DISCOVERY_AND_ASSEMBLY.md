# Auto-Discovery and Assembly Required Support

## Overview

Hunger Overhaul Reborn now features **true auto-discovery** for food mods and explicit support for **Assembly Required**.

This means:
- ✅ ANY food mod can be installed alongside our modpack
- ✅ Food items are automatically categorized into meal types
- ✅ No manual module creation needed for new mods
- ✅ Fallback system for unsupported mods

---

## How It Works

### Auto-Discovery System

The `AutoDiscoveryFoodModule` runs **AFTER** all explicit modules to catch any food items that weren't handled by dedicated integration modules.

#### Process Flow
1. **Explicit Modules Load First** (Farmers Delight, Alex's Mobs, etc.)
   - These have specialized rules and optimizations
   - Handle complex items correctly
   - Priority over auto-discovery

2. **Auto-Discovery Scans Registry**
   - Scans all items from mods NOT in the explicit list
   - Filters out non-food items (tools, ores, etc.)
   - Auto-categorizes remaining items

3. **Items Get Categorized By Name Patterns**
   - Recognizes keywords like "cooked", "raw", "pie", "soup", etc.
   - Assigns appropriate meal type and hunger/saturation values
   - Fallback to LIGHT_MEAL for unknown items

#### Meal Type Categories

| Category | Hunger | Saturation | Examples |
|----------|--------|------------|----------|
| **RAW_FOOD** | 1 | 0.05f | Raw meat, berries, crops |
| **COOKED_FOOD** | 2 | 0.1f | Cooked fish, roasted vegetables |
| **LIGHT_MEAL** | 4 | 0.2f | Snacks, simple dishes |
| **AVERAGE_MEAL** | 6 | 0.4f | Sandwiches, burgers, pasta |
| **LARGE_MEAL** | 8 | 0.5f | Soups, complex dishes |

---

## Assembly Required Support

Assembly Required is a construction/building mod with integrated food items.

### Module: `AssemblyRequiredModule`

- **Mod ID**: `assemblytreasure`
- **Auto-discovery**: YES (if not found in explicit modules)
- **Item Sources**: Registry scanning
- **Categorization**: Automatic based on item names

### Usage

The module automatically activates when Assembly Required is detected:

```java
assemblyRequiredModule.init();  // Called in ModIntegrationHandler
```

All food items from Assembly Required will be automatically categorized using the standard pattern matching.

---

## Auto-Categorization Logic

The `FoodCategorizer` uses sophisticated pattern matching to classify items:

### Raw Food Detection
- Contains: "raw", "fresh", "uncooked", "berry", "fruit", "vegetable"
- Does NOT contain cooking terms
- Returns: 1 hunger, 0.05f saturation

### Cooked Food Detection
- Contains: "cooked", "baked", "roasted", "grilled"
- Single ingredient or simple processing
- Returns: 2 hunger, 0.1-0.2f saturation

### Light Meal Detection
- Contains: "snack", "appetizer", "salad", "dip"
- Simple 2-3 ingredient dishes
- Returns: 4 hunger, 0.2-0.3f saturation

### Average Meal Detection
- Contains: "soup", "stew", "curry", "burger", "sandwich", "pizza"
- 4-5 ingredient complexity
- Returns: 6 hunger, 0.4f saturation

### Large Meal Detection
- Contains: "roast", "feast", "casserole", "pot_pie"
- 6+ ingredient complexity or named feasts
- Returns: 8 hunger, 0.5f saturation

---

## Adding Support for Specific Mods

### Option 1: Auto-Discovery (Automatic)
No action needed! The auto-discovery system will handle it.

### Option 2: Custom Module (Better Control)
Create a dedicated module for more control:

```java
// src/main/java/org/Netroaki/Main/modules/integration/YourModModule.java
public class YourModModule {
    private static final String MOD_ID = "yourmod";
    
    public void init() {
        if (!Platform.isModLoaded(MOD_ID)) return;
        
        // Apply custom food logic here
        // You can override auto-categorization for specific items
    }
}
```

Then register it in `ModIntegrationHandler`:
1. Add field: `private final YourModModule yourModModule = new YourModModule();`
2. Call in `init()`: `yourModModule.init();`
3. Add to explicit mod IDs in `populateExplicitModIds()`

---

## Configuration

The auto-discovery respects the config setting:

```json
{
  "food": {
    "modifyFoodValues": true  // Enable/disable all food modifications
  }
}
```

When disabled, auto-discovery does nothing.

---

## Troubleshooting

### Items Not Being Categorized
1. Check if item name contains food-related keywords
2. Verify the item is edible (has FoodProperties)
3. Check logs for filtering reasons
4. Create a custom module for more control

### Wrong Categorization
1. Create a custom module to override specific items
2. Use `FoodRegistry.setFoodValues()` to manually set values
3. Report to dev for pattern improvements

### Performance Impact
- Auto-discovery runs once at startup
- Minimal impact (~50-100ms for typical modpacks)
- Scales with number of items in registry

---

## Implementation Details

### Classes Involved
- `AutoDiscoveryFoodModule.java` - Main auto-discovery engine
- `AssemblyRequiredModule.java` - Assembly Required integration
- `FoodCategorizer.java` - Pattern matching logic
- `ModIntegrationHandler.java` - Central coordinator

### Key Methods
```java
// Auto-categorize item by name
FoodValue categorizeFood(String itemName)

// Check if item should be skipped
boolean shouldSkipItem(String itemName, String namespace)

// Check if name contains food keywords
boolean hasFoodKeywords(String lower)
```

---

## Future Enhancements

Possible improvements:
- [ ] Machine learning-based categorization
- [ ] Recipe complexity analysis
- [ ] Player preference profiles
- [ ] Difficulty-based hunger scaling
- [ ] Mod-specific balance presets

