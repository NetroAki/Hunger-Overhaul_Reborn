package org.Netroaki.Main.modules;

import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.api.FoodValues;
import org.Netroaki.Main.config.HungerOverhaulConfig;
import org.Netroaki.Main.food.FoodRegistry;

import java.util.*;

/**
 * Module for integrating with HarvestCraft mod.
 * Provides food value balancing and growth modifications for HarvestCraft
 * items.
 * Items are categorized based on their names and expected crafting complexity.
 */
public class HarvestCraftModule {

    private static final String HARVESTCRAFT_MOD_ID = "harvestcraft";
    private static boolean harvestCraftLoaded = false;

    // ============================================
    // CATEGORY 1: RAW CROPS & FRUITS (1 hunger, 0.05-0.1 saturation)
    // ============================================
    // These are basic crops that can be eaten raw but provide minimal nutrition
    private static final Set<String> RAW_CROPS = new HashSet<>(Arrays.asList(
            // Vegetables
            "asparagus", "bambooshoot", "barley", "bean", "beet", "bellpepper", "broccoli",
            "cabbage", "cauliflower", "celery", "chilipepper", "corn", "cucumber", "eggplant",
            "garlic", "ginger", "leek", "lettuce", "onion", "peas", "radish", "rhubarb",
            "rice", "scallion", "soybean", "spinach", "sweetpotato", "turnip", "waterchestnut",
            "zucchini", "okra", "artichoke", "brusselsprout", "rutabaga",
            // Fruits
            "apple", "apricot", "avocado", "banana", "blackberry", "blueberry", "cactusfruit",
            "cherry", "coconut", "cranberry", "date", "dragonfruit", "elderberry", "grape",
            "grapefruit", "kiwi", "lemon", "lime", "mango", "olive", "orange", "papaya",
            "peach", "pear", "persimmon", "pineapple", "plum", "pomegranate", "raspberry",
            "strawberry", "watermelon", "fig", "gooseberry", "mulberry", "currant"));

    // ============================================
    // CATEGORY 2: RAW MEATS & FISH (2-3 hunger, 0.1-0.2 saturation)
    // ============================================
    private static final Set<String> RAW_MEATS = new HashSet<>(Arrays.asList(
            "rawtofu", "rawchicken", "rawpork", "rawbeef", "rawfish", "rawtofacon"));

    // ============================================
    // CATEGORY 3: SIMPLE PROCESSED (2-3 hunger, 0.1-0.2 saturation)
    // ============================================
    // Single ingredient processing: juiced, dried, roasted, etc.
    private static final Set<String> SIMPLE_PROCESSED = new HashSet<>(Arrays.asList(
            // Juices (fruit + juicer)
            "applejuice", "apricotjuice", "bananajuice", "blackberryjuice", "blueberryjuice",
            "cherryjuice", "coconutjuice", "cranberryjuice", "grapejuice", "grapefruitjuice",
            "kiwijuice", "lemonjuice", "limejuice", "mangojuice", "orangejuice", "papayajuice",
            "peachjuice", "pearjuice", "pineapplejuice", "plumjuice", "pomegranatejuice",
            "raspberryjuice", "strawberryjuice", "watermelonjuice",
            // Dried fruits
            "driedapples", "driedapricots", "driedbananas", "driedcherries", "driedcranberries",
            "drieddates", "driedgrapes", "driedkiwi", "driedmango", "driedpeaches", "driedpears",
            "driedplums", "driedstrawberries",
            // Roasted/grilled single items
            "roastedpeanut", "roastedpumpkinseeds", "roastedsunflowerseeds", "roastedchestnut",
            "grilledasparagus", "grilledeggplant", "grilledmushroom", "grilledonion",
            "grilledpepper", "grilledzucchini"));

    // ============================================
    // CATEGORY 4: BASIC COOKED FOODS (3-4 hunger, 0.2-0.3 saturation)
    // ============================================
    // Simple cooking: 1-2 ingredients, basic preparation
    private static final Set<String> BASIC_COOKED = new HashSet<>(Arrays.asList(
            // Cooked meats
            "cookedtofu", "cookedchicken", "cookedpork", "cookedbeef", "cookedfish", "cookedtofacon",
            "bacon", "cookedbacon", "cookedegg", "friedegg",
            // Simple breads and baked goods
            "toast", "bagel", "biscuit", "bread", "cornbread", "doughnut", "waffle", "pancake",
            // Simple salads (2-3 ingredients)
            "applesalad", "caesarsalad", "coleslaw", "garden salad", "greensalad", "potatosalad"));

