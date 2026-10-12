package net.lordprinz.bloodharbourripper.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Per-player cooldown bookkeeping based on level game time. The server keeps the authoritative
 * instance; the client keeps its own only to show "on cooldown" messages without a round trip.
 */
public final class AbilityCooldowns {
    private final long durationTicks;
    private final Map<UUID, Long> lastUse = new HashMap<>();

    public AbilityCooldowns(long durationTicks) {
        this.durationTicks = durationTicks;
    }

    public long remaining(UUID player, long gameTime) {
        Long last = lastUse.get(player);
        if (last == null || gameTime < last) return 0; // never used, or time went backwards (/time set)
        return Math.max(0, durationTicks - (gameTime - last));
    }

    public void trigger(UUID player, long gameTime) {
        lastUse.put(player, gameTime);
    }

    public void clear() {
        lastUse.clear();
    }
}
