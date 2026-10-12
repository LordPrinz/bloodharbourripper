package net.lordprinz.bloodharbourripper.item;

import net.lordprinz.bloodharbourripper.entity.BoneSkewerEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Remembers each player's skewer in flight. Server thread only — the client never sees this map,
 * which also keeps singleplayer (client + integrated server in one JVM) consistent.
 */
public final class SkewerTracker {
    private static final Map<UUID, BoneSkewerEntity> ACTIVE = new HashMap<>();

    private SkewerTracker() {}

    public static void track(Player player, BoneSkewerEntity skewer) {
        ACTIVE.put(player.getUUID(), skewer);
    }

    public static void untrack(Player player, BoneSkewerEntity skewer) {
        ACTIVE.remove(player.getUUID(), skewer);
    }

    /** The player's skewer, or {@code null} if it no longer exists (picked up, unloaded, killed...). */
    public static BoneSkewerEntity get(Player player) {
        BoneSkewerEntity skewer = ACTIVE.get(player.getUUID());
        if (skewer != null && (skewer.isRemoved() || !skewer.isAlive())) {
            ACTIVE.remove(player.getUUID());
            return null;
        }
        return skewer;
    }
}
