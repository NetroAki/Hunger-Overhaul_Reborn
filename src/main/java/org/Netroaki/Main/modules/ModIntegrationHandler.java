package org.Netroaki.Main.modules;

import org.Netroaki.Main.HOReborn;
import org.Netroaki.Main.modules.integration.AetherModule;
import org.Netroaki.Main.modules.integration.AlexsCavesModule;
import org.Netroaki.Main.modules.integration.AlexsMobsModule;
import org.Netroaki.Main.modules.integration.AquacultureModule;
import org.Netroaki.Main.modules.integration.BetterEndModule;
import org.Netroaki.Main.modules.integration.BetterNetherModule;
import org.Netroaki.Main.modules.integration.BiomesOPlentyModule;
import org.Netroaki.Main.modules.integration.BiomesWeveGoneModule;
import org.Netroaki.Main.modules.integration.BornInChaosModule;
import org.Netroaki.Main.modules.integration.CataclysmModule;
import org.Netroaki.Main.modules.integration.CookingForBlockheadsModule;
import org.Netroaki.Main.modules.integration.CreateModule;
import org.Netroaki.Main.modules.integration.CreateFoodModule;
import org.Netroaki.Main.modules.integration.CreateGourmetModule;
import org.Netroaki.Main.modules.integration.DeeperDarkerModule;
import org.Netroaki.Main.modules.integration.DelightfulModule;
import org.Netroaki.Main.modules.integration.DivineRPGModule;
import org.Netroaki.Main.modules.integration.FarmersDelightModule;
import org.Netroaki.Main.modules.integration.EternalTalesModule;
import org.Netroaki.Main.modules.integration.IceAndFireModule;
import org.Netroaki.Main.modules.integration.MineColoniesModule;
import org.Netroaki.Main.modules.integration.MowziesMobsModule;
import org.Netroaki.Main.modules.integration.NaturaModule;
import org.Netroaki.Main.modules.integration.OhTheTreesModule;
import org.Netroaki.Main.modules.integration.PamsModsModule;
import org.Netroaki.Main.modules.integration.ProductiveBeesModule;
import org.Netroaki.Main.modules.integration.ProductiveTreesModule;
import org.Netroaki.Main.modules.integration.QuarkModule;
import org.Netroaki.Main.modules.integration.RegionsUnexploredModule;
import org.Netroaki.Main.modules.integration.ReliquaryModule;
import org.Netroaki.Main.modules.integration.SolCarrotModule;
import org.Netroaki.Main.modules.integration.ThermalCultivationModule;
import org.Netroaki.Main.modules.integration.TheAbyssModule;
import org.Netroaki.Main.modules.integration.TinkersConstructModule;
import org.Netroaki.Main.modules.integration.TwilightForestModule;
import org.Netroaki.Main.modules.integration.AutoDiscoveryFoodModule;
import org.Netroaki.Main.modules.integration.AssemblyRequiredModule;

import java.util.HashSet;
import java.util.Set;

/**
 * Master handler for all mod integrations.
 * Manages initialization of individual mod integration modules.
 * Also handles auto-discovery of unsupported food mods.
 */
public class ModIntegrationHandler {

