package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Natura mod.
 * 
 * NOTE: Natura is deprecated and no longer exists for Minecraft 1.20.1+.
 * Most of its functionality (berries, flour recipes) has been integrated into Tinkers' Construct.
 * This module is kept for backwards compatibility and legacy support only.
 * 
 * For flour recipe handling, see TinkersConstructModule.
 */
public class NaturaModule {

    private static final String NATURA_MOD_ID = "natura";
    private static boolean naturaLoaded = false;

    /**
     * Initialize Natura integration.
     * Note: Natura doesn't exist for 1.20.1+, functionality moved to Tinkers' Construct.
     */
    public void init() {
        naturaLoaded = Platform.isModLoaded(NATURA_MOD_ID);

        if (!naturaLoaded) {
            // Natura doesn't exist for 1.20.1+, this is expected
            HOReborn.LOGGER.debug("Natura not detected (expected for 1.20.1+ - functionality moved to Tinkers' Construct)");
            return;
        }

        // Only log if somehow Natura is detected (legacy version)
        HOReborn.LOGGER.info("Initializing Natura integration (legacy support)");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }

        // Handle flour recipe modifications
        handleFlourRecipes();
    }

    /**
     * Apply food value modifications to Natura items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw berries and fruits (no crafting)
     * - LIGHT_MEAL: Simple crafted items (berry_medley - 2-3 ingredients)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw berries and fruits
        setNaturaFoodValue("saguaro_fruit", 1, 0.05f, "RAW_FOOD");
        setNaturaFoodValue("raspberry", 1, 0.05f, "RAW_FOOD");
        setNaturaFoodValue("blueberry", 1, 0.05f, "RAW_FOOD");
        setNaturaFoodValue("blackberry", 1, 0.05f, "RAW_FOOD");
        setNaturaFoodValue("maloberry", 1, 0.05f, "RAW_FOOD");
        setNaturaFoodValue("blightberry", 1, 0.05f, "RAW_FOOD"); // Probably poisonous
        setNaturaFoodValue("duskberry", 1, 0.05f, "RAW_FOOD");
        setNaturaFoodValue("skyberry", 1, 0.05f, "RAW_FOOD");
        setNaturaFoodValue("strawberry", 1, 0.05f, "RAW_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Simple crafted items (2-3 ingredients)
        setNaturaFoodValue("berry_medley", 4, 0.3f, "LIGHT_MEAL"); // Multiple berries mixed together
    }

    /**
     * Set food values for a specific Natura item.
     */
    private void setNaturaFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = NATURA_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "NATURA_" + category
        ));
    }

    /**
     * Handle flour recipe modifications for Natura.
     * 
     * NOTE: In modern versions, flour recipe handling should be done via TinkersConstructModule
     * since Natura functionality has been integrated into Tinkers' Construct.
     * 
     * The original Hunger Overhaul had options to remove flour crafting/smelting recipes.
     */
    private void handleFlourRecipes() {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // The original mod had these config options:
        // removeFlourCraftingRecipes
        // removeFlourSmeltingRecipe
        // addAlternateFlourCraftingRecipes

        // These would require modifying or removing recipes dynamically
        // This is complex in modern Minecraft and would require recipe event interception
        // For 1.20.1+, these should be handled by TinkersConstructModule instead

        if (config.crops.removeFlourCraftingRecipes) {
            HOReborn.LOGGER.debug("Natura flour crafting recipes removal requested (handled by Tinkers' Construct in 1.20.1+)");
        }

        if (config.crops.removeFlourSmeltingRecipe) {
            HOReborn.LOGGER.debug("Natura flour smelting recipe removal requested (handled by Tinkers' Construct in 1.20.1+)");
        }

        if (config.crops.addAlternateFlourCraftingRecipes) {
            HOReborn.LOGGER.debug("Natura alternate flour crafting recipes requested (handled by Tinkers' Construct in 1.20.1+)");
        }
    }

    /**
     * Check if Natura is loaded.
     */
    public static boolean isNaturaLoaded() {
        return naturaLoaded;
    }
}

