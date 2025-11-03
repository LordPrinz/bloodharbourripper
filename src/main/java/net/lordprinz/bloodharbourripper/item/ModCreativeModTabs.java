package net.lordprinz.bloodharbourripper.item;

import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BloodHarbourRipper.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BLOOD_HARBOUR_RIPPER_TAB = CREATIVE_MODE_TABS.register("blood_harbour_ripper_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack((ModItems.BONE_SKEWER_SKELETON.get())))
                    .title(Component.translatable("creativetab.blood_harbour_ripper_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.BONE_SKEWER_SKELETON.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
