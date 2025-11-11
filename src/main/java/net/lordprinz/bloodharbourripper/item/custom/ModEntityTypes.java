package net.lordprinz.bloodharbourripper.item.custom;

import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BloodHarbourRipper.MOD_ID);

    public static final RegistryObject<EntityType<BoneSkewerSkeletonEntity>> BONE_SKEWER_SKELETON = ENTITY_TYPES.register("bone_skewer_skeleton",
            () -> EntityType.Builder.<BoneSkewerSkeletonEntity>of(BoneSkewerSkeletonEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("bone_skewer_skeleton"));

    public static final RegistryObject<EntityType<BoneSkewerRawEntity>> BONE_SKEWER_RAW = ENTITY_TYPES.register("bone_skewer_raw",
            () -> EntityType.Builder.<BoneSkewerRawEntity>of(BoneSkewerRawEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("bone_skewer_raw"));

    public static final RegistryObject<EntityType<BoneSkewerAdvancedEntity>> BONE_SKEWER_ADVANCED = ENTITY_TYPES.register("bone_skewer",
            () -> EntityType.Builder.<BoneSkewerAdvancedEntity>of(BoneSkewerAdvancedEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("bone_skewer"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}



