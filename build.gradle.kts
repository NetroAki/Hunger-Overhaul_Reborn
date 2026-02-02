plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.github.johnrengelman.shadow")
    id("me.modmuss50.mod-publish-plugin") version "0.5.1" apply false
}

val minecraft = stonecutter.current.version
val loader = loom.platform.get().name.lowercase()

version = "${mod.version}+$minecraft"
group = mod.group
base {
    archivesName.set("${mod.id}-$loader")
}


// Configure common dependencies for all loaders
architectury.common(stonecutter.tree.branches.mapNotNull {
    if (stonecutter.current.project !in it) null
    else it.prop("loom.platform")
})

repositories {
    mavenCentral()
    maven("https://files.minecraftforge.net/maven/")
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
    maven("https://maven.parchmentmc.org")
    maven("https://maven.shedaniel.me/")
    maven("https://api.modrinth.com/maven")
    maven("https://jitpack.io")
    maven("https://cursemaven.com")
}

dependencies {
    // Core Minecraft dependency
    minecraft("com.mojang:minecraft:$minecraft")
    
    // Use Parchment for 1.20.1, official Mojang mappings for 1.21.1 NeoForge
    if (minecraft == "1.20.1") {
        mappings(loom.layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-1.20.1:2023.09.03@zip")
        })
    } else if (loader == "fabric") {
        // Use Mojang mappings for 1.21.1 Fabric to match Common source
        mappings(loom.officialMojangMappings())
    } else {
        // Use official Mojang mappings for 1.21.1 NeoForge
        mappings(loom.officialMojangMappings())
    }
    
    if (loader == "fabric") {
        modImplementation("net.fabricmc:fabric-loader:${project.property("deps.fabric_loader")}")
        modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("deps.fabric_version")}")
        modImplementation("dev.architectury:architectury-fabric:${project.property("deps.architectury_version")}")

        // Runtime dependencies for Fabric
        if (minecraft == "1.20.1") {
            modRuntimeOnly("maven.modrinth:serene-seasons:9.1.0.2")
            modRuntimeOnly("maven.modrinth:glitchcore:25HLOiOl")
        } else if (minecraft == "1.21.1") {
            modRuntimeOnly("maven.modrinth:serene-seasons:UqA7miTT")
            modRuntimeOnly("maven.modrinth:glitchcore:lbSHOhee")
        }
        modRuntimeOnly("com.electronwill.night-config:core:3.6.6")
        modRuntimeOnly("com.electronwill.night-config:toml:3.6.6")
        modRuntimeOnly("net.jodah:typetools:0.6.3")
    }
    
    if (loader == "forge" || loader == "neoforge") {
        // Check if we should use NeoForge (1.21.1+) or Forge (1.20.1)
        if (loader == "neoforge") {
            val neoforgeVersion = project.property("deps.neoforge_loader") as String
            "neoForge"("net.neoforged:neoforge:$neoforgeVersion")
            modImplementation("dev.architectury:architectury-neoforge:${project.property("deps.architectury_version")}")
            
            // Runtime dependencies for NeoForge 1.21.1
            modRuntimeOnly("maven.modrinth:serene-seasons:SPj5bJoM")
            modRuntimeOnly("maven.modrinth:glitchcore:8wmCpbQ2")
        } else {
            "forge"("net.minecraftforge:forge:$minecraft-${project.property("deps.forge_loader")}")
            modImplementation("dev.architectury:architectury-forge:${project.property("deps.architectury_version")}")
            
            // Runtime dependencies for Forge 1.20.1
            modRuntimeOnly("maven.modrinth:serene-seasons:9.1.0.2")
            modRuntimeOnly("maven.modrinth:glitchcore:0.0.1.1")
        }
    }
}

