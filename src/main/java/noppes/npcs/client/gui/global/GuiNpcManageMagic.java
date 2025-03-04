package noppes.npcs.client.gui.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kamkeel.npcs.network.PacketClient;
import kamkeel.npcs.network.packets.request.magic.MagicCycleRemovePacket;
import kamkeel.npcs.network.packets.request.magic.MagicCycleSavePacket;
import kamkeel.npcs.network.packets.request.magic.MagicGetAllPacket;
import kamkeel.npcs.network.packets.request.magic.MagicRemovePacket;
import kamkeel.npcs.network.packets.request.magic.MagicSavePacket;
import noppes.npcs.client.gui.magic.SubGuiMagicCycleEdit;
import noppes.npcs.client.gui.magic.SubGuiMagicEdit;
import noppes.npcs.client.gui.magic.SubGuiMagicInteractionsEdit;
import noppes.npcs.client.gui.util.*;
import noppes.npcs.controllers.MagicController;
import noppes.npcs.controllers.data.Magic;
import noppes.npcs.controllers.data.MagicCycle;
import noppes.npcs.entity.EntityNPCInterface;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;

import static noppes.npcs.client.gui.player.inventory.GuiCNPCInventory.specialIcons;

public class GuiNpcManageMagic extends GuiNPCInterface2 implements ISubGuiListener, ICustomScrollListener, IGuiData {

    private GuiCustomScroll leftScroll;   // left scroll for cycles (viewByCycle)
    private GuiCustomScroll rightScroll;  // right scroll for magics

    // Toggle mode: false = view all magics; true = view by cycle
    private boolean viewByCycle = false;

    private HashMap<String, Integer> cycleData = new HashMap<>();
    private HashMap<String, Integer> magicData = new HashMap<>();

    private MagicCycle selectedCycle;
    private Magic selectedMagic;

    // Divider/resizing variables (only used when viewByCycle is true)
    private boolean isResizing = false;
    private int initialDragX = 0;
    private int dividerOffset = 143; // initial offset from regionLeft
    private final int dividerWidth = 5;
    private final int minScrollWidth = 50;
    private int dividerLineHeight = 20;
    private int dividerLineYOffset = 0;

    // Search strings – IDs 55 and 66 are used for search text fields
    private String cycleSearch = "";
    private String magicSearch = "";

    public GuiNpcManageMagic(EntityNPCInterface npc) {
        super(npc);
        PacketClient.sendClient(new MagicGetAllPacket());
    }

