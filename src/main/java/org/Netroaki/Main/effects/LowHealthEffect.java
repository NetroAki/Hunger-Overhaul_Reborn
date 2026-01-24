package org.Netroaki.Main.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.util.VersionDetector;

import java.util.UUID;

/**
 * Low health debuff effect. Only functional on 1.20.1.
 */
public class LowHealthEffect extends MobEffect {
    private static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635");
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("22653B89-1166-49DC-9B6B-9971489B5BE5");
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("55FCED67-E92A-486E-9800-B47AE2820395");

    public LowHealthEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B0000);

        // Add attribute modifiers on both versions - they should work the same way
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_UUID.toString(), -0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_UUID.toString(), -0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, ATTACK_SPEED_UUID.toString(), -0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) return;

        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // Check if player should have low health effects
        if (entity.getHealth() <= config.health.lowHealthEffectsThreshold) {
            // Apply nausea
            if (config.health.addLowHealthNausea) {
                entity.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.minecraft.world.effect.MobEffects.CONFUSION, 100, 0, false, true, true));
            }

            // Apply mining slowdown
            if (config.health.addLowHealthMiningSlowdown) {
                entity.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.minecraft.world.effect.MobEffects.DIG_SLOWDOWN, 40, 0, false, false, false));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true; // Apply every tick
    }

    public String getDescriptionId() {
        return "effect.hunger_overhaul_reborn.low_health";
    }

	public boolean isAvailable() {
		return true; // Available on both 1.20.1 and 1.21.1
	}
}

