plugins {
    // Applies the remapping Loom on obfuscated versions and the plain Loom on 26.1+
    id("dev.kikugie.loom-back-compat")
}

extra["mod.mc"] = sc.current.version
extra["mod.loader"] = "fabric"
extra["mod.jar"] = loomx.modJar.flatMap { it.archiveFile }
extra["mod.mc_releases"] = sc.properties.rawOrNull("mod", "mc_releases")?.asList().orEmpty().map { it.toString() }
apply(plugin = "mod-conventions")

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    loomx.applyMojangMappings()

    // `mod*` configurations are translated by loom-back-compat on 26.1+
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
}

tasks.processResources {
    exclude("META-INF/mods.toml", "META-INF/neoforge.mods.toml", "pack.mcmeta")
}
