package org.Netroaki.Main.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.Netroaki.Main.config.HungerOverhaulConfig;

/**
 * Utility for modifying bone meal behavior on crops.
 * Implements configurable bone meal effectiveness and partial growth.
 */
public class BonemealModification {

    private static final RandomSource RANDOM = RandomSource.create();

    /**
     * Handle bone meal application on crops with configurable behavior.
     *
     * @param level The server level
     * @param pos Block position
     * @param state Current block state
     * @param crop The crop block
     * @return true if bone meal was applied successfully, false otherwise
     */
    public static boolean applyBonemeal(ServerLevel level, BlockPos pos, BlockState state, CropBlock crop) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // Check bone meal effectiveness
        double effectiveness = config.crops.bonemealSuccessRateEasy; // Default to easy, modify based on difficulty

        // Adjust based on difficulty
        switch (level.getDifficulty()) {
            case EASY -> effectiveness = config.crops.bonemealSuccessRateEasy;
            case NORMAL -> effectiveness = config.crops.bonemealSuccessRateNormal;
            case HARD -> effectiveness = config.crops.bonemealSuccessRateHard;
            default -> effectiveness = config.crops.bonemealSuccessRateEasy;
        }

        // Check if bone meal succeeds
        if (RANDOM.nextDouble() >= effectiveness) {
            return false; // Bone meal failed
        }

        // Bone meal succeeded - apply growth
        if (config.crops.modifyBonemealGrowth) {
            // Partial growth instead of full growth
            applyPartialGrowth(level, pos, state, crop);
        } else {
            // Default vanilla behavior - grow to next stage
            crop.growCrops(level, pos, state);
        }

        return true;
    }

    /**
     * Apply partial growth to crops when bonemeal is used.
     * Instead of growing to maturity, grow by 2-3 stages.
     */
    private static void applyPartialGrowth(ServerLevel level, BlockPos pos, BlockState state, CropBlock crop) {
        int currentAge = crop.getAge(state);
        int maxAge = crop.getMaxAge();

        if (currentAge >= maxAge) {
            return; // Already mature
        }

        // Grow by 2-3 stages instead of to maturity
        int growthStages = 2 + RANDOM.nextInt(2); // 2 or 3 stages
        int newAge = Math.min(currentAge + growthStages, maxAge);

        // Apply the growth
        BlockState newState = crop.getStateForAge(newAge);
        level.setBlock(pos, newState, 2);
    }

    /**
     * Check if bone meal modifications are enabled.
     */
    public static boolean isBoneMealModificationEnabled() {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        return config.crops.difficultyScalingBoneMeal || config.crops.modifyBonemealGrowth;
    }
}
