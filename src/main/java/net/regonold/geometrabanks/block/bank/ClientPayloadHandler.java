package net.regonold.geometrabanks.block.bank;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.regonold.geometrabanks.block.entity.ATMBlockEntity;

public class ClientPayloadHandler {

    public static void handleDataOnMain(final SyncBalancePacket.MyData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) return;
            if (Minecraft.getInstance().player == null) return;

            Minecraft.getInstance().player.setData(BankManager.BALANCE, data.balance());

        });
    }

    public static void handleDataOnCard(final SyncCardPacket.MyData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) return;
            ATMBlockEntity bE = (ATMBlockEntity) Minecraft.getInstance().level.getBlockEntity(data.entityPos());

            if (bE != null) {
                bE.atmMenu.newBalanceRender = data.balance();
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.setData(BankManager.BALANCE, data.balance());
                }
            }
        });
    }


    public static void handleBankTab(final SyncBankTab.MyData data, final IPayloadContext context) {

    }

    public static void handlePinSync(final SyncPinPacket.MyData data, final IPayloadContext context) {

    }

}