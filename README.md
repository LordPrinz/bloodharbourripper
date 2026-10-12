# Blood Harbour Ripper

A dark, sea-themed weapon mod: throwable Bone Skewers, executions and the Phantom Undertow dash.
Inspired by Pyke, the Bloodharbor Ripper.

[CurseForge](https://www.curseforge.com/minecraft/mc-mods/the-bloodharbour-ripper) · [Modrinth](https://modrinth.com/mod/the-bloodharbour-ripper)

## Supported versions

| Minecraft | Loaders           | Java |
|-----------|-------------------|------|
| 1.20.1    | Forge, Fabric     | 17   |
| 1.21.1    | NeoForge, Fabric  | 21   |
| 1.21.11   | NeoForge, Fabric  | 21   |
| 26.2      | NeoForge, Fabric  | 25   |
| 26.3      | NeoForge, Fabric  | 25   |

Fabric builds require [Fabric API](https://modrinth.com/mod/fabric-api). The mod is required on
both client and server.

## Features

- **Bone Skewers** — three tiers (Skeleton → Raw → Bone Skewer). Swords in melee; hold right click
  to throw them like a loyal trident, right click again to recall. Hits pull the target towards you.
- **Death From Below** (`Shift` + `C`, Bone Skewer in hand) — marks the nearest enemy below 33%
  health, executes it, drops its loot and experience a second time and pulls you to it.
  Valid targets are outlined while you hold the Bone Skewer.
- **Phantom Undertow** (`Shift` + `X`, Bone Skewer in hand) — dash forward; your afterimage rushes
  back and damages, blinds and freezes everything on its path.
- **Jaull Fish Vial** — found in Ancient City chests, used to upgrade the skewers.

## Building

Everything is built from one source tree with [Stonecutter](https://stonecutter.kikugie.dev/).
Gradle downloads the required JDKs (17, 21, 25) automatically.

```bash
./gradlew build             # every version and loader
./gradlew buildAndCollect   # …and copy the jars to build/libs/<mod version>/
./gradlew :1.21.1-neoforge:build
./gradlew :26.3-fabric:runClient
```

See [docs/DEVELOPMENT.md](docs/DEVELOPMENT.md) for the project layout, how to add features without
breaking older versions, how to add a new Minecraft version and how releases are published.

## Disclaimer

Unofficial fan-made mod. Not affiliated with, endorsed or sponsored by Riot Games; League of Legends
and Pyke are trademarks of Riot Games, Inc.

## License

All Rights Reserved — see [LICENSE](LICENSE).
