package org.Netroaki.Main.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.tags.ItemTags;
import org.Netroaki.Main.config.HungerOverhaulConfig;

import java.util.List;

public class LootUtil {
    
    /**
     * Removes seed items from a loot list if tall grass seed removal is enabled
     */
    public static void removeSeedsFromLoot(List<ItemStack> loot) {
        if (!HungerOverhaulConfig.getInstance().crops.removeTallGrassSeeds) {
            return;
        }
        
        // Remove seed items from loot
        loot.removeIf(stack -> {
            return stack.is(Items.WHEAT_SEEDS) || 
                   stack.is(Items.BEETROOT_SEEDS) || 
                   stack.is(Items.PUMPKIN_SEEDS) || 
                   stack.is(Items.MELON_SEEDS) ||
                   stack.is(Items.CARROT) ||
                   stack.is(Items.POTATO) ||
                   stack.is(Items.SWEET_BERRIES) ||
                   stack.is(Items.GLOW_BERRIES);
        });
    }
    
    /**
     * Checks if an item is a seed
     */
    public static boolean isSeed(ItemStack stack) {
        return stack.is(Items.WHEAT_SEEDS) || 
               stack.is(Items.BEETROOT_SEEDS) || 
               stack.is(Items.PUMPKIN_SEEDS) || 
               stack.is(Items.MELON_SEEDS) ||
               stack.is(Items.CARROT) ||
               stack.is(Items.POTATO) ||
               stack.is(Items.SWEET_BERRIES) ||
               stack.is(Items.GLOW_BERRIES);
    }
}
