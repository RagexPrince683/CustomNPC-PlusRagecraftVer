package kamkeel.network.packets.client;

import io.netty.buffer.ByteBuf;
import kamkeel.network.AbstractPacket;
import kamkeel.network.PacketChannel;
import kamkeel.network.PacketHandler;
import kamkeel.network.enums.EnumClientPacket;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.Server;
import noppes.npcs.client.NoppesUtil;

import java.io.IOException;

public final class QuestCompletionPacket extends AbstractPacket {
    public static final String packetName = "Client|QuestCompletion";

    public QuestCompletionPacket() {}

    @Override
    public Enum getType() {
        return EnumClientPacket.QUEST_COMPLETION;
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
        NoppesUtil.guiQuestCompletion(player, Server.readNBT(in));
    }
}
