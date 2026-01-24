package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Integration module for various Pam's mods (Temperate Plants, Random Plants, Weee Flowers, etc.).
 * Provides generic food value adjustments for Pam's mod ecosystem.
 */
public class PamsModsModule {

    // List of Pam's mods to check for
    private static final List<String> PAMS_MODS = Arrays.asList(
        "pamhc2foodextended",
        "pamhc2foodcore",
        "pamhc2crops",
        "pamhc2trees",
        "temperateplants",
        "randomplants",
        "weeeflowers",
        "gardenstuff",
        "saltwatermod"
    );

    private static boolean anyPamsModsLoaded = false;

    /**
     * Initialize Pam's mods integration.
     */
    public void init() {
        anyPamsModsLoaded = PAMS_MODS.stream().anyMatch(Platform::isModLoaded);

        if (!anyPamsModsLoaded) {
            HOReborn.LOGGER.info("No Pam's mods detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing Pam's mods integration");

        // Apply food value modifications to all detected Pam's mods
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }
    }

    /**
     * Apply food value modifications to Pam's mod items.
     */
    private void applyFoodValueModifications() {
        // Apply generic food balancing to all Pam's mods
        for (String modId : PAMS_MODS) {
            if (Platform.isModLoaded(modId)) {
                applyModFoodValues(modId);
            }
        }
    }

    /**
     * Apply food values to a specific Pam's mod.
     */
    @SuppressWarnings("deprecation")
    private void applyModFoodValues(String modId) {
        // Generic food value application for Pam's mods
        // This would apply reasonable defaults to food items from these mods
        var itemRegistry = BuiltInRegistries.ITEM;
        int applied = 0;
        for (Item item : itemRegistry) {
            if (item == null) {
                continue;
            }
            ResourceLocation itemId = itemRegistry.getKey(item);
            if (itemId != null && modId.equals(itemId.getNamespace())) {
                String path = itemId.getPath();
                PamsFoodValue foodValue = calculatePamsFoodValue(modId, path);
                setPamsFoodValue(itemId.toString(), foodValue.hunger, foodValue.saturation, modId);
                applied++;
            }
        }

        if (applied > 0) {
            HOReborn.LOGGER.info("Applied food values to {} items from {}", applied, modId);
        } else {
            HOReborn.LOGGER.debug("No items from {} required food value adjustments", modId);
        }
    }

    /**
     * Determine meal type category based on hunger value and item path.
     */
    private String getMealTypeCategory(String path, int hunger) {
        String lower = path.toLowerCase(Locale.ROOT);
        
        // RAW_FOOD (1 hunger) - Raw ingredients, seeds, powders, raw items
        if (hunger == 1 || containsAny(lower, "seed", "sprout", "powder", "spice", "seasoning", "extract", "essence", "raw", "fresh")) {
            return "RAW_FOOD";
        }
        
        // COOKED_FOOD (2 hunger) - Simply cooked items
        if (hunger == 2 && containsAny(lower, "baked", "roasted", "grilled", "fried", "sauteed", "steamed", "smoked", "toasted", "cooked")) {
            return "COOKED_FOOD";
        }
        
        // LIGHT_MEAL (4 hunger) - Simple crafting (2-3 ingredients)
        if (hunger <= 4 && !containsAny(lower, "soup", "stew", "curry", "burger", "sandwich", "pasta", "lasagna")) {
            return "LIGHT_MEAL";
        }
        
        // AVERAGE_MEAL (6 hunger) - Moderate crafting (4-5 ingredients)
        if (hunger == 5 || hunger == 6) {
            return "AVERAGE_MEAL";
        }
        
        // Default to LIGHT_MEAL for hunger 3-4
        return "LIGHT_MEAL";
    }

    /**
     * Set food values for a specific Pam's mod item.
     */
    private void setPamsFoodValue(String itemId, int hunger, float saturation, String modId) {
        String path = itemId.contains(":") ? itemId.split(":")[1] : itemId;
        String mealType = getMealTypeCategory(path, hunger);
        FoodRegistry.setFoodValues(itemId, new org.Netroaki.Main.food.FoodValueData(
            hunger, saturation, "PAMS_" + modId.toUpperCase(Locale.ROOT) + "_" + mealType
        ));
    }

    /**
     * Check if any Pam's mods are loaded.
     */
    public static boolean isAnyPamsModsLoaded() {
        return anyPamsModsLoaded;
    }

    /**
     * Get the list of supported Pam's mods.
     */
    public static List<String> getPamsMods() {
        return PAMS_MODS;
    }

