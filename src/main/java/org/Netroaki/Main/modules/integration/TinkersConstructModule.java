package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Tinkers' Construct mod.
 * Handles drying rack time modifications and food value adjustments.
 * 
 * NOTE: In 1.20.1+, Tinkers' Construct includes functionality that was previously
 * in Natura (berries, flour recipes). This module handles both Tinkers' Construct
 * and legacy Natura features.
 */
public class TinkersConstructModule {

    private static final String TCONSTRUCT_MOD_ID = "tconstruct";
    private static boolean tconstructLoaded = false;

    /**
     * Initialize Tinkers' Construct integration.
     * Also handles flour recipe modifications that were previously Natura-specific.
     */
    public void init() {
        tconstructLoaded = Platform.isModLoaded(TCONSTRUCT_MOD_ID);

        if (!tconstructLoaded) {
            HOReborn.LOGGER.info("Tinkers' Construct not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Tinkers' Construct integration (includes Natura functionality)");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }

        // Register drying rack modifications
        registerDryingRackModifications();
        
        // Handle flour recipe modifications (previously Natura-specific)
        handleFlourRecipes();
    }

    /**
     * Apply food value modifications to Tinkers' Construct items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Dried items (though these seem odd to be food - kept for compatibility)
     * Note: dried_clay and dried_brick are unusual food items, but kept for mod compatibility
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Dried items (unusual food items, kept for compatibility)
        setTconFoodValue("dried_clay", 1, 0.05f, "RAW_FOOD");
        setTconFoodValue("dried_brick", 1, 0.05f, "RAW_FOOD");
    }

    /**
     * Set food values for a specific Tinkers' Construct item.
     */
    private void setTconFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = TCONSTRUCT_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "TCONSTRUCT_" + category
        ));
    }

    /**
     * Register drying rack time modifications.
     * The original Hunger Overhaul had drying rack time multipliers.
     */
    private void registerDryingRackModifications() {
        // Tinkers' Construct has drying racks that can dry various items
        // The original mod had a dryingRackTimeMultiplier config option

        // This would require hooking into Tinkers' Construct's drying rack system
        // to modify drying times based on configuration

        // For now, this is a placeholder that would need mixin implementation
        // to intercept drying rack recipes and modify their timing

        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        if (config.crops.dryingRackTimeMultiplier > 1.0) {
            HOReborn.LOGGER.info("Tinkers' Construct drying rack time multiplier: {}",
                config.crops.dryingRackTimeMultiplier);
        }
    }

    /**
     * Handle flour recipe modifications.
     * These were previously Natura-specific but are now handled by Tinkers' Construct in 1.20.1+.
     */
    private void handleFlourRecipes() {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // The original mod had these config options for Natura:
        // removeFlourCraftingRecipes
        // removeFlourSmeltingRecipe
        // addAlternateFlourCraftingRecipes

        // These would require modifying or removing recipes dynamically
        // This is complex in modern Minecraft and would require recipe event interception

        if (config.crops.removeFlourCraftingRecipes) {
            HOReborn.LOGGER.info("Tinkers' Construct flour crafting recipes removal requested (placeholder)");
        }

        if (config.crops.removeFlourSmeltingRecipe) {
            HOReborn.LOGGER.info("Tinkers' Construct flour smelting recipe removal requested (placeholder)");
        }

        if (config.crops.addAlternateFlourCraftingRecipes) {
            HOReborn.LOGGER.info("Tinkers' Construct alternate flour crafting recipes requested (placeholder)");
        }
    }

    /**
     * Check if Tinkers' Construct is loaded.
     */
    public static boolean isTconstructLoaded() {
        return tconstructLoaded;
    }
}

