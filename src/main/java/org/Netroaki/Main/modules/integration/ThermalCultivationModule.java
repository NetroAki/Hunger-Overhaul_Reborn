package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Thermal Cultivation mod.
 * Handles food value modifications and crop growth adjustments.
 */
public class ThermalCultivationModule {

    private static final String THERMAL_CULTIVATION_MOD_ID = "thermal";
    private static boolean thermalCultivationLoaded = false;

    /**
     * Initialize Thermal Cultivation integration.
     */
    public void init() {
        thermalCultivationLoaded = Platform.isModLoaded(THERMAL_CULTIVATION_MOD_ID);

        if (!thermalCultivationLoaded) {
            HOReborn.LOGGER.debug("Thermal Cultivation not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Thermal Cultivation integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Thermal Cultivation items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw crops, fruits (no crafting)
     * - COOKED_FOOD: Simply cooked items (furnace/smoker)
     * - LIGHT_MEAL: Simple processed foods, simple meals (2-3 ingredients)
     * - AVERAGE_MEAL: Complex meals, stews (4-5 ingredients)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw crops, fruits
        setThermalCultivationFoodValue("bell_pepper", 1, 0.05f, "RAW_FOOD");
        setThermalCultivationFoodValue("corn", 1, 0.05f, "RAW_FOOD");
        setThermalCultivationFoodValue("eggplant", 1, 0.05f, "RAW_FOOD");
        setThermalCultivationFoodValue("green_bean", 1, 0.05f, "RAW_FOOD");
        setThermalCultivationFoodValue("onion", 1, 0.05f, "RAW_FOOD");
        setThermalCultivationFoodValue("peanut", 1, 0.05f, "RAW_FOOD");
        setThermalCultivationFoodValue("radish", 1, 0.05f, "RAW_FOOD");
        setThermalCultivationFoodValue("spinach", 1, 0.05f, "RAW_FOOD");
        setThermalCultivationFoodValue("strawberry", 1, 0.05f, "RAW_FOOD");
        setThermalCultivationFoodValue("tomato", 1, 0.05f, "RAW_FOOD");
        setThermalCultivationFoodValue("frost_melon_slice", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simply cooked items and processed ingredients
        setThermalCultivationFoodValue("syrup_bottle", 2, 0.1f, "COOKED_FOOD"); // Processed syrup
        setThermalCultivationFoodValue("cooked_corn", 2, 0.2f, "COOKED_FOOD");
        setThermalCultivationFoodValue("cooked_eggplant", 2, 0.2f, "COOKED_FOOD");
        setThermalCultivationFoodValue("cooked_mushroom", 2, 0.2f, "COOKED_FOOD");
        setThermalCultivationFoodValue("cheese_wedge", 2, 0.1f, "COOKED_FOOD"); // Processed cheese
        
        // LIGHT_MEAL (4 hunger) - Simple processed foods, simple meals (2-3 ingredients)
        setThermalCultivationFoodValue("coffee", 4, 0.2f, "LIGHT_MEAL"); // Coffee beans + water + processing
        setThermalCultivationFoodValue("pbj_sandwich", 4, 0.3f, "LIGHT_MEAL"); // Bread + peanut butter + jam
        setThermalCultivationFoodValue("spring_salad", 4, 0.3f, "LIGHT_MEAL"); // Vegetables + dressing
        setThermalCultivationFoodValue("green_bean_pie", 4, 0.3f, "LIGHT_MEAL"); // Green beans + dough + optional
        setThermalCultivationFoodValue("sushi_maki", 4, 0.3f, "LIGHT_MEAL"); // Rice + seaweed + optional filling
        
        // AVERAGE_MEAL (6 hunger) - Complex meals, stews (4-5 ingredients)
        setThermalCultivationFoodValue("stuffed_pepper", 6, 0.4f, "AVERAGE_MEAL"); // Pepper + filling + optional ingredients
        setThermalCultivationFoodValue("hearty_stew", 6, 0.4f, "AVERAGE_MEAL"); // Meat + vegetables + broth + seasoning
        setThermalCultivationFoodValue("xp_stew", 6, 0.4f, "AVERAGE_MEAL"); // Special ingredients + broth + processing
    }

    /**
     * Set food values for a specific Thermal Cultivation item.
     */
    private void setThermalCultivationFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = THERMAL_CULTIVATION_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "THERMAL_CULTIVATION_" + category
        ));
    }

    /**
     * Check if Thermal Cultivation is loaded.
     */
    public static boolean isThermalCultivationLoaded() {
        return thermalCultivationLoaded;
    }

    /**
     * Get the Thermal Cultivation mod ID.
     */
    public static String getThermalCultivationModId() {
        return THERMAL_CULTIVATION_MOD_ID;
    }
}