    @Override
    public void initGui() {
        super.initGui();
        int y = guiTop + 5;

        addButton(new GuiToggleButton(50, guiLeft + 368, guiTop + ySize - 10, viewByCycle));
        ((GuiToggleButton) getButton(50)).setTextureOff(specialIcons).setTextureOffPos( 16, 0);
        ((GuiToggleButton) getButton(50)).setIconTexture(specialIcons).setIconPos( 16, 16, 16, 0 );

        if (!viewByCycle) {
            // Define horizontal region (similar to your quest GUI)
            int regionLeft = guiLeft + 64;
            int regionRight = guiLeft + 355;
            int dividerX = regionLeft + dividerOffset;

            // Left scroll (cycles)
            if (leftScroll == null) {
                leftScroll = new GuiCustomScroll(this, 0, 0);
            }
            leftScroll.guiLeft = regionLeft;
            leftScroll.guiTop = y;
            leftScroll.setSize(dividerX - regionLeft, 185);
            this.addScroll(leftScroll);

            // Right scroll (magics for selected cycle)
            if (rightScroll == null) {
                rightScroll = new GuiCustomScroll(this, 1, 0);
            }
            rightScroll.guiLeft = dividerX + dividerWidth;
            rightScroll.guiTop = y;
            rightScroll.setSize(regionRight - (dividerX + dividerWidth), 185);
            this.addScroll(rightScroll);

            // Add search bars below each scroll – using text field IDs 55 and 66
            addTextField(new GuiNpcTextField(55, this, fontRendererObj, regionLeft, y + 185 + 3, dividerX - regionLeft, 20, cycleSearch));
            addTextField(new GuiNpcTextField(66, this, fontRendererObj, dividerX + dividerWidth, y + 185 + 3, regionRight - (dividerX + dividerWidth), 20, magicSearch));

            // Left side cycle management buttons (mirroring your quest GUI style)
            addButton(new GuiNpcButton(10, guiLeft + 3, guiTop + 8, 58, 20, "Cycles"));
            getButton(10).setEnabled(false);
            addButton(new GuiNpcButton(4, guiLeft + 3, guiTop + 38, 58, 20, "gui.add"));
            addButton(new GuiNpcButton(5, guiLeft + 3, guiTop + 61, 58, 20, "gui.remove"));
            addButton(new GuiNpcButton(6, guiLeft + 3, guiTop + 94, 58, 20, "gui.edit"));

            // Right side magic management buttons
            addButton(new GuiNpcButton(33, guiLeft + 358, guiTop + 8, 58, 20, "Magics"));
            getButton(33).setEnabled(false);

            addButton(new GuiNpcButton(0, guiLeft + 358, guiTop + 38, 58, 20, "gui.add"));
            addButton(new GuiNpcButton(1, guiLeft + 358, guiTop + 61, 58, 20, "gui.remove"));

            addButton(new GuiNpcButton(2, guiLeft + 358, guiTop + 94, 58, 20, "gui.edit"));
        } else {
            // Default mode: one list for all magics plus one search bar
            if (rightScroll == null) {
                rightScroll = new GuiCustomScroll(this, 1, 0);
            }
            rightScroll.guiLeft = guiLeft + 10;
            rightScroll.guiTop = y;
            rightScroll.setSize(xSize - 75, 185);
            this.addScroll(rightScroll);
            addTextField(new GuiNpcTextField(66, this, fontRendererObj, guiLeft + 10, y + 185 + 3, xSize - 75, 20, magicSearch));

            addButton(new GuiNpcButton(33, guiLeft + 358, guiTop + 8, 58, 20, "Magics"));
            getButton(33).setEnabled(false);

            // Global magic management buttons
            addButton(new GuiNpcButton(0, guiLeft + 358, guiTop + 38, 58, 20, "gui.add"));
            addButton(new GuiNpcButton(1, guiLeft + 358, guiTop + 61, 58, 20, "gui.remove"));

            addButton(new GuiNpcButton(2, guiLeft + 358, guiTop + 94, 58, 20, "gui.edit"));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!viewByCycle && !hasSubGui()) {
            int regionLeft = guiLeft + 64;
            int dividerX = regionLeft + dividerOffset;
            int regionTop = guiTop + 30;
            int regionHeight = 185;
            int handleTop = regionTop + (regionHeight - dividerLineHeight) / 2 + dividerLineYOffset;
            drawRect(dividerX + 1, handleTop, dividerX + dividerWidth - 1, handleTop + dividerLineHeight, 0xFF707070);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!viewByCycle && !hasSubGui()) {
            int regionLeft = guiLeft + 64;
            int dividerX = regionLeft + dividerOffset;
            int regionTop = guiTop + 30;
            int regionHeight = 185;
            int handleTop = regionTop + (regionHeight - dividerLineHeight) / 2 + dividerLineYOffset;
            int handleBottom = handleTop + dividerLineHeight;
            if (mouseX >= dividerX && mouseX <= dividerX + dividerWidth &&
                mouseY >= handleTop && mouseY <= handleBottom) {
                isResizing = true;
                resizingActive = true;
                initialDragX = mouseX;
                return;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void save() {}

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (!viewByCycle && isResizing) {
            int dx = mouseX - initialDragX;
            initialDragX = mouseX;
            dividerOffset += dx;
            int regionLeft = guiLeft + 64;
            int regionRight = guiLeft + 355;
            int minOffset = minScrollWidth;
            int maxOffset = (regionRight - regionLeft) - dividerWidth - minScrollWidth;
            if (dividerOffset < minOffset) {
                dividerOffset = minOffset;
            }
            if (dividerOffset > maxOffset) {
                dividerOffset = maxOffset;
            }
            int dividerX = regionLeft + dividerOffset;
            leftScroll.setSize(dividerX - regionLeft, 185);
            rightScroll.guiLeft = dividerX + dividerWidth;
            rightScroll.setSize(regionRight - (dividerX + dividerWidth), 185);
            // Update search field sizes
            if (getTextField(55) != null) {
                getTextField(55).width = dividerX - regionLeft;
            }
            if (getTextField(66) != null) {
                getTextField(66).width = regionRight - (dividerX + dividerWidth);
                getTextField(66).xPosition = dividerX + dividerWidth;
            }
            return;
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
        if (isResizing) {
            isResizing = false;
            resizingActive = false;
            return;
        }
        super.mouseMovedOrUp(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char c, int i) {
        super.keyTyped(c, i);
        if (!viewByCycle) {
            if (getTextField(55) != null && getTextField(55).isFocused()) {
                if (!cycleSearch.equals(getTextField(55).getText())) {
                    cycleSearch = getTextField(55).getText().toLowerCase();
                    leftScroll.resetScroll();
                    leftScroll.setList(getCycleSearch());
                }
            }
        }
        if (getTextField(66) != null && getTextField(66).isFocused()) {
            if (!magicSearch.equals(getTextField(66).getText())) {
                magicSearch = getTextField(66).getText().toLowerCase();
                rightScroll.resetScroll();
                rightScroll.setList(getMagicSearch());
            }
        }
    }

    private List<String> getCycleSearch() {
        if (cycleSearch.isEmpty()) {
            return new ArrayList<>(cycleData.keySet());
        }
        List<String> list = new ArrayList<>();
        for (String name : cycleData.keySet()) {
            if (name.toLowerCase().contains(cycleSearch))
                list.add(name);
        }
        return list;
    }

    private List<String> getMagicSearch() {
        if (magicSearch.isEmpty()) {
            return new ArrayList<>(magicData.keySet());
        }
        List<String> list = new ArrayList<>();
        for (String name : magicData.keySet()) {
            if (name.toLowerCase().contains(magicSearch))
                list.add(name);
        }
        return list;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 50:
                viewByCycle = !viewByCycle;
                initGui();
                return;
            // Left side: Cycle management
            case 4: // Add Cycle
            {
                NBTTagCompound cycleCompound = new NBTTagCompound();
                cycleCompound.setString("Title", "New Cycle");
                PacketClient.sendClient(new MagicCycleSavePacket(cycleCompound));
            }
            break;
            case 5: // Remove Cycle
                if (selectedCycle != null) {
                    PacketClient.sendClient(new MagicCycleRemovePacket(selectedCycle.id));
                }
                break;
            case 6: // Edit Cycle
                if (selectedCycle != null) {
                    setSubGui(new SubGuiMagicCycleEdit(selectedCycle));
                }
                break;
            // Right side: Magic management (in viewByCycle)
            case 0: // Add Magic to selected cycle
                if (selectedCycle != null) {
                    NBTTagCompound magicCompound = new NBTTagCompound();
                    magicCompound.setString("Name", "New Magic");
                    magicCompound.setInteger("CycleID", selectedCycle.id);
                    magicCompound.setInteger("Index", 0);
                    magicCompound.setInteger("Priority", 0);
                    PacketClient.sendClient(new MagicSavePacket(magicCompound));
                }
                break;
            case 1: // Edit Magic
                if (selectedMagic != null) {
                    setSubGui(new SubGuiMagicEdit(selectedMagic));
                }
                break;
            case 2: // Remove Magic
                if (selectedMagic != null) {
                    PacketClient.sendClient(new MagicRemovePacket(selectedMagic.id));
                }
                break;
            case 3: // Edit Interactions
                if (selectedMagic != null) {
                    setSubGui(new SubGuiMagicInteractionsEdit(selectedMagic));
                }
                break;
            // Default mode buttons (global view)
            case 21:
                if (selectedMagic != null) {
                    setSubGui(new SubGuiMagicEdit(selectedMagic));
                }
                break;
            case 22:
                if (selectedMagic != null) {
                    PacketClient.sendClient(new MagicRemovePacket(selectedMagic.id));
                }
                break;
            case 23:
                if (selectedMagic != null) {
                    setSubGui(new SubGuiMagicInteractionsEdit(selectedMagic));
                }
                break;
        }
    }

    @Override
    public void customScrollClicked(int id, int index, int clickType, GuiCustomScroll scroll) {
        if (!viewByCycle) {
            if (scroll.id == 0) { // Left scroll: cycles
                String cycleName = scroll.getSelected();
                for (MagicCycle cycle : MagicController.getInstance().cycles.values()) {
                    if (cycle.name.equals(cycleName)) {
                        selectedCycle = cycle;
                        break;
                    }
                }
                // Update right scroll with magics in selected cycle
                if (selectedCycle != null) {
                    List<String> magicNames = new ArrayList<>();
                    for (Integer magicId : selectedCycle.associations.keySet()) {
                        Magic m = MagicController.getInstance().magics.get(magicId);
                        if (m != null) {
                            magicNames.add(m.name);
                        }
                    }
                    rightScroll.setList(magicNames);
                }
            } else if (scroll.id == 1) { // Right scroll: magics
                String magicName = scroll.getSelected();
                for (Magic m : MagicController.getInstance().magics.values()) {
                    if (m.name.equals(magicName)) {
                        selectedMagic = m;
                        break;
                    }
                }
            }
        } else {
            if (scroll.id == 1) {
                String magicName = scroll.getSelected();
                for (Magic m : MagicController.getInstance().magics.values()) {
                    if (m.name.equals(magicName)) {
                        selectedMagic = m;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void customScrollDoubleClicked(String selection, GuiCustomScroll scroll) {}

    @Override
    public void subGuiClosed(SubGuiInterface subgui) {
        PacketClient.sendClient(new MagicGetAllPacket());
        initGui();
    }

    @Override
    public void setGuiData(NBTTagCompound compound) {
        cycleData.clear();
        magicData.clear();
        if (compound.hasKey("CycleNames")) {
            String cyclesStr = compound.getString("CycleNames");
            String[] names = cyclesStr.split(",");
            int id = 0;
            for (String name : names) {
                if (!name.trim().isEmpty()) {
                    cycleData.put(name.trim(), id++);
                }
            }
        }
        if (compound.hasKey("MagicNames")) {
            String magicsStr = compound.getString("MagicNames");
            String[] names = magicsStr.split(",");
            int id = 0;
            for (String name : names) {
                if (!name.trim().isEmpty()) {
                    magicData.put(name.trim(), id++);
                }
            }
        }
        if (!viewByCycle) {
            leftScroll.setList(new ArrayList<>(cycleData.keySet()));
        }
        rightScroll.setList(new ArrayList<>(magicData.keySet()));
        initGui();
    }
}
