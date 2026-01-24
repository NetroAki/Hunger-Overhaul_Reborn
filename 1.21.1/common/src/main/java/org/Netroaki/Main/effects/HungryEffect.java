package org.Netroaki.Main.effects;

import org.Netroaki.Main.HOReborn;

// 1.21.1-compatible HungryEffect using minimal API
public class HungryEffect {

    public HungryEffect() {
        HOReborn.LOGGER.info("HungryEffect initialized for 1.21.1");
    }

    public String getDescriptionId() {
        return "effect.hunger_overhaul_reborn.hungry";
    }

    public boolean isAvailable() {
        return true; // Available on 1.21.1
    }

    // Minimal implementation - actual effect logic handled by reflection in handlers
}
