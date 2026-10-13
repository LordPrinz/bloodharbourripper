import me.modmuss50.mpp.ReleaseType

/*
 * Shared configuration for every `versions/<mc>-<loader>` node.
 *
 * Each loader build script must set, *before* applying this plugin:
 *   extra["mod.mc"]      – the Minecraft version of the node (sc.current.version)
 *   extra["mod.loader"]  – fabric | forge | neoforge
 *   extra["mod.jar"]     – Provider<RegularFile> of the final, distributable jar
 *   extra["mod.refmap"]  – (optional) mixin refmap file name to reference from the mixin config
 *   extra["mod.mc_releases"] – (optional) List<String> of game versions to tag on Modrinth/CurseForge
 */

plugins {
    java
    id("me.modmuss50.mod-publish-plugin")
}

fun prop(name: String): String = project.property(name).toString()

val mc = extra["mod.mc"] as String
val loader = extra["mod.loader"] as String
val javaVersion = prop("mod.java").toInt()
val modVersion = prop("mod.version")

/** Compares dotted versions numerically, e.g. "26.1" > "1.21.11". */
fun versionAtLeast(version: String, min: String): Boolean {
    val a = version.split('.').map { it.toIntOrNull() ?: 0 }
    val b = min.split('.').map { it.toIntOrNull() ?: 0 }
    for (i in 0 until maxOf(a.size, b.size)) {
        val x = a.getOrElse(i) { 0 }
        val y = b.getOrElse(i) { 0 }
        if (x != y) return x > y
    }
    return true
}

version = "$modVersion+$mc"
group = prop("mod.group")
base.archivesName = "${prop("mod.id")}-$loader"

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
    toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
}

/*
 * Resource overlays: data formats (recipes, advancements, loot tables, item model definitions)
 * changed between Minecraft versions and JSON cannot hold Stonecutter comments.
 * Every overlay folder `src/overlays/<min version>` whose version is <= the node version and
 * that is not superseded by a newer overlay of the same "family" is added as a resource dir.
 *
 *   1.20    – 1.20.1 data layout (plural folder names, `item` keys)
 *   1.21    – 1.21–1.21.1 (singular folder names, `id` keys)
 *   1.21.2  – 1.21.2+ (string ingredients)
 *   26.3    – 26.3+ (`recipe_unlocked` takes `recipes`)
 *   1.21.4  – 1.21.4+ item model definitions (`assets/<ns>/items`) — stacks on top of `1.21.2`
 */
val dataOverlay = listOf("26.3", "1.21.2", "1.21", "1.20").first { versionAtLeast(mc, it) }
val overlays = buildList {
    add(dataOverlay)
    if (versionAtLeast(mc, "1.21.4")) add("1.21.4")
}
sourceSets.main {
    overlays.forEach { resources.srcDir(rootProject.file("src/overlays/$it")) }
}

tasks.processResources {
    val props = mapOf(
        "id" to prop("mod.id"),
        "name" to prop("mod.name"),
        "version" to version.toString(),
        "authors" to prop("mod.authors"),
        "license" to prop("mod.license"),
        "description" to prop("mod.description"),
        "sources" to prop("mod.sources"),
        "homepage" to prop("mod.homepage"),
        "minecraft" to prop("mod.mc_compat"),
        "java" to javaVersion.toString(),
        "mixin_java" to "JAVA_$javaVersion",
        // Only Forge (SRG at runtime) needs a refmap entry; Fabric remaps mixins in place, NeoForge runs on Mojang names
        "refmap" to (project.extra.properties["mod.refmap"]?.let { "\n  \"refmap\": \"$it\"," } ?: ""),
    )
    inputs.properties(props)
    filesMatching(listOf("fabric.mod.json", "META-INF/mods.toml", "META-INF/neoforge.mods.toml", "*.mixins.json", "pack.mcmeta")) {
        expand(props)
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("**/.omc/**", "**/.DS_Store")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = javaVersion
}

tasks.withType<Jar>().configureEach {
    val id = prop("mod.id")
    from(rootProject.file("LICENSE")) { rename { "${it}_$id" } }
}

@Suppress("UNCHECKED_CAST")
val modJar = extra["mod.jar"] as Provider<RegularFile>

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    description = "Builds the mod jar and copies it to the root `build/libs/<mod version>/`"
    from(modJar)
    into(rootProject.layout.buildDirectory.dir("libs/$modVersion"))
}

/** Returns the `## [<version>]` section of CHANGELOG.md, or a fallback line. */
fun changelogFor(version: String): String {
    val file = rootProject.file("CHANGELOG.md")
    if (!file.exists()) return "No changelog provided."
    val lines = file.readLines()
    val start = lines.indexOfFirst { it.startsWith("## [$version]") }
    if (start < 0) return "No changelog provided."
    val end = lines.drop(start + 1).indexOfFirst { it.startsWith("## [") }
    val body = if (end < 0) lines.drop(start + 1) else lines.subList(start + 1, start + 1 + end)
    return body.joinToString("\n").trim()
}

@Suppress("UNCHECKED_CAST")
val mcReleases = (extra.properties["mod.mc_releases"] as? List<String>)?.takeIf { it.isNotEmpty() } ?: listOf(mc)
val modrinthId = prop("publish.modrinth_id")
val curseforgeId = prop("publish.curseforge_id")

publishMods {
    file = modJar
    displayName = "${prop("mod.name")} $modVersion ($loader $mc)"
    // Unique per file on Modrinth/CurseForge, e.g. 1.1.0+1.21.1-neoforge
    version = "$modVersion+$mc-$loader"
    changelog = changelogFor(modVersion)
    type = when (prop("publish.type")) {
        "alpha" -> ReleaseType.ALPHA
        "beta" -> ReleaseType.BETA
        else -> ReleaseType.STABLE
    }
    modLoaders.add(loader)
    // Without tokens everything runs as a dry run, so `publishMods` is safe to try locally.
    dryRun = providers.environmentVariable("MODRINTH_TOKEN").orNull == null &&
        providers.environmentVariable("CURSEFORGE_TOKEN").orNull == null

    if (modrinthId.isNotBlank()) modrinth {
        projectId = modrinthId
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.addAll(mcReleases)
        if (loader == "fabric") requires("fabric-api")
    }

    if (curseforgeId.isNotBlank()) curseforge {
        projectId = curseforgeId
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.addAll(mcReleases)
        javaVersions.add(JavaVersion.toVersion(javaVersion))
        client = true
        server = true
        if (loader == "fabric") requires("fabric-api")
    }
}
