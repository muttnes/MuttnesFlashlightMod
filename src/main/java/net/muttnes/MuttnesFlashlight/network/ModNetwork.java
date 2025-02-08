package net.muttnes.MuttnesFlashlight.network;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.muttnes.MuttnesFlashlight.MuttnesFlashlight;

public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MuttnesFlashlight.MODID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void registerMessages() {
        registerMessage(FlashlightTogglePacket.class, FlashlightTogglePacket::encode, FlashlightTogglePacket::decode, FlashlightTogglePacket::handle);
    }

    public static <T> void registerMessage(Class<T> messageType,
                                             java.util.function.BiConsumer<T, net.minecraft.network.FriendlyByteBuf> encoder,
                                             java.util.function.Function<net.minecraft.network.FriendlyByteBuf, T> decoder,
                                             java.util.function.BiConsumer<T, java.util.function.Supplier<net.minecraftforge.network.NetworkEvent.Context>> handler) {
        CHANNEL.registerMessage(packetId++, messageType, encoder, decoder, handler);
    }
}