    // Integration modules
    private final BiomesOPlentyModule bopModule = new BiomesOPlentyModule();
    private final BiomesWeveGoneModule biomesWeveGoneModule = new BiomesWeveGoneModule();
    private final BornInChaosModule bornInChaosModule = new BornInChaosModule();
    private final TinkersConstructModule tconModule = new TinkersConstructModule();
    private final NaturaModule naturaModule = new NaturaModule();
    private final PamsModsModule pamsModule = new PamsModsModule();
    private final HarvestCraftModule harvestCraftModule = new HarvestCraftModule();
    private final FarmersDelightModule farmersDelightModule = new FarmersDelightModule();
    private final DeeperDarkerModule deeperDarkerModule = new DeeperDarkerModule();
    private final DelightfulModule delightfulModule = new DelightfulModule();
    private final AquacultureModule aquacultureModule = new AquacultureModule();
    private final ThermalCultivationModule thermalCultivationModule = new ThermalCultivationModule();
    private final CreateModule createModule = new CreateModule();
    private final CreateFoodModule createFoodModule = new CreateFoodModule();
    private final CreateGourmetModule createGourmetModule = new CreateGourmetModule();
    private final SolCarrotModule solCarrotModule = new SolCarrotModule();
    private final IceAndFireModule iceAndFireModule = new IceAndFireModule();
    private final ProductiveBeesModule productiveBeesModule = new ProductiveBeesModule();
    private final ProductiveTreesModule productiveTreesModule = new ProductiveTreesModule();
    private final QuarkModule quarkModule = new QuarkModule();
    private final RegionsUnexploredModule regionsUnexploredModule = new RegionsUnexploredModule();
    private final TwilightForestModule twilightForestModule = new TwilightForestModule();
    private final AlexsMobsModule alexsMobsModule = new AlexsMobsModule();
    private final AlexsCavesModule alexsCavesModule = new AlexsCavesModule();
    private final CookingForBlockheadsModule cookingForBlockheadsModule = new CookingForBlockheadsModule();
    private final DivineRPGModule divineRPGModule = new DivineRPGModule();
    private final BetterEndModule betterEndModule = new BetterEndModule();
    private final BetterNetherModule betterNetherModule = new BetterNetherModule();
    private final AetherModule aetherModule = new AetherModule();
    private final MowziesMobsModule mowziesMobsModule = new MowziesMobsModule();
    private final MineColoniesModule mineColoniesModule = new MineColoniesModule();
    private final OhTheTreesModule ohTheTreesModule = new OhTheTreesModule();
    private final ReliquaryModule reliquaryModule = new ReliquaryModule();
    private final CataclysmModule cataclysmModule = new CataclysmModule();
    private final EternalTalesModule eternalTalesModule = new EternalTalesModule();
    private final TheAbyssModule theAbyssModule = new TheAbyssModule();
    private final AssemblyRequiredModule assemblyRequiredModule = new AssemblyRequiredModule();
    private final AutoDiscoveryFoodModule autoDiscoveryModule = new AutoDiscoveryFoodModule();

    private static boolean initialized = false;
    private Set<String> explicitModIds = new HashSet<>();

    /**
     * Initialize all mod integrations.
     */
    public void init() {
        if (initialized) return;

        HOReborn.LOGGER.info("Initializing mod integrations...");

        // Initialize individual mod integrations
        bopModule.init();
        biomesWeveGoneModule.init();
        bornInChaosModule.init();
        tconModule.init();
        naturaModule.init();
        pamsModule.init();
        harvestCraftModule.init();
        farmersDelightModule.init();
        deeperDarkerModule.init();
        delightfulModule.init();
        aquacultureModule.init();
        thermalCultivationModule.init();
        createModule.init();
        createFoodModule.init();
        createGourmetModule.init();
        solCarrotModule.init();
        iceAndFireModule.init();
        eternalTalesModule.init();
        productiveBeesModule.init();
        productiveTreesModule.init();
        quarkModule.init();
        regionsUnexploredModule.init();
        twilightForestModule.init();
        alexsMobsModule.init();
        alexsCavesModule.init();
        cookingForBlockheadsModule.init();
        divineRPGModule.init();
        betterEndModule.init();
        betterNetherModule.init();
        aetherModule.init();
        mowziesMobsModule.init();
        mineColoniesModule.init();
        ohTheTreesModule.init();
        reliquaryModule.init();
        cataclysmModule.init();
        theAbyssModule.init();
        assemblyRequiredModule.init();

        // Populate set of explicitly supported mods
        populateExplicitModIds();

        // Run auto-discovery for unsupported food mods
        autoDiscoveryModule.init(explicitModIds);

        // Log integration status
        logIntegrationStatus();

        initialized = true;
        HOReborn.LOGGER.info("Mod integrations initialized");
    }

