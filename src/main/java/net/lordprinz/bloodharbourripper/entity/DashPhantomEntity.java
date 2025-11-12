package net.lordprinz.bloodharbourripper.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DashPhantomEntity extends Entity {
    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(DashPhantomEntity.class, EntityDataSerializers.INT);
    private static final int WAIT_TICKS = 12;
    private static final float DASH_DAMAGE = 8.0f;
    private static final double RETURN_SPEED = 2.0;
    private static final double HITBOX_RADIUS = 1.5;

    private int ticksExisted = 0;
    private boolean returning = false;
    private Set<UUID> hitEntities = new HashSet<>();

    public DashPhantomEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public DashPhantomEntity(Level level, ServerPlayer owner) {
        super(net.lordprinz.bloodharbourripper.item.custom.ModEntityTypes.DASH_PHANTOM.get(), level);
        this.noPhysics = true;
        this.setOwner(owner);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_ID, -1);
    }

    public void setOwner(ServerPlayer owner) {
        this.entityData.set(OWNER_ID, owner.getId());
    }

    public ServerPlayer getOwner() {
        if (this.level() instanceof ServerLevel serverLevel) {
            int ownerId = this.entityData.get(OWNER_ID);
            Entity entity = serverLevel.getEntity(ownerId);
            if (entity instanceof ServerPlayer player) {
                return player;
            }
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            spawnParticles();
            return;
        }

        ticksExisted++;

        ServerPlayer owner = getOwner();
        if (owner == null || !owner.isAlive()) {
            this.discard();
            return;
        }

        if (!returning && ticksExisted >= WAIT_TICKS) {
            returning = true;
        }

        if (returning) {
            Vec3 ownerPos = owner.position().add(0, owner.getBbHeight() * 0.5, 0);
            Vec3 currentPos = this.position();
            Vec3 direction = ownerPos.subtract(currentPos).normalize();

            double distanceToOwner = currentPos.distanceTo(ownerPos);

            if (distanceToOwner < 1.0) {
                this.discard();
                return;
            }

            Vec3 nextPos = currentPos.add(direction.scale(RETURN_SPEED));
            this.setPos(nextPos);

            damageEntitiesOnPath(currentPos, nextPos, owner);
        }

        spawnParticles();
    }

    private void damageEntitiesOnPath(Vec3 from, Vec3 to, ServerPlayer owner) {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        AABB searchBox = new AABB(
                Math.min(from.x, to.x) - HITBOX_RADIUS,
                Math.min(from.y, to.y) - HITBOX_RADIUS,
                Math.min(from.z, to.z) - HITBOX_RADIUS,
                Math.max(from.x, to.x) + HITBOX_RADIUS,
                Math.max(from.y, to.y) + HITBOX_RADIUS,
                Math.max(from.z, to.z) + HITBOX_RADIUS
        );

        Vec3 direction = to.subtract(from).normalize();
        double pathLength = from.distanceTo(to);

        List<Entity> entities = serverLevel.getEntities(this, searchBox);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity target && entity != owner && !hitEntities.contains(target.getUUID())) {
                if (isEnemy(owner, target)) {
                    Vec3 toTarget = target.position().subtract(from);
                    double distanceAlongPath = toTarget.dot(direction);

                    if (distanceAlongPath >= 0 && distanceAlongPath <= pathLength) {
                        Vec3 closestPoint = from.add(direction.scale(distanceAlongPath));
                        double perpDistance = target.position().distanceTo(closestPoint);

                        if (perpDistance <= HITBOX_RADIUS) {
                            hitEntities.add(target.getUUID());

                            target.hurt(target.damageSources().playerAttack(owner), DASH_DAMAGE);
                            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
                            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 255));
                            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 255));

                            serverLevel.playSound(null, target.blockPosition(),
                                    net.lordprinz.bloodharbourripper.sound.ModSounds.DASH_HIT.get(),
                                    net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);

                            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                                    target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(),
                                    5, 0.3, 0.3, 0.3, 0.0);
                        }
                    }
                }
            }
        }
    }

    private boolean isEnemy(ServerPlayer player, LivingEntity target) {
        if (target instanceof ServerPlayer targetPlayer) {
            if (player.getTeam() != null && player.getTeam().equals(targetPlayer.getTeam())) {
                return false;
            }
        }

        if (target instanceof net.minecraft.world.entity.TamableAnimal tamable) {
            if (tamable.isTame() && tamable.getOwner() != null) {
                if (tamable.getOwner().equals(player)) {
                    return false;
                }
                if (player.getTeam() != null && tamable.getOwner() instanceof ServerPlayer owner) {
                    if (player.getTeam().equals(owner.getTeam())) {
                        return false;
                    }
                }
            }
        }

        if (player.getTeam() != null && target.getTeam() != null) {
            if (player.getTeam().equals(target.getTeam())) {
                return false;
            }
        }

        return true;
    }

    private void spawnParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SOUL,
                    this.getX(), this.getY() + 0.5, this.getZ(),
                    3, 0.2, 0.3, 0.2, 0.02);

            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    this.getX(), this.getY() + 0.5, this.getZ(),
                    2, 0.2, 0.3, 0.2, 0.01);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.ticksExisted = tag.getInt("TicksExisted");
        this.returning = tag.getBoolean("Returning");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("TicksExisted", this.ticksExisted);
        tag.putBoolean("Returning", this.returning);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}

