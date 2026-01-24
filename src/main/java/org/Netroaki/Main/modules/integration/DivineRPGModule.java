package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for DivineRPG mod.
 * Handles food value modifications for DivineRPG foods.
 */
public class DivineRPGModule {

    private static final String DIVINE_RPG_MOD_ID = "divinerpg";
    private static boolean divineRPGLoaded = false;

    /**
     * Initialize DivineRPG integration.
     */
    public void init() {
        divineRPGLoaded = Platform.isModLoaded(DIVINE_RPG_MOD_ID);

        if (!divineRPGLoaded) {
            HOReborn.LOGGER.debug("DivineRPG not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing DivineRPG integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to DivineRPG items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw produce, fruits, mushrooms, raw meats (no crafting)
     * - COOKED_FOOD: Simply cooked/processed items (bacon, cheese, boiled egg, simple candies)
     * - LIGHT_MEAL: Simple crafted items (chocolate log, donut, pies, drinks - 2-3 ingredients)
     * - AVERAGE_MEAL: Moderate crafting (stews, dinners, cakes, steaks - 4-5 ingredients)
     * - LARGE_MEAL: Complex items (enriched magic meat - special crafting)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw produce, fruits, mushrooms, raw meats
        setDivineRPGFoodValue("tomato", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("white_mushroom", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("sky_flower", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("pinfly", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("pink_glowbone", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("purple_glowbone", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("winterberry", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("moonbulb", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("honeysuckle", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("hitchak", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("dream_sours", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("dream_carrot", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("dream_melon", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("raw_empowered_meat", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("raw_seng_meat", 1, 0.05f, "RAW_FOOD");
        setDivineRPGFoodValue("raw_wolpertinger_meat", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simply cooked/processed items
        setDivineRPGFoodValue("bacon", 2, 0.1f, "COOKED_FOOD");
        setDivineRPGFoodValue("cheese", 2, 0.1f, "COOKED_FOOD");
        setDivineRPGFoodValue("boiled_egg", 2, 0.2f, "COOKED_FOOD");
        setDivineRPGFoodValue("cauldron_flesh", 2, 0.1f, "COOKED_FOOD");
        setDivineRPGFoodValue("peppermints", 2, 0.1f, "COOKED_FOOD");
        setDivineRPGFoodValue("snow_cones", 2, 0.1f, "COOKED_FOOD");
        setDivineRPGFoodValue("honeychunk", 2, 0.15f, "COOKED_FOOD");
        setDivineRPGFoodValue("dream_sweets", 2, 0.15f, "COOKED_FOOD");
        setDivineRPGFoodValue("weak_arcana_potion", 2, 0.1f, "COOKED_FOOD");
        setDivineRPGFoodValue("strong_arcana_potion", 2, 0.15f, "COOKED_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Simple crafted items (2-3 ingredients)
        setDivineRPGFoodValue("chocolate_log", 4, 0.3f, "LIGHT_MEAL"); // Chocolate + processing
        setDivineRPGFoodValue("forbidden_fruit", 4, 0.3f, "LIGHT_MEAL"); // Special fruit, likely processed
        setDivineRPGFoodValue("donut", 4, 0.25f, "LIGHT_MEAL"); // Dough + sugar + optional
        setDivineRPGFoodValue("egg_nog", 4, 0.25f, "LIGHT_MEAL"); // Egg + milk + spices
        setDivineRPGFoodValue("dream_pie", 4, 0.3f, "LIGHT_MEAL"); // Dream ingredients + dough
        setDivineRPGFoodValue("hot_pumpkin_pie", 4, 0.3f, "LIGHT_MEAL"); // Pumpkin + dough + sugar
        
        // AVERAGE_MEAL (6 hunger) - Moderate crafting (4-5 ingredients)
        setDivineRPGFoodValue("advanced_mushroom_stew", 6, 0.4f, "AVERAGE_MEAL"); // Mushrooms + broth + vegetables
        setDivineRPGFoodValue("chicken_dinner", 6, 0.4f, "AVERAGE_MEAL"); // Chicken + sides + preparation
        setDivineRPGFoodValue("dream_cake", 6, 0.4f, "AVERAGE_MEAL"); // Dream ingredients + flour + sugar + eggs
        setDivineRPGFoodValue("fruit_cake", 6, 0.4f, "AVERAGE_MEAL"); // Fruits + flour + sugar + eggs
        setDivineRPGFoodValue("empowered_meat", 6, 0.4f, "AVERAGE_MEAL"); // Raw empowered meat + processing
        setDivineRPGFoodValue("magic_meat", 6, 0.4f, "AVERAGE_MEAL"); // Meat + magic processing
        setDivineRPGFoodValue("seng_steak", 6, 0.4f, "AVERAGE_MEAL"); // Raw seng meat + cooking + seasoning
        setDivineRPGFoodValue("wolpertinger_steak", 6, 0.4f, "AVERAGE_MEAL"); // Raw wolpertinger meat + cooking + seasoning
        
        // LARGE_MEAL (8 hunger) - Complex items (special crafting)
        setDivineRPGFoodValue("enriched_magic_meat", 8, 0.5f, "LARGE_MEAL"); // Magic meat + enrichment process
    }

    /**
     * Set food values for a specific DivineRPG item.
     */
    private void setDivineRPGFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = DIVINE_RPG_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "DIVINE_RPG_" + category
        ));
    }

    /**
     * Check if DivineRPG is loaded.
     */
    public static boolean isDivineRPGLoaded() {
        return divineRPGLoaded;
    }

    /**
     * Get the DivineRPG mod ID.
     */
    public static String getDivineRPGModId() {
        return DIVINE_RPG_MOD_ID;
    }
}

