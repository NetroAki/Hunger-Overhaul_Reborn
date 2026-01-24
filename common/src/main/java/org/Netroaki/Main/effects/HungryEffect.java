package org.Netroaki.Main.effects;

import org.Netroaki.Main.util.VersionDetector;

import java.util.UUID;

// Version-aware effect class
public class HungryEffect {
	private static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890");
	private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160891");
	private static final UUID ATTACK_SPEED_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160892");

	// Only create the actual effect on 1.20.1
	private Object effectInstance;

	public HungryEffect() {
		// For now, always set to null - effects are disabled for compatibility
		// This allows the code to compile but effects won't work
		effectInstance = null;
	}

	public Object getEffectInstance() {
		return effectInstance;
	}

	public boolean isAvailable() {
		return effectInstance != null;
	}

	public String getDescriptionId() {
		return "effect.hunger_overhaul_reborn.hungry";
	}
}
