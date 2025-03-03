package noppes.npcs.controllers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.controllers.data.Magic;
import noppes.npcs.controllers.data.MagicCycle;
import noppes.npcs.controllers.data.MagicAssociation;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

public class MagicController {
    public HashMap<Integer, Magic> magics = new HashMap<>();
    public HashMap<Integer, Magic> magicSync = new HashMap<>();

    public HashMap<Integer, MagicCycle> cycles = new HashMap<>();
    public HashMap<Integer, MagicCycle> cyclesSync = new HashMap<>();

    public int lastUsedCycleID = 0;
    private int lastUsedID = 0;

    private static MagicController instance;

    public MagicController() {
        instance = this;
    }

    public static MagicController getInstance() {
        return instance;
    }

    public Magic getMagic(int magicId) {
        return magics.get(magicId);
    }

    public MagicCycle getCycle(int cycleID) {
        return cycles.get(cycleID);
    }

    public void load() {
        File saveDir = CustomNpcs.getWorldSaveDirectory();
        if (saveDir == null) return;
        try {
            File file = new File(saveDir, "magic.dat");
            if (file.exists()) {
                loadMagicFile(file);
            }
        } catch (Exception e) {
            try {
                File file = new File(saveDir, "magic.dat_old");
                if (file.exists()) {
                    loadMagicFile(file);
                }
            } catch (Exception ee) { }
        }

        if (magics.isEmpty()) {
            // Create default magics
            Magic earth  = new Magic(0, "Earth", 0x00DD00);
            earth.setIconItem(new ItemStack(CustomItems.earthElement));

            Magic water  = new Magic(1, "Water", 0xF2DD00);
            water.setIconItem(new ItemStack(CustomItems.waterElement));

            Magic fire   = new Magic(2, "Fire", 0xDD0000);
            fire.setIconItem(new ItemStack(CustomItems.spellFire));

            Magic air    = new Magic(3, "Air", 0xDD0000);
            air.setIconItem(new ItemStack(CustomItems.airElement));

            Magic dark   = new Magic(4, "Dark", 0xDD0000);
            dark.setIconItem(new ItemStack(CustomItems.spellDark));

            Magic holy   = new Magic(5, "Holy", 0xDD0000);
            holy.setIconItem(new ItemStack(CustomItems.spellHoly));

            Magic nature = new Magic(6, "Nature", 0xDD0000);
            nature.setIconItem(new ItemStack(CustomItems.spellNature));

            Magic arcane = new Magic(7, "Arcane", 0xDD0000);
            arcane.setIconItem(new ItemStack(CustomItems.spellArcane));

            // Insiders
            earth.interactions.put(air.id, 0.60f);
            water.interactions.put(earth.id, 0.60f);
            fire.interactions.put(water.id, 0.60f);
            air.interactions.put(fire.id, 0.60f);

            // Outsiders
            dark.interactions.put(nature.id, 0.60f);
            nature.interactions.put(holy.id, 0.60f);
            holy.interactions.put(arcane.id, 0.60f);
            arcane.interactions.put(dark.id, 0.60f);

            // Cross Interactions
            earth.interactions.put(nature.id, 0.30f);
            water.interactions.put(holy.id, 0.30f);
            fire.interactions.put(arcane.id, 0.30f);
            air.interactions.put(dark.id, 0.30f);
            dark.interactions.put(fire.id, 0.30f);
            nature.interactions.put(air.id, 0.30f);
            holy.interactions.put(earth.id, 0.30f);
            arcane.interactions.put(water.id, 0.30f);

            // Add them to the registry
            magics.put(earth.id, earth);
            magics.put(water.id, water);
            magics.put(fire.id, fire);
            magics.put(air.id, air);
            magics.put(dark.id, dark);
            magics.put(holy.id, holy);
            magics.put(nature.id, nature);
            magics.put(arcane.id, arcane);
        }
    }

    private void loadMagicFile(File file) throws IOException {
        DataInputStream stream = new DataInputStream(
            new BufferedInputStream(new GZIPInputStream(new FileInputStream(file)))
        );
        loadMagic(stream);
        stream.close();
    }

