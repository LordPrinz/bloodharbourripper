package net.lordprinz.bloodharbourripper.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

//? if >=1.21.2 {
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
//?}
//? if >=1.21.6 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

/**
 * Base for short-lived visual/effect entities that are never saved, cannot be hit and only sync
 * a single "linked entity id" to the client. All version-specific Entity boilerplate lives here.
 */
public abstract class TransientEntity extends Entity {
    protected TransientEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    /** The synced int field (an entity id) this entity needs, or {@code null}. */
    protected abstract EntityDataAccessor<Integer> linkedIdAccessor();

    //? if >=1.20.5 {
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(linkedIdAccessor(), -1);
    }
    //?} else {
    /*@Override
    protected void defineSynchedData() {
        this.entityData.define(linkedIdAccessor(), -1);
    }
    *///?}

    protected void setLinkedEntity(Entity entity) {
        this.entityData.set(linkedIdAccessor(), entity.getId());
    }

    /** Works on both sides, as long as the linked entity is loaded. */
    public Entity getLinkedEntity() {
        return this.level().getEntity(this.entityData.get(linkedIdAccessor()));
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    //? if >=1.21.2 {
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }
    //?}

    //? if >=1.21.6 {
    @Override
    protected void readAdditionalSaveData(ValueInput input) {}

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {}
    //?} else {
    /*@Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
    *///?}
}
