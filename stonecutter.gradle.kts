plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.3-fabric"

stonecutter parameters {
    val (version, loader) = current.project.split('-', limit = 2)

    // Version- and loader-specific values from `stonecutter.properties.toml`
    properties {
        tags(version, loader)
    }

    // Enables `//? if fabric {`, `//? if forge {`, `//? if neoforge {` in sources
    constants {
        match(loader, "fabric", "forge", "neoforge")
    }

    swaps["mod_version"] = "\"${properties.get<String>("mod.version")}\";"

    replacements {
        // Mojang renamed ResourceLocation to Identifier in 1.21.11
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }
    }
}
