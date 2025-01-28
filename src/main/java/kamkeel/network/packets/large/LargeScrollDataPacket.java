package kamkeel.network.packets.large;

import io.netty.buffer.ByteBuf;
import kamkeel.network.LargeAbstractPacket;
import kamkeel.network.PacketChannel;
import kamkeel.network.PacketHandler;
import kamkeel.network.enums.EnumClientPacket;
import kamkeel.util.ByteBufUtils;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.client.NoppesUtil;

import java.io.IOException;
import java.util.Map;

public final class LargeScrollDataPacket extends LargeAbstractPacket {
    public static final String packetName = "Large|ScrollData";

    private Map<String, Integer> data;

    public LargeScrollDataPacket() {}

    public LargeScrollDataPacket(Map<String, Integer> data) {
        this.data = data;
    }

    @Override
    public Enum getType() {
        return EnumClientPacket.SCROLL_DATA;
    }

    @Override
    public PacketChannel getChannel() {
        return PacketHandler.LARGE_PACKET;
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        out.writeInt(data.size());
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            ByteBufUtils.writeString(out, entry.getKey());
            out.writeInt(entry.getValue());
        }
    }

    @Override
    public void handleData(ByteBuf data, EntityPlayer player) throws IOException {
        NoppesUtil.setScrollData(data);
    }
}
