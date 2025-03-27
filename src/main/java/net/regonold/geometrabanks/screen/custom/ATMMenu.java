package net.regonold.geometrabanks.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.regonold.geometrabanks.GeometraBanks;
import net.regonold.geometrabanks.block.ModBlocks;
import net.regonold.geometrabanks.block.bank.GeocardSlot;
import net.regonold.geometrabanks.block.bank.RestrictedSlot;
import net.regonold.geometrabanks.block.entity.ATMBlockEntity;
import net.regonold.geometrabanks.screen.ModMenuTypes;
import net.sohpandaa.geometracoins.item.ModItems;

import java.util.Objects;

public class ATMMenu extends AbstractContainerMenu {
    public final ATMBlockEntity blockEntity;
    public ATMScreen AtScreen;
    private final Level level;
    public Player player;
    public Inventory slotInv;

    public int newBalanceRender;

    private static final int TOTAL_TE_SLOTS = 10;

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_ROW_COUNT * PLAYER_INVENTORY_COLUMN_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

    public ATMMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
        this.player = inv.player;
    }

    public ATMMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.ATM_MENU.get(), containerId);
        this.blockEntity = (ATMBlockEntity) blockEntity;
        this.level = inv.player.level();
        this.slotInv = inv;

        ((ATMBlockEntity) blockEntity).atmMenu = this;
        updateSlots();
    }

    public void updateSlots() {
        this.slots.clear();

        if (Objects.equals(blockEntity.bankTab, "deposit")) {
            this.blockEntity.refundWithdrawnCoins();

            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 0, 18, 33, ModItems.COIN1.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 1, 18, 55, ModItems.COIN4.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 2, 18, 77, ModItems.COIN16.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 3, 39, 44, ModItems.COIN64.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 4, 39, 67, ModItems.COIN256.toStack()));

            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 5, -1000, -1000, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 6, -1000, -1000, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 7, -1000, -1000, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 8, -1000, -1000, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 9, -1000, -1000, Items.BEDROCK.getDefaultInstance()));

            this.addSlot(new GeocardSlot(this.blockEntity.inventory, 10, 172, 72));
        } else if (Objects.equals(blockEntity.bankTab, "withdraw")) {
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 0, -1000, -1000, ModItems.COIN1.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 1, -1000, -1000, ModItems.COIN4.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 2, -1000, -1000, ModItems.COIN16.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 3, -1000, -1000, ModItems.COIN64.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 4, -1000, -1000, ModItems.COIN256.toStack()));

            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 5, 34, 83, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 6, 57, 83, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 7, 80, 83, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 8, 103, 83, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 9, 126, 83, Items.BEDROCK.getDefaultInstance()));

            this.addSlot(new GeocardSlot(this.blockEntity.inventory, 10, 172, 72));
        } else if (Objects.equals(blockEntity.bankTab, "pin")) {
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 0, -1000, -1000, ModItems.COIN1.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 1, -1000, -1000, ModItems.COIN4.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 2, -1000, -1000, ModItems.COIN16.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 3, -1000, -1000, ModItems.COIN64.toStack()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 4, -1000, -1000, ModItems.COIN256.toStack()));

            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 5, -1000, -1000, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 6, -1000, -1000, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 7, -1000, -1000, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 8, -1000, -1000, Items.BEDROCK.getDefaultInstance()));
            this.addSlot(new RestrictedSlot(this.blockEntity.inventory, 9, -1000, -1000, Items.BEDROCK.getDefaultInstance()));

            this.addSlot(new GeocardSlot(this.blockEntity.inventory, 10, 172, 72));
        }

        addPlayerInventory(slotInv);
        addPlayerHotbar(slotInv);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        if (Objects.equals(blockEntity.bankTab, "pin")) {
            return ItemStack.EMPTY;
        }

        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem())
            return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        int TE_ACTIVE_START, TE_ACTIVE_END;
        if (Objects.equals(blockEntity.bankTab, "deposit")) {
            TE_ACTIVE_START = 0;
            TE_ACTIVE_END = 5;
        } else {
            TE_ACTIVE_START = 5;
            TE_ACTIVE_END = 10;
        }
        int PLAYER_INV_START = TOTAL_TE_SLOTS;
        int PLAYER_INV_END = TOTAL_TE_SLOTS + VANILLA_SLOT_COUNT;

        if (pIndex >= PLAYER_INV_START && pIndex < PLAYER_INV_END) {
            if (Objects.equals(blockEntity.bankTab, "deposit")) {
                if (!moveItemStackTo(sourceStack, TE_ACTIVE_START, TE_ACTIVE_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (Objects.equals(blockEntity.bankTab, "withdraw")) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex >= TE_ACTIVE_START && pIndex < TE_ACTIVE_END) {
            if (!moveItemStackTo(sourceStack, PLAYER_INV_START, PLAYER_INV_END, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlocks.ATM.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 122 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 180));
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        blockEntity.opened = false;
        blockEntity.bankTab = "pin";

        if (blockEntity instanceof ATMBlockEntity atm) {
            atm.refundWithdrawnCoins();
        }

        if (!player.level().isClientSide()) {
            ATMBlockEntity atmBlockEntity = this.blockEntity;

            if (atmBlockEntity != null) {
                // Ensure we safely check for null before using the slot.
                ItemStack slot10Stack = atmBlockEntity.inventory.getStackInSlot(10);
                if (slot10Stack != null && !slot10Stack.isEmpty()) {
                    boolean addedToInventory = player.getInventory().add(slot10Stack);
                    if (addedToInventory) {
                        atmBlockEntity.inventory.setStackInSlot(10, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == 0) {
            blockEntity.bankTab = "deposit";
            blockEntity.withdrew = false;
            updateSlots();
            broadcastChanges();
            return true;
        } else if (id == 1) {
            blockEntity.bankTab = "withdraw";
            if (!blockEntity.getLevel().isClientSide()) {
                if (blockEntity.depositPlayer != null && !blockEntity.withdrew) {
                    blockEntity.withdrew = true;
                }
            }
            updateSlots();
            broadcastChanges();
            return true;
        }
        return false;
    }
}
