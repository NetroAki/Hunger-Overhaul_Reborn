package org.Netroaki.Main.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class HungryEffect extends MobEffect {
	private static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890");
	private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160891");
	private static final UUID ATTACK_SPEED_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160892");

	public HungryEffect() {
		super(MobEffectCategory.HARMFUL, 0x8B4513); // Brown color
		// Attribute modifiers scale with (amplifier + 1)
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_UUID.toString(), -0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_UUID.toString(), -2.0D, AttributeModifier.Operation.ADDITION);
		this.addAttributeModifier(Attributes.ATTACK_SPEED, ATTACK_SPEED_UUID.toString(), -0.10D, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		// No vanilla effects applied; behavior is driven by attribute modifiers and events
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return false;
	}

	public String getDescriptionId() {
		return "effect.hunger_overhaul_reborn.hungry";
	}
}
