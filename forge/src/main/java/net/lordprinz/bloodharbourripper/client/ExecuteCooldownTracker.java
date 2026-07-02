package net.lordprinz.bloodharbourripper.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExecuteCooldownTracker {
    private static final Map<UUID, Long> cooldownMap = new HashMap<>();
    private static final long COOLDOWN_DURATION = 100;

    public static boolean canExecute(UUID playerUUID, long currentTick) {
        Long lastExecuteTime = cooldownMap.get(playerUUID);

        if (lastExecuteTime == null) {
            return true;
        }

        return currentTick - lastExecuteTime >= COOLDOWN_DURATION;
    }

    public static void markExecuted(UUID playerUUID, long currentTick) {
        cooldownMap.put(playerUUID, currentTick);
    }

    public static long getRemainingCooldown(UUID playerUUID, long currentTick) {
        Long lastExecuteTime = cooldownMap.get(playerUUID);

        if (lastExecuteTime == null) {
            return 0;
        }

        long elapsed = currentTick - lastExecuteTime;
        return Math.max(0, COOLDOWN_DURATION - elapsed);
    }

    public static void clearCooldown(UUID playerUUID) {
        cooldownMap.remove(playerUUID);
    }
}

