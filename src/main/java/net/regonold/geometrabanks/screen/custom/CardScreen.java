package net.regonold.geometrabanks.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.regonold.geometrabanks.GeometraBanks;
import net.regonold.geometrabanks.block.bank.SyncBankTab;
import net.regonold.geometrabanks.block.bank.SyncPinPacket;
import net.regonold.geometrabanks.component.ModDataComponents;
import net.regonold.geometrabanks.gui.BankButton;
import net.regonold.geometrabanks.gui.CardButton;
import net.regonold.geometrabanks.item.custom.Geocard;
import org.apache.commons.lang3.StringUtils;

public class CardScreen extends AbstractContainerScreen<CardMenu> {
    private static final ResourceLocation MENU_TEXTURE = ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "textures/gui/card/card_pin.png");

    public String pinCode = "";

    public CardScreen(CardMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        guiGraphics.blit(MENU_TEXTURE, this.leftPos, this.topPos, 0, 0, 176, 132);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Font mcFont = Minecraft.getInstance().font;
        guiGraphics.drawString(mcFont, this.pinCode, 57, 28, 0x8d8d8d, false);
    }

    private void addNum(String num) {
        if (this.pinCode.length() <= 3) {
            this.pinCode += num;
        }
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(new CardButton(this.width / 2 - 37, this.height / 2, 16, 16,
                Component.literal(""), button -> {
            addNum("1");
        }));

        this.addRenderableWidget(new CardButton(this.width / 2 - 19, this.height / 2, 16, 16,
                Component.literal(""), button -> {
            addNum("2");
        }));

        this.addRenderableWidget(new CardButton(this.width / 2 - 1, this.height / 2, 16, 16,
                Component.literal(""), button -> {
            addNum("3");
        }));

        this.addRenderableWidget(new CardButton(this.width / 2 - 37, this.height / 2 - 18, 16, 16,
                Component.literal(""), button -> {
            addNum("4");
        }));

        this.addRenderableWidget(new CardButton(this.width / 2 - 19, this.height / 2 - 18, 16, 16,
                Component.literal(""), button -> {
            addNum("5");
        }));

        this.addRenderableWidget(new CardButton(this.width / 2 - 1, this.height / 2 - 18, 16, 16,
                Component.literal(""), button -> {
            addNum("6");
        }));

        this.addRenderableWidget(new CardButton(this.width / 2 - 37, this.height / 2 - 36, 16, 16,
                Component.literal(""), button -> {
            addNum("7");
        }));

        this.addRenderableWidget(new CardButton(this.width / 2 - 19, this.height / 2 - 36, 16, 16,
                Component.literal(""), button -> {
            addNum("8");
        }));

        this.addRenderableWidget(new CardButton(this.width / 2 - 1, this.height / 2 - 36, 16, 16,
                Component.literal(""), button -> {
            addNum("9");
        }));

        this.addRenderableWidget(new CardButton(this.width / 2 + 17, this.height / 2 - 36, 16, 16,
                Component.literal(""), button -> {
            addNum("0");
        }));


        this.addRenderableWidget(new CardButton(this.width / 2 + 17, this.height / 2 - 18, 16, 16,
                Component.literal(""), button -> {
            if (!this.pinCode.isEmpty()) {
                this.pinCode = this.pinCode.substring(0, this.pinCode.length() - 1);
            }
        }));

        this.addRenderableWidget(new CardButton(this.width / 2 + 17, this.height / 2, 16, 16,
                Component.literal(""), button -> {
            if (this.pinCode.length() == 4) {
                if (menu.player.getMainHandItem().getItem() instanceof Geocard) {
                   menu.player.getMainHandItem().set(ModDataComponents.PIN_COMPONENT, Integer.parseInt(pinCode));
                   PacketDistributor.sendToServer(new SyncPinPacket.MyData(menu.player.getMainHandItem(), Integer.parseInt(pinCode)));
                   System.out.println(menu.player.getMainHandItem().get(ModDataComponents.PIN_COMPONENT));
                   menu.player.closeContainer();
                }
            }
        }));

    }
}
