package org.Netroaki.Main.mixin.fabric;

import net.minecraft.world.item.*;
import org.Netroaki.Main.HOReborn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.Netroaki.Main.mixin.fabric.ItemAccessor;
import org.Netroaki.Main.config.HORConfig;

/**
 * Mixin to make container foods stackable by modifying their properties during
 * registration.
 * This handles vanilla items - bowl foods, honey bottles, milk buckets, etc.
 */
@Mixin(Items.class)
public class ItemsMixin {

    static {
        HOReborn.LOGGER.info("[HOR Debug] ItemsMixin (Fabric) loaded - generic container food handler");
    }

    /**
     * Redirect BowlFoodItem constructor calls to modify stack size based on
     * container type
     */
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/BowlFoodItem;"))
    private static BowlFoodItem modifyBowlFoodStackSize(Item.Properties properties) {
        if (HORMixinPlugin.CONFIG == null)
            HORMixinPlugin.CONFIG = HORConfig.getInstance(); // Ensure config
        int stackSize = HORMixinPlugin.CONFIG.getBowlStackSize();
        org.Netroaki.Main.HOReborn.LOGGER.info("[HOR Debug] Modifying BowlFoodItem stack size to: {}", stackSize);
        return new BowlFoodItem(properties.stacksTo(stackSize));
    }

    /**
     * Redirect SuspiciousStewItem constructor calls to modify stack size
     */
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/SuspiciousStewItem;"))
    private static SuspiciousStewItem modifySuspiciousStewStackSize(Item.Properties properties) {
        int stackSize = HORMixinPlugin.CONFIG.getBowlStackSize();
        // HOReborn.LOGGER.info("[HOR Debug] Modifying SuspiciousStewItem stack size to:
        // {}", stackSize);
        return new SuspiciousStewItem(properties.stacksTo(stackSize));
    }

    /**
     * Redirect HoneyBottleItem constructor to make honey bottles stackable
     */
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/HoneyBottleItem;"))
    private static HoneyBottleItem modifyHoneyBottleStackSize(Item.Properties properties) {
        int stackSize = HORMixinPlugin.CONFIG.getBottleStackSize();
        // HOReborn.LOGGER.info("[HOR Debug] Modifying HoneyBottleItem stack size to:
        // {}", stackSize);
        return new HoneyBottleItem(properties.stacksTo(stackSize));
    }

    /**
     * Redirect MilkBucketItem constructor to make milk buckets stackable
     */
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/MilkBucketItem;"))
    private static MilkBucketItem modifyMilkBucketStackSize(Item.Properties properties) {
        int stackSize = HORMixinPlugin.CONFIG.getBucketStackSize();
        // HOReborn.LOGGER.info("[HOR Debug] Modifying MilkBucketItem stack size to:
        // {}", stackSize);
        return new MilkBucketItem(properties.stacksTo(stackSize));
    }

    /**
     * Force set stack size for items that might have been missed by redirects
     * (e.g. Rabbit Stew if it's not using the specific BowlFoodItem constructor we
     * targeted)
     */
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void hor_onClinit(CallbackInfo ci) {
        // Ensure config is loaded
        if (HORMixinPlugin.CONFIG == null) {
            HORMixinPlugin.CONFIG = HORConfig.getInstance();
            // Try to load defaults if possible, using safe path logic
            try {
                String gameDir = System.getProperty("user.dir");
                java.io.File runDir = new java.io.File(gameDir, "run");
                if (runDir.exists() && runDir.isDirectory())
                    gameDir = runDir.getAbsolutePath();
                HORMixinPlugin.CONFIG.load(gameDir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (HORMixinPlugin.CONFIG != null) {
            int bowlStack = HORMixinPlugin.CONFIG.getBowlStackSize();

            // Fix Rabbit Stew
            ((ItemAccessor) Items.RABBIT_STEW).setMaxStackSize(bowlStack);

            // Ensure others are correct too
            ((ItemAccessor) Items.MUSHROOM_STEW).setMaxStackSize(bowlStack);
            ((ItemAccessor) Items.BEETROOT_SOUP).setMaxStackSize(bowlStack);

            org.Netroaki.Main.HOReborn.LOGGER.info("Applied Stack Size Fix: " + bowlStack);
        }
    }
}
