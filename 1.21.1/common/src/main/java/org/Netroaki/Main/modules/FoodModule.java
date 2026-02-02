package org.Netroaki.Main.modules;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.util.FoodUtil;

import net.minecraft.core.component.DataComponents;

public class FoodModule {

    public void init() {
        HOReborn.LOGGER.info("Initializing Food Module (1.21.1)");

        // Register client events safely
        EnvExecutor.runInEnv(Env.CLIENT, () -> FoodModuleClient::init);
    }

    // Legacy method kept for API compatibility if needed, but logic moved to Client
    // module for events
    public static Component getFoodTooltip(ItemStack stack) {
        if (!stack.has(DataComponents.FOOD))
            return null;
        String desc = FoodUtil.getFoodDescription(stack);
        return desc.isEmpty() ? null : Component.literal(desc);
    }
}
