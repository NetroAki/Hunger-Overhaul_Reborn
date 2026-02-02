package org.Netroaki.Main.modules;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.client.HudRenderer;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.util.FoodUtil;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;

public class FoodModuleClient {
    public static void init() {
        if (HungerOverhaulConfig.getInstance().food.showFoodTooltips) {
            ClientTooltipEvent.ITEM.register(FoodModuleClient::onTooltip);
            HOReborn.LOGGER.info("Food tooltips enabled");
        }

        if (HungerOverhaulConfig.getInstance().food.enableLowHungerWarnings) {
            // Bridge DeltaTracker (1.21) to float partialTick (1.20 style shared renderer)
            ClientGuiEvent.RENDER_HUD.register((graphics, tracker) -> HudRenderer.renderHudWarnings(graphics,
                    tracker.getGameTimeDeltaPartialTick(true)));
            HOReborn.LOGGER.info("Effect warnings available");
        }
    }

    private static void onTooltip(ItemStack stack, java.util.List<Component> lines,
            Item.TooltipContext context, net.minecraft.world.item.TooltipFlag flag) {
        if (stack.isEmpty())
            return;

        // Basic check for food
        if (!stack.has(DataComponents.FOOD)) {
            return;
        }

        String description = FoodUtil.getFoodDescription(stack);
        if (!description.isEmpty()) {
            lines.add(Component.literal(description).withStyle(net.minecraft.ChatFormatting.GOLD));
        }
    }
}
