package org.Netroaki.Main.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.Netroaki.Main.HOReborn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowlFoodItem.class)
public class BowlFoodItemMixin {

    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void onFinishUsingBowlFood(ItemStack stack, Level level, LivingEntity entity,
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
        HOReborn.LOGGER.info("[HOR Debug] BowlFoodItemMixin: Eating bowl food with count: {}", count);

        // Only modify if stack has more than 1
        if (count > 1) {
            // Cancel vanilla behavior
            cir.cancel();

            // Apply food effects first (before shrinking so getFoodProperties works
            // correctly)
            if (stack.getItem().getFoodProperties() != null) {
                player.getFoodData().eat(stack.getItem(), stack);
            }

            // Shrink by 1
            stack.shrink(1);
            HOReborn.LOGGER.info("[HOR Debug] BowlFoodItemMixin: Shrunk stack from {} to {}", count, stack.getCount());

            // Give back a bowl
            ItemStack bowl = new ItemStack(Items.BOWL);
            if (!player.getInventory().add(bowl)) {
                player.drop(bowl, false);
            }
            HOReborn.LOGGER.info("[HOR Debug] BowlFoodItemMixin: Added bowl to inventory");

            // Return the original stack (now with count-1)
            cir.setReturnValue(stack);
        }
        // For single items, let vanilla handle it
    }
}
