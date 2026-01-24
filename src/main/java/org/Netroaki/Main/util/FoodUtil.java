package org.Netroaki.Main.util;

import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FoodUtil {

    public static List<Item> getAllFoodItems() {
        List<Item> foodItems = new ArrayList<>();

        for (Item item : BuiltInRegistries.ITEM) {
            if (item.getFoodProperties() != null) {
                foodItems.add(item);
            }
        }

        return foodItems;
    }

    public static boolean isModFood(FoodProperties food) {
        // This is a simplified check - in a real implementation you'd need to track
        // which items are from mods vs vanilla
        return false;
    }

    public static boolean isModFood(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null)
            return false;

        String namespace = id.getNamespace();
        return !namespace.equals("minecraft");
    }

    public static void setFoodProperties(Item item, FoodProperties properties) {
        try {
            // Try different possible field names for food properties
            Field foodPropertiesField = null;
            String[] possibleFieldNames = { "foodProperties", "food", "foodData" };

            for (String fieldName : possibleFieldNames) {
                try {
                    foodPropertiesField = Item.class.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException ignored) {
                    // Try next field name
                }
            }

            if (foodPropertiesField != null) {
                foodPropertiesField.setAccessible(true);
                foodPropertiesField.set(item, properties);
            } else {
                System.err.println("Could not find food properties field for " + item);
            }
        } catch (Exception e) {
            // Log error but don't crash
            System.err.println("Failed to set food properties for " + item + ": " + e.getMessage());
        }
    }

    public static void setMaxStackSize(Item item, int maxStackSize) {
        try {
            // Try different possible field names for max stack size
            Field maxStackSizeField = null;
            String[] possibleFieldNames = { "maxStackSize", "maxCount", "maxStack" };

            for (String fieldName : possibleFieldNames) {
                try {
                    maxStackSizeField = Item.class.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException ignored) {
                    // Try next field name
                }
            }

            if (maxStackSizeField != null) {
                maxStackSizeField.setAccessible(true);
                maxStackSizeField.set(item, maxStackSize);
            } else {
                System.err.println("Could not find max stack size field for " + item);
            }
        } catch (Exception e) {
            // Log error but don't crash
            System.err.println("Failed to set max stack size for " + item + ": " + e.getMessage());
        }
    }

    public static String getFoodDescription(ItemStack stack) {
        FoodProperties food = stack.getItem().getFoodProperties();
        if (food == null)
            return "";

        int nutrition = food.getNutrition();
        float saturation = food.getSaturationModifier();

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

    public static boolean isHarvestCraftFood(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        return id != null && id.getNamespace().equals("harvestcraft");
    }
}
