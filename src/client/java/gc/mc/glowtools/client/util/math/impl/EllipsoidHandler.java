package gc.mc.glowtools.client.util.math.impl;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldInteger;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import gc.mc.glowtools.client.util.math.ShapeStrategy;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;

public class EllipsoidHandler implements ShapeStrategy {
    private int width = 9, height = 9, depth = 9;
    private boolean hollow = false;

    private BlockPos[] calcMinMax(BlockPos origin) {
        int w = Math.max(1, width), h = Math.max(1, height), d = Math.max(1, depth);
        BlockPos min = origin.add(-w / 2, -h / 2, -d / 2);
        BlockPos max = min.add(w - 1, h - 1, d - 1);
        return new BlockPos[]{ min, max };
    }

    @Override
    public List<BlockPos> calculate(BlockPos origin) {
        List<BlockPos> positions = new ArrayList<>();
        int w = Math.max(1, width), h = Math.max(1, height), d = Math.max(1, depth);
        BlockPos[] bounds = calcMinMax(origin);
        BlockPos minCorner = bounds[0];

        double cx = minCorner.getX() + (w - 1) / 2.0;
        double cy = minCorner.getY() + (h - 1) / 2.0;
        double cz = minCorner.getZ() + (d - 1) / 2.0;
        double rx = w / 2.0, ry = h / 2.0, rz = d / 2.0;

        boolean[][][] inside = new boolean[w][h][d];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                for (int z = 0; z < d; z++) {
                    double dx = (minCorner.getX() + x) - cx;
                    double dy = (minCorner.getY() + y) - cy;
                    double dz = (minCorner.getZ() + z) - cz;
                    if ((dx * dx) / (rx * rx) + (dy * dy) / (ry * ry) + (dz * dz) / (rz * rz) <= 1.0) {
                        inside[x][y][z] = true;
                    }
                }
            }
        }

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                for (int z = 0; z < d; z++) {
                    if (inside[x][y][z]) {
                        if (hollow) {
                            boolean isBoundary = (x == 0 || x == w - 1 || y == 0 || y == h - 1 || z == 0 || z == d - 1);
                            if (isBoundary || !inside[x + 1][y][z] || !inside[x - 1][y][z] ||
                                    !inside[x][y + 1][z] || !inside[x][y - 1][z] ||
                                    !inside[x][y][z + 1] || !inside[x][y][z - 1]) {
                                positions.add(minCorner.add(x, y, z));
                            }
                        } else {
                            positions.add(minCorner.add(x, y, z));
                        }
                    }
                }
            }
        }
        return positions;
    }

    @Override
    public BlockPos[] getBounds(BlockPos origin) {
        return calcMinMax(origin);
    }

    @Override
    public int buildUI(GuiBase gui, int x, int y, int labelWidth, int fieldWidth, int gap, List<Object> registry) {
        String label0 = StringUtils.translate("glowtools.config.schematic_generator.label.xyzDiameter");
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label0);
        int xOff = x + labelWidth + gap;

        GuiTextFieldInteger tfW = new GuiTextFieldInteger(xOff, y, fieldWidth, 20, gui.textRenderer);
        GuiTextFieldInteger tfH = new GuiTextFieldInteger(xOff + fieldWidth + gap, y, fieldWidth, 20, gui.textRenderer);
        GuiTextFieldInteger tfD = new GuiTextFieldInteger(xOff + 2 * (fieldWidth + gap), y, fieldWidth, 20, gui.textRenderer);

        tfW.setText(String.valueOf(width)); tfH.setText(String.valueOf(height)); tfD.setText(String.valueOf(depth));

        gui.addTextField(tfW, f -> { try { width = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });
        gui.addTextField(tfH, f -> { try { height = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });
        gui.addTextField(tfD, f -> { try { depth = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });

        registry.add(tfW); registry.add(tfH); registry.add(tfD);
        y += 24;

        String label1 = StringUtils.translate("glowtools.config.schematic_generator.label.hollow");
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label1);
        ButtonGeneric btnHollow = new ButtonGeneric(x + labelWidth + gap, y, 60, 20, String.valueOf(hollow));
        gui.addButton(btnHollow, (button, mb) -> {
            hollow = !hollow;
            btnHollow.setDisplayString(String.valueOf(hollow));
        });
        registry.add(btnHollow);
        y += 24;

        return y;
    }
}