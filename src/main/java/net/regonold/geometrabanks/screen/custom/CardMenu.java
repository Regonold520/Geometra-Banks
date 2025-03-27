package net.regonold.geometrabanks.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.regonold.geometrabanks.screen.ModMenuTypes;

public class CardMenu extends AbstractContainerMenu {

    public Player player;

    public CardMenu(int containerId, Inventory inv) {
        super(ModMenuTypes.CARD_MENU.get(), containerId);
        this.player = inv.player;
    }

    public CardMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv);
        this.player = inv.player;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}
