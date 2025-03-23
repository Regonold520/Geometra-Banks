package net.regonold.geometrabanks.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.regonold.geometrabanks.GeometraBanks;
import net.regonold.geometrabanks.block.bank.BankManager;
import net.regonold.geometrabanks.block.entity.ATMBlockEntity;
import net.regonold.geometrabanks.gui.BankButton;
import net.regonold.geometrabanks.gui.BankFloatingButton;


public class ATMScreen extends AbstractContainerScreen<ATMMenu> {
    private ATMMenu AtMenu;

    private static final ResourceLocation DEPOSIT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "textures/gui/atm/atm_deposit.png");

    private static final ResourceLocation WITHDRAW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "textures/gui/atm/atm_withdraw.png");

    public Player plr;


    public ATMScreen(ATMMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.AtMenu = menu;
        this.plr = menu.player;

    }



    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);




        switch (this.menu.blockEntity.bankTab){
            case "deposit":
                renderDeposit(guiGraphics, pMouseX, pMouseY);
                break;
            case "withdraw":
                renderWithdraw(guiGraphics, pMouseX, pMouseY);
                break;
            default:
                renderDeposit(guiGraphics, pMouseX, pMouseY);
        }

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Font mcFont = Minecraft.getInstance().font;

        String titleText = plr.getScoreboardName() + "'s Account";
        if (this.menu.newBalanceRender >= 0) {
            String text = String.valueOf(this.menu.newBalanceRender);


            switch (this.menu.blockEntity.bankTab) {
                case "deposit":
                    guiGraphics.drawString(mcFont, text + " C", 95, 62, 0x8d8d8d, false);
                    break;
                case "withdraw":
                    guiGraphics.drawString(mcFont, text + " C", 60, 30, 0x8d8d8d, false);
                    break;
                default:
                    guiGraphics.drawString(mcFont, text + " C", 95, 62, 0x8d8d8d, false);
            }
        } else {
            System.out.println(this.menu.blockEntity.depositPlayer);
        }
    }

    private void renderDeposit(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Font mcFont = Minecraft.getInstance().font;

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(DEPOSIT_TEXTURE, x, y, 0, 0, 176, 210);
        RenderSystem.setShaderTexture(0, DEPOSIT_TEXTURE);

        x = 95;
        y = 62;
        int color = 0x8d8d8d;

    }


    private void renderWithdraw(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(WITHDRAW_TEXTURE, x, y, 0, 0, 176, 210);
        RenderSystem.setShaderTexture(0, WITHDRAW_TEXTURE);
    }


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        super.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }


    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(new BankButton(this.width / 2 + 88 , this.height / 2 - 77, 52, 23,
                Component.literal("Deposit"), button -> {
            this.menu.blockEntity.bankTab = "deposit";

            menu.updateSlots();
        }));
        this.addRenderableWidget(new BankButton(this.width / 2 + 88 , this.height / 2 - 50, 52, 23,
                Component.literal("Withdraw"), button -> {
            this.menu.blockEntity.bankTab = "withdraw";
            System.out.println("WIEBGWUIBGE" + menu.blockEntity.getLevel().isClientSide());

            menu.updateSlots();
        }));


        /*
        this.addRenderableWidget(new ScrollPanel(Minecraft.getInstance(), 150, 200, 1, 100, 0, 10) {
            @Override
            public void updateNarration(NarrationElementOutput narrationElementOutput) {
                // ...
            }

            @Override
            public NarrationPriority narrationPriority() {
                return NarrationPriority.HOVERED;
            }

            @Override
            protected int getContentHeight() {
                return 250;
            }

            @Override
            protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
                // Use the previously fetched skin for rendering the player's face.
                List<? extends AbstractClientPlayer> players = new ArrayList<>();
                Font mcFont = Minecraft.getInstance().font;

                if (Minecraft.getInstance().level != null) {
                    players = Minecraft.getInstance().level.players();
                }

                int idx = 1;
                for (AbstractClientPlayer p : players) {
                    PlayerFaceRenderer.draw(guiGraphics, p.getSkin(), 100,   100 + relativeY - (idx * 27), 25);
                    guiGraphics.drawString(mcFont, p.getScoreboardName(), 130, 107 + relativeY - (idx * 27), 0xFFFFFF, false);
                    idx++;
                }

            }
        });

         */
    }
}