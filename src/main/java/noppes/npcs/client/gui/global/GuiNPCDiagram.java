package noppes.npcs.client.gui.global;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * Abstract GUI class for drawing diagrams that consist of icons (nodes) and connections (arrows).
 *
 * This class provides:
 *  - Multiple layout algorithms for positioning nodes (Circular, Square, Tree, Generated).
 *  - Panning and zooming functionality.
 *  - Standard drawing of a node's "box": a background and border that can be customized.
 *  - Connection drawing (including arrow heads).
 *  - Highlighting logic: when hovering over an icon, its connections and related icons are highlighted.
 *  - Tooltip support for nodes and connections.
 *
 * IMPORTANT: Child classes must implement the data provider methods to supply the list of icons and connections,
 * and implement how an icon’s image is rendered (renderIcon).
 */
public abstract class GuiNPCDiagram extends Gui {

    // ---------------
    // Diagram Area & Controls
    // ---------------
    protected int x, y, width, height;    // The drawing area.
    protected float panX = 0, panY = 0;     // Panning offsets.
    protected float zoom = 1.0f;            // Zoom factor.
    protected boolean dragging = false;     // Drag flag.
    protected int lastDragX, lastDragY;     // Last mouse positions.

    // ---------------
    // Node (Icon) Sizing
    // ---------------
    protected int iconSize = 16;            // Icon image size (square).
    protected int slotPadding = 4;          // Extra padding for the node's box.
    protected int slotSize = iconSize + slotPadding; // Overall size of the node box.

    // ---------------
    // Node Box Appearance
    // ---------------
    protected int iconBackgroundColor = 0xFF999999; // Background color.
    protected int iconBorderColor = 0xFF555555;       // Border color.
    protected int iconBorderThickness = 1;            // Border thickness in pixels.

    // ---------------
    // Connection Options
    // ---------------
    protected boolean showArrowHeads = true;  // Whether to draw arrow heads.
    protected boolean useColorScaling = true; // Whether to scale connection color based on a percentage.

    // ---------------
    // Layout Options
    // ---------------
    public enum DiagramLayout { CIRCULAR, SQUARE, TREE, GENERATED }
    protected DiagramLayout layout = DiagramLayout.CIRCULAR; // Default layout.

    /**
     * Allows setting the desired layout type.
     * This will invalidate the cached positions.
     */
    public void setLayout(DiagramLayout layout) {
        this.layout = layout;
        invalidateCache();
    }

    // ---------------
    // Caching
    // ---------------
    // Cache for positions (computed once for any given icon set & layout).
    protected Map<Integer, Point> cachedPositions = null;

    /**
     * Clears the cached positions.
     */
    public void invalidateCache() {
        cachedPositions = null;
    }

