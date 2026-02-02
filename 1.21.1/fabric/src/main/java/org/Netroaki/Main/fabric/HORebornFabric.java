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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;

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
                var inst = player.getEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(hungry));
                if (inst != null && inst.getAmplifier() >= 2) {
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 0, false, false, false));
                } else {
                    player.removeEffect(MobEffects.DIG_SLOWDOWN);
                }
            }
        });

        // Hook for ToolModule (Hoe Use) and CropModule (Bone Meal)
        // Fabric API provides (Player, World, InteractionHand, BlockHitResult) mapped
        // to Mojang
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            EventResult result = HOReborn.TOOL_MODULE.onHoeUse(player, hand, hitResult.getBlockPos(), hitResult);
            if (Boolean.TRUE.equals(result.value()))
                return InteractionResult.SUCCESS;
            if (Boolean.FALSE.equals(result.value()))
                return InteractionResult.FAIL;

            result = HOReborn.CROP_MODULE.onBoneMealUse(player, hand, hitResult.getBlockPos(), hitResult);
            if (Boolean.TRUE.equals(result.value()))
                return InteractionResult.SUCCESS;
            if (Boolean.FALSE.equals(result.value()))
                return InteractionResult.FAIL;

            return InteractionResult.PASS;
        });
    }

    private void registerClientEvents() {
        // Client events are implemented in HORebornFabricClient
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            HOReborn.LOGGER.info("Registering Hunger Overhaul Reborn Commands");
            dispatcher.register(Commands.literal("ho_hunger")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.argument("value", IntegerArgumentType.integer(0, 20))
                            .executes(context -> {
                                ServerPlayer player = context.getSource().getPlayerOrException();
                                int value = IntegerArgumentType.getInteger(context, "value");
                                player.getFoodData().setFoodLevel(value);
                                player.getFoodData().setSaturation(0f);
                                context.getSource().sendSuccess(() -> Component.literal("Set hunger to " + value),
                                        true);
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
                                    }))));

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
