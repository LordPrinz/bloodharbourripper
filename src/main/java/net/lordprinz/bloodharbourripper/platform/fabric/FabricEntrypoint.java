package net.lordprinz.bloodharbourripper.platform.fabric;

//? if fabric {
import net.fabricmc.api.ModInitializer;
import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.lordprinz.bloodharbourripper.loot.LootInjection;
import net.lordprinz.bloodharbourripper.network.ModNetworking;

//? if >=1.20.5 {
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
//?} else {
/*import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
*///?}

public final class FabricEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        BloodHarbourRipper.init();
        ModNetworking.register();

        //? if >=1.20.5 {
        LootTableEvents.MODIFY.register((key, table, source, registries) -> {
            if (source.isBuiltin() && LootInjection.isTarget(key)) table.withPool(LootInjection.jaullFishVialPool());
        });
        //?} else {
        /*LootTableEvents.MODIFY.register((resourceManager, lootManager, id, table, source) -> {
            if (source.isBuiltin() && LootInjection.isTarget(id)) table.withPool(LootInjection.jaullFishVialPool());
        });
        *///?}
    }
}
//?}
