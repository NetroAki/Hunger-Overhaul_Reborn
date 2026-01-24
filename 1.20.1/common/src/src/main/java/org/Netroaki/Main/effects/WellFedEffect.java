package org.Netroaki.Main.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class WellFedEffect extends MobEffect {
    private static final UUID HEALTH_REGEN_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890");

    public WellFedEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);
    }

    public String getDescriptionId() {
        return "effect.hunger_overhaul_reborn.well_fed";
    }

    public ItemStack getIcon() {
        return new ItemStack(Items.BREAD);
    }
}
