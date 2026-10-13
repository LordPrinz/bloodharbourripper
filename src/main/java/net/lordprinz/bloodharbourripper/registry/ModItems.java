package net.lordprinz.bloodharbourripper.registry;

import net.lordprinz.bloodharbourripper.item.BoneSkewerItem;
import net.lordprinz.bloodharbourripper.item.SkewerTier;
import net.lordprinz.bloodharbourripper.item.TooltipItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

//? if >=1.21.2 {
import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
//?}

/**
 * Item ids are part of the save format: never rename them, only add new ones.
 */
public final class ModItems {
    public static final Supplier<BoneSkewerItem> BONE_SKEWER_SKELETON = register("bone_skewer_skeleton",
            properties -> new BoneSkewerItem(SkewerTier.SKELETON, properties));
    public static final Supplier<BoneSkewerItem> BONE_SKEWER_RAW = register("bone_skewer_raw",
            properties -> new BoneSkewerItem(SkewerTier.RAW, properties));
    public static final Supplier<BoneSkewerItem> BONE_SKEWER = register("bone_skewer",
            properties -> new BoneSkewerItem(SkewerTier.ADVANCED, properties));
    public static final Supplier<TooltipItem> JAULL_FISH_VIAL = register("jaull_fish_vial", TooltipItem::new);

    // Crafting components
    public static final Supplier<TooltipItem> HARPOON_HEAD = register("harpoon_head", TooltipItem::new);
    public static final Supplier<TooltipItem> BOUND_HAFT = register("bound_haft", TooltipItem::new);
    public static final Supplier<TooltipItem> ABYSSAL_CORE = register("abyssal_core", TooltipItem::new);
    public static final Supplier<TooltipItem> DROWNED_HEART = register("drowned_heart", TooltipItem::new);

    private ModItems() {}

    private static <T extends Item> Supplier<T> register(String name, Function<Item.Properties, T> factory) {
        return RegistryHelper.register(BuiltInRegistries.ITEM, name, () -> factory.apply(properties(name)));
    }

    private static Item.Properties properties(String name) {
        //? if >=1.21.2 {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BloodHarbourRipper.id(name)));
        //?} else {
        /*return new Item.Properties();
        *///?}
    }

    public static void init() {}
}
