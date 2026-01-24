# Data Dump and Assembly Required Integration

## Overview

Hunger Overhaul Reborn uses the **data_dump** from Minecraft modpacks to discover and auto-categorize food items from any mod, including Assembly Required.

---

## Data Dump Structure

The data_dump is generated from a running modpack and contains metadata about all items:

```
data_dump/
├── item/               # All item definitions by mod
│   ├── minecraft.txt
│   ├── farmersdelight.txt
│   ├── alexsmobs.txt
│   ├── aquaculture.txt
│   ├── assemblytreasure.txt    ← Assembly Required items
│   ├── createfood.txt
│   └── ... (other mods)
├── block/
├── entity_type/
├── tag/
└── ... (other metadata)
```

---

## Assembly Required in Data Dump

When Assembly Required is installed, food items appear in:

```
data_dump/dump/item/assemblytreasure.txt
```

### Expected Format

```
assemblytreasure:raw_ingredient
assemblytreasure:cooked_ingredient
assemblytreasure:simple_meal
assemblytreasure:prepared_dish
assemblytreasure:special_food
... (more items)
```

Each line is an item ID in the format: `modid:itemname`

---

## How Auto-Discovery Uses Data Dump

### Process Flow

1. **Registry Scanning** (Runtime)
   ```java
   for (Item item : BuiltInRegistries.ITEM) {
       String itemId = item.toString();  // "assemblytreasure:cooked_beef"
       String namespace = "assemblytreasure";
       String itemName = "cooked_beef";
   }
   ```

2. **Item Filtering**
   - Skip if namespace is in explicit modules list
   - Skip if item name contains tech keywords (ore, circuit, tool, etc.)
   - Keep if name has food keywords

3. **Pattern Matching**
   - Analyze item name for food indicators
   - Detect cooking method (cooked, baked, roasted, etc.)
   - Estimate complexity from keywords

4. **Categorization**
   - Assign meal type (RAW_FOOD, COOKED_FOOD, etc.)
   - Apply hunger/saturation values
   - Store in FoodRegistry

---

## Assembly Required Food Items

### Expected Items to be Auto-Discovered

Assembly Required likely includes:

#### Raw Ingredients
```
assemblytreasure:raw_ingredient
assemblytreasure:raw_meat
assemblytreasure:fresh_produce
```
→ Categorized as: **RAW_FOOD** (1 hunger, 0.05f saturation)

#### Processed/Cooked Items
```
assemblytreasure:cooked_meat
assemblytreasure:roasted_vegetable
assemblytreasure:seasoned_ingredient
```
→ Categorized as: **COOKED_FOOD** (2 hunger, 0.1f saturation)

#### Simple Meals
```
assemblytreasure:basic_meal
assemblytreasure:quick_lunch
assemblytreasure:simple_dish
```
→ Categorized as: **LIGHT_MEAL** (4 hunger, 0.2f saturation)

#### Complex Dishes
```
assemblytreasure:prepared_dinner
assemblytreasure:gourmet_meal
assemblytreasure:special_recipe
```
→ Categorized as: **AVERAGE_MEAL** (6 hunger, 0.4f saturation)

#### Feasts
```
assemblytreasure:feast_meal
assemblytreasure:grand_dinner
assemblytreasure:celebration_meal
```
→ Categorized as: **LARGE_MEAL** (8 hunger, 0.5f saturation)

---

## How to Get/Generate Data Dump

### Method 1: From Existing Modpack

If your modpack has Assembly Required installed:

1. Launch modpack with all mods
2. Generate data dump using datapack generator
3. Files appear in: `minecraft/data_dump/dump/item/assemblytreasure.txt`

### Method 2: Manual Addition

Add to data_dump manually:

```bash
# Create the file
mkdir -p data_dump/dump/item/
echo "assemblytreasure:item1" >> data_dump/dump/item/assemblytreasure.txt
echo "assemblytreasure:item2" >> data_dump/dump/item/assemblytreasure.txt
```

---

## Integration in Hunger Overhaul Reborn

### AssemblyRequiredModule

```java
public class AssemblyRequiredModule {
    private static final String MOD_ID = "assemblytreasure";
    
    public void init() {
        // 1. Check if mod is loaded
        if (!Platform.isModLoaded(MOD_ID)) return;
        
        // 2. Scan registry for items
        for (Item item : BuiltInRegistries.ITEM) {
            // 3. Filter and categorize
            if (isAssemblyRequiredFood(item)) {
                FoodValue value = FoodCategorizer.categorizeFood(itemName);
                
                // 4. Register food values
                FoodRegistry.setFoodValues(itemId, value);
            }
        }
    }
}
```

### Auto-Discovery Fallback

If AssemblyRequired items are not explicitly handled:

```java
public class AutoDiscoveryFoodModule {
    public void init(Set<String> explicitModIds) {
        // 1. Get all mods except explicit ones
        for (Item item : BuiltInRegistries.ITEM) {
            String namespace = extractNamespace(item);
            
            // 2. Skip if already handled
            if (explicitModIds.contains(namespace)) continue;
            
            // 3. Auto-categorize
            FoodValue value = FoodCategorizer.categorizeFood(itemName);
            
            // 4. Register
            FoodRegistry.setFoodValues(itemId, value);
        }
    }
}
```