    /**
     * Populate the set of explicitly supported mod IDs.
     * Used by auto-discovery to avoid processing mods that already have explicit modules.
     */
    private void populateExplicitModIds() {
        explicitModIds.addAll(java.util.Arrays.asList(
                "biomesoplenty", "biomesweevegone", "born_in_chaos_v1",
                "tconstruct", "natura", "pamhc2crops", "pamhc2trees",
                "pamhc2foodcore", "pamhc2foodextended", "harvestcraft",
                "farmersdelight", "deeperdarker", "delightful", "aquaculture",
                "thermal_cultivation", "create", "createfood", "creategourmet",
                "solcarrot", "iceandfire", "eternaltales", "productivebees",
                "productivetrees", "quark", "regions_unexplored", "twilightforest",
                "alexsmobs", "alexscaves", "cookingforblockheads", "divinerpg",
                "betterend", "betternether", "aether", "mowziesmobs",
                "minecolonies", "immersiveengineering", "reliquary", "cataclysm",
                "theabyss", "assemblytreasure"
        ));
    }

    /**
     * Log the status of all mod integrations.
     */
    private void logIntegrationStatus() {
        StringBuilder status = new StringBuilder("Mod Integration Status:\n");

        status.append("- Biomes O' Plenty: ").append(BiomesOPlentyModule.isBiomesOPlentyLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Biomes We've Gone: ").append(BiomesWeveGoneModule.isBiomesWeveGoneLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Born in Chaos: ").append(BornInChaosModule.isBornInChaosLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Tinkers' Construct: ").append(TinkersConstructModule.isTconstructLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Natura: ").append(NaturaModule.isNaturaLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Pam's Mods: ").append(PamsModsModule.isAnyPamsModsLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- HarvestCraft: ").append(HarvestCraftModule.isHarvestCraftLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Farmer's Delight: ").append(FarmersDelightModule.isFarmersDelightLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Deeper and Darker: ").append(DeeperDarkerModule.isDeeperDarkerLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Delightful: ").append(DelightfulModule.isDelightfulLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Aquaculture 2: ").append(AquacultureModule.isAquacultureLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Thermal Cultivation: ").append(ThermalCultivationModule.isThermalCultivationLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Create: ").append(CreateModule.isCreateLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Create: Food: ").append(CreateFoodModule.isCreateFoodLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Create Gourmet: ").append(CreateGourmetModule.isCreateGourmetLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Spice of Life: Carrot Edition: ").append(SolCarrotModule.isSolCarrotLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Ice and Fire: ").append(IceAndFireModule.isIceAndFireLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Eternal Tales: ").append(EternalTalesModule.isEternalTalesLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Productive Bees: ").append(ProductiveBeesModule.isProductiveBeesLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Productive Trees: ").append(ProductiveTreesModule.isProductiveTreesLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Quark: ").append(QuarkModule.isQuarkLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Regions Unexplored: ").append(RegionsUnexploredModule.isRegionsUnexploredLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Twilight Forest: ").append(TwilightForestModule.isTwilightForestLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Alex's Mobs: ").append(AlexsMobsModule.isAlexsMobsLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Alex's Caves: ").append(AlexsCavesModule.isAlexsCavesLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- CookingForBlockheads: ").append(CookingForBlockheadsModule.isCookingForBlockheadsLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- DivineRPG: ").append(DivineRPGModule.isDivineRPGLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Better End: ").append(BetterEndModule.isBetterEndLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Better Nether: ").append(BetterNetherModule.isBetterNetherLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- The Aether: ").append(AetherModule.isAetherLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Mowzie's Mobs: ").append(MowziesMobsModule.isMowziesMobsLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- MineColonies: ").append(MineColoniesModule.isMinecoloniesLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Oh The Trees You'll Grow: ").append(OhTheTreesModule.isOhTheTreesLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Reliquary: ").append(ReliquaryModule.isReliquaryLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- Cataclysm: ").append(CataclysmModule.isCataclysmLoaded() ? "Loaded" : "Not Found").append("\n");
        status.append("- The Abyss: ").append(TheAbyssModule.isTheAbyssLoaded() ? "Loaded" : "Not Found").append("\n");

        HOReborn.LOGGER.info(status.toString());
    }

    /**
     * Get the Biomes O' Plenty integration module.
     */
    public BiomesOPlentyModule getBiomesOPlentyModule() {
        return bopModule;
    }

    /**
     * Get the Biomes We've Gone integration module.
     */
    public BiomesWeveGoneModule getBiomesWeveGoneModule() {
        return biomesWeveGoneModule;
    }

    /**
     * Get the Born in Chaos integration module.
     */
    public BornInChaosModule getBornInChaosModule() {
        return bornInChaosModule;
    }

    /**
     * Get the Tinkers' Construct integration module.
     */
    public TinkersConstructModule getTinkersConstructModule() {
        return tconModule;
    }

    /**
     * Get the Natura integration module.
     */
    public NaturaModule getNaturaModule() {
        return naturaModule;
    }

    /**
     * Get the Pam's mods integration module.
     */
    public PamsModsModule getPamsModsModule() {
        return pamsModule;
    }

    /**
     * Get the HarvestCraft integration module.
     */
    public HarvestCraftModule getHarvestCraftModule() {
        return harvestCraftModule;
    }

    /**
     * Get the Farmer's Delight integration module.
     */
    public FarmersDelightModule getFarmersDelightModule() {
        return farmersDelightModule;
    }

    /**
     * Get the Deeper and Darker integration module.
     */
    public DeeperDarkerModule getDeeperDarkerModule() {
        return deeperDarkerModule;
    }

    /**
     * Get the Delightful integration module.
     */
    public DelightfulModule getDelightfulModule() {
        return delightfulModule;
    }

    /**
     * Get the Aquaculture integration module.
     */
    public AquacultureModule getAquacultureModule() {
        return aquacultureModule;
    }

    /**
     * Get the Thermal Cultivation integration module.
     */
    public ThermalCultivationModule getThermalCultivationModule() {
        return thermalCultivationModule;
    }

    /**
     * Get the Create integration module.
     */
    public CreateModule getCreateModule() {
        return createModule;
    }

    /**
     * Get the Create: Food integration module.
     */
    public CreateFoodModule getCreateFoodModule() {
        return createFoodModule;
    }

    /**
     * Get the Create Gourmet integration module.
     */
    public CreateGourmetModule getCreateGourmetModule() {
        return createGourmetModule;
    }

    /**
     * Get the Spice of Life: Carrot Edition integration module.
     */
    public SolCarrotModule getSolCarrotModule() {
        return solCarrotModule;
    }

    /**
     * Get the Ice and Fire integration module.
     */
    public IceAndFireModule getIceAndFireModule() {
        return iceAndFireModule;
    }

    /**
     * Get the Eternal Tales integration module.
     */
    public EternalTalesModule getEternalTalesModule() {
        return eternalTalesModule;
    }

    /**
     * Get the Productive Bees integration module.
     */
    public ProductiveBeesModule getProductiveBeesModule() {
        return productiveBeesModule;
    }

    /**
     * Get the Productive Trees integration module.
     */
    public ProductiveTreesModule getProductiveTreesModule() {
        return productiveTreesModule;
    }

    /**
     * Get the Quark integration module.
     */
    public QuarkModule getQuarkModule() {
        return quarkModule;
    }

    /**
     * Get the Regions Unexplored integration module.
     */
    public RegionsUnexploredModule getRegionsUnexploredModule() {
        return regionsUnexploredModule;
    }

    /**
     * Get the Twilight Forest integration module.
     */
    public TwilightForestModule getTwilightForestModule() {
        return twilightForestModule;
    }

    /**
     * Get the Alex's Mobs integration module.
     */
    public AlexsMobsModule getAlexsMobsModule() {
        return alexsMobsModule;
    }

    /**
     * Get the Alex's Caves integration module.
     */
    public AlexsCavesModule getAlexsCavesModule() {
        return alexsCavesModule;
    }

    /**
     * Get the CookingForBlockheads integration module.
     */
    public CookingForBlockheadsModule getCookingForBlockheadsModule() {
        return cookingForBlockheadsModule;
    }

    /**
     * Get the DivineRPG integration module.
     */
    public DivineRPGModule getDivineRPGModule() {
        return divineRPGModule;
    }

    /**
     * Get the Better End integration module.
     */
    public BetterEndModule getBetterEndModule() {
        return betterEndModule;
    }

    /**
     * Get the Better Nether integration module.
     */
    public BetterNetherModule getBetterNetherModule() {
        return betterNetherModule;
    }

    /**
     * Get the Aether integration module.
     */
    public AetherModule getAetherModule() {
        return aetherModule;
    }

    /**
     * Get the Mowzie's Mobs integration module.
     */
    public MowziesMobsModule getMowziesMobsModule() {
        return mowziesMobsModule;
    }

    /**
     * Get the MineColonies integration module.
     */
    public MineColoniesModule getMineColoniesModule() {
        return mineColoniesModule;
    }

    /**
     * Get the Oh The Trees You'll Grow integration module.
     */
    public OhTheTreesModule getOhTheTreesModule() {
        return ohTheTreesModule;
    }

    /**
     * Get the Reliquary integration module.
     */
    public ReliquaryModule getReliquaryModule() {
        return reliquaryModule;
    }

    /**
     * Get the Cataclysm integration module.
     */
    public CataclysmModule getCataclysmModule() {
        return cataclysmModule;
    }

    /**
     * Get the The Abyss integration module.
     */
    public TheAbyssModule getTheAbyssModule() {
        return theAbyssModule;
    }

    /**
     * Check if any mod integrations are active.
     */
    public boolean hasActiveIntegrations() {
        return BiomesOPlentyModule.isBiomesOPlentyLoaded() ||
               BiomesWeveGoneModule.isBiomesWeveGoneLoaded() ||
               BornInChaosModule.isBornInChaosLoaded() ||
               TinkersConstructModule.isTconstructLoaded() ||
               NaturaModule.isNaturaLoaded() ||
               PamsModsModule.isAnyPamsModsLoaded() ||
               HarvestCraftModule.isHarvestCraftLoaded() ||
               FarmersDelightModule.isFarmersDelightLoaded() ||
               DeeperDarkerModule.isDeeperDarkerLoaded() ||
               DelightfulModule.isDelightfulLoaded() ||
               AquacultureModule.isAquacultureLoaded() ||
               ThermalCultivationModule.isThermalCultivationLoaded() ||
               CreateModule.isCreateLoaded() ||
               CreateFoodModule.isCreateFoodLoaded() ||
               CreateGourmetModule.isCreateGourmetLoaded() ||
               SolCarrotModule.isSolCarrotLoaded() ||
               IceAndFireModule.isIceAndFireLoaded() ||
               EternalTalesModule.isEternalTalesLoaded() ||
               ProductiveBeesModule.isProductiveBeesLoaded() ||
               ProductiveTreesModule.isProductiveTreesLoaded() ||
               QuarkModule.isQuarkLoaded() ||
               RegionsUnexploredModule.isRegionsUnexploredLoaded() ||
               TwilightForestModule.isTwilightForestLoaded() ||
               AlexsMobsModule.isAlexsMobsLoaded() ||
               AlexsCavesModule.isAlexsCavesLoaded() ||
               CookingForBlockheadsModule.isCookingForBlockheadsLoaded() ||
               DivineRPGModule.isDivineRPGLoaded() ||
               BetterEndModule.isBetterEndLoaded() ||
               BetterNetherModule.isBetterNetherLoaded() ||
               AetherModule.isAetherLoaded() ||
               MowziesMobsModule.isMowziesMobsLoaded() ||
               MineColoniesModule.isMinecoloniesLoaded() ||
               OhTheTreesModule.isOhTheTreesLoaded() ||
               ReliquaryModule.isReliquaryLoaded() ||
               CataclysmModule.isCataclysmLoaded() ||
               TheAbyssModule.isTheAbyssLoaded();
    }
}

