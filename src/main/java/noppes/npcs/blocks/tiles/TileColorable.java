package noppes.npcs.blocks.tiles;

import net.minecraft.block.BlockColored;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.items.ItemNpcTool;

public class TileColorable extends TileVariant {

    public int color = 0xFFFFFF;

    public void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        color = compound.getInteger("BrushColor");
    }

    public void writeToNBT(NBTTagCompound compound){
    	super.writeToNBT(compound);
    	compound.setInteger("BrushColor", color);
    }

    public void setColor(int color){
        this.color = color;
        this.markDirty();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    public static ColorChangeType allowColorChange(ItemStack stack){
        if(stack == null || stack.getItem() == null)
            return ColorChangeType.NONE;

        if(stack.getItem() == Items.dye)
            return ColorChangeType.DYE;

        if(ItemNpcTool.isPaintbrush(stack))
            return ColorChangeType.PAINTBRUSH;

        return ColorChangeType.NONE;
    }

    public enum ColorChangeType {
        NONE,
        DYE,
        PAINTBRUSH
    }
}
