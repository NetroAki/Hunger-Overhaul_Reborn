package org.Netroaki.Main.effects;

import org.Netroaki.Main.HOReborn;

// 1.21.1-compatible WellFedEffect using minimal API
public class WellFedEffect {

    public WellFedEffect() {
        HOReborn.LOGGER.info("WellFedEffect initialized for 1.21.1");
    }

    public String getDescriptionId() {
        return "effect.hunger_overhaul_reborn.well_fed";
    }

    public boolean isAvailable() {
        return true; // Available on 1.21.1
    }

    // Minimal implementation - actual effect logic handled by reflection in handlers
}
