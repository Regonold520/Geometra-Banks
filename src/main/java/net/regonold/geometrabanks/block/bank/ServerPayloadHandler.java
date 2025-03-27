package net.regonold.geometrabanks.block.bank;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.regonold.geometrabanks.block.entity.ATMBlockEntity;
import net.regonold.geometrabanks.component.ModDataComponents;

public class ServerPayloadHandler {

    public static void handleDataOnMain(final SyncBalancePacket.MyData data, final IPayloadContext context) {}
    public static void handleDataOnCard(final SyncCardPacket.MyData data, final IPayloadContext context) {}

    public static void handleBankTab(final SyncBankTab.MyData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ATMBlockEntity AtEntity = (ATMBlockEntity) context.player().level().getBlockEntity(data.entityPos());
            AtEntity.setTab(data.tab());
        });
    }

    public static void handlePinSync(final SyncPinPacket.MyData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            System.out.println(data.item().has(ModDataComponents.PIN_COMPONENT));
            context.player().getWeaponItem().set(ModDataComponents.PIN_COMPONENT, data.pin());
            context.player().getWeaponItem().set(ModDataComponents.UUID_COMPONENT, context.player().getUUID());
        });

    }
}