package net.lordprinz.bloodharbourripper.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

//? if >=1.21.9 {
import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
//?}

/** Key mappings; each loader entrypoint registers {@link #ALL} (and the category on NeoForge). */
public final class ModKeyMappings {
    //? if >=1.21.9 && neoforge {
    /*public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(BloodHarbourRipper.id("main"));
    *///?} elif >=1.21.9 {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(BloodHarbourRipper.id("main"));
    //?} else {
    /*public static final String CATEGORY = "key.categories.bloodharbourripper";
    *///?}

    /** Shift + C by default. */
    public static final KeyMapping EXECUTE = new KeyMapping("key.bloodharbourripper.execute", InputConstants.KEY_C, CATEGORY);
    /** Shift + X by default. */
    public static final KeyMapping DASH = new KeyMapping("key.bloodharbourripper.dash", InputConstants.KEY_X, CATEGORY);

    public static final KeyMapping[] ALL = {EXECUTE, DASH};

    private ModKeyMappings() {}
}
