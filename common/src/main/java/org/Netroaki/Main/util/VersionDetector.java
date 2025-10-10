package org.Netroaki.Main.util;

import org.Netroaki.Main.HOReborn;

/**
 * Utility class for detecting Minecraft version and providing version-specific functionality.
 * This allows the mod to work with multiple Minecraft versions using a single JAR.
 */
public class VersionDetector {
    private static String MINECRAFT_VERSION = null;
    private static Boolean IS_1_20_1 = null;
    private static Boolean IS_1_21_1 = null;
    
    /**
     * Detects the current Minecraft version at runtime.
     * @return The Minecraft version string (e.g., "1.20.1", "1.21.1")
     */
    private static String detectMinecraftVersion() {
        if (MINECRAFT_VERSION != null) {
            return MINECRAFT_VERSION;
        }
        
        try {
            // Try to get version from SharedConstants (most reliable)
            Class<?> sharedConstantsClass = Class.forName("net.minecraft.SharedConstants");
            java.lang.reflect.Field versionField = sharedConstantsClass.getDeclaredField("VERSION_NAME");
            versionField.setAccessible(true);
            String version = (String) versionField.get(null);
            HOReborn.LOGGER.info("[VersionDetector] Detected Minecraft version: {}", version);
            MINECRAFT_VERSION = version;
            return version;
        } catch (Exception e1) {
            try {
                // Fallback: try to get from Minecraft class
                Class<?> minecraftClass = Class.forName("net.minecraft.Minecraft");
                java.lang.reflect.Field versionField = minecraftClass.getDeclaredField("VERSION");
                versionField.setAccessible(true);
                String version = (String) versionField.get(null);
                HOReborn.LOGGER.info("[VersionDetector] Detected Minecraft version (fallback): {}", version);
                MINECRAFT_VERSION = version;
                return version;
            } catch (Exception e2) {
                // Last resort: use system property or default
                String version = System.getProperty("minecraft.version", "1.20.1");
                HOReborn.LOGGER.warn("[VersionDetector] Could not detect Minecraft version, defaulting to: {}", version);
                MINECRAFT_VERSION = version;
                return version;
            }
        }
    }
    
    /**
     * Gets the detected Minecraft version.
     * @return The Minecraft version string
     */
    public static String getMinecraftVersion() {
        return detectMinecraftVersion();
    }
    
    /**
     * Checks if the current version matches the specified version.
     * @param version The version to check (e.g., "1.20.1")
     * @return true if the current version matches
     */
    public static boolean isVersion(String version) {
        return detectMinecraftVersion().startsWith(version);
    }
    
    /**
     * Checks if the current version is 1.20.1.
     * @return true if running on 1.20.1
     */
    public static boolean is1_20_1() {
        if (IS_1_20_1 == null) {
            IS_1_20_1 = detectMinecraftVersion().startsWith("1.20.1");
        }
        return IS_1_20_1;
    }
    
    /**
     * Checks if the current version is 1.21.1.
     * @return true if running on 1.21.1
     */
    public static boolean is1_21_1() {
        if (IS_1_21_1 == null) {
            IS_1_21_1 = detectMinecraftVersion().startsWith("1.21.1");
        }
        return IS_1_21_1;
    }
    
    /**
     * Checks if the current version is supported.
     * @return true if the version is supported
     */
    public static boolean isSupportedVersion() {
        return is1_20_1() || is1_21_1();
    }
    
    /**
     * Gets a human-readable version string for logging.
     * @return A formatted version string
     */
    public static String getVersionString() {
        if (is1_20_1()) return "1.20.1";
        if (is1_21_1()) return "1.21.1";
        return detectMinecraftVersion();
    }
    
    /**
     * Throws an exception if the current version is not supported.
     * @throws UnsupportedOperationException if the version is not supported
     */
    public static void requireSupportedVersion() {
        if (!isSupportedVersion()) {
            throw new UnsupportedOperationException(
                "Unsupported Minecraft version: " + detectMinecraftVersion() + 
                ". Supported versions: 1.20.1, 1.21.1"
            );
        }
    }
}
