package org.Netroaki.Main.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

/**
 * Handles generation of custom village fields near existing villages.
 * This is a post-generation hook that adds fields after villages are created.
 */
public class VillageFieldGenerator {

    /**
     * Called after a structure is generated to potentially add custom village fields.
     */
    public static void onStructureGenerated(ServerLevel level, Structure structure,
                                          ChunkPos chunkPos, BoundingBox boundingBox) {
        if (!VillageFieldHandler.isVillageIntegrationEnabled()) {
            return;
        }

        // Check if this is a village structure
        if (!isVillageStructure(structure)) {
            return;
        }

        // Add custom fields near the village
        addCustomFieldsNearVillage(level, boundingBox, level.getRandom());
    }

    /**
     * Check if the structure is a village type.
     */
    private static boolean isVillageStructure(Structure structure) {
        String structureName = structure.toString().toLowerCase();
        return structureName.contains("village") || structureName.contains("plains") ||
               structureName.contains("desert") || structureName.contains("savanna") ||
               structureName.contains("snowy") || structureName.contains("taiga");
    }

    /**
     * Add custom fields near an existing village.
     */
    private static void addCustomFieldsNearVillage(ServerLevel level, BoundingBox villageBox, RandomSource random) {
        // Generate 1-3 custom fields near the village
        int numFields = random.nextInt(3) + 1;

        for (int i = 0; i < numFields; i++) {
            // Find a suitable location near the village
            BlockPos fieldCenter = findSuitableFieldLocation(level, villageBox, random);
            if (fieldCenter != null) {
                // Select field type based on weights
                int fieldType = VillageFieldHandler.selectRandomFieldType();

                // Generate the field
                generateFieldAt(level, fieldCenter, fieldType, random);
            }
        }
    }

    /**
     * Find a suitable location for a field near the village.
     */
    private static BlockPos findSuitableFieldLocation(ServerLevel level, BoundingBox villageBox, RandomSource random) {
        // Try up to 10 times to find a suitable location
        for (int attempts = 0; attempts < 10; attempts++) {
            // Generate position within 50-100 blocks of village
            int distance = 50 + random.nextInt(51);
            int angle = random.nextInt(360);

            double radians = Math.toRadians(angle);
            int x = villageBox.getCenter().getX() + (int)(Math.cos(radians) * distance);
            int z = villageBox.getCenter().getZ() + (int)(Math.sin(radians) * distance);

            // Find ground level
            BlockPos groundPos = new BlockPos(x, level.getHeight(), z);
            while (groundPos.getY() > level.getMinBuildHeight() &&
                   !level.getBlockState(groundPos).isSolid()) {
                groundPos = groundPos.below();
            }

            // Check if this is a suitable location (grass/sand, flat area)
            if (isSuitableFieldLocation(level, groundPos)) {
                return groundPos;
            }
        }

        return null;
    }

    /**
     * Check if a location is suitable for a field.
     */
    private static boolean isSuitableFieldLocation(ServerLevel level, BlockPos pos) {
        // Check if the area is relatively flat and suitable
        for (int x = pos.getX() - 8; x <= pos.getX() + 8; x++) {
            for (int z = pos.getZ() - 8; z <= pos.getZ() + 8; z++) {
                BlockPos checkPos = new BlockPos(x, pos.getY(), z);

                // Check if ground is grass or sand
                var groundBlock = level.getBlockState(checkPos).getBlock();
                if (groundBlock != Blocks.GRASS_BLOCK && groundBlock != Blocks.SAND &&
                    groundBlock != Blocks.DIRT) {
                    return false;
                }

                // Check if above ground is air
                if (!level.isEmptyBlock(checkPos.above())) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Generate a field at the specified location.
     */
    private static void generateFieldAt(ServerLevel level, BlockPos center, int fieldType, RandomSource random) {
        // Create a bounding box for the field (13x13 area)
        int size = 6;
        BoundingBox fieldBox = new BoundingBox(
            center.getX() - size, center.getY(), center.getZ() - size,
            center.getX() + size, center.getY() + 1, center.getZ() + size
        );

        // Create and generate the field structure
        VillageFieldStructure fieldStructure = new VillageFieldStructure(
            VillageFieldStructure.STRUCTURE_TYPE, fieldBox, fieldType);

        try {
            fieldStructure.postProcess(level, null, null, random, fieldBox,
                new ChunkPos(center), center);
        } catch (Exception e) {
            HOReborn.LOGGER.warn("Failed to generate village field at {}", center, e);
        }
    }
}
