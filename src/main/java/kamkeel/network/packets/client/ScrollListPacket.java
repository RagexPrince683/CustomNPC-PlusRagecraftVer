package kamkeel.network.packets.client;

import io.netty.buffer.ByteBuf;
import kamkeel.network.AbstractPacket;
import kamkeel.network.PacketChannel;
import kamkeel.network.PacketHandler;
import kamkeel.network.enums.EnumClientPacket;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.client.NoppesUtil;

import java.io.IOException;

public final class ScrollListPacket extends AbstractPacket {
    public static final String packetName = "Client|ScrollList";

    public ScrollListPacket() {}

    @Override
    public Enum getType() {
        return EnumClientPacket.SCROLL_LIST;
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
        NoppesUtil.setScrollList(in);
    }
}
