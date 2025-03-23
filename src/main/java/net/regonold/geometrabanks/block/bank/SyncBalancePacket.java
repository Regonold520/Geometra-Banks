package net.regonold.geometrabanks.block.bank;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.regonold.geometrabanks.GeometraBanks;

public class SyncBalancePacket {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                MyData.TYPE,
                MyData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleDataOnMain,
                        ServerPayloadHandler::handleDataOnMain
                )
        );
    }

    public record MyData(int balance, BlockPos pos) implements CustomPacketPayload {

        public static final CustomPacketPayload.Type<MyData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "balance"));

        // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
        // 'name' will be encoded and decoded as a string
        // 'age' will be encoded and decoded as an integer
        // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
        public static final StreamCodec<ByteBuf, MyData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                MyData::balance,
                BlockPos.STREAM_CODEC,
                MyData::pos,
                MyData::new
        );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
