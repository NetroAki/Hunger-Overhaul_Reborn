package org.Netroaki.Main.util;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;

public final class DebugCommands {

    private DebugCommands() {
    }

    public static void register() {
        CommandRegistrationEvent.EVENT.register(DebugCommands::onRegister);
    }

    private static void onRegister(CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext registryAccess,
            Commands.CommandSelection selection) {
        dispatcher.register(
                Commands.literal("ho_growtest")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> GrowthTestManager.start(ctx.getSource())));
        dispatcher.register(
                Commands.literal("ho_growthstats")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> {
                            String msg = DebugMetrics.currentContextSummary();
                            ctx.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal(msg), true);
                            return 1;
                        }));

        dispatcher.register(
                Commands.literal("ho_season")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> {
                            var level = ctx.getSource().getLevel();
                            String seasonName = SereneSeasonsAPI.getSeasonName(level);
                            float strength = SereneSeasonsAPI.getSeasonStrength(level);
                            ctx.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                                    "[HOR Debug] season=" + seasonName + " strength="
                                            + String.format(java.util.Locale.ROOT, "%.2f", strength)),
                                    true);
                            return 1;
                        }));
    }
}
