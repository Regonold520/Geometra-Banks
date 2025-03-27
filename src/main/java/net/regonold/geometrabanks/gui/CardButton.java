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
import net.minecraft.world.entity.player.Player;
import net.regonold.geometrabanks.GeometraBanks;

public class CardButton extends Button {
    private static final ResourceLocation BUTTON_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "textures/gui/card/pin_button.png");
    private static final ResourceLocation CUSTOM_SOUND =
            ResourceLocation.fromNamespaceAndPath(GeometraBanks.MODID, "button_click");

    public Player plr;

    public CardButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        boolean isHovered = isMouseOver(mouseX, mouseY);
        if (isHovered) {
            RenderSystem.enableBlend(); // Enable blending for transparency
            RenderSystem.defaultBlendFunc(); // Set proper blending function
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.6F); // Set transparency

            ResourceLocation texture = BUTTON_TEXTURE;

            guiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);

            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void renderString(GuiGraphics guiGraphics, Font font, int color) {

    }

    @Override
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(CUSTOM_SOUND), 1.0F));
    }
}
