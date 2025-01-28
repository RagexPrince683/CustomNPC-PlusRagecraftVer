package kamkeel.network.packets.client;

import io.netty.buffer.ByteBuf;
import kamkeel.network.AbstractPacket;
import kamkeel.network.PacketChannel;
import kamkeel.network.PacketHandler;
import kamkeel.network.enums.EnumClientPacket;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.Server;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.config.ConfigClient;
import noppes.npcs.client.ClientProxy.FontContainer;
import net.minecraft.util.ChatComponentTranslation;

import java.io.IOException;

public final class ConfigPacket extends AbstractPacket {
    public static final String packetName = "Client|Config";

    public ConfigPacket() {}

    @Override
    public Enum getType() {
        return EnumClientPacket.CONFIG;
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
        int configType = in.readInt();
        if (configType == 0) { // Font Configuration
            String font = Server.readString(in);
            int size = in.readInt();
            if (!font.isEmpty()) {
                ConfigClient.FontType = font;
                ConfigClient.FontSize = size;
                ClientProxy.Font = new FontContainer(ConfigClient.FontType, ConfigClient.FontSize);

                ConfigClient.FontTypeProperty.set(ConfigClient.FontType);
                ConfigClient.FontSizeProperty.set(ConfigClient.FontSize);

                if (ConfigClient.config.hasChanged()) {
                    ConfigClient.config.save();
                }

                player.addChatMessage(new ChatComponentTranslation("Font set to %s", ClientProxy.Font.getName()));
            } else {
                player.addChatMessage(new ChatComponentTranslation("Current font is %s", ClientProxy.Font.getName()));
            }
        }
    }
}
