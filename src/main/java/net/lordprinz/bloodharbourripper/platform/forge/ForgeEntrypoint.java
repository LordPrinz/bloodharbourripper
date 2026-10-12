package net.lordprinz.bloodharbourripper.platform.forge;

//? if forge {
/*import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.lordprinz.bloodharbourripper.loot.LootInjection;
import net.lordprinz.bloodharbourripper.network.ModNetworking;
import net.lordprinz.bloodharbourripper.registry.RegistryHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(BloodHarbourRipper.MOD_ID)
public final class ForgeEntrypoint {
    public ForgeEntrypoint(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        BloodHarbourRipper.init();
        RegistryHelper.bind(modBus);
        ModNetworking.register();
        MinecraftForge.EVENT_BUS.addListener((LootTableLoadEvent event) -> {
            if (LootInjection.isTarget(event.getName())) {
                event.getTable().addPool(LootInjection.jaullFishVialPool().name(BloodHarbourRipper.MOD_ID + ":jaull_fish_vial").build());
            }
        });
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClient.init(modBus);
        }
    }
}
*///?}