// Configure source directories and exclusions
sourceSets {
    main {
        java {
            if (minecraft == "1.21.1") {
                // Task to copy/filter shared sources
                val sharedCommon21 = tasks.register<Copy>("sharedCommon21") {
                    from(rootProject.file("src/main/java"))
                    into(layout.buildDirectory.dir("generated/sources/shared-21"))
                    
                    // Exclude files that are replaced by 1.21.1/common source
                    exclude("**/Main/HOReborn.java")
                    exclude("**/Main/handlers/**")
                    exclude("**/Main/modules/**")
                    
                    // Exclude only replaced util files
                    exclude("**/Main/util/BiomeGrowthRegistry.java")
                    exclude("**/Main/util/BlockGrowthRegistry.java")
                    exclude("**/Main/util/FoodCategorizer.java")
                    exclude("**/Main/util/FoodUtil.java")
                    exclude("**/Main/util/LootModifierManager.java")
                    exclude("**/Main/util/RecipeManager.java")
                    exclude("**/Main/util/VersionDetector.java")

                    exclude("**/Main/world/**")
                    exclude("**/Main/compat/**")
                    exclude("**/Main/platforms/fabric/client/**")
                    exclude("**/platforms/forge/**")
                    exclude("**/Main/platforms/fabric/HORebornFabric.java")
                    exclude("**/Main/client/HudRenderer.java")
                    exclude("**/applecore/**")
                    exclude("**/food/**")
                    exclude("**/mixin/**")
                    exclude("**/Main/effects/**")
                }
                
                // Include 1.21.1 specific code
                srcDir(rootProject.file("$minecraft/common/src/main/java"))
                // Include the filtered shared code
                srcDir(sharedCommon21)
            } else {
                // For 1.20.1, use the shared directory as is
                srcDir(rootProject.file("src/main/java"))
            }

            // Platform-specific exclusions
            if (loader == "fabric") {
                exclude("**/platforms/forge/**")
                exclude("**/mixin/forge/**")
                // For 1.21.1, explicitly include platform fabric code
                if (minecraft == "1.21.1") {
                    // Fix incorrect exclusion and ensure source is included
                    srcDir(rootProject.file("$minecraft/fabric/src/main/java"))
                }
            } else if (loader == "forge" || loader == "neoforge") {
                exclude("**/platforms/fabric/**")
                exclude("**/mixin/fabric/**")
                exclude("**/util/CompatibilityUtil.java")
            }
        }
        if (minecraft == "1.21.1") {
            // Priority: Version-Specific > Shared
            val resourcesList = ArrayList<File>()
            
            if (loader == "fabric") {
                resourcesList.add(rootProject.file("$minecraft/fabric/src/main/resources"))
            } else if (loader == "neoforge") {
                 val neoforgeResources = rootProject.file("$minecraft/forge/src/main/resources")
                 resourcesList.add(neoforgeResources)
                 // Enable property substitution for version-specific resources
                 tasks.named<ProcessResources>("processResources").configure {
                     filesMatching("META-INF/neoforge.mods.toml") {
                         expand("version" to project.version.toString())
                     }
                 }
            }
            
            // Add shared resources LAST so they don't override specific ones
            resourcesList.add(rootProject.file("src/main/resources"))
            resources.setSrcDirs(resourcesList)

            // resources.exclude("**/hunger_overhaul_reborn*.mixins.json")
            // Removed exclude("**/fabric.mod.json") to fix loading issue
            
            if (loader == "neoforge") {
                resources.exclude("**/mods.toml")
            }
        } else {
             // For 1.20.1, standard inclusion
             resources.srcDir(rootProject.file("src/main/resources"))
        }


    }
}

// Note: META-INF is manually removed from build/resources/main for Forge development
// to prevent NeoForge from scanning the directory as a mod file

loom {
    accessWidenerPath = rootProject.file("src/main/resources/hunger_overhaul_reborn.accesswidener")

    if (loader == "neoforge") {
        // NeoForge 1.21.1+
        neoForge {
            // Mixins go in META-INF/mods.toml for NeoForge
        }
    } else if (loader == "forge") {
        // Forge 1.20.1
        forge {
            mixinConfig("hunger_overhaul_reborn-forge.mixins.json")
        }
    }
}

java {
    withSourcesJar()
    // Use Java 21 for 1.21.1, Java 17 for 1.20.1
    val javaVersion = if (minecraft == "1.21.1") JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = javaVersion
    sourceCompatibility = javaVersion
}

// Set duplicates strategy for all copy tasks
tasks.withType<AbstractCopyTask> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

tasks.shadowJar {
    configurations = listOf(shadowBundle)
    archiveClassifier = "dev-shadow"
}

tasks.remapJar {
    injectAccessWidener = false // Disabled to prevent namespace conflicts in universal JAR
    input = tasks.shadowJar.get().archiveFile
    archiveClassifier = null
    dependsOn(tasks.shadowJar)
}

tasks.jar {
    archiveClassifier = "dev"
}

val buildAndCollect = tasks.register<Copy>("buildAndCollect") {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
    from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
    dependsOn("build")
}

if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
        dependsOn(buildAndCollect)
    }

    rootProject.tasks.register("runActive") {
        group = "project"
        dependsOn(tasks.named("runClient"))
    }
    
    // Unified Build Task - Registered ONLY on root project to avoid parallel execution race conditions
    rootProject.tasks.register("buildUnified") {
        group = "publishing"
        description = "Builds all versions and collects JARs into a single 'releases' directory."
        
        // This depends on the Stonecutter 'chiseledBuild' task which runs the build for all active versions
        dependsOn(rootProject.tasks.named("chiseledBuild"))
        
        doLast {
            val releasesDir = rootProject.layout.projectDirectory.dir("releases")
            // Clean releases dir safely
            project.delete(releasesDir)
            mkdir(releasesDir)
            
            println("Collecting artifacts into ${releasesDir.asFile.path}...")
            
            val libsDir = rootProject.layout.buildDirectory.dir("libs").get().asFile
            
            if (libsDir.exists()) {
                libsDir.walk().filter { it.isFile && it.name.endsWith(".jar") && !it.name.contains("-sources") && !it.name.contains("-dev") && !it.name.contains("transformProduction") }.forEach { jarFile ->
                    println("Found: ${jarFile.name}")
                    jarFile.copyTo(releasesDir.file(jarFile.name).asFile, overwrite = true)
                }
            } else {
                println("No libs directory found at ${libsDir.path}. logic error?")
            }
            
            println("Unified build complete! Check the 'releases' folder.")
        }
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    properties(
        listOf("fabric.mod.json"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to mod.prop("mc_dep_fabric")
    )
    properties(
        listOf("META-INF/mods.toml", "pack.mcmeta"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to mod.prop("mc_dep_forgelike")
    )
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}
