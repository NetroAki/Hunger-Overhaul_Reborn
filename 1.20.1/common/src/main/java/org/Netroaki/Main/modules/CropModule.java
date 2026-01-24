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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

public class CropModule {

    public void init() {
        HOReborn.LOGGER.info("Initializing Crop Module");
        LifecycleEvent.SERVER_LEVEL_LOAD.register(this::onServerStarting);
    }

    private void onServerStarting(Level level) {
        HOReborn.LOGGER.info("Crop Module: Features enabled");
    }

    public EventResult onBoneMealUse(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitResult) {
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

        // Chance by difficulty: Easy/Peaceful normal; Normal 50%; Hard 25%
        double chance;
        switch (level.getDifficulty()) {
            case PEACEFUL:
            case EASY:
                chance = 1.0;
                break;
            case NORMAL:
                chance = 0.5;
                break;
            case HARD:
                chance = 0.25;
                break;
            default:
                chance = 1.0;
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
