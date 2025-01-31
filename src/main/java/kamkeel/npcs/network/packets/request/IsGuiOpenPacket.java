package kamkeel.npcs.network.packets.request;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import kamkeel.npcs.network.AbstractPacket;
import kamkeel.npcs.network.PacketChannel;
import kamkeel.npcs.network.PacketHandler;
import kamkeel.npcs.network.enums.EnumRequestPacket;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.PlayerData;

import java.io.IOException;

public final class IsGuiOpenPacket extends AbstractPacket {
    public static final String packetName = "Request|IsGuiOpen";

    private boolean isOpen;

    public IsGuiOpenPacket() {}

    public IsGuiOpenPacket(boolean isOpen){
        this.isOpen = isOpen;
    }

    @Override
    public Enum getType() {
        return EnumRequestPacket.IsGuiOpen;
    }

    @Override
    public PacketChannel getChannel() {
        return PacketHandler.REQUEST_PACKET;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void sendData(ByteBuf out) throws IOException {
        out.writeBoolean(this.isOpen);
    }

    @Override
    public void receiveData(ByteBuf in, EntityPlayer player) throws IOException {
        PlayerData playerdata = PlayerDataController.Instance.getPlayerData(player);
        boolean isGUIOpen = in.readBoolean();
        playerdata.setGUIOpen(isGUIOpen);
    }
}
