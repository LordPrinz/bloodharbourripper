package net.lordprinz.bloodharbourripper.network;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class DashPacket {
    private static final Map<UUID, Long> dashCooldowns = new HashMap<>();
    private static final long COOLDOWN_TICKS = 100;

    public DashPacket() {
    }

    public DashPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                long currentTime = player.level().getGameTime();

                if (dashCooldowns.containsKey(player.getUUID())) {
                    long lastDash = dashCooldowns.get(player.getUUID());
                    if (currentTime - lastDash < COOLDOWN_TICKS) {
                        return;
                    }
                }

                dashCooldowns.put(player.getUUID(), currentTime);

                Vec3 lookVec = player.getLookAngle();
                Vec3 startPos = player.position().add(0, player.getEyeHeight() * 0.5, 0);

                performDash(player, startPos, lookVec);
            }
        });
        return true;
    }

    private void performDash(ServerPlayer player, Vec3 startPos, Vec3 direction) {
        ServerLevel level = (ServerLevel) player.level();

        level.playSound(null, player.blockPosition(),
                net.lordprinz.bloodharbourripper.sound.ModSounds.DASH_SOUND.get(),
                net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);

        net.lordprinz.bloodharbourripper.entity.DashPhantomEntity phantom =
                new net.lordprinz.bloodharbourripper.entity.DashPhantomEntity(level, player);
        phantom.setPos(startPos);
        level.addFreshEntity(phantom);

        Vec3 dashVelocity = direction.scale(1.8);
        player.setDeltaMovement(dashVelocity.x, dashVelocity.y * 0.5, dashVelocity.z);
        player.hurtMarked = true;

        level.sendParticles(ParticleTypes.CLOUD,
                player.getX(), player.getY() + 0.5, player.getZ(),
                20, 0.3, 0.3, 0.3, 0.1);
    }


    public static long getRemainingCooldown(UUID playerUUID, long currentTime) {
        if (dashCooldowns.containsKey(playerUUID)) {
            long lastDash = dashCooldowns.get(playerUUID);
            long elapsed = currentTime - lastDash;
            return Math.max(0, COOLDOWN_TICKS - elapsed);
        }
        return 0;
    }
}

