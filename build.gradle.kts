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
        // Use Yarn for 1.21.1 Fabric
        mappings("net.fabricmc:yarn:1.21.1+build.${project.property("deps.yarn_build")}:v2")
    } else {
        // Use official Mojang mappings for 1.21.1 NeoForge
        mappings(loom.officialMojangMappings())
    }
    
    if (loader == "fabric") {
        modImplementation("net.fabricmc:fabric-loader:${project.property("deps.fabric_loader")}")
        modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("deps.fabric_version")}")
        modImplementation("dev.architectury:architectury-fabric:${project.property("deps.architectury_version")}")

        // Runtime dependencies for Fabric - only for 1.20.1 as these mods aren't updated for 1.21.1
        if (minecraft == "1.20.1") {
            modRuntimeOnly("maven.modrinth:serene-seasons:c1BdjabH") // Serene Seasons 9.1.0.2
            modRuntimeOnly("maven.modrinth:glitchcore:25HLOiOl") // GlitchCore 0.0.1.1
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
        } else {
            "forge"("net.minecraftforge:forge:$minecraft-${project.property("deps.forge_loader")}")
            modImplementation("dev.architectury:architectury-forge:${project.property("deps.architectury_version")}")
        }
    }
}

// Configure source directories and exclusions
sourceSets {
    main {
        java {
            // Use shared source directory for both versions (with version detection)
            srcDir(rootProject.file("src/main/java"))
            // Add version-specific overrides on top
            if (minecraft == "1.21.1") {
                srcDir(rootProject.file("$minecraft/common/src/main/java"))
            }

            // Version-specific exclusions
            if (minecraft == "1.21.1") {
                exclude("**/Main/HOReborn.java") // Use 1.21.1-specific HOReborn.java
                exclude("**/Main/handlers/**") // Use 1.21.1-specific handlers
                exclude("**/Main/modules/**") // Use 1.21.1-specific modules
                exclude("**/Main/util/**") // Use 1.21.1-specific utilities
                exclude("**/Main/world/**") // Use 1.21.1-specific world utilities
                exclude("**/Main/compat/**") // Use 1.21.1-specific compatibility layer
                exclude("**/Main/platforms/fabric/client/**") // Use 1.21.1-specific fabric client code
                exclude("**/platforms/forge/**") // Use 1.21.1-specific forge implementation
                exclude("**/Main/platforms/fabric/HORebornFabric.java") // Use 1.21.1-specific HORebornFabric
                exclude("**/Main/client/HudRenderer.java") // Use 1.21.1-specific HudRenderer
                exclude("**/applecore/**") // Exclude applecore package for 1.21.1
                exclude("**/food/**") // Exclude food package for 1.21.1
                exclude("**/mixin/**") // Exclude mixins for 1.21.1
                exclude("**/config/HORConfig.java") // Exclude mixin config for 1.21.1
                exclude("**/Main/effects/**") // Use 1.21.1-specific effect implementations
            }

            // Platform-specific exclusions
            if (loader == "fabric") {
                exclude("**/platforms/forge/**")
                exclude("**/mixin/forge/**")
                // For 1.21.1, exclude shared platform fabric code
                if (minecraft == "1.21.1") {
                    exclude("src/main/java/org/Netroaki/Main/platforms/fabric/**")
                }
            } else if (loader == "forge" || loader == "neoforge") {
                exclude("**/platforms/fabric/**")
                exclude("**/mixin/fabric/**")
                exclude("**/util/CompatibilityUtil.java")
            }
        }
        // Always include shared resources first
        resources.srcDir(rootProject.file("src/main/resources"))

        // Exclude problematic files for 1.21.1 builds
        if (minecraft == "1.21.1") {
            resources.exclude("**/hunger_overhaul_reborn*.mixins.json")
            // For Fabric, exclude shared fabric.mod.json since we use version-specific one
            if (loader == "fabric") {
                resources.exclude("**/fabric.mod.json")
            }
            // For NeoForge, exclude shared mods.toml since we use neoforge.mods.toml
            if (loader == "neoforge") {
                resources.exclude("**/mods.toml")
            }
        }

        // Add version-specific resources on top (these will override shared resources)
        if (minecraft == "1.21.1") {
            if (loader == "fabric") {
                resources.srcDir(rootProject.file("$minecraft/fabric/src/main/resources"))
            } else if (loader == "neoforge") {
                val neoforgeResources = rootProject.file("$minecraft/forge/src/main/resources")
                resources.srcDir(neoforgeResources)
                // Enable property substitution for version-specific resources
                tasks.named<ProcessResources>("processResources").configure {
                    filesMatching("META-INF/neoforge.mods.toml") {
                        expand("version" to project.version.toString())
                    }
                }
            }
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
