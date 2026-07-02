package net.lordprinz.bloodharbourripper.fabric;

import net.fabricmc.api.ModInitializer;
import net.lordprinz.bloodharbourripper.Constants;

public class BloodHarbourRipperFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.LOG.warn("{} is not yet ported to Fabric; the Forge version contains the mod content.", Constants.MOD_NAME);
    }
}
