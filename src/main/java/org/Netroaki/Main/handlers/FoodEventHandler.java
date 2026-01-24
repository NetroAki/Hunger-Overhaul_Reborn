package org.Netroaki.Main.handlers;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.applecore.FoodEvent;
import org.Netroaki.Main.applecore.FoodValues;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;
import org.Netroaki.Main.food.FoodValueLoader;
import org.Netroaki.Main.util.FoodUtil;
import org.Netroaki.Main.util.FoodCategorizer;

import java.util.HashMap;
import java.util.Map;

public class FoodEventHandler {
    private static final Map<Item, FoodProperties> ORIGINAL_FOOD_PROPERTIES = new HashMap<>();
    private static final Map<Item, Integer> ORIGINAL_MAX_STACK_SIZES = new HashMap<>();

    public static void register() {
        LifecycleEvent.SERVER_LEVEL_LOAD.register(FoodEventHandler::onServerStarting);
    }

    private static void onServerStarting(Level level) {
        // Load food values from JSON (auto-generates if needed)
        FoodValueLoader.loadFoodValues();

        if (HungerOverhaulConfig.getInstance().food.modifyFoodStats) {
            modifyFoodProperties();
        }
    }

    public static void onFoodConsumed(Player player, ItemStack stack) {
        if (HungerOverhaulConfig.getInstance().food.enableWellFedEffect && stack.isEdible()) {
            FoodProperties food = stack.getItem().getFoodProperties();
            if (food != null) {
                // Note: AppleCore FoodEaten event would be fired here in full implementation

                // Apply food regenerates health feature
                HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
                if (config.health.foodRegensHealth) {
                    float healthToRestore = food.getNutrition() / (float) config.health.foodHealDivider;
                    if (healthToRestore > 0) {
                        player.heal(healthToRestore);
                    }
                }

                int foodValue = food.getNutrition();
                int duration = getWellFedDuration(foodValue);

                if (duration > 0) {
                    MobEffectInstance existingEffect = player.getEffect(HOReborn.WELL_FED_EFFECT.get());
                    int newDuration = existingEffect != null ? existingEffect.getDuration() + duration : duration;
                    int newAmplifier = Math.min(existingEffect != null ? existingEffect.getAmplifier() + 1 : 0, 3);

                    player.addEffect(new MobEffectInstance(HOReborn.WELL_FED_EFFECT.get(), newDuration, newAmplifier));
                }
            }
        }
    }

    public static void onItemUse(Player player, ItemStack stack) {
        if (HungerOverhaulConfig.getInstance().food.modifyEatingSpeed && stack.isEdible()) {
            FoodProperties food = stack.getItem().getFoodProperties();
            if (food != null) {
                int foodValue = food.getNutrition();
                int useDuration = getEatingDuration(foodValue);
            }
        }
    }

    private static void modifyFoodProperties() {
        // Store original properties for rollback if needed
        for (Item item : FoodUtil.getAllFoodItems()) {
            if (item.getFoodProperties() != null) {
                ORIGINAL_FOOD_PROPERTIES.put(item, item.getFoodProperties());
                ORIGINAL_MAX_STACK_SIZES.put(item, item.getMaxStackSize());
            }
        }

        // Apply stack size modifications (still done directly since not part of AppleCore)
        if (HungerOverhaulConfig.getInstance().food.modifyStackSizes) {
            for (Item item : FoodUtil.getAllFoodItems()) {
                FoodProperties food = item.getFoodProperties();
                if (food != null) {
                    int newStackSize = getStackSizeForFoodValue(food.getNutrition());
                    FoodUtil.setMaxStackSize(item, newStackSize);
                }
            }
        }
    }

    /**
     * Get modified food values for an item (used by JSON system).
     */
    public static FoodValues getModifiedFoodValues(ItemStack stack) {
        if (!HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            return FoodValues.getUnmodified(stack);
        }

        Item item = stack.getItem();

        // Check if we have JSON override first
        FoodValues jsonOverride = FoodRegistry.getFoodValues(stack);
        if (jsonOverride != null) {
            return jsonOverride;
        }

        // Fall back to modified properties
        FoodProperties original = ORIGINAL_FOOD_PROPERTIES.get(item);
        if (original != null) {
            FoodProperties modified = createModifiedFoodProperties(item, original);
            return new FoodValues(modified.getNutrition(), modified.getSaturationModifier());
        }

        return FoodValues.getUnmodified(stack);
    }

