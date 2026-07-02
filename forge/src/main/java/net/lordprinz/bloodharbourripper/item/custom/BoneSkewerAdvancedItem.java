package net.lordprinz.bloodharbourripper.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BoneSkewerAdvancedItem extends SwordItem {
    private static final double EXECUTE_RANGE = 12.0;

    public BoneSkewerAdvancedItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (isSelected && entity instanceof Player player && player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                AABB searchBox = player.getBoundingBox().inflate(EXECUTE_RANGE);
                List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, searchBox,
                    e -> e != player && e.isAlive());

                for (LivingEntity target : nearbyEntities) {
                    double distance = player.distanceTo(target);

                    if (distance <= EXECUTE_RANGE) {
                        float healthPercentage = target.getHealth() / target.getMaxHealth();

                        if (healthPercentage < 0.33f) {
                            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 2, 0, false, false));
                        }
                    }
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (BoneSkewerTracker.hasActiveSkewer(player)) {
            if (!level.isClientSide) {
                AbstractArrow activeSkewer = BoneSkewerTracker.getActiveSkewer(player);
                if (activeSkewer instanceof BoneSkewerAdvancedEntity skewer) {
                    skewer.forceReturn();
                    player.getCooldowns().addCooldown(this, 100000);
                    level.playSound(null, player.blockPosition(),
                        net.lordprinz.bloodharbourripper.sound.ModSounds.BONE_SKEWER_RETURN.get(),
                        SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
            return InteractionResultHolder.success(itemstack);
        }

        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {

            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                if (BoneSkewerTracker.hasActiveSkewer(player)) {
                    return;
                }

                if (!level.isClientSide) {
                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(entity.getUsedItemHand()));
                    BoneSkewerAdvancedEntity skewer = new BoneSkewerAdvancedEntity(level, player, stack);
                    skewer.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);

                    level.addFreshEntity(skewer);
                    level.playSound(null, skewer, net.lordprinz.bloodharbourripper.sound.ModSounds.BONE_SKEWER_RELEASE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                    BoneSkewerTracker.trackSkewer(player, skewer);
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
}

