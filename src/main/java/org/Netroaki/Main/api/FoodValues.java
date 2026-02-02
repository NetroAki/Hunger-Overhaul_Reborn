package org.Netroaki.Main.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * FoodValues is a utility class used to retrieve and hold food values.
 *
 * To get food values for any given food, use any of the static {@link #get}
 * methods.
 *
 * <pre>
 * {@code
 * FoodValues appleFoodValues = FoodValues.get(new ItemStack(Items.apple));
 * }
 * </pre>
 */
public class FoodValues {
    public final int hunger;
    public final float saturationModifier;

    public FoodValues(int hunger, float saturationModifier) {
        this.hunger = hunger;
        this.saturationModifier = saturationModifier;
    }

    public FoodValues(FoodValues other) {
        this(other.hunger, other.saturationModifier);
    }

    /**
     * @return The amount of saturation that the food values would provide, ignoring
     *         any limits.
     */
    public float getUnboundedSaturationIncrement() {
        return hunger * saturationModifier * 2f;
    }

    /**
     * @return The bounded amount of saturation that the food values would provide
     *         to this player,
     *         taking their max hunger level into account.
     */
    public float getSaturationIncrement(Player player) {
        // Note: In vanilla MC, max hunger is 20 (10 shanks)
        // AppleCore would access this via an accessor, but we'll use vanilla value
        return Math.min(20.0f, getUnboundedSaturationIncrement());
    }

    /**
     * Get unmodified food values for any given food.
     * Uses reflection to support both 1.20.1 (Legacy Methods) and 1.21.1
     * (DataComponents).
     */
    public static FoodValues getUnmodified(ItemStack itemStack) {
        // Try 1.21 Logic (DataComponents)
        try {
            Class<?> dataComponentsClass = Class.forName("net.minecraft.core.component.DataComponents");
            java.lang.reflect.Field foodField = dataComponentsClass.getField("FOOD");
            Object foodComponentType = foodField.get(null);
            Class<?> componentTypeClass = Class.forName("net.minecraft.core.component.DataComponentType");

            java.lang.reflect.Method getMethod = ItemStack.class.getMethod("get", componentTypeClass);
            Object foodProps = getMethod.invoke(itemStack, foodComponentType);

            if (foodProps != null) {
                java.lang.reflect.Method nutritionMethod = foodProps.getClass().getMethod("nutrition");
                java.lang.reflect.Method saturationMethod = foodProps.getClass().getMethod("saturation");

                int nutrition = (int) nutritionMethod.invoke(foodProps);
                float saturation = (float) saturationMethod.invoke(foodProps);
                return new FoodValues(nutrition, saturation);
            }
            return new FoodValues(0, 0f); // Found valid item but not food
        } catch (Exception e) {
            // Ignore (Not 1.21 or failed)
        }

        // Try 1.20 Logic (Legacy Methods)
        try {
            java.lang.reflect.Method isEdibleMethod = ItemStack.class.getMethod("isEdible");
            if ((boolean) isEdibleMethod.invoke(itemStack)) {
                java.lang.reflect.Method getItemMethod = ItemStack.class.getMethod("getItem");
                Object item = getItemMethod.invoke(itemStack);

                java.lang.reflect.Method getFoodPropertiesMethod = item.getClass().getMethod("getFoodProperties");
                Object foodProps = getFoodPropertiesMethod.invoke(item);

                if (foodProps != null) {
                    java.lang.reflect.Method getNutritionMethod = foodProps.getClass().getMethod("getNutrition");
                    java.lang.reflect.Method getSaturationModifierMethod = foodProps.getClass()
                            .getMethod("getSaturationModifier");

                    int nutrition = (int) getNutritionMethod.invoke(foodProps);
                    float saturation = (float) getSaturationModifierMethod.invoke(foodProps);
                    return new FoodValues(nutrition, saturation);
                }
            }
            return new FoodValues(0, 0f);
        } catch (Exception e) {
            // Failed both attempts
            return new FoodValues(0, 0f);
        }
    }

    /**
     * Get food values for any given food.
     */
    public static FoodValues get(ItemStack itemStack) {
        return getUnmodified(itemStack); // For now, no modification
    }

    /**
     * Get food values for any given food for a specific player.
     */
    public static FoodValues get(ItemStack itemStack, Player player) {
        return get(itemStack); // For now, no player-specific modifications
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + hunger;
        result = prime * result + Float.floatToIntBits(saturationModifier);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FoodValues other = (FoodValues) obj;
        if (hunger != other.hunger)
            return false;
        if (Float.floatToIntBits(saturationModifier) != Float.floatToIntBits(other.saturationModifier))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FoodValues{hunger=" + hunger + ", saturationModifier=" + saturationModifier + "}";
    }
}
