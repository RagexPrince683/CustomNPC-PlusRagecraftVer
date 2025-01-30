package kamkeel.npcs.network.packets.client.sync;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import kamkeel.npcs.network.AbstractPacket;
import kamkeel.npcs.network.PacketChannel;
import kamkeel.npcs.network.PacketHandler;
import kamkeel.npcs.network.enums.EnumClientPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.Server;

import java.io.IOException;

public final class SyncConfigPacket extends AbstractPacket {
    public static final String packetName = "Client|SyncConfig";

    public SyncConfigPacket() {}

    @Override
    public Enum getType() {
        return EnumClientPacket.SYNC_CONFIG;
    }

    @Override
    public PacketChannel getChannel() {
        return PacketHandler.CLIENT_PACKET;
    }

    @Override
    public void sendData(ByteBuf out) throws IOException {
        // TODO: Send Packet
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void receiveData(ByteBuf in, EntityPlayer player) throws IOException {
        NBTTagCompound configNBT = Server.readNBT(in);
        SyncController.receiveConfigs(configNBT);
    }
}
