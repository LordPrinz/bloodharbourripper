package net.lordprinz.bloodharbourripper.client;

import net.lordprinz.bloodharbourripper.ability.AbilityCooldowns;
import net.lordprinz.bloodharbourripper.ability.DashAbility;
import net.lordprinz.bloodharbourripper.ability.ExecuteAbility;
import net.lordprinz.bloodharbourripper.ability.Targeting;
import net.lordprinz.bloodharbourripper.compat.Compat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.Locale;

/**
 * Loader-independent client logic. Loader entrypoints only forward the end-of-tick event here.
 * The client-side cooldowns are predictions for UI messages; the server has the final word.
 */
public final class ModClient {
    private static final AbilityCooldowns EXECUTE_COOLDOWN = new AbilityCooldowns(ExecuteAbility.COOLDOWN_TICKS);
    private static final AbilityCooldowns DASH_COOLDOWN = new AbilityCooldowns(DashAbility.COOLDOWN_TICKS);

    private static ChargingSound chargingSound;

    private ModClient() {}

    public static void onEndClientTick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            chargingSound = null;
            return;
        }

        while (ModKeyMappings.EXECUTE.consumeClick()) {
            tryExecute(minecraft, player);
        }
        while (ModKeyMappings.DASH.consumeClick()) {
            tryDash(minecraft, player);
        }

        if (ChargingSound.isCharging(player) && (chargingSound == null || chargingSound.isStopped())) {
            chargingSound = new ChargingSound(player);
            minecraft.getSoundManager().play(chargingSound);
        }
    }

    private static void tryExecute(Minecraft minecraft, LocalPlayer player) {
        if (!ExecuteAbility.canUse(player)) return;
        long now = minecraft.level.getGameTime();
        long remaining = EXECUTE_COOLDOWN.remaining(player.getUUID(), now);
        if (remaining > 0) {
            Compat.actionBar(player, cooldownMessage("message.bloodharbourripper.execute.cooldown", remaining));
            return;
        }

        LivingEntity target = Targeting.findExecuteTarget(player);
        if (target == null) {
            Compat.actionBar(player, Component.translatable("message.bloodharbourripper.execute.no_target").withStyle(ChatFormatting.RED));
            return;
        }
        ClientNetworking.sendExecute(target.getId());
        EXECUTE_COOLDOWN.trigger(player.getUUID(), now);
        Compat.actionBar(player, Component.translatable("message.bloodharbourripper.execute.success").withStyle(ChatFormatting.GOLD));
    }

    private static void tryDash(Minecraft minecraft, LocalPlayer player) {
        if (!DashAbility.canUse(player)) return;
        long now = minecraft.level.getGameTime();
        long remaining = DASH_COOLDOWN.remaining(player.getUUID(), now);
        if (remaining > 0) {
            Compat.actionBar(player, cooldownMessage("message.bloodharbourripper.dash.cooldown", remaining));
            return;
        }
        ClientNetworking.sendDash();
        DASH_COOLDOWN.trigger(player.getUUID(), now);
        Compat.actionBar(player, Component.translatable("message.bloodharbourripper.dash.success").withStyle(ChatFormatting.AQUA));
    }

    private static Component cooldownMessage(String key, long remainingTicks) {
        String seconds = String.format(Locale.ROOT, "%.1f", remainingTicks / 20.0);
        return Component.translatable(key, seconds).withStyle(ChatFormatting.RED);
    }
}
