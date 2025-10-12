package org.Netroaki.Main.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.handlers.FoodEventHandler;
import org.Netroaki.Main.modules.CropModule;
import org.Netroaki.Main.modules.FoodModule;
import org.Netroaki.Main.modules.ToolModule;

// Brigadier
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.BlockHitResult;

@Mod(HOReborn.MOD_ID)
public final class HORebornForge {
    public HORebornForge() {
        EventBuses.registerModEventBus(HOReborn.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        HOReborn.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // Client-side initialization
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> cmd = Commands.literal("ho_hunger")
                .requires(src -> src.hasPermission(2))
                .then(Commands.argument("value", IntegerArgumentType.integer(0, 20))
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            int value = IntegerArgumentType.getInteger(ctx, "value");
                            player.getFoodData().setFoodLevel(value);
                            player.getFoodData().setSaturation(0f);
                            ctx.getSource().sendSuccess(() -> Component.literal("Set hunger to " + value), true);
                            return 1;
                        })
                        .then(Commands.argument("saturation", FloatArgumentType.floatArg(0f, 20f))
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    int value = IntegerArgumentType.getInteger(ctx, "value");
                                    float sat = FloatArgumentType.getFloat(ctx, "saturation");
                                    player.getFoodData().setFoodLevel(value);
                                    player.getFoodData().setSaturation(sat);
                                    ctx.getSource().sendSuccess(
                                            () -> Component
                                                    .literal("Set hunger to " + value + " and saturation to " + sat),
                                            true);
                                    return 1;
                                })));
        event.getDispatcher().register(cmd);
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (HungerOverhaulConfig.getInstance().food.showFoodTooltips) {
            Component tooltip = FoodModule.getFoodTooltip(event.getItemStack());
            if (tooltip != null) {
                event.getToolTip().add(tooltip);
            }
        }
    }

    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof net.minecraft.world.entity.player.Player player) {
            FoodEventHandler.onFoodConsumed(player, event.getItem());
        }
    }

    @SubscribeEvent
    public void onItemUseStart(LivingEntityUseItemEvent.Start event) {
        ItemStack stack = event.getItem();
        if (stack.isEdible() && HungerOverhaulConfig.getInstance().food.modifyEatingSpeed) {
            var food = stack.getItem().getFoodProperties();
            if (food != null) {
                int foodValue = food.getNutrition();
                int duration = org.Netroaki.Main.handlers.FoodEventHandler.getEatingDuration(foodValue);
                event.setDuration(duration);
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(net.minecraftforge.event.level.BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() instanceof net.minecraft.world.level.block.TallGrassBlock) {
            event.setCanceled(true);
            LevelAccessor level = event.getLevel();
            BlockPos pos = event.getPos();
            level.removeBlock(pos, false);
            return;
        }
        if (event.getState().getBlock() instanceof CropBlock crop) {
            if (!event.getLevel().isClientSide()) {
                ServerLevel server = (ServerLevel) event.getLevel();
                BlockPos pos = event.getPos();
                ItemStack seed = crop.getCloneItemStack(server, pos, event.getState());
                event.setCanceled(true);
                server.removeBlock(pos, false);
                if (!seed.isEmpty()) {
                    net.minecraft.world.level.block.Block.popResource(server, pos, new ItemStack(seed.getItem(), 1));
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreakSpeed(BreakSpeed event) {
        var effect = HOReborn.HUNGRY_EFFECT.get();
        if (event.getEntity() != null && event.getEntity().hasEffect(effect)) {
            var inst = event.getEntity().getEffect(effect);
            if (inst != null && inst.getAmplifier() >= 2) {
                event.setNewSpeed(event.getNewSpeed() * 0.5f);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        if (state.getBlock() instanceof CropBlock crop) {
            if (crop.isMaxAge(state)) {
                if (!event.getLevel().isClientSide() && event.getEntity() != null) {
                    ServerLevel server = (ServerLevel) event.getLevel();
                    for (ItemStack drop : net.minecraft.world.level.block.Block.getDrops(state, server, pos, null,
                            event.getEntity(), event.getItemStack())) {
                        net.minecraft.world.level.block.Block.popResource(server, pos, drop);
                    }
                    server.setBlock(pos, crop.getStateForAge(0), 3);
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                return;
            }
        }

        ToolModule toolModule = new ToolModule();
        dev.architectury.event.EventResult result = toolModule.onHoeUse(
                event.getEntity(),
                event.getHand(),
                event.getPos(),
                (BlockHitResult) event.getHitVec());

        if (result != dev.architectury.event.EventResult.pass()) {
            event.setCanceled(true);
            return;
        }

        CropModule cropModule = new CropModule();
        result = cropModule.onBoneMealUse(
                event.getEntity(),
                event.getHand(),
                event.getPos(),
                (BlockHitResult) event.getHitVec());

        if (result != dev.architectury.event.EventResult.pass()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof AgeableMob mob) {
            double childMul = HungerOverhaulConfig.getInstance().animals.childDurationMultiplier;
            if (childMul > 1.0 && mob.isBaby() && !mob.level().isClientSide()) {
                mob.setAge(mob.getAge() - (int) ((childMul - 1.0) * 1));
            }
        }
    }

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        if (!HungerOverhaulConfig.getInstance().integration.enableHarvestCraftIntegration)
            return;
        if (event.getType() == VillagerProfession.FARMER) {
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD), ItemStack.EMPTY,
                    Items.EMERALD.getDefaultInstance(), 0, 0, 0f));
            for (Item item : net.minecraft.core.registries.BuiltInRegistries.ITEM) {
                var id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
                if (id != null && id.getNamespace().equals("harvestcraft") && item.getFoodProperties() != null) {
                    event.getTrades().get(1).add(new BasicItemListing(new ItemStack(item, 8), ItemStack.EMPTY,
                            new ItemStack(Items.EMERALD, 1), 16, 2, 0.05f));
                }
            }
        }
        if (event.getType() == VillagerProfession.BUTCHER) {
            for (Item item : net.minecraft.core.registries.BuiltInRegistries.ITEM) {
                var id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
                if (id != null && id.getNamespace().equals("harvestcraft") && item.getFoodProperties() != null) {
                    int nutrition = item.getFoodProperties().getNutrition();
                    if (nutrition >= 8) {
                        event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3),
                                ItemStack.EMPTY, new ItemStack(item, 1), 12, 5, 0.05f));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBabySpawn(BabyEntitySpawnEvent event) {
        double breedMul = HungerOverhaulConfig.getInstance().animals.breedingTimeoutMultiplier;
        if (breedMul > 1.0 && event.getParentA() != null && event.getParentB() != null) {
            int base = 6000;
            int newCd = (int) Math.round(base * breedMul);
            if (event.getParentA() instanceof AgeableMob) {
                ((AgeableMob) event.getParentA()).setAge(newCd);
            }
            if (event.getParentB() instanceof AgeableMob) {
                ((AgeableMob) event.getParentB()).setAge(newCd);
            }
        }
        if (event.getChild() instanceof AgeableMob) {
            double childMul = HungerOverhaulConfig.getInstance().animals.childDurationMultiplier;
            int baseBaby = -24000;
            ((AgeableMob) event.getChild()).setAge((int) Math.round(baseBaby * childMul));
        }
    }
}
