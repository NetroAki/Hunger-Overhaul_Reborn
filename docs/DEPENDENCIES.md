# Optional Mod Dependencies

This document explains how to add optional mod dependencies for testing integrations.

## Finding Dependency IDs

### CurseForge Mods (curse.maven)

1. Go to the mod's CurseForge page
2. Find the **Project ID** in the URL or mod description
3. For a specific version:
   - Go to the "Files" tab
   - Click on the version you want
   - The **File ID** is in the URL (e.g., `https://www.curseforge.com/minecraft/mc-mods/mod-name/files/FILE_ID`)

Format: `curse.maven:mod-slug-PROJECT_ID:FILE_ID`

### Modrinth Mods (maven.modrinth)

1. Go to the mod's Modrinth page
2. Find the **Version ID**:
   - Go to the "Versions" tab
   - Click on the version you want
   - The **Version ID** is in the URL or can be found via Modrinth API

Format: `maven.modrinth:mod-slug:VERSION_ID`

## Required Mods for Full Integration Testing

### HarvestCraft (Pam's HarvestCraft 2)
- **CurseForge**: https://www.curseforge.com/minecraft/mc-mods/pams-harvestcraft-2
- **Project ID**: 361841
- **Example** (replace FILE_ID):
  ```gradle
  modRuntimeOnly "curse.maven:pams-harvestcraft-2-361841:FILE_ID"
  ```

### Natura
- **Status**: ❌ **DEPRECATED** - No longer exists for Minecraft 1.20.1+
- **Note**: Natura functionality (berries, flour recipes) has been integrated into Tinkers' Construct
- **Alternative**: Use Tinkers' Construct instead, which includes all Natura features
- **Legacy**: If you're on an older Minecraft version, Natura may still be available

### Tinkers' Construct
- **Modrinth**: https://modrinth.com/mod/tinkers-construct
- **CurseForge**: https://www.curseforge.com/minecraft/mc-mods/tinkers-construct
- **Project ID**: 74072
- **Note**: In 1.20.1+, Tinkers' Construct includes functionality previously in Natura (berries, flour recipes)
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:tinkers-construct:VERSION_ID"
  ```
- **CurseForge Example** (replace FILE_ID):
  ```gradle
  modRuntimeOnly "curse.maven:tinkers-construct-74072:FILE_ID"
  ```

### Biomes O' Plenty
- **Modrinth**: https://modrinth.com/mod/biomes-o-plenty
- **CurseForge**: https://www.curseforge.com/minecraft/mc-mods/biomes-o-plenty
- **Project ID**: 220318
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:biomes-o-plenty:VERSION_ID"
  ```
- **CurseForge Example** (replace FILE_ID):
  ```gradle
  modRuntimeOnly "curse.maven:biomes-o-plenty-220318:FILE_ID"
  ```

### Farmer's Delight
- **Modrinth**: https://modrinth.com/mod/farmers-delight
- **Mod ID**: `farmersdelight`
- **Modrinth Project ID**: R2OftAxM
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:farmers-delight:VERSION_ID"
  ```
- **Note**: Adds crops, food items, and cooking mechanics

### Delightful
- **Modrinth**: https://modrinth.com/mod/delightful
- **Mod ID**: `delightful`
- **Modrinth Project ID**: JtSnhtNJ
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:delightful:VERSION_ID"
  ```
- **Note**: Adds various food items and ingredients

### Aquaculture 2
- **CurseForge**: https://www.curseforge.com/minecraft/mc-mods/aquaculture
- **Mod ID**: `aquaculture`
- **Project ID**: 60028
- **CurseForge Example** (replace FILE_ID):
  ```gradle
  modRuntimeOnly "curse.maven:aquaculture-60028:FILE_ID"
  ```
- **Note**: Adds fish, fishing mechanics, and fish-based foods

### Thermal Cultivation
- **Modrinth**: https://modrinth.com/mod/thermal-cultivation
- **Mod ID**: `thermal_cultivation`
- **Modrinth Project ID**: 5beJoehw
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:thermal-cultivation:VERSION_ID"
  ```
- **Note**: Adds crops and food items as part of the Thermal series
- **Requires**: Thermal Foundation (CoFH Core)

### Create: Food
- **Modrinth**: https://modrinth.com/mod/create-food
- **Mod ID**: `create_food`
- **Modrinth Project ID**: 4HnO3el1
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:create-food:VERSION_ID"
  ```