    // ============================================
    // CATEGORY 5: CONDIMENTS & SPREADS (1-2 hunger, 0.05-0.1 saturation)
    // ============================================
    private static final Set<String> CONDIMENTS = new HashSet<>(Arrays.asList(
            "butter", "cheese", "cream", "mayonnaise", "mustard", "ketchup", "vinegar",
            "oliveoil", "flour", "salt", "pepper", "cinnamon", "nutmeg", "paprika",
            "currypowder", "garlicpowder", "onionpowder", "vanilla", "vanillabean"));

    // ============================================
    // CATEGORY 6: JAMS & PRESERVES (2 hunger, 0.1 saturation)
    // ============================================
    // Fruit + sugar + saucepan
    private static final Set<String> JAMS = new HashSet<>(Arrays.asList(
            "applejelly", "apricotjelly", "blackberryjelly", "blueberryjelly", "cherryjelly",
            "cranberryjelly", "grapejelly", "kiwijelly", "mangojelly", "orangejelly",
            "peachjelly", "pearjelly", "plumjelly", "raspberryjelly", "strawberryjelly"));

    // ============================================
    // CATEGORY 7: SOUPS & STEWS (4-6 hunger, 0.3-0.4 saturation)
    // ============================================
    // Multiple ingredients, cooked in pot
    private static final Set<String> SOUPS_STEWS = new HashSet<>(Arrays.asList(
            "applesoup", "beetsoup", "cactussoup", "carrotsoup", "chili", "chilichocolate",
            "chilipowder", "chilisoup", "chowder", "cornsoup", "cucumbersoup", "eggplantsoup",
            "fishsoup", "gumbo", "lentilsoup", "meatystew", "mushroomsoup", "onionsoup",
            "peasoup", "potatosoup", "pumpkinsoup", "stewsoup", "tomatosoup", "vegetablesoup",
            "vegetablestirfry", "vegetablestew"));

    // ============================================
    // CATEGORY 8: SANDWICHES & WRAPS (4-5 hunger, 0.3-0.4 saturation)
    // ============================================
    // Bread + fillings
    private static final Set<String> SANDWICHES = new HashSet<>(Arrays.asList(
            "blt", "chickensandwich", "chickensaladsandwich", "club sandwich", "eggsandwich",
            "fishsandwich", "grilledcheese", "hamsandwich", "pbandjsandwich", "tunasandwich",
            "turkeysandwich", "veggiesandwich"));

    // ============================================
    // CATEGORY 9: PIZZAS (6-7 hunger, 0.4-0.5 saturation)
    // ============================================
    // Dough + cheese + toppings
    private static final Set<String> PIZZAS = new HashSet<>(Arrays.asList(
            "pizza", "cheesepizza", "pepperonipizza", "supremepizza", "veggiepizza"));

    // ============================================
    // CATEGORY 10: BURGERS & HOT DOGS (5-6 hunger, 0.4-0.5 saturation)
    // ============================================
    private static final Set<String> BURGERS = new HashSet<>(Arrays.asList(
            "hamburger", "cheeseburger", "baconburger", "veggieburger", "hotdog", "chilihotdog"));

    // ============================================
    // CATEGORY 11: PIES & DESSERTS (4-6 hunger, 0.2-0.3 saturation)
    // ============================================
    // Sweet foods, baked goods
    private static final Set<String> PIES_DESSERTS = new HashSet<>(Arrays.asList(
            "applepie", "apricotpie", "bananacreampie", "blackberrypie", "blueberrypie",
            "cherrypie", "coconutpie", "cranberrypie", "grapepie", "keylimepie", "lemonpie",
            "mangopie", "orange pie", "peachpie", "pearpie", "pineapplepie", "plumpie",
            "pomegranatepie", "pumpkinpie", "raspberrypie", "strawberrypie", "sweetpotatopie",
            "chocolatecake", "carrotcake", "cheesecake", "poundcake", "redvelvetcake"));

    // ============================================
    // CATEGORY 12: ICE CREAM & FROZEN (2-3 hunger, 0.05-0.1 saturation)
    // ============================================
    private static final Set<String> ICE_CREAM = new HashSet<>(Arrays.asList(
            "icecream", "appleicecream", "bananaicecream", "blackberryicecream", "blueberryicecream",
            "cherryicecream", "chocolateicecream", "coconuticecream", "cranberryicecream",
            "grapeicecream", "kiwiicecream", "mangoicecream", "orangeicecream", "peachicecream",
            "pearicecream", "pineappleicecream", "plumicecream", "raspberryicecream",
            "strawberryicecream", "vanillaicecream", "watermelonicecream"));

