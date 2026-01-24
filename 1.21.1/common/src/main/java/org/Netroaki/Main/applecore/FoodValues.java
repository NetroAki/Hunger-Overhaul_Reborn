package org.Netroaki.Main.applecore;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * FoodValues is a utility class used to retrieve and hold food values.
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

    public float getUnboundedSaturationIncrement() {
        return hunger * saturationModifier * 2f;
    }

    public float getSaturationIncrement(Player player) {
        return Math.min(20.0f, getUnboundedSaturationIncrement());
    }

    public static FoodValues getUnmodified(ItemStack itemStack) {
        if (!itemStack.isEdible()) {
            return new FoodValues(0, 0f);
        }

        var foodProperties = itemStack.getItem().getFoodProperties();
        if (foodProperties == null) {
            return new FoodValues(0, 0f);
        }

        return new FoodValues(foodProperties.getNutrition(), foodProperties.getSaturationModifier());
    }

    public static FoodValues get(ItemStack itemStack) {
        return getUnmodified(itemStack);
    }

    public static FoodValues get(ItemStack itemStack, Player player) {
        return get(itemStack);
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
