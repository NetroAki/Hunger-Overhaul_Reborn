package org.Netroaki.Main.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import org.Netroaki.Main.util.VersionDetector;

import java.util.UUID;

public class WellFedEffect extends MobEffect {
    private static final UUID HEALTH_REGEN_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890");

    public WellFedEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) return;

        // Heal the player continuously while Well Fed
        if (entity.getHealth() < entity.getMaxHealth()) {
            float healAmount = 1.0f + (amplifier * 0.5f);
            entity.heal(healAmount);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true; // Apply every tick for continuous healing
    }

    public String getDescriptionId() {
        return "effect.hunger_overhaul_reborn.well_fed";
    }

    public ItemStack getIcon() {
        return new ItemStack(Items.BREAD);
    }

	public boolean isAvailable() {
		return true; // Available on both 1.20.1 and 1.21.1
	}
}
