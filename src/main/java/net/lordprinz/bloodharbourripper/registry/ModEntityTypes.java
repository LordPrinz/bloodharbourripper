package net.lordprinz.bloodharbourripper.registry;

import net.lordprinz.bloodharbourripper.entity.BoneSkewerEntity;
import net.lordprinz.bloodharbourripper.entity.DashPhantomEntity;
import net.lordprinz.bloodharbourripper.entity.ExecuteMarkerEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

//? if >=1.21.2 {
import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
//?}

/**
 * Entity type ids are part of the save format: never rename them, only add new ones.
 */
public final class ModEntityTypes {
    public static final Supplier<EntityType<BoneSkewerEntity>> BONE_SKEWER_SKELETON = skewer("bone_skewer_skeleton");
    public static final Supplier<EntityType<BoneSkewerEntity>> BONE_SKEWER_RAW = skewer("bone_skewer_raw");
    public static final Supplier<EntityType<BoneSkewerEntity>> BONE_SKEWER = skewer("bone_skewer");

    public static final Supplier<EntityType<DashPhantomEntity>> DASH_PHANTOM = register("dash_phantom",
            EntityType.Builder.<DashPhantomEntity>of(DashPhantomEntity::new, MobCategory.MISC)
                    .sized(0.8F, 1.8F).clientTrackingRange(8).updateInterval(1).noSave());

    public static final Supplier<EntityType<ExecuteMarkerEntity>> EXECUTE_MARKER = register("execute_marker",
            EntityType.Builder.<ExecuteMarkerEntity>of(ExecuteMarkerEntity::new, MobCategory.MISC)
                    .sized(1.0F, 0.1F).clientTrackingRange(8).updateInterval(1).noSave());

    private ModEntityTypes() {}

    private static Supplier<EntityType<BoneSkewerEntity>> skewer(String name) {
        return register(name, EntityType.Builder.<BoneSkewerEntity>of(BoneSkewerEntity::new, MobCategory.MISC)
                .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
    }

    private static <T extends Entity> Supplier<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return RegistryHelper.register(BuiltInRegistries.ENTITY_TYPE, name, () -> build(builder, name));
    }

    private static <T extends Entity> EntityType<T> build(EntityType.Builder<T> builder, String name) {
        //? if >=1.21.2 {
        return builder.build(ResourceKey.create(Registries.ENTITY_TYPE, BloodHarbourRipper.id(name)));
        //?} else {
        /*return builder.build(name);
        *///?}
    }

    public static void init() {}
}
