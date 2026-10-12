plugins {
    id("net.neoforged.moddev") version "2.0.148"
    // Prevents several NeoForge nodes from decompiling Minecraft at the same time
    id("neoforge-mutex")
}

extra["mod.mc"] = sc.current.version
extra["mod.loader"] = "neoforge"
extra["mod.jar"] = tasks.jar.flatMap { it.archiveFile }
extra["mod.mc_releases"] = sc.properties.rawOrNull("mod", "mc_releases")?.asList().orEmpty().map { it.toString() }
apply(plugin = "mod-conventions")

neoForge {
    version = property("deps.neoforge") as String

    mods {
        register(property("mod.id") as String) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            client()
            gameDirectory = file("run")
        }
        register("server") {
            server()
            gameDirectory = file("run")
            programArgument("--nogui")
        }
    }
}

tasks.processResources {
    exclude("fabric.mod.json", "META-INF/mods.toml", "pack.mcmeta")
}

tasks.named("createMinecraftArtifacts") {
    dependsOn("stonecutterGenerate")
}
