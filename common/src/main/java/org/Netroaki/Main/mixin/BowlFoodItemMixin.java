package org.Netroaki.Main.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowlFoodItem.class)
public class BowlFoodItemMixin {
	private static final ThreadLocal<ItemStack> ORIGINAL_STACK = new ThreadLocal<>();
	private static final ThreadLocal<Integer> ORIGINAL_COUNT = new ThreadLocal<>();

	@Inject(method = "finishUsingItem", at = @At("HEAD"))
	private void captureOriginal(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
		ORIGINAL_STACK.set(stack.copy());
		ORIGINAL_COUNT.set(stack.getCount());
	}

	@Inject(method = "finishUsingItem", at = @At("RETURN"), cancellable = true)
	private void onFinishUsingItem(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack original = ORIGINAL_STACK.get();
		Integer origCount = ORIGINAL_COUNT.get();
		ORIGINAL_STACK.remove();
		ORIGINAL_COUNT.remove();

		if (!(entity instanceof Player player)) return;
		if (level.isClientSide) return;
		if (player.getAbilities().instabuild) return;
		if (original == null || origCount == null) return;

		ItemStack returned = cir.getReturnValue();

		if (origCount > 1) {
			// We had a stack: ensure exactly one consumed and bowl added once
			ItemStack newStack = original.copy();
			newStack.shrink(1); // consume one
			// Add one empty bowl
			ItemStack bowl = new ItemStack(Items.BOWL);
			if (!player.getInventory().add(bowl)) {
				player.drop(bowl, false);
			}
			cir.setReturnValue(newStack);
		} else {
			// Single item: keep vanilla bowl-in-hand behavior
			if (returned.getItem() != Items.BOWL) {
				cir.setReturnValue(new ItemStack(Items.BOWL));
			}
		}
	}
}
