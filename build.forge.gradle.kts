plugins {
    id("net.neoforged.moddev.legacyforge") version "2.0.148"
}

val modId = property("mod.id") as String

extra["mod.mc"] = sc.current.version
extra["mod.loader"] = "forge"
extra["mod.jar"] = provider { tasks.getByName("reobfJar") }.flatMap { (it as AbstractArchiveTask).archiveFile }
extra["mod.refmap"] = "$modId.refmap.json"
extra["mod.mc_releases"] = sc.properties.rawOrNull("mod", "mc_releases")?.asList().orEmpty().map { it.toString() }
apply(plugin = "mod-conventions")

legacyForge {
    version = property("deps.forge") as String

    mods {
        register(modId) {
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

// Forge 1.20.1 has no `[[mixins]]` in mods.toml: register the config in the manifest and generate a refmap
mixin {
    add(sourceSets.main.get(), "$modId.refmap.json")
    config("$modId.mixins.json")
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    compileOnly("org.jetbrains:annotations:24.1.0")
}

tasks.jar {
    manifest.attributes("MixinConfigs" to "$modId.mixins.json")
}

tasks.processResources {
    exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
}

tasks.named("createMinecraftArtifacts") {
    dependsOn("stonecutterGenerate")
}
