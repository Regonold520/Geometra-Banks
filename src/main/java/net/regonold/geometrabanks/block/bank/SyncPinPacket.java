package net.regonold.geometrabanks.block.bank;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.regonold.geometrabanks.GeometraBanks;

import java.util.UUID;

public class SyncPinPacket {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                MyData.TYPE,
                MyData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handlePinSync,
                        ServerPayloadHandler::handlePinSync
                )
        );
    }

    public record MyData(ItemStack item, int pin) implements CustomPacketPayload {

        public static final Type<MyData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "card"));

        // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
        // 'name' will be encoded and decoded as a string
        // 'age' will be encoded and decoded as an integer
        // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
        public static final StreamCodec<RegistryFriendlyByteBuf, MyData> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC,
                MyData::item,
                ByteBufCodecs.VAR_INT,
                MyData::pin,
                MyData::new
        );


        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
