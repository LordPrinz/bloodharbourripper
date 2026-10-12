package net.lordprinz.bloodharbourripper.item;

import net.lordprinz.bloodharbourripper.compat.Compat;
import net.lordprinz.bloodharbourripper.entity.BoneSkewerEntity;
import net.lordprinz.bloodharbourripper.registry.ModEntityTypes;
import net.lordprinz.bloodharbourripper.registry.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

//? if >=1.21.5 {
import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;
//?} elif >=1.20.5 {
/*import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
*///?} else {
/*import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
*///?}

/**
 * Every stat of the three Bone Skewers in one place. Adding a fourth skewer means adding an
 * enum constant plus its item/entity registrations — no new classes.
 */
public enum SkewerTier {
    //        mining level, durability, speed, tier dmg, enchant, item dmg, thrown dmg, velocity, inaccuracy, pull factor, pull max
    SKELETON(2, 250, 6.0F, 3.0F, 14, 3, 7.0F, 1.0F, 1.5F, 0.3, 2.0,
            () -> ModItems.BONE_SKEWER_SKELETON.get(), () -> ModEntityTypes.BONE_SKEWER_SKELETON.get()),
    RAW(3, 1561, 8.0F, 4.0F, 10, 4, 8.0F, 2.5F, 1.0F, 0.4, 2.5,
            () -> ModItems.BONE_SKEWER_RAW.get(), () -> ModEntityTypes.BONE_SKEWER_RAW.get()),
    ADVANCED(4, 2031, 9.0F, 6.0F, 15, 8, 15.0F, 3.0F, 1.0F, 0.5, 3.0,
            () -> ModItems.BONE_SKEWER.get(), () -> ModEntityTypes.BONE_SKEWER.get());

    public static final float ATTACK_SPEED = -2.4F;

    final int miningLevel;
    final int durability;
    final float miningSpeed;
    final float tierDamage;
    final int enchantability;
    final int itemDamage;
    public final float thrownDamage;
    public final float velocity;
    public final float inaccuracy;
    public final double pullFactor;
    public final double pullMax;
    private final Supplier<Item> item;
    private final Supplier<EntityType<BoneSkewerEntity>> entityType;

    SkewerTier(int miningLevel, int durability, float miningSpeed, float tierDamage, int enchantability, int itemDamage,
               float thrownDamage, float velocity, float inaccuracy, double pullFactor, double pullMax,
               Supplier<Item> item, Supplier<EntityType<BoneSkewerEntity>> entityType) {
        this.miningLevel = miningLevel;
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.tierDamage = tierDamage;
        this.enchantability = enchantability;
        this.itemDamage = itemDamage;
        this.thrownDamage = thrownDamage;
        this.velocity = velocity;
        this.inaccuracy = inaccuracy;
        this.pullFactor = pullFactor;
        this.pullMax = pullMax;
        this.item = item;
        this.entityType = entityType;
    }

    public Item item() {
        return item.get();
    }

    public EntityType<BoneSkewerEntity> entityType() {
        return entityType.get();
    }

    public static SkewerTier byEntityType(EntityType<?> type) {
        for (SkewerTier tier : values()) {
            if (tier.entityType() == type) return tier;
        }
        return SKELETON;
    }

    /** Status effects applied when a thrown skewer hits a living target. */
    public void applyHitEffects(LivingEntity target) {
        switch (this) {
            case RAW -> target.addEffect(Compat.slowness(40, 1));
            case ADVANCED -> {
                target.addEffect(Compat.slowness(80, 1));
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 160, 0));
            }
            default -> {}
        }
    }

    //? if >=1.21.5 {
    private static final TagKey<net.minecraft.world.item.Item> REPAIR_ITEMS =
            TagKey.create(Registries.ITEM, BloodHarbourRipper.id("bone_skewer_repair_materials"));

    public ToolMaterial material() {
        return new ToolMaterial(incorrectBlocks(), durability, miningSpeed, tierDamage, enchantability, REPAIR_ITEMS);
    }
    //?} else {
    /*public Tier tier() {
        SkewerTier self = this;
        return new Tier() {
            @Override public int getUses() { return self.durability; }
            @Override public float getSpeed() { return self.miningSpeed; }
            @Override public float getAttackDamageBonus() { return self.tierDamage; }
            @Override public int getEnchantmentValue() { return self.enchantability; }
            @Override public Ingredient getRepairIngredient() { return Ingredient.EMPTY; }
            //? if >=1.20.5 {
            /^@Override public TagKey<Block> getIncorrectBlocksForDrops() { return self.incorrectBlocks(); }
            ^///?} else {
            @Override public int getLevel() { return self.miningLevel; }
            //?}
        };
    }
    *///?}

    //? if >=1.20.5 {
    private TagKey<Block> incorrectBlocks() {
        return switch (miningLevel) {
            case 2 -> BlockTags.INCORRECT_FOR_IRON_TOOL;
            case 3 -> BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
            default -> BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
        };
    }
    //?}
}
