package net.lordprinz.bloodharbourripper.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public final class ModCreativeTabs {
    public static final Supplier<CreativeModeTab> MAIN = RegistryHelper.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            "blood_harbour_ripper_tab", () -> builder()
                    .title(Component.translatable("itemGroup.bloodharbourripper.main"))
                    .icon(() -> new ItemStack(ModItems.BONE_SKEWER.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.BONE_SKEWER_SKELETON.get());
                        output.accept(ModItems.BONE_SKEWER_RAW.get());
                        output.accept(ModItems.BONE_SKEWER.get());
                        output.accept(ModItems.JAULL_FISH_VIAL.get());
                        output.accept(ModItems.HARPOON_HEAD.get());
                        output.accept(ModItems.BOUND_HAFT.get());
                        output.accept(ModItems.ABYSSAL_CORE.get());
                        output.accept(ModItems.DROWNED_HEART.get());
                    })
                    .build());

    private ModCreativeTabs() {}

    private static CreativeModeTab.Builder builder() {
        //? if fabric && >=26.1 {
        return net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab.builder();
        //?} elif fabric {
        /*return net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup.builder();
        *///?} else {
        /*return CreativeModeTab.builder();
        *///?}
    }

    public static void init() {}
}
