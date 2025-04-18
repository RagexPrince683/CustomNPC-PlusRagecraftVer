package noppes.npcs;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.data.IScriptHandler;

import java.util.*;

public class NBTTags {

    public static int TAG_End = 0;
    public static int TAG_Byte = 1;
    public static int TAG_Short = 2;
    public static int TAG_Int = 3;
    public static int TAG_Long = 4;
    public static int TAG_Float = 5;
    public static int TAG_Double = 6;
    public static int TAG_Byte_Array = 7;
    public static int TAG_String = 8;
    public static int TAG_List = 9;
    public static int TAG_Compound = 10;
    public static int TAG_Int_Array = 11;

    public static HashMap<Integer, ItemStack> getItemStackList(
        NBTTagList tagList) {
        HashMap<Integer, ItemStack> list = new HashMap<Integer, ItemStack>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            try {
                list.put(nbttagcompound.getByte("Slot") & 0xff, NoppesUtilServer.readItem(nbttagcompound));
            } catch (ClassCastException e) {
                list.put(nbttagcompound.getInteger("Slot"), NoppesUtilServer.readItem(nbttagcompound));
            }
        }
        return list;
    }

    public static ItemStack[] getItemStackArray(
        NBTTagList tagList) {
        ItemStack[] list = new ItemStack[tagList.tagCount()];
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list[nbttagcompound.getByte("Slot") & 0xff] = NoppesUtilServer.readItem(nbttagcompound);
        }
        return list;
    }

    public static ArrayList<int[]> getIntegerArraySet(NBTTagList tagList) {
        ArrayList<int[]> set = new ArrayList<int[]>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound compound = tagList.getCompoundTagAt(i);
            set.add(compound.getIntArray("Array"));
        }
        return set;
    }

    public static HashMap<Integer, Boolean> getBooleanList(NBTTagList tagList) {
        HashMap<Integer, Boolean> list = new HashMap<Integer, Boolean>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getBoolean("Boolean"));
        }
        return list;
    }

    public static HashMap<Integer, Integer> getIntegerIntegerMap(
        NBTTagList tagList) {
        HashMap<Integer, Integer> list = new HashMap<Integer, Integer>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getInteger("Integer"));
        }
        return list;
    }

    public static HashMap<Integer, Float> getIntegerFloatMap(
        NBTTagList tagList) {
        HashMap<Integer, Float> list = new HashMap<Integer, Float>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getFloat("Float"));
        }
        return list;
    }

    public static HashMap<Integer, Double> getIntegerDoubleMap(
        NBTTagList tagList) {
        HashMap<Integer, Double> list = new HashMap<Integer, Double>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getDouble("Double"));
        }
        return list;
    }

    public static HashMap<Integer, Long> getIntegerLongMap(
        NBTTagList tagList) {
        HashMap<Integer, Long> list = new HashMap<Integer, Long>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getLong("Long"));
        }
        return list;
    }

    public static HashSet<String> getStringSet(NBTTagList tagList) {
        HashSet<String> list = new HashSet<>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.add(nbttagcompound.getString("String"));
        }
        return list;
    }

    public static HashMap<Integer, Byte> getIntegerByteMap(
        NBTTagList tagList) {
        HashMap<Integer, Byte> list = new HashMap<Integer, Byte>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getByte("Byte"));
        }
        return list;
    }

    public static NBTTagList nbtIntegerByteMap(Map<Integer, Byte> lines) {
        NBTTagList nbttaglist = new NBTTagList();
        if (lines == null)
            return nbttaglist;
        for (int slot : lines.keySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Slot", slot);
            nbttagcompound.setByte("Byte", lines.get(slot));
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtStringSet(HashSet<String> collection) {
        NBTTagList nbttaglist = new NBTTagList();
        if (collection == null)
            return nbttaglist;
        for (String slot : collection) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("String", slot);
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static HashSet<Integer> getIntegerSet(NBTTagList tagList) {
        HashSet<Integer> list = new HashSet<Integer>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.add(nbttagcompound.getInteger("Integer"));
        }
        return list;
    }

    public static NBTTagList nbtIntegerSet(HashSet<Integer> set) {
        NBTTagList nbttaglist = new NBTTagList();
        if (set == null)
            return nbttaglist;
        for (int slot : set) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Integer", slot);
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static HashMap<String, String> getStringStringMap(NBTTagList tagList) {
        HashMap<String, String> list = new HashMap<String, String>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.put(nbttagcompound.getString("Slot"), nbttagcompound.getString("Value"));
        }
        return list;
    }

    public static HashMap<Integer, String> getIntegerStringMap(NBTTagList tagList) {
        HashMap<Integer, String> list = new HashMap<Integer, String>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getString("Value"));
        }
        return list;
    }

    public static HashMap<String, Integer> getStringIntegerMap(NBTTagList tagList) {
        HashMap<String, Integer> list = new HashMap<String, Integer>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.put(nbttagcompound.getString("Slot"), nbttagcompound.getInteger("Value"));
        }
        return list;
    }

    public static HashMap<String, int[]> getStringIntegerArrayMap(NBTTagList tagList) {
        HashMap<String, int[]> list = new HashMap<String, int[]>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.put(nbttagcompound.getString("Slot"), nbttagcompound.getIntArray("Value"));
        }
        return list;
    }

    /**
     * @param tagList
     * @param arrayLength if an int array is not this length a new int array with this length is made
     * @return
     */
    public static HashMap<String, int[]> getStringIntegerArrayMap(NBTTagList tagList, int arrayLength) {
        HashMap<String, int[]> list = new HashMap<String, int[]>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            int[] a = nbttagcompound.getIntArray("Value");
            if (a.length != arrayLength) a = new int[arrayLength];
            list.put(nbttagcompound.getString("Slot"), a);
        }
        return list;
    }

    public static HashMap<String, Vector<String>> getVectorMap(NBTTagList tagList) {
        HashMap<String, Vector<String>> map = new HashMap<String, Vector<String>>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            Vector<String> values = new Vector<String>();
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            NBTTagList list = nbttagcompound.getTagList("Values", 10);
            for (int j = 0; j < list.tagCount(); j++) {
                NBTTagCompound value = list.getCompoundTagAt(j);
                values.add(value.getString("Value"));
            }

            map.put(nbttagcompound.getString("Key"), values);
        }
        return map;
    }


    public static List<String> getStringList(NBTTagList tagList) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            String line = nbttagcompound.getString("Line");
            list.add(line);
        }
        return list;
    }

    public static String[] getStringArray(NBTTagList tagList, int size) {
        String[] arr = new String[size];
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            String line = nbttagcompound.getString("Value");
            int slot = nbttagcompound.getInteger("Slot");
            arr[slot] = line;
        }
        return arr;
    }

    public static NBTTagList nbtIntegerArraySet(List<int[]> set) {
        NBTTagList nbttaglist = new NBTTagList();
        if (set == null)
            return nbttaglist;
        for (int[] arr : set) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setIntArray("Array", arr);
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtItemStackList(HashMap<Integer, ItemStack> inventory) {
        NBTTagList nbttaglist = new NBTTagList();
        if (inventory == null)
            return nbttaglist;
        for (int slot : inventory.keySet()) {
            ItemStack item = inventory.get(slot);
            if (item == null)
                continue;
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setByte("Slot", (byte) slot);

            NoppesUtilServer.writeItem(item, nbttagcompound);

            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtItemStackArray(ItemStack[] inventory) {
        NBTTagList nbttaglist = new NBTTagList();
        if (inventory == null)
            return nbttaglist;
        for (int slot = 0; slot < inventory.length; slot++) {
            ItemStack item = inventory[slot];
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setByte("Slot", (byte) slot);

            if (item != null)
                NoppesUtilServer.writeItem(item, nbttagcompound);

            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtBooleanList(HashMap<Integer, Boolean> updatedSlots) {
        NBTTagList nbttaglist = new NBTTagList();
        if (updatedSlots == null)
            return nbttaglist;
        HashMap<Integer, Boolean> inventory2 = updatedSlots;
        for (Integer slot : inventory2.keySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Slot", slot);
            nbttagcompound.setBoolean("Boolean", inventory2.get(slot));

            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtIntegerIntegerMap(Map<Integer, Integer> lines) {
        NBTTagList nbttaglist = new NBTTagList();
        if (lines == null)
            return nbttaglist;
        for (int slot : lines.keySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Slot", slot);
            nbttagcompound.setInteger("Integer", lines.get(slot));
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtIntegerFloatMap(Map<Integer, Float> lines) {
        NBTTagList nbttaglist = new NBTTagList();
        if (lines == null)
            return nbttaglist;
        for (int slot : lines.keySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Slot", slot);
            nbttagcompound.setDouble("Float", lines.get(slot));
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtIntegerDoubleMap(Map<Integer, Double> lines) {
        NBTTagList nbttaglist = new NBTTagList();
        if (lines == null)
            return nbttaglist;
        for (int slot : lines.keySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Slot", slot);
            nbttagcompound.setDouble("Double", lines.get(slot));
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtIntegerLongMap(Map<Integer, Long> lines) {
        NBTTagList nbttaglist = new NBTTagList();
        if (lines == null)
            return nbttaglist;
        for (int slot : lines.keySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Slot", slot);
            nbttagcompound.setLong("Long", lines.get(slot));
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtVectorMap(Map<String, Vector<String>> map) {
        NBTTagList list = new NBTTagList();
        if (map == null)
            return list;
        for (String key : map.keySet()) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("Key", key);
            NBTTagList values = new NBTTagList();
            for (String value : map.get(key)) {
                NBTTagCompound comp = new NBTTagCompound();
                comp.setString("Value", value);
                values.appendTag(comp);
            }
            compound.setTag("Values", values);
            list.appendTag(compound);
        }
        return list;
    }

    public static NBTTagList nbtStringStringMap(Map<String, String> map) {
        NBTTagList nbttaglist = new NBTTagList();
        if (map == null)
            return nbttaglist;
        for (String slot : map.keySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Slot", slot);
            nbttagcompound.setString("Value", map.get(slot));

            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtStringIntegerMap(Map<String, Integer> map) {
        NBTTagList nbttaglist = new NBTTagList();
        if (map == null)
            return nbttaglist;
        for (String slot : map.keySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Slot", slot);
            nbttagcompound.setInteger("Value", map.get(slot));

            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtStringIntegerArrayMap(Map<String, int[]> map) {
        NBTTagList nbttaglist = new NBTTagList();
        if (map == null)
            return nbttaglist;
        for (String slot : map.keySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Slot", slot);
            nbttagcompound.setIntArray("Value", map.get(slot));

            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTBase nbtIntegerStringMap(HashMap<Integer, String> map) {
        NBTTagList nbttaglist = new NBTTagList();
        if (map == null)
            return nbttaglist;
        for (int slot : map.keySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Slot", slot);
            nbttagcompound.setString("Value", map.get(slot));

            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtStringArray(String[] list) {
        NBTTagList nbttaglist = new NBTTagList();
        if (list == null)
            return nbttaglist;
        for (int i = 0; i < list.length; i++) {
            if (list[i] == null)
                continue;
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Value", list[i]);
            nbttagcompound.setInteger("Slot", i);
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtStringList(List<String> list) {
        NBTTagList nbttaglist = new NBTTagList();
        for (String s : list) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Line", s);
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
    }

    public static NBTTagList nbtDoubleList(double... par1ArrayOfDouble) {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble = par1ArrayOfDouble;
        int i = par1ArrayOfDouble.length;

        for (int j = 0; j < i; ++j) {
            double d1 = adouble[j];
            nbttaglist.appendTag(new NBTTagDouble(d1));
        }

        return nbttaglist;
    }

    public static NBTTagCompound NBTMerge(NBTTagCompound data, NBTTagCompound merge) {
        NBTTagCompound compound = (NBTTagCompound) data.copy();
        Set<String> names = merge.func_150296_c();
        for (String name : names) {
            NBTBase base = merge.getTag(name);
            if (base.getId() == 10)
                base = NBTMerge(compound.getCompoundTag(name), (NBTTagCompound) base);
            compound.setTag(name, base);
        }
        return compound;
    }

    public static TreeMap<Long, String> GetLongStringMap(NBTTagList tagList) {
        TreeMap list = new TreeMap();

        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            list.put(Long.valueOf(nbttagcompound.getLong("Long")), nbttagcompound.getString("String"));
        }

        return list;
    }

    public static NBTTagList NBTLongStringMap(Map<Long, String> map) {
        NBTTagList nbttaglist = new NBTTagList();
        if (map == null) {
            return nbttaglist;
        } else {
            Iterator var2 = map.keySet().iterator();

            while (var2.hasNext()) {
                long slot = ((Long) var2.next()).longValue();
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setLong("Long", slot);
                nbttagcompound.setString("String", (String) map.get(Long.valueOf(slot)));
                nbttaglist.appendTag(nbttagcompound);
            }

            return nbttaglist;
        }
    }

    public static List<ScriptContainer> GetScriptOld(NBTTagList list, IScriptHandler handler) {
        ArrayList scripts = new ArrayList();

        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound compoundd = list.getCompoundTagAt(i);
            ScriptContainer script = new ScriptContainer(handler);
            script.readFromNBT(compoundd);
            scripts.add(script);
        }

        return scripts;
    }

    public static List<ScriptContainer> GetScript(NBTTagCompound compound, IScriptHandler handler) {
        ArrayList<ScriptContainer> scripts = new ArrayList<>();

        for (int i = 0; i < compound.getInteger("TotalScripts"); ++i) {
            NBTTagCompound containerCompound = compound.getCompoundTag("Tab" + i);
            ScriptContainer script = new ScriptContainer(handler);
            script.readFromNBT(containerCompound);
            scripts.add(script);
        }

        return scripts;
    }

    public static NBTTagList NBTScript(List<ScriptContainer> scripts) {
        NBTTagList list = new NBTTagList();
        Iterator var2 = scripts.iterator();

        while (var2.hasNext()) {
            ScriptContainer script = (ScriptContainer) var2.next();
            NBTTagCompound compound = new NBTTagCompound();
            script.writeToNBT(compound);
            list.appendTag(compound);
        }

        return list;
    }

    public static int getIntAt(NBTTagList tagList, int index) {
        if (index >= 0 && index < tagList.tagCount()) {
            NBTBase nbtbase = tagList.getCompoundTagAt(index);

            if (nbtbase.getId() == 3) {
                return ((NBTTagInt) nbtbase).func_150287_d();
            }
        }

        return 0;
    }
}
