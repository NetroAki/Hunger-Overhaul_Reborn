package org.Netroaki.Main.modules;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.client.HudRenderer;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.util.FoodUtil;

public class FoodModuleClient {
    public static void init() {
        if (HungerOverhaulConfig.getInstance().food.showFoodTooltips) {
            ClientTooltipEvent.ITEM.register(FoodModuleClient::onTooltip);
            HOReborn.LOGGER.info("Food tooltips enabled");
        }

        if (HungerOverhaulConfig.getInstance().food.enableLowHungerWarnings) {
            ClientGuiEvent.RENDER_HUD.register(HudRenderer::renderHudWarnings);
            HOReborn.LOGGER.info("Effect warnings available");
        }
    }

    private static void onTooltip(ItemStack stack, java.util.List<Component> lines,
            net.minecraft.world.item.TooltipFlag flag) {
        if (stack.isEmpty())
            return;

        // Basic check for food
        if (!stack.isEdible()) {
            // Some mods might have edible items that don't return true for isEdible()
            // checks in some contexts?
            // But usually isEdible() is safe.
            // We can double check if it has food properties
            if (stack.getItem().getFoodProperties() == null)
                return;
        }

        String description = FoodUtil.getFoodDescription(stack);
        if (!description.isEmpty()) {
            lines.add(Component.literal(description).withStyle(net.minecraft.ChatFormatting.GOLD));
        }
    }
}
