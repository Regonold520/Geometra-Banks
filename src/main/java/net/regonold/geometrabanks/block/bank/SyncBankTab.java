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

public class SyncBankTab {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                SyncBankTab.MyData.TYPE,
                SyncBankTab.MyData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleBankTab,
                        ServerPayloadHandler::handleBankTab
                )
        );
    }

    public record MyData(BlockPos entityPos, String tab) implements CustomPacketPayload {

        public static final Type<SyncBankTab.MyData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "banktab"));


        public static final StreamCodec<ByteBuf, SyncBankTab.MyData> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC,
                SyncBankTab.MyData::entityPos,
                ByteBufCodecs.STRING_UTF8,
                SyncBankTab.MyData::tab,
                SyncBankTab.MyData::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
