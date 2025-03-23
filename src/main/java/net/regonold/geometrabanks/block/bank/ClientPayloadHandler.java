package net.regonold.geometrabanks.block.bank;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.regonold.geometrabanks.block.entity.ATMBlockEntity;

public class ClientPayloadHandler {

    public static void handleDataOnMain(final SyncBalancePacket.MyData data, final IPayloadContext context) {
        assert Minecraft.getInstance().level != null;
        Minecraft.getInstance().level.getPlayerByUUID(context.player().getUUID()).setData(BankManager.BALANCE, data.balance());
    }

    public static void handleDataOnCard(final SyncCardPacket.MyData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) return;
            ATMBlockEntity bE = (ATMBlockEntity) Minecraft.getInstance().level.getBlockEntity(data.entityPos());

            if (bE != null) {
                bE.atmMenu.newBalanceRender = data.balance();
                context.player().setData(BankManager.BALANCE, data.balance());
                System.out.println("Balance updated on client: " + data.balance());
            } else {
                System.err.println("ATMBlockEntity not found at " + data.entityPos());
            }
        });
    }

}