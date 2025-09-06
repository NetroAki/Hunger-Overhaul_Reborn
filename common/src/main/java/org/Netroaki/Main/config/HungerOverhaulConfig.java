package org.Netroaki.Main.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.Netroaki.Main.HOReborn;
import dev.architectury.platform.Platform;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

// JSON-backed configuration class
public class HungerOverhaulConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "hunger_overhaul_reborn.json";
    private static HungerOverhaulConfig INSTANCE;

    // Default configuration values
    public final FoodSettings food = new FoodSettings();
    public final CropSettings crops = new CropSettings();
    public final AnimalSettings animals = new AnimalSettings();
    public final ToolSettings tools = new ToolSettings();
    public final HungerSettings hunger = new HungerSettings();
    public final HealthSettings health = new HealthSettings();
    public final IntegrationSettings integration = new IntegrationSettings();

    public static class FoodSettings {
        public boolean modifyFoodStats = true;
        public boolean modifyFoodValues = true;
        public boolean showFoodTooltips = true;
        public boolean enableWellFedEffect = true;
        public boolean modifyStackSizes = true;
        public boolean modifyEatingSpeed = true;
        public double modFoodValueDivider = 4.0;
        public boolean enableLowHungerWarnings = true;
    }

    public static class CropSettings {
        public boolean cropsOnlyGrowInDaylight = true;
        public boolean cropsTakeLongerToGrow = true;
        public double cropGrowthMultiplier = 0.5;
        public boolean cropsOnlyGiveSeeds = true;
        public boolean difficultyScalingBoneMeal = true;
        public boolean removeTallGrassSeeds = true;
    }

    public static class AnimalSettings {
        public double eggTimeoutMultiplier = 4.0;
        public double breedingTimeoutMultiplier = 4.0;
        public double childDurationMultiplier = 4.0;
    }

    public static class ToolSettings {
        public boolean modifyHoeUse = true;
        public boolean removeHoeRecipes = true;
        public double hoeToolDamageMultiplier = 5.0;
        public double seedChanceMultiplier = 1.0;
        public double seedChanceBase = 0.4;
    }

    public static class HungerSettings {
        public boolean constantHungerLoss = true;
        public double constantHungerLossRate = 0.001;
        public boolean modifyRespawnHunger = true;
        public int respawnHungerValue = 20;
        public int respawnHungerDifficultyModifier = 4;
        public boolean difficultyScalingRespawnHunger = true;
        public boolean difficultyScalingHungerLoss = true;
        public boolean harvestCraftHungerLossIncrease = true;
        public double harvestCraftHungerLossMultiplier = 1.33;
        public boolean instantDeathOnZeroHunger = true;
        public boolean lowHungerEffects = true;
        public int lowHungerSlownessThreshold = 6;
        public int lowHungerWeaknessThreshold = 4;
    }

    public static class HealthSettings {
        public boolean healthHealsAboveThreeShanks = true;
        public int healthHealingThreshold = 6;
        public boolean lowHealthEffects = true;
        public int lowHealthEffectsThreshold = 6;
        public boolean difficultyScalingHealing = true;
    }

    public static class IntegrationSettings {
        public boolean enableHarvestCraftIntegration = true;
        public boolean enableVillageIntegration = true;
        public boolean enableChestLootIntegration = true;
    }

    public static synchronized HungerOverhaulConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = loadOrCreate();
        }
        return INSTANCE;
    }

    public static void init() {
        getInstance();
        HOReborn.LOGGER.info("Configuration initialized with JSON backing");
    }

    private static HungerOverhaulConfig loadOrCreate() {
        try {
            Path configDir = Platform.getConfigFolder();
            Files.createDirectories(configDir);
            Path cfg = configDir.resolve(FILE_NAME);
            if (Files.exists(cfg)) {
                try (Reader r = Files.newBufferedReader(cfg)) {
                    HungerOverhaulConfig loaded = GSON.fromJson(r, HungerOverhaulConfig.class);
                    if (loaded != null)
                        return loaded;
                }
            }
            HungerOverhaulConfig def = new HungerOverhaulConfig();
            save(def);
            return def;
        } catch (Exception e) {
            HOReborn.LOGGER.error("Failed to load config, using defaults", e);
            return new HungerOverhaulConfig();
        }
    }

    public static void save(HungerOverhaulConfig cfg) {
        try {
            Path configDir = Platform.getConfigFolder();
            Files.createDirectories(configDir);
            Path cfgPath = configDir.resolve(FILE_NAME);
            try (Writer w = Files.newBufferedWriter(cfgPath)) {
                GSON.toJson(cfg, w);
            }
        } catch (IOException e) {
            HOReborn.LOGGER.error("Failed to save config", e);
        }
    }
}