    public void loadMagic(DataInputStream stream) throws IOException {
        NBTTagCompound compound = CompressedStreamTools.read(stream);
        lastUsedID = compound.getInteger("lastID");
        lastUsedCycleID = compound.getInteger("lastCatID");

        // Load magics
        magics.clear();
        NBTTagList magicList = compound.getTagList("Magics", 10);
        for (int i = 0; i < magicList.tagCount(); i++) {
            NBTTagCompound magCompound = magicList.getCompoundTagAt(i);
            Magic mag = new Magic();
            mag.readNBT(magCompound);
            magics.put(mag.id, mag);
        }

        // Load categories
        cycles.clear();
        NBTTagList catList = compound.getTagList("Cycles", 10);
        for (int i = 0; i < catList.tagCount(); i++) {
            NBTTagCompound catCompound = catList.getCompoundTagAt(i);
            MagicCycle cat = new MagicCycle();
            cat.readNBT(catCompound);
            cycles.put(cat.id, cat);
        }
    }

    public NBTTagCompound getNBT() {
        NBTTagList magicList = new NBTTagList();
        for (Magic mag : magics.values()) {
            NBTTagCompound magCompound = new NBTTagCompound();
            mag.writeNBT(magCompound);
            magicList.appendTag(magCompound);
        }
        NBTTagList catList = new NBTTagList();
        for (MagicCycle cat : cycles.values()) {
            NBTTagCompound catCompound = new NBTTagCompound();
            cat.writeNBT(catCompound);
            catList.appendTag(catCompound);
        }
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("lastID", lastUsedID);
        compound.setInteger("lastCycleID", lastUsedCycleID);
        compound.setTag("Magics", magicList);
        compound.setTag("Cycles", catList);
        return compound;
    }

    public void saveMagicData() {
        try {
            File saveDir = CustomNpcs.getWorldSaveDirectory();
            File fileNew = new File(saveDir, "magic.dat_new");
            File fileOld = new File(saveDir, "magic.dat_old");
            File fileCurrent = new File(saveDir, "magic.dat");
            CompressedStreamTools.writeCompressed(getNBT(), new FileOutputStream(fileNew));
            if (fileOld.exists()) fileOld.delete();
            fileCurrent.renameTo(fileOld);
            if (fileCurrent.exists()) fileCurrent.delete();
            fileNew.renameTo(fileCurrent);
            if (fileNew.exists()) fileNew.delete();
        } catch (Exception e) {
            LogWriter.except(e);
        }
    }

    public void saveMagic(Magic mag) {
        if (mag.id < 0) {
            mag.id = getUnusedId();
            while (hasName(mag.name))
                mag.name += "_";
        } else {
            Magic existing = magics.get(mag.id);
            if (existing != null && !existing.name.equals(mag.name))
                while (hasName(mag.name))
                    mag.name += "_";
        }
        magics.put(mag.id, mag);
        saveMagicData();
    }

    public int getUnusedId() {
        if (lastUsedID == 0) {
            for (int id : magics.keySet())
                if (id > lastUsedID)
                    lastUsedID = id;
        }
        lastUsedID++;
        return lastUsedID;
    }

    public boolean hasName(String newName) {
        if (newName.trim().isEmpty()) return true;
        for (Magic mag : magics.values())
            if (mag.name.equals(newName))
                return true;
        return false;
    }

    // === Category management methods ===

    public void addCycle(MagicCycle category) {
        if (category.id < 0) {
            lastUsedCycleID++;
            category.id = lastUsedCycleID;
        } else {
            while (containsCategoryName(category.title))
                category.title += "_";
        }
        cycles.put(category.id, category);
        saveMagicData();
    }

    public boolean containsCategoryName(String title) {
        title = title.toLowerCase();
        for (MagicCycle cat : cycles.values()) {
            if (cat.title.toLowerCase().equals(title))
                return true;
        }
        return false;
    }

    public void removeCategory(int categoryId) {
        if (cycles.containsKey(categoryId)) {
            cycles.remove(categoryId);
            saveMagicData();
        }
    }

    /**
     * Associates a magic with a category along with its per-category ordering data.
     */
    public void addMagicToCycle(int magicId, int categoryId, int index, int priority) {
        MagicCycle cat = cycles.get(categoryId);
        if (cat == null) return;
        MagicAssociation assoc = new MagicAssociation();
        assoc.magicId = magicId;
        assoc.index = index;
        assoc.priority = priority;
        cat.associations.put(magicId, assoc);
        saveMagicData();
    }

    public void removeMagicFromCycle(int magicId, int categoryId) {
        MagicCycle cat = cycles.get(categoryId);
        if (cat == null) return;
        cat.associations.remove(magicId);
        saveMagicData();
    }
}
