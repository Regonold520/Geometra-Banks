package net.regonold.geometrabanks.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.regonold.geometrabanks.GeometraBanks;
import net.regonold.geometrabanks.component.ModDataComponents;
import net.regonold.geometrabanks.item.custom.Geocard;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GeometraBanks.MODID);

    public static final DeferredItem<Geocard> GREEN_GEOCARD = ITEMS.register("green_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Geocard> RED_GEOCARD = ITEMS.register("red_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Geocard> BLUE_GEOCARD = ITEMS.register("blue_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Geocard> PURPLE_GEOCARD = ITEMS.register("purple_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Geocard> ORANGE_GEOCARD = ITEMS.register("orange_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Geocard> YELLOW_GEOCARD = ITEMS.register("yellow_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Geocard> SPRIG_GEOCARD = ITEMS.register("sprig_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Geocard> PUMPKABOO_GEOCARD = ITEMS.register("pumpkaboo_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Geocard> BISHARP_GEOCARD = ITEMS.register("bisharp_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Geocard> CYNDAQUIL_GEOCARD = ITEMS.register("cyndaquil_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Geocard> PANDAA_GEOCARD = ITEMS.register("pandaa_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Geocard> CYPHER_GEOCARD = ITEMS.register("cypher_geocard", () -> new Geocard(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BLANK_GEOCARD = ITEMS.register("blank_geocard", () -> new Item(new Item.Properties().stacksTo(64)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
