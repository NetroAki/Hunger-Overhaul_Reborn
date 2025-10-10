package org.Netroaki.Main.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.level.BlockEvent;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
// import org.Netroaki.Main.util.DebugMetrics;
import org.Netroaki.Main.util.SereneSeasonsAPI;

import java.util.concurrent.ThreadLocalRandom;

@Mod.EventBusSubscriber(modid = HOReborn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GrowthEventHandler {

    @SubscribeEvent
    public static void onCropGrowPre(BlockEvent.CropGrowEvent.Pre event) {
        if (!(event.getLevel() instanceof Level level))
            return;

        // Track attempt
        // DebugMetrics.recordAttempt();

        BlockPos pos = event.getPos();

        // Update debug context early so HUD/logs show actual season
        // try {
        // double base = HungerOverhaulConfig.getInstance().crops.cropGrowthMultiplier;
        // // preview adjusted just for context; do not short-circuit here
        // double adjustedPreview = SereneSeasonsAPI.calculateGrowthMultiplier(level,
        // base);
        // DebugMetrics.updateContext(SereneSeasonsAPI.getSeasonName(level),
        // SereneSeasonsAPI.getSeasonStrength(level),
        // base, adjustedPreview);
        // } catch (Throwable ignored) {
        // }

        // Daylight-only gate
        if (HungerOverhaulConfig.getInstance().crops.cropsOnlyGrowInDaylight) {
            int sky = level.getBrightness(LightLayer.SKY, pos);
            if (!level.isDay() || sky < 9) {
                // DebugMetrics.recordDaylightBlocked();
                event.setResult(Event.Result.DENY);
                return;
            }
        }

        // Serene Seasons fertility gate - check if crop can grow in current season
        if (!SereneSeasonsAPI.shouldCropsGrow(level, pos, event.getState())) {
            // DebugMetrics.recordRandomRejected(); // Use random rejected for fertility
            // blocking
            event.setResult(Event.Result.DENY);
            return;
        }

        // Probability gate: 66% reduction for fertile crops
        double base = HungerOverhaulConfig.getInstance().crops.cropGrowthMultiplier;
        double adjusted = SereneSeasonsAPI.calculateGrowthMultiplier(level, base);

        if (ThreadLocalRandom.current().nextDouble() > adjusted) {
            // DebugMetrics.recordRandomRejected();
            event.setResult(Event.Result.DENY);
            return;
        }

        // Allowed
        // DebugMetrics.recordAllowed();
    }
}
