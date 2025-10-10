package org.Netroaki.Main.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.util.DebugMetrics;

import java.lang.reflect.Method;

/**
 * Interface for interacting with Serene Seasons API
 * Uses reflection to avoid compile-time dependencies
 */
public class SereneSeasonsAPI {

    private static boolean apiAvailable = false;
    private static Class<?> seasonHelperClass = null;
    private static Class<?> seasonClass = null;
    private static Class<?> iSeasonStateClass = null;
    private static Method getSeasonStateMethod = null;
    private static Method stateGetSeasonMethod = null;
    private static Method stateGetSubSeasonMethod = null;

    // Season enum values (cached for performance)
    private static Object SPRING = null;
    private static Object SUMMER = null;
    private static Object AUTUMN = null;
    private static Object WINTER = null;

    static {
        initializeAPI();
    }

    private static void initializeAPI() {
        try {
            // Load Serene Seasons API classes
            seasonHelperClass = Class.forName("sereneseasons.api.season.SeasonHelper");
            seasonClass = Class.forName("sereneseasons.api.season.Season");
            iSeasonStateClass = Class.forName("sereneseasons.api.season.ISeasonState");

            // Get SeasonHelper.getSeasonState(Level) method
            getSeasonStateMethod = seasonHelperClass.getMethod("getSeasonState", Level.class);

            // Get ISeasonState methods
            stateGetSeasonMethod = iSeasonStateClass.getMethod("getSeason");
            stateGetSubSeasonMethod = iSeasonStateClass.getMethod("getSubSeason");

            // Cache season enum values
            SPRING = seasonClass.getField("SPRING").get(null);
            SUMMER = seasonClass.getField("SUMMER").get(null);
            AUTUMN = seasonClass.getField("AUTUMN").get(null);
            WINTER = seasonClass.getField("WINTER").get(null);

            apiAvailable = true;
            HOReborn.LOGGER.info("Serene Seasons API initialized successfully");

        } catch (Exception e) {
            apiAvailable = false;
            HOReborn.LOGGER.debug("Serene Seasons API not available: " + e.getMessage());
        }
    }

    /**
     * Check if Serene Seasons API is available
     */
    public static boolean isAvailable() {
        return apiAvailable;
    }

    /**
     * Get the current season
     */
    public static SeasonInfo getCurrentSeason(Level level) {
        if (!apiAvailable) {
            return null;
        }

        try {
            // Use SeasonHelper.getSeasonState(Level) to get ISeasonState
            Object seasonState = getSeasonStateMethod.invoke(null, level);
            if (seasonState == null) {
                return null;
            }

            // Get season and sub-season from the state
            Object season = stateGetSeasonMethod.invoke(seasonState);
            Object subSeason = stateGetSubSeasonMethod.invoke(seasonState);

            // Compute strength based on sub-season
            float strength = computeDerivedStrength(subSeason);

            return new SeasonInfo(season, subSeason, strength);
        } catch (Exception e) {
            HOReborn.LOGGER.warn("Failed to get season info: " + e.getMessage());
            return null;
        }
    }

    private static float computeDerivedStrength(Object subSeasonObj) {
        // All sub-seasons have equal strength within their season
        return 1.0f;
    }

    /**
     * Check if the current season is winter
     */
    public static boolean isWinter(Level level) {
        SeasonInfo info = getCurrentSeason(level);
        if (info == null)
            return false;
        return matchesBaseSeason(info, WINTER, "WINTER");
    }

    /**
     * Check if the current season is summer
     */
    public static boolean isSummer(Level level) {
        SeasonInfo info = getCurrentSeason(level);
        if (info == null)
            return false;
        return matchesBaseSeason(info, SUMMER, "SUMMER");
    }

    /**
     * Check if the current season is spring
     */
    public static boolean isSpring(Level level) {
        SeasonInfo info = getCurrentSeason(level);
        if (info == null)
            return false;
        return matchesBaseSeason(info, SPRING, "SPRING");
    }

    /**
     * Check if the current season is autumn
     */
    public static boolean isAutumn(Level level) {
        SeasonInfo info = getCurrentSeason(level);
        if (info == null)
            return false;
        return matchesBaseSeason(info, AUTUMN, "AUTUMN");
    }

    /**
     * Get season name as string
     */
    public static String getSeasonName(Level level) {
        SeasonInfo info = getCurrentSeason(level);
        if (info == null)
            return "UNKNOWN";
        // Prefer sub-season if present: e.g., EARLY_SPRING -> "Early Spring"
        if (info.subSeason != null) {
            String name = toDisplayName(String.valueOf(info.subSeason));
            if (!name.isEmpty())
                return name;
        }
        if (info.season != null) {
            String base = toDisplayName(String.valueOf(info.season));
            if (!base.isEmpty())
                return base;
        }
        return "UNKNOWN";
    }

