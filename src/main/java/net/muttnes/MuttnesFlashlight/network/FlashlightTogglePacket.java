package net.muttnes.MuttnesFlashlight.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FlashlightTogglePacket {

    private final boolean isOn;

    public FlashlightTogglePacket(boolean isOn) {
        this.isOn = isOn;
    }

    public static void encode(FlashlightTogglePacket packet, FriendlyByteBuf buf) {
        buf.writeBoolean(packet.isOn);
    }

    public static FlashlightTogglePacket decode(FriendlyByteBuf buf) {
        return new FlashlightTogglePacket(buf.readBoolean());
    }

    public static void handle(FlashlightTogglePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
