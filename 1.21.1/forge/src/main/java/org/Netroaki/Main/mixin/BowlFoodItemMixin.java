package org.Netroaki.Main.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.Netroaki.Main.HOReborn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 1.21.1 version: BowlFoodItem was removed, bowl foods now handled through Item.finishUsingItem
 * with specific logic for items that return bowls.
 */
@Mixin(Item.class)
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

        // Check if this is a bowl food item (items that should return a bowl)
        // In 1.21.1, bowl foods like mushroom stew, rabbit stew, etc. still return bowls
        boolean isBowlFood = isBowlFoodItem(stack.getItem());

        if (!isBowlFood) {
            return; // Not a bowl food, let vanilla handle it
        }

        int count = stack.getCount();
        if (count > 1) {
            // Cancel vanilla behavior
            cir.cancel();

            // Apply food effects first
            if (stack.getItem().getFoodProperties() != null) {
                player.getFoodData().eat(stack.getItem(), stack);
            }

            // Shrink by 1
            stack.shrink(1);

            // Give back a bowl
            ItemStack bowl = new ItemStack(Items.BOWL);
            if (!player.getInventory().add(bowl)) {
                player.drop(bowl, false);
            }

            // Return the modified stack
            cir.setReturnValue(stack);
        }
        // For single items, let vanilla handle it
    }

    private boolean isBowlFoodItem(Item item) {
        // Check if this item is one that should return a bowl when eaten
        return item == Items.MUSHROOM_STEW ||
               item == Items.RABBIT_STEW ||
               item == Items.BEETROOT_SOUP ||
               item == Items.SUSPICIOUS_STEW;
    }
}