    private static FoodProperties createModifiedFoodProperties(Item item, FoodProperties original) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        float saturation = original.getSaturationModifier();
        
        // Get item name for categorization
        ResourceLocation itemId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
        String itemName = itemId != null ? itemId.getPath() : "unknown";
        
        // Auto-categorize based on item name
        FoodCategorizer.FoodValue foodValue = FoodCategorizer.categorizeFood(itemName);
        int nutrition = foodValue.hunger;

        // Apply original Hunger Overhaul calculation logic
        if (config.food.modifyFoodValues) {
            // First apply the hunger divider
            nutrition = Math.max(1, (int) Math.floor(nutrition / config.food.foodHungerDivider));

            // Then apply saturation calculation
            if (config.food.foodHungerToSaturationDivider > 0) {
                // Original logic: saturation = modified_hunger / divider
                saturation = nutrition / (float) config.food.foodHungerToSaturationDivider;
            }

            // Finally apply saturation divider
            saturation /= config.food.foodSaturationDivider;
        }

        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationMod(saturation);

        if (original.isMeat())
            builder.meat();
        if (original.canAlwaysEat())
            builder.alwaysEat();
        if (original.isFastFood())
            builder.fast();

        for (var pair : original.getEffects()) {
            MobEffectInstance inst = pair.getFirst();
            float chance = pair.getSecond();
            if (inst != null && chance > 0f) {
                MobEffectInstance copy = new MobEffectInstance(inst);
                builder.effect(copy, chance);
            }
        }

        if (config.food.enableWellFedEffect) {
            int wfDuration = getWellFedDuration(nutrition);
            if (wfDuration > 0) {
                builder.effect(new MobEffectInstance(HOReborn.WELL_FED_EFFECT.get(), wfDuration, 0), 1.0f);
            }
        }

        return builder.build();
    }

    private static int getWellFedDuration(int foodValue) {
        // Thresholds match our meal tier system: 8 (LARGE_MEAL), 6 (AVERAGE_MEAL), 4 (LIGHT_MEAL), 2 (COOKED_FOOD)
        if (foodValue >= 8)
            return 480;  // LARGE_MEAL: 24 seconds
        if (foodValue >= 6)
            return 240;  // AVERAGE_MEAL: 12 seconds
        if (foodValue >= 4)
            return 120;  // LIGHT_MEAL: 6 seconds
        if (foodValue >= 2)
            return 40;   // COOKED_FOOD: 2 seconds
        return 0;        // RAW_FOOD: no well-fed effect
    }

    public static int getEatingDuration(int foodValue) {
        // Dramatic scaling: the more complex/filling the meal, the longer it takes.
        // Thresholds still match meal tiers (8, 6, 4, 2), but durations have larger gaps.
        if (foodValue >= 8)
            return 64;  // LARGE_MEAL: 3.2 seconds (very slow, feast-tier)
        if (foodValue >= 6)
            return 48;  // AVERAGE_MEAL: 2.4 seconds
        if (foodValue >= 4)
            return 32;  // LIGHT_MEAL: 1.6 seconds
        if (foodValue >= 2)
            return 20;  // COOKED_FOOD: 1.0 second
        return 10;      // RAW_FOOD: 0.5 seconds (snack-tier, instant bite)
    }

    private static int getStackSizeForFoodValue(int foodValue) {
        // Thresholds match our meal tier system: higher value = lower stack size (more filling)
        if (foodValue >= 8)
            return 1;   // LARGE_MEAL: very filling, stack size 1
        if (foodValue >= 6)
            return 4;   // AVERAGE_MEAL: filling, stack size 4
        if (foodValue >= 4)
            return 16;  // LIGHT_MEAL: moderate, stack size 16
        if (foodValue >= 2)
            return 32;  // COOKED_FOOD: less filling, stack size 32
        return 32;      // RAW_FOOD: stack size 32 (same as cooked, not filling)
    }

    public static String getFoodDescription(int foodValue, float saturation) {
        // Determine category based on hunger value
        if (foodValue >= 8) return "LARGE_MEAL";
        if (foodValue >= 6) return "AVERAGE_MEAL";
        if (foodValue >= 4) return "LIGHT_MEAL";
        if (foodValue >= 2) return "COOKED_FOOD";
        return "RAW_FOOD";
    }
}
