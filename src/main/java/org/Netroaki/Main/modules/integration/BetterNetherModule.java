package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Better Nether mod.
 * Handles food value modifications for Nether dimension foods.
 */
public class BetterNetherModule {

    private static final String BETTER_NETHER_MOD_ID = "betternether";
    private static boolean betterNetherLoaded = false;

    /**
     * Initialize Better Nether integration.
     */
    public void init() {
        betterNetherLoaded = Platform.isModLoaded(BETTER_NETHER_MOD_ID);

        if (!betterNetherLoaded) {
            HOReborn.LOGGER.debug("Better Nether not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Better Nether integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Better Nether items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw apples, mushrooms, warts (no crafting)
     * - COOKED_FOOD: Simply cooked mushrooms (furnace)
     * - LIGHT_MEAL: Simple crafted bowls (bowl + ingredient - 2 ingredients)
     * - LIGHT_MEAL: Medicine items (processed - 2-3 ingredients)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw apples, mushrooms, warts
        setBetterNetherFoodValue("black_apple", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simply cooked mushrooms
        setBetterNetherFoodValue("hook_mushroom_cooked", 2, 0.25f, "COOKED_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Simple crafted bowls (bowl + ingredient)
        setBetterNetherFoodValue("stalagnate_bowl_apple", 4, 0.2f, "LIGHT_MEAL"); // Bowl + apple
        setBetterNetherFoodValue("stalagnate_bowl_mushroom", 4, 0.2f, "LIGHT_MEAL"); // Bowl + mushroom
        setBetterNetherFoodValue("stalagnate_bowl_wart", 4, 0.2f, "LIGHT_MEAL"); // Bowl + wart
        
        // LIGHT_MEAL (4 hunger) - Medicine items (processed)
        setBetterNetherFoodValue("agave_medicine", 4, 0.2f, "LIGHT_MEAL"); // Agave + processing
        setBetterNetherFoodValue("herbal_medicine", 4, 0.2f, "LIGHT_MEAL"); // Herbs + processing
    }

    /**
     * Set food values for a specific Better Nether item.
     */
    private void setBetterNetherFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = BETTER_NETHER_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "BETTER_NETHER_" + category
        ));
    }

    /**
     * Check if Better Nether is loaded.
     */
    public static boolean isBetterNetherLoaded() {
        return betterNetherLoaded;
    }

    /**
     * Get the Better Nether mod ID.
     */
    public static String getBetterNetherModId() {
        return BETTER_NETHER_MOD_ID;
    }
}

