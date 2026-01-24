pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://files.minecraftforge.net/maven/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://jitpack.io")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.5"
}

stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true
    shared {
        fun mc(loader: String, vararg versions: String) {
            for (version in versions) vers("$version-$loader", version)
        }
        mc("fabric", "1.20.1")
        mc("forge", "1.20.1")
        mc("fabric", "1.21.1")
        mc("forge", "1.21.1")
    }
    create(rootProject)
}

rootProject.name = "hunger_overhaul_reborn"

