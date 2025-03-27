package net.regonold.geometrabanks.block.entity;

import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import net.regonold.geometrabanks.block.bank.BankManager;
import net.regonold.geometrabanks.block.bank.SyncBalancePacket;
import net.regonold.geometrabanks.block.bank.SyncCardPacket;
import net.regonold.geometrabanks.component.ModDataComponents;
import net.regonold.geometrabanks.screen.custom.ATMMenu;
import net.sohpandaa.geometracoins.item.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ATMBlockEntity extends BlockEntity implements MenuProvider {
    public ServerPlayer depositPlayer;
    public boolean withdrew = false;

    public final ItemStackHandler inventory = new ItemStackHandler(11) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 128;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();

            if (depositPlayer != null) {
                handleDeposit(slot);

            }

            if (!level.isClientSide()) {
                if (slot == 10 && inventory.getStackInSlot(10).isEmpty() && bankTab.equals("withdraw")) {
                    refundWithdrawnCoins();
                    bankTab = "pin";
                }

                if (slot == 10 && !inventory.getStackInSlot(10).isEmpty() && bankTab.equals("withdraw")) {
                    ItemStack card = getStackInSlot(10);
                    if (!card.isEmpty() && card.get(ModDataComponents.UUID_COMPONENT) != null) {
                        Player cardPlayer = level.getPlayerByUUID(card.get(ModDataComponents.UUID_COMPONENT));
                        depositPlayer = (ServerPlayer) cardPlayer;
                        handleWithdraw();
                    }
                }

                if (slot == 10) {
                    ItemStack card = getStackInSlot(10);
                    if (!card.isEmpty() && card.get(ModDataComponents.UUID_COMPONENT) != null) {
                        Player cardPlayer = level.getPlayerByUUID(card.get(ModDataComponents.UUID_COMPONENT));
                        if (cardPlayer instanceof ServerPlayer) {
                            depositPlayer = (ServerPlayer) cardPlayer;
                            if (!withdrew) {
                                withdrew = true;
                            }
                        }
                    } else {
                        if (withdrew) {
                            withdrew = false;
                        }
                        depositPlayer = null;
                    }
                }
            } else {
                if (slot == 10 && getStackInSlot(10) != ItemStack.EMPTY) {
                    ItemStack card = getStackInSlot(slot);
                    if (card.get(ModDataComponents.UUID_COMPONENT) != null) {
                        Player cardPlayer = level.getPlayerByUUID(card.get(ModDataComponents.UUID_COMPONENT));
                        atmMenu.newBalanceRender = cardPlayer.getData(BankManager.BALANCE);
                    }
                }
                if (slot == 10 && getStackInSlot(10) == ItemStack.EMPTY) {
                    atmMenu.newBalanceRender = 0;
                    if (!bankTab.equals("pin")) {
                        bankTab = "pin";
                        if (atmMenu.AtScreen != null) {
                            System.out.println("eariossss");
                            atmMenu.AtScreen.setupButtons();
                        }
                        atmMenu.updateSlots();
                    }
                }

                if (slot < 10 && getStackInSlot(10) != ItemStack.EMPTY && bankTab.equals("deposit")) {
                    ItemStack card = getStackInSlot(10);
                    if (card.get(ModDataComponents.UUID_COMPONENT) != null) {
                        Player cardPlayer = level.getPlayerByUUID(card.get(ModDataComponents.UUID_COMPONENT));
                        atmMenu.newBalanceRender = cardPlayer.getData(BankManager.BALANCE);
                    }
                }

                if (slot >= 5 && slot <= 9 && bankTab.equals("withdraw")) {
                    int withdrawTotal = 0;
                    for (int i = 5; i <= 9; i++) {
                        ItemStack stack = getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            if (stack.is(ModItems.COIN256)) withdrawTotal += stack.getCount() * 256;
                            if (stack.is(ModItems.COIN64)) withdrawTotal += stack.getCount() * 64;
                            if (stack.is(ModItems.COIN16)) withdrawTotal += stack.getCount() * 16;
                            if (stack.is(ModItems.COIN4)) withdrawTotal += stack.getCount() * 4;
                            if (stack.is(ModItems.COIN1)) withdrawTotal += stack.getCount();
                        }
                    }
                    atmMenu.newBalanceRender = withdrawTotal;
                }
            }

            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    };


        public String bankTab = "pin";
    public boolean opened = false;
    private ServerPlayer player;
    public ATMMenu atmMenu;

    public void setTab(String newTab) {
        if (!level.isClientSide) {
            this.bankTab = newTab;

            ItemStack card = this.inventory.getStackInSlot(10);
            if (!card.isEmpty() && card.get(ModDataComponents.UUID_COMPONENT) != null) {
                Player cardPlayer = level.getPlayerByUUID(card.get(ModDataComponents.UUID_COMPONENT));
                depositPlayer = (ServerPlayer) cardPlayer;
                if (newTab.equals("withdraw")) {

                    handleWithdraw();
                } else {
                    refundWithdrawnCoins();
                }
            }
        }
    }

    public ATMBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ATM_BE.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Atm");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inv, @NotNull Player player) {
        this.player = (ServerPlayer) player;
        atmMenu = new ATMMenu(i, inv, this);
        return atmMenu;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        ATMBlockEntity be = (ATMBlockEntity) level.getBlockEntity(pos);
    }

    public void handleDeposit(int slot) {
        if (slot <= 4) {

            assert level != null;
            if (!level.isClientSide()) {
                if (inventory.getStackInSlot(slot).is(ModItems.COIN1)) {
                    addBalance(inventory.getStackInSlot(slot).getCount());
                }
                if (inventory.getStackInSlot(slot).is(ModItems.COIN4)) {
                    addBalance(inventory.getStackInSlot(slot).getCount() * 4);
                }
                if (inventory.getStackInSlot(slot).is(ModItems.COIN16)) {
                    addBalance(inventory.getStackInSlot(slot).getCount() * 16);
                }
                if (inventory.getStackInSlot(slot).is(ModItems.COIN64)) {
                    addBalance(inventory.getStackInSlot(slot).getCount() * 64);
                }
                if (inventory.getStackInSlot(slot).is(ModItems.COIN256)) {
                    addBalance(inventory.getStackInSlot(slot).getCount() * 256);
                }

                if (!inventory.getStackInSlot(slot).is(ItemStack.EMPTY.getItem())) {
                    inventory.setStackInSlot(slot, ItemStack.EMPTY);
                }
            }
        }
    }

    public void handleWithdraw() {
        if (depositPlayer == null) return;
        this.withdrew = true;

        int originalBalance = depositPlayer.getData(BankManager.BALANCE);
        int runningTotal = originalBalance;

        int coin256Amount = runningTotal / 256;
        inventory.setStackInSlot(9, ModItems.COIN256.toStack(coin256Amount));
        runningTotal -= coin256Amount * 256;

        int coin64Amount = runningTotal / 64;
        inventory.setStackInSlot(8, ModItems.COIN64.toStack(coin64Amount));
        runningTotal -= coin64Amount * 64;

        int coin16Amount = runningTotal / 16;
        inventory.setStackInSlot(7, ModItems.COIN16.toStack(coin16Amount));
        runningTotal -= coin16Amount * 16;

        int coin4Amount = runningTotal / 4;
        inventory.setStackInSlot(6, ModItems.COIN4.toStack(coin4Amount));
        runningTotal -= coin4Amount * 4;

        int coin1Amount = runningTotal;
        inventory.setStackInSlot(5, ModItems.COIN1.toStack(coin1Amount));

        int withdrawnTotal = originalBalance - runningTotal;
        addBalance(-withdrawnTotal);

        atmMenu.newBalanceRender = 0;
    }

    public void refundWithdrawnCoins() {
        if (depositPlayer == null) return;

        this.withdrew = false;

        int refundAmount = 0;

        for (int slot = 5; slot <= 9; slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                if (stack.is(ModItems.COIN256)) refundAmount += stack.getCount() * 256;
                if (stack.is(ModItems.COIN64)) refundAmount += stack.getCount() * 64;
                if (stack.is(ModItems.COIN16)) refundAmount += stack.getCount() * 16;
                if (stack.is(ModItems.COIN4)) refundAmount += stack.getCount() * 4;
                if (stack.is(ModItems.COIN1)) refundAmount += stack.getCount();

                inventory.setStackInSlot(slot, ItemStack.EMPTY);
            }
        }

        if (refundAmount > 0) {
            addBalance(refundAmount);
        }

        if (atmMenu != null) {
            atmMenu.newBalanceRender = depositPlayer.getData(BankManager.BALANCE);
        }

        PacketDistributor.sendToPlayer(depositPlayer,
                new SyncBalancePacket.MyData(depositPlayer.getData(BankManager.BALANCE), getBlockPos()));

        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }


    public void addBalance(int money) {
        if (depositPlayer != null) {
            depositPlayer.setData(BankManager.BALANCE, depositPlayer.getData(BankManager.BALANCE) + money);
            PacketDistributor.sendToPlayer(depositPlayer,new SyncBalancePacket.MyData(depositPlayer.getData(BankManager.BALANCE), getBlockPos()));
        }
    }
}
