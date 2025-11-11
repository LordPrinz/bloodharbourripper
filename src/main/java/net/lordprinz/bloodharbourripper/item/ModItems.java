package net.lordprinz.bloodharbourripper.item;

import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.lordprinz.bloodharbourripper.item.custom.BoneSkewerSkeletonItem;
import net.lordprinz.bloodharbourripper.item.custom.BoneSkewerRawItem;
import net.lordprinz.bloodharbourripper.item.custom.BoneSkewerAdvancedItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BloodHarbourRipper.MOD_ID);

    // Iron Sword damage = 6, więc +1 = 7 total damage (attack damage modifier = 3 bo base damage to 4)
    public static final RegistryObject<Item> BONE_SKEWER_SKELETON = ITEMS.register("bone_skewer_skeleton",
            () -> new BoneSkewerSkeletonItem(ModToolTiers.BONE_SKEWER_SKELETON, 3, -2.4f, new Item.Properties()));

    // Diamond Sword damage = 7, więc +1 = 8 total damage (attack damage modifier = 4 bo base damage to 4)
    public static final RegistryObject<Item> BONE_SKEWER_RAW = ITEMS.register("bone_skewer_raw",
            () -> new BoneSkewerRawItem(ModToolTiers.BONE_SKEWER_RAW, 4, -2.4f, new Item.Properties()));

    // Netherite Sword + Sharpness V + 2 = 13 total damage (attack damage modifier = 9 bo base damage to 4)
    public static final RegistryObject<Item> BONE_SKEWER = ITEMS.register("bone_skewer",
            () -> new BoneSkewerAdvancedItem(ModToolTiers.BONE_SKEWER_ADVANCED, 9, -2.4f, new Item.Properties()));

    public static final RegistryObject<Item> JAULL_FISH_VIAL = ITEMS.register("jaull_fish_vial",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}