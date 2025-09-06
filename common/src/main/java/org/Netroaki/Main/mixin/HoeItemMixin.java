package org.Netroaki.Main.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(HoeItem.class)
public class HoeItemMixin {

    private static final Random RANDOM = new Random();

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void onUseOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (!HungerOverhaulConfig.getInstance().tools.modifyHoeUse) {
            return; // Use vanilla behavior
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (state.is(Blocks.GRASS_BLOCK)) {
            boolean waterNearby = isWaterNearby(level, pos);

            if (!waterNearby) {
                level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);

                double seedChance = getSeedChanceForDifficulty(level);
                if (RANDOM.nextDouble() < seedChance) {
                    ItemStack seedStack = getRandomSeed();
                    Block.popResource(level, pos, seedStack);
                }

                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
            }
        }
    }

    private boolean isWaterNearby(Level level, BlockPos pos) {
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                for (int y = -2; y <= 2; y++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    if (level.getBlockState(checkPos).is(Blocks.WATER)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private double getSeedChanceForDifficulty(Level level) {
        double baseChance = HungerOverhaulConfig.getInstance().tools.seedChanceBase;
        double multiplier = HungerOverhaulConfig.getInstance().tools.seedChanceMultiplier;

        switch (level.getDifficulty()) {
            case PEACEFUL:
                return baseChance * multiplier * 0.4;
            case EASY:
                return baseChance * multiplier * 0.3;
            case NORMAL:
                return baseChance * multiplier * 0.2;
            case HARD:
                return baseChance * multiplier * 0.1;
            default:
                return baseChance * multiplier * 0.2;
        }
    }

    private ItemStack getRandomSeed() {
        ItemStack[] seeds = {
                new ItemStack(Items.WHEAT_SEEDS),
                new ItemStack(Items.BEETROOT_SEEDS),
                new ItemStack(Items.PUMPKIN_SEEDS),
                new ItemStack(Items.MELON_SEEDS)
        };
        return seeds[RANDOM.nextInt(seeds.length)];
    }
}
