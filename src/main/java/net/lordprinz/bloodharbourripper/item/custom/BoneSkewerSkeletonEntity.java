package net.lordprinz.bloodharbourripper.item.custom;
import net.lordprinz.bloodharbourripper.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
public class BoneSkewerSkeletonEntity extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(BoneSkewerSkeletonEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(BoneSkewerSkeletonEntity.class, EntityDataSerializers.BOOLEAN);
    private ItemStack skewerItem = new ItemStack(ModItems.BONE_SKEWER_SKELETON.get());
    private boolean dealtDamage;
    public int clientSideReturnTridentTickCount;
    public BoneSkewerSkeletonEntity(EntityType<? extends BoneSkewerSkeletonEntity> entityType, Level level) {
        super(entityType, level);
    }
    public BoneSkewerSkeletonEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(ModEntityTypes.BONE_SKEWER_SKELETON.get(), shooter, level);
        this.skewerItem = stack.copy();
        this.entityData.set(ID_LOYALTY, (byte) 3);
        this.entityData.set(ID_FOIL, stack.hasFoil());
        this.pickup = AbstractArrow.Pickup.CREATIVE_ONLY; // Nie pozwalaj na normalne podnoszenie
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
        this.entityData.define(ID_FOIL, false);
    }
    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * (double) i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }
                double d0 = 0.05 * (double) i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    // Dźwięk powrotu usunięty - tylko custom dźwięki
                }
                ++this.clientSideReturnTridentTickCount;
            }
        }
        super.tick();
    }
    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();

        if (entity instanceof LivingEntity livingTarget) {
            if (livingTarget.isBlocking()) {
                livingTarget.stopUsingItem();
                if (livingTarget instanceof Player playerTarget) {
                    playerTarget.getCooldowns().addCooldown(livingTarget.getUseItem().getItem(), 100);
                }
            }
        }

        float f = 7.0F;
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.skewerItem, livingentity.getMobType());
        }
        Entity owner = this.getOwner();
        DamageSource damagesource = owner != null ? this.damageSources().mobAttack((LivingEntity) owner) : this.damageSources().generic();
        this.dealtDamage = true;
        SoundEvent soundevent = net.lordprinz.bloodharbourripper.sound.ModSounds.BONE_SKEWER_HIT.get();
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingentity1) {
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) owner, livingentity1);
                }
                this.doPostHurtEffects(livingentity1);

                // Przyciąganie do gracza - silne przyciąganie
                if (owner != null && !this.level().isClientSide) {
                    Vec3 ownerPos = owner.position();
                    Vec3 entityPos = entity.position();
                    double distance = ownerPos.distanceTo(entityPos);
                    Vec3 direction = ownerPos.subtract(entityPos).normalize();
                    // Siła przyciągania skalowana odległością - mob leci prawie do gracza
                    double pullStrength = Math.min(distance * 0.3, 2.0); // Minimum 30% dystansu, max 2.0
                    entity.setDeltaMovement(direction.scale(pullStrength));
                    entity.hurtMarked = true;
                }
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(soundevent, 1.0F, 1.0F);
    }
    @Override
    protected ItemStack getPickupItem() {
        return this.skewerItem.copy();
    }
    @Override
    protected boolean tryPickup(Player player) {
        // Nie pozwalaj na normalne podnoszenie - harpun może tylko wrócić sam
        return false;
    }
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        // Custom dźwięk powrotu/uderzenia w ziemię
        return net.lordprinz.bloodharbourripper.sound.ModSounds.BONE_SKEWER_RETURN.get();
    }

    @Override
    protected void onHitBlock(net.minecraft.world.phys.BlockHitResult result) {
        super.onHitBlock(result);
        // Odtwórz dźwięk BONE_SKEWER_RETURN gdy harpun uderza w blok (hybienie)
        this.playSound(net.lordprinz.bloodharbourripper.sound.ModSounds.BONE_SKEWER_RETURN.get(), 1.0F, 1.0F);
    }

    @Override
    public void playerTouch(Player player) {
        // Jeśli harpun wraca (jest w trybie NoPhysics) i należy do gracza
        if (this.ownedBy(player) && this.isNoPhysics()) {
            if (!this.level().isClientSide) {

                // Usuń śledzenie gdy harpun wraca
                BoneSkewerTracker.removeSkewer(player);
                // Usuń cooldown - gracz może znowu użyć harpuna
                player.getCooldowns().removeCooldown(ModItems.BONE_SKEWER_SKELETON.get());
                // Usuń encję harpuna (nie dodawaj do ekwipunku - item już tam jest)
                this.discard();
            }
        }
        // Nie wywołuj super.playerTouch() - zapobiega to podnoszeniu
    }
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Skewer", 10)) {
            this.skewerItem = ItemStack.of(compound.getCompound("Skewer"));
        }
        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, (byte) 3);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Skewer", this.skewerItem.save(new CompoundTag()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }
    @Override
    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }
    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }
    public ItemStack getSkewerItem() {
        return this.skewerItem;
    }

    // Metoda do wymuszenia powrotu harpuna
    public void forceReturn() {
        this.dealtDamage = true;
        this.setNoPhysics(true);
        // Natychmiast zatrzymaj harpun, aby w następnym ticku zaczął wracać
        this.setDeltaMovement(Vec3.ZERO);
    }
}
