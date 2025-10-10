package org.Netroaki.Main.modules;

import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

public class HarvestCraftModule {
    
    public void init() {
        HOReborn.LOGGER.info("Initializing HarvestCraft Module");
        
        if (!Platform.isModLoaded("harvestcraft")) {
            HOReborn.LOGGER.warn("HarvestCraft not found, skipping integration");
            return;
        }
        
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        
        if (config.integration.enableHarvestCraftIntegration) {
            HOReborn.LOGGER.info("HarvestCraft integration enabled");
            
            // Initialize HarvestCraft-specific features
            initVillageIntegration();
            initChestLootIntegration();
            initFoodModifications();
        }
    }
    
    private void initVillageIntegration() {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        
        if (config.integration.enableVillageIntegration) {
            HOReborn.LOGGER.info("HarvestCraft village integration enabled");
            // This would add HarvestCraft foods to villager trades
        }
    }
    
    private void initChestLootIntegration() {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        
        if (config.integration.enableChestLootIntegration) {
            HOReborn.LOGGER.info("HarvestCraft chest loot integration enabled");
            // This would add HarvestCraft foods to dungeon chests
        }
    }
    
    private void initFoodModifications() {
        HOReborn.LOGGER.info("HarvestCraft food modifications enabled");
        // HarvestCraft foods will be nerfed by the main food modification system
    }
}
