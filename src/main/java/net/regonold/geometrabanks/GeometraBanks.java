package net.regonold.geometrabanks;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.regonold.geometrabanks.block.ModBlocks;
import net.regonold.geometrabanks.block.bank.BankManager;
import net.regonold.geometrabanks.block.bank.SyncBalancePacket;
import net.regonold.geometrabanks.block.bank.SyncBankTab;
import net.regonold.geometrabanks.block.bank.SyncPinPacket;
import net.regonold.geometrabanks.block.entity.ModBlockEntities;
import net.regonold.geometrabanks.component.ModDataComponents;
import net.regonold.geometrabanks.item.ModCreativeModeTabs;
import net.regonold.geometrabanks.item.ModItems;
import net.regonold.geometrabanks.screen.ModMenuTypes;
import net.regonold.geometrabanks.screen.custom.ATMScreen;
import net.regonold.geometrabanks.screen.custom.CardScreen;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.awt.*;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(GeometraBanks.MODID)
public class GeometraBanks
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "geometrabanks";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();




    public GeometraBanks(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        BankManager.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(ModBlocks.ATM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.ATM_MENU.get(), ATMScreen::new);
            event.register(ModMenuTypes.CARD_MENU.get(), CardScreen::new);
        }

        @SubscribeEvent
        public static void registerSyncBalance(RegisterPayloadHandlersEvent event) {
            SyncBalancePacket.register(event);
            SyncBankTab.register(event);
            SyncPinPacket.register(event);
        }
    }
}
