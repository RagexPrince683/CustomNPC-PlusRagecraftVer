package noppes.npcs.client.renderer.blocks;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.BlockChair;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelChair;
import noppes.npcs.client.model.blocks.legacy.ModelLegacyChair;
import noppes.npcs.config.ConfigClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class BlockChairRenderer extends BlockRendererInterface{

    private final ModelLegacyChair legacyModel = new ModelLegacyChair();
	private final ModelChair model = new ModelChair();

	public BlockChairRenderer(){
		((BlockChair)CustomItems.chair).renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(this);
	}
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {
		TileColorable tile = (TileColorable) var1;
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var2 + 0.5f, (float)var4 + 1.68f, (float)var6 + 0.5f);
        GL11.glScalef(1.2f, 1.1f, 1.2f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(90 * tile.rotation, 0, 1, 0);
        GL11.glColor3f(1, 1, 1);

        setWoodTexture(var1.getBlockMetadata());
        if(ConfigClient.LegacyChair)
            legacyModel.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);
        else
            model.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);

		GL11.glPopMatrix();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 1f, 0);
        GL11.glScalef(1.2f, 1.1f, 1.2f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(180, 0, 1, 0);

        setWoodTexture(metadata);
        GL11.glColor3f(1, 1, 1);
        if(ConfigClient.LegacyChair)
            legacyModel.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);
        else
            model.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}



	@Override
	public int getRenderId() {
		return CustomItems.chair.getRenderType();
	}
}
