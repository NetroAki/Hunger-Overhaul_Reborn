package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;
import org.Netroaki.Main.util.FoodCategorizer;

/**
 * Assembly Required Mod Integration
 * 
 * Assembly Required is a building/construction mod with a food system.
 * Food items should exist in the data_dump at:
 * /data_dump/dump/item/assemblytreasure.txt
 * 
 * Auto-discovers and categorizes all food items from the mod.
 */
public class AssemblyRequiredModule {

    private static final String MOD_ID = "assemblytreasure";
    private static final String MOD_NAME = "Assembly Required";
    private static boolean modLoaded = false;
    private int itemsProcessed = 0;

    public void init() {
        modLoaded = Platform.isModLoaded(MOD_ID);

        if (!modLoaded) {
            HOReborn.LOGGER.debug("Assembly Required not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing {} food integration", MOD_NAME);

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
            
            if (itemsProcessed > 0) {
                HOReborn.LOGGER.info("Applied food values to {} items from {}", itemsProcessed, MOD_NAME);
            }
        }
    }

    /**
     * Apply food value modifications to all food items from Assembly Required.
     * Uses automatic categorization based on item names.
     * Note: Uses deprecated BuiltInRegistries.ITEM for registry iteration, which is required.
     */
    @SuppressWarnings("deprecation")
    private void applyFoodValueModifications() {
        itemsProcessed = 0;

        // Since Assembly Required may have many food items, use registry scanning
        net.minecraft.core.registries.BuiltInRegistries.ITEM.forEach(item -> {
            String itemId = item.toString();
            
            // Check if this item belongs to Assembly Required
            if (!itemId.startsWith(MOD_ID + ":")) {
                return;
            }

            // Extract just the item name without namespace
            String itemName = itemId.substring((MOD_ID + ":").length());

            // Skip non-food items
            if (shouldSkipItem(itemName)) {
                return;
            }

            // Auto-categorize the food
            FoodCategorizer.FoodValue foodValue = FoodCategorizer.categorizeFood(itemName);

            // Apply the food value
            String category = MOD_NAME + "_" + foodValue.mealType;
            
            FoodRegistry.setFoodValues(itemId, new org.Netroaki.Main.food.FoodValueData(
                foodValue.hunger,
                foodValue.saturation,
                category
            ));

            itemsProcessed++;
        });
    }

    /**
     * Determine if an item should be skipped from food processing.
     */
    private boolean shouldSkipItem(String itemName) {
        String lower = itemName.toLowerCase();

        // Skip non-edible items
        if (containsAny(lower,
                "block", "ore", "ingot", "dust", "gear", "cable", "wire", "circuit",
                "machine", "tool", "sword", "pickaxe", "shovel", "hoe", "axe",
                "helmet", "chestplate", "leggings", "boots", "nugget", "icon")) {
            return true;
        }

        // Skip non-food items
        if (itemName.equalsIgnoreCase("spawn_egg") || 
            containsAny(lower, "spawn", "debug", "test")) {
            return true;
        }

        return false;
    }

    /**
     * Check if text contains any of the given tokens (case-insensitive)
     */
    private boolean containsAny(String text, String... tokens) {
        for (String token : tokens) {
            if (token != null && !token.isEmpty() && text.contains(token)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the mod is loaded
     */
    public static boolean isModLoaded() {
        return modLoaded;
    }

    /**
     * Get the mod ID
     */
    public static String getModId() {
        return MOD_ID;
    }

    /**
     * Get items processed count
     */
    public int getItemsProcessed() {
        return itemsProcessed;
    }
}

