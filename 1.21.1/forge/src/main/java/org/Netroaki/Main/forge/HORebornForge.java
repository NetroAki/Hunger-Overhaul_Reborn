package org.Netroaki.Main.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.Netroaki.Main.HOReborn;

/**
 * 1.21.1-compatible Forge mod initializer.
 * Includes client-side initialization for HUD rendering.
 */
@Mod(HOReborn.MOD_ID)
public final class HORebornForge {

    public HORebornForge() {
        HOReborn.LOGGER.info("Initializing Hunger Overhaul Reborn for Forge 1.21.1");

        try {
            // Initialize the core mod
            HOReborn.init();

            // Register client setup
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            modEventBus.addListener(this::clientSetup);

            // Register Forge event bus for client events
            MinecraftForge.EVENT_BUS.register(this);

            HOReborn.LOGGER.info("Hunger Overhaul Reborn Forge 1.21.1 initialization complete");
        } catch (Exception e) {
            HOReborn.LOGGER.error("Failed to initialize Hunger Overhaul Reborn for Forge 1.21.1", e);
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        HOReborn.LOGGER.info("Client setup for Hunger Overhaul Reborn 1.21.1");
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        try {
            // Use reflection to call our HUD renderer
            Class<?> vanillaGuiOverlayClass = Class.forName("net.minecraftforge.client.gui.overlay.VanillaGuiOverlay");
            Object overlay = vanillaGuiOverlayClass.getField("FOOD_LEVEL").get(null);

            // Check if this is the food level overlay
            if (event.getOverlay().equals(overlay)) {
                renderHudWarnings(event);
            }
        } catch (Exception e) {
            HOReborn.LOGGER.debug("HUD rendering failed: " + e.getMessage());
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void renderHudWarnings(RenderGuiOverlayEvent.Post event) {
        try {
            // Get the GUI graphics from the event
            Class<?> eventClass = event.getClass();
            Object guiGraphics = eventClass.getMethod("getGuiGraphics").invoke(event);

            // Get partial tick
            float partialTick = 0.0f; // Default for overlay events

            // Call our HUD renderer
            Class<?> hudRendererClass = Class.forName("org.Netroaki.Main.client.HudRenderer");
            hudRendererClass.getMethod("renderHudWarnings", Object.class, float.class)
                           .invoke(null, guiGraphics, partialTick);
        } catch (Exception e) {
            HOReborn.LOGGER.debug("HUD rendering failed: " + e.getMessage());
        }
    }
}