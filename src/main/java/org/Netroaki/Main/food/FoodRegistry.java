package org.Netroaki.Main.food;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.applecore.FoodEvent;
import org.Netroaki.Main.applecore.FoodValues;
import org.Netroaki.Main.util.FoodCategorizer;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for managing food values with JSON support.
 * Provides auto-generation and manual override capabilities.
 */
public class FoodRegistry {
    private static final Map<String, FoodValueData> foodValues = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Initialize the food registry with auto-generated values.
     */
    public static void initialize() {
        if (initialized) return;

        // Auto-generate food values for all food items
        for (Item item : BuiltInRegistries.ITEM) {
            if (item.getFoodProperties() == null) continue;

            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            if (id == null) continue;

            String itemId = id.toString();

            // Skip if already in registry (manual override)
            if (foodValues.containsKey(itemId)) continue;

            // Auto-categorize and generate values
            String itemName = id.getPath();
            FoodCategorizer.FoodValue categorized = FoodCategorizer.categorizeFood(itemName);

            // Store the categorized values
            foodValues.put(itemId, new FoodValueData(
                categorized.hunger,
                categorized.saturation,
                categorized.mealType
            ));
        }

        initialized = true;
        HOReborn.LOGGER.info("FoodRegistry initialized with {} food items", foodValues.size());
    }

    /**
     * Get food values for an item, applying JSON overrides.
     */
    public static FoodValues getFoodValues(ItemStack stack) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id == null) return FoodValues.getUnmodified(stack);

        String itemId = id.toString();
        FoodValueData data = foodValues.get(itemId);

        // Get unmodified values first
        FoodValues unmodified = FoodValues.getUnmodified(stack);

        if (data == null) {
            // No data in registry, use unmodified and add to registry
            foodValues.put(itemId, new FoodValueData(
                unmodified.hunger,
                unmodified.saturationModifier,
                "AUTO_GENERATED"
            ));
            return unmodified;
        }

        // Apply JSON override
        return new FoodValues(data.hunger, data.saturation);
    }

    /**
     * Get food values for a player (currently same as general values).
     */
    public static FoodValues getFoodValuesForPlayer(ItemStack stack, net.minecraft.world.entity.player.Player player) {
        // For now, player-specific modifications are the same as general ones
        return getFoodValues(stack);
    }

    /**
     * Add or update food values in the registry.
     */
    public static void setFoodValues(String itemId, FoodValueData data) {
        foodValues.put(itemId, data);
    }

    /**
     * Get all registered food values (for JSON serialization).
     */
    public static Map<String, FoodValueData> getAllFoodValues() {
        return new HashMap<>(foodValues);
    }

    /**
     * Check if an item is registered.
     */
    public static boolean hasFoodValues(String itemId) {
        return foodValues.containsKey(itemId);
    }

    /**
     * Clear the registry (for reloading).
     */
    public static void clear() {
        foodValues.clear();
        initialized = false;
    }

    /**
     * Get the number of registered food items.
     */
    public static int size() {
        return foodValues.size();
    }
}
