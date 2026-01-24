package org.Netroaki.Main.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for per-block growth multipliers.
 * Maps specific blocks or block classes to their growth multipliers.
 */
public class BlockGrowthRegistry {
    private static final Map<Class<? extends Block>, Double> blockClassMultipliers = new HashMap<>();
    private static final Map<Block, Double> blockMultipliers = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Initialize the registry with default per-block multipliers.
     */
    public static void initialize() {
        if (initialized) return;

        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // Register specific blocks
        registerBlockMultiplier(Blocks.CACTUS, config.crops.cactusRegrowthMultiplier);
        registerBlockMultiplier(Blocks.SUGAR_CANE, config.crops.sugarcaneRegrowthMultiplier);
        registerBlockMultiplier(Blocks.NETHER_WART, config.crops.netherWartRegrowthMultiplier);

        // Register block classes (for mod compatibility)
        // Cocoa beans are handled specially in crop growth logic
        // Saplings are handled by Serene Seasons if present

        initialized = true;
        HOReborn.LOGGER.info("BlockGrowthRegistry initialized with {} block multipliers",
            blockMultipliers.size() + blockClassMultipliers.size());
    }

    /**
     * Register a specific block with a growth multiplier.
     */
    public static void registerBlockMultiplier(Block block, double multiplier) {
        blockMultipliers.put(block, multiplier);
    }

    /**
     * Register a block class with a growth multiplier.
     */
    public static void registerBlockClassMultiplier(Class<? extends Block> blockClass, double multiplier) {
        blockClassMultipliers.put(blockClass, multiplier);
    }

    /**
     * Get the growth multiplier for a block.
     * Checks specific blocks first, then block classes, then returns default.
     */
    public static double getGrowthMultiplier(Block block) {
        if (block == null) return 1.0;

        // Check specific block mappings first
        Double specificMultiplier = blockMultipliers.get(block);
        if (specificMultiplier != null) {
            return specificMultiplier;
        }

        // Check block class mappings
        Double classMultiplier = blockClassMultipliers.get(block.getClass());
        if (classMultiplier != null) {
            return classMultiplier;
        }

        // Check instanceof for registered classes
        for (Map.Entry<Class<? extends Block>, Double> entry : blockClassMultipliers.entrySet()) {
            if (entry.getKey().isAssignableFrom(block.getClass())) {
                return entry.getValue();
            }
        }

        // Return default crop multiplier
        return HungerOverhaulConfig.getInstance().crops.cropGrowthMultiplier;
    }

    /**
     * Check if a block has a custom multiplier registered.
     */
    public static boolean hasCustomMultiplier(Block block) {
        if (block == null) return false;

        if (blockMultipliers.containsKey(block)) return true;

        for (Class<? extends Block> blockClass : blockClassMultipliers.keySet()) {
            if (blockClass.isAssignableFrom(block.getClass())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get all registered block multipliers (for debugging).
     */
    public static Map<Block, Double> getAllBlockMultipliers() {
        return new HashMap<>(blockMultipliers);
    }

    /**
     * Get all registered block class multipliers (for debugging).
     */
    public static Map<Class<? extends Block>, Double> getAllBlockClassMultipliers() {
        return new HashMap<>(blockClassMultipliers);
    }

    /**
     * Clear the registry (for reloading).
     */
    public static void clear() {
        blockMultipliers.clear();
        blockClassMultipliers.clear();
        initialized = false;
    }

    /**
     * Reload the registry with current config values.
     */
    public static void reload() {
        clear();
        initialize();
    }
}
