package net.lordprinz.bloodharbourripper.registry;

import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public final class ModSounds {
    public static final Supplier<SoundEvent> EXECUTE = register("execute");
    public static final Supplier<SoundEvent> BONE_SKEWER_CHARGING = register("bone_skewer_charging");
    public static final Supplier<SoundEvent> BONE_SKEWER_RELEASE = register("bone_skewer_release");
    public static final Supplier<SoundEvent> BONE_SKEWER_HIT = register("bone_skewer_hit");
    public static final Supplier<SoundEvent> BONE_SKEWER_RETURN = register("bone_skewer_return");
    public static final Supplier<SoundEvent> DASH = register("dash");
    public static final Supplier<SoundEvent> DASH_HIT = register("dash_hit");

    private ModSounds() {}

    private static Supplier<SoundEvent> register(String name) {
        Identifier id = BloodHarbourRipper.id(name);
        return RegistryHelper.register(BuiltInRegistries.SOUND_EVENT, name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void init() {}
}
