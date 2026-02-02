package org.Netroaki.Main.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.Netroaki.Main.HOReborn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;

@Mixin(Item.class)
public class FoodItemMixin {
    static {
        HOReborn.LOGGER.info("[HOR Debug] FoodItemMixin loaded successfully");
    }

    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void onFinishUsingItem(ItemStack stack, Level level, LivingEntity entity,
            CallbackInfoReturnable<ItemStack> cir) {
        // Only process for food items
        FoodProperties foodProps = stack.get(DataComponents.FOOD);
        if (foodProps == null) {
            return;
        }

        if (!(entity instanceof Player player)) {
            return;
        }
        if (level.isClientSide) {
            return;
        }
        if (player.getAbilities().instabuild) {
            return;
        }

        int originalCount = stack.getCount();

        // Only modify behavior if stack has more than 1 item
        if (originalCount > 1) {
            // Check if item has a crafting remainder (container item like bowl)
            // Use local method or Item method if available
            Item craftingRemainder = stack.getItem().getCraftingRemainingItem();
            boolean hasContainer = craftingRemainder != null;

            // Cancel the original method call
            cir.cancel();

            // Shrink the original stack by 1
            stack.shrink(1);

            // Apply the food effects manually
            // Use the 1.21.1 method signature
            player.getFoodData().eat(foodProps);

            // If item has a container, give it to the player
            if (hasContainer) {
                ItemStack containerStack = new ItemStack(craftingRemainder);
                if (!player.getInventory().add(containerStack)) {
                    player.drop(containerStack, false);
                }
            }

            // Return the modified stack
            cir.setReturnValue(stack);
        }
        // For single items, let vanilla handle it normally
    }
}
