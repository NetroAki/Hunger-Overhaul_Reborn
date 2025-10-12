package org.Netroaki.Main.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.modules.FoodModule;

public final class HORebornFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register tooltip callback for food descriptions
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (HungerOverhaulConfig.getInstance().food.showFoodTooltips) {
                var tooltip = FoodModule.getFoodTooltip(stack);
                if (tooltip != null) {
                    lines.add(tooltip);
                }
            }
        });
    }
}
