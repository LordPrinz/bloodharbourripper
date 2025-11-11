package net.lordprinz.bloodharbourripper.item.custom;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoneSkewerTracker {
    private static final Map<UUID, AbstractArrow> activeSkewerMap = new HashMap<>();

    public static void trackSkewer(Player player, AbstractArrow skewer) {
        activeSkewerMap.put(player.getUUID(), skewer);
    }

    public static void removeSkewer(Player player) {
        activeSkewerMap.remove(player.getUUID());
    }

    public static AbstractArrow getActiveSkewer(Player player) {
        AbstractArrow skewer = activeSkewerMap.get(player.getUUID());
        // Sprawdź czy harpun nadal istnieje
        if (skewer != null && !skewer.isAlive()) {
            activeSkewerMap.remove(player.getUUID());
            return null;
        }
        return skewer;
    }

    public static boolean hasActiveSkewer(Player player) {
        return getActiveSkewer(player) != null;
    }
}

