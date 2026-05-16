package net.muttnes.MuttnesFlashlight.service;

import atomicstryker.dynamiclights.server.DynamicLights;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.muttnes.MuttnesFlashlight.server.FlashlightLightManager;
import net.muttnes.MuttnesFlashlight.server.FlashlightLightSource;

import java.util.UUID;

public class FlashlightController {

    public static void ensureLight(ServerLevel world, ServerPlayer player, ItemStack stack, UUID id) {
        if (!FlashlightLightManager.contains(id)) {
            FlashlightLightSource light =
                    new FlashlightLightSource(world, player, 13);

            DynamicLights.addLightSource(light);
            FlashlightLightManager.put(id, light);
        }
    }

    public static void updateLight(UUID id) {
        FlashlightLightSource source = FlashlightLightManager.get(id);
        if (source != null) {
            source.update();
        }
    }

    public static void removeLight(UUID id) {
        FlashlightLightSource source =
                FlashlightLightManager.remove(id);

        if (source != null) {
            source.remove();
        }
    }

    public static void turnOff(ItemStack stack, UUID id) {
        stack.getOrCreateTag().putBoolean("on", false);
        removeLight(id);
    }
}