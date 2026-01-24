package org.Netroaki.Main;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.effects.WellFedEffect;
import org.Netroaki.Main.effects.HungryEffect;
import org.Netroaki.Main.effects.LowHealthEffect;
import org.Netroaki.Main.handlers.FoodEventHandler;
import org.Netroaki.Main.handlers.PlayerEventHandler;
import org.Netroaki.Main.handlers.WorldEventHandler;
import org.Netroaki.Main.modules.*;
import org.Netroaki.Main.compat.CompatibilityLayer;
import org.Netroaki.Main.util.BiomeGrowthRegistry;
import org.Netroaki.Main.util.BlockGrowthRegistry;
import org.Netroaki.Main.util.LootModifierManager;
import org.Netroaki.Main.util.RecipeManager;
import org.Netroaki.Main.util.VersionDetector;
import org.Netroaki.Main.world.VillageFieldHandler;
// import org.Netroaki.Main.util.DebugCommands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class HOReborn {
    public static final String MOD_ID = "hunger_overhaul_reborn";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    // Registries
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(MOD_ID, Registries.MOB_EFFECT);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);

    // Effects - Version-aware registration
    public static WellFedEffect WELL_FED_EFFECT_INSTANCE;
    public static HungryEffect HUNGRY_EFFECT_INSTANCE;
    public static LowHealthEffect LOW_HEALTH_EFFECT_INSTANCE;

    public static RegistrySupplier<MobEffect> WELL_FED_EFFECT;
    public static RegistrySupplier<MobEffect> HUNGRY_EFFECT;
    public static RegistrySupplier<MobEffect> LOW_HEALTH_EFFECT;

    static {
        // Create effect instances - they handle version detection internally
        WELL_FED_EFFECT_INSTANCE = new WellFedEffect();
        HUNGRY_EFFECT_INSTANCE = new HungryEffect();
        LOW_HEALTH_EFFECT_INSTANCE = new LowHealthEffect();

        // Register effects on both 1.20.1 and 1.21.1
        try {
            if (WELL_FED_EFFECT_INSTANCE.isAvailable()) {
                WELL_FED_EFFECT = EFFECTS.register("well_fed", () -> WELL_FED_EFFECT_INSTANCE);
            }
            if (HUNGRY_EFFECT_INSTANCE.isAvailable()) {
                HUNGRY_EFFECT = EFFECTS.register("hungry", () -> HUNGRY_EFFECT_INSTANCE);
            }
            if (LOW_HEALTH_EFFECT_INSTANCE.isAvailable()) {
                LOW_HEALTH_EFFECT = EFFECTS.register("low_health", () -> LOW_HEALTH_EFFECT_INSTANCE);
            }
        } catch (Exception e) {
            // Fallback - don't register effects if registration fails
            LOGGER.warn("Could not register effects", e);
            WELL_FED_EFFECT = null;
            HUNGRY_EFFECT = null;
            LOW_HEALTH_EFFECT = null;
        }
    }

    // Modules
    public static final FoodModule FOOD_MODULE = new FoodModule();
    public static final CropModule CROP_MODULE = new CropModule();
    public static final AnimalModule ANIMAL_MODULE = new AnimalModule();
    public static final ToolModule TOOL_MODULE = new ToolModule();
    public static final HungerModule HUNGER_MODULE = new HungerModule();
    public static final HealthModule HEALTH_MODULE = new HealthModule();
    public static final SereneSeasonsModule SERENE_SEASONS_MODULE = new SereneSeasonsModule();
    public static final HarvestCraftModule HARVESTCRAFT_MODULE = new HarvestCraftModule();
    public static final ModIntegrationHandler MOD_INTEGRATION_HANDLER = new ModIntegrationHandler();

    public static void init() {
        LOGGER.info("Initializing Hunger Overhaul Reborn");

        // Initialize compatibility layer for multi-version support
        CompatibilityLayer.initialize();
        LOGGER.info("Running on Minecraft {}", VersionDetector.getVersionString());

        // Register registries
        EFFECTS.register();
        ITEMS.register();

        // Initialize config
        HungerOverhaulConfig.init();

        // Initialize growth registries
        BlockGrowthRegistry.initialize();
        BiomeGrowthRegistry.initialize();

        // Initialize loot modifier manager
        LootModifierManager.initialize();

        // Initialize recipe manager
        RecipeManager.initialize();

        // Initialize village field handler
        VillageFieldHandler.initialize();

        // Register event handlers
        FoodEventHandler.register();
        PlayerEventHandler.register();
        WorldEventHandler.register();

        // Initialize modules
        FOOD_MODULE.init();
        CROP_MODULE.init();
        ANIMAL_MODULE.init();
        TOOL_MODULE.init();
        HUNGER_MODULE.init();
        HEALTH_MODULE.init();
        // Register debug commands
        // DebugCommands.register();
        SERENE_SEASONS_MODULE.init();
        HARVESTCRAFT_MODULE.init();
        MOD_INTEGRATION_HANDLER.init();

        // Initialize optional mod integrations (commented out until dependencies are
        // available)
        /*
         * if (Platform.isModLoaded("harvestcraft")) {
         * HARVESTCRAFT_MODULE.init();
         * LOGGER.info("HarvestCraft integration enabled");
         * }
         */

        LOGGER.info("Hunger Overhaul Reborn initialized successfully");
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
