package net.lordprinz.bloodharbourripper.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

//? if >=1.21.5 {
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
//?} elif >=1.20.5 {
/*import java.util.List;
*///?} else {
/*import net.minecraft.world.level.Level;
import java.util.List;
*///?}

/** A plain item with the mod's standard description tooltip. */
public class TooltipItem extends Item {
    public TooltipItem(Item.Properties properties) {
        super(properties);
    }

    //? if >=1.21.5 {
    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        ModTooltips.append(this, tooltip);
    }
    //?} elif >=1.20.5 {
    /*@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        ModTooltips.append(this, tooltip::add);
    }
    *///?} else {
    /*@Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        ModTooltips.append(this, tooltip::add);
    }
    *///?}
}
