package com.ladya.chromaspiral.station;



import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ChromaDyeTableScreen extends AbstractContainerScreen<ChromaDyeTableMenu> {

    @SuppressWarnings("removal")
	private static final ResourceLocation TEXTURE =
            new ResourceLocation("chromaspiral", "textures/gui/chroma_dye_table.png");

    public ChromaDyeTableScreen(ChromaDyeTableMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    private void drawWaterBar(GuiGraphics guiGraphics) {
    	int color = menu.getWaterColor();
    	int argb = (0xAA << 24) | color;

    	
        int water = menu.getWaterLevel();
        int max = ChromaDyeTableBlockEntity.MAX_WATER;

        if (water <= 0) return;

        int barHeight = 48;
        int filled = (int)((water / (float) max) * barHeight);

        // 🔥 THESE ARE THE IMPORTANT NUMBERS
        int barX = leftPos + 8;     // LEFT column (matches your mock)
        int barY = topPos + 18;    // TOP of bar

        int textureU = 176;        // RIGHT side of texture
        int textureV = 0 + (barHeight - filled);

        guiGraphics.blit(
            TEXTURE,
            barX,
            barY + (barHeight - filled),
            textureU,
            textureV,
            12,
            argb
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

        // 1️⃣ Background
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        guiGraphics.blit(
            TEXTURE,
            leftPos,
            topPos,
            0,
            0,
            imageWidth,
            imageHeight,
            256,
            256
        );
        
        int rgb = menu.getWaterColor(); // 0xRRGGBB
        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;

        // Enable tinting
        RenderSystem.setShaderColor(r, g, b, 1.0f);

        drawWaterBar(guiGraphics);

        int water = menu.getWaterLevel();
        int max = ChromaDyeTableBlockEntity.MAX_WATER;

        int barTop = topPos + 18;
        int barBottom = topPos + 66;
        int barLeft  = leftPos + 8;

        int barHeight = barBottom - barTop;
        int barWidth = 16;

        // scale water → pixels
        int filled = (int)((water / (float)max) * barHeight);

        int texU = 229;
        int texV = (barHeight - filled);

        // Draw ONLY the bottom slice of the texture
        guiGraphics.blit(
            TEXTURE,
            barLeft,
            barBottom - filled, // screen Y (fill upward)
            texU,
            texV,               // texture Y (clip from bottom)
            barWidth,
            filled
        );
        
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

    }



    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 8, 6, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, 72, 0x404040, false);
    }
} 

