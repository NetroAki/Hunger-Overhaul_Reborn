package org.Netroaki.Main.food;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.applecore.FoodValues;
import org.Netroaki.Main.util.FoodCategorizer;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for managing food values with JSON support.
 */
public class FoodRegistry {
    private static final Map<String, FoodValueData> foodValues = new HashMap<>();
    private static boolean initialized = false;

    public static void initialize() {
        if (initialized)
            return;

        for (Item item : BuiltInRegistries.ITEM) {
            if (item.getFoodProperties() == null)
                continue;

            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            if (id == null)
                continue;

            String itemId = id.toString();

            if (foodValues.containsKey(itemId))
                continue;

            String itemName = id.getPath();
            FoodCategorizer.FoodValue categorized = FoodCategorizer.categorizeFood(itemName);

            foodValues.put(itemId, new FoodValueData(
                    categorized.hunger,
                    categorized.saturation,
                    categorized.mealType));
        }

        initialized = true;
        HOReborn.LOGGER.info("FoodRegistry (1.21.1) initialized with {} food items", foodValues.size());
    }

    public static FoodValues getFoodValues(ItemStack stack) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id == null)
            return FoodValues.getUnmodified(stack);

        String itemId = id.toString();
        FoodValueData data = foodValues.get(itemId);

        FoodValues unmodified = FoodValues.getUnmodified(stack);

        if (data == null) {
            foodValues.put(itemId, new FoodValueData(
                    unmodified.hunger,
                    unmodified.saturationModifier,
                    "AUTO_GENERATED"));
            return unmodified;
        }

        return new FoodValues(data.hunger, data.saturation);
    }

    public static FoodValues getFoodValuesForPlayer(ItemStack stack, net.minecraft.world.entity.player.Player player) {
        return getFoodValues(stack);
    }

    public static void setFoodValues(String itemId, FoodValueData data) {
        foodValues.put(itemId, data);
    }

    public static Map<String, FoodValueData> getAllFoodValues() {
        return new HashMap<>(foodValues);
    }

    public static boolean hasFoodValues(String itemId) {
        return foodValues.containsKey(itemId);
    }

    public static void clear() {
        foodValues.clear();
        initialized = false;
    }

    public static int size() {
        return foodValues.size();
    }
}