    private PamsFoodValue calculatePamsFoodValue(String modId, String path) {
        String lower = path.toLowerCase(Locale.ROOT);

        // RAW_FOOD (1 hunger) - Truly raw, unprocessed items
        if (containsAny(lower, "raw", "fresh") && !containsAny(lower, "cooked", "baked", "roasted", "grilled", "fried")) {
            return new PamsFoodValue(1, 0.05f);
        }

        if (containsAny(lower, "seed", "sprout")) {
            return new PamsFoodValue(1, 0.05f);
        }

        // COOKED_FOOD (2 hunger) - Processed/prepared single ingredients
        if (containsAny(lower, "powder", "spice", "seasoning", "extract", "essence")) {
            return new PamsFoodValue(2, 0.1f);
        }

        if (containsAny(lower, "jam", "jelly", "butter", "syrup", "sauce", "spread", "dressing", "dip", "hummus", "guacamole", "ketchup", "mustard", "mayonnaise", "paste")) {
            return new PamsFoodValue(2, 0.1f);
        }

        // Pam's tree mods processing
        if ("pamhc2trees".equals(modId)) {
            if (containsAny(lower, "syrup", "butter", "paste")) {
                return new PamsFoodValue(2, 0.1f); // Processed tree product
            }
            return new PamsFoodValue(1, 0.05f); // Raw fruit
        }

        if ("pamhc2crops".equals(modId)) {
            if (lower.startsWith("baked") || containsAny(lower, "baked", "roasted", "grilled", "fried", "sauteed", "steamed", "smoked", "toasted")) {
                return new PamsFoodValue(4, 0.3f); // Cooked meal component
            }
            return new PamsFoodValue(1, 0.05f); // Raw crop
        }

        // LIGHT_MEAL (4 hunger) - Simple drinks and beverages
        if (containsAny(lower, "juice", "smoothie", "milkshake", "milk", "tea", "coffee", "cider", "shake", "latte", "mocha", "float", "soda", "punch", "lemonade", "frappe", "cocoa")) {
            return new PamsFoodValue(4, 0.2f);
        }

        // LIGHT_MEAL (4 hunger) - Simple desserts and processed items
        if (containsAny(lower, "yogurt", "pudding", "custard", "gelatin", "sorbet", "icecream", "popsicle", "snowcone", "slush")) {
            return new PamsFoodValue(4, 0.25f);
        }

        if (containsAny(lower, "salad", "salsa", "coleslaw", "slaw", "relish", "medley", "medly", "mix", "tabbouleh", "kimchi", "chutney")) {
            return new PamsFoodValue(4, 0.3f);
        }

        if (containsAny(lower, "pancake", "waffle", "toast", "omelet", "omelette", "quiche", "frittata", "crepe", "hash", "breakfast")) {
            return new PamsFoodValue(4, 0.3f);
        }

        if (containsAny(lower, "jerky", "chips", "fries", "crisps", "popcorn", "pretzel", "cracker", "granola", "trailmix", "snackbar", "proteinbar", "snack", "nuts")) {
            return new PamsFoodValue(4, 0.2f);
        }

        if (containsAny(lower, "cookie", "cake", "pie", "donut", "doughnut", "brownie", "muffin", "cupcake", "tart", "strudel", "fritter", "candy", "truffle", "sweet", "baklava", "brittle")) {
            return new PamsFoodValue(4, 0.3f);
        }

        // AVERAGE_MEAL (6 hunger) - Prepared meals with multiple components
        if (containsAny(lower, "soup", "stew", "curry", "gumbo", "chowder", "hotpot", "broth", "pho", "ramen", "udon", "noodle", "risotto", "paella", "jambalaya", "tagine", "stirfry", "stir_fry", "ragout", "ragù")) {
            return new PamsFoodValue(6, 0.4f);
        }

        if (containsAny(lower, "burger", "sandwich", "burrito", "wrap", "pizza", "calzone", "quesadilla", "enchilada", "fajita", "taco", "gyro", "kebab", "panini", "sub", "hoagie", "po'boy")) {
            return new PamsFoodValue(6, 0.4f);
        }

        if (containsAny(lower, "pasta", "spaghetti", "fettuccine", "mac", "macaroni", "gnocchi", "ravioli", "dumpling", "pierogi", "potpie", "shepherd", "casserole", "stuffed", "bake", "gratins", "hashbrown", "skillet")) {
            return new PamsFoodValue(6, 0.4f);
        }

        if (containsAny(lower, "sausage", "steak", "roast", "ham", "meatloaf", "meatball", "cutlet", "fillet", "porkchop", "drumstick", "kabob")) {
            return new PamsFoodValue(6, 0.4f);
        }

        if (containsAny(lower, "bacon", "egg", "sausage", "ham", "lasagna")) {
            return new PamsFoodValue(6, 0.4f);
        }

        // Default to LIGHT_MEAL if cooked but unspecified
        if (containsAny(lower, "baked", "roasted", "grilled", "fried", "sauteed", "smoked", "glazed", "braised")) {
            return new PamsFoodValue(4, 0.3f);
        }

        return new PamsFoodValue(4, 0.2f); // Default to LIGHT_MEAL for unknown prepared items
    }

    private boolean containsAny(String text, String... tokens) {
        for (String token : tokens) {
            if (token != null && !token.isEmpty() && text.contains(token)) {
                return true;
            }
        }
        return false;
    }

    private static class PamsFoodValue {
        private final int hunger;
        private final float saturation;

        private PamsFoodValue(int hunger, float saturation) {
            this.hunger = hunger;
            this.saturation = saturation;
        }
    }
}

