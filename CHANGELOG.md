# Changelog

All notable changes to this project are documented here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the project uses
[Semantic Versioning](https://semver.org/). The section matching `mod.version` in
`stonecutter.properties.toml` is used as the Modrinth/CurseForge changelog.

## [1.1.3]

### Changed
- The source code is now public: https://github.com/LordPrinz/bloodharbourripper. Report bugs in
  its issue tracker.
- The mod list in game links to the mod page, the source code and the issue tracker.

## [1.1.2]

### Fixed
- Minecraft 26.3: Phantom Undertow did not launch the player forward (the afterimage still
  appeared), and Bone Skewer hits did not pull other players. The new velocity was not sent to
  the affected player's own client.

### Changed
- Shorter item tooltips, split into a few lines instead of one very wide line.

## [1.1.1]

### Fixed
- Bone Skewers could not be enchanted on Minecraft 1.21 and newer (they now count as swords for
  Sharpness, Smite, Looting, Fire Aspect, Sweeping Edge, Unbreaking, Mending and Curse of
  Vanishing).

### Changed
- Higher resolution mod icon.
- Now also published on Modrinth.

## [1.1.0]

### Added
- Support for Minecraft **1.20.1** (Forge, Fabric), **1.21.1**, **1.21.11**, **26.2** and **26.3**
  (NeoForge, Fabric) from a single code base.
- The execution mark is now visible to every nearby player, not only to the executor.
- Subtitles for all mod sounds and translatable action-bar messages.

### Changed
- New crafting progression:
  - **Bone Skewer Skeleton** — crafting table: 3 Prismarine Shards, 1 Nautilus Shell, 1 Bone
    (previously a Nether Star, Trident, Diamond Sword, Gold Block, Diamond Block, Recovery Compass
    and Heart of the Sea — more than the final tier).
  - **Bone Skewer Raw** — smithing table: Jaull Fish Vial + Bone Skewer Skeleton + Echo Shard.
  - **Bone Skewer** — smithing table: Jaull Fish Vial + Bone Skewer Raw + Heart of the Sea.
  - Upgrading at the smithing table keeps enchantments, name and durability.
- The Jaull Fish Vial Ancient City loot is now injected the same way on every loader
  (33% chance for one vial per chest, as before on Forge).
- Executions give a second roll of the target's loot table and experience instead of
  duplicating every dropped item.
- The Bone Skewer charging sound now follows the player.

### Fixed
- Executing a low-health **player** duplicated their whole inventory.
- The server did not check the execution cooldown or the held item, so a modified client could
  execute without the Bone Skewer and without waiting.
- The dash could be triggered without holding the Bone Skewer (server side) and showed a wrong
  cooldown on dedicated servers.
- Creative and spectator players could be executed.
- An entity that survived an execution (e.g. with a Totem of Undying) dropped double loot the next
  time it died.
- Recalling a skewer could leave the item on a ~83 minute cooldown when the skewer never made it
  back (for example after the owner changed dimension).
- Hitting a blocking player put the cooldown on nothing instead of on their shield.
- Holding the Bone Skewer while sneaking made low-health mobs glow for **every** player and
  cleared glowing caused by other sources (Spectral Arrows, the Glowing effect).
- Network packets were accepted from any mod version (protocol now checked).
- Removed the unused example config and the dead Forge loot table.

## [1.0.0]

- Initial release for Forge 1.20.1.
