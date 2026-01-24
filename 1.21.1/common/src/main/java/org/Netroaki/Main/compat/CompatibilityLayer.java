package org.Netroaki.Main.compat;
import org.Netroaki.Main.util.VersionDetector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompatibilityLayer {
    private static final Logger LOGGER = LogManager.getLogger("hunger_overhaul_reborn");
    
    public static void initialize() {
        LOGGER.info("[CompatibilityLayer] Initializing for Minecraft {}", VersionDetector.getVersionString());
        
        if (VersionDetector.is1_20_1()) {
            initialize1_20_1();
        } else if (VersionDetector.is1_21_1()) {
            initialize1_21_1();
        } else {
            LOGGER.warn("[CompatibilityLayer] Version detection failed, defaulting to 1.21.1 behavior");
            initialize1_21_1();
        }
        
        LOGGER.info("[CompatibilityLayer] Initialization complete");
    }
    
    private static void initialize1_20_1() {
        LOGGER.info("[CompatibilityLayer] Using 1.20.1 specific implementations");
    }
    
    private static void initialize1_21_1() {
        LOGGER.info("[CompatibilityLayer] Using 1.21.1 specific implementations");
    }
    
    public static void init() {
        initialize();
    }
}
