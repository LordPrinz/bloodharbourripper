package net.lordprinz.bloodharbourripper.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

//? if >=1.21.2 {
import net.minecraft.server.level.ServerLevel;
//?}

/** Exposes the protected loot methods used by the execution's bonus drops. */
@Mixin(LivingEntity.class)
public interface LivingEntityInvoker {
    //? if >=1.21.2 {
    @Invoker("dropFromLootTable")
    void bhr$dropFromLootTable(ServerLevel level, DamageSource source, boolean playerKilled);

    @Invoker("shouldDropLoot")
    boolean bhr$shouldDropLoot(ServerLevel level);
    //?} else {
    /*@Invoker("dropFromLootTable")
    void bhr$dropFromLootTable(DamageSource source, boolean playerKilled);

    @Invoker("shouldDropLoot")
    boolean bhr$shouldDropLoot();
    *///?}

    //? if <1.21 {
    /*@Invoker("getExperienceReward")
    int bhr$getExperienceReward();
    *///?}
}
