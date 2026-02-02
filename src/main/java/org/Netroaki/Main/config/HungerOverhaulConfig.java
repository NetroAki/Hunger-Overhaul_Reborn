package org.Netroaki.Main.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

// JSON-backed configuration class
public class HungerOverhaulConfig {

    private static final Logger LOGGER = LogManager.getLogger("hunger_overhaul_reborn");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "hunger_overhaul_reborn.json";
    private static HungerOverhaulConfig INSTANCE;

    // Default configuration values
    public final FoodSettings food = new FoodSettings();
    public final CropSettings crops = new CropSettings();
    public final HarvestingSettings harvesting = new HarvestingSettings();
    public final AnimalSettings animals = new AnimalSettings();
    public final ToolSettings tools = new ToolSettings();
    public final HungerSettings hunger = new HungerSettings();
    public final HealthSettings health = new HealthSettings();
    public final GUISettings gui = new GUISettings();
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

        // Original Hunger Overhaul food calculation settings
        public double foodHungerDivider = 4.0; // Divide hunger by this
        public double foodSaturationDivider = 1.0; // Divide saturation by this
        public double foodHungerToSaturationDivider = 20.0; // Set saturation = hunger / this (if > 0)

        // Stack size limits based on food value
        public int stackSizeLargeMeal = 4; // Foods with hunger >= 8 (e.g. Rabbit Stew)
        public int stackSizeAverageMeal = 4; // Foods with hunger >= 6 (e.g. Mushroom Stew)
        public int stackSizeLightMeal = 16; // Foods with hunger >= 4
        public int stackSizeCookedMeal = 32; // Foods with hunger >= 2
        public int stackSizeRawMeal = 32; // Foods with hunger < 2
    }

    public static class CropSettings {
        public boolean cropsOnlyGrowInDaylight = false; // Original default: false
        public boolean cropsTakeLongerToGrow = true;
        public double cropGrowthMultiplier = 0.25; // Original default: 0.25 (4x slower)
        public boolean cropsOnlyGiveSeeds = true;
        public boolean difficultyScalingBoneMeal = true;
        public double bonemealSuccessRateEasy = 1.0;
        public double bonemealSuccessRateNormal = 0.5;
        public double bonemealSuccessRateHard = 0.25;
        public boolean removeTallGrassSeeds = true;
        public boolean disableCropGrowthModifications = false;
        public boolean autoDetectGrowthMods = true;

        // Serene Seasons compatibility settings
        public boolean enableSereneSeasonsCompatibility = true;
        public boolean respectSeasonalGrowth = true;
        public double winterGrowthMultiplier = 0.3;
        public double springGrowthMultiplier = 1.0;
        public double summerGrowthMultiplier = 1.2;
        public double autumnGrowthMultiplier = 0.6;

        // Per-crop growth multipliers (original Hunger Overhaul values)
        public double cactusRegrowthMultiplier = 0.25;
        public double cocoaRegrowthMultiplier = 0.25;
        public double sugarcaneRegrowthMultiplier = 0.25;
        public double treeCropRegrowthMultiplier = 0.25;
        public double saplingRegrowthMultiplier = 0.25;
        public double netherWartRegrowthMultiplier = 0.25;
        public double stemRegrowthMultiplier = 0.25;

        // Flour recipe modifications (for Tinkers' Construct - includes legacy Natura
        // functionality)
        public boolean removeFlourCraftingRecipes = true;
        public boolean removeFlourSmeltingRecipe = true;
        public boolean addAlternateFlourCraftingRecipes = false;

        // Sunlight and biome modifiers
        public double noSunlightRegrowthMultiplier = 2.0;
        public double wrongBiomeRegrowthMultiplier = 2.0;
        public double wrongBiomeRegrowthMultiplierSugarcane = 2.0;

        // Bone meal settings
        public boolean modifyBonemealGrowth = true;

        // Drying rack settings (for Tinkers' Construct)
        public double dryingRackTimeMultiplier = 4.0;
    }

    public static class HarvestingSettings {
        public boolean enableRightClickHarvesting = true;
        public boolean modifyCropDropsRightClick = true;
        public boolean modifyCropDropsBreak = true;

        // Right-click harvest drops
        public int seedsPerHarvestRightClickMin = 0;
        public int seedsPerHarvestRightClickMax = 0;
        public int producePerHarvestRightClickMin = 2;
        public int producePerHarvestRightClickMax = 4;

        // Break harvest drops
        public int seedsPerHarvestBreakMin = 0;
        public int seedsPerHarvestBreakMax = 0;
        public int producePerHarvestBreakMin = 2;
        public int producePerHarvestBreakMax = 4;
    }

    public static class AnimalSettings {
        public double eggTimeoutMultiplier = 4.0;
        public double breedingTimeoutMultiplier = 4.0;
        public double childDurationMultiplier = 4.0;
        public int milkedTimeout = 20; // Minutes between milkings
    }

    public static class ToolSettings {
        public boolean modifyHoeUse = true;
        public boolean removeHoeRecipes = true;
        public double hoeToolDamageMultiplier = 5.0;
        public double seedChanceMultiplier = 1.0;
        public double seedChanceBase = 0.4;

        // Seed system configurations
        public boolean allSeedsEqual = true; // Make all seeds drop with equal chance
        public boolean addSeedsCraftingRecipe = true; // Add 1 wheat -> 1 seed recipe
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
        public double hungerLossRatePercentage = 133.33; // 4/3 of vanilla speed
        public boolean instantDeathOnZeroHunger = false; // Use configurable damage instead
        public int damageOnStarve = 200; // 10 hearts (original default)
        public boolean lowHungerEffects = true;
        public int lowHungerSlownessThreshold = 6;
        public int lowHungerWeaknessThreshold = 4;
    }

    public static class HealthSettings {
        public boolean healthHealsAboveThreeShanks = true;
        public int healthHealingThreshold = 7; // Original default: 7
        public int minHungerToHeal = 7; // Original default: 7
        public boolean lowHealthEffects = true;
        public int lowHealthEffectsThreshold = 6;

        // Low health effect settings
        public boolean addLowHealthNausea = true;
        public boolean addLowHealthSlowness = true;
        public boolean addLowHealthWeakness = true;
        public boolean addLowHealthMiningSlowdown = true;
        public int lowHealthNauseaThreshold = 3; // Very low health threshold

        // Enhanced health features
        public boolean foodRegensHealth = false; // Alternative healing mechanic
        public double foodHealDivider = 4.0; // Health restored = food_value / divider
        public boolean disableHealingHungerDrain = true; // Vanilla 1.6.2 behavior
        public double healthRegenRatePercentage = 100.0; // Overall regen speed (100 = vanilla)
        public boolean modifyRegenRateOnLowHealth = true; // Low health = slower regen
        public double lowHealthRegenRateModifier = 5.0; // Delay increase factor

        public boolean difficultyScalingHealing = true;
    }

    public static class GUISettings {
        public boolean addGuiText = true;
        public int lowHungerTextThreshold = 6;
        public int lowHealthTextThreshold = 6;
        public boolean effect_warnings = false; // Combined toggle for hunger/health warnings and effect status
        public int guiTextX = 5;
        public int guiTextY = 5;
    }

    public static class IntegrationSettings {
        public boolean enableHarvestCraftIntegration = true;
        public boolean enableVillageIntegration = true;
        public boolean enableChestLootIntegration = true;

        // Village generation settings
        public boolean addCustomVillageField = true;
        public int fieldNormalWeight = 70;
        public int fieldReedWeight = 10;
        public int fieldStemWeight = 10;
    }

    public static synchronized HungerOverhaulConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = loadOrCreate();
        }
        return INSTANCE;
    }

    public static void init() {
        getInstance();
        LOGGER.info("Configuration initialized with JSON backing");
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
            LOGGER.error("Failed to load config, using defaults", e);
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
            LOGGER.error("Failed to save config", e);
        }
    }
}
