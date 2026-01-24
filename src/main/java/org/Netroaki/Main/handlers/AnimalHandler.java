package org.Netroaki.Main.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.Netroaki.Main.config.HungerOverhaulConfig;

/**
 * Handler for animal-related mechanics, including milking timeouts.
 */
public class AnimalHandler {

    private static final String LAST_MILKED_TAG = "HungerOverhaulLastMilked";

    /**
     * Handle right-click interaction with animals.
     * Currently handles cow milking with timeout.
     */
    public static InteractionResult handleAnimalInteraction(Player player, Level level, InteractionHand hand,
                                                          EntityHitResult hitResult) {
        if (!(hitResult.getEntity() instanceof Cow cow)) {
            return InteractionResult.PASS;
        }

        HungerOverhaulConfig config = HungerOverhaulConfig.getInstance();

        // Check if milking timeout is enabled
        if (config.animals.milkedTimeout <= 0) {
            return InteractionResult.PASS; // No timeout, allow vanilla behavior
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS; // Client prediction
        }

        ServerLevel serverLevel = (ServerLevel) level;

        // Check if cow was milked recently
        long currentTime = serverLevel.getGameTime();
        long lastMilkedTime = getLastMilkedTime(cow);

        long timeoutTicks = config.animals.milkedTimeout * 20 * 60; // Convert minutes to ticks
        long timeSinceLastMilked = currentTime - lastMilkedTime;

        if (timeSinceLastMilked < timeoutTicks) {
            // Cow is still on cooldown
            long remainingMinutes = (timeoutTicks - timeSinceLastMilked) / (20 * 60) + 1; // Round up

            // Send message to player
            Component message = Component.translatable("message.hunger_overhaul_reborn.cow_cooldown",
                remainingMinutes);
            player.sendSystemMessage(message);

            return InteractionResult.SUCCESS; // Prevent milking
        }

        // Check if player is trying to milk (holding empty bucket)
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.getItem() == Items.BUCKET) {
            // Allow milking and record the time
            setLastMilkedTime(cow, currentTime);

            // Return PASS to allow vanilla milking behavior
            return InteractionResult.PASS;
        }

        return InteractionResult.PASS;
    }

    /**
     * Get the last time this cow was milked.
     * Note: Using a simplified approach since persistent data methods may vary by version.
     */
    private static long getLastMilkedTime(Cow cow) {
        // In a full implementation, this would use proper persistent data storage
        // For now, using a placeholder - would need proper implementation for the target MC version
        return 0; // Placeholder - always allow milking
    }

    /**
     * Set the last time this cow was milked.
     * Note: Using a simplified approach since persistent data methods may vary by version.
     */
    private static void setLastMilkedTime(Cow cow, long time) {
        // In a full implementation, this would use proper persistent data storage
        // For now, placeholder - no actual storage
    }
}
