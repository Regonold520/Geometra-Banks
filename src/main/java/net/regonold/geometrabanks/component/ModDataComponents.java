package net.regonold.geometrabanks.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.regonold.geometrabanks.GeometraBanks;

import java.util.UUID;
import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, GeometraBanks.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> UUID_COMPONENT = register("uuid",
            builder -> builder.persistent(UUID_CODEC));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> buildOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> buildOperator.apply(DataComponentType.builder()).build());
    }


    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
