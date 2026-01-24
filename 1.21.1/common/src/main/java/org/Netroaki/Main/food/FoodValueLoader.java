package org.Netroaki.Main.food;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles loading and saving food values from/to JSON.
 */
public class FoodValueLoader {
    private static final String FOOD_VALUES_FILE = "food_values.json";
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void loadFoodValues() {
        try {
            Path configDir = Platform.getConfigFolder();
            Path foodValuesPath = configDir.resolve("HungerOverhaul").resolve(FOOD_VALUES_FILE);

            Files.createDirectories(foodValuesPath.getParent());

            Map<String, FoodValueData> loadedValues = new HashMap<>();

            if (Files.exists(foodValuesPath)) {
                try (Reader reader = Files.newBufferedReader(foodValuesPath)) {
                    java.lang.reflect.Type mapType = new com.google.gson.reflect.TypeToken<Map<String, FoodValueData>>() {
                    }.getType();
                    Map<String, FoodValueData> fromJson = GSON.fromJson(reader, mapType);
                    if (fromJson != null) {
                        loadedValues.putAll(fromJson);
                    }
                } catch (JsonSyntaxException e) {
                    HOReborn.LOGGER.error("Failed to parse food_values.json, using defaults", e);
                }
            }

            for (Map.Entry<String, FoodValueData> entry : loadedValues.entrySet()) {
                FoodRegistry.setFoodValues(entry.getKey(), entry.getValue());
            }

            FoodRegistry.initialize();
            saveFoodValues();

            HOReborn.LOGGER.info("Food values loaded from {}", foodValuesPath);

        } catch (IOException e) {
            HOReborn.LOGGER.error("Failed to load food values", e);
            FoodRegistry.initialize();
        }
    }

    public static void saveFoodValues() {
        try {
            Path configDir = Platform.getConfigFolder();
            Path foodValuesPath = configDir.resolve("HungerOverhaul").resolve(FOOD_VALUES_FILE);

            Files.createDirectories(foodValuesPath.getParent());

            Map<String, FoodValueData> allValues = FoodRegistry.getAllFoodValues();

            try (Writer writer = Files.newBufferedWriter(foodValuesPath)) {
                GSON.toJson(allValues, writer);
            }

            HOReborn.LOGGER.info("Food values saved to {} ({} entries)", foodValuesPath, allValues.size());

        } catch (IOException e) {
            HOReborn.LOGGER.error("Failed to save food values", e);
        }
    }

    public static void reloadFoodValues() {
        HOReborn.LOGGER.info("Reloading food values...");
        FoodRegistry.clear();
        loadFoodValues();
    }

    public static Path getFoodValuesPath() {
        return Platform.getConfigFolder().resolve("HungerOverhaul").resolve(FOOD_VALUES_FILE);
    }

    public static boolean foodValuesFileExists() {
        return Files.exists(getFoodValuesPath());
    }
}
