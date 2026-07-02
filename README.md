# Blood Harbour Ripper

A dark and mysterious Minecraft mod for Minecraft 1.20.1.

Built on the [MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template): the `common` module holds loader-neutral code, while loader-specific modules load the mod.

## Status

| Loader | Status |
|---|---|
| Forge 1.20.1 (47.4.0) | Fully implemented (`forge/`) |
| Fabric 1.20.1 | Stub only — not yet ported (`fabric/`) |

The mod content currently lives entirely in the `forge/` module because it relies on Forge-specific APIs (SimpleChannel networking, global loot modifiers, `ForgeConfigSpec` config, Forge event bus).

## Features

- **Bone Skewers** — three tiers of throwable skewer weapons (skeleton, raw, advanced) with custom projectile entities, renderers, and sounds
- **Execute** — keybound execute attack with cooldown tracking, target highlighting, and marker rendering
- **Dash** — keybound dash ability with a dash phantom effect
- **Jaull Fish Vial** — found in Ancient City loot (injected via global loot modifier)
- Custom creative tab, advancements, and recipes

## Building

Requires Java 17.

```bash
./gradlew build          # build all modules
./gradlew :forge:build   # Forge jar only
```

Jars land in `<module>/build/libs/`.

## Running in development

```bash
./gradlew :forge:runClient
./gradlew :forge:runServer
```

## License

All Rights Reserved — see [LICENSE](LICENSE).
