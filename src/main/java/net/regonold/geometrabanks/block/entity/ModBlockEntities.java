package net.regonold.geometrabanks.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.regonold.geometrabanks.GeometraBanks;
import net.regonold.geometrabanks.block.ModBlocks;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, GeometraBanks.MODID);

    public static final Supplier<BlockEntityType<ATMBlockEntity>> ATM_BE =
            BLOCK_ENTITIES.register("atm_be", () -> BlockEntityType.Builder.of(
                    ATMBlockEntity::new, ModBlocks.ATM.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
