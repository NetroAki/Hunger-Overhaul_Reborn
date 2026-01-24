package org.Netroaki.Main.util;

import java.util.Locale;

/**
 * Utility class for automatic food categorization based on item names and
 * patterns.
 * Provides both simple pattern matching and more sophisticated heuristic-based
 * categorization.
 */
public class FoodCategorizer {

    /**
     * Represents a categorized food value with hunger, saturation, and meal type.
     */
    public static class FoodValue {
        public final int hunger;
        public final float saturation;
        public final String mealType;

        public FoodValue(int hunger, float saturation, String mealType) {
            this.hunger = hunger;
            this.saturation = saturation;
            this.mealType = mealType;
        }
    }

    /**
     * Categorize a food item based on its name using advanced heuristics.
     * Handles complexity assessment, ingredient inference, and cooking method
     * detection.
     */
    public static FoodValue categorizeFood(String itemName) {
        String lower = itemName.toLowerCase(Locale.ROOT);

        // RAW_FOOD (1 hunger, 0.05f saturation)
        if (isRawFood(lower)) {
            return new FoodValue(1, 0.05f, "RAW_FOOD");
        }

        // COOKED_FOOD (2 hunger, 0.1-0.2f saturation)
        if (isProcessedIngredient(lower)) {
            return new FoodValue(2, 0.1f, "COOKED_FOOD");
        }

        if (isSimplyCookedItem(lower)) {
            return new FoodValue(2, 0.2f, "COOKED_FOOD");
        }

        // LIGHT_MEAL (4 hunger, 0.2-0.3f saturation)
        if (isLightMeal(lower)) {
            float saturation = assessLightMealSaturation(lower);
            return new FoodValue(4, saturation, "LIGHT_MEAL");
        }

        // AVERAGE_MEAL (6 hunger, 0.4f saturation)
        if (isAverageMeal(lower)) {
            return new FoodValue(6, 0.4f, "AVERAGE_MEAL");
        }

        // LARGE_MEAL (8 hunger, 0.5f saturation)
        if (isLargeMeal(lower)) {
            return new FoodValue(8, 0.5f, "LARGE_MEAL");
        }

        // Default: LIGHT_MEAL for unknown prepared items
        return new FoodValue(4, 0.2f, "LIGHT_MEAL");
    }

    /**
     * Determine if an item is raw/unprocessed food.
     */
    private static boolean isRawFood(String lower) {
        // Explicitly raw items
        if (containsAny(lower, "raw", "fresh", "uncooked") &&
                !containsAny(lower, "cooked", "baked", "roasted", "grilled", "fried", "sauteed", "smoked")) {
            return true;
        }

        // Unprocessed crops and produce
        if (containsAny(lower, "seed", "sprout", "berry", "apple", "fruit", "vegetable", "crop")) {
            return !containsAny(lower, "cooked", "baked", "dried", "powder");
        }

        // Raw meats and seafood
        if (containsAny(lower, "meat", "fish", "flesh", "fillet", "shrimp", "crab", "lobster") &&
                !containsAny(lower, "cooked", "baked", "grilled", "fried", "roasted", "smoked")) {
            return true;
        }

        return false;
    }

    /**
     * Determine if an item is a processed single ingredient (butter, sauce, powder,
     * etc).
     */
    private static boolean isProcessedIngredient(String lower) {
        return containsAny(lower, "powder", "spice", "seasoning", "extract", "essence",
                "jam", "jelly", "butter", "syrup", "sauce", "spread", "dressing",
                "dip", "hummus", "ketchup", "mustard", "mayonnaise", "paste",
                "cheese", "cream", "yogurt", "milk");
    }

    /**
     * Determine if an item is simply cooked (furnace/smoker cooked).
     */
    private static boolean isSimplyCookedItem(String lower) {
        if (containsAny(lower, "cooked_", "roasted_", "smoked_")) {
            // Make sure it's not a complex meal component
            return !containsAny(lower, "dinner", "plate", "meal", "stew", "soup");
        }

        return false;
    }

