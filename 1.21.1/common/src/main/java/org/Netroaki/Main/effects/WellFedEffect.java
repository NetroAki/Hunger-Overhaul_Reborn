package org.Netroaki.Main.effects;

import org.Netroaki.Main.HOReborn;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class WellFedEffect extends MobEffect {

    public WellFedEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xDAA520); // Golden
        HOReborn.LOGGER.info("WellFedEffect initialized for 1.21.1");
    }

    public boolean isAvailable() {
        return true;
    }
}
