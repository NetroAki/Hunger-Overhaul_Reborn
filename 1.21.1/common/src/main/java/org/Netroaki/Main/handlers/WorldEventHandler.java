package org.Netroaki.Main.handlers;

import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.Netroaki.Main.HOReborn;

import java.util.Random;

public class WorldEventHandler {
    private static final Random RANDOM = new Random();

    public static void register() {
        // Register server level tick event
        TickEvent.SERVER_LEVEL_PRE.register(WorldEventHandler::onWorldTick);
        HOReborn.LOGGER.info("WorldEventHandler (1.21.1) registered");
    }

    private static void onWorldTick(Level level) {
        // Handle world-level events like crop growth if needed
        // Currently mostly handled by specific block mixins which are disabled in
        // 1.21.1
    }

    public static boolean shouldBoneMealWork(Level level) {
        // Check if bone meal should work based on difficulty
        if (level.getDifficulty() == net.minecraft.world.Difficulty.PEACEFUL ||
                level.getDifficulty() == net.minecraft.world.Difficulty.EASY) {
            return true;
        } else if (level.getDifficulty() == net.minecraft.world.Difficulty.NORMAL) {
            return level.getRandom().nextDouble() < 0.5;
        } else if (level.getDifficulty() == net.minecraft.world.Difficulty.HARD) {
            return level.getRandom().nextDouble() < 0.25;
        }
        return false;
    }

    public static boolean shouldCropGrow(BlockState state, Level level, BlockPos pos) {
        // Check if crop should grow based on daylight
        return level.getBrightness(net.minecraft.world.level.LightLayer.SKY, pos) >= 9;
    }
}
