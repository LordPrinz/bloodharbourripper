package net.lordprinz.bloodharbourripper;

import net.lordprinz.bloodharbourripper.registry.ModCreativeTabs;
import net.lordprinz.bloodharbourripper.registry.ModEntityTypes;
import net.lordprinz.bloodharbourripper.registry.ModItems;
import net.lordprinz.bloodharbourripper.registry.ModSounds;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loader-independent entry point. Every loader entrypoint in the {@code platform} package calls {@link #init()}
 * first and then wires its own events to the shared logic.
 */
public final class BloodHarbourRipper {
    public static final String MOD_ID = "bloodharbourripper";
    public static final String VERSION = /*$ mod_version*/ "1.1.3";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private BloodHarbourRipper() {}

    public static Identifier id(String path) {
        //? if >=1.21 {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
        //?} else {
        /*return new Identifier(MOD_ID, path);
        *///?}
    }

    /** Registers all content. Order matters on Fabric, where registration happens immediately. */
    public static void init() {
        ModSounds.init();
        ModEntityTypes.init();
        ModItems.init();
        ModCreativeTabs.init();
        LOGGER.info("Blood Harbour Ripper {} initialised", VERSION);
    }
}
