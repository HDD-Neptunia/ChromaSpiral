package com.ladya.chromaspiral.station;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;

public class ChromaDyeTableRenderer implements BlockEntityRenderer<ChromaDyeTableBlockEntity> {

    public ChromaDyeTableRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
    public void render(
            ChromaDyeTableBlockEntity be,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        int water = be.getWaterLevel();
        if (water <= 2) return;

        int color = be.getWaterColor();
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        poseStack.pushPose();

        // ─── Height ──────────────────────────────
        float minY = 8f / 16f;
        float maxY = 15f / 16f;

        // remap logical → visual
        float visualRatio = (water - 3) / 5f;
        visualRatio = Mth.clamp(visualRatio, 0f, 1f);
        float stepped = Math.round(visualRatio * 4f) / 4f;
        float level = Mth.lerp(stepped, minY, maxY);

        // wobble
        float time = be.getLevel().getGameTime() + partialTick;
        float wobble = (float) Math.sin(time * 0.08f) * (0.5f / 16f);
        level += wobble;


        float inset = 2f / 16f;

        VertexConsumer vc = buffer.getBuffer(RenderType.translucent());

		TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(new ResourceLocation("minecraft:block/water_still"));

        Matrix4f mat = poseStack.last().pose();

        // 🌊 UV scroll (slow, subtle)
        float scroll = (time * 0.002f) % 1f;
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        
        float vRange = sprite.getV1() - sprite.getV0();
        float vOffset = scroll * vRange;

        float v0 = sprite.getV0() + vOffset;
        float v1 = sprite.getV1() + vOffset;

        // ─── TOP SURFACE ─────────────────────────
        vc.vertex(mat, inset, level, inset)
                .color(r, g, b, 200)
                .uv(u0, v0)
                .uv2(packedLight)
                .normal(0f, 1f, 0f)
                .endVertex();

        vc.vertex(mat, inset, level, 1f - inset)
                .color(r, g, b, 200)
                .uv(u0, v1)
                .uv2(packedLight)
                .normal(0f, 1f, 0f)
                .endVertex();

        vc.vertex(mat, 1f - inset, level, 1f - inset)
                .color(r, g, b, 200)
                .uv(u1, v1)
                .uv2(packedLight)
                .normal(0f, 1f, 0f)
                .endVertex();

        vc.vertex(mat, 1f - inset, level, inset)
                .color(r, g, b, 200)
                .uv(u1, v0)
                .uv2(packedLight)
                .normal(0f, 1f, 0f)
                .endVertex();

        float bottom = minY;

        // ─── SIDE WALLS ──────────────────────────
        drawWall(vc, mat, sprite,
                inset, inset,
                1f - inset, inset,
                bottom, level,
                r, g, b,
                0f, -1f,
                packedLight);

        drawWall(vc, mat, sprite,
                1f - inset, 1f - inset,
                inset, 1f - inset,
                bottom, level,
                r, g, b,
                0f, 1f,
                packedLight);

        drawWall(vc, mat, sprite,
                inset, 1f - inset,
                inset, inset,
                bottom, level,
                r, g, b,
                -1f, 0f,
                packedLight);

        drawWall(vc, mat, sprite,
                1f - inset, inset,
                1f - inset, 1f - inset,
                bottom, level,
                r, g, b,
                1f, 0f,
                packedLight);

        poseStack.popPose();
    }

    // ─── WALL HELPER (with alpha gradient) ──────
    private void drawWall(
            VertexConsumer vc,
            Matrix4f mat,
            TextureAtlasSprite sprite,
            float x1, float z1,
            float x2, float z2,
            float bottom, float top,
            int r, int g, int b,
            float nx, float nz,
            int packedLight
    ) {
        // bottom darker / murkier
        vc.vertex(mat, x1, bottom, z1)
                .color(r, g, b, 120)
                .uv(sprite.getU0(), sprite.getV1())
                .uv2(packedLight)
                .normal(nx, 0f, nz)
                .endVertex();

        vc.vertex(mat, x2, bottom, z2)
                .color(r, g, b, 120)
                .uv(sprite.getU1(), sprite.getV1())
                .uv2(packedLight)
                .normal(nx, 0f, nz)
                .endVertex();

        // top clearer
        vc.vertex(mat, x2, top, z2)
                .color(r, g, b, 200)
                .uv(sprite.getU1(), sprite.getV0())
                .uv2(packedLight)
                .normal(nx, 0f, nz)
                .endVertex();

        vc.vertex(mat, x1, top, z1)
                .color(r, g, b, 200)
                .uv(sprite.getU0(), sprite.getV0())
                .uv2(packedLight)
                .normal(nx, 0f, nz)
                .endVertex();
    }
}
