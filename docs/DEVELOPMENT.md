# Development guide

## How the project is built

One source tree (`src/main`) is compiled for every **node**, i.e. every Minecraft version × loader
pair, by [Stonecutter](https://stonecutter.kikugie.dev/). Nodes live in `versions/<mc>-<loader>`
and are declared in `settings.gradle.kts`.

```
settings.gradle.kts            nodes: match("1.21.1", "fabric", "neoforge") …
stonecutter.gradle.kts         active node, loader constants, ResourceLocation → Identifier swap
stonecutter.properties.toml    mod info + per-version/per-loader dependency versions
build.fabric.gradle.kts        Fabric nodes   (Loom; remapping picked by loom-back-compat)
build.neoforge.gradle.kts      NeoForge nodes (ModDevGradle)
build.forge.gradle.kts         Forge nodes    (ModDevGradle legacyforge)
buildSrc/…/mod-conventions     shared: Java version, resource overlays, metadata, publishing
src/main/java                  all code, with Stonecutter conditions
src/main/resources             assets and loader metadata shared by all versions
src/overlays/<version>         data whose JSON format changed between versions
```

### Stonecutter in 30 seconds

Version- and loader-specific code is written with comments that Stonecutter toggles:

```java
//? if >=1.21.2 {
return InteractionResult.SUCCESS;
//?} else {
/*return InteractionResultHolder.success(stack);
*///?}

//? if fabric {
…
//?} elif neoforge {
/*…
*///?} else {
/*… (forge)
*///?}
```

The files in git are always in the state of the **active** node (`stonecutter active` in
`stonecutter.gradle.kts`, currently `26.3-fabric`). To work comfortably on another version, run the
Gradle task `Set active project to 1.21.1-neoforge` (in the `stonecutter` group) — it rewrites the
comments so your IDE sees that version. **Switch back to the VCS version (`Reset active project`)
before committing.** Building any node never requires switching; each node gets its own processed
copy of the sources.

## Project layout (Java)

| Package      | Contents |
|--------------|----------|
| *(root)*     | `BloodHarbourRipper` — mod id, `id()`, common `init()` |
| `registry`   | `RegistryHelper` (the only loader-specific registration code) and the `Mod*` registries |
| `item`       | `BoneSkewerItem`, `SkewerTier` (all skewer stats), tooltips, the server-side skewer tracker |
| `entity`     | `BoneSkewerEntity`, `DashPhantomEntity`, `ExecuteMarkerEntity`, `TransientEntity` base |
| `ability`    | Execute / dash server logic, targeting rules, cooldowns, bonus loot |
| `network`    | Payload records + per-loader server registration |
| `loot`       | Loot table injection (Ancient City vials) |
| `client`     | Key mappings, client tick logic, highlight, sending packets, renderers |
| `compat`     | `Compat` — tiny wrappers around vanilla calls that changed between versions |
| `mixin`      | `LivingEntityInvoker` (loot/XP), `client.MinecraftMixin` (target outline) |
| `platform.*` | One entrypoint per loader: wires loader events to the shared code, nothing else |

Rules of thumb:

1. **Gameplay logic is loader-independent.** Loader code only lives in `platform.*`,
   `RegistryHelper`, `ModNetworking`, `ClientNetworking` and the creative tab builder.
2. **Put version differences in one place.** If a vanilla call changed, wrap it in `Compat` (or
   `RenderCompat` for rendering) instead of sprinkling conditions everywhere.
3. **The server validates everything.** Client code may predict (cooldown messages, highlight), but
   abilities are re-checked in `ability/*` when the packet arrives.

## Adding a feature without breaking older versions

1. Write the feature in the shared packages, as if for the active version.
2. Build **every** node: `./gradlew build`. CI does the same on every push and pull request, so a
   feature that breaks e.g. 1.20.1 cannot be merged unnoticed.
3. Where a node fails to compile, add a Stonecutter condition (or a `Compat` method) for the
   versions that need different code. Use the exact version the API changed in.
4. Boot a dedicated server for the changed nodes (`./gradlew :<node>:runServer`) — it catches data
   pack errors, registry problems and mixin failures that compilation cannot. Test the client with
   `runClient` on at least the oldest and the newest version.
5. Add an entry to `CHANGELOG.md` under the next version.

Compatibility contracts that must never break:

- **Registry ids** (`bone_skewer`, `bone_skewer_raw`, `bone_skewer_skeleton`, `jaull_fish_vial`,
  `dash_phantom`, `execute_marker`, the sound ids) are stored in worlds. Never rename or remove them;
  add new ones instead.
- **Saved data keys** (`DealtDamage`, …) must keep their name and meaning.
- **Network payloads**: if you change a payload's fields, bump `ModNetworking.PROTOCOL_VERSION` so
  mismatched client/server versions are rejected cleanly instead of crashing.
- **Translation keys** used by older releases should stay in `en_us.json`.

### Data files (recipes, advancements, item models)

JSON cannot hold Stonecutter comments, so data whose format changed lives in
`src/overlays/<first version that uses this format>`:

| Overlay  | Used by | Why |
|----------|---------|-----|
| `1.20`   | 1.20.x          | plural folders (`recipes/`), `{"item": …}` keys |
| `1.21`   | 1.21–1.21.1     | singular folders, `{"id": …}` results |
| `1.21.2` | 1.21.2–26.2     | ingredients are plain strings |
| `26.3`   | 26.3+           | `recipe_unlocked` uses `recipes` |
| `1.21.4` | 1.21.4+ (stacked on top) | item model definitions in `assets/<ns>/items/` |

When you add a recipe or advancement, add it to every overlay. When Minecraft changes a format
again, copy the newest overlay to a folder named after the new version, adapt it and add the folder
name to the list in `buildSrc/src/main/kotlin/mod-conventions.gradle.kts`.

## Adding a new Minecraft version

1. `settings.gradle.kts`: add `match("26.4", "fabric", "neoforge")`.
2. `stonecutter.properties.toml`: add the `["26.4"]`, `[fabric."26.4"]`, `[neoforge."26.4"]`
   sections (Java version, `mc_releases`, `mc_compat`, Fabric API and NeoForge versions).
3. `./gradlew :26.4-fabric:build :26.4-neoforge:build`, fix compile errors with conditions.
4. `./gradlew :26.4-fabric:runServer` and `:26.4-neoforge:runServer`; fix data errors with an
   overlay.
5. Optionally move `vcsVersion` / `stonecutter active` to the newest version.

Dropping a version is the reverse: remove its `match(...)` and toml sections; old conditions can be
cleaned up later.

## Releasing to Modrinth and CurseForge

Publishing uses [mod-publish-plugin](https://modmuss50.github.io/mod-publish-plugin/), configured
once in `buildSrc/src/main/kotlin/mod-conventions.gradle.kts`.

One-time setup:

1. Create the projects on Modrinth and CurseForge and put their ids into
   `publish.modrinth_id` / `publish.curseforge_id` in `stonecutter.properties.toml`.
2. Create API tokens and add them as GitHub repository secrets `MODRINTH_TOKEN` and
   `CURSEFORGE_TOKEN`.

Every release:

1. Bump `mod.version` in `stonecutter.properties.toml` and add a `## [x.y.z]` section to
   `CHANGELOG.md` (it becomes the changelog on both sites).
2. Commit, then `git tag vx.y.z && git push --tags`.
3. The `Release` workflow builds all nodes, uploads one file per version/loader to both sites
   (tagged with the right game versions, loader and Fabric API dependency) and creates a GitHub
   release with all jars.

Locally, `./gradlew publishMods` without tokens performs a dry run.
