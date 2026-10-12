pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "FabricMC" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    // https://stonecutter.kikugie.dev/ — single source tree, many Minecraft versions
    id("dev.kikugie.stonecutter") version "0.9.8"
    // Picks the remapping Loom for obfuscated versions (<26.1) and the plain one for 26.1+
    id("dev.kikugie.loom-back-compat") version "0.4.3"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        /**
         * Creates `versions/{mc}-{loader}` nodes, each using `build.{loader}.gradle.kts`.
         * To support a new Minecraft version: add a `match(...)` line here and a section
         * in `stonecutter.properties.toml`, then fix whatever fails to compile.
         */
        fun match(version: String, vararg loaders: String) {
            for (loader in loaders) version("$version-$loader", version).buildscript("build.$loader.gradle.kts")
        }

        match("1.20.1", "fabric", "forge")
        match("1.21.1", "fabric", "neoforge")
        match("1.21.11", "fabric", "neoforge")
        match("26.2", "fabric", "neoforge")
        match("26.3", "fabric", "neoforge")
        vcsVersion = "26.3-fabric"
    }
}

rootProject.name = "bloodharbour-mod"
