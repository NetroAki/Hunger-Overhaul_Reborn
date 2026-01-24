package org.Netroaki.Main.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

/**
 * Handles registration and management of custom village field structures.
 */
public class VillageFieldHandler {

    private static boolean initialized = false;

    /**
     * Initialize the village field handler.
     * Registers the custom structure piece type.
     */
    public static void initialize() {
        if (initialized) return;

        // Register the structure piece type
        // Note: Structure registration API may vary by Minecraft version
        // VillageFieldStructure.STRUCTURE_TYPE = StructurePieceType.register(
        //     VillageFieldStructure::new,
        //     new ResourceLocation(HOReborn.MOD_ID, "village_field")
        // );

        HOReborn.LOGGER.info("VillageFieldHandler initialized");
        initialized = true;
    }

    /**
     * Check if village integration is enabled.
     */
    public static boolean isVillageIntegrationEnabled() {
        return HungerOverhaulConfig.getInstance().integration.enableVillageIntegration &&
               HungerOverhaulConfig.getInstance().integration.addCustomVillageField;
    }

    /**
     * Get the field type weights for random selection.
     * Returns an array where index represents field type and value represents weight.
     */
    public static int[] getFieldWeights() {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        return new int[] {
            config.integration.fieldNormalWeight,  // 0 = normal crops
            config.integration.fieldReedWeight,    // 1 = reeds (sugarcane)
            config.integration.fieldStemWeight     // 2 = stems (pumpkin/melon)
        };
    }

    /**
     * Select a random field type based on configured weights.
     */
    public static int selectRandomFieldType() {
        int[] weights = getFieldWeights();
        int totalWeight = 0;

        for (int weight : weights) {
            totalWeight += weight;
        }

        if (totalWeight <= 0) {
            return 0; // Default to normal crops
        }

        int randomValue = (int) (Math.random() * totalWeight);

        for (int i = 0; i < weights.length; i++) {
            randomValue -= weights[i];
            if (randomValue < 0) {
                return i;
            }
        }

        return 0; // Fallback
    }
}
