package net.regonold.geometrabanks.block.bank;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class RestrictedSlot extends SlotItemHandler {
    public final ItemStack restrictedItem;

    public RestrictedSlot(IItemHandler handler, int index, int x, int y, ItemStack restrictedItem) {
        super(handler, index, x, y);
        this.restrictedItem = restrictedItem;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (restrictedItem.isEmpty()) {
            return false;
        }
        return stack.is(restrictedItem.getItem());
    }
}
