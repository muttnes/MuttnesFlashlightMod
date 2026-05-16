package net.muttnes.MuttnesFlashlight.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.muttnes.MuttnesFlashlight.items.custom.FlashlightItem;
import net.muttnes.MuttnesFlashlight.server.FlashlightLightManager;
import net.muttnes.MuttnesFlashlight.server.FlashlightLightSource;
import net.muttnes.MuttnesFlashlight.state.FlashlightState;

import java.util.UUID;

@Mod.EventBusSubscriber
public class FlashlightEvents {

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        removePlayerFlashlights(player);
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        removePlayerFlashlights(player);
    }

    @SubscribeEvent
    public static void onServerStop(ServerStoppingEvent event) {
        FlashlightLightManager.clearAll();
    }

    private static void removePlayerFlashlights(ServerPlayer player) {

        removeFlashlight(player.getMainHandItem());
        removeFlashlight(player.getOffhandItem());
    }

    private static void removeFlashlight(ItemStack stack) {

        if (!(stack.getItem() instanceof FlashlightItem)) {
            return;
        }

        UUID id = FlashlightState.getId(stack);

        FlashlightLightSource source =
                FlashlightLightManager.remove(id);

        if (source != null) {
            source.remove();
        }
    }
}