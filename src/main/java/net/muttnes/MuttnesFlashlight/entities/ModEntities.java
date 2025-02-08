package net.muttnes.MuttnesFlashlight.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.muttnes.MuttnesFlashlight.MuttnesFlashlight;
import net.muttnes.MuttnesFlashlight.entities.custom.LightEntity;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
     DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MuttnesFlashlight.MODID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static final RegistryObject<EntityType<LightEntity>> LIGHT_ENTITY = ENTITIES.register("light_entity",
    () -> EntityType.Builder.<LightEntity>of(LightEntity::new, MobCategory.MISC)
        .sized(0.25F, 0.25F)
        .clientTrackingRange(4)
        .updateInterval(10)
        .build("light_entity"));

    
}
