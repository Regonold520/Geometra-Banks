package net.regonold.geometrabanks.block.bank;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.regonold.geometrabanks.GeometraBanks;

import java.util.function.Supplier;

public class BankManager {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, GeometraBanks.MODID);

    public static final Supplier<AttachmentType<Integer>> BALANCE = ATTACHMENT_TYPES.register(
            "balance", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
