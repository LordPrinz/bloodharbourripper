package net.lordprinz.bloodharbourripper.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModToolTiers {
    public static final Tier BONE_SKEWER = new ForgeTier(
            2,
            250,
            6.0f,
            3.0f,
            14,
            BlockTags.NEEDS_IRON_TOOL,
            () -> Ingredient.EMPTY
    );
}

