package net.regonold.geometrabanks.block.bank;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.regonold.geometrabanks.item.custom.Geocard;

public class GeocardSlot extends SlotItemHandler {

    public GeocardSlot(IItemHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof Geocard;
    }
}
