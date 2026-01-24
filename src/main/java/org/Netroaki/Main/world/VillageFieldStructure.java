package org.Netroaki.Main.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

/**
 * Custom village field structure piece that generates fields with Hunger Overhaul crops.
 */
public class VillageFieldStructure extends StructurePiece {

    private static final ResourceLocation STRUCTURE_TYPE_ID = new ResourceLocation(HOReborn.MOD_ID, "village_field");

    // Structure piece type - registered in VillageFieldHandler
    public static StructurePieceType STRUCTURE_TYPE;

    private int fieldType; // 0 = normal crops, 1 = reeds, 2 = stems

    public VillageFieldStructure(StructurePieceType type, CompoundTag tag) {
        super(type, tag);
        this.fieldType = tag.getInt("FieldType");
    }

    public VillageFieldStructure(StructurePieceType type, BoundingBox boundingBox, int fieldType) {
        super(type, 0, boundingBox);
        this.fieldType = fieldType;
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("FieldType", this.fieldType);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
                          RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        if (!HungerOverhaulConfig.getInstance().integration.addCustomVillageField) {
            return;
        }

        // Generate the field based on type
        switch (fieldType) {
            case 0 -> generateNormalCropField(level, random, box);
            case 1 -> generateReedField(level, random, box);
            case 2 -> generateStemField(level, random, box);
        }
    }

    private void generateNormalCropField(WorldGenLevel level, RandomSource random, BoundingBox box) {
        // Generate a field of normal crops (wheat, carrots, potatoes, beetroot)
        for (int x = box.minX(); x <= box.maxX(); x++) {
            for (int z = box.minZ(); z <= box.maxZ(); z++) {
                BlockPos groundPos = new BlockPos(x, box.minY(), z);
                BlockPos cropPos = groundPos.above();

                // Check if we should place a crop here (some randomness)
                if (random.nextFloat() < 0.8f) {
                    // Place farmland
                    if (level.isEmptyBlock(groundPos) || level.getBlockState(groundPos).getBlock() == Blocks.GRASS_BLOCK) {
                        level.setBlock(groundPos, Blocks.FARMLAND.defaultBlockState(), 2);
                    }

                    // Place a random crop
                    if (level.isEmptyBlock(cropPos)) {
                        BlockState cropState = getRandomCrop(random);
                        level.setBlock(cropPos, cropState, 2);
                    }
                }
            }
        }
    }

    private void generateReedField(WorldGenLevel level, RandomSource random, BoundingBox box) {
        // Generate sugarcane field
        for (int x = box.minX(); x <= box.maxX(); x++) {
            for (int z = box.minZ(); z <= box.maxZ(); z++) {
                BlockPos groundPos = new BlockPos(x, box.minY(), z);
                BlockPos reedPos = groundPos.above();

                // Sugarcane needs water nearby and sand/grass
                if (isValidReedLocation(level, groundPos) && random.nextFloat() < 0.6f) {
                    level.setBlock(reedPos, Blocks.SUGAR_CANE.defaultBlockState(), 2);
                }
            }
        }
    }

    private void generateStemField(WorldGenLevel level, RandomSource random, BoundingBox box) {
        // Generate pumpkin/melon stem field
        for (int x = box.minX(); x <= box.maxX(); x++) {
            for (int z = box.minZ(); z <= box.maxZ(); z++) {
                BlockPos groundPos = new BlockPos(x, box.minY(), z);
                BlockPos stemPos = groundPos.above();

                if (random.nextFloat() < 0.7f) {
                    // Place farmland
                    if (level.isEmptyBlock(groundPos) || level.getBlockState(groundPos).getBlock() == Blocks.GRASS_BLOCK) {
                        level.setBlock(groundPos, Blocks.FARMLAND.defaultBlockState(), 2);
                    }

                    // Place stem
                    if (level.isEmptyBlock(stemPos)) {
                        BlockState stemState = random.nextBoolean() ?
                            Blocks.PUMPKIN_STEM.defaultBlockState() :
                            Blocks.MELON_STEM.defaultBlockState();
                        level.setBlock(stemPos, stemState, 2);
                    }
                }
            }
        }
    }

    private BlockState getRandomCrop(RandomSource random) {
        float rand = random.nextFloat();
        if (rand < 0.4f) {
            return Blocks.WHEAT.defaultBlockState();
        } else if (rand < 0.7f) {
            return Blocks.CARROTS.defaultBlockState();
        } else if (rand < 0.9f) {
            return Blocks.POTATOES.defaultBlockState();
        } else {
            return Blocks.BEETROOTS.defaultBlockState();
        }
    }

    private boolean isValidReedLocation(WorldGenLevel level, BlockPos pos) {
        // Check if there's water nearby (within 4 blocks horizontally)
        for (int x = pos.getX() - 4; x <= pos.getX() + 4; x++) {
            for (int z = pos.getZ() - 4; z <= pos.getZ() + 4; z++) {
                BlockPos checkPos = new BlockPos(x, pos.getY(), z);
                if (level.getBlockState(checkPos).getBlock() == Blocks.WATER) {
                    return true;
                }
            }
        }
        return false;
    }
}
