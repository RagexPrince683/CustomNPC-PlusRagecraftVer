package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.EventHooks;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.config.ConfigScript;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.event.ItemEvent;
import noppes.npcs.scripted.item.ScriptCustomItem;
import org.lwjgl.opengl.GL11;

public class ItemScripted extends ItemCustomizable {

    public ItemScripted() {
        maxStackSize = 1;
        setCreativeTab(CustomItems.tab);
        CustomNpcs.proxy.registerItem(this);
        setHasSubtypes(true);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if(player.isSneaking() && player.capabilities.isCreativeMode) {
            if(!ConfigScript.canScript(player, CustomNpcsPermissions.TOOL_SCRIPTED_ITEM)){
                player.addChatMessage(new ChatComponentTranslation("availability.permission"));
            } else {
                CustomNpcs.proxy.openGui(0, 0, 0, EnumGuiType.ScriptItem, player);
            }
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLivingBase, ItemStack stack){
        if (entityLivingBase.worldObj.isRemote) {
            return false;
        }

        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        ItemEvent.AttackEvent eve = new ItemEvent.AttackEvent( (ScriptCustomItem) istack, NpcAPI.Instance().getIEntity(entityLivingBase), 2, null);
        return EventHooks.onScriptItemAttack((ScriptCustomItem) istack, eve);
    }

    public int getMaxItemUseDuration(ItemStack stack){
        return (new ScriptCustomItem(stack)).getMaxItemUseDuration();
    }

    public static ScriptCustomItem GetWrapper(ItemStack stack) {
        return new ScriptCustomItem(stack);
    }

    public boolean showDurabilityBar(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem && (new ScriptCustomItem(stack)).itemDisplay.durabilityShow;
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem ? 1.0D - (new ScriptCustomItem(stack)).durabilityValue : 1.0D;
    }

    public int getItemStackLimit(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem ? (new ScriptCustomItem(stack)).getMaxStackSize() : super.getItemStackLimit(stack);
    }

    public boolean isItemTool(ItemStack stack)
    {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem ? (new ScriptCustomItem(stack)).isTool() : super.isItemTool(stack);
    }

    public float getDigSpeed(ItemStack stack, Block block, int metadata)
    {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem ? (new ScriptCustomItem(stack)).getDigSpeed() : super.getDigSpeed(stack, block, metadata);
    }

    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity)
    {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);

        if((new ScriptCustomItem(stack)).getArmorType() == -1)
            return true;

        return istack instanceof ScriptCustomItem ? armorType == (new ScriptCustomItem(stack)).getArmorType() : super.isValidArmor(stack, armorType, entity);
    }

    public int getItemEnchantability(ItemStack stack)
    {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem ? (new ScriptCustomItem(stack)).getEnchantability() : super.getItemEnchantability(stack);
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        ItemEvent.AttackEvent eve = new ItemEvent.AttackEvent( (ScriptCustomItem) istack, NpcAPI.Instance().getIEntity(attacker), 1, NpcAPI.Instance().getIEntity(target));
        return EventHooks.onScriptItemAttack((ScriptCustomItem) istack, eve);
    }

    public void renderOffset(ScriptCustomItem scriptCustomItem) {
        GL11.glTranslatef(0.135F * scriptCustomItem.itemDisplay.scaleX, 0.2F * scriptCustomItem.itemDisplay.scaleY, 0.07F * scriptCustomItem.itemDisplay.scaleZ);
    }
}
