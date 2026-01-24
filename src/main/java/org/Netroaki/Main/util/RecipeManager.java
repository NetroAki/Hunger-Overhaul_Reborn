package org.Netroaki.Main.util;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
// Note: Uses fully qualified name to avoid naming conflict
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

/**
 * Manages dynamic recipe registration based on configuration.
 */
public class RecipeManager {

    private static boolean initialized = false;

    /**
     * Initialize recipe manager.
     * Registers dynamic recipes based on config.
     */
    public static void initialize() {
        if (initialized) return;

        LifecycleEvent.SERVER_STARTING.register(server -> {
            var recipeManager = server.getRecipeManager();

            // Register seeds crafting recipe if enabled
            if (HungerOverhaulConfig.getInstance().tools.addSeedsCraftingRecipe) {
                // The recipe is already defined in data/hunger_overhaul_reborn/recipes/wheat_to_seeds.json
                // and will be loaded automatically by Minecraft
                HOReborn.LOGGER.info("Seeds crafting recipe enabled");
            }
        });

        HOReborn.LOGGER.info("RecipeManager initialized");
        initialized = true;
    }
}
