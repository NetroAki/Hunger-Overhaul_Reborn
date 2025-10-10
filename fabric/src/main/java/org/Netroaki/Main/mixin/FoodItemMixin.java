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

@Mixin(Item.class)
public class FoodItemMixin {
    static {
        HOReborn.LOGGER.info("[HOR Debug] FoodItemMixin loaded successfully");
    }

    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void onFinishUsingItem(ItemStack stack, Level level, LivingEntity entity,
            CallbackInfoReturnable<ItemStack> cir) {
        // Only process for food items
        if (!stack.isEdible()) {
            return;
        }

        if (!(entity instanceof Player player)) {
            // HOReborn.LOGGER.info("[HOR Debug] FoodItemMixin: Not a player, skipping");
            return;
        }
        if (level.isClientSide) {
            // HOReborn.LOGGER.info("[HOR Debug] FoodItemMixin: Client side, skipping");
            return;
        }
        if (player.getAbilities().instabuild) {
            // HOReborn.LOGGER.info("[HOR Debug] FoodItemMixin: Creative mode, skipping");
            return;
        }

        int originalCount = stack.getCount();
        // HOReborn.LOGGER.info("[HOR Debug] FoodItemMixin HEAD: Processing food item -
        // count: {}", originalCount);

        // Only modify behavior if stack has more than 1 item
        if (originalCount > 1) {
            // Check if item has a crafting remainder (container item like bowl)
            Item craftingRemainder = stack.getItem().getCraftingRemainingItem();
            boolean hasContainer = craftingRemainder != null;

            // HOReborn.LOGGER.info("[HOR Debug] FoodItemMixin: Item has container: {}",
            // hasContainer);

            // Cancel the original method call
            cir.cancel();

            // Shrink the original stack by 1
            stack.shrink(1);
            // HOReborn.LOGGER.info("[HOR Debug] FoodItemMixin: Stack had {} items,
            // shrinking to {} items",
            // originalCount, stack.getCount());

            // Apply the food effects manually
            if (stack.getItem().getFoodProperties() != null) {
                player.getFoodData().eat(stack.getItem(), stack);
            }

            // If item has a container, give it to the player
            if (hasContainer) {
                ItemStack containerStack = new ItemStack(craftingRemainder);
                if (!player.getInventory().add(containerStack)) {
                    player.drop(containerStack, false);
                }
                // HOReborn.LOGGER.info("[HOR Debug] FoodItemMixin: Added container item to
                // inventory");
            }

            // Return the modified stack
            cir.setReturnValue(stack);
        }
        // For single items, let vanilla handle it normally
    }
}
