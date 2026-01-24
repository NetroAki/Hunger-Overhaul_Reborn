package org.Netroaki.Main.fabric;

import net.fabricmc.api.ModInitializer;
import org.Netroaki.Main.HOReborn;

/**
 * 1.21.1-compatible Fabric mod initializer.
 * Simplified version that focuses on core initialization.
 */
public final class HORebornFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        HOReborn.LOGGER.info("Initializing Hunger Overhaul Reborn for Fabric 1.21.1");

        try {
            // Initialize the core mod
            HOReborn.init();

            // Register basic Fabric-specific features using reflection
            registerFabricEvents();

            HOReborn.LOGGER.info("Hunger Overhaul Reborn Fabric 1.21.1 initialization complete");
        } catch (Exception e) {
            HOReborn.LOGGER.error("Failed to initialize Hunger Overhaul Reborn for Fabric 1.21.1", e);
        }
    }

    private void registerFabricEvents() {
        try {
            // Register server lifecycle events using reflection
            Class<?> serverLifecycleEventsClass = Class.forName("net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents");
            serverLifecycleEventsClass.getMethod("SERVER_STARTING", Class.forName("net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarting"))
                                   .invoke(null, (Object) server -> HOReborn.LOGGER.info("Server starting..."));

            // Register player events using reflection
            Class<?> useItemCallbackClass = Class.forName("net.fabricmc.fabric.api.event.player.UseItemCallback");
            // Simplified event registration - full implementation would use proper callbacks

        } catch (Exception e) {
            HOReborn.LOGGER.warn("Some Fabric events could not be registered: " + e.getMessage());
        }
    }
}