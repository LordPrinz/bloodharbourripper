package net.lordprinz.bloodharbourripper.compat;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

//? if >=1.21.2 {
import net.minecraft.server.level.ServerLevel;
//?}

/**
 * Small, version-sensitive vanilla calls that are used in more than one place.
 * Keeping them here means a Minecraft update usually only touches this file.
 */
public final class Compat {
    private Compat() {}

    /** Server-side damage. Returns {@code false} on the client. */
    public static boolean hurt(Entity target, DamageSource source, float amount) {
        //? if >=1.21.2 {
        return target.level() instanceof ServerLevel level && target.hurtServer(level, source, amount);
        //?} else {
        /*return target.hurt(source, amount);
        *///?}
    }

    public static void addCooldown(Player player, ItemStack stack, int ticks) {
        //? if >=1.21.2 {
        player.getCooldowns().addCooldown(stack, ticks);
        //?} else {
        /*player.getCooldowns().addCooldown(stack.getItem(), ticks);
        *///?}
    }

    public static void removeCooldown(Player player, ItemStack stack) {
        //? if >=1.21.2 {
        player.getCooldowns().removeCooldown(player.getCooldowns().getCooldownGroup(stack));
        //?} else {
        /*player.getCooldowns().removeCooldown(stack.getItem());
        *///?}
    }

    public static MobEffectInstance slowness(int duration, int amplifier) {
        //? if >=1.21.5 {
        return new MobEffectInstance(MobEffects.SLOWNESS, duration, amplifier);
        //?} else {
        /*return new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, amplifier);
        *///?}
    }

    /** Forces the server to send the entity's new velocity to clients (needed after setDeltaMovement). */
    public static void syncVelocity(Entity entity) {
        //? if >=26.3 {
        entity.syncVelocity = true;
        //?} else {
        /*entity.hurtMarked = true;
        *///?}
    }

    /** Shows a message above the hotbar. */
    public static void actionBar(Player player, Component message) {
        //? if >=26.1 {
        player.sendOverlayMessage(message);
        //?} else {
        /*player.displayClientMessage(message, true);
        *///?}
    }
}
