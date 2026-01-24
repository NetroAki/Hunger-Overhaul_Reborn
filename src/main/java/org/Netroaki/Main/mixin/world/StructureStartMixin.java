package org.Netroaki.Main.mixin.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.Netroaki.Main.world.VillageFieldGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for StructureStart to hook into structure generation and add custom village fields.
 */
@Mixin(StructureStart.class)
public class StructureStartMixin {

    @Shadow
    private BoundingBox boundingBox;

    @Shadow
    private Structure structure;

    /**
     * Called after a structure is placed to potentially add custom village fields.
     */
    @Inject(method = "placeInChunk", at = @At("TAIL"))
    private void hor_onStructurePlaced(ServerLevel level, StructureManager structureManager,
                                     ChunkGenerator generator, RandomSource random, BoundingBox chunkBox,
                                     ChunkPos chunkPos, CallbackInfo ci) {
        // Call our village field generator
        VillageFieldGenerator.onStructureGenerated(level, this.structure, chunkPos, this.boundingBox);
    }
}
