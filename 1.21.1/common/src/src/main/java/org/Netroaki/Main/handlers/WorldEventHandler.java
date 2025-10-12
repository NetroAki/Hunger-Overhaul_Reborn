package org.Netroaki.Main.handlers;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.Netroaki.Main.config.HungerOverhaulConfig;

import java.util.Random;

public class WorldEventHandler {
    private static final Random RANDOM = new Random();

    public static void register() {
        // Block break for removing tall grass seeds
        // BlockEvent.BREAK.register(WorldEventHandler::onBlockBreak);

        // World tick for crop growth and animal modifications
        TickEvent.SERVER_LEVEL_PRE.register(WorldEventHandler::onWorldTick);
    }

    private static EventResult onBlockBreak(Level level, BlockPos pos, BlockState state, ServerPlayer player, int xp) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        if (config.crops.removeTallGrassSeeds && state.is(net.minecraft.world.level.block.Blocks.TALL_GRASS)) {
            // Remove seed drops from tall grass
            return EventResult.interrupt(false);
        }

        return EventResult.pass();
    }

    private static void onWorldTick(Level level) {
        // Handle world-level events like crop growth and animal modifications
        // This will be implemented in platform-specific handlers
    }

    public static boolean shouldBoneMealWork(Level level) {
        // Check if bone meal should work based on difficulty
        if (level.getDifficulty() == net.minecraft.world.Difficulty.PEACEFUL ||
                level.getDifficulty() == net.minecraft.world.Difficulty.EASY) {
            return true; // Normal effect
        } else if (level.getDifficulty() == net.minecraft.world.Difficulty.NORMAL) {
            // 50% chance to work on Normal difficulty
            return level.getRandom().nextDouble() < 0.5;
        } else if (level.getDifficulty() == net.minecraft.world.Difficulty.HARD) {
            // 25% chance to work on Hard difficulty
            return level.getRandom().nextDouble() < 0.25;
        }
        return false;
    }

    public static boolean shouldCropGrow(BlockState state, Level level, BlockPos pos) {
        // Check if crop should grow based on daylight
        return level.getBrightness(net.minecraft.world.level.LightLayer.SKY, pos) >= 9;
    }
}
