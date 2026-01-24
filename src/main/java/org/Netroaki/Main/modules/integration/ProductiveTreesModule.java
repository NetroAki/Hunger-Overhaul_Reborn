package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Productive Trees mod.
 * Handles food value modifications for tree fruits and products.
 */
public class ProductiveTreesModule {

    private static final String PRODUCTIVE_TREES_MOD_ID = "productivetrees";
    private static boolean productiveTreesLoaded = false;

    /**
     * Initialize Productive Trees integration.
     */
    public void init() {
        productiveTreesLoaded = Platform.isModLoaded(PRODUCTIVE_TREES_MOD_ID);

        if (!productiveTreesLoaded) {
            HOReborn.LOGGER.debug("Productive Trees not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Productive Trees integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Productive Trees items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw nuts and fruits (no crafting)
     * - COOKED_FOOD: Simply roasted nuts (furnace)
     * - LIGHT_MEAL: Processed items (maple syrup, date palm juice - 2-3 ingredients)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Raw nuts and fruits
        setProductiveTreesFoodValue("almond", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("brazil_nut", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("beechnut", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("butternut", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("candlenut", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("cashew", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("chestnut", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("ginkgo_nut", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("hazelnut", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("pecan", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("pistachio", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("walnut", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Simply roasted nuts
        setProductiveTreesFoodValue("roasted_brazil_nut", 2, 0.1f, "COOKED_FOOD");
        setProductiveTreesFoodValue("roasted_beechnut", 2, 0.1f, "COOKED_FOOD");
        setProductiveTreesFoodValue("roasted_butternut", 2, 0.1f, "COOKED_FOOD");
        setProductiveTreesFoodValue("roasted_candlenut", 2, 0.1f, "COOKED_FOOD");
        setProductiveTreesFoodValue("roasted_cashew", 2, 0.1f, "COOKED_FOOD");
        setProductiveTreesFoodValue("roasted_chestnut", 2, 0.15f, "COOKED_FOOD");
        setProductiveTreesFoodValue("roasted_ginkgo_nut", 2, 0.1f, "COOKED_FOOD");
        setProductiveTreesFoodValue("roasted_hazelnut", 2, 0.1f, "COOKED_FOOD");
        setProductiveTreesFoodValue("roasted_pecan", 2, 0.1f, "COOKED_FOOD");
        setProductiveTreesFoodValue("roasted_pistachio", 2, 0.1f, "COOKED_FOOD");
        setProductiveTreesFoodValue("roasted_walnut", 2, 0.1f, "COOKED_FOOD");
        
        // RAW_FOOD (1 hunger) - Raw fruits
        setProductiveTreesFoodValue("apricot", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("asai_berry", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("avocado", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("banana", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("red_banana", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("baobab_fruit", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("beliy_naliv_apple", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("black_cherry", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("breadfruit", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("buddhas_hand", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("cempedak", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("cherry_plum", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("citron", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("coconut", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("copoazu", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("date", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("elderberry", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("fig", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("finger_lime", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("flowering_crabapple", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("golden_delicious_apple", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("granny_smith_apple", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("grapefruit", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("hala_fruit", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("haw", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("jackfruit", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("juniper_berry", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("key_lime", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("kumquat", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("lemon", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("lime", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("mandarin", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("mango", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("nectarine", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("olive", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("orange", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("osange_orange", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("papaya", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("peach", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("planet_peach", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("pear", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("sand_pear", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("persimmon", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("plantain", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("plum", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("pomegranate", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("pomelo", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("prairie_crabapple", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("rowan", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("satsuma", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("sloe", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("snake_fruit", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("sour_cherry", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("soursop", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("sparkling_cherry", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("star_fruit", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("sweet_crabapple", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("sweetsop", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("tangerine", 1, 0.05f, "RAW_FOOD");
        setProductiveTreesFoodValue("wild_cherry", 1, 0.05f, "RAW_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Processed items (2-3 ingredients)
        setProductiveTreesFoodValue("maple_syrup", 4, 0.2f, "LIGHT_MEAL"); // Sap + processing
        setProductiveTreesFoodValue("date_palm_juice", 4, 0.2f, "LIGHT_MEAL"); // Dates + processing
    }

    /**
     * Set food values for a specific Productive Trees item.
     */
    private void setProductiveTreesFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = PRODUCTIVE_TREES_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "PRODUCTIVE_TREES_" + category
        ));
    }

    /**
     * Check if Productive Trees is loaded.
     */
    public static boolean isProductiveTreesLoaded() {
        return productiveTreesLoaded;
    }

    /**
     * Get the Productive Trees mod ID.
     */
    public static String getProductiveTreesModId() {
        return PRODUCTIVE_TREES_MOD_ID;
    }
}

