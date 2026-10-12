package net.lordprinz.bloodharbourripper.ability;

import net.lordprinz.bloodharbourripper.mixin.LivingEntityInvoker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;

//? if >=1.21.11 {
import net.minecraft.world.level.gamerules.GameRules;
//?} else {
/*import net.minecraft.world.level.GameRules;
*///?}

/**
 * Executed targets drop their loot table and experience a second time.
 * Only the loot table is rolled again — equipment and player inventories are never duplicated.
 */
final class BonusLoot {
    private BonusLoot() {}

    static void dropAgain(ServerLevel level, LivingEntity target, ServerPlayer killer) {
        LivingEntityInvoker invoker = (LivingEntityInvoker) target;
        boolean byPlayer = killer != null;

        if (mobLootEnabled(level) && shouldDropLoot(invoker, level)) {
            //? if >=1.21.2 {
            invoker.bhr$dropFromLootTable(level, target.damageSources().fellOutOfWorld(), byPlayer);
            //?} else {
            /*invoker.bhr$dropFromLootTable(target.damageSources().fellOutOfWorld(), byPlayer);
            *///?}
        }

        if (!target.isBaby()) {
            //? if >=1.21 {
            int experience = target.getExperienceReward(level, killer);
            //?} else {
            /*int experience = invoker.bhr$getExperienceReward();
            *///?}
            if (experience > 0) {
                ExperienceOrb.award(level, target.position(), experience);
            }
        }
    }

    private static boolean shouldDropLoot(LivingEntityInvoker invoker, ServerLevel level) {
        //? if >=1.21.2 {
        return invoker.bhr$shouldDropLoot(level);
        //?} else {
        /*return invoker.bhr$shouldDropLoot();
        *///?}
    }

    private static boolean mobLootEnabled(ServerLevel level) {
        //? if >=1.21.11 {
        return level.getGameRules().get(GameRules.MOB_DROPS);
        //?} else {
        /*return level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
        *///?}
    }
}
