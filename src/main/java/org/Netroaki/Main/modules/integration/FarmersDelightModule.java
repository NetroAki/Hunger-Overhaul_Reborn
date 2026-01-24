package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Farmer's Delight mod.
 * Handles food value modifications and crop growth adjustments.
 */
public class FarmersDelightModule {

    private static final String FARMERS_DELIGHT_MOD_ID = "farmersdelight";
    private static boolean farmersDelightLoaded = false;

    /**
     * Initialize Farmer's Delight integration.
     */
    public void init() {
        farmersDelightLoaded = Platform.isModLoaded(FARMERS_DELIGHT_MOD_ID);

        if (!farmersDelightLoaded) {
            HOReborn.LOGGER.debug("Farmer's Delight not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Farmer's Delight integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Farmer's Delight items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw crops, ingredients, raw meats (no crafting)
     * - COOKED_FOOD: Simply cooked ingredients (furnace/smoker)
     * - LIGHT_MEAL: Simple meals, soups, pies, snacks (2-3 ingredients)
     * - AVERAGE_MEAL: Complex meals, stews (4-5 ingredients)
     * - LARGE_MEAL: Very complex meals (6+ ingredients)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Only truly raw, unprocessed crops and meats
        setFarmersDelightFoodValue("cabbage", 1, 0.05f, "RAW_FOOD");
        setFarmersDelightFoodValue("cabbage_leaf", 1, 0.05f, "RAW_FOOD");
        setFarmersDelightFoodValue("tomato", 1, 0.05f, "RAW_FOOD");
        setFarmersDelightFoodValue("onion", 1, 0.05f, "RAW_FOOD");
        setFarmersDelightFoodValue("pumpkin_slice", 1, 0.05f, "RAW_FOOD");
        setFarmersDelightFoodValue("chicken_cuts", 1, 0.05f, "RAW_FOOD");
        setFarmersDelightFoodValue("mutton_chops", 1, 0.05f, "RAW_FOOD");
        setFarmersDelightFoodValue("cod_slice", 1, 0.05f, "RAW_FOOD");
        setFarmersDelightFoodValue("salmon_slice", 1, 0.05f, "RAW_FOOD");
        setFarmersDelightFoodValue("kelp_roll_slice", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Prepared/processed ingredients and lightly cooked items
        setFarmersDelightFoodValue("wheat_dough", 2, 0.1f, "COOKED_FOOD"); // Prepared dough
        setFarmersDelightFoodValue("raw_pasta", 2, 0.1f, "COOKED_FOOD"); // Prepared pasta
        setFarmersDelightFoodValue("pie_crust", 2, 0.1f, "COOKED_FOOD"); // Prepared crust
        setFarmersDelightFoodValue("tomato_sauce", 2, 0.1f, "COOKED_FOOD"); // Processed sauce
        setFarmersDelightFoodValue("minced_beef", 2, 0.1f, "COOKED_FOOD"); // Ground/processed meat
        setFarmersDelightFoodValue("beef_patty", 2, 0.1f, "COOKED_FOOD"); // Formed meat patty
        setFarmersDelightFoodValue("bacon", 2, 0.1f, "COOKED_FOOD"); // Cured/processed meat
        setFarmersDelightFoodValue("ham", 2, 0.1f, "COOKED_FOOD"); // Cured/processed meat
        
        // COOKED_FOOD (2 hunger) - Simply cooked ingredients
        setFarmersDelightFoodValue("fried_egg", 2, 0.2f, "COOKED_FOOD");
        setFarmersDelightFoodValue("cooked_chicken_cuts", 2, 0.25f, "COOKED_FOOD");
        setFarmersDelightFoodValue("cooked_mutton_chops", 2, 0.25f, "COOKED_FOOD");
        setFarmersDelightFoodValue("roasted_mutton_chops", 2, 0.25f, "COOKED_FOOD");
        setFarmersDelightFoodValue("cooked_cod_slice", 2, 0.25f, "COOKED_FOOD");
        setFarmersDelightFoodValue("cooked_salmon_slice", 2, 0.25f, "COOKED_FOOD");
        setFarmersDelightFoodValue("cooked_bacon", 2, 0.2f, "COOKED_FOOD");
        setFarmersDelightFoodValue("smoked_ham", 2, 0.25f, "COOKED_FOOD");
        setFarmersDelightFoodValue("cooked_rice", 2, 0.2f, "COOKED_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Simple meals, soups, pies, snacks (2-3 ingredients)
        setFarmersDelightFoodValue("honey_cookie", 4, 0.2f, "LIGHT_MEAL"); // Dough + honey
        setFarmersDelightFoodValue("sweet_berry_cookie", 4, 0.2f, "LIGHT_MEAL"); // Dough + sweet berries
        setFarmersDelightFoodValue("melon_popsicle", 4, 0.2f, "LIGHT_MEAL"); // Melon + stick + freezing
        setFarmersDelightFoodValue("apple_cider", 4, 0.2f, "LIGHT_MEAL"); // Apple + processing
        setFarmersDelightFoodValue("glow_berry_custard", 4, 0.25f, "LIGHT_MEAL"); // Glow berry + milk + egg
        setFarmersDelightFoodValue("egg_sandwich", 4, 0.3f, "LIGHT_MEAL"); // Bread + egg
        setFarmersDelightFoodValue("bacon_sandwich", 4, 0.3f, "LIGHT_MEAL"); // Bread + bacon
        setFarmersDelightFoodValue("chicken_sandwich", 4, 0.3f, "LIGHT_MEAL"); // Bread + chicken
        setFarmersDelightFoodValue("cod_roll", 4, 0.25f, "LIGHT_MEAL"); // Cod + wrapper
        setFarmersDelightFoodValue("salmon_roll", 4, 0.25f, "LIGHT_MEAL"); // Salmon + wrapper
        setFarmersDelightFoodValue("kelp_roll", 4, 0.2f, "LIGHT_MEAL"); // Kelp + wrapper
        setFarmersDelightFoodValue("mixed_salad", 4, 0.3f, "LIGHT_MEAL"); // Vegetables + dressing
        setFarmersDelightFoodValue("fruit_salad", 4, 0.3f, "LIGHT_MEAL"); // Fruits + optional
        setFarmersDelightFoodValue("fried_rice", 4, 0.3f, "LIGHT_MEAL"); // Rice + vegetables + optional egg
        
        // LIGHT_MEAL (4 hunger) - Simple prepared items
        setFarmersDelightFoodValue("vegetable_noodles", 4, 0.3f, "LIGHT_MEAL"); // Noodles + vegetables (simple, minimal cooking)
        
        // LIGHT_MEAL (4 hunger) - Pies and desserts
        setFarmersDelightFoodValue("apple_pie_slice", 4, 0.2f, "LIGHT_MEAL"); // Pie slice = fruit + dough + sugar
        setFarmersDelightFoodValue("chocolate_pie_slice", 4, 0.2f, "LIGHT_MEAL");
        setFarmersDelightFoodValue("sweet_berry_cheesecake_slice", 4, 0.2f, "LIGHT_MEAL");
        setFarmersDelightFoodValue("cake_slice", 4, 0.2f, "LIGHT_MEAL");
        
        // AVERAGE_MEAL (6 hunger) - Soups and stews (multiple ingredients + broth/cooking process)
        setFarmersDelightFoodValue("chicken_soup", 6, 0.4f, "AVERAGE_MEAL"); // Chicken + broth + optional vegetables
        setFarmersDelightFoodValue("vegetable_soup", 6, 0.4f, "AVERAGE_MEAL"); // Vegetables + broth
        setFarmersDelightFoodValue("pumpkin_soup", 6, 0.4f, "AVERAGE_MEAL"); // Pumpkin + broth + optional
        setFarmersDelightFoodValue("fish_stew", 6, 0.4f, "AVERAGE_MEAL"); // Fish + broth + optional vegetables
        setFarmersDelightFoodValue("baked_cod_stew", 6, 0.4f, "AVERAGE_MEAL"); // Cod + broth + optional vegetables
        setFarmersDelightFoodValue("bone_broth", 6, 0.4f, "AVERAGE_MEAL"); // Bones + water + optional vegetables
        setFarmersDelightFoodValue("beef_stew", 6, 0.4f, "AVERAGE_MEAL"); // Beef + broth + optional vegetables
        setFarmersDelightFoodValue("noodle_soup", 6, 0.4f, "AVERAGE_MEAL"); // Noodles + broth + optional
        
        // AVERAGE_MEAL (6 hunger) - Complex meals (4-5 ingredients)
        setFarmersDelightFoodValue("mutton_wrap", 6, 0.4f, "AVERAGE_MEAL"); // Mutton + wrapper + vegetables + sauce
        setFarmersDelightFoodValue("nether_salad", 6, 0.4f, "AVERAGE_MEAL"); // Nether vegetables + dressing + optional
        setFarmersDelightFoodValue("barbecue_stick", 6, 0.4f, "AVERAGE_MEAL"); // Meat + vegetables + stick + sauce
        setFarmersDelightFoodValue("hamburger", 6, 0.4f, "AVERAGE_MEAL"); // Bun + meat + vegetables + optional toppings
        setFarmersDelightFoodValue("dumplings", 6, 0.4f, "AVERAGE_MEAL"); // Dough + filling + optional vegetables
        setFarmersDelightFoodValue("bacon_and_eggs", 6, 0.4f, "AVERAGE_MEAL"); // Bacon + eggs + optional sides
        setFarmersDelightFoodValue("stuffed_potato", 6, 0.4f, "AVERAGE_MEAL"); // Potato + filling + optional toppings
        setFarmersDelightFoodValue("cabbage_rolls", 6, 0.4f, "AVERAGE_MEAL"); // Cabbage + filling + optional
        setFarmersDelightFoodValue("mushroom_rice", 6, 0.4f, "AVERAGE_MEAL"); // Rice + mushrooms + optional vegetables
        setFarmersDelightFoodValue("grilled_salmon", 6, 0.4f, "AVERAGE_MEAL"); // Salmon + seasoning + optional sides
        
        // AVERAGE_MEAL (6 hunger) - Complex pasta and main dishes
        setFarmersDelightFoodValue("pasta_with_meatballs", 6, 0.4f, "AVERAGE_MEAL"); // Pasta + meatballs + sauce + optional
        setFarmersDelightFoodValue("pasta_with_mutton_chop", 6, 0.4f, "AVERAGE_MEAL"); // Pasta + mutton + sauce + optional
        setFarmersDelightFoodValue("squid_ink_pasta", 6, 0.4f, "AVERAGE_MEAL"); // Pasta + squid ink + optional ingredients
        setFarmersDelightFoodValue("ratatouille", 6, 0.4f, "AVERAGE_MEAL"); // Multiple vegetables + herbs + cooking
        setFarmersDelightFoodValue("steak_and_potatoes", 6, 0.4f, "AVERAGE_MEAL"); // Steak + potatoes + optional vegetables
        setFarmersDelightFoodValue("roast_chicken", 6, 0.4f, "AVERAGE_MEAL"); // Chicken + seasoning + optional vegetables
        setFarmersDelightFoodValue("stuffed_pumpkin", 6, 0.4f, "AVERAGE_MEAL"); // Pumpkin + filling + optional ingredients
        
        // LARGE_MEAL (8 hunger) - Very complex meals (6+ ingredients)
        setFarmersDelightFoodValue("shepherds_pie", 8, 0.5f, "LARGE_MEAL"); // Meat + vegetables + potatoes + multiple steps
        setFarmersDelightFoodValue("honey_glazed_ham", 8, 0.5f, "LARGE_MEAL"); // Ham + honey + glaze + optional sides
        
        // COOKED_FOOD (2 hunger) - Pet food (simple processed)
        setFarmersDelightFoodValue("dog_food", 2, 0.1f, "COOKED_FOOD"); // Processed food for pets
    }

    /**
     * Set food values for a specific Farmer's Delight item.
     */
    private void setFarmersDelightFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = FARMERS_DELIGHT_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "FARMERS_DELIGHT_" + category
        ));
    }

    /**
     * Check if Farmer's Delight is loaded.
     */
    public static boolean isFarmersDelightLoaded() {
        return farmersDelightLoaded;
    }

    /**
     * Get the Farmer's Delight mod ID.
     */
    public static String getFarmersDelightModId() {
        return FARMERS_DELIGHT_MOD_ID;
    }
}

