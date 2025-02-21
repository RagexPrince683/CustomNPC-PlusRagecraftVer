package noppes.npcs.client.renderer.items;

import kamkeel.npcs.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;
import noppes.npcs.client.renderer.blocks.BlockTallLampRenderer;
import noppes.npcs.config.ConfigClient;
import org.lwjgl.opengl.GL11;

public class ItemTallLampRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        int meta = item.getItemDamage();
        int colorValue = ColorUtil.colorTableInts[15 - meta];
        if (item.hasTagCompound() && item.getTagCompound().hasKey("BrushColor")) {
            colorValue = item.getTagCompound().getInteger("BrushColor");
        }
        float[] color = ColorUtil.hexToRGB(colorValue);

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0.42f, 0);
        GL11.glScalef(0.76f, 0.66f, 0.76f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(180, 0, 1, 0);

        TextureManager manager = Minecraft.getMinecraft().getTextureManager();
        if(ConfigClient.LegacyTallLamp){
            BlockRendererInterface.setMaterialTexture(meta);
            GL11.glColor3f(1, 1, 1);
            BlockTallLampRenderer.modelLegacy.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);

            manager.bindTexture(BlockTallLampRenderer.resourceTop);
            GL11.glColor3f(color[0], color[1], color[2]);
            BlockTallLampRenderer.topLegacy.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);
        } else {
            BlockTallLampRenderer.setLampTexture(meta);
            BlockTallLampRenderer.model.Lamp.render(0.0625F);
            BlockTallLampRenderer.model.Light.render(0.0625F);
            GL11.glColor3f(color[0], color[1], color[2]);
            BlockTallLampRenderer. model.Shade.render(0.0625F);
        }

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPopAttrib();

        GL11.glPopMatrix();
    }
}
