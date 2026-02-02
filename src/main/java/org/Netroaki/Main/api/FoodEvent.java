package org.Netroaki.Main.api;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Base class for all FoodEvent events.
 *
 * All children of this event are fired using Architectury events.
 */
public abstract class FoodEvent {

    /**
     * Fired every time food values are retrieved to allow player-independent
     * control over their values.
     *
     * This event is fired when food values are retrieved.
     *
     * foodValues contains the values of the food.
     * unmodifiedFoodValues contains the food values of the food before the
     * GetFoodValues event was fired.
     * food contains the food in question.
     *
     * This event is not cancelable.
     */
    public static class GetFoodValues {
        public FoodValues foodValues;
        public final FoodValues unmodifiedFoodValues;
        public final ItemStack food;

        public GetFoodValues(ItemStack itemStack, FoodValues foodValues) {
            this.food = itemStack;
            this.foodValues = foodValues;
            this.unmodifiedFoodValues = foodValues;
        }
    }

    /**
     * Fired every time food values are retrieved to allow player-dependent control
     * over their values.
     * This event will always be preceded by GetFoodValues being fired.
     *
     * This event is fired when food values are retrieved for a player.
     *
     * player contains the player.
     * foodValues contains the values of the food.
     * unmodifiedFoodValues contains the food values of the food before the
     * GetFoodValues event was fired.
     * food contains the food in question.
     *
     * This event is not cancelable.
     */
    public static class GetPlayerFoodValues {
        public FoodValues foodValues;
        public final FoodValues unmodifiedFoodValues;
        public final ItemStack food;
        public final Player player;

        public GetPlayerFoodValues(Player player, ItemStack itemStack, FoodValues foodValues) {
            this.player = player;
            this.food = itemStack;
            this.foodValues = foodValues;
            this.unmodifiedFoodValues = foodValues;
        }
    }

    /**
     * Fired after food is eaten, containing the effects and context for the food
     * that was eaten.
     *
     * This event is fired after food consumption.
     *
     * This event is not cancelable.
     */
    public static class FoodEaten {
        public final FoodValues foodValues;
        public final int hungerAdded;
        public final float saturationAdded;
        public final ItemStack food;
        public final Player player;

        public FoodEaten(Player player, ItemStack itemStack, FoodValues foodValues,
                int hungerAdded, float saturationAdded) {
            this.player = player;
            this.food = itemStack;
            this.foodValues = foodValues;
            this.hungerAdded = hungerAdded;
            this.saturationAdded = saturationAdded;
        }
    }

    /**
     * Fired when hunger/saturation is added to a player's FoodStats.
     *
     * This event is fired when hunger and saturation are added to FoodStats.
     *
     * This event is cancelable.
     * If this event is canceled, the hunger and saturation of the FoodStats will
     * not change.
     */
    public static class FoodStatsAddition {
        public final FoodValues foodValuesToBeAdded;
        public final Player player;
        public boolean canceled = false;

        public FoodStatsAddition(Player player, FoodValues foodValuesToBeAdded) {
            this.player = player;
            this.foodValuesToBeAdded = foodValuesToBeAdded;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }

        public boolean isCanceled() {
            return canceled;
        }
    }

    // Event instances for firing
    public static final Event<GetFoodValues> GET_FOOD_VALUES = EventFactory.createLoop();
    public static final Event<GetPlayerFoodValues> GET_PLAYER_FOOD_VALUES = EventFactory.createLoop();
    public static final Event<FoodEaten> FOOD_EATEN = EventFactory.createLoop();
    public static final Event<FoodStatsAddition> FOOD_STATS_ADDITION = EventFactory.createLoop();
}
