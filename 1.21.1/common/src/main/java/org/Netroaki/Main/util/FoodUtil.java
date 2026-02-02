package org.Netroaki.Main.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.component.DataComponents;

public class FoodUtil {

    public static List<Item> getAllFoodItems() {
        List<Item> foodItems = new ArrayList<>();

        for (Item item : BuiltInRegistries.ITEM) {
            // Check if item has food properties via Data Components
            if (item.components().has(DataComponents.FOOD)) {
                foodItems.add(item);
            }
        }

        return foodItems;
    }

    public static boolean isModFood(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null)
            return false;

        String namespace = id.getNamespace();
        return !namespace.equals("minecraft");
    }

    public static void setFoodProperties(Item item, FoodProperties properties) {
        // In 1.21.1, food properties are Data Components and immutability is stricter.
        // We cannot easily reflectively set a field on Item class as it doesn't exist
        // in same way.
        // For now, we log a warning that this is not supported without Mixins or
        // Component modification.
        // TODO: Implement component modification if possible
        // System.err.println("setFoodProperties not supported in 1.21.1 without
        // Mixins");
    }

    public static void setMaxStackSize(Item item, int maxStackSize) {
        // In 1.21.1, max stack size is a component.
        // properties.component(DataComponents.MAX_STACK_SIZE, size)
        // We cannot modify registered items easily.
        // System.err.println("setMaxStackSize not supported in 1.21.1 without Mixins");
    }

    public static String getFoodDescription(ItemStack stack) {
        // Use modified values to ensure tooltip matches actual effect
        var values = org.Netroaki.Main.handlers.FoodEventHandler.getModifiedFoodValues(stack);
        if (values == null)
            return "";

        int nutrition = values.hunger;
        float saturation = values.saturationModifier;

        return getFoodDescription(nutrition, saturation);
    }

    public static String getFoodDescription(int nutrition, float saturation) {
        if (nutrition >= 14)
            return "Feast";
        if (nutrition >= 10)
            return "Large Meal";
        if (nutrition >= 7)
            return "Nourishing Meal";
        if (nutrition >= 4)
            return "Light Meal";
        if (nutrition >= 2)
            return "Snack";
        return "Morsel";
    }

    public static int getWellFedDuration(int foodValue) {
        if (foodValue >= 14)
            return 480; // 8 minutes for feast
        if (foodValue >= 10)
            return 240; // 4 minutes for large meal
        if (foodValue >= 7)
            return 120; // 2 minutes for meal
        if (foodValue >= 4)
            return 40; // 40 seconds for light meal
        return 0; // No effect for snacks
    }

    public static int getEatingDuration(int foodValue) {
        if (foodValue >= 14)
            return 64; // Very slow for feast
        if (foodValue >= 10)
            return 48; // Slow for large meal
        if (foodValue >= 7)
            return 32; // Normal for meal
        if (foodValue >= 4)
            return 24; // Fast for light meal
        return 16; // Very fast for snacks
    }

    public static int getStackSizeForFoodValue(int foodValue) {
        if (foodValue >= 14)
            return 1; // Feast - single item
        if (foodValue >= 10)
            return 4; // Large meal - 4 items
        if (foodValue >= 7)
            return 8; // Meal - 8 items
        if (foodValue >= 4)
            return 16; // Light meal - 16 items
        return 32; // Snacks - 32 items
    }
}
