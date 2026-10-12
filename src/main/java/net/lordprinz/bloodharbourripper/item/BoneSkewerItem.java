package net.lordprinz.bloodharbourripper.item;

import net.lordprinz.bloodharbourripper.compat.Compat;
import net.lordprinz.bloodharbourripper.entity.BoneSkewerEntity;
import net.lordprinz.bloodharbourripper.registry.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

//? if >=1.21.2 {
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemUseAnimation;
//?} else {
/*import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.UseAnim;
*///?}
//? if >=1.21.5 {
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
//?} else {
/*import net.minecraft.world.item.SwordItem;
import java.util.List;
*///?}

/**
 * All three Bone Skewers. A sword in melee; hold right click to charge and throw it like a loyal
 * trident. While it is in flight, right click again to recall it.
 */
public class BoneSkewerItem extends
        //? if >=1.21.5 {
        Item
        //?} else {
        /*SwordItem
        *///?}
{
    private static final int MIN_CHARGE_TICKS = 10;
    private static final int RECALL_COOLDOWN_TICKS = 60;
    private static final int USE_DURATION = 72000;

    private final SkewerTier skewerTier;

    public BoneSkewerItem(SkewerTier tier, Item.Properties properties) {
        //? if >=1.21.5 {
        super(properties.sword(tier.material(), tier.itemDamage, SkewerTier.ATTACK_SPEED));
        //?} elif >=1.20.5 {
        /*super(tier.tier(), properties.attributes(SwordItem.createAttributes(tier.tier(), tier.itemDamage, SkewerTier.ATTACK_SPEED)));
        *///?} else {
        /*super(tier.tier(), tier.itemDamage, SkewerTier.ATTACK_SPEED, properties);
        *///?}
        this.skewerTier = tier;
    }

    public SkewerTier skewerTier() {
        return skewerTier;
    }

    // --- Using ---------------------------------------------------------------------------------

    private enum Outcome { SUCCESS, CONSUME, FAIL }

    private Outcome startUse(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            BoneSkewerEntity active = SkewerTracker.get(player);
            if (active != null) {
                active.recall();
                Compat.addCooldown(player, stack, RECALL_COOLDOWN_TICKS);
                level.playSound(null, player.blockPosition(), ModSounds.BONE_SKEWER_RETURN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                return Outcome.SUCCESS;
            }
        }
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return Outcome.FAIL;
        }
        player.startUsingItem(hand);
        return Outcome.CONSUME;
    }

    private boolean throwSkewer(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return false;
        if (USE_DURATION - timeLeft < MIN_CHARGE_TICKS) return false;

        if (!level.isClientSide()) {
            if (SkewerTracker.get(player) != null) return false;

            //? if >=1.21.11 {
            stack.hurtAndBreak(1, player, player.getUsedItemHand());
            //?} elif >=1.20.5 {
            /*stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
            *///?} else {
            /*stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
            *///?}
            BoneSkewerEntity skewer = new BoneSkewerEntity(level, player, stack, skewerTier);
            skewer.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, skewerTier.velocity, skewerTier.inaccuracy);
            level.addFreshEntity(skewer);
            level.playSound(null, skewer, ModSounds.BONE_SKEWER_RELEASE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            SkewerTracker.track(player, skewer);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return true;
    }

    //? if >=1.21.2 {
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return switch (startUse(level, player, hand)) {
            case SUCCESS -> InteractionResult.SUCCESS;
            case CONSUME -> InteractionResult.CONSUME;
            case FAIL -> InteractionResult.FAIL;
        };
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        return throwSkewer(stack, level, entity, timeLeft);
    }
    //?} else {
    /*@Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return switch (startUse(level, player, hand)) {
            case SUCCESS -> InteractionResultHolder.success(stack);
            case CONSUME -> InteractionResultHolder.consume(stack);
            case FAIL -> InteractionResultHolder.fail(stack);
        };
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        throwSkewer(stack, level, entity, timeLeft);
    }
    *///?}

    //? if >=1.21.11 {
    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.TRIDENT;
    }
    //?} elif >=1.21.2 {
    /*@Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.SPEAR;
    }
    *///?} else {
    /*@Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }
    *///?}

    //? if >=1.20.5 {
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION;
    }
    //?} else {
    /*@Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }
    *///?}

    // --- Tooltip -------------------------------------------------------------------------------

    //? if >=1.21.5 {
    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        ModTooltips.append(this, tooltip);
    }
    //?} elif >=1.20.5 {
    /*@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        ModTooltips.append(this, tooltip::add);
    }
    *///?} else {
    /*@Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        ModTooltips.append(this, tooltip::add);
    }
    *///?}
}
