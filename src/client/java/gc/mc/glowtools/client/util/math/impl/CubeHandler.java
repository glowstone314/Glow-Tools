package gc.mc.glowtools.client.util.math.impl;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldInteger;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.wrappers.TextFieldType;
import fi.dy.masa.malilib.util.StringUtils;
import gc.mc.glowtools.client.util.math.ShapeStrategy;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;

public class CubeHandler implements ShapeStrategy {
    private int width = 5;
    private int height = 5;
    private int depth = 5;
    private boolean hollow = false;

    @Override
    public List<BlockPos> calculate(BlockPos origin) {
        List<BlockPos> positions = new ArrayList<>();
        int w = Math.max(1, width);
        int h = Math.max(1, height);
        int d = Math.max(1, depth);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                for (int z = 0; z < d; z++) {
                    if (hollow) {
                        boolean isBoundary = (x == 0 || x == w - 1 ||
                                y == 0 || y == h - 1 ||
                                z == 0 || z == d - 1);
                        if (!isBoundary) {
                            continue;
                        }
                    }
                    positions.add(origin.add(x, y, z));
                }
            }
        }
        return positions;
    }

    @Override
    public BlockPos[] getBounds(BlockPos origin) {
        int w = Math.max(1, width);
        int h = Math.max(1, height);
        int d = Math.max(1, depth);
        return new BlockPos[]{ origin, origin.add(w - 1, h - 1, d - 1) };
    }

    @Override
    public int buildUI(GuiBase gui, int x, int y, int labelWidth, int fieldWidth, int gap, List<Object> registry) {
        String label0 = StringUtils.translate("glowtools.config.schematic_generator.label.xyzSideLength");
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label0);
        int xOff = x + labelWidth + gap;

        GuiTextFieldInteger tfW = new GuiTextFieldInteger(xOff, y, fieldWidth, 20, gui.font);
        GuiTextFieldInteger tfH = new GuiTextFieldInteger(xOff + fieldWidth + gap, y, fieldWidth, 20, gui.font);
        GuiTextFieldInteger tfD = new GuiTextFieldInteger(xOff + 2 * (fieldWidth + gap), y, fieldWidth, 20, gui.font);

        tfW.setText(String.valueOf(width));
        tfH.setText(String.valueOf(height));
        tfD.setText(String.valueOf(depth));

        gui.addTextField(tfW, f -> { try { width = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; }, TextFieldType.INTEGER);
        gui.addTextField(tfH, f -> { try { height = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; }, TextFieldType.INTEGER);
        gui.addTextField(tfD, f -> { try { depth = Integer.parseInt(f.getText().trim()); }
        catch (Exception ignored) {} return false; }, TextFieldType.INTEGER);

        registry.add(tfW);
        registry.add(tfH);
        registry.add(tfD);
        y += 24;

        String label1 = StringUtils.translate("glowtools.config.schematic_generator.label.hollow");
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label1);
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
