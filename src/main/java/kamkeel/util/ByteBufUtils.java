package kamkeel.util;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.village.MerchantRecipeList;
import noppes.npcs.LogWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ByteBufUtils extends cpw.mods.fml.common.network.ByteBufUtils {

    // TODO: Modify this to check if it a large packet so we don't need the condition at the bottom or make two
    // differnt methods one for fillBuffer normal and one fillLargeBuffer
    public static boolean fillBuffer(ByteBuf buffer, Enum enu, Object... obs) throws IOException{
        buffer.writeInt(enu.ordinal());
        for(Object ob : obs){
            if(ob == null)
                continue;
            if(ob instanceof Map){
                Map<String,Integer> map = (Map<String, Integer>) ob;

                buffer.writeInt(map.size());
                for(String key : map.keySet()){
                    int value = map.get(key);
                    buffer.writeInt(value);
                    writeString(buffer, key);
                }
            }
            else if(ob instanceof MerchantRecipeList)
                ((MerchantRecipeList)ob).func_151391_a(new PacketBuffer(buffer));
            else if(ob instanceof List){
                List<String> list = (List<String>) ob;
                buffer.writeInt(list.size());
                for(String s : list)
                    writeString(buffer, s);
            }
            else if(ob instanceof Enum)
                buffer.writeInt(((Enum<?>) ob).ordinal());
            else if(ob instanceof Integer)
                buffer.writeInt((Integer) ob);
            else if(ob instanceof Boolean)
                buffer.writeBoolean((Boolean) ob);
            else if(ob instanceof String)
                writeString(buffer, (String) ob);
            else if(ob instanceof Float)
                buffer.writeFloat((Float) ob);
            else if(ob instanceof Long)
                buffer.writeLong((Long) ob);
            else if(ob instanceof Double)
                buffer.writeDouble((Double) ob);
            else if (ob instanceof byte[]) {
                // Write byte array length followed by bytes
                byte[] byteArray = (byte[]) ob;
                buffer.writeShort((short) byteArray.length);
                buffer.writeBytes(byteArray);
            }
            else if(ob instanceof NBTTagCompound)
                writeNBT(buffer, (NBTTagCompound) ob);
        }
        if(buffer.array().length >= 32767){
            LogWriter.error("Packet " + enu + " was too big to be send");
            LogWriter.script( "Issue occurred with the following Packet - " + enu + ":\n" + Arrays.toString(obs));
            return false;
        }
        return true;
    }

    public static void writeIntArray(ByteBuf buffer, int[] arr) {
        buffer.writeInt(arr.length);
        for (int i : arr) {
            buffer.writeInt(i);
        }
    }

    public static int[] readIntArray(ByteBuf buffer) {
        int length = buffer.readInt();
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            arr[i] = buffer.readInt();
        }
        return arr;
    }

    public static void writeNBT(ByteBuf buffer, NBTTagCompound compound) throws IOException {
        byte[] bytes = CompressedStreamTools.compress(compound);
        buffer.writeShort((short)bytes.length);
        buffer.writeBytes(bytes);
    }

    public static NBTTagCompound readNBT(ByteBuf buffer) throws IOException {
        byte[] bytes = new byte[buffer.readShort()];
        buffer.readBytes(bytes);
        return CompressedStreamTools.func_152457_a(bytes, new NBTSizeTracker(2097152L));
    }

    public static void writeString(ByteBuf buffer, String s){
        byte[] bytes = s.getBytes(Charsets.UTF_8);
        buffer.writeShort((short)bytes.length);
        buffer.writeBytes(bytes);
    }

    public static String readString(ByteBuf buffer){
        try{
            byte[] bytes = new byte[buffer.readShort()];
            buffer.readBytes(bytes);
            return new String(bytes, Charsets.UTF_8);
        }
        catch(IndexOutOfBoundsException ex){
            return null;
        }
    }
}
