package net.lordprinz.bloodharbourripper.platform.neoforge;

//? if neoforge {
/*import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.lordprinz.bloodharbourripper.loot.LootInjection;
import net.lordprinz.bloodharbourripper.network.ModNetworking;
import net.lordprinz.bloodharbourripper.registry.RegistryHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.LootTableLoadEvent;

@Mod(BloodHarbourRipper.MOD_ID)
public final class NeoForgeEntrypoint {
    public NeoForgeEntrypoint(IEventBus modBus) {
        BloodHarbourRipper.init();
        RegistryHelper.bind(modBus);
        modBus.addListener(ModNetworking::register);
        NeoForge.EVENT_BUS.addListener((LootTableLoadEvent event) -> {
            if (LootInjection.isTarget(event.getKey())) event.getTable().addPool(LootInjection.jaullFishVialPool().build());
        });
    }
}
*///?}
