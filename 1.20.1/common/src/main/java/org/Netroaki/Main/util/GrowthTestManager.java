package org.Netroaki.Main.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.commands.CommandSourceStack;
import org.Netroaki.Main.HOReborn;

import java.util.ArrayList;
import java.util.List;

public final class GrowthTestManager {

    private GrowthTestManager() {
    }

    public static int start(CommandSourceStack source) {
        ServerLevel level = source.getLevel();

        // Use a fixed integer origin based on the source position
        BlockPos origin = BlockPos.containing(source.getPosition());

        // Discover all registered crop blocks
        List<CropBlock> crops = new ArrayList<>();
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block instanceof CropBlock crop) {
                crops.add(crop);
            }
        }

        if (crops.isEmpty()) {
            source.sendFailure(net.minecraft.network.chat.Component.literal("[HOR Debug] No crops found!"));
            return 0;
        }

        HOReborn.LOGGER.info("[HOR Debug] Found {} crop types to test", crops.size());

        // Create 7x7 plots with 1 block spacing between each
        // Each plot is 7x7, with water at center (0,0 relative), so we need 8 blocks
        // per plot (7 + 1 spacing)
        int plotSize = 7;
        int spacing = 1;
        int stride = plotSize + spacing; // 8 blocks per plot
        int plotsPerRow = 8; // Arrange in an 8x8 grid of plots

        long startTime = level.getGameTime();
        List<PlotInfo> plots = new ArrayList<>();
        int cropTotal = 0;

        for (int i = 0; i < crops.size(); i++) {
            CropBlock crop = crops.get(i);
            int plotRow = i / plotsPerRow;
            int plotCol = i % plotsPerRow;

            // Calculate base position for this plot
            int baseX = origin.getX() + (plotCol * stride);
            int baseZ = origin.getZ() + (plotRow * stride);

            // Create 7x7 farmland with water at center
            for (int x = 0; x < plotSize; x++) {
                for (int z = 0; z < plotSize; z++) {
                    BlockPos farmPos = new BlockPos(baseX + x, 200, baseZ + z);
                    level.setBlock(farmPos, Blocks.FARMLAND.defaultBlockState(), 3);
                }
            }

            // Water at center (3,3 in 7x7 grid)
            BlockPos waterPos = new BlockPos(baseX + 3, 200, baseZ + 3);
            level.setBlock(waterPos, Blocks.WATER.defaultBlockState(), 3);

            // Plant crops at age 0 (except at water position)
            int cropsInPlot = 0;
            for (int x = 0; x < plotSize; x++) {
                for (int z = 0; z < plotSize; z++) {
                    if (x == 3 && z == 3)
                        continue; // Skip water position

                    BlockPos cropPos = new BlockPos(baseX + x, 201, baseZ + z);
                    BlockState cropState = crop.getStateForAge(0);
                    level.setBlock(cropPos, cropState, 3);
                    cropsInPlot++;
                }
            }

            plots.add(new PlotInfo(baseX, baseZ));
            cropTotal += cropsInPlot;
        }

        // Schedule polling
        final int totalCrops = cropTotal;
        schedulePoll(level, source, startTime, plots, totalCrops);
        source.sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                "[HOR Debug] Growth test started with " + crops.size() + " crop types (" + totalCrops
                        + " total crops)."),
                true);
        return 1;
    }

    private static void schedulePoll(ServerLevel level, CommandSourceStack source, long startGameTime,
            List<PlotInfo> plots, int totalCrops) {
        level.getServer().execute(() -> poll(level, source, startGameTime, plots, totalCrops));
    }

    private static void poll(ServerLevel level, CommandSourceStack source, long startGameTime, List<PlotInfo> plots,
            int totalCrops) {
        int matured = 0;
        int plotSize = 7;

        // Check all plots
        for (PlotInfo plot : plots) {
            for (int x = 0; x < plotSize; x++) {
                for (int z = 0; z < plotSize; z++) {
                    if (x == 3 && z == 3)
                        continue; // Skip water position

                    BlockPos pos = new BlockPos(plot.baseX + x, 201, plot.baseZ + z);
                    BlockState state = level.getBlockState(pos);
                    if (state.getBlock() instanceof CropBlock crop) {
                        if (crop.isMaxAge(state)) {
                            matured++;
                        }
                    }
                }
            }
        }

        if (matured >= totalCrops) {
            final int maturedFinal = matured;
            final int totalFinal = totalCrops;
            final long ticksFinal = level.getGameTime() - startGameTime;
            source.sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                    "[HOR Debug] Growth test complete: " + maturedFinal + "/" + totalFinal + " in " + ticksFinal
                            + " ticks."),
                    true);
            return;
        }

        // Reschedule after ~10 ticks
        level.getServer().tell(new net.minecraft.server.TickTask(level.getServer().getTickCount() + 10,
                () -> poll(level, source, startGameTime, plots, totalCrops)));
    }

    private static class PlotInfo {
        final int baseX;
        final int baseZ;

        PlotInfo(int baseX, int baseZ) {
            this.baseX = baseX;
            this.baseZ = baseZ;
        }
    }
}
