package org.Netroaki.Main.modules;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.util.SereneSeasonsAPI;
// import org.Netroaki.Main.util.DebugMetrics;

public class SereneSeasonsModule {

    private static boolean compatibilityEnabled = true;

    public void init() {
        HOReborn.LOGGER.info("Initializing Serene Seasons compatibility module");
        LifecycleEvent.SERVER_LEVEL_LOAD.register(this::onServerStarting);
    }

    private void onServerStarting(net.minecraft.world.level.Level level) {
        if (SereneSeasonsAPI.isAvailable() && compatibilityEnabled) {
            HOReborn.LOGGER.info("Serene Seasons compatibility: Active");
            HOReborn.LOGGER.info("Current season: " + SereneSeasonsAPI.getSeasonName(level));
        }
    }

    public EventResult onCropRandomTick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide() || !SereneSeasonsAPI.isAvailable() || !compatibilityEnabled) {
            return EventResult.pass();
        }

        Block block = state.getBlock();
        if (!(block instanceof CropBlock)) {
            return EventResult.pass();
        }

        // DebugMetrics.recordAttempt();

        // Get current season info from Serene Seasons API
        SereneSeasonsAPI.SeasonInfo seasonInfo = SereneSeasonsAPI.getCurrentSeason(level);
        if (seasonInfo == null) {
            // Fall back to Hunger Overhaul logic only
            return applyHungerOverhaulLogic(level, pos, state);
        }

        // Apply Serene Seasons growth logic first
        boolean shouldGrow = shouldCropGrowInSeason(seasonInfo, level, pos, state);

        if (!shouldGrow) {
            return EventResult.interrupt(false); // Cancel growth
        }

        // Then apply Hunger Overhaul logic on top of Serene Seasons
        return applyHungerOverhaulLogic(level, pos, state);
    }

    private boolean shouldCropGrowInSeason(SereneSeasonsAPI.SeasonInfo seasonInfo, Level level, BlockPos pos,
            BlockState state) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // Check if seasonal growth is enabled
        if (!config.crops.respectSeasonalGrowth) {
            return true; // Allow growth regardless of season
        }

        // Use the new API to check if crops should grow
        // Serene Seasons handles fertility checks internally
        return SereneSeasonsAPI.shouldCropsGrow(level, pos, state);
    }

    private EventResult applyHungerOverhaulLogic(Level level, BlockPos pos, BlockState state) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // Apply daylight-only growth
        if (config.crops.cropsOnlyGrowInDaylight) {
            int sky = level.getBrightness(LightLayer.SKY, pos);
            if (!level.isDay() || sky < 9) {
                // DebugMetrics.recordDaylightBlocked();
                return EventResult.interrupt(false);
            }
        }

        // Apply growth multiplier
        if (config.crops.cropsTakeLongerToGrow) {
            RandomSource random = level.getRandom();
            double multiplier = config.crops.cropGrowthMultiplier;

            // Use the new API to calculate season-adjusted multiplier
            multiplier = SereneSeasonsAPI.calculateGrowthMultiplier(level, multiplier);

            if (random.nextDouble() > multiplier) {
                // DebugMetrics.recordRandomRejected();
                return EventResult.interrupt(false);
            }
        }

        // DebugMetrics.recordAllowed();
        return EventResult.pass();
    }

    public static boolean isSereneSeasonsLoaded() {
        return SereneSeasonsAPI.isAvailable();
    }

    public static void setCompatibilityEnabled(boolean enabled) {
        compatibilityEnabled = enabled;
        if (SereneSeasonsAPI.isAvailable()) {
            HOReborn.LOGGER.info("Serene Seasons compatibility " + (enabled ? "enabled" : "disabled"));
        }
    }

    public static boolean isCompatibilityEnabled() {
        return compatibilityEnabled;
    }
}
