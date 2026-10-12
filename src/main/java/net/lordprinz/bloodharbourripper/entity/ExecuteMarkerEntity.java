package net.lordprinz.bloodharbourripper.entity;

import net.lordprinz.bloodharbourripper.ability.ExecuteAbility;
import net.lordprinz.bloodharbourripper.registry.ModEntityTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

/**
 * "Death From Below": a glowing mark under the doomed target. It follows the target and performs
 * the execution when its countdown ends — so the countdown also works without any server tick hook.
 */
public class ExecuteMarkerEntity extends TransientEntity {
    public static final int DELAY_TICKS = 13;
    private static final EntityDataAccessor<Integer> TARGET_ID =
            SynchedEntityData.defineId(ExecuteMarkerEntity.class, EntityDataSerializers.INT);

    private UUID executor;

    public ExecuteMarkerEntity(EntityType<? extends ExecuteMarkerEntity> type, Level level) {
        super(type, level);
    }

    public static ExecuteMarkerEntity create(ServerLevel level, ServerPlayer executor, LivingEntity target) {
        ExecuteMarkerEntity marker = new ExecuteMarkerEntity(ModEntityTypes.EXECUTE_MARKER.get(), level);
        marker.executor = executor.getUUID();
        marker.setLinkedEntity(target);
        marker.setPos(target.getX(), target.getY(), target.getZ());
        return marker;
    }

    @Override
    protected EntityDataAccessor<Integer> linkedIdAccessor() {
        return TARGET_ID;
    }

    @Override
    public void tick() {
        super.tick();
        Entity target = getLinkedEntity();
        if (target != null) {
            this.setPos(target.getX(), target.getY(), target.getZ());
        }
        if (!(this.level() instanceof ServerLevel level)) return;

        if (!(target instanceof LivingEntity living) || !living.isAlive()) {
            this.discard();
            return;
        }
        if (this.tickCount >= DELAY_TICKS) {
            ExecuteAbility.performExecution(level, executor, living);
            this.discard();
        }
    }
}
