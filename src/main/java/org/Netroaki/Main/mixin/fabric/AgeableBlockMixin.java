package org.Netroaki.Main.mixin.fabric;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.Netroaki.Main.api.PlantGrowthEvent;
import org.Netroaki.Main.config.HungerOverhaulConfig;
// import org.Netroaki.Main.util.DebugMetrics;
import org.Netroaki.Main.util.BiomeGrowthRegistry;
import org.Netroaki.Main.util.BlockGrowthRegistry;
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
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // Initialize registries if needed
        BlockGrowthRegistry.initialize();
        BiomeGrowthRegistry.initialize();

        // Fire AppleCore PlantGrowthEvent.AllowGrowthTick
        CropBlock cropBlock = (CropBlock) (Object) this;
        int age = cropBlock.getAge(state);
        int maxAge = cropBlock.getMaxAge();
        BlockState newState = cropBlock.getStateForAge(age + 1);

        // Note: Plant growth event firing would go here in full AppleCore
        // implementation

        // Track attempt
        // DebugMetrics.recordAttempt();

        // Apply growth modifiers in order (original Hunger Overhaul logic)

        // 1. Sunlight check - match Forge behavior: strict block if not day or low sky
        // light
        if (config.crops.cropsOnlyGrowInDaylight) {
            int sky = level.getBrightness(LightLayer.SKY, pos);
            // Log debug info occasionally or if night
            // Log debug info occasionally or if night
            if (!level.isDay())
                org.Netroaki.Main.HOReborn.LOGGER
                        .info("Night growth check: Day=" + level.isDay() + " Sky=" + sky + " Pos=" + pos);

            if (!level.isDay() || sky < 9) {
                // Block growth completely like Forge does
                // DebugMetrics.recordDaylightBlocked();
                ci.cancel();
                return;
            }
        }

        // 2. Biome check
        double biomeModifier = 1.0;
        var biomeKey = level.getBiome(pos).unwrapKey();
        if (biomeKey.isPresent()) {
            // Check if sugarcane (special case)
            boolean isSugarcane = state.getBlock() == net.minecraft.world.level.block.Blocks.SUGAR_CANE;
            if (isSugarcane) {
                biomeModifier = BiomeGrowthRegistry.getSugarcaneBiomeMultiplier(
                        level.getBiome(pos).value(), biomeKey.get());
            } else {
                biomeModifier = BiomeGrowthRegistry.getBiomeMultiplier(
                        level.getBiome(pos).value(), biomeKey.get());
            }

            if (biomeModifier == 0) {
                // DebugMetrics.recordRandomRejected();
                ci.cancel();
                return;
            }
        }

        // 3. Serene Seasons fertility check
        if (!SereneSeasonsAPI.shouldCropsGrow(level, pos, state)) {
            // DebugMetrics.recordRandomRejected();
            ci.cancel();
            return;
        }

        // 4. Get per-crop type multiplier
        double cropTypeModifier = BlockGrowthRegistry.getGrowthMultiplier(state.getBlock());

        // 5. Apply season multiplier (Serene Seasons)
        double seasonModifier = SereneSeasonsAPI.calculateGrowthMultiplier(level, 1.0);

        // 6. Apply difficulty multiplier
        double difficultyModifier = getDifficultyMultiplier(level);

        // Calculate final probability
        double finalMultiplier = biomeModifier * cropTypeModifier *
                seasonModifier * difficultyModifier;

        // Clamp to valid range
        finalMultiplier = Math.max(0.0, Math.min(1.0, finalMultiplier));

        // Apply random check
        if (random.nextDouble() > finalMultiplier) {
            // DebugMetrics.recordRandomRejected();
            ci.cancel();
            return;
        }

        // Allowed to grow
        // DebugMetrics.recordAllowed();
    }

    private double getDifficultyMultiplier(ServerLevel level) {
        switch (level.getDifficulty()) {
            case PEACEFUL:
            case EASY:
                return 0.75; // 75% of normal growth
            case NORMAL:
                return 1.0; // Normal growth
            case HARD:
                return 1.5; // 150% slower growth (66% chance)
            default:
                return 1.0;
        }
    }
}
