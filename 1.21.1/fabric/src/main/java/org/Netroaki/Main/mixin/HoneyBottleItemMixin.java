package org.Netroaki.Main.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.Netroaki.Main.HOReborn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;

@Mixin(HoneyBottleItem.class)
public class HoneyBottleItemMixin {

    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void onFinishUsingHoneyBottle(ItemStack stack, Level level, LivingEntity entity,
            CallbackInfoReturnable<ItemStack> cir) {
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
        HOReborn.LOGGER.info("[HOR Debug] HoneyBottleItemMixin: Eating honey bottle with count: {}", originalCount);

        if (originalCount > 1) {
            // Cancel vanilla behavior
            cir.cancel();

            // Shrink stack by 1
            stack.shrink(1);
            HOReborn.LOGGER.info("[HOR Debug] HoneyBottleItemMixin: Shrunk stack from {} to {}", originalCount,
                    stack.getCount());

            // Apply food effects manually
            FoodProperties foodProps = stack.get(DataComponents.FOOD);
            if (foodProps != null) {
                player.getFoodData().eat(foodProps);
            }

            // Add glass bottle to inventory
            ItemStack bottleStack = new ItemStack(Items.GLASS_BOTTLE);
            if (!player.getInventory().add(bottleStack)) {
                player.drop(bottleStack, false);
            }
            HOReborn.LOGGER.info("[HOR Debug] HoneyBottleItemMixin: Added glass bottle to inventory");

            // Return the modified stack
            cir.setReturnValue(stack);
        }
    }
}
