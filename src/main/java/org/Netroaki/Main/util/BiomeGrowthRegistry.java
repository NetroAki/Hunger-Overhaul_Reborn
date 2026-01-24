package org.Netroaki.Main.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for biome-based crop growth modifiers.
 * Maps biomes and biome tags to growth multipliers.
 */
public class BiomeGrowthRegistry {
    private static final Map<ResourceKey<Biome>, Double> biomeMultipliers = new HashMap<>();
    private static final Map<TagKey<Biome>, Double> biomeTagMultipliers = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Initialize the registry with default biome preferences.
     */
    public static void initialize() {
        if (initialized) return;

        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // For now, we'll use a simplified approach without biome tags
        // In a full implementation, this would register specific biomes or biome categories

        // Desert biomes - preferred for cactus (would be registered by name)
        // Plains biomes - preferred for most crops (would be registered by name)
        // etc.

        initialized = true;
        HOReborn.LOGGER.info("BiomeGrowthRegistry initialized (simplified)");
    }

    /**
     * Register a specific biome with a growth multiplier.
     */
    public static void registerBiomeMultiplier(ResourceKey<Biome> biome, double multiplier) {
        biomeMultipliers.put(biome, multiplier);
    }

    /**
     * Register a biome tag with a growth multiplier.
     */
    public static void registerBiomeTagMultiplier(TagKey<Biome> biomeTag, double multiplier) {
        biomeTagMultipliers.put(biomeTag, multiplier);
    }

    /**
     * Get the growth multiplier for a biome.
     * Checks specific biomes first, then returns wrong biome penalty.
     */
    public static double getBiomeMultiplier(Biome biome, ResourceKey<Biome> biomeKey) {
        if (biome == null) return HungerOverhaulConfig.getInstance().crops.wrongBiomeRegrowthMultiplier;

        // Check specific biome mappings first
        Double specificMultiplier = biomeMultipliers.get(biomeKey);
        if (specificMultiplier != null) {
            return specificMultiplier;
        }

        // For now, all biomes get the wrong biome penalty
        // In a full implementation, we'd check biome categories
        return HungerOverhaulConfig.getInstance().crops.wrongBiomeRegrowthMultiplier;
    }

    /**
     * Check if a biome has a custom multiplier registered.
     */
    public static boolean hasCustomMultiplier(Biome biome, ResourceKey<Biome> biomeKey) {
        if (biome == null) return false;

        if (biomeMultipliers.containsKey(biomeKey)) return true;

        // For now, simplified - biome tag checking would need proper implementation
        // for the target Minecraft version

        return false;
    }

    /**
     * Get the sugarcane-specific biome multiplier.
     */
    public static double getSugarcaneBiomeMultiplier(Biome biome, ResourceKey<Biome> biomeKey) {
        // Sugarcane has different biome preferences than regular crops
        // For now, simplified - would check biome type in full implementation

        // Wrong biome penalty for sugarcane (unless it's a preferred biome)
        return HungerOverhaulConfig.getInstance().crops.wrongBiomeRegrowthMultiplierSugarcane;
    }

    /**
     * Get all registered biome multipliers (for debugging).
     */
    public static Map<ResourceKey<Biome>, Double> getAllBiomeMultipliers() {
        return new HashMap<>(biomeMultipliers);
    }

    /**
     * Get all registered biome tag multipliers (for debugging).
     */
    public static Map<TagKey<Biome>, Double> getAllBiomeTagMultipliers() {
        return new HashMap<>(biomeTagMultipliers);
    }

    /**
     * Clear the registry (for reloading).
     */
    public static void clear() {
        biomeMultipliers.clear();
        biomeTagMultipliers.clear();
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
