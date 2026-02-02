package org.Netroaki.Main.platforms.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.world.phys.EntityHitResult;
import dev.architectury.platform.Platform;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.client.HudRenderer;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.handlers.AnimalHandler;
import org.Netroaki.Main.handlers.HarvestHandler;
import org.Netroaki.Main.modules.FoodModule;
import org.Netroaki.Main.modules.ToolModule;
import org.Netroaki.Main.modules.CropModule;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import dev.architectury.event.EventResult;
import net.minecraft.world.InteractionResult;

public final class HORebornFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        HOReborn.init();

        // Register Fabric-specific events
        registerFabricEvents();

        // Register commands
        registerCommands();

        // Register villager trades (HarvestCraft) if enabled
        registerVillagerTrades();

        HOReborn.LOGGER.info("[HOR Cleanup] Final Rabbit Stew Stack Size: " + Items.RABBIT_STEW.getMaxStackSize());
    }

    private void registerFabricEvents() {
        // Register HUD render callback for GUI warnings (client-side only)
        try {
            HudRenderCallback.EVENT.register(HudRenderer::renderHudWarnings);
        } catch (Exception e) {
            // Client-side classes not available on server, skip HUD rendering
            HOReborn.LOGGER.debug("HUD rendering not available on server");
        }

        // Register tooltip callback for food descriptions (client-side only)
        try {
            ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
                if (HungerOverhaulConfig.getInstance().food.showFoodTooltips) {
                    var tooltip = FoodModule.getFoodTooltip(stack);
                    if (tooltip != null) {
                        lines.add(tooltip);
                    }
                }
            });
        } catch (Exception e) {
            // Client-side classes not available on server, skip tooltip registration
            HOReborn.LOGGER.debug("Tooltip registration not available on server");
        }

        // Note: Mining fatigue is now handled in HungryEffect.applyEffectTick()

        // Register block interaction events (bonemeal, hoe use, crop harvesting)
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (player == null || world == null || hitResult == null) {
                return InteractionResult.PASS;
            }

            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);

            // Handle crop harvesting (right-click on mature crops)
            InteractionResult harvestResult = HarvestHandler.handleRightClickHarvest(player, world, hand, hitResult);
            if (harvestResult != InteractionResult.PASS) {
                return harvestResult;
            }

            // Handle tool use (hoe)
            ToolModule toolModule = new ToolModule();
            EventResult toolResult = toolModule.onHoeUse(player, hand, pos, hitResult);
            if (toolResult != EventResult.pass()) {
                return InteractionResult.SUCCESS; // Tool interaction handled
            }

            // Handle bonemeal use
            CropModule cropModule = new CropModule();
            EventResult bonemealResult = cropModule.onBoneMealUse(player, hand, pos, hitResult);
            if (bonemealResult != EventResult.pass()) {
                return InteractionResult.SUCCESS; // Bonemeal interaction handled
            }

            return InteractionResult.PASS;
        });

        // Register animal interaction events (milking timeout)
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player == null || world == null || entity == null || hitResult == null) {
                return InteractionResult.PASS;
            }

            // Handle animal interactions (cow milking timeout)
            InteractionResult animalResult = AnimalHandler.handleAnimalInteraction(player, world, hand,
                    (EntityHitResult) hitResult);
            if (animalResult != InteractionResult.PASS) {
                return animalResult;
            }

            return InteractionResult.PASS;
        });

        // Register food consumption events
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (player == null || world == null) {
                return net.minecraft.world.InteractionResultHolder.pass(ItemStack.EMPTY);
            }

            ItemStack stack = player.getItemInHand(hand);

            // Handle eating speed modification
            if (stack.isEdible() && HungerOverhaulConfig.getInstance().food.modifyEatingSpeed) {
                var food = stack.getItem().getFoodProperties();
                if (food != null) {
                    int foodValue = food.getNutrition();
                    int duration = org.Netroaki.Main.handlers.FoodEventHandler.getEatingDuration(foodValue);
                    // Note: Fabric doesn't have direct access to modify eating duration like Forge
                    // This would need to be handled via mixins or other mechanisms
                }
            }

            return net.minecraft.world.InteractionResultHolder.pass(stack);
        });

        // Loot Table replacement to remove Tall Grass seeds
        net.fabricmc.fabric.api.loot.v2.LootTableEvents.REPLACE
                .register((resourceManager, lootManager, id, original, source) -> {
                    if (HungerOverhaulConfig.getInstance().crops.removeTallGrassSeeds) {
                        if (id.equals(net.minecraft.world.level.block.Blocks.TALL_GRASS.getLootTable()) ||
                                id.equals(net.minecraft.world.level.block.Blocks.GRASS.getLootTable())) {
                            return net.minecraft.world.level.storage.loot.LootTable.EMPTY;
                        }
                    }
                    return null; // Keep original
                });

        // Register entity events for animal breeding and growth
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof net.minecraft.world.entity.AgeableMob mob) {
                // Handle animal growth duration modification
                double childMul = HungerOverhaulConfig.getInstance().animals.childDurationMultiplier;
                if (childMul > 1.0 && mob.isBaby()) {
                    // Note: This would need mixin access to modify the age
                    // For now, we'll log that the feature is detected
                    HOReborn.LOGGER.debug("Animal growth modification detected for: " + entity.getType());
                }
            }
        });

        // Crop growth handled by mixins
    }

    private void registerVillagerTrades() {
        if (!HungerOverhaulConfig.getInstance().integration.enableHarvestCraftIntegration)
            return;

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 1, factories -> {
            for (Item item : BuiltInRegistries.ITEM) {
                var id = BuiltInRegistries.ITEM.getKey(item);
                if (id != null && id.getNamespace().equals("harvestcraft") && item.getFoodProperties() != null) {
                    factories.add((entity, random) -> new MerchantOffer(new ItemStack(item, 8),
                            new ItemStack(Items.EMERALD, 1), 16, 2, 0.05f));
                }
            }
        });

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.BUTCHER, 3, factories -> {
            for (Item item : BuiltInRegistries.ITEM) {
                var id = BuiltInRegistries.ITEM.getKey(item);
                if (id != null && id.getNamespace().equals("harvestcraft") && item.getFoodProperties() != null) {
                    int nutrition = item.getFoodProperties().getNutrition();
                    if (nutrition >= 8) {
                        factories.add((entity, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 3),
                                new ItemStack(item, 1), 12, 5, 0.05f));
                    }
                }
            }
        });
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
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
                                                () -> Component.literal(
                                                        "Set hunger to " + value + " and saturation to " + sat),
                                                true);
                                        return 1;
                                    })));
            dispatcher.register(cmd);

            LiteralArgumentBuilder<CommandSourceStack> tagsCmd = Commands.literal("ho_item_tags")
                    .requires(src -> src.hasPermission(0))
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        ItemStack stack = player.getMainHandItem();
                        if (stack.isEmpty()) {
                            ctx.getSource().sendSuccess(() -> Component.literal("Hand is empty"), false);
                            return 1;
                        }

                        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
                        var tags = stack.getItem().builtInRegistryHolder().tags()
                                .map(t -> t.location().toString())
                                .sorted()
                                .toList();
                        if (tags.isEmpty()) {
                            ctx.getSource().sendSuccess(
                                    () -> Component.literal("No tags for " + id), false);
                        } else {
                            String joined = String.join("\n - ", tags);
                            ctx.getSource().sendSuccess(
                                    () -> Component.literal("Tags for " + id + ":\n - " + joined), false);
                        }
                        return 1;
                    });
            dispatcher.register(tagsCmd);
        });
    }
}
