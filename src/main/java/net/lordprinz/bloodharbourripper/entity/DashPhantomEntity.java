package net.lordprinz.bloodharbourripper.entity;

import net.lordprinz.bloodharbourripper.ability.Targeting;
import net.lordprinz.bloodharbourripper.compat.Compat;
import net.lordprinz.bloodharbourripper.registry.ModEntityTypes;
import net.lordprinz.bloodharbourripper.registry.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * The "Phantom Undertow" afterimage: waits behind the dashing player, then rushes back to them and
 * damages, blinds and freezes every enemy on its path.
 */
public class DashPhantomEntity extends TransientEntity {
    private static final EntityDataAccessor<Integer> OWNER_ID =
            SynchedEntityData.defineId(DashPhantomEntity.class, EntityDataSerializers.INT);
    private static final int WAIT_TICKS = 12;
    private static final int MAX_LIFETIME_TICKS = 200;
    private static final float DAMAGE = 8.0F;
    private static final double RETURN_SPEED = 2.0;
    private static final double HITBOX_RADIUS = 1.5;

    private final Set<UUID> hitEntities = new HashSet<>();

    public DashPhantomEntity(EntityType<? extends DashPhantomEntity> type, Level level) {
        super(type, level);
    }

    public static DashPhantomEntity create(ServerLevel level, ServerPlayer owner, Vec3 position) {
        DashPhantomEntity phantom = new DashPhantomEntity(ModEntityTypes.DASH_PHANTOM.get(), level);
        phantom.setLinkedEntity(owner);
        phantom.setPos(position.x, position.y, position.z);
        return phantom;
    }

    @Override
    protected EntityDataAccessor<Integer> linkedIdAccessor() {
        return OWNER_ID;
    }

    /** The dashing player (client and server). */
    public Entity getOwner() {
        return getLinkedEntity();
    }

    @Override
    public void tick() {
        super.tick();
        if (!(this.level() instanceof ServerLevel level)) return;

        if (!(getOwner() instanceof ServerPlayer owner) || !owner.isAlive() || this.tickCount > MAX_LIFETIME_TICKS) {
            this.discard();
            return;
        }

        if (this.tickCount >= WAIT_TICKS) {
            Vec3 ownerPos = owner.position().add(0, owner.getBbHeight() * 0.5, 0);
            Vec3 from = this.position();
            if (from.distanceTo(ownerPos) < 1.0) {
                this.discard();
                return;
            }
            Vec3 to = from.add(ownerPos.subtract(from).normalize().scale(RETURN_SPEED));
            this.setPos(to.x, to.y, to.z);
            damageEntitiesOnPath(level, owner, from, to);
        }

        level.sendParticles(ParticleTypes.SOUL, getX(), getY() + 0.5, getZ(), 3, 0.2, 0.3, 0.2, 0.02);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY() + 0.5, getZ(), 2, 0.2, 0.3, 0.2, 0.01);
    }

    private void damageEntitiesOnPath(ServerLevel level, ServerPlayer owner, Vec3 from, Vec3 to) {
        Vec3 direction = to.subtract(from).normalize();
        double pathLength = from.distanceTo(to);
        AABB area = new AABB(from, to).inflate(HITBOX_RADIUS);

        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area, e -> e != owner && e.isAlive())) {
            if (hitEntities.contains(target.getUUID()) || Targeting.isAlly(owner, target)) continue;

            double along = target.position().subtract(from).dot(direction);
            if (along < 0 || along > pathLength) continue;
            if (target.position().distanceTo(from.add(direction.scale(along))) > HITBOX_RADIUS) continue;

            hitEntities.add(target.getUUID());
            Compat.hurt(target, level.damageSources().playerAttack(owner), DAMAGE);
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
            target.addEffect(Compat.slowness(60, 255));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 255));

            level.playSound(null, target.blockPosition(), ModSounds.DASH_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            level.sendParticles(ParticleTypes.SWEEP_ATTACK, target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(),
                    5, 0.3, 0.3, 0.3, 0.0);
        }
    }
}