- **Note**: Adds food items using Create's processing methods
- **Requires**: Create mod

### Create Gourmet
- **CurseForge**: https://www.curseforge.com/minecraft/mc-mods/create-gourmet
- **Mod ID**: `gourmet`
- **Project ID**: 867119
- **CurseForge Example** (replace FILE_ID):
  ```gradle
  modRuntimeOnly "curse.maven:create-gourmet-867119:FILE_ID"
  ```
- **Note**: Adds gourmet food items for Create mod
- **Requires**: Create mod

### Spice of Life: Carrot Edition
- **CurseForge**: https://www.curseforge.com/minecraft/mc-mods/spice-of-life-carrot-edition
- **Mod ID**: `solcarrot`
- **Project ID**: 277616
- **CurseForge Example** (replace FILE_ID):
  ```gradle
  modRuntimeOnly "curse.maven:sol-carrot-277616:FILE_ID"
  ```
- **Note**: Tracks food diversity (doesn't add new food items)
- **Integration**: Currently just detects the mod, future integration could adjust food values based on diversity bonuses

### Ice and Fire
- **Modrinth**: https://modrinth.com/mod/ice-and-fire
- **Mod ID**: `iceandfire`
- **Modrinth Project ID**: LVnvHVBp
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:ice-and-fire:VERSION_ID"
  ```
- **Note**: Adds dragons, sea serpents, and other creatures with various meats and foods
- **Food Items**: Dragon meat, sea serpent meat, hydra chops, myrmex eggs, cyclops meat, etc.

### Productive Bees
- **Modrinth**: https://modrinth.com/mod/productive-bees
- **Mod ID**: `productivebees`
- **Modrinth Project ID**: jH6iiqkd
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:productive-bees:VERSION_ID"
  ```
- **Note**: Adds bees and honey-related food items
- **Food Items**: Honey, honeycomb, honey treats, honey bread, etc.

### Productive Trees
- **Modrinth**: https://modrinth.com/mod/productive-trees
- **Mod ID**: `productivetrees`
- **Modrinth Project ID**: BTmup08e
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:productive-trees:VERSION_ID"
  ```
- **Note**: Adds trees that produce various fruits
- **Food Items**: Various fruits (apples, pears, cherries, plums, etc.), dried fruits, fruit juices, etc.

### Twilight Forest
- **CurseForge**: https://www.curseforge.com/minecraft/mc-mods/the-twilight-forest
- **Mod ID**: `twilightforest`
- **Project ID**: 227639
- **CurseForge Example** (replace FILE_ID):
  ```gradle
  modRuntimeOnly "curse.maven:twilight-forest-227639:FILE_ID"
  ```
- **Note**: Adds a new dimension with various foods
- **Food Items**: Torberries, maze waflers, venison, meef, hydra chops, experiment 115, etc.

### Alex's Mobs
- **Modrinth**: https://modrinth.com/mod/alexs-mobs
- **Mod ID**: `alexsmobs`
- **Modrinth Project ID**: 2cMuAZAp
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:alexs-mobs:VERSION_ID"
  ```
- **Note**: Adds various mobs with food drops
- **Food Items**: Catfish, bunfish, kangaroo meat, caiman meat, moose ribs, lobster tail, crab meat, etc.

### CookingForBlockheads
- **Modrinth**: https://modrinth.com/mod/cookingforblockheads
- **Mod ID**: `cookingforblockheads`
- **Modrinth Project ID**: vJnhuDde
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:cookingforblockheads:VERSION_ID"
  ```
- **Note**: UI/crafting mod that doesn't add food items itself
- **Integration**: Currently just detects the mod, future integration could show food values in the cooking book
- **Note**: This mod works with other food mods and provides a cooking station and recipe book

### DivineRPG
- **Modrinth**: https://modrinth.com/mod/divinerpg
- **Mod ID**: `divinerpg`
- **Modrinth Project ID**: F3kLpGw6
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:divinerpg:VERSION_ID"
  ```
- **Note**: Large RPG mod that adds various foods from different dimensions
- **Food Items**: Arlemite meat, realmite meat, rupee meat, eden meat, wildwood meat, various fruits, etc.

### Better End
- **Modrinth**: https://modrinth.com/mod/better-end
- **Mod ID**: `betterend`
- **Modrinth Project ID**: gc8OEnCC
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:better-end:VERSION_ID"
  ```
- **Note**: Enhances the End dimension with new foods and plants
- **Food Items**: Shadow berries, blossom berries, sweet berries, end fish, chorus mushrooms, etc.

### Better Nether
- **Modrinth**: https://modrinth.com/mod/better-nether
- **Mod ID**: `betternether`
- **Modrinth Project ID**: MpzVLzy5
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:better-nether:VERSION_ID"
  ```
- **Note**: Enhances the Nether dimension with new foods and plants
- **Food Items**: Black apples, cincinnasite apples, glowberries, orange mushrooms, barrel cactus, etc.

### The Aether
- **Modrinth**: https://modrinth.com/mod/the-aether
- **Mod ID**: `aether`
- **Modrinth Project ID**: YhmgMVyu
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:the-aether:VERSION_ID"
  ```
- **Note**: Adds the Aether dimension with various foods
- **Food Items**: Blue berries, white apples, enchanted blue berries, swet balls, candy canes, gingerbread men, phyg meat, etc.

### Mowzie's Mobs
- **Modrinth**: https://modrinth.com/mod/mowzies-mobs
- **Mod ID**: `mowziesmobs`
- **Modrinth Project ID**: BFbX9xcm
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:mowzies-mobs:VERSION_ID"
  ```
- **Note**: Adds various mobs (primarily combat-focused, may not add food items)
- **Food Items**: Currently no food items (module kept for future compatibility)

### Oh The Trees You'll Grow
- **Modrinth**: https://modrinth.com/mod/oh-the-trees-youll-grow
- **Mod ID**: `oh_the_trees_youll_grow`
- **Modrinth Project ID**: g8NOG5OR
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:oh-the-trees-youll-grow:VERSION_ID"
  ```
- **Note**: Adds trees that produce various fruits
- **Food Items**: Various fruits (apples, pears, cherries, plums, etc.), dried fruits, fruit juices, fruit salads, etc.

### Reliquary
- **CurseForge**: https://www.curseforge.com/minecraft/mc-mods/reliquary-v1-3
- **Mod ID**: `reliquary`
- **Project ID**: 241319
- **CurseForge Example** (replace FILE_ID):
  ```gradle
  modRuntimeOnly "curse.maven:reliquary-241319:FILE_ID"
  ```
- **Note**: Adds relics and special items (primarily utility-focused, may not add food items)
- **Food Items**: Currently no food items (module kept for future compatibility)

### Cataclysm (L_Ender's Cataclysm)
- **Modrinth**: https://modrinth.com/mod/cataclysm
- **Mod ID**: `cataclysm`
- **Modrinth Project ID**: 46KJle7n
- **Modrinth Example** (replace VERSION_ID):
  ```gradle
  modRuntimeOnly "maven.modrinth:cataclysm:VERSION_ID"
  ```
- **Note**: Adds bosses and combat items (primarily combat-focused, may not add food items)
- **Food Items**: Currently no food items (module kept for future compatibility)

## Quick Lookup Script

You can use this script to find CurseForge file IDs:

```bash
# Replace PROJECT_ID and VERSION with actual values
curl "https://api.curseforge.com/v1/mods/PROJECT_ID/files?gameVersion=1.20.1" | grep -o '"id":[0-9]*' | head -1
```

Or use the CurseForge API directly:
```
https://api.curseforge.com/v1/mods/PROJECT_ID/files?gameVersion=1.20.1
```

## Adding Dependencies

1. Uncomment the desired dependency line in `1.20.1/fabric/build.gradle` or `1.20.1/forge/build.gradle`
2. Replace `FILE_ID` or `VERSION_ID` with the actual ID
3. Sync Gradle
4. Run the game to test the integration

## Notes

- All optional dependencies are `modRuntimeOnly` - they won't be included in the final JAR
- Dependencies are only needed for testing integrations
- The mod works fine without these dependencies (integrations are optional)

