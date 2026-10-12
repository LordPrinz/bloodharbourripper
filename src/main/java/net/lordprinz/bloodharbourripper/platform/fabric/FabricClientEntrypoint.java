package net.lordprinz.bloodharbourripper.platform.fabric;

//? if fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.lordprinz.bloodharbourripper.client.ModClient;
import net.lordprinz.bloodharbourripper.client.ModKeyMappings;
import net.lordprinz.bloodharbourripper.client.ModRenderers;
import net.minecraft.client.KeyMapping;

public final class FabricClientEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (KeyMapping mapping : ModKeyMappings.ALL) {
            //? if >=26.1 {
            net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper.registerKeyMapping(mapping);
            //?} else {
            /*net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(mapping);
            *///?}
        }
        ClientTickEvents.END_CLIENT_TICK.register(ModClient::onEndClientTick);
        ModRenderers.registerAll(EntityRendererRegistry::register);
    }
}
//?}
