package net.regonold.geometrabanks.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.regonold.geometrabanks.GeometraBanks;
import net.regonold.geometrabanks.block.bank.BankManager;

public class BankButton extends Button {
    private static final ResourceLocation BUTTON_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "textures/gui/button/normal.png");
    private static final ResourceLocation BUTTON_HOVER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "textures/gui/button/hovering.png");
    private static final ResourceLocation CUSTOM_SOUND =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "button_click");

    public BankButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        boolean isHovered = isMouseOver(mouseX, mouseY);
        ResourceLocation texture = isHovered ? BUTTON_HOVER_TEXTURE : BUTTON_TEXTURE;

        guiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);

        Font mcFont = Minecraft.getInstance().font;
        String text = this.getMessage().getString(); // Get the button text
        int textWidth = mcFont.width(text); // Get text width

        int x = this.getX() + (this.width - textWidth) / 2;
        int y = this.getY() + (this.height - 8) / 2;
        int color2 = 0x8d8d8d; // White color
        guiGraphics.drawString(mcFont, text, x, y, color2, false);
    }

    @Override
    public void renderString(GuiGraphics guiGraphics, Font font, int color) {
        Font mcFont = Minecraft.getInstance().font;
        String text = this.getMessage().getString();
        int x = 95; // X position
        int y = 62; // Y position
        int color2 = 0x8d8d8d; // White color
        guiGraphics.drawString(mcFont, text + " C", x, y, color2, false);
    }

    @Override
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(CUSTOM_SOUND), 1.0F));
    }
}
