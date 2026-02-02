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

    // Module References (Parity)
    public static FoodModule FOOD_MODULE;
    public static HungerModule HUNGER_MODULE;
    public static HealthModule HEALTH_MODULE;
    public static CropModule CROP_MODULE;
    public static AnimalModule ANIMAL_MODULE;
    public static ToolModule TOOL_MODULE;

    static {
        // Create effect instances
        WELL_FED_EFFECT_INSTANCE = new WellFedEffect();
        HUNGRY_EFFECT_INSTANCE = new HungryEffect();
        LOW_HEALTH_EFFECT_INSTANCE = new LowHealthEffect();

        // Register effects
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
            LOGGER.warn("Could not register effects", e);
            WELL_FED_EFFECT = null;
            HUNGRY_EFFECT = null;
            LOW_HEALTH_EFFECT = null;
        }
    }

    public static void init() {
        LOGGER.info("Initializing Hunger Overhaul Reborn for Minecraft 1.21.1");

        // Version detection
        VersionDetector.init();

        // Register registries
        EFFECTS.register();
        ITEMS.register();

        // Initialize configuration
        HungerOverhaulConfig.init();

        // Initialize registries with version-aware logic
        BiomeGrowthRegistry.init();
        BlockGrowthRegistry.init();
        LootModifierManager.init();
        RecipeManager.init();
        VillageFieldHandler.init();

        // Initialize modules
        // Initialize modules
        // Store references for platform access (PARITY with 1.20.1)
        FOOD_MODULE = new FoodModule();
        FOOD_MODULE.init();
        
        HUNGER_MODULE = new HungerModule();
        HUNGER_MODULE.init();

        HEALTH_MODULE = new HealthModule();
        HEALTH_MODULE.init();

        CROP_MODULE = new CropModule();
        CROP_MODULE.init();

        ANIMAL_MODULE = new AnimalModule();
        ANIMAL_MODULE.init();

        TOOL_MODULE = new ToolModule();
        TOOL_MODULE.init();

        // Initialize mod integrations
        new ModIntegrationHandler().init();

        // Register event handlers
        FoodEventHandler.register();
        PlayerEventHandler.register();
        WorldEventHandler.register();

        // Initialize compatibility layer
        CompatibilityLayer.init();

        LOGGER.info("Hunger Overhaul Reborn initialization complete for 1.21.1");
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}