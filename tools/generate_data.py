#!/usr/bin/env python3
"""Generates recipes and their unlock advancements for every data overlay in src/overlays.

JSON formats changed between Minecraft versions, so the same recipe has to exist once per overlay.
Edit RECIPES / ADVANCEMENTS below and run `python3 tools/generate_data.py` instead of editing the
JSON files by hand.
"""
import json
import pathlib
import shutil

NS = "bloodharbourripper"
ROOT = pathlib.Path(__file__).resolve().parent.parent / "src" / "overlays"

# overlay folder -> data format era
ERAS = {
    "1.20": "1.20",      # plural folders, {"item": ...} everywhere
    "1.21": "1.21",      # singular folders, {"item": ...} ingredients, {"id": ...} results
    "1.21.2": "1.21.2",  # ingredients are plain strings
    "26.3": "26.3",      # recipe_unlocked uses "recipes"
}

RECIPES = {
    # --- Components -------------------------------------------------------------------------------
    # A prismarine harpoon head around a nautilus shell, set on an iron socket.
    "harpoon_head": {
        "type": "shaped",
        "category": "misc",
        "pattern": [" P ", "PNP", " I "],
        "key": {"P": "minecraft:prismarine_shard", "N": "minecraft:nautilus_shell", "I": "minecraft:iron_ingot"},
        "unlocked_by": ["minecraft:nautilus_shell", "minecraft:prismarine_shard"],
    },
    # Bone blocks lashed together with string around a strand of kelp.
    "bound_haft": {
        "type": "shaped",
        "category": "misc",
        "pattern": ["S B", " K ", "B S"],
        "key": {"S": "minecraft:string", "B": "minecraft:bone_block", "K": "minecraft:kelp"},
        "unlocked_by": ["minecraft:bone_block", "minecraft:kelp"],
    },
    # Echo shards bound around a sculk catalyst, sealed with prismarine crystals.
    "abyssal_core": {
        "type": "shaped",
        "category": "misc",
        "pattern": ["CEC", "EXE", "CEC"],
        "key": {"C": "minecraft:prismarine_crystals", "E": "minecraft:echo_shard", "X": "minecraft:sculk_catalyst"},
        "unlocked_by": ["minecraft:echo_shard", "minecraft:sculk_catalyst"],
    },
    # A Heart of the Sea crowned with a trident and fed by a wither rose.
    "drowned_heart": {
        "type": "shaped",
        "category": "misc",
        "pattern": [" T ", "CHC", " W "],
        "key": {"T": "minecraft:trident", "C": "minecraft:prismarine_crystals",
                "H": "minecraft:heart_of_the_sea", "W": "minecraft:wither_rose"},
        "unlocked_by": ["minecraft:heart_of_the_sea", "minecraft:trident"],
    },
    # --- Skewers ----------------------------------------------------------------------------------
    # Tier 1 — the head and the haft joined over a bone block pommel.
    "bone_skewer_skeleton": {
        "type": "shaped",
        "pattern": ["  H", " F ", "B  "],
        "key": {"H": f"{NS}:harpoon_head", "F": f"{NS}:bound_haft", "B": "minecraft:bone_block"},
        "unlocked_by": [f"{NS}:harpoon_head", f"{NS}:bound_haft"],
    },
    # Tier 2 — smithing upgrade (keeps enchantments and durability).
    "bone_skewer_raw": {
        "type": "smithing",
        "template": f"{NS}:jaull_fish_vial",
        "base": f"{NS}:bone_skewer_skeleton",
        "addition": f"{NS}:abyssal_core",
        "unlocked_by": [f"{NS}:jaull_fish_vial", f"{NS}:abyssal_core"],
    },
    # Tier 3 — the Bloodharbor Ripper's own harpoon.
    "bone_skewer": {
        "type": "smithing",
        "template": f"{NS}:jaull_fish_vial",
        "base": f"{NS}:bone_skewer_raw",
        "addition": f"{NS}:drowned_heart",
        "unlocked_by": [f"{NS}:jaull_fish_vial", f"{NS}:drowned_heart"],
    },
}


