package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Alex's Mobs mod.
 * Handles food value modifications for mob drops and special foods.
 */
public class AlexsMobsModule {

    private static final String ALEXS_MOBS_MOD_ID = "alexsmobs";
    private static boolean alexsMobsLoaded = false;

    /**
     * Initialize Alex's Mobs integration.
     */
    public void init() {
        alexsMobsLoaded = Platform.isModLoaded(ALEXS_MOBS_MOD_ID);

        if (!alexsMobsLoaded) {
            HOReborn.LOGGER.debug("Alex's Mobs not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Alex's Mobs integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Alex's Mobs items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw meats, fish, produce (no crafting)
     * - COOKED_FOOD: Simply cooked meats/fish (furnace/smoker)
     * - LIGHT_MEAL: Simple processed items (2-3 ingredients)
     * - AVERAGE_MEAL: Complex meals (4-5 ingredients, multi-step recipes)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw meats and fish
        setAlexsMobsFoodValue("raw_catfish", 1, 0.05f, "RAW_FOOD");
        setAlexsMobsFoodValue("kangaroo_meat", 1, 0.05f, "RAW_FOOD");
        setAlexsMobsFoodValue("moose_ribs", 1, 0.05f, "RAW_FOOD");
        setAlexsMobsFoodValue("lobster_tail", 1, 0.05f, "RAW_FOOD");
        setAlexsMobsFoodValue("blobfish", 1, 0.05f, "RAW_FOOD");
        setAlexsMobsFoodValue("flying_fish", 1, 0.05f, "RAW_FOOD");
        setAlexsMobsFoodValue("maggot", 1, 0.05f, "RAW_FOOD");
        
        // RAW_FOOD (1 hunger) - Raw produce
        setAlexsMobsFoodValue("banana", 1, 0.05f, "RAW_FOOD");
        setAlexsMobsFoodValue("gongylidia", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simply cooked meats/fish
        setAlexsMobsFoodValue("cooked_catfish", 2, 0.25f, "COOKED_FOOD");
        setAlexsMobsFoodValue("cooked_kangaroo_meat", 2, 0.25f, "COOKED_FOOD");
        setAlexsMobsFoodValue("cooked_lobster_tail", 2, 0.25f, "COOKED_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simple processed items
        setAlexsMobsFoodValue("boiled_emu_egg", 2, 0.2f, "COOKED_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Simple processed items
        setAlexsMobsFoodValue("cosmic_cod", 4, 0.2f, "LIGHT_MEAL"); // Special fish, likely processed
        setAlexsMobsFoodValue("rainbow_jelly", 4, 0.2f, "LIGHT_MEAL");
        
        // AVERAGE_MEAL (6 hunger) - Stews (multiple ingredients + broth)
        setAlexsMobsFoodValue("mosquito_repellent_stew", 6, 0.4f, "AVERAGE_MEAL"); // Stew = multiple ingredients + broth
        setAlexsMobsFoodValue("sopa_de_macaco", 4, 0.3f, "LIGHT_MEAL");
        
        // AVERAGE_MEAL (6 hunger) - Complex meals (multiple ingredients)
        setAlexsMobsFoodValue("cooked_moose_ribs", 6, 0.4f, "AVERAGE_MEAL"); // Large ribs, likely seasoned
        setAlexsMobsFoodValue("kangaroo_burger", 6, 0.4f, "AVERAGE_MEAL"); // Burger = bun + meat + toppings
        setAlexsMobsFoodValue("shrimp_fried_rice", 6, 0.4f, "AVERAGE_MEAL"); // Rice + shrimp + vegetables + seasoning
    }

    /**
     * Set food values for a specific Alex's Mobs item.
     */
    private void setAlexsMobsFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = ALEXS_MOBS_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "ALEXS_MOBS_" + category
        ));
    }

    /**
     * Check if Alex's Mobs is loaded.
     */
    public static boolean isAlexsMobsLoaded() {
        return alexsMobsLoaded;
    }

    /**
     * Get the Alex's Mobs mod ID.
     */
    public static String getAlexsMobsModId() {
        return ALEXS_MOBS_MOD_ID;
    }
}

