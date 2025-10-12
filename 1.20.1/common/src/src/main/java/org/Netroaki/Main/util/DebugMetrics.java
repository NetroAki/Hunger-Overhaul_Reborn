package org.Netroaki.Main.util;

import org.Netroaki.Main.HOReborn;

import java.util.concurrent.atomic.AtomicLong;

public final class DebugMetrics {

    private static final long LOG_INTERVAL_MS = 10_000L;

    private static final AtomicLong windowAttempts = new AtomicLong();
    private static final AtomicLong windowAllowed = new AtomicLong();
    private static final AtomicLong windowDaylightBlocked = new AtomicLong();
    private static final AtomicLong windowStrengthBlocked = new AtomicLong();
    private static final AtomicLong windowRandomRejected = new AtomicLong();

    private static final AtomicLong totalAttempts = new AtomicLong();
    private static final AtomicLong totalAllowed = new AtomicLong();
    private static final AtomicLong totalDaylightBlocked = new AtomicLong();
    private static final AtomicLong totalStrengthBlocked = new AtomicLong();
    private static final AtomicLong totalRandomRejected = new AtomicLong();

    private static volatile long lastLogMs = System.currentTimeMillis();

    private static volatile String lastSeason = "UNKNOWN";
    private static volatile float lastSeasonStrength = -1f;
    private static volatile double lastBaseMultiplier = -1.0;
    private static volatile double lastAdjustedMultiplier = -1.0;

    private DebugMetrics() {
    }

    public static void updateContext(String seasonName, float seasonStrength, double baseMultiplier,
            double adjustedMultiplier) {
        lastSeason = seasonName;
        lastSeasonStrength = seasonStrength;
        lastBaseMultiplier = baseMultiplier;
        lastAdjustedMultiplier = adjustedMultiplier;
    }

    public static void recordAttempt() {
        windowAttempts.incrementAndGet();
        totalAttempts.incrementAndGet();
        maybeLog();
    }

    public static void recordAllowed() {
        windowAllowed.incrementAndGet();
        totalAllowed.incrementAndGet();
        maybeLog();
    }

    public static void recordDaylightBlocked() {
        windowDaylightBlocked.incrementAndGet();
        totalDaylightBlocked.incrementAndGet();
        maybeLog();
    }

    public static void recordStrengthBlocked() {
        windowStrengthBlocked.incrementAndGet();
        totalStrengthBlocked.incrementAndGet();
        maybeLog();
    }

    public static void recordRandomRejected() {
        windowRandomRejected.incrementAndGet();
        totalRandomRejected.incrementAndGet();
        maybeLog();
    }

    public static synchronized void maybeLog() {
        long now = System.currentTimeMillis();
        if (now - lastLogMs < LOG_INTERVAL_MS) {
            return;
        }

        long wa = windowAttempts.getAndSet(0);
        long wl = windowAllowed.getAndSet(0);
        long wd = windowDaylightBlocked.getAndSet(0);
        long ws = windowStrengthBlocked.getAndSet(0);
        long wr = windowRandomRejected.getAndSet(0);

        double acceptRate = wa > 0 ? (double) wl / (double) wa : 0.0;

        HOReborn.LOGGER.info(String.format(
                "[HOR Debug] 10s window: attempts=%d allowed=%d daylight_block=%d winter_strength_block=%d prob_reject=%d accept_rate=%.3f season=%s strength=%.2f base=%.3f adjusted=%.3f total_attempts=%d total_allowed=%d",
                wa, wl, wd, ws, wr, acceptRate, lastSeason, lastSeasonStrength, lastBaseMultiplier,
                lastAdjustedMultiplier,
                totalAttempts.get(), totalAllowed.get()));

        lastLogMs = now;
    }

    public static String getGrowthStats() {
        return String.format(
                "Season: %s (strength %.2f), Base: %.3f, Adjusted: %.3f, Total Attempts: %d, Total Allowed: %d, Total Daylight Blocked: %d, Total Winter Strength Blocked: %d, Total Probability Rejected: %d",
                lastSeason, lastSeasonStrength, lastBaseMultiplier, lastAdjustedMultiplier,
                totalAttempts.get(), totalAllowed.get(), totalDaylightBlocked.get(), totalStrengthBlocked.get(),
                totalRandomRejected.get());
    }

    public static String currentContextSummary() {
        return String.format(
                "[HOR Debug] season=%s strength=%.2f base=%.3f adjusted=%.3f attempts=%d allowed=%d daylight_block=%d winter_strength_block=%d prob_reject=%d",
                lastSeason, lastSeasonStrength, lastBaseMultiplier, lastAdjustedMultiplier,
                totalAttempts.get(), totalAllowed.get(), totalDaylightBlocked.get(), totalStrengthBlocked.get(),
                totalRandomRejected.get());
    }

    public static void reset() {
        windowAttempts.set(0);
        windowAllowed.set(0);
        windowDaylightBlocked.set(0);
        windowStrengthBlocked.set(0);
        windowRandomRejected.set(0);
        totalAttempts.set(0);
        totalAllowed.set(0);
        totalDaylightBlocked.set(0);
        totalStrengthBlocked.set(0);
        totalRandomRejected.set(0);
        lastLogMs = System.currentTimeMillis();
        lastSeason = "UNKNOWN";
        lastSeasonStrength = -1f;
        lastBaseMultiplier = -1.0;
        lastAdjustedMultiplier = -1.0;
    }
}
