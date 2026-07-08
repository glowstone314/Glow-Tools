package gc.mc.glowtools.client.util.math.impl;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldInteger;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import gc.mc.glowtools.client.util.math.ShapeStrategy;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import java.util.ArrayList;
import java.util.List;

public class CylinderHandler implements ShapeStrategy {
    private int cylinderLength = 9;
    private int ellipseLength = 7;
    private int ellipseWidth = 7;
    private boolean hollow = false;
    private Direction.Axis axis = Direction.Axis.Y;

    private int[] getActualDimensions() {
        int w, h, d;
        if (axis == Direction.Axis.Y) {
            w = ellipseLength;
            d = ellipseWidth;
            h = cylinderLength;
        } else if (axis == Direction.Axis.X) {
            w = cylinderLength;
            h = ellipseWidth;
            d = ellipseLength;
        } else {
            d = cylinderLength;
            h = ellipseWidth;
            w = ellipseLength;
        }
        return new int[]{Math.max(1, w), Math.max(1, h), Math.max(1, d)};
    }

    private BlockPos[] calcMinMax(BlockPos origin) {
        int[] dims = getActualDimensions();
        BlockPos min = origin.add(-dims[0] / 2, -dims[1] / 2, -dims[2] / 2);
        BlockPos max = min.add(dims[0] - 1, dims[1] - 1, dims[2] - 1);
        return new BlockPos[]{ min, max };
    }

    @Override
    public List<BlockPos> calculate(BlockPos origin) {
        List<BlockPos> positions = new ArrayList<>();
        int[] dims = getActualDimensions();
        int w = dims[0], h = dims[1], d = dims[2];

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

                    boolean isInsideCircle = false;
                    switch (axis) {
                        case X -> isInsideCircle = ((dy * dy) / (ry * ry) + (dz * dz) / (rz * rz) <= 1.0);
                        case Y -> isInsideCircle = ((dx * dx) / (rx * rx) + (dz * dz) / (rz * rz) <= 1.0);
                        case Z -> isInsideCircle = ((dx * dx) / (rx * rx) + (dy * dy) / (ry * ry) <= 1.0);
                    }
                    if (isInsideCircle) inside[x][y][z] = true;
                }
            }
        }

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                for (int z = 0; z < d; z++) {
                    if (inside[x][y][z]) {
                        if (hollow) {
                            boolean isBoundary;
                            if (axis == Direction.Axis.X) {
                                isBoundary = (y == 0 || y == h - 1 || z == 0 || z == d - 1 ||
                                        !inside[x][y + 1][z] || !inside[x][y - 1][z] ||
                                        !inside[x][y][z + 1] || !inside[x][y][z - 1]);
                            } else if (axis == Direction.Axis.Y) {
                                isBoundary = (x == 0 || x == w - 1 || z == 0 || z == d - 1 ||
                                        !inside[x + 1][y][z] || !inside[x - 1][y][z] ||
                                        !inside[x][y][z + 1] || !inside[x][y][z - 1]);
                            } else {
                                isBoundary = (x == 0 || x == w - 1 || y == 0 || y == h - 1 ||
                                        !inside[x + 1][y][z] || !inside[x - 1][y][z] ||
                                        !inside[x][y + 1][z] || !inside[x][y - 1][z]);
                            }
                            if (isBoundary) positions.add(minCorner.add(x, y, z));
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
        String label0 = StringUtils.translate("glowtools.config.schematic_generator.label.axis");
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label0);
        ButtonGeneric btnAxis = new ButtonGeneric(x + labelWidth + gap, y, 60, 20, axis.name());
        gui.addButton(btnAxis, (button, mb) -> {
            axis = Direction.Axis.values()[(axis.ordinal() + 1) % 3];
            gui.initGui();
        });
        registry.add(btnAxis);
        y += 24;

        String label1 = StringUtils.translate("glowtools.config.schematic_generator.label.columnHeight");
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label1);
        GuiTextFieldInteger tfLen = new GuiTextFieldInteger(x + labelWidth + gap, y, fieldWidth, 20, gui.textRenderer);
        tfLen.setText(String.valueOf(cylinderLength));
        gui.addTextField(tfLen, f -> { try { cylinderLength = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });
        registry.add(tfLen);
        y += 24;

        String label2 = StringUtils.translate("glowtools.config.schematic_generator.label.baseDiameters");
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label2);
        int xOff = x + labelWidth + gap;
        GuiTextFieldInteger tfElLength = new GuiTextFieldInteger(xOff, y, fieldWidth, 20, gui.textRenderer);
        GuiTextFieldInteger tfElWidth = new GuiTextFieldInteger(xOff + fieldWidth + gap, y, fieldWidth, 20, gui.textRenderer);

        tfElLength.setText(String.valueOf(ellipseLength));
        tfElWidth.setText(String.valueOf(ellipseWidth));

        gui.addTextField(tfElLength, f -> { try { ellipseLength = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });
        gui.addTextField(tfElWidth, f -> { try { ellipseWidth = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; });

        registry.add(tfElLength);
        registry.add(tfElWidth);
        y += 24;

        String label3 = StringUtils.translate("glowtools.config.schematic_generator.label.hollow");
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label3);
        ButtonGeneric btnHollow = new ButtonGeneric(x + labelWidth + gap, y, 60, 20,
                StringUtils.translate(hollow ? "gui.yes" : "gui.no"));
        gui.addButton(btnHollow, (button, mb) -> {
            hollow = !hollow;
            btnHollow.setDisplayString(StringUtils.translate(hollow ? "gui.yes" : "gui.no"));
        });
        registry.add(btnHollow);
        y += 24;

        return y;
    }
}
