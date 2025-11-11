package net.lordprinz.bloodharbourripper.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExecuteCooldownTracker {
    private static final Map<UUID, Long> cooldownMap = new HashMap<>();
    private static final long COOLDOWN_DURATION = 100; // 5 sekund = 100 ticków (20 ticks/sec * 5)

    /**
     * Sprawdź czy gracz może wykonać egzekucję
     */
    public static boolean canExecute(UUID playerUUID, long currentTick) {
        Long lastExecuteTime = cooldownMap.get(playerUUID);

        if (lastExecuteTime == null) {
            return true;
        }

        return currentTick - lastExecuteTime >= COOLDOWN_DURATION;
    }

    /**
     * Zarejestruj wykonanie egzekucji
     */
    public static void markExecuted(UUID playerUUID, long currentTick) {
        cooldownMap.put(playerUUID, currentTick);
    }

    /**
     * Pobierz pozostały czas cooldownu w tickach
     */
    public static long getRemainingCooldown(UUID playerUUID, long currentTick) {
        Long lastExecuteTime = cooldownMap.get(playerUUID);

        if (lastExecuteTime == null) {
            return 0;
        }

        long elapsed = currentTick - lastExecuteTime;
        return Math.max(0, COOLDOWN_DURATION - elapsed);
    }

    /**
     * Wyczyść cooldown dla gracza (użyteczne przy testowaniu)
     */
    public static void clearCooldown(UUID playerUUID) {
        cooldownMap.remove(playerUUID);
    }
}