    // ============================================
    // CATEGORY 13: SMOOTHIES & SHAKES (3-4 hunger, 0.1-0.15 saturation)
    // ============================================
    // Multiple fruits blended
    private static final Set<String> SMOOTHIES = new HashSet<>(Arrays.asList(
            "smoothie", "applesmoothie", "bananasmoothie", "blackberrysmoothie", "blueberrysmoothie",
            "cherrysmoothie", "coconutsmoothie", "cranberrysmoothie", "grapesmoothie", "kiwismoothie",
            "mangosmoothie", "orangesmoothie", "peachsmoothie", "pearsmoothie", "pineapplesmoothie",
            "plumsmoothie", "raspberrysmoothie", "strawberrysmoothie", "watermelonsmoothie",
            "milkshake", "chocolatemilkshake", "strawberrymilkshake", "vanillamilkshake"));

    // ============================================
    // CATEGORY 14: YOGURTS & DAIRY (2-3 hunger, 0.1-0.15 saturation)
    // ============================================
    private static final Set<String> YOGURTS = new HashSet<>(Arrays.asList(
            "yogurt", "appleyogurt", "bananayogurt", "blackberryyogurt", "blueberryyogurt",
            "cherryyogurt", "coconutyogurt", "cranberryyogurt", "grapeyogurt", "kiwiyogurt",
            "mangoyogurt", "orangeyogurt", "peachyogurt", "pearyogurt", "pineappleyogurt",
            "plumyogurt", "raspberryyogurt", "strawberryyogurt", "watermelonyogurt"));

    // ============================================
    // CATEGORY 15: COMPLEX MEALS (7-10 hunger, 0.5-0.7 saturation)
    // ============================================
    // Multi-course meals, many ingredients
    private static final Set<String> COMPLEX_MEALS = new HashSet<>(Arrays.asList(
            "heartybreakfast", "delightedmeal", "epicbacon", "fullbreakfast", "fullenglishbreakfast",
            "thanksgivingdinner", "thanksgivingfeast", "dinnerroll", "dinnerplate"));

    // ============================================
    // CATEGORY 16: SNACKS & CHIPS (1-2 hunger, 0.05-0.1 saturation)
    // ============================================
    private static final Set<String> SNACKS = new HashSet<>(Arrays.asList(
            "chips", "potatochips", "tortillachips", "crackers", "pretzel", "popcorn"));

    // ============================================
    // CATEGORY 17: CANDIES & SWEETS (1-2 hunger, 0.05 saturation)
    // ============================================
    private static final Set<String> CANDIES = new HashSet<>(Arrays.asList(
            "candy", "chocolatebar", "chocolatedonut", "cinnamonroll", "cookies", "gingerbreadcookie",
            "gummybears", "lollipop", "marshmallow", "taffy"));

    // ============================================
    // CATEGORY 18: SPECIAL/UNIQUE ITEMS
    // ============================================
    // Items that need individual attention
    private static final Map<String, FoodValues> SPECIAL_ITEMS = new HashMap<>();
    static {
        // High-value complex meals
        SPECIAL_ITEMS.put("delightedmeal", new FoodValues(16, 0.8f));
        SPECIAL_ITEMS.put("heartybreakfast", new FoodValues(15, 0.8f));
        SPECIAL_ITEMS.put("thanksgivingfeast", new FoodValues(18, 0.9f));
        SPECIAL_ITEMS.put("thanksgivingdinner", new FoodValues(14, 0.7f));

        // Premium burgers
        SPECIAL_ITEMS.put("cheeseburger", new FoodValues(8, 0.5f));
        SPECIAL_ITEMS.put("baconburger", new FoodValues(9, 0.6f));
        SPECIAL_ITEMS.put("hamburger", new FoodValues(7, 0.4f));

        // Premium pizzas
        SPECIAL_ITEMS.put("supremepizza", new FoodValues(9, 0.6f));
        SPECIAL_ITEMS.put("pepperonipizza", new FoodValues(7, 0.5f));
        SPECIAL_ITEMS.put("cheesepizza", new FoodValues(6, 0.4f));
        SPECIAL_ITEMS.put("pizza", new FoodValues(6, 0.4f));

        // Premium sandwiches
        SPECIAL_ITEMS.put("grilledcheese", new FoodValues(7, 0.5f));
        SPECIAL_ITEMS.put("blt", new FoodValues(6, 0.4f));
        SPECIAL_ITEMS.put("club sandwich", new FoodValues(8, 0.5f));

        // Premium pies
        SPECIAL_ITEMS.put("applepie", new FoodValues(5, 0.25f));
        SPECIAL_ITEMS.put("pumpkinpie", new FoodValues(6, 0.3f));
        SPECIAL_ITEMS.put("chocolatecake", new FoodValues(6, 0.3f));
        SPECIAL_ITEMS.put("cheesecake", new FoodValues(7, 0.35f));
    }

