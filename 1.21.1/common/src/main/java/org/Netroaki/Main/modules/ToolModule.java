package org.Netroaki.Main.modules;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;

import java.util.List;
import java.util.Random;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class ToolModule {
    private static final Random RANDOM = new Random();

    public void init() {
        HOReborn.LOGGER.info("Initializing Tool Module");
        LifecycleEvent.SERVER_LEVEL_LOAD.register(this::onServerStarting);
    }

    private void onServerStarting(Level level) {
        HOReborn.LOGGER.info("Tool Module: Features enabled");
    }

    public EventResult onHoeUse(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(ItemTags.HOES)) {
            Level level = player.level();
            BlockState state = level.getBlockState(pos);

            if (HungerOverhaulConfig.getInstance().tools.modifyHoeUse) {
                if (state.is(Blocks.GRASS_BLOCK)) {
                    boolean waterNearby = isWaterNearby(level, pos);

                    if (!waterNearby) {
                        level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                        if (player.level().isClientSide()) {
                            player.swing(hand);
                        }
                        // 1.21.1 Mappings: DIRT might be Blocks.DIRT
                        level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);

                        // Fixed 20% chance regardless of difficulty
                        double seedChance = 0.20;

                        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
                            if (RANDOM.nextDouble() < seedChance) {
                                // Use reloadable registries / loot data
                                ResourceLocation tableId = HOReborn.id("gameplay/hoe_grass");
                                var key = net.minecraft.resources.ResourceKey.create(
                                        net.minecraft.core.registries.Registries.LOOT_TABLE, tableId);
                                LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(key);

                                LootParams.Builder builder = new LootParams.Builder(serverLevel)
                                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                                        .withParameter(LootContextParams.TOOL, stack)
                                        .withParameter(LootContextParams.THIS_ENTITY, player)
                                        .withParameter(LootContextParams.BLOCK_STATE,
                                                Blocks.SHORT_GRASS.defaultBlockState())
                                        .withLuck(player.getLuck());

                                List<ItemStack> drops = lootTable
                                        .getRandomItems(builder.create(LootContextParamSets.BLOCK));
                                if (!drops.isEmpty()) {
                                    ItemStack drop = drops.get(0);
                                    if (!drop.isEmpty()) {
                                        net.minecraft.world.level.block.Block.popResource(serverLevel, pos.above(),
                                                drop);
                                    }
                                }
                            }
                        }

                        stack.hurtAndBreak(4, player, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
                        return EventResult.interrupt(true);
                    }
                }
            }
        }

        return EventResult.pass();
    }

    private boolean isWaterNearby(Level level, BlockPos pos) {
        int y = pos.getY();
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                BlockPos checkPos = new BlockPos(pos.getX() + dx, y, pos.getZ() + dz);
                if (level.getBlockState(checkPos).is(Blocks.WATER)) {
                    return true;
                }
            }
        }
        return false;
    }
}
