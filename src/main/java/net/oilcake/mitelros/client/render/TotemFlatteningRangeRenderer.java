package net.oilcake.mitelros.client.render;

import net.minecraft.EntityClientPlayerMP;
import net.minecraft.ItemStack;
import net.minecraft.Minecraft;
import net.minecraft.RenderManager;
import net.minecraft.Tessellator;
import net.oilcake.mitelros.item.ItemTotemFlattening;
import net.oilcake.mitelros.registry.item.Items;
import org.lwjgl.opengl.GL11;

public final class TotemFlatteningRangeRenderer {

    public static void render() {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityClientPlayerMP player = minecraft.thePlayer;

        ItemStack heldItem = player.getHeldItemStack();
        if (heldItem == null || heldItem.getItem() != Items.totemOfFlattening || !((ItemTotemFlattening) Items.totemOfFlattening).canTrigger(minecraft.theWorld, player)) {
            return;
        }

        int range = ItemTotemFlattening.getRange();
        int centerX = player.getBlockPosX();
        int centerY = player.getBlockPosY();
        int centerZ = player.getBlockPosZ();

        double minX = centerX - range - RenderManager.renderPosX - 0.002D;
        double maxX = centerX + range + 1.0D - RenderManager.renderPosX + 0.002D;
        double minY = centerY - 5.0D - RenderManager.renderPosY - 0.002D;
        double maxY = centerY + 5.0D - RenderManager.renderPosY + 0.002D;
        double minZ = centerZ - range - RenderManager.renderPosZ - 0.002D;
        double maxZ = centerZ + range + 1.0D - RenderManager.renderPosZ + 0.002D;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_LINE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        try {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(false);
            GL11.glPushMatrix();
            try {
                Tessellator tessellator = Tessellator.instance;
                GL11.glLineWidth(2.0F);
                drawFace(tessellator, minX, minY, minZ, maxX, maxY, minZ);
                drawFace(tessellator, maxX, minY, maxZ, minX, maxY, maxZ);
                drawFace(tessellator, maxX, minY, minZ, maxX, maxY, maxZ);
                drawFace(tessellator, minX, minY, maxZ, minX, maxY, minZ);
            } finally {
                GL11.glPopMatrix();
            }
        } finally {
            GL11.glPopAttrib();
        }
    }

    private static void drawFace(Tessellator tessellator, double x1, double y1, double z1, double x2, double y2, double z2) {
        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        tessellator.setColorRGBA(255, 80, 40, 255);
        tessellator.addVertex(x1, y2, z1);
        tessellator.addVertex(x1, y1, z1);
        tessellator.addVertex(x2, y1, z2);
        tessellator.addVertex(x2, y2, z2);
        tessellator.draw();
    }
}
