package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

/**
 * Integration module for Delightful mod.
 * Handles food value modifications for Delightful items.
 */
public class DelightfulModule {

    private static final String DELIGHTFUL_MOD_ID = "delightful";
    private static boolean delightfulLoaded = false;

    /**
     * Initialize Delightful integration.
     */
    public void init() {
        delightfulLoaded = Platform.isModLoaded(DELIGHTFUL_MOD_ID);

        if (!delightfulLoaded) {
            HOReborn.LOGGER.debug("Delightful not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Delightful integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Delightful items.
     * Meal Type Reasoning:
     * - RAW_FOOD: Raw fruits, ingredients, raw meats (no crafting)
     * - COOKED_FOOD: Simply cooked meats (furnace/smoker)
     * - LIGHT_MEAL: Simple processed foods, snacks, simple meals, pies, ice cream, cookies (2-3 ingredients)
     * - AVERAGE_MEAL: Complex meals, stews, curries (4-5 ingredients)
     */
    private void applyFoodValueModifications() {
        // RAW_FOOD (1 hunger) - Only truly raw, unprocessed items
        setDelightfulFoodValue("salmonberries", 1, 0.05f, "RAW_FOOD");
        setDelightfulFoodValue("cantaloupe_slice", 1, 0.05f, "RAW_FOOD");
        setDelightfulFoodValue("acorn", 1, 0.05f, "RAW_FOOD");
        setDelightfulFoodValue("green_tea_leaf", 1, 0.05f, "RAW_FOOD");
        setDelightfulFoodValue("cactus_flesh", 1, 0.05f, "RAW_FOOD");
        setDelightfulFoodValue("raw_goat", 1, 0.05f, "RAW_FOOD");
        setDelightfulFoodValue("venison_chops", 1, 0.05f, "RAW_FOOD");
        
        // COOKED_FOOD (2 hunger) - Processed and prepared ingredients
        setDelightfulFoodValue("animal_fat", 2, 0.1f, "COOKED_FOOD"); // Rendered fat, processed ingredient
        setDelightfulFoodValue("chopped_clover", 2, 0.1f, "COOKED_FOOD"); // Processed/chopped ingredient
        setDelightfulFoodValue("nut_dough", 2, 0.1f, "COOKED_FOOD"); // Prepared dough mixture
        
        // LIGHT_MEAL (4 hunger) - Candies and sweets
        setDelightfulFoodValue("rock_candy", 4, 0.2f, "LIGHT_MEAL"); // Crafted candy, sweet treat
        
        // COOKED_FOOD (2 hunger) - Simply cooked meats
        setDelightfulFoodValue("roasted_acorn", 2, 0.1f, "COOKED_FOOD");
        setDelightfulFoodValue("cooked_goat", 2, 0.25f, "COOKED_FOOD");
        setDelightfulFoodValue("cooked_venison_chops", 2, 0.25f, "COOKED_FOOD");
        setDelightfulFoodValue("cooked_marshmallow_stick", 2, 0.2f, "COOKED_FOOD");
        
        // LIGHT_MEAL (4 hunger) - Simple processed foods, snacks, simple meals (2-3 ingredients)
        setDelightfulFoodValue("nut_butter_bottle", 4, 0.2f, "LIGHT_MEAL"); // Nuts + processing
        setDelightfulFoodValue("matcha", 4, 0.2f, "LIGHT_MEAL"); // Tea leaves + processing
        setDelightfulFoodValue("matcha_latte", 4, 0.25f, "LIGHT_MEAL"); // Matcha + milk
        setDelightfulFoodValue("berry_matcha_latte", 4, 0.25f, "LIGHT_MEAL"); // Matcha + milk + berries
        setDelightfulFoodValue("azalea_tea", 4, 0.2f, "LIGHT_MEAL"); // Azalea + water
        setDelightfulFoodValue("lavender_tea", 4, 0.2f, "LIGHT_MEAL"); // Lavender + water
        setDelightfulFoodValue("prickly_pear_juice", 4, 0.2f, "LIGHT_MEAL"); // Prickly pear + processing
        setDelightfulFoodValue("long_prickly_pear_juice", 4, 0.25f, "LIGHT_MEAL"); // Prickly pear + processing + optional
        setDelightfulFoodValue("jam_jar", 4, 0.2f, "LIGHT_MEAL"); // Fruit + sugar
        setDelightfulFoodValue("glow_jam_jar", 4, 0.2f, "LIGHT_MEAL"); // Glow berries + sugar
        setDelightfulFoodValue("honey_glazed_walnut", 4, 0.2f, "LIGHT_MEAL"); // Walnut + honey
        setDelightfulFoodValue("ender_nectar", 4, 0.25f, "LIGHT_MEAL"); // Ender + processing
        
        // LIGHT_MEAL (4 hunger) - Snacks and candies
        setDelightfulFoodValue("cantaloupe_gummy", 4, 0.2f, "LIGHT_MEAL"); // Cantaloupe + sugar + gelatin
        setDelightfulFoodValue("cantaloupe_popsicle", 4, 0.2f, "LIGHT_MEAL"); // Cantaloupe + stick + freezing
        setDelightfulFoodValue("salmonberry_gummy", 4, 0.2f, "LIGHT_MEAL"); // Salmonberry + sugar + gelatin
        setDelightfulFoodValue("source_berry_gummy", 4, 0.2f, "LIGHT_MEAL"); // Source berry + sugar + gelatin
        setDelightfulFoodValue("matcha_gummy", 4, 0.2f, "LIGHT_MEAL"); // Matcha + sugar + gelatin
        setDelightfulFoodValue("marshmallow_stick", 4, 0.2f, "LIGHT_MEAL"); // Sugar + gelatin + stick
        setDelightfulFoodValue("smore", 4, 0.3f, "LIGHT_MEAL"); // Marshmallow + chocolate + graham cracker
        
        // LIGHT_MEAL (4 hunger) - Simple meals
        setDelightfulFoodValue("cantaloupe_bread", 4, 0.25f, "LIGHT_MEAL"); // Flour + cantaloupe
        setDelightfulFoodValue("nut_butter_and_jam_sandwich", 4, 0.3f, "LIGHT_MEAL"); // Bread + nut butter + jam
        setDelightfulFoodValue("chunkwich", 4, 0.3f, "LIGHT_MEAL"); // Bread + chunks
        setDelightfulFoodValue("crab_rangoon", 4, 0.25f, "LIGHT_MEAL"); // Crab + wrapper + optional
        setDelightfulFoodValue("cactus_chili", 4, 0.2f, "LIGHT_MEAL"); // Cactus + spices
        setDelightfulFoodValue("cactus_steak", 4, 0.3f, "LIGHT_MEAL"); // Cactus + cooking
        setDelightfulFoodValue("field_salad", 4, 0.3f, "LIGHT_MEAL"); // Vegetables + dressing
        
        // LIGHT_MEAL (4 hunger) - Pies and desserts
        setDelightfulFoodValue("salmonberry_pie_slice", 4, 0.2f, "LIGHT_MEAL"); // Pie slice = fruit + dough + sugar
        setDelightfulFoodValue("pumpkin_pie_slice", 4, 0.2f, "LIGHT_MEAL");
        setDelightfulFoodValue("green_apple_pie_slice", 4, 0.2f, "LIGHT_MEAL");
        setDelightfulFoodValue("blueberry_pie_slice", 4, 0.2f, "LIGHT_MEAL");
        setDelightfulFoodValue("chorus_pie_slice", 4, 0.2f, "LIGHT_MEAL");
        setDelightfulFoodValue("mulberry_pie_slice", 4, 0.2f, "LIGHT_MEAL");
        setDelightfulFoodValue("gloomgourd_pie_slice", 4, 0.2f, "LIGHT_MEAL");
        setDelightfulFoodValue("source_berry_pie_slice", 4, 0.2f, "LIGHT_MEAL");
        setDelightfulFoodValue("passion_fruit_tart_slice", 4, 0.2f, "LIGHT_MEAL");
        setDelightfulFoodValue("baklava_slice", 4, 0.3f, "LIGHT_MEAL"); // Pastry + nuts + honey
        
        // LIGHT_MEAL (4 hunger) - Ice cream and milkshakes
        setDelightfulFoodValue("matcha_ice_cream", 4, 0.2f, "LIGHT_MEAL"); // Matcha + milk + sugar + freezing
        setDelightfulFoodValue("salmonberry_ice_cream", 4, 0.2f, "LIGHT_MEAL"); // Salmonberry + milk + sugar + freezing
        setDelightfulFoodValue("source_berry_ice_cream", 4, 0.2f, "LIGHT_MEAL"); // Source berry + milk + sugar + freezing
        setDelightfulFoodValue("matcha_milkshake", 4, 0.2f, "LIGHT_MEAL"); // Matcha + milk + ice cream
        setDelightfulFoodValue("salmonberry_milkshake", 4, 0.2f, "LIGHT_MEAL"); // Salmonberry + milk + ice cream
        setDelightfulFoodValue("source_berry_milkshake", 4, 0.2f, "LIGHT_MEAL"); // Source berry + milk + ice cream
        
        // LIGHT_MEAL (4 hunger) - Cookies
        setDelightfulFoodValue("glow_jam_cookie", 4, 0.2f, "LIGHT_MEAL"); // Dough + glow jam
        setDelightfulFoodValue("source_berry_cookie", 4, 0.2f, "LIGHT_MEAL"); // Dough + source berry
        setDelightfulFoodValue("chorus_muffin", 4, 0.25f, "LIGHT_MEAL"); // Dough + chorus fruit + optional
        
        // AVERAGE_MEAL (6 hunger) - Complex meals, soups, stews, curries (4-5 ingredients)
        setDelightfulFoodValue("cactus_soup", 6, 0.4f, "AVERAGE_MEAL"); // Cactus + broth + optional vegetables
        setDelightfulFoodValue("cactus_soup_cup", 6, 0.4f, "AVERAGE_MEAL"); // Same as cactus_soup
        setDelightfulFoodValue("cheeseburger", 6, 0.4f, "AVERAGE_MEAL"); // Bun + meat + cheese + toppings
        setDelightfulFoodValue("deluxe_cheeseburger", 6, 0.4f, "AVERAGE_MEAL"); // Bun + meat + cheese + extra toppings
        setDelightfulFoodValue("venison_stew", 6, 0.4f, "AVERAGE_MEAL"); // Venison + vegetables + broth + seasoning
        setDelightfulFoodValue("venison_stew_cup", 6, 0.4f, "AVERAGE_MEAL"); // Same as venison_stew
        setDelightfulFoodValue("coconut_curry", 6, 0.4f, "AVERAGE_MEAL"); // Coconut + vegetables + spices + optional meat
        setDelightfulFoodValue("sinigang", 6, 0.4f, "AVERAGE_MEAL"); // Meat + vegetables + souring agent + broth
        setDelightfulFoodValue("stuffed_cantaloupe", 6, 0.4f, "AVERAGE_MEAL"); // Cantaloupe + filling + optional ingredients
        setDelightfulFoodValue("wrapped_cantaloupe", 6, 0.35f, "AVERAGE_MEAL"); // Cantaloupe + wrapper + optional
    }

    /**
     * Set food values for a specific Delightful item.
     */
    private void setDelightfulFoodValue(String itemName, int hunger, float saturation, String category) {
        String fullId = DELIGHTFUL_MOD_ID + ":" + itemName;
        FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "DELIGHTFUL_" + category
        ));
    }

    /**
     * Check if Delightful is loaded.
     */
    public static boolean isDelightfulLoaded() {
        return delightfulLoaded;
    }

    /**
     * Get the Delightful mod ID.
     */
    public static String getDelightfulModId() {
        return DELIGHTFUL_MOD_ID;
    }
}

