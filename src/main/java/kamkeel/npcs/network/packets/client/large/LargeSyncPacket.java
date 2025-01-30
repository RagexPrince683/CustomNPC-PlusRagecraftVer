package kamkeel.npcs.network.packets.client.large;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import kamkeel.npcs.controllers.SyncController;
import kamkeel.npcs.network.LargeAbstractPacket;
import kamkeel.npcs.network.PacketChannel;
import kamkeel.npcs.network.PacketHandler;
import kamkeel.npcs.network.enums.EnumClientPacket;
import kamkeel.npcs.network.enums.EnumSyncAction;
import kamkeel.npcs.network.enums.EnumSyncType;
import kamkeel.npcs.util.ByteBufUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcs;

import java.io.IOException;

/**
 * A large sync packet that sends chunked data to the client for a given SyncType
 * along with a SyncAction (RELOAD, UPDATE, REMOVE).
 */
public final class LargeSyncPacket extends LargeAbstractPacket {

    private EnumSyncType enumSyncType;
    private EnumSyncAction enumSyncAction;
    private NBTTagCompound syncData;
    private int operationID;

    public LargeSyncPacket() {}

    /**
     * Constructs a new LargeSyncPacket.
     */
    public LargeSyncPacket(EnumSyncType enumSyncType, EnumSyncAction enumSyncAction, int catId, NBTTagCompound syncData) {
        this.enumSyncType = enumSyncType;
        this.enumSyncAction = enumSyncAction;
        this.syncData = syncData;
        this.operationID = catId;
    }

    @Override
    public Enum getType() {
        return EnumClientPacket.SYNC;
    }

    @Override
    public PacketChannel getChannel() {
        return PacketHandler.LARGE_PACKET; // Or whichever channel you want.
    }

    @Override
    protected byte[] getData() throws IOException {
        ByteBuf buffer = Unpooled.buffer();
        // 1) Write SyncType
        buffer.writeInt(enumSyncType.ordinal());
        // 2) Write SyncAction
        buffer.writeInt(enumSyncAction.ordinal());
        // 3) Optional Category ID
        buffer.writeInt(operationID);
        // 4) Write the NBTTagCompound
        ByteBufUtils.writeBigNBT(buffer, syncData);

        // Copy the buffer’s readable bytes into a byte[]
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        return bytes;
    }

    @Override
    protected void handleCompleteData(ByteBuf data, EntityPlayer player) throws IOException {
        if(CustomNpcs.side() != Side.CLIENT)
            return;

        // Reconstruct everything from the ByteBuf
        int syncTypeOrdinal = data.readInt();
        int syncActionOrdinal = data.readInt();
        int categoryID = data.readInt();

        EnumSyncType type = EnumSyncType.values()[syncTypeOrdinal];
        EnumSyncAction action = EnumSyncAction.values()[syncActionOrdinal];
        NBTTagCompound tag = ByteBufUtils.readBigNBT(data);

        // Now do your client-side logic (similar to your old clientSync() or clientSyncUpdate() approach)
        handleSyncPacketClient(type, action, categoryID, tag);
    }

    private void handleSyncPacketClient(EnumSyncType enumSyncType, EnumSyncAction enumSyncAction, int id, NBTTagCompound data) {
        switch (enumSyncAction) {
            case RELOAD:
                SyncController.clientSync(enumSyncType, data);
                break;
            case UPDATE:
                SyncController.clientUpdate(enumSyncType, id, data);
                break;
            case REMOVE:
                SyncController.clientSyncRemove(enumSyncType, id);
                break;
        }
    }
}