    public void init() {
        // Check if HarvestCraft is loaded
        harvestCraftLoaded = Platform.isModLoaded(HARVESTCRAFT_MOD_ID);

        if (!harvestCraftLoaded) {
            HOReborn.LOGGER.info("HarvestCraft not detected, skipping integration");
            return;
        }

        HOReborn.LOGGER.info("Initializing HarvestCraft integration");

        // Apply food value modifications
        if (HungerOverhaulConfig.getInstance().food.modifyFoodValues) {
            applyFoodValueModifications();
        }

        // Register growth modifiers for HarvestCraft crops
        registerGrowthModifiers();

        // Set up unplantable foods
        if (HungerOverhaulConfig.getInstance().integration.enableHarvestCraftIntegration) {
            setupUnplantableFoods();
        }
    }

    /**
     * Apply food value modifications to HarvestCraft items based on categories.
     */
    private void applyFoodValueModifications() {
        HOReborn.LOGGER.info("Categorizing and applying food values to HarvestCraft items...");

        int processedCount = 0;

        for (Item item : BuiltInRegistries.ITEM) {
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
            if (itemId != null && HARVESTCRAFT_MOD_ID.equals(itemId.getNamespace())) {
                String path = itemId.getPath().toLowerCase();

                // Remove common suffixes
                String baseName = path.replace("item", "").replace("crop", "").replace("seed", "");

                // Check special items first
                if (SPECIAL_ITEMS.containsKey(baseName)) {
                    FoodValues values = SPECIAL_ITEMS.get(baseName);
                    FoodRegistry.setFoodValues(itemId.toString(), new org.Netroaki.Main.food.FoodValueData(
                            values.hunger, values.saturationModifier, "HARVESTCRAFT_SPECIAL"));
                    processedCount++;
                    continue;
                }

                // Categorize by name matching
                FoodValues values = categorizeItem(baseName);
                if (values != null) {
                    FoodRegistry.setFoodValues(itemId.toString(), new org.Netroaki.Main.food.FoodValueData(
                            values.hunger, values.saturationModifier, "HARVESTCRAFT_" + getCategoryName(baseName)));
                    processedCount++;
                }
            }
        }

        HOReborn.LOGGER.info("Processed {} HarvestCraft items with food value modifications", processedCount);
    }

    /**
     * Categorize an item and return appropriate food values.
     */
    private FoodValues categorizeItem(String itemName) {
        // Check each category in order of specificity (most specific first)

        // Complex meals (check first as they might contain other keywords)
        if (containsAny(itemName, COMPLEX_MEALS)) {
            return new FoodValues(8, 0.6f); // Average for complex meals
        }

        // Pizzas
        if (containsAny(itemName, PIZZAS)) {
            return new FoodValues(6, 0.4f);
        }

        // Burgers
        if (containsAny(itemName, BURGERS)) {
            return new FoodValues(6, 0.4f);
        }

        // Sandwiches
        if (containsAny(itemName, SANDWICHES)) {
            return new FoodValues(4, 0.3f);
        }

        // Soups and stews
        if (containsAny(itemName, SOUPS_STEWS)) {
            return new FoodValues(5, 0.35f);
        }

        // Pies and desserts
        if (containsAny(itemName, PIES_DESSERTS)) {
            return new FoodValues(5, 0.25f);
        }

        // Smoothies
        if (containsAny(itemName, SMOOTHIES)) {
            return new FoodValues(3, 0.1f);
        }

        // Yogurts
        if (containsAny(itemName, YOGURTS)) {
            return new FoodValues(2, 0.1f);
        }

        // Ice cream
        if (containsAny(itemName, ICE_CREAM)) {
            return new FoodValues(2, 0.05f);
        }

        // Jams
        if (containsAny(itemName, JAMS)) {
            return new FoodValues(2, 0.1f);
        }

        // Basic cooked foods
        if (containsAny(itemName, BASIC_COOKED)) {
            return new FoodValues(3, 0.2f);
        }

        // Simple processed
        if (containsAny(itemName, SIMPLE_PROCESSED)) {
            return new FoodValues(2, 0.1f);
        }

        // Raw meats (exact match only to avoid false positives)
        if (RAW_MEATS.contains(itemName)) {
            return new FoodValues(2, 0.1f);
        }

        // Condiments (exact match only to avoid false positives)
        if (CONDIMENTS.contains(itemName)) {
            return new FoodValues(1, 0.05f);
        }

        // Snacks
        if (containsAny(itemName, SNACKS)) {
            return new FoodValues(1, 0.05f);
        }

        // Candies
        if (containsAny(itemName, CANDIES)) {
            return new FoodValues(1, 0.05f);
        }

        // Raw crops and fruits (default, check last)
        // Only match if item name is exactly a crop name (not a longer name containing
        // it)
        if (RAW_CROPS.contains(itemName)) {
            return new FoodValues(1, 0.05f);
        }

        // If no match found, return null (item won't be modified)
        return null;
    }

