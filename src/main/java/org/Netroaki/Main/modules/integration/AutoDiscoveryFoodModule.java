package org.Netroaki.Main.modules.integration;

import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;
import org.Netroaki.Main.util.FoodCategorizer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;

/**
 * Automatic Food Discovery Module
 * 
 * Scans ALL mods in the registry and auto-categorizes any food items found,
 * even for mods without explicit integration modules.
 * 
 * This is the fallback system that catches any food items missed by explicit modules.
 * Runs AFTER all explicit modules to avoid conflicts.
 */
public class AutoDiscoveryFoodModule {

    private int itemsProcessed = 0;
    private Set<String> processedNamespaces = new HashSet<>();

    /**
     * Initialize auto-discovery - should be called AFTER all explicit modules
     */
    public void init(Set<String> explicitModIds) {
        if (!HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            return;
        }

        HOReborn.LOGGER.info("Starting auto-discovery for unsupported food mods...");
        itemsProcessed = 0;
        processedNamespaces = new HashSet<>(explicitModIds);

        scanAndCategorizeAllItems();

        if (itemsProcessed > 0) {
            HOReborn.LOGGER.info("Auto-discovery: Categorized {} items from {} unsupported mods",
                itemsProcessed, getDiscoveredModCount());
        } else {
            HOReborn.LOGGER.debug("Auto-discovery: No additional food items found");
        }
    }

    /**
     * Scan all items in registry and categorize those from mods without explicit modules
     * Note: Uses deprecated BuiltInRegistries.ITEM for registry iteration, which is required.
     */
    @SuppressWarnings("deprecation")
    private void scanAndCategorizeAllItems() {
        for (Item item : BuiltInRegistries.ITEM) {
            String itemId = item.toString();

            // Extract namespace (mod ID)
            int colonIndex = itemId.indexOf(':');
            if (colonIndex == -1) {
                continue; // Invalid item ID format
            }

            String namespace = itemId.substring(0, colonIndex);
            String itemName = itemId.substring(colonIndex + 1);

            // Skip if already handled by explicit module
            if (processedNamespaces.contains(namespace)) {
                continue;
            }

            // Skip non-food items
            if (shouldSkipItem(itemName, namespace)) {
                continue;
            }

            // Auto-categorize
            try {
                FoodCategorizer.FoodValue foodValue = FoodCategorizer.categorizeFood(itemName);
                
                String category = "AUTO_" + foodValue.mealType;
                FoodRegistry.setFoodValues(itemId, new org.Netroaki.Main.food.FoodValueData(
                    foodValue.hunger,
                    foodValue.saturation,
                    category
                ));

                itemsProcessed++;

                // Track this namespace as processed
                processedNamespaces.add(namespace);
            } catch (Exception e) {
                HOReborn.LOGGER.warn("Failed to categorize item {}: {}", itemId, e.getMessage());
            }
        }
    }

    /**
     * Determine if an item should be skipped from auto-discovery
     */
    private boolean shouldSkipItem(String itemName, String namespace) {
        String lower = itemName.toLowerCase();

        // Skip Minecraft vanilla items (handled separately)
        if ("minecraft".equals(namespace)) {
            return true;
        }

        // Skip tech/machinery items
        if (containsAny(lower,
                "block", "ore", "ingot", "nugget", "dust", "gear", "rod", "plate",
                "cable", "wire", "circuit", "machine", "motor", "pipe", "tank",
                "tool", "sword", "pickaxe", "shovel", "hoe", "axe", "wrench",
                "helmet", "chestplate", "leggings", "boots", "armor",
                "crafting", "storage", "container", "chest", "barrel")) {
            return true;
        }

        // Skip spawn eggs and invalid items
        if (containsAny(lower, "spawn_egg", "egg", "debug", "test", "placeholder")) {
            return true;
        }

        // Skip items that are clearly not food
        if (containsAny(lower, "alchemy", "scroll", "book", "quill", "lens",
                "crystal", "gem", "stone", "brick", "wood", "planks", "log")) {
            return true;
        }

        // If it doesn't have any food-related keywords, skip it
        if (!hasFoodKeywords(lower)) {
            return true;
        }

        return false;
    }

    /**
     * Check if item name contains any food-related keywords
     */
    private boolean hasFoodKeywords(String lower) {
        return containsAny(lower,
                // Basic food types
                "food", "meal", "dish", "dish", "recipe",
                // Meats
                "meat", "beef", "pork", "chicken", "fish", "salmon", "cod", "tuna", "shrimp", "crab",
                "steak", "chop", "rib", "wing", "leg", "drumstick", "bacon", "ham", "sausage",
                // Produce
                "vegetable", "fruit", "apple", "berry", "grape", "melon", "pumpkin", "carrot",
                "potato", "wheat", "corn", "rice", "bean", "tomato", "onion", "lettuce",
                // Prepared foods
                "soup", "stew", "curry", "burger", "sandwich", "pizza", "pasta", "noodle",
                "bread", "cake", "pie", "cookie", "candy", "chocolate", "pudding", "custard",
                "salad", "sauce", "gravy", "seasoning", "spice", "herb", "tea", "coffee",
                "juice", "drink", "wine", "beer", "milk", "cheese", "butter", "cream",
                // Descriptors
                "cooked", "baked", "roasted", "grilled", "fried", "raw", "fresh",
                "sweet", "savory", "spicy", "salty", "sour", "bitter", "umami");
    }

    /**
     * Get count of discovered mods (mods auto-discovered but not explicit)
     */
    private int getDiscoveredModCount() {
        Set<String> explicitMods = new HashSet<>();
        // These should match all explicit modules
        explicitMods.addAll(java.util.Arrays.asList(
                "minecraft", "farmersdelight", "alexsmobs", "aquaculture", "betterend",
                "betternether", "aether", "divinerpg", "createfood", "create",
                "twilightforest", "iceandfire", "cataclysm", "alexscaves", "delightful",
                "thermalcultivation", "creategourmet", "biomesoplenty", "natura",
                "tinkersconstruct", "reliquary", "mowziesmobs", "productivebees",
                "productivetrees", "biomesweevegone", "quark", "regionsunexplored",
                "deeperdarker", "minecolonies", "borninchaosvl", "theabyss"
        ));

        return processedNamespaces.size() - explicitMods.size();
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
     * Get the total items processed
     */
    public int getItemsProcessed() {
        return itemsProcessed;
    }

    /**
     * Get the namespaces processed
     */
    public Set<String> getProcessedNamespaces() {
        return new HashSet<>(processedNamespaces);
    }
}