    // ---------------
    // Constructor
    // ---------------
    public GuiNPCDiagram(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // ---------------
    // Data Providers (abstract methods to be implemented by child classes)
    // ---------------
    protected abstract List<DiagramIcon> getIcons();
    protected abstract List<DiagramConnection> getConnections();
    protected abstract void renderIcon(DiagramIcon icon, int posX, int posY, boolean highlighted);
    protected List<String> getIconTooltip(DiagramIcon icon) { return null; }
    protected List<String> getConnectionTooltip(DiagramConnection conn) {
        List<String> tooltip = new ArrayList<>();
        DiagramIcon iconFrom = getIconById(conn.idFrom);
        DiagramIcon iconTo = getIconById(conn.idTo);
        String nameFrom = iconFrom != null ? getIconName(iconFrom) : "Unknown";
        String nameTo = iconTo != null ? getIconName(iconTo) : "Unknown";
        tooltip.add(nameFrom + " > " + nameTo + ":");
        tooltip.add(conn.hoverText);
        DiagramConnection reverse = getConnectionByIds(conn.idTo, conn.idFrom);
        if (reverse != null) {
            tooltip.add(nameTo + " > " + nameFrom + ":");
            tooltip.add(reverse.hoverText);
        }
        return tooltip;
    }
    protected String getIconName(DiagramIcon icon) { return "Icon " + icon.id; }

    // ---------------
    // Layout Calculation – choose algorithm based on layout type.
    // ---------------
    /**
     * Returns a mapping from each icon's id to its (x,y) center position.
     * Uses caching so that positions are computed only once (unless invalidated).
     */
    protected Map<Integer, Point> calculatePositions() {
        if (cachedPositions != null) {
            return cachedPositions;
        }
        switch (layout) {
            case CIRCULAR:
                cachedPositions = calculateCircularPositions();
                break;
            case SQUARE:
                cachedPositions = calculateSquarePositions();
                break;
            case TREE:
                cachedPositions = calculateTreePositions();
                break;
            case GENERATED:
                cachedPositions = calculateGeneratedPositions();
                break;
            default:
                cachedPositions = calculateCircularPositions();
                break;
        }
        return cachedPositions;
    }

    protected Map<Integer, Point> calculateCircularPositions() {
        List<DiagramIcon> icons = getIcons();
        Map<Integer, Point> positions = new HashMap<>();
        int count = icons.size();
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int radius = Math.min(width, height) / 2 - iconSize - 10;
        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            int posX = centerX + (int)(radius * Math.cos(angle));
            int posY = centerY + (int)(radius * Math.sin(angle));
            positions.put(icons.get(i).id, new Point(posX, posY));
        }
        return positions;
    }

    protected Map<Integer, Point> calculateSquarePositions() {
        List<DiagramIcon> icons = getIcons();
        Map<Integer, Point> positions = new HashMap<>();
        int count = icons.size();
        int n = (int)Math.ceil(Math.sqrt(count));
        int gridWidth = width - 20;
        int gridHeight = height - 20;
        int cellWidth = gridWidth / n;
        int cellHeight = gridHeight / n;
        int startX = x + (width - gridWidth) / 2;
        int startY = y + (height - gridHeight) / 2;
        for (int i = 0; i < count; i++) {
            int row = i / n;
            int col = i % n;
            int posX = startX + col * cellWidth + cellWidth / 2;
            int posY = startY + row * cellHeight + cellHeight / 2;
            positions.put(icons.get(i).id, new Point(posX, posY));
        }
        return positions;
    }

    protected Map<Integer, Point> calculateTreePositions() {
        List<DiagramIcon> icons = getIcons();
        Map<Integer, Point> positions = new HashMap<>();
        int count = icons.size();
        int levels = (int)Math.ceil(Math.log(count + 1) / Math.log(2));
        int levelHeight = height / (levels + 1);
        int index = 0;
        for (int level = 0; level < levels && index < count; level++) {
            int nodesThisLevel = (int)Math.min(Math.pow(2, level), count - index);
            int levelWidth = width - 20;
            int cellWidth = levelWidth / nodesThisLevel;
            for (int i = 0; i < nodesThisLevel && index < count; i++, index++) {
                int posX = x + 10 + i * cellWidth + cellWidth / 2;
                int posY = y + (level + 1) * levelHeight;
                positions.put(icons.get(index).id, new Point(posX, posY));
            }
        }
        return positions;
    }

