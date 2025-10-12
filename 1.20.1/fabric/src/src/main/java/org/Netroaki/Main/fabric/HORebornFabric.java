package org.Netroaki.Main.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.config.HungerOverhaulConfig;
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

        // Register villager trades (HarvestCraft) if enabled - DISABLED FOR NOW
        // registerVillagerTrades();
    }

    private void registerFabricEvents() {
        // Register client-only events only on client side
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            registerClientEvents();
        }

        // Apply hidden Mining Fatigue at Hungry level 3 to reduce mining speed
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                var hungry = HOReborn.HUNGRY_EFFECT.get();
                var inst = player.getEffect(hungry);
                if (inst != null && inst.getAmplifier() >= 2) {
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 0, false, false, false));
                } else {
                    player.removeEffect(MobEffects.DIG_SLOWDOWN);
                }
            }
        });

        // Crop growth handled by mixins
    }

    private void registerClientEvents() {
        // Client events are now handled in HORebornFabricClient
        // This method is kept for compatibility but does nothing
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
