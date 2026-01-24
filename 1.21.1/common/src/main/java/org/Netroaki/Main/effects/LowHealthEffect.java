package org.Netroaki.Main.effects;

import org.Netroaki.Main.HOReborn;

// 1.21.1-compatible LowHealthEffect using minimal API
public class LowHealthEffect {

    public LowHealthEffect() {
        HOReborn.LOGGER.info("LowHealthEffect initialized for 1.21.1");
    }

    public String getDescriptionId() {
        return "effect.hunger_overhaul_reborn.low_health";
    }

    public boolean isAvailable() {
        return true; // Available on 1.21.1
    }

    // Minimal implementation - actual effect logic handled by reflection in handlers
}
