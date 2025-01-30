package kamkeel.npcs.network.packets.client.npc;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import kamkeel.npcs.network.AbstractPacket;
import kamkeel.npcs.network.PacketChannel;
import kamkeel.npcs.network.PacketHandler;
import kamkeel.npcs.network.enums.EnumClientPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.CustomNpcs;
import noppes.npcs.entity.EntityNPCInterface;

import java.io.IOException;

public final class DeleteNpcPacket extends AbstractPacket {
    public static final String packetName = "Client|DeleteNpc";

    private int entityId;

    public DeleteNpcPacket() {}

    public DeleteNpcPacket(int id){
        this.entityId = id;
    }

    @Override
    public Enum getType() {
        return EnumClientPacket.DELETE_NPC;
    }

    @Override
    public PacketChannel getChannel() {
        return PacketHandler.CLIENT_PACKET;
    }

    @Override
    public void sendData(ByteBuf out) throws IOException {
        out.writeInt(this.entityId);
    }

    @Override
    public void receiveData(ByteBuf in, EntityPlayer player) throws IOException {
        if(CustomNpcs.side() != Side.CLIENT)
            return;

        Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(in.readInt());
        if (entity instanceof EntityNPCInterface) {
            ((EntityNPCInterface) entity).delete();
        }
    }
}
