package net.lordprinz.bloodharbourripper.ability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

/** Who the abilities may hit. Shared by client prediction and server validation. */
public final class Targeting {
    private Targeting() {}

    /** Teams, own pets and pets of team mates (vanilla's alliance rules, checked both ways). */
    public static boolean isAlly(Player player, Entity target) {
        return player.isAlliedTo(target) || target.isAlliedTo(player);
    }

    public static boolean isValidExecuteTarget(Player player, LivingEntity target) {
        if (target == player || !target.isAlive() || !target.attackable()) return false;
        if (target instanceof Player other && (other.isCreative() || other.isSpectator())) return false;
        if (player.distanceTo(target) > ExecuteAbility.RANGE) return false;
        if (target.getHealth() / target.getMaxHealth() >= ExecuteAbility.HEALTH_THRESHOLD) return false;
        return !isAlly(player, target);
    }

    /** The closest valid execution target around the player, or {@code null}. */
    public static LivingEntity findExecuteTarget(Player player) {
        AABB area = player.getBoundingBox().inflate(ExecuteAbility.RANGE);
        LivingEntity best = null;
        double bestDistance = Double.MAX_VALUE;
        for (LivingEntity target : player.level().getEntitiesOfClass(LivingEntity.class, area, e -> isValidExecuteTarget(player, e))) {
            double distance = player.distanceTo(target);
            if (distance < bestDistance) {
                best = target;
                bestDistance = distance;
            }
        }
        return best;
    }
}
