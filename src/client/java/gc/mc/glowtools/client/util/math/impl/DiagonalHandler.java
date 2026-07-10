package gc.mc.glowtools.client.util.math.impl;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldInteger;
import gc.mc.glowtools.client.util.math.ShapeStrategy;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;

public class DiagonalHandler implements ShapeStrategy {
    private int startX = 0, startY = 0, startZ = 0;
    private int endX = 0, endY = 5, endZ = 5;

    @Override
    public List<BlockPos> calculate(BlockPos origin) {
        List<BlockPos> positions = new ArrayList<>();
        BlockPos start = origin.add(startX, startY, startZ);
        BlockPos end = origin.add(endX, endY, endZ);

        int x1 = start.getX(), y1 = start.getY(), z1 = start.getZ();
        int x2 = end.getX(), y2 = end.getY(), z2 = end.getZ();

        int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1), dz = Math.abs(z2 - z1);
        int xs = (x2 > x1) ? 1 : -1, ys = (y2 > y1) ? 1 : -1, zs = (z2 > z1) ? 1 : -1;

        if (dx >= dy && dx >= dz) {
            int err1 = 2 * dy - dx, err2 = 2 * dz - dx;
            int y = y1, z = z1;
            for (int x = x1; x != x2 + xs; x += xs) {
                positions.add(new BlockPos(x, y, z));
                if (err1 > 0) { y += ys; err1 -= 2 * dx; }
                if (err2 > 0) { z += zs; err2 -= 2 * dx; }
                err1 += 2 * dy; err2 += 2 * dz;
            }
        } else if (dy >= dx && dy >= dz) {
            int err1 = 2 * dx - dy, err2 = 2 * dz - dy;
            int x = x1, z = z1;
            for (int y = y1; y != y2 + ys; y += ys) {
                positions.add(new BlockPos(x, y, z));
                if (err1 > 0) { x += xs; err1 -= 2 * dy; }
                if (err2 > 0) { z += zs; err2 -= 2 * dy; }
                err1 += 2 * dx; err2 += 2 * dz;
            }
        } else {
            int err1 = 2 * dx - dz, err2 = 2 * dy - dz;
            int x = x1, y = y1;
            for (int z = z1; z != z2 + zs; z += zs) {
                positions.add(new BlockPos(x, y, z));
                if (err1 > 0) { x += xs; err1 -= 2 * dz; }
                if (err2 > 0) { y += ys; err2 -= 2 * dz; }
                err1 += 2 * dx; err2 += 2 * dy;
            }
        }
        return positions;
    }

    @Override
    public BlockPos[] getBounds(BlockPos origin) {
        BlockPos start = origin.add(startX, startY, startZ);
        BlockPos end = origin.add(endX, endY, endZ);
        BlockPos min = new BlockPos(Math.min(start.getX(), end.getX()),
                Math.min(start.getY(), end.getY()), Math.min(start.getZ(), end.getZ()));
        BlockPos max = new BlockPos(Math.max(start.getX(), end.getX()),
                Math.max(start.getY(), end.getY()), Math.max(start.getZ(), end.getZ()));
        return new BlockPos[]{ min, max };
    }

    @Override
    public int buildUI(GuiBase gui, int x, int y, int labelWidth, int fieldWidth, int gap, List<Object> registry) {
        String label0 = "glowtools.config.schematic_generator.label.startPos";
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label0);
        int xOff = x + labelWidth + gap;

        GuiTextFieldInteger tfSx = new GuiTextFieldInteger(xOff, y, fieldWidth, 20, gui.textRenderer);
        GuiTextFieldInteger tfSy = new GuiTextFieldInteger(xOff + fieldWidth + gap, y, fieldWidth, 20, gui.textRenderer);
        GuiTextFieldInteger tfSz = new GuiTextFieldInteger(xOff + 2 * (fieldWidth + gap), y, fieldWidth, 20, gui.textRenderer);

        tfSx.setText(String.valueOf(startX)); tfSy.setText(String.valueOf(startY)); tfSz.setText(String.valueOf(startZ));

        gui.addTextField(tfSx, f -> { try { startX = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });
        gui.addTextField(tfSy, f -> { try { startY = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });
        gui.addTextField(tfSz, f -> { try { startZ = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });

        registry.add(tfSx); registry.add(tfSy); registry.add(tfSz);
        y += 24;

        String label1 = "glowtools.config.schematic_generator.label.endPos";
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label1);
        GuiTextFieldInteger tfEx = new GuiTextFieldInteger(xOff, y, fieldWidth, 20, gui.textRenderer);
        GuiTextFieldInteger tfEy = new GuiTextFieldInteger(xOff + fieldWidth + gap, y, fieldWidth, 20, gui.textRenderer);
        GuiTextFieldInteger tfEz = new GuiTextFieldInteger(xOff + 2 * (fieldWidth + gap), y, fieldWidth, 20, gui.textRenderer);

        tfEx.setText(String.valueOf(endX)); tfEy.setText(String.valueOf(endY)); tfEz.setText(String.valueOf(endZ));

        gui.addTextField(tfEx, f -> { try { endX = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });
        gui.addTextField(tfEy, f -> { try { endY = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });
        gui.addTextField(tfEz, f -> { try { endZ = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });

        registry.add(tfEx); registry.add(tfEy); registry.add(tfEz);
        y += 24;

        return y;
    }
}