    /**
     * Generated layout: uses a force-directed algorithm to position nodes.
     * Computed only once using a fixed seed for determinism.
     */
    protected Map<Integer, Point> calculateGeneratedPositions() {
        List<DiagramIcon> icons = getIcons();
        Map<Integer, Point> positions = new HashMap<>();
        int count = icons.size();
        int margin = 20; // margin inside diagram area

        // Fixed seed for deterministic initial placement.
        Random rand = new Random(100);
        // Initialize nodes at random positions (with margin).
        for (DiagramIcon icon : icons) {
            int posX = x + margin + rand.nextInt(Math.max(1, width - 2 * margin));
            int posY = y + margin + rand.nextInt(Math.max(1, height - 2 * margin));
            positions.put(icon.id, new Point(posX, posY));
        }

        // Fruchterman–Reingold parameters.
        double area = width * height;
        // Ideal distance between nodes.
        double k = Math.sqrt(area / (double) count);
        int iterations = 100;
        double temperature = width / 10.0; // initial max displacement
        double cooling = temperature / (iterations + 1);
        double minDistance = iconSize + slotPadding; // enforce minimal separation

        // Get the list of connections (only actual edges).
        List<DiagramConnection> connections = getConnections();

        for (int iter = 0; iter < iterations; iter++) {
            // Initialize displacement for each node.
            Map<Integer, double[]> disp = new HashMap<>();
            for (DiagramIcon v : icons) {
                disp.put(v.id, new double[] {0.0, 0.0});
            }

            // Repulsive forces: for every pair of nodes.
            for (int i = 0; i < icons.size(); i++) {
                DiagramIcon v = icons.get(i);
                Point posV = positions.get(v.id);
                for (int j = i + 1; j < icons.size(); j++) {
                    DiagramIcon u = icons.get(j);
                    Point posU = positions.get(u.id);
                    double dx = posV.x - posU.x;
                    double dy = posV.y - posU.y;
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    if (distance < minDistance) distance = minDistance;
                    double repForce = (k * k) / distance;
                    double[] dispV = disp.get(v.id);
                    double[] dispU = disp.get(u.id);
                    dispV[0] += (dx / distance) * repForce;
                    dispV[1] += (dy / distance) * repForce;
                    dispU[0] -= (dx / distance) * repForce;
                    dispU[1] -= (dy / distance) * repForce;
                }
            }

            // Attractive forces: only for nodes that are connected.
            for (DiagramConnection conn : connections) {
                Point posV = positions.get(conn.idFrom);
                Point posU = positions.get(conn.idTo);
                double dx = posV.x - posU.x;
                double dy = posV.y - posU.y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance < 0.01) distance = 0.01;
                double attrForce = (distance * distance) / k;
                double[] dispV = disp.get(conn.idFrom);
                double[] dispU = disp.get(conn.idTo);
                dispV[0] -= (dx / distance) * attrForce;
                dispV[1] -= (dy / distance) * attrForce;
                dispU[0] += (dx / distance) * attrForce;
                dispU[1] += (dy / distance) * attrForce;
            }

            // Update positions based on displacement vectors.
            for (DiagramIcon v : icons) {
                double[] d = disp.get(v.id);
                double dispLength = Math.sqrt(d[0] * d[0] + d[1] * d[1]);
                if (dispLength < 0.01) dispLength = 0.01;
                int newX = positions.get(v.id).x + (int)((d[0] / dispLength) * Math.min(dispLength, temperature));
                int newY = positions.get(v.id).y + (int)((d[1] / dispLength) * Math.min(dispLength, temperature));
                newX = Math.max(x + margin, Math.min(x + width - margin, newX));
                newY = Math.max(y + margin, Math.min(y + height - margin, newY));
                positions.get(v.id).x = newX;
                positions.get(v.id).y = newY;
            }
            temperature -= cooling;
        }
        return positions;
    }

    // ---------------
    // Standard Icon Box Drawing (Background + Border)
    // ---------------
    protected void drawIconBox(int slotX, int slotY, int slotSize, boolean highlighted) {
        int bg = highlighted ? mixColors(iconBackgroundColor, 0xFFFFFFFF, 0.2f) : iconBackgroundColor;
        drawRect(slotX, slotY, slotX + slotSize, slotY + slotSize, bg);
        for (int i = 0; i < iconBorderThickness; i++) {
            drawHorizontalLine(slotX + i, slotX + slotSize - i, slotY + i, iconBorderColor);
            drawHorizontalLine(slotX + i, slotX + slotSize - i, slotY + slotSize - 1 - i, iconBorderColor);
            drawVerticalLine(slotX + i, slotY + i, slotY + slotSize - i, iconBorderColor);
            drawVerticalLine(slotX + slotSize - 1 - i, slotY + i, slotY + slotSize - i, iconBorderColor);
        }
    }

    protected int mixColors(int color1, int color2, float ratio) {
        int a1 = (color1 >> 24) & 0xFF, r1 = (color1 >> 16) & 0xFF, g1 = (color1 >> 8) & 0xFF, b1 = color1 & 0xFF;
        int a2 = (color2 >> 24) & 0xFF, r2 = (color2 >> 16) & 0xFF, g2 = (color2 >> 8) & 0xFF, b2 = color2 & 0xFF;
        int a = (int)(a1 + (a2 - a1) * ratio);
        int r = (int)(r1 + (r2 - r1) * ratio);
        int g = (int)(g1 + (g2 - g1) * ratio);
        int b = (int)(b1 + (b2 - b1) * ratio);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    // ---------------
    // Main Draw Method
    // ---------------
    public void drawDiagram(int mouseX, int mouseY) {
        handleMouseScroll(Mouse.getDWheel());

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int factor = sr.getScaleFactor();

        Map<Integer, Point> positions = calculatePositions();
        int centerX = x + width / 2, centerY = y + height / 2;
        int tMouseX = centerX + (int)((mouseX - centerX) / zoom);
        int tMouseY = centerY + (int)((mouseY - centerY) / zoom);

        Integer hoveredIconId = null;
        int hoveredConnFrom = -1, hoveredConnTo = -1;
        HashSet<Integer> selectedIconIds = new HashSet<>();

        for (DiagramIcon icon : getIcons()) {
            Point pos = positions.get(icon.id);
            if (pos == null) continue;
            int slotX = pos.x + (int)panX - slotSize / 2;
            int slotY = pos.y + (int)panY - slotSize / 2;
            if (tMouseX >= slotX && tMouseX < slotX + slotSize &&
                tMouseY >= slotY && tMouseY < slotY + slotSize) {
                hoveredIconId = icon.id;
                selectedIconIds.add(icon.id);
                break;
            }
        }
        if (hoveredIconId != null) {
            for (DiagramConnection conn : getConnections()) {
                if (conn.idFrom == hoveredIconId || conn.idTo == hoveredIconId) {
                    selectedIconIds.add(conn.idFrom);
                    selectedIconIds.add(conn.idTo);
                }
            }
        } else {
            final double threshold = 5.0;
            outer:
            for (DiagramConnection conn : getConnections()) {
                Point pFrom = positions.get(conn.idFrom);
                Point pTo = positions.get(conn.idTo);
                if (pFrom == null || pTo == null) continue;
                double dist = pointLineDistance(tMouseX, tMouseY,
                    pFrom.x + panX, pFrom.y + panY, pTo.x + panX, pTo.y + panY);
                if (dist < threshold) {
                    hoveredConnFrom = conn.idFrom;
                    hoveredConnTo = conn.idTo;
                    selectedIconIds.add(conn.idFrom);
                    selectedIconIds.add(conn.idTo);
                    break outer;
                }
            }
        }

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x * factor, (sr.getScaledHeight() - (y + height)) * factor, width * factor, height * factor);
        drawRect(x, y, x + width, y + height, 0xFF333333);

        GL11.glPushMatrix();
        GL11.glTranslatef(centerX, centerY, 0);
        GL11.glScalef(zoom, zoom, 1.0f);
        GL11.glTranslatef(-centerX, -centerY, 0);

        for (DiagramConnection conn : getConnections()) {
            Point pFrom = positions.get(conn.idFrom);
            Point pTo = positions.get(conn.idTo);
            if (pFrom == null || pTo == null) continue;
            int ax = pFrom.x + (int)panX;
            int ay = pFrom.y + (int)panY;
            int bx = pTo.x + (int)panX;
            int by = pTo.y + (int)panY;
            boolean dim = !selectedIconIds.isEmpty() &&
                !(selectedIconIds.contains(conn.idFrom) && selectedIconIds.contains(conn.idTo));
            drawConnectionLine(ax, ay, bx, by, conn, dim);
        }

        for (DiagramIcon icon : getIcons()) {
            Point pos = positions.get(icon.id);
            if (pos == null) continue;
            int slotX = pos.x + (int)panX - slotSize / 2;
            int slotY = pos.y + (int)panY - slotSize / 2;
            boolean highlighted = !selectedIconIds.isEmpty() && selectedIconIds.contains(icon.id);
            drawIconBox(slotX, slotY, slotSize, highlighted);
            renderIcon(icon, pos.x + (int)panX, pos.y + (int)panY, highlighted);
        }
        GL11.glPopMatrix();

        if (showArrowHeads) {
            GL11.glPushMatrix();
            GL11.glTranslatef(centerX, centerY, 0);
            GL11.glScalef(zoom, zoom, 1.0f);
            GL11.glTranslatef(-centerX, -centerY, 0);
            for (DiagramConnection conn : getConnections()) {
                Point pFrom = positions.get(conn.idFrom);
                Point pTo = positions.get(conn.idTo);
                if (pFrom == null || pTo == null) continue;
                int ax = pFrom.x + (int)panX;
                int ay = pFrom.y + (int)panY;
                int bx = pTo.x + (int)panX;
                int by = pTo.y + (int)panY;
                boolean dim = !selectedIconIds.isEmpty() &&
                    !(selectedIconIds.contains(conn.idFrom) && selectedIconIds.contains(conn.idTo));
                drawArrowHead(ax, ay, bx, by, conn, dim);
            }
            GL11.glPopMatrix();
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (hoveredIconId != null) {
            DiagramIcon icon = getIconById(hoveredIconId);
            List<String> tooltip = getIconTooltip(icon);
            if (tooltip != null && !tooltip.isEmpty()) {
                drawHoveringText(tooltip, mouseX, mouseY, mc.fontRenderer);
            }
        } else if (hoveredConnFrom != -1 && hoveredConnTo != -1) {
            DiagramConnection conn = getConnectionByIds(hoveredConnFrom, hoveredConnTo);
            List<String> tooltip = getConnectionTooltip(conn);
            if (tooltip != null && !tooltip.isEmpty()) {
                drawHoveringText(tooltip, mouseX, mouseY, mc.fontRenderer);
            }
        }
    }

    protected DiagramIcon getIconById(int id) {
        for (DiagramIcon icon : getIcons()) {
            if (icon.id == id)
                return icon;
        }
        return null;
    }

    protected DiagramConnection getConnectionByIds(int idFrom, int idTo) {
        for (DiagramConnection conn : getConnections()) {
            if (conn.idFrom == idFrom && conn.idTo == idTo)
                return conn;
        }
        return null;
    }

    protected void drawConnectionLine(int x1, int y1, int x2, int y2, DiagramConnection conn, boolean dim) {
        int color = getConnectionColor(conn);
        if (!useColorScaling) { color = 0xFFFFFFFF; }
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(2.0F);
        float r = ((color >> 16)&0xFF)/255f;
        float g = ((color >> 8)&0xFF)/255f;
        float b = (color&0xFF)/255f;
        if (dim) { r*=0.4f; g*=0.4f; b*=0.4f; }
        GL11.glColor4f(r, g, b, 1f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopAttrib();
    }

    protected void drawArrowHead(int x1, int y1, int x2, int y2, DiagramConnection conn, boolean dim) {
        float dx = x2 - x1, dy = y2 - y1;
        float len = (float)Math.sqrt(dx*dx+dy*dy);
        if (len==0)return;
        float ux = dx/len, uy = dy/len;
        float defenderEdgeX = x2 - ux*(slotSize/2f);
        float defenderEdgeY = y2 - uy*(slotSize/2f);
        int arrowSize = 6;
        double angle = Math.atan2(uy,ux);
        float leftX = (float)(defenderEdgeX - arrowSize * Math.cos(angle - Math.PI/6));
        float leftY = (float)(defenderEdgeY - arrowSize * Math.sin(angle - Math.PI/6));
        float rightX = (float)(defenderEdgeX - arrowSize * Math.cos(angle + Math.PI/6));
        float rightY = (float)(defenderEdgeY - arrowSize * Math.sin(angle + Math.PI/6));
        int color = getConnectionColor(conn);
        if (!useColorScaling) { color = 0xFFFFFFFF; }
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        float r = ((color >> 16)&0xFF)/255f;
        float g = ((color >> 8)&0xFF)/255f;
        float b = (color&0xFF)/255f;
        if(dim){ r*=0.4f; g*=0.4f; b*=0.4f; }
        GL11.glColor4f(r, g, b, 1f);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2f(defenderEdgeX, defenderEdgeY);
        GL11.glVertex2f(leftX, leftY);
        GL11.glVertex2f(rightX, rightY);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopAttrib();
    }

    protected int getConnectionColor(DiagramConnection conn) {
        float percent = conn.percent;
        if (percent<=0.10f)return 0xFF80FF80;
        else if (percent<=0.40f)return 0xFFCCFF66;
        else if (percent<=0.50f)return 0xFFFFA500;
        else {
            float ratio = (percent-0.50f)/0.50f;
            ratio = Math.min(1f, Math.max(0f, ratio));
            int r1 = 0xFF, g1 = 0xA5, b1 = 0x00;
            int r2 = 0xFF, g2 = 0x00, b2 = 0x00;
            int r = r1 + (int)((r2-r1)*ratio);
            int g = g1 + (int)((g2-g1)*ratio);
            int b = b1 + (int)((b2-b1)*ratio);
            return (0xFF << 24) | (r << 16) | (g << 8) | b;
        }
    }

    private double pointLineDistance(double px, double py, double x1, double y1, double x2, double y2) {
        double A = px - x1, B = py - y1, C = x2 - x1, D = y2 - y1;
        double dot = A * C + B * D;
        double lenSq = C * C + D * D;
        double param = (lenSq!=0)?(dot/lenSq):-1;
        double xx, yy;
        if(param<0){ xx = x1; yy = y1; }
        else if(param>1){ xx = x2; yy = y2; }
        else { xx = x1+param*C; yy = y1+param*D; }
        double dx = px-xx, dy = py-yy;
        return Math.sqrt(dx*dx+dy*dy);
    }

    // ---------------
    // Mouse Handling
    // ---------------
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton==0 && isWithin(mouseX, mouseY)) {
            dragging = true;
            lastDragX = mouseX;
            lastDragY = mouseY;
            return true;
        }
        return false;
    }

    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
        if(dragging){
            int dx = mouseX-lastDragX;
            int dy = mouseY-lastDragY;
            panX += dx/zoom*0.7f;
            panY += dy/zoom*0.7f;
            lastDragX = mouseX;
            lastDragY = mouseY;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    protected boolean isWithin(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x+width && mouseY >= y && mouseY <= y+height;
    }

    /**
     * Handles zoom in/out based on mouse wheel scrolling.
     */
    public void handleMouseScroll(int scrollDelta) {
        zoom += scrollDelta * 0.0009f;
        if(zoom<0.5f) zoom = 0.5f;
        if(zoom>2.0f) zoom = 2.0f;
    }

    /**
     * Override this method if you wish to render tooltips differently.
     */
    protected void drawHoveringText(List<String> text, int mouseX, int mouseY, FontRenderer fontRenderer) {
        // Default implementation is empty.
    }

    // ---------------
    // Flag Setters
    // ---------------
    public void setShowArrowHeads(boolean showArrowHeads) {
        this.showArrowHeads = showArrowHeads;
    }

    public void setUseColorScaling(boolean useColorScaling) {
        this.useColorScaling = useColorScaling;
    }

    // ---------------
    // Inner Classes for Data Wrapping
    // ---------------
    public static class DiagramIcon {
        public int id;
        public DiagramIcon(int id) { this.id = id; }
    }

    public static class DiagramConnection {
        public int idFrom, idTo;
        public float percent;
        public String hoverText;
        public DiagramConnection(int idFrom, int idTo, float percent, String hoverText) {
            this.idFrom = idFrom;
            this.idTo = idTo;
            this.percent = percent;
            this.hoverText = hoverText;
        }
    }
}
