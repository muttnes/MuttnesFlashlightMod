package net.muttnes.MuttnesFlashlight.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FlashlightStatePacket {
    private final InteractionHand hand;
    private final boolean on;
    private final int batteryLevel;

    public FlashlightStatePacket(InteractionHand hand, boolean on, int batteryLevel) {
        this.hand = hand;
        this.on = on;
        this.batteryLevel = batteryLevel;
    }

    public static FlashlightStatePacket decode(FriendlyByteBuf buf) {
        return new FlashlightStatePacket(buf.readEnum(InteractionHand.class), buf.readBoolean(), buf.readInt());
    }

    public static void encode(FlashlightStatePacket packet, FriendlyByteBuf buf) {
        buf.writeEnum(packet.hand);
        buf.writeBoolean(packet.on);
        buf.writeInt(packet.batteryLevel);
    }

    public static void handle(FlashlightStatePacket packet, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            updateClientFlashlightState(packet.hand, packet.on, packet.batteryLevel);
        });
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void updateClientFlashlightState(InteractionHand hand, boolean on, int batteryLevel) {
        var mc = Minecraft.getInstance();
        if (mc.player != null) {
            var stack = hand == InteractionHand.MAIN_HAND ? mc.player.getMainHandItem() : mc.player.getOffhandItem();
            if (stack.getItem() instanceof net.muttnes.MuttnesFlashlight.items.custom.FlashlightItem) {
                stack.getOrCreateTag().putBoolean("on", on);
                stack.getOrCreateTag().putInt("batteryLevel", batteryLevel);
            }
        }
    }
}
