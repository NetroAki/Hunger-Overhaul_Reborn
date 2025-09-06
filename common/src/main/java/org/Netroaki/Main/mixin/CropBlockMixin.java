package org.Netroaki.Main.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CropBlock.class)
public class CropBlockMixin {

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void onRandomTick(BlockState state, ServerLevel level, BlockPos pos,
            RandomSource random, CallbackInfo ci) {
        if (HungerOverhaulConfig.getInstance().crops.cropsOnlyGrowInDaylight) {
            int sky = level.getBrightness(LightLayer.SKY, pos);
            if (!level.isDay() || sky < 9) {
                ci.cancel();
                return;
            }
        }

        if (HungerOverhaulConfig.getInstance().crops.cropsTakeLongerToGrow) {
            double mul = HungerOverhaulConfig.getInstance().crops.cropGrowthMultiplier;
            if (random.nextDouble() > mul) {
                ci.cancel();
            }
        }
    }
}
