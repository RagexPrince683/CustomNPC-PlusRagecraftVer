package noppes.npcs.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.constants.EnumGuiType;

import java.util.List;

public class BlockCarpentryBench extends BlockContainer {
    public int renderId = -1;

    public BlockCarpentryBench() {
        super(Material.rock);
        setStepSound(soundTypePiston);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (block != this) {
            return block.getLightValue(world, x, y, z);
        }
        if (meta >= 4)
            return 5;

        return getLightValue();
    }

    @Override
    public boolean onBlockActivated(World par1World, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (!par1World.isRemote) {
            int meta = par1World.getBlockMetadata(i, j, k);
            if (meta >= 4) {
                player.openGui(CustomNpcs.instance, EnumGuiType.PlayerAnvil.ordinal(), par1World, i, j, k);
            } else {
                player.openGui(CustomNpcs.instance, EnumGuiType.PlayerCarpentryBench.ordinal(), par1World, i, j, k);
            }
        }
        return true;
    }

    @Override
    public boolean isOpaqueCube()//isOpaqueCube
    {
        return false;
    }

    @Override
    public int getRenderType()//getRenderType
    {
        return renderId;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()//renderAsNormalBlock
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta >= 4) {
            return Blocks.anvil.getIcon(side, 0);
        }
        return Blocks.planks.getIcon(side, 0);
    }

    @Override
    public int damageDropped(int par1) {
        return par1 / 4;
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack item) {
        int var6 = MathHelper.floor_double((double) (par5EntityLiving.rotationYaw / 90.0F) + 0.5D) & 3;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, var6 + item.getItemDamage() * 4, 2);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileBlockAnvil();
    }
}
