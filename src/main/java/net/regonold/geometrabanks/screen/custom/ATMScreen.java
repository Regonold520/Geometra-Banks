package net.regonold.geometrabanks.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.regonold.geometrabanks.GeometraBanks;
import net.regonold.geometrabanks.block.bank.BankManager;
import net.regonold.geometrabanks.block.bank.SyncBankTab;
import net.regonold.geometrabanks.block.bank.SyncPinPacket;
import net.regonold.geometrabanks.component.ModDataComponents;
import net.regonold.geometrabanks.gui.BankButton;
import net.regonold.geometrabanks.gui.CardButton;
import net.regonold.geometrabanks.item.custom.Geocard;

import java.util.ArrayList;
import java.util.List;

public class ATMScreen extends AbstractContainerScreen<ATMMenu> {
    private ATMMenu AtMenu;
    public String pinCode = "";

    // Persistent list of bank buttons
    private final List<BankButton> bankButtons = new ArrayList<>();

    private static final ResourceLocation DEPOSIT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "textures/gui/atm/atm_deposit.png");
    private static final ResourceLocation WITHDRAW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "textures/gui/atm/atm_withdraw.png");
    private static final ResourceLocation PIN_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "textures/gui/atm/atm_pin.png");

    public Player plr;

    public ATMScreen(ATMMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.AtMenu = menu;
        this.plr = menu.player;
        this.AtMenu.AtScreen = this;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
        switch (this.menu.blockEntity.bankTab) {
            case "deposit":
                renderDeposit(guiGraphics, pMouseX, pMouseY);
                break;
            case "withdraw":
                renderWithdraw(guiGraphics, pMouseX, pMouseY);
                break;
            case "pin":
                renderPin(guiGraphics, pMouseX, pMouseY);
                break;
            default:
                renderDeposit(guiGraphics, pMouseX, pMouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Font mcFont = Minecraft.getInstance().font;

        if (this.menu.newBalanceRender >= 0) {
            String text = String.valueOf(this.menu.newBalanceRender);

            switch (this.menu.blockEntity.bankTab) {
                case "deposit":
                    guiGraphics.drawString(mcFont, text + " C", 95, 62, 0x8d8d8d, false);
                    break;
                case "withdraw":
                    guiGraphics.drawString(mcFont, text + " C", 60, 30, 0x8d8d8d, false);
                    break;
                case "pin":
                    guiGraphics.drawString(mcFont, this.pinCode, 57, 28, 0x8d8d8d, false);
                    break;
                default:
                    break;
            }
        }
    }

    private void renderDeposit(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(DEPOSIT_TEXTURE, x, y, 0, 0, 192, 210);
        RenderSystem.setShaderTexture(0, DEPOSIT_TEXTURE);
    }

    private void renderPin(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(PIN_TEXTURE, x, y, 0, 0, 192, 210);
        RenderSystem.setShaderTexture(0, PIN_TEXTURE);
    }

    private void renderWithdraw(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(WITHDRAW_TEXTURE, x, y, 0, 0, 192, 210);
        RenderSystem.setShaderTexture(0, WITHDRAW_TEXTURE);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        super.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    private void addNum(String num) {
        if (this.pinCode.length() <= 3) {
            this.pinCode += num;
        }
    }

    @Override
    protected void init() {
        super.init();
        setupButtons();
    }

    public void setupButtons() {
        // Set the current tab to "pin" when reinitializing the screen
        this.menu.blockEntity.bankTab = "pin";

        // Remove any existing bank buttons from previous calls
        for (BankButton bB : bankButtons) {
            this.removeWidget(bB);
            System.out.println("Removed old bank button.");
        }
        bankButtons.clear();

        // Local list for CardButtons
        List<CardButton> cardButtons = new ArrayList<>();

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 - 37, this.height / 2, 16, 16,
                Component.literal(""), button -> addNum("1"))));

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 - 19, this.height / 2, 16, 16,
                Component.literal(""), button -> addNum("2"))));

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 - 1, this.height / 2, 16, 16,
                Component.literal(""), button -> addNum("3"))));

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 - 37, this.height / 2 - 18, 16, 16,
                Component.literal(""), button -> addNum("4"))));

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 - 19, this.height / 2 - 18, 16, 16,
                Component.literal(""), button -> addNum("5"))));

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 - 1, this.height / 2 - 18, 16, 16,
                Component.literal(""), button -> addNum("6"))));

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 - 37, this.height / 2 - 36, 16, 16,
                Component.literal(""), button -> addNum("7"))));

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 - 19, this.height / 2 - 36, 16, 16,
                Component.literal(""), button -> addNum("8"))));

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 - 1, this.height / 2 - 36, 16, 16,
                Component.literal(""), button -> addNum("9"))));

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 + 17, this.height / 2 - 36, 16, 16,
                Component.literal(""), button -> addNum("0"))));

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 + 17, this.height / 2 - 18, 16, 16,
                Component.literal(""), button -> {
            if (!this.pinCode.isEmpty()) {
                this.pinCode = this.pinCode.substring(0, this.pinCode.length() - 1);
            }
        })));

        cardButtons.add(this.addRenderableWidget(new CardButton(this.width / 2 + 17, this.height / 2, 16, 16,
                Component.literal(""), button -> {
            if (this.pinCode.length() == 4) {
                // Use a null-check on the PIN stack before checking its contents
                ItemStack pinStack = menu.blockEntity.inventory.getStackInSlot(10);
                if (pinStack != null && !pinStack.isEmpty() &&
                        Integer.parseInt(pinCode) == pinStack.get(ModDataComponents.PIN_COMPONENT)) {
                    System.out.println("Correct PIN, switching tab.");
                    menu.blockEntity.bankTab = "deposit";
                    menu.updateSlots();

                    // Add bank buttons and add them to our persistent list
                    bankButtons.add(this.addRenderableWidget(new BankButton(this.width / 2 + 88, this.height / 2 - 77, 52, 23,
                            Component.literal("Deposit"), button2 -> {
                        PacketDistributor.sendToServer(new SyncBankTab.MyData(AtMenu.blockEntity.getBlockPos(), "deposit"));
                        this.menu.clickMenuButton(plr, 0);
                    })));

                    bankButtons.add(this.addRenderableWidget(new BankButton(this.width / 2 + 88, this.height / 2 - 50, 52, 23,
                            Component.literal("Withdraw"), button2 -> {
                        PacketDistributor.sendToServer(new SyncBankTab.MyData(AtMenu.blockEntity.getBlockPos(), "withdraw"));
                        this.menu.clickMenuButton(plr, 1);
                    })));

                    // Remove the CardButtons after switching to bank buttons
                    for (CardButton card : cardButtons) {
                        this.removeWidget(card);
                    }
                }
            }
        })));
    }
}
