package net.lordprinz.bloodharbourripper.registry;

import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.minecraft.core.Registry;

import java.util.function.Supplier;

//? if neoforge {
/*import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Map;
*///?} elif forge {
/*import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Map;
*///?}

/**
 * The only place that knows how each loader registers content.
 * Fabric registers immediately, (Neo)Forge queues entries in {@code DeferredRegister}s that are
 * bound to the mod event bus by the loader entrypoint via {@link #bind}.
 */
public final class RegistryHelper {
    private RegistryHelper() {}

    //? if fabric {
    public static <R, T extends R> Supplier<T> register(Registry<R> registry, String name, Supplier<T> factory) {
        T value = Registry.register(registry, BloodHarbourRipper.id(name), factory.get());
        return () -> value;
    }
    //?} else {
    /*private static final Map<ResourceKey<?>, DeferredRegister<?>> REGISTERS = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public static <R, T extends R> Supplier<T> register(Registry<R> registry, String name, Supplier<T> factory) {
        DeferredRegister<R> register = (DeferredRegister<R>) REGISTERS.computeIfAbsent(
                registry.key(), key -> DeferredRegister.create(registry.key(), BloodHarbourRipper.MOD_ID));
        return register.register(name, factory);
    }

    public static void bind(IEventBus modBus) {
        REGISTERS.values().forEach(register -> register.register(modBus));
    }
    *///?}
}