    private static boolean matchesBaseSeason(SeasonInfo info, Object constant, String... nameHints) {
        try {
            if (info.season != null && info.season.equals(constant))
                return true;
            String sub = info.subSeason != null ? String.valueOf(info.subSeason) : "";
            String base = info.season != null ? String.valueOf(info.season) : "";
            for (String hint : nameHints) {
                if (sub.contains(hint))
                    return true;
                if (base.contains(hint))
                    return true;
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    private static String toDisplayName(String enumName) {
        if (enumName == null || enumName.isEmpty())
            return "";
        String name = enumName.toLowerCase().replace('_', ' ');
        String[] parts = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty())
                continue;
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1)).append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * Get season strength (0.0 to 1.0)
     */
    public static float getSeasonStrength(Level level) {
        SeasonInfo info = getCurrentSeason(level);
        return info != null ? info.strength : 1.0f;
    }

    /**
     * Calculate growth multiplier based on season and difficulty
     * Difficulty-based reductions:
     * - Peaceful: 0% reduction (1.0x multiplier)
     * - Easy: 33% reduction (0.67x multiplier)
     * - Normal: 50% reduction (0.5x multiplier)
     * - Hard: 66% reduction (0.34x multiplier)
     * Serene Seasons handles fertile season checks separately
     */
    public static double calculateGrowthMultiplier(Level level, double baseMultiplier) {
        if (!apiAvailable) {
            return baseMultiplier;
        }

        SeasonInfo info = getCurrentSeason(level);
        if (info == null) {
            return baseMultiplier;
        }

        // Get difficulty-based multiplier
        double difficultyMultiplier = getDifficultyMultiplier(level);

        // Apply difficulty-based reduction
        double adjusted = baseMultiplier * difficultyMultiplier;

        // Clamp to [0, 1] to avoid invalid probabilities
        if (adjusted < 0.0)
            adjusted = 0.0;
        if (adjusted > 1.0)
            adjusted = 1.0;

        // Update debug context for periodic logs
        try {
            String difficulty = level.getDifficulty().name();
            DebugMetrics.updateContext(
                    getSeasonName(level) + " (" + difficulty + ")",
                    getSeasonStrength(level),
                    baseMultiplier,
                    adjusted);
        } catch (Throwable t) {
            // ignore
        }
        return adjusted;
    }

    /**
     * Check if a crop is fertile in the current season using Serene Seasons'
     * fertility system
     */
    public static boolean isCropFertile(Level level, BlockPos pos, BlockState state) {
        if (!apiAvailable) {
            return true; // Default to fertile if Serene Seasons not available
        }

        try {
            // Use reflection to call ModFertility.isCropFertile
            Class<?> modFertilityClass = Class.forName("sereneseasons.init.ModFertility");
            Method isCropMethod = modFertilityClass.getMethod("isCrop", BlockState.class);
            Method isCropFertileMethod = modFertilityClass.getMethod("isCropFertile", String.class, Level.class,
                    BlockPos.class);

            // Check if this is a crop
            boolean isCrop = (Boolean) isCropMethod.invoke(null, state);
            if (!isCrop) {
                return true; // Not a crop, allow growth
            }

            // Get block registry key
            String blockKey = level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.BLOCK)
                    .getKey(state.getBlock()).toString();

            // Check fertility
            return (Boolean) isCropFertileMethod.invoke(null, blockKey, level, pos);
        } catch (Exception e) {
            HOReborn.LOGGER.warn("Failed to check crop fertility: " + e.getMessage());
            return true; // Default to fertile on error
        }
    }

    /**
     * Check if crops should grow in current season
     * Uses Serene Seasons' fertility system to determine if crops can grow
     */
    public static boolean shouldCropsGrow(Level level, BlockPos pos, BlockState state) {
        if (!apiAvailable) {
            return true; // Default to allowing growth if Serene Seasons not available
        }

        return isCropFertile(level, pos, state);
    }

    /**
     * Container class for season information
     */
    public static class SeasonInfo {
        public final Object season;
        public final Object subSeason;
        public final float strength;

        public SeasonInfo(Object season, Object subSeason, float strength) {
            this.season = season;
            this.subSeason = subSeason;
            this.strength = strength;
        }

        @Override
        public String toString() {
            return "SeasonInfo{season=" + season + ", subSeason=" + subSeason + ", strength=" + strength + "}";
        }
    }

    /**
     * Get the growth multiplier based on world difficulty
     */
    private static double getDifficultyMultiplier(Level level) {
        try {
            net.minecraft.world.Difficulty difficulty = level.getDifficulty();

            switch (difficulty) {
                case PEACEFUL:
                    return 1.0; // No reduction - full vanilla speed
                case EASY:
                    return 0.67; // 33% reduction
                case NORMAL:
                    return 0.5; // 50% reduction
                case HARD:
                    return 0.34; // 66% reduction
                default:
                    return 0.5; // Default to Normal
            }
        } catch (Exception e) {
            HOReborn.LOGGER.error("Error getting difficulty multiplier", e);
            return 0.5; // Default to Normal difficulty
        }
    }
}
