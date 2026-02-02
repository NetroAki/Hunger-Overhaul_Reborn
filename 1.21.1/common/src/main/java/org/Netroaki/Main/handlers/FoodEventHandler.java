package org.Netroaki.Main.handlers;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.applecore.FoodValues;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;
import org.Netroaki.Main.food.FoodValueLoader;
import org.Netroaki.Main.util.FoodCategorizer;
import org.Netroaki.Main.util.FoodUtil;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;

public class FoodEventHandler {
    private static final Map<Item, FoodProperties> ORIGINAL_FOOD_PROPERTIES = new HashMap<>();
    private static final Map<Item, Integer> ORIGINAL_MAX_STACK_SIZES = new HashMap<>();

    public static void register() {
        LifecycleEvent.SERVER_LEVEL_LOAD.register(FoodEventHandler::onServerStarting);

        // Register eating event to handle Well Fed and Health Regen
    }

    private static void onServerStarting(Level level) {
        // Load food values from JSON
        FoodValueLoader.loadFoodValues();

        if (HungerOverhaulConfig.getInstance().food.modifyFoodStats) {
            modifyFoodProperties();
        }
    }

    public static void onFoodConsumed(Player player, ItemStack stack) {
        if (HungerOverhaulConfig.getInstance().food.enableWellFedEffect && stack.has(DataComponents.FOOD)) {

            // Use modified values for effects logic to respect config
            FoodValues modifiedValues = getModifiedFoodValues(stack);

            if (modifiedValues != null) {
                // Apply food regenerates health feature
                HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
                if (config.health.foodRegensHealth) {
                    float healthToRestore = modifiedValues.hunger / (float) config.health.foodHealDivider;
                    if (healthToRestore > 0) {
                        player.heal(healthToRestore);
                    }
                }

                int foodValue = modifiedValues.hunger;
                int duration = getWellFedDuration(foodValue);

                if (duration > 0 && HOReborn.WELL_FED_EFFECT != null) {
                    // Check if initialized safely for 1.21.1
                    var effectHolder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(HOReborn.WELL_FED_EFFECT.get());
                    MobEffectInstance existingEffect = player.getEffect(effectHolder);
                    int newDuration = existingEffect != null ? existingEffect.getDuration() + duration : duration;
                    int newAmplifier = Math.min(existingEffect != null ? existingEffect.getAmplifier() + 1 : 0, 3);

                    player.addEffect(new MobEffectInstance(effectHolder, newDuration, newAmplifier));
                }
            }
        }
    }

    public static void onItemUse(Player player, ItemStack stack) {
        // Implement if eating speed modification becomes possible without Mixins
    }

    private static void modifyFoodProperties() {
        // Currently a no-op in 1.21.1 implementation of FoodUtil
        // setup only for future compatibility
        for (Item item : FoodUtil.getAllFoodItems()) {
            if (item.components().has(DataComponents.FOOD)) {
                ORIGINAL_FOOD_PROPERTIES.put(item, item.components().get(DataComponents.FOOD));
                ORIGINAL_MAX_STACK_SIZES.put(item, item.components().getOrDefault(DataComponents.MAX_STACK_SIZE, 64));
            }
        }
    }

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

        // Fall back to modified properties calculation
        // Even if we didn't change the item, we can calculate what it SHOULD be
        FoodProperties original = item.components().get(DataComponents.FOOD); // Use current as base if original map
                                                                              // empty
        if (ORIGINAL_FOOD_PROPERTIES.containsKey(item)) {
            original = ORIGINAL_FOOD_PROPERTIES.get(item);
        }

        if (original != null) {
            return calculateModifiedValues(item, original);
        }

        return FoodValues.getUnmodified(stack);
    }

    private static FoodValues calculateModifiedValues(Item item, FoodProperties original) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // Get item name for categorization
        ResourceLocation itemId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
        String itemName = itemId != null ? itemId.getPath() : "unknown";

        // Auto-categorize based on item name
        FoodCategorizer.FoodValue foodValue = FoodCategorizer.categorizeFood(itemName);
        int nutrition = foodValue.hunger;
        float saturation = original.saturation();

        // Apply original Hunger Overhaul calculation logic
        if (config.food.modifyFoodValues) {
            nutrition = Math.max(1, (int) Math.floor(nutrition / config.food.foodHungerDivider));
            if (config.food.foodHungerToSaturationDivider > 0) {
                saturation = nutrition / (float) config.food.foodHungerToSaturationDivider;
            }
            saturation /= config.food.foodSaturationDivider;
        }

        return new FoodValues(nutrition, saturation);
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
        if (foodValue >= 8)
            return "LARGE_MEAL";
        if (foodValue >= 6)
            return "AVERAGE_MEAL";
        if (foodValue >= 4)
            return "LIGHT_MEAL";
        if (foodValue >= 2)
            return "COOKED_FOOD";
        return "RAW_FOOD";
    }
}