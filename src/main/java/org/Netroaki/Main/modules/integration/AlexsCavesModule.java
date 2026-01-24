package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

import java.util.Locale;

/**
 * Integration module for Alex's Caves mod.
 * Categorizes food items based on crafting complexity inferred from item names.
 * 
 * Meal Type Reasoning:
 * - RAW_FOOD: Raw fish, produce, and ingredients (no crafting required)
 * - COOKED_FOOD: Simply cooked items (furnace/smoker - single ingredient)
 * - LIGHT_MEAL: Simple crafted items (2-3 ingredients, basic recipes)
 * - AVERAGE_MEAL: Moderate crafting (4-5 ingredients, multiple steps)
 * - LARGE_MEAL: Complex meals (6+ ingredients, advanced crafting)
 */
public class AlexsCavesModule {

    private static final String ALEXS_CAVES_MOD_ID = "alexscaves";
    private static boolean alexsCavesLoaded = false;

    /**
     * Initialize Alex's Caves integration.
     */
    public void init() {
        alexsCavesLoaded = Platform.isModLoaded(ALEXS_CAVES_MOD_ID);

        if (!alexsCavesLoaded) {
            HOReborn.LOGGER.debug("Alex's Caves not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Alex's Caves integration");

        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Alex's Caves items.
     * Uses pattern matching to categorize items based on crafting complexity.
     */
    @SuppressWarnings("deprecation")
    private void applyFoodValueModifications() {
        int applied = 0;

        for (ResourceLocation itemId : BuiltInRegistries.ITEM.keySet()) {
            if (!ALEXS_CAVES_MOD_ID.equals(itemId.getNamespace())) {
                continue;
            }

            AlexsCavesFoodValue values = determineFoodValue(itemId.getPath());
            if (values != null) {
                FoodRegistry.setFoodValues(
                    itemId.toString(),
                    new org.Netroaki.Main.food.FoodValueData(values.hunger, values.saturation, "ALEXS_CAVES_" + values.category)
                );
                applied++;
            }
        }

        HOReborn.LOGGER.info("Applied Alex's Caves food values to {} items", applied);
    }

    /**
     * Determine food value based on item name patterns and inferred crafting complexity.
     */
    private AlexsCavesFoodValue determineFoodValue(String path) {
        String lower = path.toLowerCase(Locale.ROOT);

        // Skip non-edible blocks (building blocks, decorative items)
        if (containsAny(lower, "block", "slab", "stairs", "wall", "door", "pole", "brick", "chiseled", "stripped", "barrel", "crumbs", "powder", "vine")) {
            // But allow edible blocks like chocolate blocks, cookie blocks
            if (!containsAny(lower, "chocolate", "cookie", "gingerbread", "wafer", "dough", "frosting", "caramel")) {
                return null; // Skip non-edible building blocks
            }
        }

        // RAW_FOOD (1 hunger, 0.05-0.1 saturation) - Raw ingredients, no crafting
        // Raw fish and seafood
        if (containsAny(lower, "lanternfish", "radgill", "trilocaris_tail", "tripodfish", "mussel", "sea_pig", "stinky_fish", "sweetish_fish")) {
            if (!lower.contains("cooked")) {
                return new AlexsCavesFoodValue(1, 0.05f, "RAW_FOOD");
            }
        }
        
        // Raw produce and ingredients
        if (containsAny(lower, "licoroot", "licoroot_sprout", "pine_nuts", "frostmint", "giant_sweetberry", "darkened_apple", "peppermint")) {
            if (!lower.contains("powder") && !lower.contains("large") && !lower.contains("small")) {
                return new AlexsCavesFoodValue(1, 0.05f, "RAW_FOOD");
            }
        }

        // COOKED_FOOD (2 hunger, 0.2-0.3 saturation) - Simple cooking (furnace/smoker)
        if (containsAny(lower, "cooked_lanternfish", "cooked_mussel", "cooked_radgill", "cooked_trilocaris_tail", "cooked_tripodfish")) {
            return new AlexsCavesFoodValue(2, 0.25f, "COOKED_FOOD");
        }
        
        // Vesper wing - cooked mob drop
        if (lower.equals("vesper_wing")) {
            return new AlexsCavesFoodValue(2, 0.2f, "COOKED_FOOD");
        }

        // LIGHT_MEAL (4 hunger, 0.2-0.3 saturation) - Simple crafting (2-3 ingredients)
        // Simple candies and sweets (sugar + flavoring)
        if (containsAny(lower, "candy_cane", "caramel_apple", "gelatin", "gummy_ring", "jelly_bean", "lollipop", "rock_candy", "sprinkles", "gumball")) {
            return new AlexsCavesFoodValue(4, 0.2f, "LIGHT_MEAL");
        }
        
        // Simple drinks (liquid + flavoring)
        if (containsAny(lower, "hot_chocolate_bottle", "purple_soda_bottle", "green_soylent")) {
            return new AlexsCavesFoodValue(4, 0.25f, "LIGHT_MEAL");
        }
        
        // Ice cream (milk + sugar + flavoring)
        if (containsAny(lower, "chocolate_ice_cream", "sweetberry_ice_cream", "vanilla_ice_cream", "sundae")) {
            return new AlexsCavesFoodValue(4, 0.2f, "LIGHT_MEAL");
        }
        
        // Simple processed foods
        if (containsAny(lower, "dinosaur_nugget", "sweet_puff", "sundrop", "slam", "spelunkie")) {
            return new AlexsCavesFoodValue(4, 0.25f, "LIGHT_MEAL");
        }
        
        // Simple blocks (multiple ingredients but basic recipe)
        if (containsAny(lower, "block_of_chocolate", "cookie_block", "wafer_cookie", "dough_block", "cake_layer")) {
            return new AlexsCavesFoodValue(4, 0.2f, "LIGHT_MEAL");
        }
        
        // Caramel (sugar processing)
        if (lower.equals("caramel")) {
            return new AlexsCavesFoodValue(4, 0.2f, "LIGHT_MEAL");
        }
        
        // Peppermint powder (processed ingredient)
        if (lower.equals("peppermint_powder")) {
            return new AlexsCavesFoodValue(4, 0.15f, "LIGHT_MEAL");
        }
        
        // Large/small peppermint (processed)
        if (containsAny(lower, "large_peppermint", "small_peppermint")) {
            return new AlexsCavesFoodValue(4, 0.2f, "LIGHT_MEAL");
        }
        
        // Sugar glass (processed sugar)
        if (lower.equals("sugar_glass")) {
            return new AlexsCavesFoodValue(4, 0.15f, "LIGHT_MEAL");
        }
        
        // Sharpened candy cane (crafted weapon/food)
        if (lower.equals("sharpened_candy_cane")) {
            return new AlexsCavesFoodValue(4, 0.2f, "LIGHT_MEAL");
        }

        // AVERAGE_MEAL (6 hunger, 0.35-0.4 saturation) - Moderate crafting (4-5 ingredients)
        // Complex meals requiring multiple ingredients
        if (lower.equals("alex_meal")) {
            // Named meal, likely complex - multiple ingredients
            return new AlexsCavesFoodValue(6, 0.4f, "AVERAGE_MEAL");
        }
        
        // Sushi roll (fish + rice + seaweed + other ingredients)
        if (lower.equals("deep_sea_sushi_roll")) {
            return new AlexsCavesFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }
        
        // Salads (multiple vegetables/ingredients)
        if (lower.equals("serene_salad")) {
            return new AlexsCavesFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }
        
        // Complex soups/stews (multiple ingredients, cooking process)
        if (containsAny(lower, "primordial_soup", "seething_stew", "vesper_stew")) {
            return new AlexsCavesFoodValue(6, 0.4f, "AVERAGE_MEAL");
        }
        
        // Frosted/processed blocks (chocolate + frosting + processing)
        if (containsAny(lower, "block_of_chocolate_frosting", "block_of_frosted_chocolate", "block_of_frosting", "block_of_vanilla_frosting", 
                        "block_of_polished_chocolate", "block_of_chiseled_chocolate", "frosted_gingerbread")) {
            return new AlexsCavesFoodValue(6, 0.4f, "AVERAGE_MEAL");
        }
        
        // Gingerbread blocks (flour + sugar + spices + crafting)
        if (containsAny(lower, "gingerbread_block", "gingerbread_brick")) {
            return new AlexsCavesFoodValue(6, 0.4f, "AVERAGE_MEAL");
        }

        // Default fallback for unrecognized items
        return new AlexsCavesFoodValue(4, 0.2f, "LIGHT_MEAL");
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
     * Check if Alex's Caves is loaded.
     */
    public static boolean isAlexsCavesLoaded() {
        return alexsCavesLoaded;
    }

    /**
     * Get the Alex's Caves mod ID.
     */
    public static String getAlexsCavesModId() {
        return ALEXS_CAVES_MOD_ID;
    }

    /**
     * Inner class to hold food value data with category.
     */
    private static class AlexsCavesFoodValue {
        private final int hunger;
        private final float saturation;
        private final String category;

        private AlexsCavesFoodValue(int hunger, float saturation, String category) {
            this.hunger = hunger;
            this.saturation = saturation;
            this.category = category;
        }
    }
}

