package org.Netroaki.Main.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Configuration for Hunger Overhaul Reborn
 */
public class HORConfig {
    private static final String CONFIG_FILE = "hunger_overhaul_reborn.properties";
    private static final String CONFIG_DIR = "config";

    // Default values
    private static final int DEFAULT_BOWL_STACK_SIZE = 16;
    private static final int DEFAULT_BOTTLE_STACK_SIZE = 8;
    private static final int DEFAULT_BUCKET_STACK_SIZE = 4;
    private static final int DEFAULT_CONTAINER_STACK_SIZE = 8;

    // Config values
    private int bowlStackSize = DEFAULT_BOWL_STACK_SIZE;
    private int bottleStackSize = DEFAULT_BOTTLE_STACK_SIZE;
    private int bucketStackSize = DEFAULT_BUCKET_STACK_SIZE;
    private int defaultContainerStackSize = DEFAULT_CONTAINER_STACK_SIZE;

    private static HORConfig INSTANCE;

    private HORConfig() {
        // Private constructor for singleton
    }

    public static HORConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HORConfig();
        }
        return INSTANCE;
    }

    /**
     * Load configuration from file, creating it with defaults if it doesn't exist
     */
    public void load(String gameDirectory) {
        Path configPath = Paths.get(gameDirectory, CONFIG_DIR, CONFIG_FILE);
        Properties props = new Properties();

        try {
            // Create config directory if it doesn't exist
            Files.createDirectories(configPath.getParent());

            if (Files.exists(configPath)) {
                // Load existing config
                try (InputStream input = Files.newInputStream(configPath)) {
                    props.load(input);
                    bowlStackSize = Integer.parseInt(
                            props.getProperty("containerFood.bowl.stackSize", String.valueOf(DEFAULT_BOWL_STACK_SIZE)));
                    bottleStackSize = Integer.parseInt(
                            props.getProperty("containerFood.bottle.stackSize",
                                    String.valueOf(DEFAULT_BOTTLE_STACK_SIZE)));
                    bucketStackSize = Integer.parseInt(
                            props.getProperty("containerFood.bucket.stackSize",
                                    String.valueOf(DEFAULT_BUCKET_STACK_SIZE)));
                    defaultContainerStackSize = Integer.parseInt(
                            props.getProperty("containerFood.default.stackSize",
                                    String.valueOf(DEFAULT_CONTAINER_STACK_SIZE)));
                    // System.out.println("[HOR Config] Loaded configuration: bowl=" + bowlStackSize
                    // +
                    // ", bottle=" + bottleStackSize + ", bucket=" + bucketStackSize +
                    // ", default=" + defaultContainerStackSize);
                }
            } else {
                // Create default config
                props.setProperty("containerFood.bowl.stackSize", String.valueOf(DEFAULT_BOWL_STACK_SIZE));
                props.setProperty("containerFood.bottle.stackSize", String.valueOf(DEFAULT_BOTTLE_STACK_SIZE));
                props.setProperty("containerFood.bucket.stackSize", String.valueOf(DEFAULT_BUCKET_STACK_SIZE));
                props.setProperty("containerFood.default.stackSize", String.valueOf(DEFAULT_CONTAINER_STACK_SIZE));
                try (OutputStream output = Files.newOutputStream(configPath)) {
                    props.store(output, "Hunger Overhaul Reborn Configuration\n" +
                            "Container Food Stack Sizes:\n" +
                            "  containerFood.bowl.stackSize: Stack size for foods with bowl containers (mushroom stew, rabbit stew, etc.) Default: 16\n"
                            +
                            "  containerFood.bottle.stackSize: Stack size for foods with bottle containers (honey bottles, potions, etc.) Default: 8\n"
                            +
                            "  containerFood.bucket.stackSize: Stack size for foods with bucket containers (milk buckets, etc.) Default: 4\n"
                            +
                            "  containerFood.default.stackSize: Stack size for other container foods Default: 8\n" +
                            "Valid range: 1-64");
                }
                bowlStackSize = DEFAULT_BOWL_STACK_SIZE;
                bottleStackSize = DEFAULT_BOTTLE_STACK_SIZE;
                bucketStackSize = DEFAULT_BUCKET_STACK_SIZE;
                defaultContainerStackSize = DEFAULT_CONTAINER_STACK_SIZE;
                // System.out.println("[HOR Config] Created default configuration file at " +
                // configPath);
            }

            // Clamp values to valid range
            bowlStackSize = Math.max(1, Math.min(64, bowlStackSize));
            bottleStackSize = Math.max(1, Math.min(64, bottleStackSize));
            bucketStackSize = Math.max(1, Math.min(64, bucketStackSize));
            defaultContainerStackSize = Math.max(1, Math.min(64, defaultContainerStackSize));

        } catch (IOException e) {
            // System.err.println("[HOR Config] Failed to load configuration, using
            // defaults");
            // e.printStackTrace();
            bowlStackSize = DEFAULT_BOWL_STACK_SIZE;
            bottleStackSize = DEFAULT_BOTTLE_STACK_SIZE;
            bucketStackSize = DEFAULT_BUCKET_STACK_SIZE;
            defaultContainerStackSize = DEFAULT_CONTAINER_STACK_SIZE;
        } catch (NumberFormatException e) {
            // System.err.println("[HOR Config] Invalid number format in config, using
            // default");
            // e.printStackTrace();
            bowlStackSize = DEFAULT_BOWL_STACK_SIZE;
            bottleStackSize = DEFAULT_BOTTLE_STACK_SIZE;
            bucketStackSize = DEFAULT_BUCKET_STACK_SIZE;
            defaultContainerStackSize = DEFAULT_CONTAINER_STACK_SIZE;
        }
    }

    public int getBowlStackSize() {
        return bowlStackSize;
    }

    public int getBottleStackSize() {
        return bottleStackSize;
    }

    public int getBucketStackSize() {
        return bucketStackSize;
    }

    public int getDefaultContainerStackSize() {
        return defaultContainerStackSize;
    }

    /**
     * Get the appropriate stack size for a given container item
     */
    public int getStackSizeForContainer(net.minecraft.world.item.Item containerItem) {
        if (containerItem == null) {
            return defaultContainerStackSize;
        }

        String itemId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(containerItem).toString();

        // Check for bowls
        if (itemId.contains("bowl")) {
            return bowlStackSize;
        }
        // Check for bottles
        if (itemId.contains("bottle") || itemId.contains("potion")) {
            return bottleStackSize;
        }
        // Check for buckets
        if (itemId.contains("bucket")) {
            return bucketStackSize;
        }

        return defaultContainerStackSize;
    }
}
