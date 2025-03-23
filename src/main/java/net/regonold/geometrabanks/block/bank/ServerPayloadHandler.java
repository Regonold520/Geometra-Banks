package net.regonold.geometrabanks.block.bank;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

    public static void handleDataOnMain(final SyncBalancePacket.MyData data, final IPayloadContext context) {}
    public static void handleDataOnCard(final SyncCardPacket.MyData data, final IPayloadContext context) {}
}