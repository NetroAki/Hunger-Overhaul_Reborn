package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Aquaculture 2 mod.
 * Handles food value modifications for fish items.
 */
public class AquacultureModule {

    private static final String AQUACULTURE_MOD_ID = "aquaculture";
    private static boolean aquacultureLoaded = false;

    /**
     * Initialize Aquaculture integration.
     */
    public void init() {
        aquacultureLoaded = Platform.isModLoaded(AQUACULTURE_MOD_ID);

        if (!aquacultureLoaded) {
            HOReborn.LOGGER.debug("Aquaculture 2 not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Aquaculture 2 integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Aquaculture items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw fish and algae (no crafting)
     * - COOKED_FOOD: Simply cooked fish (furnace/smoker)
     * - AVERAGE_MEAL: Complex meals (sushi, soup - 4-5 ingredients)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw fish and algae
        setAquacultureFoodValue("fish_fillet_raw", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("algae", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("arapaima", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("atlantic_cod", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("atlantic_halibut", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("atlantic_herring", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("bayad", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("blackfish", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("bluegill", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("boulti", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("brown_shrooma", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("brown_trout", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("capitaine", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("carp", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("catfish", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("gar", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("muskellunge", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("pacific_halibut", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("perch", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("pink_salmon", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("piranha", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("pollock", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("rainbow_trout", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("red_grouper", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("red_shrooma", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("smallmouth_bass", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("synodontis", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("tambaqui", 1, 0.05f, "RAW_FOOD");
        setAquacultureFoodValue("tuna", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simply cooked fish
        setAquacultureFoodValue("fish_fillet_cooked", 2, 0.25f, "COOKED_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Simple prepared fish meals
        // (Note: sushi and turtle soup are complex enough for AVERAGE_MEAL below)
        
        // AVERAGE_MEAL (6 hunger) - Complex meals (4+ ingredients)
        setAquacultureFoodValue("sushi", 6, 0.4f, "AVERAGE_MEAL"); // Fish + rice + seaweed + vegetables
        setAquacultureFoodValue("turtle_soup", 6, 0.4f, "AVERAGE_MEAL"); // Turtle + vegetables + broth
    }

    /**
     * Set food values for a specific Aquaculture item.
     */
    private void setAquacultureFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = AQUACULTURE_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "AQUACULTURE_" + category
        ));
    }

    /**
     * Check if Aquaculture is loaded.
     */
    public static boolean isAquacultureLoaded() {
        return aquacultureLoaded;
    }

    /**
     * Get the Aquaculture mod ID.
     */
    public static String getAquacultureModId() {
        return AQUACULTURE_MOD_ID;
    }
}

