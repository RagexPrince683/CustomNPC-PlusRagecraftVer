package noppes.npcs.client.renderer.blocks;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.BlockCrate;
import noppes.npcs.blocks.tiles.TileVariant;
import noppes.npcs.client.model.blocks.ModelCrate;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class BlockCrateRenderer extends BlockRendererInterface {

    private final ModelCrate model = new ModelCrate();

    public BlockCrateRenderer() {
        ((BlockCrate) CustomItems.crate).renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(this);
    }

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4,
                                   double var6, float var8) {
        TileVariant tile = (TileVariant) var1;
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
        //GL11.glScalef(0.85f, 0.85f, 0.7f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(90 * tile.rotation, 0, 1, 0);
        GL11.glColor3f(1, 1, 1);

        setWoodTexture(var1.getBlockMetadata());
        model.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);

        GL11.glPopMatrix();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId,
                                     RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0.99f, 0);
        //GL11.glScalef(0.7f, 0.7f, 0.7f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(180, 0, 1, 0);

        setWoodTexture(metadata);
        GL11.glColor3f(1, 1, 1);
        model.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);

        GL11.glPopMatrix();
    }

    @Override
    public int getRenderId() {
        return CustomItems.crate.getRenderType();
    }
}
