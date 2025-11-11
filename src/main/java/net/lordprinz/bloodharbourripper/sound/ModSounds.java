package net.lordprinz.bloodharbourripper.sound;

import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BloodHarbourRipper.MOD_ID);

    public static final RegistryObject<SoundEvent> EXECUTE_SOUND = registerSoundEvent("execute");

    public static final RegistryObject<SoundEvent> BONE_SKEWER_CHARGING = registerSoundEvent("bone_skewer_charging");

    public static final RegistryObject<SoundEvent> BONE_SKEWER_RELEASE = registerSoundEvent("bone_skewer_release");

    public static final RegistryObject<SoundEvent> BONE_SKEWER_HIT = registerSoundEvent("bone_skewer_hit");

    public static final RegistryObject<SoundEvent> BONE_SKEWER_RETURN = registerSoundEvent("bone_skewer_return");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(BloodHarbourRipper.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

