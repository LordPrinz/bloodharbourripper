package net.lordprinz.bloodharbourripper.ability;

import net.lordprinz.bloodharbourripper.compat.Compat;
import net.lordprinz.bloodharbourripper.entity.ExecuteMarkerEntity;
import net.lordprinz.bloodharbourripper.registry.ModItems;
import net.lordprinz.bloodharbourripper.registry.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * "Death From Below" (Shift + C while holding the Bone Skewer): marks the nearest enemy under 33%
 * health; after {@link ExecuteMarkerEntity#DELAY_TICKS} it is killed, drops double loot and XP,
 * and the executor is pulled to its position.
 */
public final class ExecuteAbility {
    public static final double RANGE = 12.0;
    public static final float HEALTH_THRESHOLD = 0.33F;
    public static final int COOLDOWN_TICKS = 100;

    private static final AbilityCooldowns SERVER_COOLDOWNS = new AbilityCooldowns(COOLDOWN_TICKS);

    private ExecuteAbility() {}

    public static boolean canUse(Player player) {
        return player.isShiftKeyDown() && player.getMainHandItem().is(ModItems.BONE_SKEWER.get());
    }

    /** Server side of the C2S request. Everything the client claims is re-validated here. */
    public static void handleRequest(ServerPlayer player, int targetId) {
        if (!canUse(player) || !(player.level() instanceof ServerLevel level)) return;
        long now = level.getGameTime();
        if (SERVER_COOLDOWNS.remaining(player.getUUID(), now) > 0) return;

        Entity entity = level.getEntity(targetId);
        if (!(entity instanceof LivingEntity target) || !Targeting.isValidExecuteTarget(player, target)) return;

        SERVER_COOLDOWNS.trigger(player.getUUID(), now);
        level.playSound(null, target.blockPosition(), ModSounds.EXECUTE.get(), SoundSource.PLAYERS, 1.5F, 1.0F);
        level.addFreshEntity(ExecuteMarkerEntity.create(level, player, target));
    }

    public static void performExecution(ServerLevel level, UUID executorId, LivingEntity target) {
        ServerPlayer executor = executorId == null ? null : level.getServer().getPlayerList().getPlayer(executorId);
        if (target.isBlocking()) {
            target.stopUsingItem();
        }

        float damage = target.getMaxHealth() * 100.0F;
        if (executor != null) {
            Compat.hurt(target, level.damageSources().playerAttack(executor), damage);
        }
        if (target.isAlive()) {
            Compat.hurt(target, level.damageSources().fellOutOfWorld(), damage);
        }
        if (!target.isAlive()) {
            BonusLoot.dropAgain(level, target, executor);
        }

        if (executor != null && executor.level() == level) {
            executor.teleportTo(target.getX(), target.getY(), target.getZ());
            spawnOceanParticles(level, target.getX(), target.getY(), target.getZ());
        }
    }

    private static void spawnOceanParticles(ServerLevel level, double x, double y, double z) {
        level.sendParticles(ParticleTypes.BUBBLE_POP, x, y + 1, z, 30, 0.5, 0.5, 0.5, 0.1);
        level.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 20, 0.3, 0.3, 0.3, 0.1);
        level.sendParticles(ParticleTypes.SPLASH, x, y + 0.5, z, 40, 0.8, 0.5, 0.8, 0.2);
        level.sendParticles(ParticleTypes.FISHING, x, y + 1.5, z, 15, 0.4, 0.4, 0.4, 0.15);
    }
}
