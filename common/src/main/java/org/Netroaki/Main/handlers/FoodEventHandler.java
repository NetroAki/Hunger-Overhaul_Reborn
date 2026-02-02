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
import org.Netroaki.Main.config.HungerOverhaulConfig;
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
        if (HungerOverhaulConfig.getInstance().food.modifyFoodStats) {
            modifyFoodProperties();
        }
    }

    public static void onFoodConsumed(Player player, ItemStack stack) {
        if (HungerOverhaulConfig.getInstance().food.enableWellFedEffect && stack.isEdible()) {
            FoodProperties food = stack.getItem().getFoodProperties();
            if (food != null) {
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
        for (Item item : FoodUtil.getAllFoodItems()) {
            if (item.getFoodProperties() != null) {
                ORIGINAL_FOOD_PROPERTIES.put(item, item.getFoodProperties());
                ORIGINAL_MAX_STACK_SIZES.put(item, item.getMaxStackSize());
            }
        }

        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            for (Item item : FoodUtil.getAllFoodItems()) {
                FoodProperties original = ORIGINAL_FOOD_PROPERTIES.get(item);
                if (original != null) {
                    FoodProperties modified = createModifiedFoodProperties(item, original);
                    FoodUtil.setFoodProperties(item, modified);
                }
            }
        }

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

    private static FoodProperties createModifiedFoodProperties(Item item, FoodProperties original) {
        float saturation = original.getSaturationModifier();
        int nutrition = FoodCategorizer.categorizeFood(item, original);

        if (FoodCategorizer.isModFood(item)) {
            nutrition = (int) Math.max(1,
                    Math.floor(nutrition / (double) HungerOverhaulConfig.getInstance().food.modFoodValueDivider));
            saturation = saturation / (float) HungerOverhaulConfig.getInstance().food.modFoodValueDivider;
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

        if (HungerOverhaulConfig.getInstance().food.enableWellFedEffect) {
            int wfDuration = getWellFedDuration(nutrition);
            if (wfDuration > 0) {
                builder.effect(new MobEffectInstance(HOReborn.WELL_FED_EFFECT.get(), wfDuration, 0), 1.0f);
            }
        }

        return builder.build();
    }

    private static int getWellFedDuration(int foodValue) {
        if (foodValue >= 8)
            return 480;
        if (foodValue >= 6)
            return 240;
        if (foodValue >= 4)
            return 120;
        if (foodValue >= 2)
            return 40;
        return 0;
    }

    public static int getEatingDuration(int foodValue) {
        if (foodValue >= 8)
            return 64;
        if (foodValue >= 6)
            return 48;
        if (foodValue >= 4)
            return 32;
        if (foodValue >= 2)
            return 20;
        return 10;
    }

    private static int getStackSizeForFoodValue(int foodValue) {
        HungerOverhaulConfig.FoodSettings foodConfig = HungerOverhaulConfig.getInstance().food;

        if (foodValue >= 8)
            return foodConfig.stackSizeLargeMeal;
        if (foodValue >= 6)
            return foodConfig.stackSizeAverageMeal;
        if (foodValue >= 4)
            return foodConfig.stackSizeLightMeal;
        if (foodValue >= 2)
            return foodConfig.stackSizeCookedMeal;
        return foodConfig.stackSizeRawMeal;
    }

    public static String getFoodDescription(int foodValue, float saturation) {
        return FoodCategorizer.getFoodCategoryDescription(foodValue);
    }
}
