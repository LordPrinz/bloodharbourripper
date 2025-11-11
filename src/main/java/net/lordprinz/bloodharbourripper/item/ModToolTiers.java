package net.lordprinz.bloodharbourripper.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModToolTiers {
    // Tier for bone_skewer_skeleton (Iron level durability: 250)
    public static final Tier BONE_SKEWER_SKELETON = new ForgeTier(
            2,
            250,  // Iron sword durability
            6.0f,
            3.0f,
            14,
            BlockTags.NEEDS_IRON_TOOL,
            () -> Ingredient.EMPTY
    );

    // Tier for bone_skewer_raw (Diamond level durability: 1561)
    public static final Tier BONE_SKEWER_RAW = new ForgeTier(
            3,
            1561,  // Diamond sword durability
            8.0f,
            4.0f,
            10,
            BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.EMPTY
    );

    // Tier for bone_skewer (Netherite level durability: 2031)
    public static final Tier BONE_SKEWER_ADVANCED = new ForgeTier(
            4,
            2031,  // Netherite sword durability
            9.0f,
            9.0f,
            15,
            BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.EMPTY
    );
}