---

## Keyword Detection in Assembly Required Items

### Auto-Categorization Examples

| Item Name | Keywords Found | Category | Hunger |
|-----------|---------------|---------|----|
| `raw_ingredient` | raw | RAW_FOOD | 1 |
| `cooked_beef` | cooked, beef | COOKED_FOOD | 2 |
| `fruit_salad` | fruit, salad | LIGHT_MEAL | 4 |
| `hearty_stew` | stew, hearty | AVERAGE_MEAL | 6 |
| `grand_feast` | feast, grand | LARGE_MEAL | 8 |

### Pattern Matching Logic

```java
String itemName = "assemblytreasure:roasted_vegetable_medley";
String lower = itemName.toLowerCase();

// Check patterns in order:
if (lower.contains("raw") || lower.contains("fresh")) {
    // RAW_FOOD
}
else if (lower.contains("roasted") || lower.contains("cooked")) {
    // COOKED_FOOD
}
else if (lower.contains("salad") || lower.contains("appetizer")) {
    // LIGHT_MEAL
}
else if (lower.contains("stew") || lower.contains("soup")) {
    // AVERAGE_MEAL
}
else if (lower.contains("feast") || lower.contains("roast")) {
    // LARGE_MEAL
}
```

---

## Configuration and Customization

### Config File

```json
{
  "food": {
    "modifyFoodValues": true,           // Enable all modifications
    "foodHungerDivider": 1.0,           // Divider for hunger values
    "foodHungerToSaturationDivider": 2.0 // Saturation calculation
  }
}
```

### Override Assembly Required Items

To override specific Assembly Required items:

```java
// In ModIntegrationHandler or custom module
FoodRegistry.setFoodValues(
    "assemblytreasure:special_item",
    new FoodValueData(
        hunger: 6,
        saturation: 0.4f,
        category: "AVERAGE_MEAL"
    )
);
```

---

## Checking Auto-Discovery Results

### In Server Logs

```
[INFO] Starting auto-discovery for unsupported food mods...
[INFO] Auto-discovery: Categorized 15 items from 3 unsupported mods
[INFO] Processed mods: assemblytreasure, randomfoodmod, etc.
```

### In Registry

After initialization, check FoodRegistry:

```java
FoodValueData data = FoodRegistry.getFoodValues(
    new ItemStack(assemblyRequiredItem)
);

System.out.println("Hunger: " + data.hunger);           // e.g., 4
System.out.println("Saturation: " + data.saturation);   // e.g., 0.2f
System.out.println("Category: " + data.category);       // e.g., LIGHT_MEAL
```

---

## Troubleshooting Assembly Required Integration

### Problem: Items Not Found
**Cause**: Mod not installed or items not in registry
**Solution**: 
1. Verify Assembly Required is installed
2. Check mod ID is correct: "assemblytreasure"
3. Ensure items have FoodProperties

### Problem: Wrong Categorization
**Cause**: Item name doesn't match patterns
**Solution**:
1. Add custom override in module
2. Report pattern to dev
3. Use FoodRegistry.setFoodValues() for specific items

### Problem: No Items Discovered
**Cause**: MOD_ID not in explicit list or auto-discovery disabled
**Solution**:
1. Add to explicitModIds if using explicit module
2. Enable auto-discovery in config
3. Check server logs for errors

---

## Data Flow Diagram

```
Assembly Required Installed
         ↓
    Registry Update
         ↓
  Server Startup
         ↓
  AssemblyRequiredModule.init()
         ↓
  Scan Registry for Items
         ↓
  Extract Item Names
         ↓
  Filter Non-Food Items
         ↓
  Pattern Match Keywords
         ↓
  Categorize into Meal Types
         ↓
  Register in FoodRegistry
         ↓
  ✅ Ready to Use!
```

---

## Advanced: Extending for Other Mods

### Process for New Mods

1. **Identify Mod ID**
   ```
   In data_dump/dump/item/[modid].txt
   ```

2. **Add to Auto-Discovery**
   ```
   Automatically handled by AutoDiscoveryFoodModule
   ```

3. **Or Create Custom Module**
   ```java
   public class MyModModule {
       private static final String MOD_ID = "mymod";
       public void init() { /* ... */ }
   }
   ```

4. **Register in ModIntegrationHandler**
   ```java
   private final MyModModule myModModule = new MyModModule();
   // ... init call
   // ... explicit mod ID registration
   ```

---

## Summary

✅ **Assembly Required food items** are automatically discovered and categorized

✅ **Data dump** provides item metadata for accurate matching

✅ **Auto-discovery** handles any food mod without explicit modules

✅ **Pattern matching** detects food types from item names

✅ **Categorization** assigns appropriate hunger/saturation values

✅ **Fallback system** ensures no food items are missed

🎉 **Result**: Works out-of-the-box with any food mod!

