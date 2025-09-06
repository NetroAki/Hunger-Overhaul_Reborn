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
import org.Netroaki.Main.handlers.FoodEventHandler;
import org.Netroaki.Main.handlers.PlayerEventHandler;
import org.Netroaki.Main.handlers.WorldEventHandler;
import org.Netroaki.Main.modules.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class HOReborn {
    public static final String MOD_ID = "hunger_overhaul_reborn";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    // Registries
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(MOD_ID, Registries.MOB_EFFECT);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);

    // Effects
    public static final RegistrySupplier<MobEffect> WELL_FED_EFFECT = EFFECTS.register("well_fed", WellFedEffect::new);
    public static final RegistrySupplier<MobEffect> HUNGRY_EFFECT = EFFECTS.register("hungry", HungryEffect::new);

    // Modules
    public static final FoodModule FOOD_MODULE = new FoodModule();
    public static final CropModule CROP_MODULE = new CropModule();
    public static final AnimalModule ANIMAL_MODULE = new AnimalModule();
    public static final ToolModule TOOL_MODULE = new ToolModule();
    public static final HungerModule HUNGER_MODULE = new HungerModule();
    public static final HealthModule HEALTH_MODULE = new HealthModule();
    // public static final HarvestCraftModule HARVESTCRAFT_MODULE = new
    // HarvestCraftModule();

    public static void init() {
        LOGGER.info("Initializing Hunger Overhaul Reborn");

        // Register registries
        EFFECTS.register();
        ITEMS.register();

        // Initialize config
        HungerOverhaulConfig.init();

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
