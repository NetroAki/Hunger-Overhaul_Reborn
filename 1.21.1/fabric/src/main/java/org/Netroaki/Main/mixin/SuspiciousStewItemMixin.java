package org.Netroaki.Main.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import org.Netroaki.Main.HOReborn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;

@Mixin(SuspiciousStewItem.class)
public class SuspiciousStewItemMixin {

    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void onFinishUsingSuspiciousStew(ItemStack stack, Level level, LivingEntity entity,
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

        int count = stack.getCount();
        HOReborn.LOGGER.info("[HOR Debug] SuspiciousStewItemMixin: Eating suspicious stew with count: {}", count);

        // Only modify if stack has more than 1
        if (count > 1) {
            // Cancel vanilla behavior
            cir.cancel();

            // Apply food effects first (before shrinking)
            FoodProperties foodProps = stack.get(DataComponents.FOOD);
            if (foodProps != null) {
                player.getFoodData().eat(foodProps);
            }

            // Shrink by 1
            stack.shrink(1);
            HOReborn.LOGGER.info("[HOR Debug] SuspiciousStewItemMixin: Shrunk stack from {} to {}", count,
                    stack.getCount());

            // Give back a bowl
            ItemStack bowl = new ItemStack(Items.BOWL);
            if (!player.getInventory().add(bowl)) {
                player.drop(bowl, false);
            }
            HOReborn.LOGGER.info("[HOR Debug] SuspiciousStewItemMixin: Added bowl to inventory");

            // Return the original stack (now with count-1)
            cir.setReturnValue(stack);
        }
        // For single items, let vanilla handle it
    }
}
