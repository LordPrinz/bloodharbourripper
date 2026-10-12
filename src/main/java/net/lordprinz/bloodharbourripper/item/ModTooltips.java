package net.lordprinz.bloodharbourripper.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

/** Shared tooltip lines: "item.<ns>.<id>.desc" in gray, then the mod name. */
public final class ModTooltips {
    private ModTooltips() {}

    public static void append(Item item, Consumer<Component> tooltip) {
        var id = BuiltInRegistries.ITEM.getKey(item);
        tooltip.accept(Component.translatable("item." + id.getNamespace() + "." + id.getPath() + ".desc").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.empty());
        tooltip.accept(Component.translatable("tooltip.bloodharbourripper.modname").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
    }
}
