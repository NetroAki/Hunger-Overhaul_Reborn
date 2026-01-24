package org.Netroaki.Main.handlers;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEventHandler {
    private static final int BASE_HUNGER_TICK_RATE = 24000;
    private static int hungerTickCounter = 0;
    private static final Map<UUID, Integer> wellFedRegenTicksRemaining = new HashMap<>();
    private static final Map<UUID, Integer> lastWellFedAmplifier = new HashMap<>();

    public static void register() {
        TickEvent.PLAYER_PRE.register(PlayerEventHandler::onPlayerTick);
        PlayerEvent.PLAYER_RESPAWN.register(PlayerEventHandler::onPlayerRespawn);
        PlayerEvent.PLAYER_JOIN.register(PlayerEventHandler::onPlayerJoin);
    }

    private static void onPlayerTick(Player player) {
        if (player.level().isClientSide())
            return;

        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        Level level = player.level();

        double hungerSpeed = 1.0;
        double healSpeed = 1.0;
        switch (level.getDifficulty()) {
            case PEACEFUL:
            case EASY:
                hungerSpeed = 0.75;
                healSpeed = 0.75;
                break;
            case NORMAL:
                hungerSpeed = 1.0;
                healSpeed = 1.0;
                break;
            case HARD:
                hungerSpeed = 1.5;
                healSpeed = 1.5;
                break;
        }
        if (config.hunger.harvestCraftHungerLossIncrease && Platform.isModLoaded("harvestcraft")) {
            hungerSpeed *= config.hunger.harvestCraftHungerLossMultiplier;
        }

        if (config.hunger.constantHungerLoss) {
            hungerTickCounter++;
            int interval = (int) Math.max(1, Math.round(BASE_HUNGER_TICK_RATE / hungerSpeed));
            if (hungerTickCounter >= interval) {
                hungerTickCounter = 0;
                FoodData foodData = player.getFoodData();
                if (foodData.getFoodLevel() > 0) {
                    foodData.setFoodLevel(foodData.getFoodLevel() - 1);
                }
            }
        }

        FoodData foodData = player.getFoodData();
        int foodLevel = foodData.getFoodLevel();

        if (config.hunger.lowHungerEffects) {
            int desiredAmplifier = -1;
            if (foodLevel <= 2)
                desiredAmplifier = 2;
            else if (foodLevel <= 4)
                desiredAmplifier = 1;
            else if (foodLevel <= 6)
                desiredAmplifier = 0;

            if (desiredAmplifier >= 0) {
                MobEffectInstance current = player.getEffect(HOReborn.HUNGRY_EFFECT.get());
                if (current == null || current.getAmplifier() != desiredAmplifier) {
                    player.removeEffect(HOReborn.HUNGRY_EFFECT.get());
                    player.addEffect(new MobEffectInstance(HOReborn.HUNGRY_EFFECT.get(), Integer.MAX_VALUE,
                            desiredAmplifier, false, true, true));
                }
            } else {
                player.removeEffect(HOReborn.HUNGRY_EFFECT.get());
            }
        }

        if (config.health.healthHealsAboveThreeShanks && player.getHealth() < player.getMaxHealth()) {
            int threshold = Math.max(0, config.health.healthHealingThreshold);
            if (foodLevel >= threshold) {
                long gameTime = level.getGameTime();
                int baseHealInterval = 120;
                int healInterval = (int) Math.max(1, Math.round(baseHealInterval / healSpeed));
                if (gameTime % healInterval == 0) {
                    player.heal(1.0F);
                }
            }
        }

        // One-second regeneration pulse when Well Fed is applied or upgraded
        var wellFed = HOReborn.WELL_FED_EFFECT.get();
        MobEffectInstance wf = player.getEffect(wellFed);
        UUID id = player.getUUID();
        if (wf != null) {
            int amp = wf.getAmplifier();
            Integer lastAmp = lastWellFedAmplifier.get(id);
            if (lastAmp == null || lastAmp != amp) {
                // Start 1-second pulse (20 ticks)
                wellFedRegenTicksRemaining.put(id, 20);
                lastWellFedAmplifier.put(id, amp);
            }
            Integer ticks = wellFedRegenTicksRemaining.get(id);
            if (ticks != null && ticks > 0) {
                // Heal every 10 ticks for a brief, noticeable regen-like effect
                if (ticks % 10 == 0) {
                    if (player.getHealth() < player.getMaxHealth()) {
                        player.heal(1.0F);
                    }
                }
                wellFedRegenTicksRemaining.put(id, ticks - 1);
            }
        } else {
            lastWellFedAmplifier.remove(id);
            wellFedRegenTicksRemaining.remove(id);
        }

        if (foodLevel <= 0 && config.hunger.instantDeathOnZeroHunger) {
            player.hurt(player.damageSources().starve(), Float.MAX_VALUE);
        }
    }

    private static void onPlayerRespawn(ServerPlayer player, boolean conqueredEnd) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        if (config.hunger.modifyRespawnHunger) {
            int respawnHunger = getRespawnHungerValue(player.level().getDifficulty());
            player.getFoodData().setFoodLevel(respawnHunger);
        }
    }

    private static void onPlayerJoin(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            onPlayerRespawn(serverPlayer, false);
        }
    }

    private static int getRespawnHungerValue(Difficulty difficulty) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        int baseHunger = config.hunger.respawnHungerValue;
        int modifier = config.hunger.respawnHungerDifficultyModifier;

        switch (difficulty) {
            case PEACEFUL:
            case EASY:
                return baseHunger;
            case NORMAL:
                return baseHunger - modifier;
            case HARD:
                return baseHunger - (modifier * 2);
            default:
                return baseHunger;
        }
    }
}
