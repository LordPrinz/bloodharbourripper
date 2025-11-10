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

    public static final RegistryObject<EntityType<BoneSkewerEntity>> BONE_SKEWER = ENTITY_TYPES.register("bone_skewer",
            () -> EntityType.Builder.<BoneSkewerEntity>of(BoneSkewerEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("bone_skewer"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

