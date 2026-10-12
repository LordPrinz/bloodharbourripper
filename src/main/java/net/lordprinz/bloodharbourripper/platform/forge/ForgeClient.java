package net.lordprinz.bloodharbourripper.platform.forge;

//? if forge {
/*import net.lordprinz.bloodharbourripper.client.ModClient;
import net.lordprinz.bloodharbourripper.client.ModKeyMappings;
import net.lordprinz.bloodharbourripper.client.ModRenderers;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;

/^* Client-only wiring; only loaded when running on the physical client. ^/
final class ForgeClient {
    private ForgeClient() {}

    static void init(IEventBus modBus) {
        modBus.addListener((RegisterKeyMappingsEvent event) -> {
            for (KeyMapping mapping : ModKeyMappings.ALL) event.register(mapping);
        });
        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> ModRenderers.registerAll(event::registerEntityRenderer));
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) ModClient.onEndClientTick(Minecraft.getInstance());
        });
    }
}
*///?}
