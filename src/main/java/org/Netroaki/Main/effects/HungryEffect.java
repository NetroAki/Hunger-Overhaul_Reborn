package org.Netroaki.Main.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.Netroaki.Main.util.VersionDetector;

import java.util.UUID;

public class HungryEffect extends MobEffect {
	private static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890");
	private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160891");
	private static final UUID ATTACK_SPEED_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160892");

	public HungryEffect() {
		super(MobEffectCategory.HARMFUL, 0x8B4513); // Brown color

		// Add attribute modifiers on both versions - they should work the same way
		// Attribute modifiers scale with (amplifier + 1)
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_UUID.toString(), -0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_UUID.toString(), -2.0D, AttributeModifier.Operation.ADDITION);
		this.addAttributeModifier(Attributes.ATTACK_SPEED, ATTACK_SPEED_UUID.toString(), -0.10D, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity.level().isClientSide()) return;

		// Apply mining fatigue at amplifier 2+ (very hungry/starvation level)
		if (amplifier >= 2) {
			entity.addEffect(new net.minecraft.world.effect.MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 0, false, false, false));
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		// Need to tick to apply mining fatigue
		return amplifier >= 2;
	}

	public String getDescriptionId() {
		return "effect.hunger_overhaul_reborn.hungry";
	}

	public boolean isAvailable() {
		return true; // Available on both 1.20.1 and 1.21.1
	}
}
