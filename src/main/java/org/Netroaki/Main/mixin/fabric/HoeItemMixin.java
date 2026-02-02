package org.Netroaki.Main.mixin.fabric;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for HoeItem to modify seed drops when hoeing grass.
 */
@Mixin(HoeItem.class)
public class HoeItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"))
    private void hor_onUseOn(net.minecraft.world.item.context.UseOnContext context,
            CallbackInfoReturnable<net.minecraft.world.InteractionResult> cir) {
        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();
        if (!config.tools.addSeedsCraftingRecipe)
            return; // Re-using seed config or addSeedsCraftingRecipe?
        // Wait, 'addSeedsCraftingRecipe' is wrong. config.tools.seedChanceMultiplier?
        // Actually, let's use seedChanceBase.

        Level level = context.getLevel();
        if (level.isClientSide)
            return;

        BlockState state = level.getBlockState(context.getClickedPos());
        if (state.getBlock() instanceof net.minecraft.world.level.block.GrassBlock ||
                state.getBlock() == net.minecraft.world.level.block.Blocks.DIRT ||
                state.getBlock() == net.minecraft.world.level.block.Blocks.COARSE_DIRT) { // Hook on dirt/grass

            // Logic: if random < chance, drop seeds
            double chance = config.tools.seedChanceBase * config.tools.seedChanceMultiplier;
            if (level.random.nextDouble() < chance) {
                net.minecraft.world.level.block.Block.popResource(level, context.getClickedPos().above(),
                        new ItemStack(net.minecraft.world.item.Items.WHEAT_SEEDS));
            }
        }
    }
}
