package org.Netroaki.Main.modules.integration;

import org.Netroaki.Main.util.FoodCategorizer;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Example of a specialized food module that uses FoodCategorizer with custom overrides.
 * 
 * This demonstrates:
 * 1. Using FoodCategorizer for most items
 * 2. Adding custom categorization for specific items
 * 3. Filtering out non-food items
 * 4. Adjusting saturation for specific item categories
 */
public class ExampleSpecializedFoodModule {
    
    /**
     * Example: Categorize a single food item using the automatic system
     */
    public static void categorizeSingleItem(String itemName) {
        FoodCategorizer.FoodValue foodValue = FoodCategorizer.categorizeFood(itemName);
        
        System.out.println("Item: " + itemName);
        System.out.println("  Hunger: " + foodValue.hunger);
        System.out.println("  Saturation: " + foodValue.saturation);
        System.out.println("  Meal Type: " + foodValue.mealType);
    }
    
    /**
     * Example: Apply custom categorization for specific items while using auto-categorization for others
     */
    public static void applyMixedCategorization(String modId, String modName) {
        // First, apply auto-categorization for all items
        // (This would be done in the actual module)
        
        // Then override specific items with custom logic
        String prefix = modId + ":";
        
        // Example overrides for a hypothetical "FancyFood" mod
        applyCustomFoodValue(prefix + "legendary_feast", 10, 0.6f, "SPECIAL_FEAST");
        applyCustomFoodValue(prefix + "raw_ingredient", 1, 0.05f, "RAW_FOOD");
        applyCustomFoodValue(prefix + "moldy_food", 1, 0.02f, "SPOILED_FOOD");
    }
    
    /**
     * Apply a custom food value override
     */
    private static void applyCustomFoodValue(String itemId, int hunger, float saturation, String category) {
        FoodRegistry.setFoodValues(itemId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, category
        ));
    }
    
    /**
     * Example: Show how to adjust saturation based on item characteristics
     */
    public static float adjustSaturationByQuality(String itemName) {
        String lower = itemName.toLowerCase();
        
        // Premium items get slightly higher saturation
        if (containsAny(lower, "premium", "deluxe", "gourmet", "supreme", "ultimate")) {
            return 0.1f; // Add 0.1f bonus
        }
        
        // Poor quality items get reduced saturation
        if (containsAny(lower, "stale", "old", "moldy", "rotten", "spoiled")) {
            return -0.05f; // Reduce by 0.05f
        }
        
        // Special ingredients with lower satisfaction
        if (containsAny(lower, "candy", "treat", "sweet", "junk")) {
            return 0.0f; // No adjustment
        }
        
        return 0.0f; // No adjustment for normal items
    }
    
    /**
     * Demonstrate the categorizer on various item names
     */
    public static void demonstrateCategorization() {
        String[] testItems = {
            // Raw items
            "raw_beef",
            "fresh_apple",
            "wheat_seed",
            
            // Processed single ingredients
            "tomato_sauce",
            "cinnamon_powder",
            "butter",
            
            // Light meals
            "apple_juice",
            "chocolate_cookie",
            "grilled_cheese_sandwich",
            "chicken_nuggets",
            "berry_pie_slice",
            
            // Average meals
            "beef_stew",
            "mushroom_pasta",
            "deluxe_burger",
            "chicken_soup",
            "spaghetti_carbonara",
            
            // Large meals
            "gourmet_dinner_platter",
            "full_breakfast_plate",
            "ultimate_feast"
        };
        
        System.out.println("=== FOOD CATEGORIZATION EXAMPLES ===\n");
        
        for (String item : testItems) {
            FoodCategorizer.FoodValue value = FoodCategorizer.categorizeFood(item);
            System.out.printf("%-35s | Hunger: %d | Saturation: %.2f | Type: %s%n",
                item, value.hunger, value.saturation, value.mealType);
        }
    }
    
    /**
     * Utility: Check if text contains any of the given tokens
     */
    private static boolean containsAny(String text, String... tokens) {
        for (String token : tokens) {
            if (token != null && !token.isEmpty() && text.contains(token)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Main method for testing (remove in production)
     */
    public static void main(String[] args) {
        demonstrateCategorization();
    }
}

