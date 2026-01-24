package org.Netroaki.Main.applecore;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Events related to plant growth mechanics.
 */
public abstract class PlantGrowthEvent {

    /**
     * Fired when a plant is about to grow to allow modification of the growth process.
     *
     * This event is fired before plant growth occurs.
     *
     * This event can be canceled or modified via the result.
     */
    public static class AllowGrowthTick {
        public final Level level;
        public final BlockPos pos;
        public final BlockState state;
        public final BlockState newState;
        public final int age;
        public final int maxAge;

        public AllowGrowthTick(Level level, BlockPos pos, BlockState state, BlockState newState, int age, int maxAge) {
            this.level = level;
            this.pos = pos;
            this.state = state;
            this.newState = newState;
            this.age = age;
            this.maxAge = maxAge;
        }
    }

    /**
     * Fired when a plant has successfully grown.
     *
     * This event is fired after plant growth occurs.
     *
     * This event is not cancelable.
     */
    public static class PlantGrowth {
        public final Level level;
        public final BlockPos pos;
        public final BlockState oldState;
        public final BlockState newState;

        public PlantGrowth(Level level, BlockPos pos, BlockState oldState, BlockState newState) {
            this.level = level;
            this.pos = pos;
            this.oldState = oldState;
            this.newState = newState;
        }
    }

    // Event instances for firing
    public static final Event<AllowGrowthTick> ALLOW_GROWTH_TICK = EventFactory.createLoop();
    public static final Event<PlantGrowth> PLANT_GROWTH = EventFactory.createLoop();
}
