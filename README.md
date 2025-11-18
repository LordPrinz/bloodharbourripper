# The Bloodharbour Ripper

![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green.svg)
![Forge](https://img.shields.io/badge/Forge-47.4.0-orange.svg)
![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)

A dark and mysterious Minecraft mod that introduces powerful bone harpoons with unique abilities, deadly execution mechanics, and phantom dash abilities.

## 📋 Table of Contents

- [Features](#features)
- [Items](#items)
- [Abilities](#abilities)
- [Crafting Recipes](#crafting-recipes)
- [Rare Loot](#rare-loot)
- [Requirements](#requirements)
- [Development](#development)
- [Building](#building)
- [Future Plans](#future-plans)
- [License](#license)

## ✨ Features

### Combat System
- Three-tier harpoon progression system
- Unique throwing mechanics with boomerang-style return
- Shield-breaking attacks
- Pull enemies towards you with increasing strength per tier
- Custom sound effects and visual effects

### Execution Mechanic
- Execute low-health enemies (below 33% HP)
- Stunning visual effects with underwater particles
- Teleportation to target location
- Cooldown-based system to prevent abuse
- Team-aware (won't execute allies)

### Phantom Dash
- Leave behind a spectral phantom that returns to you
- Damages and stuns enemies in its path
- Double experience and loot from execution kills
- Directional dash with extended range

## 🗡️ Items

### Bone Skewer (Skeleton)
**Base Tier Harpoon**

- **Damage:** Iron Sword + 1
- **Durability:** Iron tier (250 uses)
- **Range:** Short-medium (fishing rod range)
- **Special:** Can be thrown and recalled
- **Pull Strength:** Weak

The entry-level bone harpoon, crafted from basic materials. While not the most powerful, it introduces the core mechanics of the weapon system.

### Bone Skewer (Raw)
**Intermediate Tier Harpoon**

- **Damage:** Diamond Sword + 1
- **Durability:** Diamond tier (1561 uses)
- **Range:** Extended (trident range)
- **Special:** 
  - Inflicts Slowness II for 2 seconds on hit
  - Enhanced pull strength
  - Faster throw speed
- **Pull Strength:** Moderate

An enhanced version infused with mysterious Jaull Fish essence, providing significant combat improvements.

### Bone Skewer (Advanced)
**Ultimate Tier Harpoon**

- **Damage:** Netherite Sword with Sharpness V + 2 (15 damage)
- **Durability:** Netherite tier (2031 uses)
- **Range:** Extended (trident range)
- **Special:**
  - Inflicts Slowness II for 4 seconds
  - Inflicts Wither effect for 32 seconds (8x duration)
  - Fastest throw speed (2x base speed)
  - Execution ability (see below)
  - Phantom Dash ability
  - Double loot and XP from executions
- **Pull Strength:** Strong

The pinnacle of bone harpoon technology, capable of devastating attacks and special abilities.

### Jaull Fish Vial
**Rare Crafting Material**

A mysterious vial containing essence from deep ocean creatures. Required for crafting advanced bone harpoons.

- **Rarity:** Very Rare
- **Source:** Ancient City chests (33% chance, max 1 per chest)

## 🎯 Abilities

### Harpoon Throw & Recall
1. **Charge:** Hold right-click to charge the harpoon
2. **Throw:** Release to launch the harpoon
3. **Recall:** Right-click again while airborne to recall immediately
4. **Auto-Return:** Harpoon automatically returns if it hits a block
5. **Cooldown:** Active until harpoon returns

**Mechanics:**
- Pulls hit entities towards the player
- Breaks shields like axes
- Custom charging and release sounds
- Dynamic in-flight rotation
- Cannot be lost (always returns)

### Death From Below (Execution)
**Keybind:** `Shift + C` (customizable in controls)

**Requirements:**
- Must be holding Bone Skewer (Advanced)
- Target must be below 33% health
- 5-second cooldown between executions

**Visual Indicators:**
- Low-health enemies are highlighted
- Highlighted enemies can be executed through walls
- Large "X" marker appears under viable targets

**Execution Process:**
1. Press `Shift + C` while looking at highlighted enemy
2. Anvil sound plays immediately
3. 0.65-second charge time with particle effects
4. Instant kill with maximum damage
5. Teleport to target's location with ocean particles
6. Double XP and loot drops
7. Works on most entities, special handling for bosses

**Team System:**
- Will not highlight allied players
- Will not execute team members
- Respects multiplayer team assignments

### Phantom Dash
**Keybind:** `X` (customizable in controls)

**Requirements:**
- Must be holding any Bone Skewer
- 5-second cooldown

**Mechanics:**
1. Leaves a spectral phantom at starting position
2. Dash forward quickly (2x distance for Advanced tier)
3. After 0.6 seconds, phantom rapidly returns to player
4. Phantom has player's skin and proper orientation
5. Enemies caught in phantom's return path are:
   - Stunned briefly
   - Damaged
   - Cannot attack during stun
6. Custom dash and hit sounds

**Visual Effects:**
- Phantom displays player's actual skin
- Particle effects during dash
- Distinct sound on enemy hit

## 📜 Crafting Recipes

### Bone Skewer (Skeleton)
```
Standard crafting recipe
(Check JEI in-game for details)
```

### Bone Skewer (Raw)
```
Center: Bone Skewer (Skeleton)
Surrounding: 1x Jaull Fish Vial
```

### Bone Skewer (Advanced)
```
Center: Bone Skewer (Raw)
Surrounding: 8x Jaull Fish Vial
```
*Total cost: 9 Jaull Fish Vials to go from Raw to Advanced*

## 🎁 Rare Loot

### Jaull Fish Vial
- **Location:** Ancient City chests
- **Chance:** 33% per chest
- **Quantity:** Maximum 1 per chest

Ancient Cities are dangerous underground structures found in the Deep Dark biome. Be prepared for Warden encounters when searching for these valuable materials.

## 📦 Requirements

### Minecraft
- **Version:** 1.20.1
- **Type:** Java Edition

### Mod Loader
- **Forge:** 47.4.0 or higher
- **Minimum:** Forge 47.x
- **Recommended:** Latest Forge 47.x

### Java
- **Version:** Java 17 or higher
- **Note:** Minecraft 1.20.1 ships with Java 17

### Optional
- **JEI (Just Enough Items):** Recommended for viewing recipes

## 🛠️ Development

### Setup
```bash
# Clone the repository
git clone <repository-url>
cd bloodharbourripper

# Setup the development environment
./gradlew genIntellijRuns  # For IntelliJ IDEA
./gradlew genEclipseRuns   # For Eclipse
```

### Project Structure
```
bloodharbourripper/
├── src/main/java/net/lordprinz/bloodharbourripper/
│   ├── BloodHarbourRipper.java          # Main mod class
│   ├── Config.java                       # Configuration
│   ├── client/                           # Client-side rendering
│   │   ├── BoneSkewerSkeletonRenderer.java
│   │   ├── BoneSkewerRawRenderer.java
│   │   ├── BoneSkewerAdvancedRenderer.java
│   │   ├── DashPhantomRenderer.java
│   │   └── HarpoonChargingSoundHandler.java
│   ├── entity/                           # Custom entities
│   │   └── DashPhantomEntity.java
│   ├── item/                             # Items and tools
│   │   ├── ModItems.java
│   │   ├── ModToolTiers.java
│   │   └── custom/
│   │       ├── BoneSkewerSkeletonItem.java
│   │       ├── BoneSkewerRawItem.java
│   │       ├── BoneSkewerAdvancedItem.java
│   │       ├── BoneSkewerTracker.java
│   │       └── ModEntityTypes.java
│   ├── loot/                             # Loot modifications
│   │   └── AddItemModifier.java
│   ├── network/                          # Networking
│   │   ├── ModNetworking.java
│   │   ├── ExecuteEntityPacket.java
│   │   └── DashPacket.java
│   └── sound/                            # Custom sounds
│       └── ModSounds.java
├── src/main/resources/
│   ├── assets/bloodharbourripper/
│   │   ├── textures/item/               # Item textures
│   │   ├── models/item/                 # Item models
│   │   ├── sounds/                      # Sound files
│   │   └── lang/                        # Translations
│   └── data/bloodharbourripper/
│       ├── recipes/                     # Crafting recipes
│       └── loot_modifiers/              # Loot table modifications
└── build.gradle                         # Build configuration
```

### Key Components

#### Custom Entities
- **BoneSkewerSkeletonEntity:** Basic thrown harpoon projectile
- **BoneSkewerRawEntity:** Enhanced harpoon with slowness effect
- **BoneSkewerAdvancedEntity:** Ultimate harpoon with execution ability
- **DashPhantomEntity:** Spectral phantom for dash ability

#### Network Packets
- **ExecuteEntityPacket:** Handles execution ability synchronization
- **DashPacket:** Handles phantom dash ability synchronization

#### Client Rendering
- Custom renderers for each harpoon tier
- Phantom renderer with player skin support
- Dynamic rotation and positioning
- Particle effects system

#### Sound System
- Custom charging sounds
- Release and return sounds
- Execution sound effects
- Dash and impact sounds
- Automatic sound cleanup on release

### Technologies Used
- **Forge Mod Loader:** 47.4.0+
- **Parchment Mappings:** 2023.09.03-1.20.1
- **Gradle:** 8.8
- **Java:** 17

## 🔨 Building

### Build the Mod
```bash
# Build the JAR file
./gradlew build

# Output location
build/libs/bloodharbourripper-1.0.0.jar
```

### For Production
```bash
# Clean and build
./gradlew clean build

# The production JAR will be in:
build/libs/bloodharbourripper-1.0.0.jar
```

### Install
1. Build the JAR file using the command above
2. Copy `bloodharbourripper-1.0.0.jar` to your Minecraft `mods/` folder
3. Launch Minecraft with Forge 1.20.1

## 🚀 Future Plans

### Upcoming Features

#### The Boss
A powerful maritime boss encounter is planned, featuring:
- Unique combat mechanics utilizing water and ocean themes
- Multiple phases with increasing difficulty
- Special rewards upon defeat
- Integration with the harpoon weapon system
- Summoning mechanism using Jaull Fish Vials

#### Additional Content
- More weapon variants and upgrades
- New execution animations
- Additional ocean-themed items
- Enchantments specific to bone harpoons
- More rare loot opportunities
- Configuration options for ability cooldowns and damage values

### Roadmap
- **v1.1.0:** Boss implementation
- **v1.2.0:** Additional weapons and abilities
- **v1.3.0:** Enchantment system
- **v2.0.0:** Complete overhaul with new content

## 📄 License

All Rights Reserved

Copyright (c) 2025 LordPrinz

This mod is the property of LordPrinz. All rights reserved.

## 🤝 Credits

### Development
- **LordPrinz** - Lead Developer & Creator

### Special Thanks
- Minecraft Forge team for the modding framework
- The Minecraft modding community for tools and resources

## 📞 Support

For bug reports, feature requests, or general discussion:
- Check the Issues tab on the repository

