package org.Netroaki.Main.effects;

import org.Netroaki.Main.HOReborn;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class LowHealthEffect extends MobEffect {

    public LowHealthEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF0000); // Red
        HOReborn.LOGGER.info("LowHealthEffect initialized for 1.21.1");
    }

    public boolean isAvailable() {
        return true;
    }
}
