package net.muttnes.MuttnesFlashlight.client;

import net.muttnes.MuttnesFlashlight.items.custom.FlashlightItem;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FlashlightHUD {
    
    private static String batteryText = "";

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            ItemStack mainHand = mc.player.getMainHandItem();
            ItemStack offHand = mc.player.getOffhandItem();
            
            if (mainHand.getItem() instanceof FlashlightItem || offHand.getItem() instanceof FlashlightItem) {
                ItemStack flashlight = mainHand.getItem() instanceof FlashlightItem ? mainHand : offHand;
                int batteryLevel = FlashlightItem.getBatteryLevel(flashlight);
                batteryText = I18n.get("hud.muttnes_flashlight.battery", batteryLevel);
            } else {
                batteryText = "";
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (!batteryText.isEmpty()) {
            mc.gui.setOverlayMessage(Component.literal(batteryText), false);
        }
    }
}
