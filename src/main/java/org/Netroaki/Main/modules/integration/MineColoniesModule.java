package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

import java.util.Locale;

/**
 * Integration module for MineColonies mod.
 * Categorizes food items based on crafting complexity inferred from item names.
 * 
 * Meal Type Reasoning:
 * - RAW_FOOD: Raw crops and ingredients (no crafting required)
 * - COOKED_FOOD: Simply cooked items (furnace/smoker - single ingredient)
 * - LIGHT_MEAL: Simple crafted items (2-3 ingredients, basic recipes like bread)
 * - AVERAGE_MEAL: Moderate crafting (4-5 ingredients, soups, simple meals)
 * - LARGE_MEAL: Complex meals (6+ ingredients, multi-step recipes like dinners)
 */
public class MineColoniesModule {

    private static final String MINECOLONIES_MOD_ID = "minecolonies";
    private static boolean minecoloniesLoaded = false;

    /**
     * Initialize MineColonies integration.
     */
    public void init() {
        minecoloniesLoaded = Platform.isModLoaded(MINECOLONIES_MOD_ID);

        if (!minecoloniesLoaded) {
            HOReborn.LOGGER.debug("MineColonies not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing MineColonies integration");

        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to MineColonies items.
     * Uses pattern matching to categorize items based on crafting complexity.
     */
    @SuppressWarnings("deprecation")
    private void applyFoodValueModifications() {
        int applied = 0;

        for (ResourceLocation itemId : BuiltInRegistries.ITEM.keySet()) {
            if (!MINECOLONIES_MOD_ID.equals(itemId.getNamespace())) {
                continue;
            }

            MineColoniesFoodValue values = determineFoodValue(itemId.getPath());
            if (values != null) {
                FoodRegistry.setFoodValues(
                    itemId.toString(),
                    new org.Netroaki.Main.food.FoodValueData(values.hunger, values.saturation, "MINECOLONIES_" + values.category)
                );
                applied++;
            }
        }

        HOReborn.LOGGER.info("Applied MineColonies food values to {} items", applied);
    }

    /**
     * Determine food value based on item name patterns and inferred crafting complexity.
     */
    private MineColoniesFoodValue determineFoodValue(String path) {
        String lower = path.toLowerCase(Locale.ROOT);

        // RAW_FOOD (1 hunger, 0.05-0.1 saturation) - Raw crops and ingredients
        if (containsAny(lower, "bell_pepper", "butternut_squash", "cabbage", "chickpea", "corn", "durum", 
                        "eggplant", "garlic", "mint", "nether_pepper", "onion", "peas", "rice", "soybean", "tomato")) {
            // But exclude processed versions
            if (!lower.contains("cooked") && !lower.contains("soup") && !lower.contains("stew") && 
                !lower.contains("dinner") && !lower.contains("pasta") && !lower.contains("fried") &&
                !lower.contains("spicy") && !lower.contains("stuffed") && !lower.contains("dolma")) {
                return new MineColoniesFoodValue(1, 0.05f, "RAW_FOOD");
            }
        }

        // COOKED_FOOD (2 hunger, 0.2-0.3 saturation) - Simple cooking
        if (containsAny(lower, "cooked_rice", "baked_salmon")) {
            return new MineColoniesFoodValue(2, 0.25f, "COOKED_FOOD");
        }

        // LIGHT_MEAL (4 hunger, 0.2-0.3 saturation) - Simple crafting (2-3 ingredients)
        // Simple breads (flour + water + optional ingredient)
        if (containsAny(lower, "flatbread", "golden_bread", "manchet_bread", "milky_bread", "sugary_bread", "chorus_bread")) {
            return new MineColoniesFoodValue(4, 0.25f, "LIGHT_MEAL");
        }
        
        // Simple processed foods (basic processing)
        if (containsAny(lower, "tofu", "yogurt", "mint_tea", "mint_jelly", "cabochis")) {
            return new MineColoniesFoodValue(4, 0.2f, "LIGHT_MEAL");
        }
        
        // Simple snacks
        if (containsAny(lower, "rice_ball", "muffin")) {
            return new MineColoniesFoodValue(4, 0.25f, "LIGHT_MEAL");
        }
        
        // Simple cheeses (milk processing)
        if (containsAny(lower, "cheddar_cheese", "feta_cheese")) {
            return new MineColoniesFoodValue(4, 0.2f, "LIGHT_MEAL");
        }
        
        // Yogurt with berries (yogurt + berries)
        if (lower.equals("yogurt_with_berries")) {
            return new MineColoniesFoodValue(4, 0.25f, "LIGHT_MEAL");
        }

        // AVERAGE_MEAL (6 hunger, 0.35-0.4 saturation) - Moderate crafting (4-5 ingredients)
        // Soups and broths (multiple ingredients, cooking process)
        if (containsAny(lower, "chicken_broth", "corn_chowder", "eggdrop_soup", "pea_soup", "potato_soup", 
                        "squash_soup", "veggie_soup", "borscht", "congee", "ramen")) {
            return new MineColoniesFoodValue(6, 0.4f, "AVERAGE_MEAL");
        }
        
        // Simple pasta dishes (pasta + sauce + optional ingredients)
        if (containsAny(lower, "pasta_plain", "pasta_tomato")) {
            return new MineColoniesFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }
        
        // Simple meals (multiple ingredients but straightforward)
        if (containsAny(lower, "fried_rice", "polenta", "pottage")) {
            return new MineColoniesFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }
        
        // Simple pizzas (dough + cheese + toppings)
        if (containsAny(lower, "cheese_pizza", "mushroom_pizza")) {
            return new MineColoniesFoodValue(6, 0.4f, "AVERAGE_MEAL");
        }
        
        // Simple dishes (wraps, kebabs, etc.)
        if (containsAny(lower, "kebab", "pierogi", "tacos", "tortillas", "stuffed_pita", "hand_pie")) {
            return new MineColoniesFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }
        
        // Simple desserts
        if (containsAny(lower, "plain_cheesecake", "mintchoco_cheesecake")) {
            return new MineColoniesFoodValue(6, 0.4f, "AVERAGE_MEAL");
        }
        
        // Simple stuffed/processed vegetables
        if (containsAny(lower, "spicy_eggplant", "eggplant_dolma", "stuffed_pepper")) {
            return new MineColoniesFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }
        
        // Simple ravioli (pasta + filling)
        if (containsAny(lower, "cheese_ravioli", "meat_ravioli", "veggie_ravioli")) {
            return new MineColoniesFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }
        
        // Simple quiche (pastry + filling)
        if (lower.equals("veggie_quiche")) {
            return new MineColoniesFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }
        
        // Simple spreads
        if (containsAny(lower, "pepper_hummus", "pita_hummus")) {
            return new MineColoniesFoodValue(6, 0.4f, "AVERAGE_MEAL");
        }
        
        // Simple processed foods
        if (containsAny(lower, "kimchi", "spicy_grilled_chicken", "schnitzel")) {
            return new MineColoniesFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }
        
        // Simple pies
        if (lower.equals("apple_pie")) {
            return new MineColoniesFoodValue(6, 0.4f, "AVERAGE_MEAL");
        }
        
        // Simple bread variant
        if (lower.equals("lembas_scone")) {
            return new MineColoniesFoodValue(6, 0.4f, "AVERAGE_MEAL");
        }
        
        // Simple sushi
        if (lower.equals("sushi_roll")) {
            return new MineColoniesFoodValue(6, 0.35f, "AVERAGE_MEAL");
        }

        // LARGE_MEAL (8 hunger, 0.5 saturation) - Complex meals (6+ ingredients)
        // Full dinners (meat + sides + preparation)
        if (containsAny(lower, "fish_dinner", "mutton_dinner", "steak_dinner")) {
            return new MineColoniesFoodValue(8, 0.5f, "LARGE_MEAL");
        }
        
        // Complex dishes (fish and chips - fish + batter + chips + sides)
        if (lower.equals("fish_n_chips")) {
            return new MineColoniesFoodValue(8, 0.5f, "LARGE_MEAL");
        }
        
        // Complex stews (meat + vegetables + broth + seasoning)
        if (containsAny(lower, "lamb_stew")) {
            return new MineColoniesFoodValue(8, 0.5f, "LARGE_MEAL");
        }
        
        // Complex trencher (bread bowl + stew)
        if (lower.equals("stew_trencher")) {
            return new MineColoniesFoodValue(8, 0.5f, "LARGE_MEAL");
        }

        // Default fallback for unrecognized items
        return new MineColoniesFoodValue(4, 0.2f, "LIGHT_MEAL");
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
     * Check if MineColonies is loaded.
     */
    public static boolean isMinecoloniesLoaded() {
        return minecoloniesLoaded;
    }

    /**
     * Get the MineColonies mod ID.
     */
    public static String getMinecoloniesModId() {
        return MINECOLONIES_MOD_ID;
    }

    /**
     * Inner class to hold food value data with category.
     */
    private static class MineColoniesFoodValue {
        private final int hunger;
        private final float saturation;
        private final String category;

        private MineColoniesFoodValue(int hunger, float saturation, String category) {
            this.hunger = hunger;
            this.saturation = saturation;
            this.category = category;
        }
    }
}

