package org.Netroaki.Main.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import org.Netroaki.Main.HOReborn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin {

    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void onFinishUsingMilkBucket(ItemStack stack, Level level, LivingEntity entity,
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
        HOReborn.LOGGER.info("[HOR Debug] MilkBucketItemMixin: Drinking milk bucket with count: {}", originalCount);

        if (originalCount > 1) {
            // Cancel vanilla behavior
            cir.cancel();

            // Shrink stack by 1
            stack.shrink(1);
            HOReborn.LOGGER.info("[HOR Debug] MilkBucketItemMixin: Shrunk stack from {} to {}", originalCount,
                    stack.getCount());

            // Milk buckets clear effects, not food
            player.removeAllEffects();

            // Add empty bucket to inventory
            ItemStack bucketStack = new ItemStack(Items.BUCKET);
            if (!player.getInventory().add(bucketStack)) {
                player.drop(bucketStack, false);
            }
            HOReborn.LOGGER.info("[HOR Debug] MilkBucketItemMixin: Added empty bucket to inventory");

            // Return the modified stack
            cir.setReturnValue(stack);
        }
    }
}
