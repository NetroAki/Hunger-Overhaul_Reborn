package org.Netroaki.Main.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.Netroaki.Main.config.HungerOverhaulConfig;

import java.util.Random;

/**
 * Handles right-click harvesting of crops.
 * When a player right-clicks on a mature crop, it resets to age 0 and drops configurable amounts.
 */
public class HarvestHandler {

    private static final Random RANDOM = new Random();

    /**
     * Handle right-click interaction with blocks.
     * Returns true if harvesting was handled, false otherwise.
     */
    public static InteractionResult handleRightClickHarvest(Player player, Level level, InteractionHand hand,
                                                          BlockHitResult hitResult) {
        if (!HungerOverhaulConfig.getInstance().harvesting.enableRightClickHarvesting) {
            return InteractionResult.PASS;
        }

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        if (!(state.getBlock() instanceof CropBlock crop)) {
            return InteractionResult.PASS;
        }

        // Check if crop is mature
        if (!crop.isMaxAge(state)) {
            return InteractionResult.PASS;
        }

        // Handle harvesting on server side only
        if (level.isClientSide) {
            return InteractionResult.SUCCESS; // Client prediction
        }

        ServerLevel serverLevel = (ServerLevel) level;

        // Reset crop to age 0
        serverLevel.setBlock(pos, crop.getStateForAge(0), 3);

        // Drop items based on configuration
        dropHarvestItems(serverLevel, pos, crop, state, true); // true = right-click harvest

        return InteractionResult.SUCCESS;
    }

    /**
     * Drop harvest items based on configuration.
     */
    public static void dropHarvestItems(ServerLevel level, BlockPos pos, CropBlock crop,
                                       BlockState state, boolean isRightClick) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // Get drop configuration
        int seedsMin, seedsMax, produceMin, produceMax;

        if (isRightClick && config.harvesting.modifyCropDropsRightClick) {
            seedsMin = config.harvesting.seedsPerHarvestRightClickMin;
            seedsMax = config.harvesting.seedsPerHarvestRightClickMax;
            produceMin = config.harvesting.producePerHarvestRightClickMin;
            produceMax = config.harvesting.producePerHarvestRightClickMax;
        } else if (!isRightClick && config.harvesting.modifyCropDropsBreak) {
            seedsMin = config.harvesting.seedsPerHarvestBreakMin;
            seedsMax = config.harvesting.seedsPerHarvestBreakMax;
            produceMin = config.harvesting.producePerHarvestBreakMin;
            produceMax = config.harvesting.producePerHarvestBreakMax;
        } else {
            // Use vanilla drops if not configured
            dropVanillaItems(level, pos, state);
            return;
        }

        // Drop seeds
        if (seedsMax > 0) {
            int seedsToDrop = seedsMin;
            if (seedsMax > seedsMin) {
                seedsToDrop += RANDOM.nextInt(seedsMax - seedsMin + 1);
            }

            ItemStack seedStack = crop.getCloneItemStack(level, pos, state);
            if (!seedStack.isEmpty()) {
                seedStack.setCount(seedsToDrop);
                net.minecraft.world.level.block.Block.popResource(level, pos, seedStack);
            }
        }

        // Drop produce
        if (produceMax > 0) {
            int produceToDrop = produceMin;
            if (produceMax > produceMin) {
                produceToDrop += RANDOM.nextInt(produceMax - produceMin + 1);
            }

            // Get crop drops (this will include the produce)
            var drops = net.minecraft.world.level.block.Block.getDrops(state, level,
                pos, null, null, ItemStack.EMPTY);

            for (ItemStack drop : drops) {
                if (drop.getItem() != crop.getCloneItemStack(level, pos, state).getItem()) {
                    // This is produce (not seeds)
                    ItemStack produceStack = drop.copy();
                    produceStack.setCount(Math.min(produceToDrop, produceStack.getCount()));
                    net.minecraft.world.level.block.Block.popResource(level, pos, produceStack);
                    produceToDrop -= produceStack.getCount();
                    if (produceToDrop <= 0) break;
                }
            }
        }
    }

    /**
     * Drop vanilla items (fallback when not configured).
     */
    private static void dropVanillaItems(ServerLevel level, BlockPos pos, BlockState state) {
        var drops = net.minecraft.world.level.block.Block.getDrops(state, level, pos, null, null, ItemStack.EMPTY);
        for (ItemStack drop : drops) {
            net.minecraft.world.level.block.Block.popResource(level, pos, drop);
        }
    }

    /**
     * Check if a crop should be harvestable by right-click.
     */
    public static boolean isHarvestableByRightClick(BlockState state) {
        if (!(state.getBlock() instanceof CropBlock crop)) {
            return false;
        }

        return crop.isMaxAge(state) && HungerOverhaulConfig.getInstance().harvesting.enableRightClickHarvesting;
    }
}
