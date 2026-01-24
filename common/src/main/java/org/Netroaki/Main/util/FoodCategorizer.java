package org.Netroaki.Main.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import dev.architectury.platform.Platform;

import java.util.HashMap;
import java.util.Map;

public class FoodCategorizer {

    // Specific item categorizations
    private static final Map<Item, FoodCategory> SPECIFIC_CATEGORIES = new HashMap<>();

    public enum FoodCategory {
        RAW_FOOD(1), // Half shank - raw crops/meat
        COOKED_FOOD(2), // One shank - cooked food
        LIGHT_MEAL(4), // 2 shanks - light meals
        AVERAGE_MEAL(6), // 3-4 shanks - average meals
        LARGE_MEAL(8), // 5+ shanks - large meals
        FEAST(10); // 10+ shanks - feasts

        private final int nutritionValue;

        FoodCategory(int nutritionValue) {
            this.nutritionValue = nutritionValue;
        }

        public int getNutritionValue() {
            return nutritionValue;
        }
    }

    static {
        // Initialize specific item categorizations

        // Raw foods (half shank)
        SPECIFIC_CATEGORIES.put(Items.WHEAT, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.CARROT, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.POTATO, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.BEETROOT, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.APPLE, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.SWEET_BERRIES, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.GLOW_BERRIES, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.CHORUS_FRUIT, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.MELON_SLICE, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.PUMPKIN, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.BEEF, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.PORKCHOP, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.CHICKEN, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.MUTTON, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.RABBIT, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.COD, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.SALMON, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.TROPICAL_FISH, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.ROTTEN_FLESH, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.SPIDER_EYE, FoodCategory.RAW_FOOD);
        SPECIFIC_CATEGORIES.put(Items.POISONOUS_POTATO, FoodCategory.RAW_FOOD);

        // Cooked foods (one shank)
        SPECIFIC_CATEGORIES.put(Items.COOKED_BEEF, FoodCategory.COOKED_FOOD);
        SPECIFIC_CATEGORIES.put(Items.COOKED_PORKCHOP, FoodCategory.COOKED_FOOD);
        SPECIFIC_CATEGORIES.put(Items.COOKED_CHICKEN, FoodCategory.COOKED_FOOD);
        SPECIFIC_CATEGORIES.put(Items.COOKED_MUTTON, FoodCategory.COOKED_FOOD);
        SPECIFIC_CATEGORIES.put(Items.COOKED_RABBIT, FoodCategory.COOKED_FOOD);
        SPECIFIC_CATEGORIES.put(Items.COOKED_COD, FoodCategory.COOKED_FOOD);
        SPECIFIC_CATEGORIES.put(Items.COOKED_SALMON, FoodCategory.COOKED_FOOD);
        SPECIFIC_CATEGORIES.put(Items.BAKED_POTATO, FoodCategory.COOKED_FOOD);

        // Light meals (2 shanks) - Bread is here!
        SPECIFIC_CATEGORIES.put(Items.BREAD, FoodCategory.LIGHT_MEAL);
        SPECIFIC_CATEGORIES.put(Items.COOKIE, FoodCategory.LIGHT_MEAL);
        SPECIFIC_CATEGORIES.put(Items.CAKE, FoodCategory.LIGHT_MEAL);
        SPECIFIC_CATEGORIES.put(Items.PUMPKIN_PIE, FoodCategory.LIGHT_MEAL);
        SPECIFIC_CATEGORIES.put(Items.GOLDEN_APPLE, FoodCategory.LIGHT_MEAL);
        SPECIFIC_CATEGORIES.put(Items.ENCHANTED_GOLDEN_APPLE, FoodCategory.LIGHT_MEAL);
        SPECIFIC_CATEGORIES.put(Items.DRIED_KELP, FoodCategory.LIGHT_MEAL);
        SPECIFIC_CATEGORIES.put(Items.HONEY_BOTTLE, FoodCategory.LIGHT_MEAL);
        SPECIFIC_CATEGORIES.put(Items.MUSHROOM_STEW, FoodCategory.LIGHT_MEAL); // nerfed
        SPECIFIC_CATEGORIES.put(Items.BEETROOT_SOUP, FoodCategory.LIGHT_MEAL); // nerfed

        // Average meals (3-4 shanks)
        SPECIFIC_CATEGORIES.put(Items.RABBIT_STEW, FoodCategory.AVERAGE_MEAL);
        SPECIFIC_CATEGORIES.put(Items.SUSPICIOUS_STEW, FoodCategory.AVERAGE_MEAL);

        // Large meals (5+ shanks)
        // These are typically complex dishes or high-value foods

        // Feasts (10+ shanks)
        // Reserved for the most valuable foods
    }

    /**
     * Categorize a food item and return the appropriate nutrition value
     */
    public static int categorizeFood(Item item, FoodProperties originalProperties) {
        // Check for specific categorization first
        FoodCategory specificCategory = SPECIFIC_CATEGORIES.get(item);
        if (specificCategory != null) {
            return specificCategory.getNutritionValue();
        }

        // Auto-categorize based on original nutrition value
        int originalNutrition = originalProperties.getNutrition();

        if (originalNutrition <= 2) {
            return FoodCategory.RAW_FOOD.getNutritionValue();
        } else if (originalNutrition <= 4) {
            return FoodCategory.COOKED_FOOD.getNutritionValue();
        } else if (originalNutrition <= 6) {
            return FoodCategory.LIGHT_MEAL.getNutritionValue();
        } else if (originalNutrition <= 8) {
            return FoodCategory.AVERAGE_MEAL.getNutritionValue();
        } else if (originalNutrition <= 12) {
            return FoodCategory.LARGE_MEAL.getNutritionValue();
        } else {
            return FoodCategory.FEAST.getNutritionValue();
        }
    }

    /**
     * Get the food category description
     */
    public static String getFoodCategoryDescription(int nutritionValue) {
        if (nutritionValue >= 10) {
            return "Feast";
        } else if (nutritionValue >= 8) {
            return "Large Meal";
        } else if (nutritionValue >= 6) {
            return "Nourishing Meal";
        } else if (nutritionValue >= 4) {
            return "Light Meal";
        } else if (nutritionValue >= 2) {
            return "Cooked Food";
        } else {
            return "Raw Food";
        }
    }

    /**
     * Check if an item is from a mod (for additional nerfing)
     */
    public static boolean isModFood(Item item) {
        return FoodUtil.isModFood(item);
    }
}
