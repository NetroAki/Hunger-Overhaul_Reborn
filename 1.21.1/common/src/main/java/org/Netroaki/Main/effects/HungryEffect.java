package org.Netroaki.Main.effects;

import org.Netroaki.Main.HOReborn;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class HungryEffect extends MobEffect {

    public HungryEffect() {
        super(MobEffectCategory.HARMFUL, 0x884400); // Brown
        HOReborn.LOGGER.info("HungryEffect initialized for 1.21.1");
    }

    public boolean isAvailable() {
        return true;
    }
}
