package kamkeel.npcs.network.packets.request.jobs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import kamkeel.npcs.network.AbstractPacket;
import kamkeel.npcs.network.PacketChannel;
import kamkeel.npcs.network.PacketHandler;
import kamkeel.npcs.network.PacketUtil;
import kamkeel.npcs.network.enums.EnumItemPacketType;
import kamkeel.npcs.network.enums.EnumRequestPacket;
import kamkeel.npcs.util.ByteBufUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcsPermissions;

import java.io.IOException;
import java.util.Set;

public final class JobSavePacket extends AbstractPacket {
    public static String packetName = "Request|JobSave";

    public JobSavePacket() { }

    @Override
    public Enum getType() {
        return EnumRequestPacket.JobSave;
    }

    @Override
    public PacketChannel getChannel() {
        return PacketHandler.REQUEST_PACKET;
    }

    @Override
    public CustomNpcsPermissions.Permission getPermission() {
        return CustomNpcsPermissions.NPC_ADVANCED;
    }

    @Override
    public boolean needsNPC() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void sendData(ByteBuf out) throws IOException { }

    @Override
    public void receiveData(ByteBuf in, EntityPlayer player) throws IOException {
        if (!(player instanceof EntityPlayerMP)) return;
        if (!PacketUtil.verifyItemPacket(EnumItemPacketType.WAND, player)) return;
        NBTTagCompound original = npc.jobInterface.writeToNBT(new NBTTagCompound());
        NBTTagCompound compound = ByteBufUtils.readNBT(in);
        Set<String> names = compound.func_150296_c();
        for(String name : names)
            original.setTag(name, compound.getTag(name));

        npc.jobInterface.readFromNBT(original);
        npc.updateClient = true;
    }
}
