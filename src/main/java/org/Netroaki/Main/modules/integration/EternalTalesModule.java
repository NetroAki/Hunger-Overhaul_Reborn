package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

import java.util.Locale;

/**
 * Integration module for Eternal Tales mod.
 * Applies food value adjustments for the wide range of consumables the mod adds.
 */
public class EternalTalesModule {

    private static final String ETERNAL_TALES_MOD_ID = "eternal_tales";
    private static boolean eternalTalesLoaded = false;

    /**
     * Initialize Eternal Tales integration.
     */
    public void init() {
        eternalTalesLoaded = Platform.isModLoaded(ETERNAL_TALES_MOD_ID);

        if (!eternalTalesLoaded) {
            HOReborn.LOGGER.debug("Eternal Tales not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Eternal Tales integration");

        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Eternal Tales items.
     */
    @SuppressWarnings("deprecation")
    private void applyFoodValueModifications() {
        int applied = 0;

        for (ResourceLocation itemId : BuiltInRegistries.ITEM.keySet()) {
            if (!ETERNAL_TALES_MOD_ID.equals(itemId.getNamespace())) {
                continue;
            }

            EternalFoodValue values = determineFoodValue(itemId.getPath());
            FoodRegistry.setFoodValues(
                itemId.toString(),
                new org.Netroaki.Main.food.FoodValueData(values.hunger, values.saturation, "ETERNAL_TALES_" + values.category)
            );
            applied++;
        }

        HOReborn.LOGGER.info("Applied Eternal Tales food values to {} items", applied);
    }

    private EternalFoodValue determineFoodValue(String path) {
        String lower = path.toLowerCase(Locale.ROOT);

        // RAW_FOOD (1 hunger) - Unprocessed items
        if (containsAny(lower, "berries", "berry", "apple", "fruit", "pepper", "cabbage", "mushroom", "acorn", "vegetable")) {
            return new EternalFoodValue(1, 0.05f, "RAW_FOOD");
        }

        if (containsAny(lower, "raw", "uncooked")) {
            return new EternalFoodValue(1, 0.05f, "RAW_FOOD");
        }

        // LIGHT_MEAL (4 hunger) - Simple drinks and processed items
        if (containsAny(lower, "potion", "elixir", "flask", "tincture", "pills", "tonic")) {
            return new EternalFoodValue(4, 0.2f, "LIGHT_MEAL"); // Crafted potion items
        }

        if (containsAny(lower, "juice", "tea", "coffee", "cocoa", "milk", "cup", "bottle", "brew", "drink")) {
            return new EternalFoodValue(4, 0.2f, "LIGHT_MEAL");
        }

        // LIGHT_MEAL (4 hunger) - Simple prepared items
        if (containsAny(lower, "caviar", "sushi", "fish", "shrimp", "crab", "seafood", "catfish", "latimeria", "bubblefish")) {
            return new EternalFoodValue(4, 0.3f, "LIGHT_MEAL"); // Prepared seafood
        }

        if (containsAny(lower, "pie", "cake", "cookie", "candy", "cheesecake", "donut", "sweet", "pastry", "tart", "chocolate", "icecream")) {
            return new EternalFoodValue(4, 0.3f, "LIGHT_MEAL"); // Desserts
        }

        // AVERAGE_MEAL (6 hunger) - Complex meals
        if (containsAny(lower, "stew", "soup", "broth", "ragout", "goulash", "chowder", "porridge", "casserole", "hotpot")) {
            return new EternalFoodValue(6, 0.4f, "AVERAGE_MEAL"); // Multiple ingredients
        }

        if (containsAny(lower, "burger", "sandwich", "burrito", "wrap", "kebab", "skewer", "bbq", "barbecue", "platter")) {
            return new EternalFoodValue(6, 0.4f, "AVERAGE_MEAL"); // Assembled meals
        }

        if (containsAny(lower, "cooked", "roasted", "grilled", "fried", "smoked", "steak", "meat", "jerky", "bacon")) {
            return new EternalFoodValue(6, 0.4f, "AVERAGE_MEAL"); // Prepared meats as components
        }

        return new EternalFoodValue(4, 0.2f, "LIGHT_MEAL"); // Default to LIGHT_MEAL for unknowns
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
     * Check if Eternal Tales is loaded.
     */
    public static boolean isEternalTalesLoaded() {
        return eternalTalesLoaded;
    }

    private static class EternalFoodValue {
        private final int hunger;
        private final float saturation;
        private final String category;

        private EternalFoodValue(int hunger, float saturation, String category) {
            this.hunger = hunger;
            this.saturation = saturation;
            this.category = category;
        }
    }
}