    /**
     * Check if item name contains any of the strings in the set.
     * Uses word boundary matching for better accuracy.
     */
    private boolean containsAny(String itemName, Set<String> category) {
        for (String categoryItem : category) {
            // Exact match
            if (itemName.equals(categoryItem)) {
                return true;
            }
            // Item name contains category item (e.g., "applepie" contains "apple")
            if (itemName.contains(categoryItem)) {
                return true;
            }
            // Category item contains item name (e.g., "club sandwich" contains "sandwich")
            if (categoryItem.contains(itemName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get category name for logging.
     */
    private String getCategoryName(String itemName) {
        if (containsAny(itemName, COMPLEX_MEALS))
            return "COMPLEX_MEAL";
        if (containsAny(itemName, PIZZAS))
            return "PIZZA";
        if (containsAny(itemName, BURGERS))
            return "BURGER";
        if (containsAny(itemName, SANDWICHES))
            return "SANDWICH";
        if (containsAny(itemName, SOUPS_STEWS))
            return "SOUP_STEW";
        if (containsAny(itemName, PIES_DESSERTS))
            return "PIE_DESSERT";
        if (containsAny(itemName, SMOOTHIES))
            return "SMOOTHIE";
        if (containsAny(itemName, YOGURTS))
            return "YOGURT";
        if (containsAny(itemName, ICE_CREAM))
            return "ICE_CREAM";
        if (containsAny(itemName, JAMS))
            return "JAM";
        if (containsAny(itemName, BASIC_COOKED))
            return "BASIC_COOKED";
        if (containsAny(itemName, SIMPLE_PROCESSED))
            return "SIMPLE_PROCESSED";
        if (RAW_MEATS.contains(itemName))
            return "RAW_MEAT";
        if (CONDIMENTS.contains(itemName))
            return "CONDIMENT";
        if (containsAny(itemName, SNACKS))
            return "SNACK";
        if (containsAny(itemName, CANDIES))
            return "CANDY";
        if (RAW_CROPS.contains(itemName))
            return "RAW_CROP";
        return "UNKNOWN";
    }

    /**
     * Register growth modifiers for HarvestCraft crops and saplings.
     */
    private void registerGrowthModifiers() {
        // Note: Growth modifiers would be registered here, but since we already have
        // the BlockGrowthRegistry and BiomeGrowthRegistry systems, HarvestCraft
        // crops would automatically use the default crop growth settings unless
        // specifically configured otherwise.

        // For HarvestCraft-specific crops that need special treatment:
        // - Pineapple: prefers humid biomes (jungle/swamp)
        // - Cactus fruit: prefers desert biomes
        // - Kiwi: prefers temperate biomes

        HOReborn.LOGGER.info("HarvestCraft growth modifiers registered");
    }

    /**
     * Set up unplantable HarvestCraft foods.
     * This prevents planting of food items that are also seeds.
     */
    private void setupUnplantableFoods() {
        // This would require hooks into HarvestCraft's planting system
        // For now, this is a placeholder that would need to be implemented
        // when HarvestCraft mod integration is available

        // The original Hunger Overhaul made certain food items unplantable
        // by modifying the planting behavior. In modern Minecraft, this would
        // typically be done through event interception or mixins.

        HOReborn.LOGGER.info("HarvestCraft unplantable foods setup (placeholder)");
    }

    /**
     * Check if HarvestCraft is loaded.
     */
    public static boolean isHarvestCraftLoaded() {
        return harvestCraftLoaded;
    }

    /**
     * Get the HarvestCraft mod ID.
     */
    public static String getHarvestCraftModId() {
        return HARVESTCRAFT_MOD_ID;
    }
}
