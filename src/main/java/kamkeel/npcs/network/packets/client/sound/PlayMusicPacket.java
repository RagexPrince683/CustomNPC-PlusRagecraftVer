package kamkeel.npcs.network.packets.client.sound;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import kamkeel.npcs.network.AbstractPacket;
import kamkeel.npcs.network.PacketChannel;
import kamkeel.npcs.network.PacketHandler;
import kamkeel.npcs.network.enums.EnumClientPacket;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.CustomNpcs;
import noppes.npcs.Server;
import noppes.npcs.client.controllers.MusicController;

import java.io.IOException;

public final class PlayMusicPacket extends AbstractPacket {
    public static final String packetName = "Client|PlayMusic";

    public PlayMusicPacket() {}

    @Override
    public Enum getType() {
        return EnumClientPacket.PLAY_MUSIC;
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
        if(CustomNpcs.side() != Side.CLIENT)
            return;

        String musicName = Server.readString(in);
        MusicController.Instance.playMusicBackground(musicName, player, Integer.MAX_VALUE);
    }
}
