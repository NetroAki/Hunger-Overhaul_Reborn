package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

import java.util.Locale;

/**
 * Integration module for The Abyss mod.
 * Categorizes food items based on crafting complexity inferred from item names.
 * 
 * Meal Type Reasoning:
 * - RAW_FOOD: Raw meat and fish (no crafting required)
 * - COOKED_FOOD: Simply cooked items (furnace/smoker - single ingredient)
 * - LIGHT_MEAL: Simple crafted items (2-3 ingredients, potions, essences, upgrades)
 * - AVERAGE_MEAL: Moderate crafting (4-5 ingredients, enchanted potions)
 * - LARGE_MEAL: Complex items (6+ ingredients, special/immortal items)
 */
public class TheAbyssModule {

    private static final String THE_ABYSS_MOD_ID = "theabyss";
    private static boolean theAbyssLoaded = false;

    /**
     * Initialize The Abyss integration.
     */
    public void init() {
        theAbyssLoaded = Platform.isModLoaded(THE_ABYSS_MOD_ID);

        if (!theAbyssLoaded) {
            HOReborn.LOGGER.debug("The Abyss not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing The Abyss integration");

        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to The Abyss items.
     * Uses pattern matching to categorize items based on crafting complexity.
     */
    @SuppressWarnings("deprecation")
    private void applyFoodValueModifications() {
        int applied = 0;

        for (ResourceLocation itemId : BuiltInRegistries.ITEM.keySet()) {
            if (!THE_ABYSS_MOD_ID.equals(itemId.getNamespace())) {
                continue;
            }

            TheAbyssFoodValue values = determineFoodValue(itemId.getPath());
            if (values != null) {
                FoodRegistry.setFoodValues(
                    itemId.toString(),
                    new org.Netroaki.Main.food.FoodValueData(values.hunger, values.saturation, "THE_ABYSS_" + values.category)
                );
                applied++;
            }
        }

        HOReborn.LOGGER.info("Applied The Abyss food values to {} items", applied);
    }

    /**
     * Determine food value based on item name patterns and inferred crafting complexity.
     */
    private TheAbyssFoodValue determineFoodValue(String path) {
        String lower = path.toLowerCase(Locale.ROOT);

        // RAW_FOOD (1 hunger, 0.05-0.1 saturation) - Raw meat and fish
        if (containsAny(lower, "deer_beef", "fish", "rotten_flesh")) {
            // But exclude cooked versions
            if (!lower.contains("cooked")) {
                return new TheAbyssFoodValue(1, 0.05f, "RAW_FOOD");
            }
        }

        // COOKED_FOOD (2 hunger, 0.2-0.3 saturation) - Simple cooking
        if (containsAny(lower, "cooked_deer_beef", "cooked_fish")) {
            return new TheAbyssFoodValue(2, 0.25f, "COOKED_FOOD");
        }

        // LIGHT_MEAL (4 hunger, 0.2-0.3 saturation) - Simple crafting (2-3 ingredients)
        // Simple produce
        if (lower.equals("jungle_melon_item")) {
            return new TheAbyssFoodValue(4, 0.2f, "LIGHT_MEAL");
        }
        
        // Simple potions/essences (liquid + ingredients)
        if (containsAny(lower, "bottle_of_somnium", "anti_fear_essence", "anti_infect_essence", "lurker_juice")) {
            return new TheAbyssFoodValue(4, 0.2f, "LIGHT_MEAL");
        }
        
        // Simple upgrades (processed items)
        if (containsAny(lower, "somnium_cooldown_upgrade", "somnium_damage_upragde", "somnium_regen_upgrade", "somnium_upgrade")) {
            return new TheAbyssFoodValue(4, 0.2f, "LIGHT_MEAL");
        }

        // AVERAGE_MEAL (6 hunger, 0.35-0.4 saturation) - Moderate crafting (4-5 ingredients)
        // Complex potions (enchanted versions)
        if (lower.equals("enchanted_bottle_of_somnium")) {
            return new TheAbyssFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }

        // LARGE_MEAL (8 hunger, 0.5 saturation) - Complex items (6+ ingredients, special effects)
        // Special/immortal items (very complex crafting, special effects)
        if (containsAny(lower, "apple_of_immortality", "immortal_substance", "node_shard")) {
            return new TheAbyssFoodValue(8, 0.5f, "LARGE_MEAL");
        }

        // Default fallback for unrecognized items
        return new TheAbyssFoodValue(4, 0.2f, "LIGHT_MEAL");
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
     * Check if The Abyss is loaded.
     */
    public static boolean isTheAbyssLoaded() {
        return theAbyssLoaded;
    }

    /**
     * Get the The Abyss mod ID.
     */
    public static String getTheAbyssModId() {
        return THE_ABYSS_MOD_ID;
    }

    /**
     * Inner class to hold food value data with category.
     */
    private static class TheAbyssFoodValue {
        private final int hunger;
        private final float saturation;
        private final String category;

        private TheAbyssFoodValue(int hunger, float saturation, String category) {
            this.hunger = hunger;
            this.saturation = saturation;
            this.category = category;
        }
    }
}

