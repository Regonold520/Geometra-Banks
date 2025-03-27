package net.regonold.geometrabanks.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.regonold.geometrabanks.GeometraBanks;

public class BankFloatingButton extends Button {
    private static final ResourceLocation BUTTON_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "textures/gui/button/screen-normal.png");
    private static final ResourceLocation BUTTON_HOVER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "textures/gui/button/screen-hovering.png");
    private static final ResourceLocation CUSTOM_SOUND =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "button_click");

    public Player plr;

    public BankFloatingButton(Player plr,int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.plr = plr;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        boolean isHovered = isMouseOver(mouseX, mouseY);
        ResourceLocation texture = isHovered ? BUTTON_HOVER_TEXTURE : BUTTON_TEXTURE;

        guiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);

        Font mcFont = Minecraft.getInstance().font;
        String text = this.getMessage().getString();
        int textWidth = mcFont.width(text);

        int x = this.getX() + (this.width - textWidth) / 2;
        int y = this.getY() + (this.height - 8) / 2;
        int color2 = 0x8d8d8d;
        guiGraphics.drawString(mcFont, text, x, y, color2, false);
    }

    @Override
    public void renderString(GuiGraphics guiGraphics, Font font, int color) {
        Font mcFont = Minecraft.getInstance().font;
        String text = this.getMessage().getString();
        int x = 95;
        int y = 62;
        int color2 = 0x8d8d8d;
        guiGraphics.drawString(mcFont, text + " C", x, y, color2, false);
    }

    @Override
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(CUSTOM_SOUND), 1.0F));
    }
}
