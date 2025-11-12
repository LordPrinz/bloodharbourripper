package net.lordprinz.bloodharbourripper.client;

import net.lordprinz.bloodharbourripper.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "bloodharbourripper", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemTooltipHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.getItem() == ModItems.BONE_SKEWER_SKELETON.get()) {
            event.getToolTip().add(Component.translatable("item.bloodharbourripper.bone_skewer_skeleton.desc").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("tooltip.bloodharbourripper.modname").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        }
        else if (stack.getItem() == ModItems.BONE_SKEWER_RAW.get()) {
            event.getToolTip().add(Component.translatable("item.bloodharbourripper.bone_skewer_raw.desc").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("tooltip.bloodharbourripper.modname").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        }
        else if (stack.getItem() == ModItems.BONE_SKEWER.get()) {
            event.getToolTip().add(Component.translatable("item.bloodharbourripper.bone_skewer.desc").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("tooltip.bloodharbourripper.modname").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        }
        else if (stack.getItem() == ModItems.JAULL_FISH_VIAL.get()) {
            event.getToolTip().add(Component.translatable("item.bloodharbourripper.jaull_fish_vial.desc").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("tooltip.bloodharbourripper.modname").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        }
    }
}

