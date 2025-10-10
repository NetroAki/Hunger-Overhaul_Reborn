package org.Netroaki.Main.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.util.DebugMetrics;
import org.Netroaki.Main.util.SereneSeasonsAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CropBlock.class)
public class AgeableBlockMixin {

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void hor_onRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random,
            CallbackInfo ci) {
        // Track attempt
        DebugMetrics.recordAttempt();

        // Update debug context early so HUD/logs show actual season
        try {
            double base = HungerOverhaulConfig.getInstance().crops.cropGrowthMultiplier;
            // preview adjusted just for context; do not short-circuit here
            double adjustedPreview = SereneSeasonsAPI.calculateGrowthMultiplier(level, base);
            DebugMetrics.updateContext(SereneSeasonsAPI.getSeasonName(level), SereneSeasonsAPI.getSeasonStrength(level),
                    base, adjustedPreview);
        } catch (Throwable ignored) {
        }

        // Daylight-only gate
        if (HungerOverhaulConfig.getInstance().crops.cropsOnlyGrowInDaylight) {
            int sky = level.getBrightness(LightLayer.SKY, pos);
            if (!level.isDay() || sky < 9) {
                DebugMetrics.recordDaylightBlocked();
                ci.cancel();
                return;
            }
        }

        // Serene Seasons fertility gate - check if crop can grow in current season
        if (!SereneSeasonsAPI.shouldCropsGrow(level, pos, state)) {
            DebugMetrics.recordRandomRejected(); // Use random rejected for fertility blocking
            ci.cancel();
            return;
        }

        // Probability gate: 66% reduction for fertile crops
        double base = HungerOverhaulConfig.getInstance().crops.cropGrowthMultiplier;
        double adjusted = SereneSeasonsAPI.calculateGrowthMultiplier(level, base);

        if (random.nextDouble() > adjusted) {
            DebugMetrics.recordRandomRejected();
            ci.cancel();
            return;
        }

        // Allowed
        DebugMetrics.recordAllowed();
    }
}
