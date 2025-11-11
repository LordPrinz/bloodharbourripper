package net.lordprinz.bloodharbourripper.item.custom;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
public class BoneSkewerSkeletonItem extends SwordItem {
    public BoneSkewerSkeletonItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // Sprawdź czy gracz ma aktywny harpun w locie
        if (BoneSkewerTracker.hasActiveSkewer(player)) {
            // Odwołaj harpun (tylko po stronie serwera)
            if (!level.isClientSide) {
                AbstractArrow activeSkewer = BoneSkewerTracker.getActiveSkewer(player);
                if (activeSkewer instanceof BoneSkewerSkeletonEntity skewer) {
                    skewer.forceReturn();
                    // Ustaw cooldown na bardzo długi czas - zostanie usunięty gdy harpun wróci
                    player.getCooldowns().addCooldown(this, 100000);
                    // Odtwórz dźwięk powrotu/przywołania
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
                // Nie pozwalaj rzucić jeśli jest aktywny harpun
                if (BoneSkewerTracker.hasActiveSkewer(player)) {
                    return;
                }

                if (!level.isClientSide) {
                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(entity.getUsedItemHand()));
                    BoneSkewerSkeletonEntity skewer = new BoneSkewerSkeletonEntity(level, player, stack);
                    // Podstawowy rzut - jak fishing rod, mniejsza prędkość
                    skewer.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.0F, 1.5F);
                    level.addFreshEntity(skewer);
                    level.playSound(null, skewer, net.lordprinz.bloodharbourripper.sound.ModSounds.BONE_SKEWER_RELEASE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                    // Śledź rzucony harpun
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
