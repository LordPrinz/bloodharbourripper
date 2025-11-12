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

                    // Sprawdź czy cel nie jest sojusznikiem
                    boolean isAlly = false;

                    // Sprawdź czy to gracz z tej samej drużyny
                    if (target instanceof ServerPlayer targetPlayer) {
                        if (player.getTeam() != null && player.getTeam().equals(targetPlayer.getTeam())) {
                            isAlly = true;
                        }
                    }

                    // Sprawdź czy mob jest oswojony przez gracza lub jego drużynę
                    if (target instanceof net.minecraft.world.entity.TamableAnimal tamable) {
                        if (tamable.isTame() && tamable.getOwner() != null) {
                            if (tamable.getOwner().equals(player)) {
                                isAlly = true;
                            }
                            // Sprawdź czy właściciel jest w drużynie gracza
                            if (player.getTeam() != null && tamable.getOwner() instanceof ServerPlayer owner) {
                                if (player.getTeam().equals(owner.getTeam())) {
                                    isAlly = true;
                                }
                            }
                        }
                    }

                    // Sprawdź czy mob jest w tej samej drużynie co gracz
                    if (player.getTeam() != null && target.getTeam() != null) {
                        if (player.getTeam().equals(target.getTeam())) {
                            isAlly = true;
                        }
                    }

                    if (distance <= 12.0 && healthPercentage < 0.33f && !isAlly) {
                        player.level().playSound(null, target.blockPosition(),
                            net.lordprinz.bloodharbourripper.sound.ModSounds.EXECUTE_SOUND.get(),
                            SoundSource.PLAYERS, 1.5F, 1.0F);

                        ExecutionScheduler.scheduleExecution(target.getUUID(), player.getUUID(),
                            target.position(), 13);
                    }
                }
            }
        });
        return true;
    }

    @Mod.EventBusSubscriber
    public static class ExecutionScheduler {
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
                List<UUID> toRemove = new ArrayList<>();

                for (Map.Entry<UUID, ExecutionData> entry : scheduledExecutions.entrySet()) {
                    ExecutionData data = entry.getValue();
                    data.remainingTicks--;

                    if (data.remainingTicks <= 0) {
                        UUID entityUUID = entry.getKey();
                        UUID playerUUID = data.playerUUID;
                        Vec3 targetPosition = data.targetPosition;

                        ServerPlayer executingPlayer = null;
                        ServerLevel executingPlayerLevel = null;
                        for (ServerLevel searchLevel : event.getServer().getAllLevels()) {
                            executingPlayer = (ServerPlayer) searchLevel.getPlayerByUUID(playerUUID);
                            if (executingPlayer != null) {
                                executingPlayerLevel = searchLevel;
                                break;
                            }
                        }

                        boolean found = false;
                        for (ServerLevel searchLevel : event.getServer().getAllLevels()) {
                            for (Entity searchEntity : searchLevel.getAllEntities()) {
                                if (searchEntity.getUUID().equals(entityUUID) && searchEntity instanceof LivingEntity targetLiving && targetLiving.isAlive()) {
                                    if (targetLiving.isBlocking()) {
                                        targetLiving.stopUsingItem();
                                    }

                                    float executionDamage = targetLiving.getMaxHealth() * 100.0f;

                                    if (executingPlayer != null) {
                                        targetLiving.hurt(targetLiving.damageSources().playerAttack(executingPlayer), executionDamage);

                                        if (targetLiving.isAlive()) {
                                            targetLiving.hurt(targetLiving.damageSources().fellOutOfWorld(), executionDamage);
                                        }

                                        executingPlayer.teleportTo(targetPosition.x, targetPosition.y, targetPosition.z);

                                        spawnOceanParticles(executingPlayerLevel, targetPosition);
                                    } else {
                                        targetLiving.hurt(targetLiving.damageSources().fellOutOfWorld(), executionDamage);
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

            level.sendParticles(net.minecraft.core.particles.ParticleTypes.BUBBLE_POP,
                position.x, position.y + 1, position.z,
                30, 0.5, 0.5, 0.5, 0.1);

            level.sendParticles(net.minecraft.core.particles.ParticleTypes.BUBBLE_COLUMN_UP,
                position.x, position.y, position.z,
                20, 0.3, 0.3, 0.3, 0.1);

            level.sendParticles(net.minecraft.core.particles.ParticleTypes.SPLASH,
                position.x, position.y + 0.5, position.z,
                40, 0.8, 0.5, 0.8, 0.2);

            level.sendParticles(net.minecraft.core.particles.ParticleTypes.FISHING,
                position.x, position.y + 1.5, position.z,
                15, 0.4, 0.4, 0.4, 0.15);
        }
    }
}

