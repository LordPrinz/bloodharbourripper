package net.lordprinz.bloodharbourripper.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class ExecuteEntityPacket {
    private final int entityId;

    public ExecuteEntityPacket(int entityId) {
        this.entityId = entityId;
    }

    public ExecuteEntityPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                Entity entity = player.level().getEntity(entityId);

                if (entity instanceof LivingEntity target && target.isAlive()) {
                    double distance = player.distanceTo(target);
                    float healthPercentage = target.getHealth() / target.getMaxHealth();

                    // Sprawdź czy mob jest w zasięgu (12 bloków) i ma <33% HP
                    if (distance <= 12.0 && healthPercentage < 0.33f) {
                        // Odtwórz CUSTOM dźwięk egzekucji NATYCHMIAST
                        player.level().playSound(null, target.blockPosition(),
                            net.lordprinz.bloodharbourripper.sound.ModSounds.EXECUTE_SOUND.get(),
                            SoundSource.PLAYERS, 1.5F, 1.0F);

                        // Zaplanuj śmierć moba po 0.65 sekundy używając ExecutionScheduler
                        // Przekaż UUID gracza i pozycję moba
                        ExecutionScheduler.scheduleExecution(target.getUUID(), player.getUUID(),
                            target.position(), 13); // 13 ticków = 0.65 sekundy
                    }
                }
            }
        });
        return true;
    }

    // Scheduler do opóźnionej egzekucji
    @Mod.EventBusSubscriber
    public static class ExecutionScheduler {
        // Mapa: UUID moba -> (UUID gracza, pozycja moba, pozostały czas)
        private static final Map<UUID, ExecutionData> scheduledExecutions = new HashMap<>();

        private static class ExecutionData {
            final UUID playerUUID;
            final Vec3 targetPosition;
            int remainingTicks;

            ExecutionData(UUID playerUUID, Vec3 targetPosition, int remainingTicks) {
                this.playerUUID = playerUUID;
                this.targetPosition = targetPosition;
                this.remainingTicks = remainingTicks;
            }
        }

        public static void scheduleExecution(UUID entityUUID, UUID playerUUID, Vec3 targetPosition, int delayTicks) {
            scheduledExecutions.put(entityUUID, new ExecutionData(playerUUID, targetPosition, delayTicks));
        }

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                // Lista UUID do usunięcia
                List<UUID> toRemove = new ArrayList<>();

                for (Map.Entry<UUID, ExecutionData> entry : scheduledExecutions.entrySet()) {
                    ExecutionData data = entry.getValue();
                    data.remainingTicks--;

                    if (data.remainingTicks <= 0) {
                        // Czas wykonać egzekucję
                        UUID entityUUID = entry.getKey();
                        UUID playerUUID = data.playerUUID;
                        Vec3 targetPosition = data.targetPosition;

                        // Znajdź gracza
                        ServerPlayer executingPlayer = null;
                        ServerLevel executingPlayerLevel = null;
                        for (ServerLevel searchLevel : event.getServer().getAllLevels()) {
                            executingPlayer = (ServerPlayer) searchLevel.getPlayerByUUID(playerUUID);
                            if (executingPlayer != null) {
                                executingPlayerLevel = searchLevel;
                                break;
                            }
                        }

                        // Znajdź i zabij encję
                        boolean found = false;
                        for (ServerLevel searchLevel : event.getServer().getAllLevels()) {
                            for (Entity searchEntity : searchLevel.getAllEntities()) {
                                if (searchEntity.getUUID().equals(entityUUID) && searchEntity instanceof LivingEntity targetLiving && targetLiving.isAlive()) {
                                    // Użyj hurt() z player damage source aby gracz dostał XP
                                    if (executingPlayer != null) {
                                        targetLiving.hurt(targetLiving.damageSources().playerAttack(executingPlayer), Float.MAX_VALUE);

                                        // Teleportuj gracza do pozycji moba
                                        executingPlayer.teleportTo(targetPosition.x, targetPosition.y, targetPosition.z);

                                        // Spawn ocean particles w miejscu egzekucji
                                        spawnOceanParticles(executingPlayerLevel, targetPosition);
                                    } else {
                                        targetLiving.hurt(targetLiving.damageSources().generic(), Float.MAX_VALUE);
                                    }
                                    found = true;
                                    break;
                                }
                            }
                            if (found) break;
                        }

                        toRemove.add(entityUUID);
                    }
                }

                // Usuń wykonane egzekucje
                for (UUID uuid : toRemove) {
                    scheduledExecutions.remove(uuid);
                }
            }
        }

        private static void spawnOceanParticles(ServerLevel level, Vec3 position) {
            if (level == null) return;

            // Spawn różnych typów ocean particles
            // BUBBLE_POP - bąbelki pękające
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.BUBBLE_POP,
                position.x, position.y + 1, position.z,
                30, 0.5, 0.5, 0.5, 0.1);

            // BUBBLE_COLUMN_UP - kolumna bąbelków w górę
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.BUBBLE_COLUMN_UP,
                position.x, position.y, position.z,
                20, 0.3, 0.3, 0.3, 0.1);

            // SPLASH - splash wody
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.SPLASH,
                position.x, position.y + 0.5, position.z,
                40, 0.8, 0.5, 0.8, 0.2);

            // FISHING - efekt wędkarski (ocean look)
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.FISHING,
                position.x, position.y + 1.5, position.z,
                15, 0.4, 0.4, 0.4, 0.15);
        }
    }
}

