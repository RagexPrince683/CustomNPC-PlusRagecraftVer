package kamkeel.network.packets.client;

import io.netty.buffer.ByteBuf;
import kamkeel.network.AbstractPacket;
import kamkeel.network.PacketChannel;
import kamkeel.network.PacketHandler;
import kamkeel.network.enums.EnumClientPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.Server;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.entity.EntityNPCInterface;

import java.io.IOException;

public final class DialogPacket extends AbstractPacket {
    public static final String packetName = "Client|Dialog";

    public DialogPacket() {}

    @Override
    public Enum getType() {
        return EnumClientPacket.DIALOG;
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
        Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(in.readInt());
        if (!(entity instanceof EntityNPCInterface)) {
            return;
        }
        NoppesUtil.openDialog(Server.readNBT(in), (EntityNPCInterface) entity, player);
    }
}
