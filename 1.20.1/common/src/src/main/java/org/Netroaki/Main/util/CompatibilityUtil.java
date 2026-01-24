package org.Netroaki.Main.util;

import net.fabricmc.loader.api.FabricLoader;
import org.Netroaki.Main.HOReborn;

import java.util.Arrays;
import java.util.List;

public class CompatibilityUtil {

    // Known mods that modify crop growth
    private static final List<String> GROWTH_MOD_IDS = Arrays.asList(
            "sereneseasons", // Serene Seasons
            "croptopia", // Croptopia
            "agricraft", // AgriCraft
            "mysticalagriculture", // Mystical Agriculture
            "botania", // Botania (has some crop features)
            "immersiveengineering", // Immersive Engineering (has some crop features)
            "thermal_cultivation", // Thermal Cultivation
            "create", // Create (has some crop features)
            "farmersdelight", // Farmer's Delight
            "harvestcraft" // HarvestCraft (has some crop features)
    );

    private static boolean growthModsDetected = false;
    private static String detectedMods = "";

    public static void detectGrowthMods() {
        if (!growthModsDetected) {
            StringBuilder detected = new StringBuilder();

            // Check Fabric mods
            if (FabricLoader.getInstance().isModLoaded("fabric")) {
                for (String modId : GROWTH_MOD_IDS) {
                    if (FabricLoader.getInstance().isModLoaded(modId)) {
                        detected.append(modId).append(" ");
                        HOReborn.LOGGER.info("Detected growth mod: " + modId);
                    }
                }
            }

            // Check Forge mods (using reflection to avoid compile-time dependency)
            try {
                Class<?> modListClass = Class.forName("net.minecraftforge.fml.ModList");
                Object modList = modListClass.getMethod("get").invoke(null);
                if (modList != null) {
                    for (String modId : GROWTH_MOD_IDS) {
                        boolean isLoaded = (Boolean) modListClass.getMethod("isLoaded", String.class).invoke(modList, modId);
                        if (isLoaded) {
                            detected.append(modId).append(" ");
                            HOReborn.LOGGER.info("Detected growth mod: " + modId);
                        }
                    }
                }
            } catch (Exception e) {
                // Not running on Forge, ignore
            }

            detectedMods = detected.toString().trim();
            growthModsDetected = true;

            if (!detectedMods.isEmpty()) {
                HOReborn.LOGGER.warn("Detected crop growth mods: " + detectedMods);
                HOReborn.LOGGER.warn(
                        "Consider disabling crop growth modifications in Hunger Overhaul Reborn config to avoid conflicts");
            }
        }
    }

    public static boolean hasGrowthMods() {
        return !detectedMods.isEmpty();
    }

    public static String getDetectedMods() {
        return detectedMods;
    }

    public static boolean shouldDisableCropGrowthModifications(boolean autoDetect, boolean manualDisable) {
        if (manualDisable) {
            return true;
        }

        if (autoDetect) {
            detectGrowthMods();
            return hasGrowthMods();
        }

        return false;
    }
}
