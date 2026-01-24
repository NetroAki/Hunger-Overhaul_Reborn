package org.Netroaki.Main.food;

import com.google.gson.annotations.SerializedName;

/**
 * Data class for food values in JSON format.
 */
public class FoodValueData {
    @SerializedName("hunger")
    public int hunger;

    @SerializedName("saturation")
    public float saturation;

    @SerializedName("category")
    public String category;

    public FoodValueData() {
    }

    public FoodValueData(int hunger, float saturation, String category) {
        this.hunger = hunger;
        this.saturation = saturation;
        this.category = category;
    }

    @Override
    public String toString() {
        return "FoodValueData{hunger=" + hunger + ", saturation=" + saturation + ", category='" + category + "'}";
    }
}
