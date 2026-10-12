package net.lordprinz.bloodharbourripper.client;

import net.lordprinz.bloodharbourripper.ability.Targeting;
import net.lordprinz.bloodharbourripper.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/** Client-only outline of enemies that "Death From Below" could execute right now. */
public final class ExecuteHighlight {
    private ExecuteHighlight() {}

    public static boolean shouldHighlight(Entity entity) {
        if (!(entity instanceof LivingEntity target)) return false;
        Player player = Minecraft.getInstance().player;
        return player != null
                && player.getMainHandItem().is(ModItems.BONE_SKEWER.get())
                && Targeting.isValidExecuteTarget(player, target);
    }
}
