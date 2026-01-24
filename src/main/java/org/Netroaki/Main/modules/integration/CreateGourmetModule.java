package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Create Gourmet mod.
 * Handles food value modifications for Create Gourmet items.
 */
public class CreateGourmetModule {

    private static final String CREATE_GOURMET_MOD_ID = "gourmet";
    private static boolean createGourmetLoaded = false;

    /**
     * Initialize Create Gourmet integration.
     */
    public void init() {
        createGourmetLoaded = Platform.isModLoaded(CREATE_GOURMET_MOD_ID);

        if (!createGourmetLoaded) {
            HOReborn.LOGGER.debug("Create Gourmet not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Create Gourmet integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Create Gourmet items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw ingredients, raw meats (no crafting)
     * - COOKED_FOOD: Simply cooked items (furnace/smoker)
     * - LIGHT_MEAL: Simple processed foods, basic meals, ice cream (2-3 ingredients)
     * - AVERAGE_MEAL: Complex meals (4-5 ingredients)
     * - LARGE_MEAL: Very complex meals (6+ ingredients)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Only truly raw, unprocessed ingredients
        setCreateGourmetFoodValue("raw_bacon", 1, 0.05f, "RAW_FOOD"); // Raw bacon before cooking
        setCreateGourmetFoodValue("raw_drumstick", 1, 0.05f, "RAW_FOOD"); // Raw poultry
        setCreateGourmetFoodValue("raw_potato_fry", 1, 0.05f, "RAW_FOOD"); // Raw potato pieces
        
        // COOKED_FOOD (2 hunger) - Processed/prepared single ingredients
        setCreateGourmetFoodValue("butter", 2, 0.1f, "COOKED_FOOD"); // Butter is processed dairy
        setCreateGourmetFoodValue("carrot_slices", 2, 0.1f, "COOKED_FOOD"); // Pre-sliced prepared ingredient
        setCreateGourmetFoodValue("ground_beef", 2, 0.1f, "COOKED_FOOD"); // Ground/processed meat
        setCreateGourmetFoodValue("beef_patty", 2, 0.1f, "COOKED_FOOD"); // Formed/shaped meat patty
        
        // COOKED_FOOD (2 hunger) - Simply cooked items
        setCreateGourmetFoodValue("cheese", 2, 0.1f, "COOKED_FOOD"); // Processed dairy product
        setCreateGourmetFoodValue("buns", 2, 0.1f, "COOKED_FOOD"); // Baked bread
        
        // LIGHT_MEAL (4 hunger) - Cooked/prepared meals and components
        setCreateGourmetFoodValue("cooked_bacon", 4, 0.2f, "LIGHT_MEAL"); // Cooked prepared meat
        setCreateGourmetFoodValue("caramelized_bacon", 4, 0.2f, "LIGHT_MEAL"); // Cooked + caramelized
        setCreateGourmetFoodValue("cooked_drumstick", 4, 0.25f, "LIGHT_MEAL"); // Cooked prepared meat
        setCreateGourmetFoodValue("cooked_fries", 4, 0.2f, "LIGHT_MEAL"); // Prepared fried potatoes
        setCreateGourmetFoodValue("fried_egg", 4, 0.2f, "LIGHT_MEAL"); // Prepared egg dish
        setCreateGourmetFoodValue("flatcake", 4, 0.1f, "LIGHT_MEAL"); // Baked prepared item
        
        // LIGHT_MEAL (4 hunger) - Simple prepared foods, basic meals (2-3 ingredients)
        setCreateGourmetFoodValue("breakfast_sandwich", 4, 0.3f, "LIGHT_MEAL"); // Bun + egg + optional bacon
        setCreateGourmetFoodValue("chicken_sandwich", 4, 0.3f, "LIGHT_MEAL"); // Bun + chicken + optional
        setCreateGourmetFoodValue("croissant", 4, 0.25f, "LIGHT_MEAL"); // Dough + butter + shaping
        setCreateGourmetFoodValue("flatcake_stack", 4, 0.3f, "LIGHT_MEAL"); // Multiple flatcakes
        setCreateGourmetFoodValue("waffle", 4, 0.25f, "LIGHT_MEAL"); // Dough + waffle iron
        setCreateGourmetFoodValue("waffle_stack", 4, 0.35f, "LIGHT_MEAL"); // Multiple waffles
        setCreateGourmetFoodValue("dumpling", 4, 0.25f, "LIGHT_MEAL"); // Dough + filling
        setCreateGourmetFoodValue("cheese_danish", 4, 0.25f, "LIGHT_MEAL"); // Dough + cheese + optional
        setCreateGourmetFoodValue("sweet_berry_danish", 4, 0.25f, "LIGHT_MEAL"); // Dough + sweet berries + optional
        setCreateGourmetFoodValue("glow_berry_danish", 4, 0.25f, "LIGHT_MEAL"); // Dough + glow berries + optional
        setCreateGourmetFoodValue("carrot_cake_base", 4, 0.25f, "LIGHT_MEAL"); // Carrots + flour + sugar + eggs
        
        // LIGHT_MEAL (4 hunger) - Ice cream and frozen treats (2-3 ingredients)
        setCreateGourmetFoodValue("ice_cream_cone", 4, 0.2f, "LIGHT_MEAL"); // Ice cream + cone
        setCreateGourmetFoodValue("ice_cream_scoop", 4, 0.2f, "LIGHT_MEAL"); // Ice cream scoop
        setCreateGourmetFoodValue("chocolate_ice_cream_cone", 4, 0.25f, "LIGHT_MEAL"); // Chocolate ice cream + cone
        setCreateGourmetFoodValue("chocolate_ice_cream_scoop", 4, 0.25f, "LIGHT_MEAL"); // Chocolate ice cream scoop
        setCreateGourmetFoodValue("sweet_berry_ice_cream_cone", 4, 0.25f, "LIGHT_MEAL"); // Sweet berry ice cream + cone
        setCreateGourmetFoodValue("sweet_berry_ice_cream_scoop", 4, 0.25f, "LIGHT_MEAL"); // Sweet berry ice cream scoop
        setCreateGourmetFoodValue("neapolitan_ice_cream_cone", 4, 0.25f, "LIGHT_MEAL"); // Multiple flavors + cone
        setCreateGourmetFoodValue("ice_cream_sandwich", 4, 0.25f, "LIGHT_MEAL"); // Ice cream + cookies
        
        // AVERAGE_MEAL (6 hunger) - Complex meals (4-5 ingredients)
        setCreateGourmetFoodValue("bacon_burger", 6, 0.4f, "AVERAGE_MEAL"); // Bun + meat + bacon + toppings
        setCreateGourmetFoodValue("fried_chicken", 6, 0.4f, "AVERAGE_MEAL"); // Chicken + batter + seasoning + cooking
        setCreateGourmetFoodValue("southern_breakfast", 6, 0.4f, "AVERAGE_MEAL"); // Multiple breakfast items + sides
        setCreateGourmetFoodValue("comforting_dumpling_meal", 6, 0.4f, "AVERAGE_MEAL"); // Dumplings + broth + optional sides
        setCreateGourmetFoodValue("carrot_cake", 6, 0.4f, "AVERAGE_MEAL"); // Carrot cake base + frosting + optional decorations
        
        // LARGE_MEAL (8 hunger) - Very complex meals (6+ ingredients)
        setCreateGourmetFoodValue("big_bacon_burger_meal", 8, 0.5f, "LARGE_MEAL"); // Large burger + multiple toppings + sides
        setCreateGourmetFoodValue("variety_dessert_platter", 8, 0.5f, "LARGE_MEAL"); // Multiple desserts + presentation
    }

    /**
     * Set food values for a specific Create Gourmet item.
     */
    private void setCreateGourmetFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = CREATE_GOURMET_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "CREATE_GOURMET_" + category
        ));
    }

    /**
     * Check if Create Gourmet is loaded.
     */
    public static boolean isCreateGourmetLoaded() {
        return createGourmetLoaded;
    }

    /**
     * Get the Create Gourmet mod ID.
     */
    public static String getCreateGourmetModId() {
        return CREATE_GOURMET_MOD_ID;
    }
}

