package kamkeel.network.packets.client;

import io.netty.buffer.ByteBuf;
import kamkeel.network.AbstractPacket;
import kamkeel.network.PacketChannel;
import kamkeel.network.PacketHandler;
import kamkeel.network.enums.EnumClientPacket;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.EnumGuiType;

import java.io.IOException;

public final class ClonerPacket extends AbstractPacket {
    public static final String packetName = "Client|Cloner";

    public ClonerPacket() {}

    @Override
    public Enum getType() {
        return EnumClientPacket.CLONER;
    }

    @Override
    public PacketChannel getChannel() {
        return PacketHandler.CLIENT_PACKET;
    }

    @Override
    public void sendData(ByteBuf out) throws IOException {
        // TODO: Send Packet
    }

    @Override
    public void receiveData(ByteBuf in, EntityPlayer player) throws IOException {
        int x = in.readInt();
        int y = in.readInt();
        int z = in.readInt();
        CustomNpcs.proxy.openGui(x, y, z, EnumGuiType.MobSpawner, player);
    }
}
