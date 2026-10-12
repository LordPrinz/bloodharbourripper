package net.lordprinz.bloodharbourripper.ability;

import net.lordprinz.bloodharbourripper.compat.Compat;
import net.lordprinz.bloodharbourripper.entity.DashPhantomEntity;
import net.lordprinz.bloodharbourripper.registry.ModItems;
import net.lordprinz.bloodharbourripper.registry.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/** "Phantom Undertow" (Shift + X while holding the Bone Skewer): a forward dash leaving a damaging afterimage. */
public final class DashAbility {
    public static final int COOLDOWN_TICKS = 100;
    private static final double DASH_SPEED = 3.6;

    private static final AbilityCooldowns SERVER_COOLDOWNS = new AbilityCooldowns(COOLDOWN_TICKS);

    private DashAbility() {}

    public static boolean canUse(Player player) {
        return player.isShiftKeyDown() && player.getMainHandItem().is(ModItems.BONE_SKEWER.get());
    }

    public static void handleRequest(ServerPlayer player) {
        if (!canUse(player) || !(player.level() instanceof ServerLevel level)) return;
        long now = level.getGameTime();
        if (SERVER_COOLDOWNS.remaining(player.getUUID(), now) > 0) return;
        SERVER_COOLDOWNS.trigger(player.getUUID(), now);

        Vec3 look = player.getLookAngle();
        Vec3 start = player.position().add(0, player.getEyeHeight() * 0.5, 0);

        level.playSound(null, player.blockPosition(), ModSounds.DASH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        level.addFreshEntity(DashPhantomEntity.create(level, player, start.subtract(look.scale(1.5))));

        Vec3 velocity = look.scale(DASH_SPEED);
        player.setDeltaMovement(velocity.x, velocity.y * 0.5, velocity.z);
        Compat.syncVelocity(player);
        level.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY() + 0.5, player.getZ(), 20, 0.3, 0.3, 0.3, 0.1);
    }
}
