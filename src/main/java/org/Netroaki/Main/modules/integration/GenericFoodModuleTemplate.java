package org.Netroaki.Main.modules.integration;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;
import org.Netroaki.Main.util.FoodCategorizer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

/**
 * Template for generic food mod integration using automatic categorization.
 * 
 * Copy this class and modify:
 * 1. MOD_ID - the mod identifier
 * 2. MOD_NAME - friendly name for logging
 * 3. Constructor - to match your module name
 * 4. applyFoodValueModifications() - add any custom overrides if needed
 * 
 * The module will automatically categorize all food items from the mod based on their names.
 */
public class GenericFoodModuleTemplate {
    
    private static final String MOD_ID = "example_mod"; // Change this
    private static final String MOD_NAME = "Example Mod"; // Change this
    private static boolean modLoaded = false;
    private int itemsProcessed = 0;

    public void init() {
        modLoaded = Platform.isModLoaded(MOD_ID);

        if (!modLoaded) {
            HOReborn.LOGGER.debug("{} not detected, skipping integration", MOD_NAME);
            return;
        }

        HOReborn.LOGGER.info("Initializing {} food integration", MOD_NAME);

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
            HOReborn.LOGGER.info("Applied food values to {} items from {}", itemsProcessed, MOD_NAME);
        }
    }

    /**
     * Apply food value modifications to all food items from this mod.
     * Uses automatic categorization based on item names.
     */
    @SuppressWarnings({"deprecation"})
    private void applyFoodValueModifications() {
        itemsProcessed = 0;

        for (Item item : BuiltInRegistries.ITEM) {
            String itemId = item.toString();
            
            // Check if this item belongs to our mod
            if (!itemId.startsWith(MOD_ID + ":")) {
                continue;
            }

            // Extract just the item name without namespace
            String itemName = itemId.substring((MOD_ID + ":").length());

            // Skip non-food items and items we don't want to modify
            if (shouldSkipItem(itemName)) {
                continue;
            }

            // Auto-categorize the food
            FoodCategorizer.FoodValue foodValue = FoodCategorizer.categorizeFood(itemName);

            // Apply the food value
            String fullId = MOD_ID + ":" + itemName;
            String category = MOD_NAME + "_" + foodValue.mealType;
            
            FoodRegistry.setFoodValues(fullId, new org.Netroaki.Main.food.FoodValueData(
                foodValue.hunger,
                foodValue.saturation,
                category
            ));

            itemsProcessed++;
        }
    }

    /**
     * Determine if an item should be skipped from food processing.
     * Override this in subclasses to add custom skip logic.
     */
    protected boolean shouldSkipItem(String itemName) {
        // Skip non-edible items
        if (containsAny(itemName.toLowerCase(),
                "block", "ore", "ingot", "dust", "gear", "cable", "wire", "circuit",
                "machine", "tool", "sword", "pickaxe", "shovel", "hoe", "axe",
                "helmet", "chestplate", "leggings", "boots", "nugget", "icon")) {
            return true;
        }

        // Skip items that definitely aren't food
        if (itemName.equalsIgnoreCase("spawn_egg") || 
            containsAny(itemName.toLowerCase(), "spawn", "egg")) {
            return true;
        }

        return false;
    }

    /**
     * Check if text contains any of the given tokens (case-insensitive).
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
     * Check if the mod is loaded.
     */
    public static boolean isModLoaded() {
        return modLoaded;
    }

    /**
     * Get the mod ID.
     */
    public static String getModId() {
        return MOD_ID;
    }
}

