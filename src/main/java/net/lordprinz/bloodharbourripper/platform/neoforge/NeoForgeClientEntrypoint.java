package net.lordprinz.bloodharbourripper.platform.neoforge;

//? if neoforge {
/*import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.lordprinz.bloodharbourripper.client.ModClient;
import net.lordprinz.bloodharbourripper.client.ModKeyMappings;
import net.lordprinz.bloodharbourripper.client.ModRenderers;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = BloodHarbourRipper.MOD_ID, dist = Dist.CLIENT)
public final class NeoForgeClientEntrypoint {
    public NeoForgeClientEntrypoint(IEventBus modBus) {
        modBus.addListener((RegisterKeyMappingsEvent event) -> {
            //? if >=1.21.9
            event.registerCategory(ModKeyMappings.CATEGORY);
            for (KeyMapping mapping : ModKeyMappings.ALL) event.register(mapping);
        });
        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> ModRenderers.registerAll(event::registerEntityRenderer));
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> ModClient.onEndClientTick(Minecraft.getInstance()));
    }
}
*///?}
