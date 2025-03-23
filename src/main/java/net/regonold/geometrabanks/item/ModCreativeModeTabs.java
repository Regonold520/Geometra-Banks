package net.regonold.geometrabanks.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.regonold.geometrabanks.GeometraBanks;
import net.regonold.geometrabanks.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GeometraBanks.MODID);

    public static Supplier<CreativeModeTab> GEO_BANKS_TAB = CREATIVE_MODE_TAB.register("geo_banks_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.ATM.asItem()))
                    .title(Component.literal("Geometra Banks Items"))

                    .displayItems(new CreativeModeTab.DisplayItemsGenerator() {
                        @Override
                        public void accept(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
                            output.accept(ModBlocks.ATM);

                            output.accept(ModItems.BLANK_GEOCARD);

                            output.accept(ModItems.RED_GEOCARD);
                            output.accept(ModItems.ORANGE_GEOCARD);
                            output.accept(ModItems.YELLOW_GEOCARD);
                            output.accept(ModItems.PURPLE_GEOCARD);
                            output.accept(ModItems.GREEN_GEOCARD);
                            output.accept(ModItems.BLUE_GEOCARD);

                            output.accept(ModItems.SPRIG_GEOCARD);
                            output.accept(ModItems.PUMPKABOO_GEOCARD);
                            output.accept(ModItems.BISHARP_GEOCARD);


                            output.accept(ModItems.PANDAA_GEOCARD);
                            output.accept(ModItems.CYPHER_GEOCARD);
                        }
                    })

                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
