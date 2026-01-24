package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Create: Food mod.
 * Handles food value modifications for Create food items.
 * Create: Food adds 662 food items with various complexity levels.
 * 
 * Note: Due to the large number of items (662), we use runtime registry scanning
 * to apply food values based on item name patterns.
 */
public class CreateFoodModule {

    private static final String CREATE_FOOD_MOD_ID = "createfood";
    private static boolean createFoodLoaded = false;

    /**
     * Initialize Create: Food integration.
     */
    public void init() {
        createFoodLoaded = Platform.isModLoaded(CREATE_FOOD_MOD_ID);

        if (!createFoodLoaded) {
            HOReborn.LOGGER.debug("Create: Food not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Create: Food integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Create: Food items.
     * Since Create: Food has 662 items, we scan the registry and apply values based on patterns.
     */
    private void applyFoodValueModifications() {
        int registeredCount = 0;
        
        // Scan all items in the registry for createfood mod
        // Note: BuiltInRegistries.ITEM is deprecated, but we need it for registry scanning
        @SuppressWarnings("deprecation")
        var itemRegistry = BuiltInRegistries.ITEM;
        for (ResourceLocation location : itemRegistry.keySet()) {
            if (location.getNamespace().equals(CREATE_FOOD_MOD_ID)) {
                String itemName = location.getPath();
                int hunger = getHungerForItem(itemName);
                float saturation = getSaturationForItem(itemName, hunger);
                
                setCreateFoodValue(itemName, hunger, saturation);
                registeredCount++;
            }
        }
        
        HOReborn.LOGGER.info("Registered {} Create: Food items with food values", registeredCount);
    }
    
    /**
     * Determine hunger value based on item name patterns.
     */
    private int getHungerForItem(String itemName) {
        // Very simple base items (1 hunger)
        if (itemName.contains("_base") || itemName.contains("_hole") && !itemName.contains("donut_hole_sugar")) {
            return 1;
        }
        
        // Ice cream items (2-4 hunger)
        if (itemName.contains("_ice_cream")) {
            if (itemName.contains("_bowl") || itemName.contains("_sandwich")) {
                return 4; // Ice cream bowl/sandwich = LIGHT_MEAL (was 3)
            }
            return 2; // Simple ice cream
        }
        
        // Pies and cakes (4 hunger)
        if (itemName.contains("_pie_slice") || itemName.contains("_cake_slice") || itemName.contains("_cheesecake_slice")) {
            return 4; // Pie/cake slice = LIGHT_MEAL (was 3)
        }
        
        // Pastries - frosted/glazed items (4 hunger)
        if (itemName.contains("frosted") || itemName.contains("glazed")) {
            if (itemName.contains("_cupcake") || itemName.contains("_waffle") || itemName.contains("_sweet_roll") || itemName.contains("_donut")) {
                return 4; // Frosted pastry = LIGHT_MEAL (was 3)
            }
        }
        
        // Sandwiches (4 hunger)
        if (itemName.contains("_sandwich") || itemName.contains("_jam_sandwich")) {
            return 4;
        }
        
        // Pizzas (4 hunger)
        if (itemName.contains("_pizza_slice")) {
            return 4;
        }
        
        // Calzones (6 hunger)
        if (itemName.contains("_calzone")) {
            return 6; // Calzone = AVERAGE_MEAL (was 5)
        }
        
        // Bread slices with toppings (2-4 hunger)
        if (itemName.startsWith("bread_slice_")) {
            // Count underscores to determine complexity
            long underscoreCount = itemName.chars().filter(ch -> ch == '_').count();
            if (underscoreCount >= 4) {
                return 4; // Complex bread slice = LIGHT_MEAL
            } else if (underscoreCount >= 3) {
                return 4; // Moderate bread slice = LIGHT_MEAL (was 3)
            }
            return 2; // Simple slice = COOKED_FOOD
        }
        
        // Buns (5-6 hunger)
        if (itemName.contains("_bun")) {
            // Count ingredients
            int ingredientCount = 0;
            if (itemName.contains("bacon")) ingredientCount++;
            if (itemName.contains("lettuce")) ingredientCount++;
            if (itemName.contains("tomato")) ingredientCount++;
            if (itemName.contains("onion")) ingredientCount++;
            if (itemName.contains("cheese")) ingredientCount++;
            if (itemName.contains("peanut_butter")) ingredientCount++;
            
            if (itemName.contains("cheese_and_")) {
                return 6; // Complex sandwich = AVERAGE_MEAL
            } else if (ingredientCount >= 3) {
                return 6; // Many ingredients = AVERAGE_MEAL
            } else if (ingredientCount >= 2) {
                return 4; // 2 ingredients = LIGHT_MEAL (was 5)
            }
            return 4; // Simple sandwich = LIGHT_MEAL (was 5)
        }
        
        // Burgers (5-7 hunger)
        if (itemName.contains("burger") || itemName.equals("hamburger") || itemName.equals("cheeseburger")) {
            if (itemName.contains("cheeseburger")) {
                // Count toppings
                int toppingCount = 0;
                if (itemName.contains("bacon")) toppingCount++;
                if (itemName.contains("lettuce")) toppingCount++;
                if (itemName.contains("tomato")) toppingCount++;
                if (itemName.contains("onion")) toppingCount++;
                
                if (toppingCount >= 3) {
                    return 8; // Deluxe cheeseburger = LARGE_MEAL (was 7)
                }
                return 6; // Standard cheeseburger = AVERAGE_MEAL
            }
            return 4; // Basic burger = LIGHT_MEAL (was 5)
        }
        
        // Baked potatoes (4-6 hunger)
        if (itemName.contains("baked_potato")) {
            long underscoreCount = itemName.chars().filter(ch -> ch == '_').count();
            if (underscoreCount >= 4) {
                return 6; // Loaded potato = AVERAGE_MEAL (was 5)
            }
            return 4; // Simple baked potato = LIGHT_MEAL
        }
        
        // Chicken items (4 hunger)
        if (itemName.contains("chicken_nuggets")) {
            return 4; // Nuggets = LIGHT_MEAL
        }
        if (itemName.contains("chicken_patty")) {
            return 4; // Patty = LIGHT_MEAL (was 3)
        }
        
        // Breakfast items (4-6 hunger)
        if (itemName.contains("breakfast_")) {
            if (itemName.contains("plate")) {
                return 6; // Full plate = AVERAGE_MEAL
            } else if (itemName.contains("bar") || itemName.contains("sausages")) {
                return 4; // Simple breakfast bar = LIGHT_MEAL (was 3)
            }
            return 4; // Standard breakfast = LIGHT_MEAL (was 5)
        }
        
        // Complex meals (6-8 hunger)
        if (itemName.contains("dinner_plate")) {
            return 8; // Dinner plate = LARGE_MEAL
        }
        if (itemName.contains("full_breakfast")) {
            return 6; // Full breakfast = AVERAGE_MEAL (was 7)
        }
        
        // Graham crackers (4 hunger)
        if (itemName.contains("graham_cracker") && !itemName.contains("_cookie")) {
            return 4; // Light snack = LIGHT_MEAL (was 3)
        }
        
        // Caramel/Chocolate glazed items (4 hunger)
        if ((itemName.contains("caramel_") || itemName.contains("chocolate_glazed") || itemName.contains("dark_chocolate_glazed") || itemName.contains("white_chocolate_glazed")) 
            && (itemName.contains("apple") || itemName.contains("berries"))) {
            return 4; // Glazed treat = LIGHT_MEAL (was 3)
        }
        
        // Marshmallow sticks (4 hunger)
        if (itemName.contains("marshmallow_stick") || itemName.contains("covered_marshmallow_stick")) {
            return 4; // Marshmallow treat = LIGHT_MEAL (was 3)
        }
        
        // Fried egg plates (4 hunger)
        if (itemName.contains("fried_egg") && itemName.contains("plate")) {
            return 4; // Egg plate = LIGHT_MEAL (was 3)
        }
        
        // Donuts, pastries, muffins, cupcakes, waffles, sweet rolls (2 hunger default, 4 if frosted/glazed)
        if (itemName.contains("_donut") || itemName.contains("_pastry") || itemName.contains("_muffin") 
            || itemName.contains("_cupcake") || itemName.contains("_waffle") || itemName.contains("_sweet_roll")) {
            if (itemName.contains("frosted") || itemName.contains("glazed")) {
                return 4; // Frosted = LIGHT_MEAL (was 3)
            }
            return 2; // Plain = COOKED_FOOD
        }
        
        // Chocolate bars, cookies, fudge, gelatin, popcorn (2 hunger)
        if (itemName.contains("bar_of_") || itemName.contains("_cookie") || itemName.contains("_fudge") 
            || itemName.contains("_gelatin") || itemName.contains("_popcorn")) {
            return 2;
        }
        
        // Simple slices, bottles, popsicles (2 hunger)
        if (itemName.contains("_slice") || itemName.contains("_bottle") || itemName.contains("_popsicle") 
            || itemName.contains("_bits") || itemName.equals("biscuit") || itemName.equals("bun")) {
            return 2;
        }
        
        // Default: simple food (2 hunger)
        return 2;
    }
    
    /**
     * Determine saturation value based on hunger value.
     */
    private float getSaturationForItem(String itemName, int hunger) {
        // Saturation is typically hunger * 0.1, but can vary
        switch (hunger) {
            case 1:
                return 0.05f;
            case 2:
                return 0.1f;
            case 3:
                return 0.2f;
            case 4:
                return 0.3f;
            case 5:
                return 0.35f;
            case 6:
                return 0.4f;
            case 7:
                return 0.5f;
            case 8:
                return 0.5f;
            default:
                return hunger * 0.1f;
        }
    }

    /**
     * Determine meal type category based on hunger value and item name.
     */
    private String getMealTypeCategory(String itemName, int hunger) {
        // RAW_FOOD (1 hunger) - Raw ingredients, no crafting
        if (hunger == 1) {
            return "RAW_FOOD";
        }
        
        // COOKED_FOOD (2 hunger) - Simply cooked items
        if (hunger == 2) {
            if (itemName.contains("cooked") || itemName.contains("fried") || 
                itemName.contains("baked") || itemName.contains("roasted")) {
                return "COOKED_FOOD";
            }
            return "LIGHT_MEAL";
        }
        
        // LIGHT_MEAL (4 hunger) - Simple crafting (2-3 ingredients)
        if (hunger == 4) {
            return "LIGHT_MEAL";
        }
        
        // AVERAGE_MEAL (6 hunger) - Moderate crafting (4-5 ingredients)
        if (hunger == 6) {
            return "AVERAGE_MEAL";
        }
        
        // LARGE_MEAL (8 hunger) - Complex crafting (6+ ingredients)
        if (hunger >= 8) {
            return "LARGE_MEAL";
        }
        
        // Default to LIGHT_MEAL for any unexpected values
        return "LIGHT_MEAL";
    }

    /**
     * Set food values for a specific Create: Food item.
     */
    private void setCreateFoodValue(String itemName, int hunger, float saturation) {
        String fullId = CREATE_FOOD_MOD_ID + ":" + itemName;
        String category = getMealTypeCategory(itemName, hunger);
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "CREATE_FOOD_" + category
        ));
    }

    /**
     * Check if Create: Food is loaded.
     */
    public static boolean isCreateFoodLoaded() {
        return createFoodLoaded;
    }

    /**
     * Get the Create: Food mod ID.
     */
    public static String getCreateFoodModId() {
        return CREATE_FOOD_MOD_ID;
    }
}
