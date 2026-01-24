package org.Netroaki.Main.modules;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.util.BonemealModification;

public class CropModule {

    public void init() {
        HOReborn.LOGGER.info("Initializing Crop Module");
        LifecycleEvent.SERVER_LEVEL_LOAD.register(this::onServerStarting);
    }

    private void onServerStarting(Level level) {
        HOReborn.LOGGER.info("Crop Module: Features enabled");
    }

    public EventResult onBoneMealUse(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitResult) {
        // Check if bonemeal difficulty scaling is enabled
        if (!HungerOverhaulConfig.getInstance().crops.difficultyScalingBoneMeal) {
            return EventResult.pass();
        }

        ItemStack stack = player.getItemInHand(hand);

        if (!(stack.getItem() instanceof BoneMealItem)) {
            return EventResult.pass();
        }

        Level level = player.level();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        // Only gate bone meal for bonemealable blocks (crops etc.)
        if (!(block instanceof BonemealableBlock)) {
            return EventResult.pass();
        }

        // Special handling for crops using the new BonemealModification system
        if (block instanceof CropBlock crop && level instanceof ServerLevel serverLevel) {
            // Use the new bonemeal modification system for crops
            boolean success = BonemealModification.applyBonemeal(serverLevel, pos, state, crop);

            // Handle consumption and effects
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            player.swing(hand);

            if (!level.isClientSide) {
                level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (success) {
                    level.levelEvent(2005, pos, 0); // bone meal particles only on success
                }
            }

            // Always return success to show interaction, even if growth didn't occur
            return EventResult.interrupt(true);
        }

        // For non-crop bonemealable blocks, use the old difficulty scaling logic
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        double chance;
        switch (level.getDifficulty()) {
            case PEACEFUL:
            case EASY:
                chance = config.crops.bonemealSuccessRateEasy;
                break;
            case NORMAL:
                chance = config.crops.bonemealSuccessRateNormal;
                break;
            case HARD:
                chance = config.crops.bonemealSuccessRateHard;
                break;
            default:
                chance = config.crops.bonemealSuccessRateEasy; // Default to easy rate
        }

        if (player.getRandom().nextDouble() <= chance) {
            // Allow vanilla to process fully (animation, growth, particles)
            return EventResult.pass();
        }

        // Fail path: consume one (unless creative), play sound and particles so it
        // looks like interaction
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        // Ensure client swing animation
        player.swing(hand);
        if (!level.isClientSide) {
            level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
            level.levelEvent(2005, pos, 0); // bone meal particles
        }
        // Return success to show hand animation and cancel vanilla growth
        return EventResult.interrupt(true);
    }

    public EventResult onCropRandomTick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide())
            return EventResult.pass();

        Block block = state.getBlock();

        if (block instanceof CropBlock) {
            // Additional throttling handled in mixin
            return EventResult.pass();
        }

        return EventResult.pass();
    }
}
