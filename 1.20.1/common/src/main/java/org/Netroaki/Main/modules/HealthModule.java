package org.Netroaki.Main.modules;

import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

public class HealthModule {
    
    public void init() {
        HOReborn.LOGGER.info("Initializing Health Module");
        
        // Health-related features are handled in PlayerEventHandler
        // This module is mainly for organization and future expansion
        
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        
        if (config.health.healthHealsAboveThreeShanks) {
            HOReborn.LOGGER.info("Health healing above three shanks enabled");
        }
        
        if (config.health.lowHealthEffects) {
            HOReborn.LOGGER.info("Low health effects enabled");
        }
        
        if (config.health.difficultyScalingHealing) {
            HOReborn.LOGGER.info("Difficulty scaling healing enabled");
        }
    }
}
