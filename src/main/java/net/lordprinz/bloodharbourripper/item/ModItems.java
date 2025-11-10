package net.lordprinz.bloodharbourripper.item;

import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.lordprinz.bloodharbourripper.item.custom.BoneSkewerItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BloodHarbourRipper.MOD_ID);

    public static final RegistryObject<Item> BONE_SKEWER_SKELETON = ITEMS.register("bone_skewer_skeleton",
            () -> new BoneSkewerItem(ModToolTiers.BONE_SKEWER, 3, -2.4f, new Item.Properties()));

    public static final RegistryObject<Item> JAULL_FISH_VIAL = ITEMS.register("jaull_fish_vial",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}