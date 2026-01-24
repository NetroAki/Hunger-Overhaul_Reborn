package org.Netroaki.Main.mixin.fabric;

import org.Netroaki.Main.config.HORConfig;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Mixin plugin for Fabric to load configuration before mixins are applied
 */
public class HORMixinPlugin implements IMixinConfigPlugin {

    public static HORConfig CONFIG;

    @Override
    public void onLoad(String mixinPackage) {
        // System.out.println("[HOR MixinPlugin] Loading Fabric mixin plugin");

        // Get game directory
        String gameDir = System.getProperty("user.dir");

        // Check if we're in a dev environment
        File runDir = new File(gameDir, "run");
        if (runDir.exists() && runDir.isDirectory()) {
            gameDir = runDir.getAbsolutePath();
        }

        // Load configuration
        CONFIG = HORConfig.getInstance();
        CONFIG.load(gameDir);

        // System.out.println("[HOR MixinPlugin] Container food stack sizes - bowl:" +
        // CONFIG.getBowlStackSize() +
        // " bottle:" + CONFIG.getBottleStackSize() + " bucket:" +
        // CONFIG.getBucketStackSize() +
        // " default:" + CONFIG.getDefaultContainerStackSize());
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
