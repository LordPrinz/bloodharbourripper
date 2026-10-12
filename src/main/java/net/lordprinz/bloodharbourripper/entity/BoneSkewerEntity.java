package net.lordprinz.bloodharbourripper.entity;

import net.lordprinz.bloodharbourripper.compat.Compat;
import net.lordprinz.bloodharbourripper.item.SkewerTier;
import net.lordprinz.bloodharbourripper.item.SkewerTracker;
import net.lordprinz.bloodharbourripper.registry.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

//? if >=1.21.11 {
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import java.util.Collection;
import java.util.List;
//?} else {
/*import net.minecraft.world.entity.projectile.AbstractArrow;
*///?}
//? if >=1.21 {
import net.minecraft.server.level.ServerLevel;
//?}
//? if >=1.21.6 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

/**
 * A thrown Bone Skewer. Behaves like a trident with Loyalty III: after hitting something (or when
 * recalled) it flies back to its owner and disappears on contact. The item never leaves the hand.
 */
public class BoneSkewerEntity extends AbstractArrow {
    private static final int LOYALTY = 3;
    private static final String TAG_DEALT_DAMAGE = "DealtDamage";

    private boolean dealtDamage;
    private ItemStack displayStack;
    //? if <1.20.5 {
    /*private static final String TAG_SKEWER = "Skewer";
    private ItemStack skewerItem = ItemStack.EMPTY;
    *///?}

    public BoneSkewerEntity(EntityType<? extends BoneSkewerEntity> type, Level level) {
        super(type, level);
    }

    public BoneSkewerEntity(Level level, LivingEntity owner, ItemStack stack, SkewerTier tier) {
        //? if >=1.20.5 {
        super(tier.entityType(), owner, level, stack.copyWithCount(1), null);
        //?} else {
        /*super(tier.entityType(), owner, level);
        this.skewerItem = stack.copyWithCount(1);
        *///?}
        this.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
    }

    public SkewerTier tier() {
        return SkewerTier.byEntityType(getType());
    }

    /** The stack this skewer was thrown from (enchantments apply to the throw). */
    public ItemStack skewerStack() {
        //? if >=1.20.5 {
        return getPickupItemStackOrigin();
        //?} else {
        /*return skewerItem.isEmpty() ? new ItemStack(tier().item()) : skewerItem;
        *///?}
    }

    /** The stack the renderer draws (always the plain item of this tier). */
    public ItemStack displayStack() {
        if (displayStack == null) displayStack = new ItemStack(tier().item());
        return displayStack;
    }

    /** Makes the skewer fly back to its owner immediately. */
    public void recall() {
        this.dealtDamage = true;
        this.setNoPhysics(true);
        this.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity owner = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && owner != null) {
            if (!isAcceptableReturnOwner(owner)) {
                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 toOwner = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + toOwner.y * 0.015 * LOYALTY, this.getZ());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(toOwner.normalize().scale(0.05 * LOYALTY)));
            }
        }

        super.tick();
    }

    private static boolean isAcceptableReturnOwner(Entity owner) {
        return owner.isAlive() && !(owner instanceof ServerPlayer && owner.isSpectator());
    }

    // A returning skewer must not hit anything on its way back.
    @Override
    protected EntityHitResult findHitEntity(Vec3 from, Vec3 to) {
        return this.dealtDamage ? null : super.findHitEntity(from, to);
    }

    //? if >=1.21.11 {
    @Override
    protected Collection<EntityHitResult> findHitEntities(Vec3 from, Vec3 to) {
        EntityHitResult hit = this.findHitEntity(from, to);
        return hit != null ? List.of(hit) : List.of();
    }
    //?}

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.dealtDamage) return;
        Entity target = result.getEntity();
        SkewerTier tier = tier();
        Entity owner = this.getOwner();

        // Piercing a raised shield disables it, like an axe.
        if (target instanceof LivingEntity living && living.isBlocking()) {
            ItemStack blockedWith = living.getUseItem();
            living.stopUsingItem();
            if (living instanceof Player player && !blockedWith.isEmpty()) {
                Compat.addCooldown(player, blockedWith, 100);
            }
        }

        DamageSource source = this.damageSources().trident(this, owner == null ? this : owner);
        float damage = tier.thrownDamage;
        //? if >=1.21 {
        if (this.level() instanceof ServerLevel serverLevel) {
            damage = EnchantmentHelper.modifyDamage(serverLevel, skewerStack(), target, source, damage);
        }
        //?} else {
        /*if (target instanceof LivingEntity living) {
            damage += EnchantmentHelper.getDamageBonus(skewerStack(), living.getMobType());
        }
        *///?}

        this.dealtDamage = true;
        if (Compat.hurt(target, source, damage) && target instanceof LivingEntity living) {
            //? if >=1.21 {
            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffects(serverLevel, living, source);
            }
            //?} else {
            /*if (owner instanceof LivingEntity livingOwner) {
                EnchantmentHelper.doPostHurtEffects(living, livingOwner);
                EnchantmentHelper.doPostDamageEffects(livingOwner, living);
            }
            *///?}
            this.doPostHurtEffects(living);
            tier.applyHitEffects(living);

            // Harpoon: drag the target towards the thrower.
            if (owner != null) {
                Vec3 toOwner = owner.position().subtract(living.position());
                double strength = Math.min(toOwner.length() * tier.pullFactor, tier.pullMax);
                living.setDeltaMovement(toOwner.normalize().scale(strength));
                Compat.syncVelocity(living);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(ModSounds.BONE_SKEWER_HIT.get(), 1.0F, 1.0F);
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) && this.isNoPhysics() && !this.level().isClientSide()) {
            SkewerTracker.untrack(player, this);
            Compat.removeCooldown(player, new ItemStack(tier().item()));
            this.discard();
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.BONE_SKEWER_RETURN.get();
    }

    //? if >=1.20.5 {
    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(tier().item());
    }
    //?} else {
    /*@Override
    protected ItemStack getPickupItem() {
        return skewerStack().copy();
    }
    *///?}

    //? if >=1.21.6 {
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.dealtDamage = input.getBooleanOr(TAG_DEALT_DAMAGE, false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean(TAG_DEALT_DAMAGE, this.dealtDamage);
    }
    //?} elif >=1.20.5 {
    /*@Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.dealtDamage = tag.getBoolean(TAG_DEALT_DAMAGE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean(TAG_DEALT_DAMAGE, this.dealtDamage);
    }
    *///?} else {
    /*@Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(TAG_SKEWER, 10)) {
            this.skewerItem = ItemStack.of(tag.getCompound(TAG_SKEWER));
        }
        this.dealtDamage = tag.getBoolean(TAG_DEALT_DAMAGE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put(TAG_SKEWER, skewerStack().save(new CompoundTag()));
        tag.putBoolean(TAG_DEALT_DAMAGE, this.dealtDamage);
    }
    *///?}
}
