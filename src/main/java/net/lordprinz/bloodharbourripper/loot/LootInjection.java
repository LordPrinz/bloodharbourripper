package net.lordprinz.bloodharbourripper.loot;

import net.lordprinz.bloodharbourripper.registry.ModItems;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

//? if >=1.20.5 {
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
//?} else {
/*import net.minecraft.resources.Identifier;
*///?}

/**
 * Adds an extra pool to vanilla loot tables. Each loader calls {@link #isTarget} from its own
 * "loot table loaded" event and appends {@link #jaullFishVialPool()} when it matches.
 */
public final class LootInjection {
    private static final float JAULL_FISH_VIAL_CHANCE = 0.33F;

    private LootInjection() {}

    //? if >=1.20.5 {
    public static boolean isTarget(ResourceKey<LootTable> table) {
        return BuiltInLootTables.ANCIENT_CITY.equals(table);
    }
    //?} else {
    /*public static boolean isTarget(Identifier table) {
        return BuiltInLootTables.ANCIENT_CITY.equals(table);
    }
    *///?}

    /** One Jaull Fish Vial with a 33% chance per Ancient City chest. */
    public static LootPool.Builder jaullFishVialPool() {
        return LootPool.lootPool()
                .when(LootItemRandomChanceCondition.randomChance(JAULL_FISH_VIAL_CHANCE))
                .add(LootItem.lootTableItem(ModItems.JAULL_FISH_VIAL.get()));
    }
}
