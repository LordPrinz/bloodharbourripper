package net.lordprinz.bloodharbourripper.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

/**
 * Shared tooltip lines: "item.<ns>.<id>.desc" in gray, followed by any "…desc.2", "…desc.3" lines
 * present in the language file, then the mod name. Tooltips do not wrap, so keep each line short.
 */
public final class ModTooltips {
    private ModTooltips() {}

    public static void append(Item item, Consumer<Component> tooltip) {
        var id = BuiltInRegistries.ITEM.getKey(item);
        String key = "item." + id.getNamespace() + "." + id.getPath() + ".desc";
        tooltip.accept(Component.translatable(key).withStyle(ChatFormatting.GRAY));
        for (int line = 2; Language.getInstance().has(key + "." + line); line++) {
            tooltip.accept(Component.translatable(key + "." + line).withStyle(ChatFormatting.GRAY));
        }
        tooltip.accept(Component.empty());
        tooltip.accept(Component.translatable("tooltip.bloodharbourripper.modname").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
    }
}
