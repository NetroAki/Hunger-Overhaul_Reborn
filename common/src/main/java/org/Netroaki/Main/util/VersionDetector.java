package org.Netroaki.Main.util;

import org.Netroaki.Main.HOReborn;

/**
 * Utility class for detecting Minecraft version and providing version-specific functionality.
 * This allows the mod to work with multiple Minecraft versions using a single JAR.
 */
public class VersionDetector {
    private static final String MINECRAFT_VERSION = detectMinecraftVersion();
    private static final boolean IS_1_20_1 = MINECRAFT_VERSION.startsWith("1.20.1");
    private static final boolean IS_1_21_1 = MINECRAFT_VERSION.startsWith("1.21.1");
    
    /**
     * Detects the current Minecraft version at runtime.
     * @return The Minecraft version string (e.g., "1.20.1", "1.21.1")
     */
    private static String detectMinecraftVersion() {
        try {
            // Try to get version from SharedConstants (most reliable)
            Class<?> sharedConstantsClass = Class.forName("net.minecraft.SharedConstants");
            java.lang.reflect.Field versionField = sharedConstantsClass.getDeclaredField("VERSION_NAME");
            versionField.setAccessible(true);
            String version = (String) versionField.get(null);
            HOReborn.LOGGER.info("[VersionDetector] Detected Minecraft version: {}", version);
            return version;
        } catch (Exception e1) {
            try {
                // Fallback: try to get from Minecraft class
                Class<?> minecraftClass = Class.forName("net.minecraft.Minecraft");
                java.lang.reflect.Field versionField = minecraftClass.getDeclaredField("VERSION");
                versionField.setAccessible(true);
                String version = (String) versionField.get(null);
                HOReborn.LOGGER.info("[VersionDetector] Detected Minecraft version (fallback): {}", version);
                return version;
            } catch (Exception e2) {
                // Last resort: use system property or default
                String version = System.getProperty("minecraft.version", "unknown");
                HOReborn.LOGGER.warn("[VersionDetector] Could not detect Minecraft version, using: {}", version);
                return version;
            }
        }
    }
    
    /**
     * Gets the detected Minecraft version.
     * @return The Minecraft version string
     */
    public static String getMinecraftVersion() {
        return MINECRAFT_VERSION;
    }
    
    /**
     * Checks if the current version matches the specified version.
     * @param version The version to check (e.g., "1.20.1")
     * @return true if the current version matches
     */
    public static boolean isVersion(String version) {
        return MINECRAFT_VERSION.startsWith(version);
    }
    
    /**
     * Checks if the current version is 1.20.1.
     * @return true if running on 1.20.1
     */
    public static boolean is1_20_1() {
        return IS_1_20_1;
    }
    
    /**
     * Checks if the current version is 1.21.1.
     * @return true if running on 1.21.1
     */
    public static boolean is1_21_1() {
        return IS_1_21_1;
    }
    
    /**
     * Checks if the current version is supported.
     * @return true if the version is supported
     */
    public static boolean isSupportedVersion() {
        return IS_1_20_1 || IS_1_21_1;
    }
    
    /**
     * Gets a human-readable version string for logging.
     * @return A formatted version string
     */
    public static String getVersionString() {
        if (IS_1_20_1) return "1.20.1";
        if (IS_1_21_1) return "1.21.1";
        return MINECRAFT_VERSION;
    }
    
    /**
     * Throws an exception if the current version is not supported.
     * @throws UnsupportedOperationException if the version is not supported
     */
    public static void requireSupportedVersion() {
        if (!isSupportedVersion()) {
            throw new UnsupportedOperationException(
                "Unsupported Minecraft version: " + MINECRAFT_VERSION + 
                ". Supported versions: 1.20.1, 1.21.1"
            );
        }
    }
}
