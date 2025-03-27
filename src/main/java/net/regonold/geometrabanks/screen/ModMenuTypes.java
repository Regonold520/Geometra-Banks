package net.regonold.geometrabanks.screen;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.regonold.geometrabanks.GeometraBanks;
import net.regonold.geometrabanks.screen.custom.ATMMenu;
import net.regonold.geometrabanks.screen.custom.CardMenu;

import java.awt.*;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, GeometraBanks.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ATMMenu>> ATM_MENU = registerMenuType("atm_menu", ATMMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<CardMenu>> CARD_MENU = registerMenuType("card_menu", CardMenu::new);


    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
