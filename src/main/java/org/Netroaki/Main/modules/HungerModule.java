package org.Netroaki.Main.modules;

import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

public class HungerModule {
    
    public void init() {
        HOReborn.LOGGER.info("Initializing Hunger Module");
        
        // Hunger-related features are handled in PlayerEventHandler
        // This module is mainly for organization and future expansion
        
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        
        if (config.hunger.constantHungerLoss) {
            HOReborn.LOGGER.info("Constant hunger loss enabled");
        }
        
        if (config.hunger.instantDeathOnZeroHunger) {
            HOReborn.LOGGER.info("Instant death on zero hunger enabled");
        }
        
        if (config.hunger.lowHungerEffects) {
            HOReborn.LOGGER.info("Low hunger effects enabled");
        }
        
        if (config.hunger.modifyRespawnHunger) {
            HOReborn.LOGGER.info("Respawn hunger modification enabled");
        }
    }
}
