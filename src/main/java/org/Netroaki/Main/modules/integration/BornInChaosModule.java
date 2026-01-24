package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

import java.util.Locale;

/**
 * Integration module for Born in Chaos mod.
 * Categorizes food items based on crafting complexity inferred from item names.
 * 
 * Meal Type Reasoning:
 * - RAW_FOOD: Raw monster parts and ingredients (no crafting required)
 * - COOKED_FOOD: Simply cooked items (furnace/smoker - single ingredient)
 * - LIGHT_MEAL: Simple crafted items (2-3 ingredients, candies, simple potions)
 * - AVERAGE_MEAL: Moderate crafting (4-5 ingredients, complex candies, special desserts)
 */
public class BornInChaosModule {

    private static final String BORN_IN_CHAOS_MOD_ID = "born_in_chaos_v1";
    private static boolean bornInChaosLoaded = false;

    /**
     * Initialize Born in Chaos integration.
     */
    public void init() {
        bornInChaosLoaded = Platform.isModLoaded(BORN_IN_CHAOS_MOD_ID);

        if (!bornInChaosLoaded) {
            HOReborn.LOGGER.debug("Born in Chaos not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Born in Chaos integration");

        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Born in Chaos items.
     * Uses pattern matching to categorize items based on crafting complexity.
     */
    @SuppressWarnings("deprecation")
    private void applyFoodValueModifications() {
        int applied = 0;

        for (ResourceLocation itemId : BuiltInRegistries.ITEM.keySet()) {
            if (!BORN_IN_CHAOS_MOD_ID.equals(itemId.getNamespace())) {
                continue;
            }

            BornInChaosFoodValue values = determineFoodValue(itemId.getPath());
            if (values != null) {
                FoodRegistry.setFoodValues(
                    itemId.toString(),
                    new org.Netroaki.Main.food.FoodValueData(values.hunger, values.saturation, "BORN_IN_CHAOS_" + values.category)
                );
                applied++;
            }
        }

        HOReborn.LOGGER.info("Applied Born in Chaos food values to {} items", applied);
    }

    /**
     * Determine food value based on item name patterns and inferred crafting complexity.
     */
    private BornInChaosFoodValue determineFoodValue(String path) {
        String lower = path.toLowerCase(Locale.ROOT);

        // RAW_FOOD (1 hunger, 0.05-0.1 saturation) - Raw monster parts and ingredients
        if (containsAny(lower, "monster_flesh", "corpse_maggot", "bloody_gadfly_eye", "sea_terror_eye", "rotten_fish")) {
            // But exclude cooked/processed versions
            if (!lower.contains("fried") && !lower.contains("smoked") && !lower.contains("cooked")) {
                return new BornInChaosFoodValue(1, 0.05f, "RAW_FOOD");
            }
        }

        // COOKED_FOOD (2 hunger, 0.2-0.3 saturation) - Simple cooking
        if (containsAny(lower, "fried_maggot", "smoked_fish", "smoked_flesh", "smoked_monster_flesh")) {
            return new BornInChaosFoodValue(2, 0.25f, "COOKED_FOOD");
        }

        // LIGHT_MEAL (4 hunger, 0.2-0.3 saturation) - Simple crafting (2-3 ingredients)
        // Simple candies (sugar + flavoring)
        if (containsAny(lower, "caramel_pepper", "coffee_candy", "mint_candy", "holiday_candy", "eternal_candy", "gummy_vampire_teeth")) {
            return new BornInChaosFoodValue(4, 0.2f, "LIGHT_MEAL");
        }
        
        // Simple desserts (basic recipes)
        if (containsAny(lower, "chocolate_heart", "mint_ice_cream", "creepy_cookies_with_milk", "spiritual_gingerbread")) {
            return new BornInChaosFoodValue(4, 0.25f, "LIGHT_MEAL");
        }
        
        // Simple potions/elixirs (liquid + ingredients)
        if (containsAny(lower, "bottle_of_magical_energy", "elixir_of_vampirism", "elixirof_ice_barrier", 
                        "elixirof_insect_protection", "elixirof_wither_resistance", "potion_of_rampage", 
                        "intoxicating_decoction", "stimulating_decoction")) {
            return new BornInChaosFoodValue(4, 0.2f, "LIGHT_MEAL");
        }

        // AVERAGE_MEAL (6 hunger, 0.35-0.4 saturation) - Moderate crafting (4-5 ingredients)
        // Complex candies (multiple ingredients, special processing)
        if (lower.equals("magical_holiday_candy")) {
            return new BornInChaosFoodValue(6, 0.4f, "AVERAGE_MEAL");
        }
        
        // Complex desserts (multiple ingredients, special effects)
        if (lower.equals("transforming_easter_cake")) {
            return new BornInChaosFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }

        // Default fallback for unrecognized items
        return new BornInChaosFoodValue(4, 0.2f, "LIGHT_MEAL");
    }

    private boolean containsAny(String text, String... tokens) {
        for (String token : tokens) {
            if (token != null && !token.isEmpty() && text.contains(token)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if Born in Chaos is loaded.
     */
    public static boolean isBornInChaosLoaded() {
        return bornInChaosLoaded;
    }

    /**
     * Get the Born in Chaos mod ID.
     */
    public static String getBornInChaosModId() {
        return BORN_IN_CHAOS_MOD_ID;
    }

    /**
     * Inner class to hold food value data with category.
     */
    private static class BornInChaosFoodValue {
        private final int hunger;
        private final float saturation;
        private final String category;

        private BornInChaosFoodValue(int hunger, float saturation, String category) {
            this.hunger = hunger;
            this.saturation = saturation;
            this.category = category;
        }
    }
}