    /**
     * Determine if an item is a LIGHT_MEAL (simple prepared items, 2-3
     * ingredients).
     */
    private static boolean isLightMeal(String lower) {
        // Beverages
        if (containsAny(lower, "juice", "smoothie", "milkshake", "milk", "tea", "coffee",
                "cider", "shake", "latte", "mocha", "float", "soda", "punch",
                "lemonade", "frappe", "cocoa", "drink")) {
            return true;
        }

        // Simple desserts
        if (containsAny(lower, "yogurt", "pudding", "custard", "gelatin", "sorbet",
                "icecream", "popsicle", "snowcone", "slush", "candy", "lollipop")) {
            return true;
        }

        // Simple prepared foods (salads, snacks)
        if (containsAny(lower, "salad", "salsa", "slaw", "coleslaw", "relish", "medley",
                "tabbouleh", "kimchi", "chutney", "jerky", "chips", "fries",
                "crisps", "popcorn", "pretzel", "cracker", "granola", "trail", "snack")) {
            return true;
        }

        // Breakfast items
        if (containsAny(lower, "pancake", "waffle", "toast", "omelet", "omelette",
                "quiche", "frittata", "crepe", "hash", "breakfast", "egg")) {
            return true;
        }

        // Simple pastries and baked goods
        if (containsAny(lower, "cookie", "pie_slice", "cake_slice", "donut", "doughnut",
                "brownie", "muffin", "cupcake", "tart", "biscuit", "scone")) {
            return true;
        }

        // Sandwiches and wraps (simple ones, not deluxe)
        if (containsAny(lower, "sandwich", "wrap", "roll") &&
                !containsAny(lower, "club", "deluxe", "special", "double", "triple")) {
            return true;
        }

        return false;
    }

    /**
     * Determine if an item is an AVERAGE_MEAL (4-5 ingredients, complex
     * preparation).
     */
    private static boolean isAverageMeal(String lower) {
        // Soups and stews (definitely 4+ ingredients)
        if (containsAny(lower, "soup", "stew", "broth", "curry", "gumbo", "chowder",
                "hotpot", "pho", "ramen", "udon")) {
            return true;
        }

        // Prepared pasta dishes
        if (containsAny(lower, "pasta", "noodle", "spaghetti", "fettuccine", "mac",
                "macaroni", "gnocchi", "ravioli", "risotto")) {
            return true;
        }

        // Main dishes with meat/protein
        if (containsAny(lower, "burger", "sandwich", "burrito", "pizza", "calzone",
                "quesadilla", "enchilada", "fajita", "taco", "gyro", "kebab",
                "panini", "sub", "hoagie")) {
            return true;
        }

        // Prepared meat dishes
        if (containsAny(lower, "steak", "roast", "meatloaf", "meatball", "cutlet",
                "fillet", "porkchop", "drumstick", "ribs", "chop")) {
            return true;
        }

        // Casseroles, baked dishes, complex combinations
        if (containsAny(lower, "casserole", "bake", "gratins", "stuffed", "lasagna",
                "pot_pie", "shepherd", "stroganoff")) {
            return true;
        }

        // Salads with protein/complex components
        if (containsAny(lower, "salad") && containsAny(lower, "chicken", "beef", "fish",
                "egg", "protein", "caesar")) {
            return true;
        }

        return false;
    }

    /**
     * Determine if an item is a LARGE_MEAL (6+ ingredients, very complex
     * preparation).
     */
    private static boolean isLargeMeal(String lower) {
        // Full platters and special dishes
        if (containsAny(lower, "platter", "feast", "banquet", "special_meal",
                "full_breakfast", "dinner_plate")) {
            return true;
        }

        // Complex multi-component meals
        if (containsAny(lower, "full_", "complete_", "ultimate_", "supreme_", "deluxe_") &&
                containsAny(lower, "meal", "dinner", "platter", "feast")) {
            return true;
        }

        // Very complex deluxe items
        if (containsAny(lower, "deluxe", "premium", "gourmet") &&
                (containsAny(lower, "burger", "sandwich", "pizza", "meal") || countUnderscore(lower) >= 5)) {
            return true;
        }

        return false;
    }

    /**
     * Assess saturation level for LIGHT_MEAL items based on complexity.
     */
    private static float assessLightMealSaturation(String lower) {
        // More complex/ingredients = higher saturation
        if (containsAny(lower, "yogurt", "pudding", "custard", "gelatin")) {
            return 0.25f; // Dairy-based, more filling
        }

        if (containsAny(lower, "salad", "salsa", "slaw", "coleslaw")) {
            return 0.3f; // Multi-ingredient base
        }

        if (containsAny(lower, "pancake", "waffle", "omelet", "quiche")) {
            return 0.3f; // Substantial base items
        }

        if (containsAny(lower, "sandwich", "wrap", "roll", "burrito") &&
                countUnderscore(lower) >= 3) {
            return 0.3f; // Multiple ingredients
        }

        // Default for most light meals
        return 0.2f;
    }

    /**
     * Count underscore occurrences to estimate ingredient complexity.
     */
    private static int countUnderscore(String text) {
        return (int) text.chars().filter(ch -> ch == '_').count();
    }

    /**
     * Check if text contains any of the given tokens (case-insensitive).
     */
    private static boolean containsAny(String text, String... tokens) {
        for (String token : tokens) {
            if (token != null && !token.isEmpty() && text.contains(token)) {
                return true;
            }
        }
        return false;
    }

}