def ingredient(item, era):
    return item if era in ("1.21.2", "26.3") else {"item": item}


def result(item, era):
    return {"item": item, "count": 1} if era == "1.20" else {"id": item, "count": 1}


def item_predicate(item, era):
    return {"items": [item]} if era == "1.20" else {"items": item}


def recipe_json(name, spec, era):
    if spec["type"] == "shaped":
        recipe = {
            "type": "minecraft:crafting_shaped",
            "category": spec.get("category", "equipment"),
            "pattern": spec["pattern"],
            "key": {k: ingredient(v, era) for k, v in spec["key"].items()},
            "result": result(f"{NS}:{name}", era),
            "show_notification": True,
        }
        if spec.get("category", "equipment") == "equipment":
            recipe["group"] = "bone_skewer"
        return recipe
    return {
        "type": "minecraft:smithing_transform",
        "template": ingredient(spec["template"], era),
        "base": ingredient(spec["base"], era),
        "addition": ingredient(spec["addition"], era),
        "result": result(f"{NS}:{name}", era),
    }


def unlock_advancement(name, spec, era):
    criteria = {
        f"has_{item.split(':')[1]}": {
            "trigger": "minecraft:inventory_changed",
            "conditions": {"items": [item_predicate(item, era)]},
        }
        for item in spec["unlocked_by"]
    }
    key = "recipes" if era == "26.3" else "recipe"
    criteria["has_the_recipe"] = {
        "trigger": "minecraft:recipe_unlocked",
        "conditions": {key: f"{NS}:{name}"},
    }
    return {
        "parent": "minecraft:recipes/root",
        "criteria": criteria,
        "requirements": [list(criteria)],
        "rewards": {"recipes": [f"{NS}:{name}"]},
    }


def sink_them_all(era):
    icon_key = "item" if era == "1.20" else "id"
    return {
        "parent": "minecraft:story/root",
        "display": {
            "icon": {icon_key: f"{NS}:bone_skewer"},
            "title": {"translate": f"advancement.{NS}.sink_them_all.title"},
            "description": {"translate": f"advancement.{NS}.sink_them_all.description"},
            "frame": "challenge",
            "show_toast": True,
            "announce_to_chat": True,
            "hidden": False,
        },
        "criteria": {
            "has_bone_skewer": {
                "trigger": "minecraft:inventory_changed",
                "conditions": {"items": [item_predicate(f"{NS}:bone_skewer", era)]},
            }
        },
        "rewards": {"experience": 100},
    }


# Vanilla item tags the skewers join. `minecraft:swords` feeds every sword enchantment tag
# (enchantable/sword, sharp_weapon, melee_weapon, fire_aspect, sweeping, durability, ...).
ITEM_TAGS = {
    "minecraft:swords": [f"{NS}:bone_skewer_skeleton", f"{NS}:bone_skewer_raw", f"{NS}:bone_skewer"],
}


def write(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2) + "\n")


def main():
    for overlay, era in ERAS.items():
        data = ROOT / overlay / "data" / NS
        plural = era == "1.20"
        recipe_dir = data / ("recipes" if plural else "recipe")
        advancement_dir = data / ("advancements" if plural else "advancement")
        shutil.rmtree(recipe_dir, ignore_errors=True)
        shutil.rmtree(advancement_dir, ignore_errors=True)
        for name, spec in RECIPES.items():
            write(recipe_dir / f"{name}.json", recipe_json(name, spec, era))
            write(advancement_dir / "recipes" / "bone_skewer" / f"{name}.json", unlock_advancement(name, spec, era))
        write(advancement_dir / "sink_them_all.json", sink_them_all(era))
        for tag, values in ITEM_TAGS.items():
            namespace, path = tag.split(":")
            tag_dir = ROOT / overlay / "data" / namespace / "tags" / ("items" if plural else "item")
            write(tag_dir / f"{path}.json", {"replace": False, "values": values})
        print(f"generated {overlay}")


if __name__ == "__main__":
    main()
