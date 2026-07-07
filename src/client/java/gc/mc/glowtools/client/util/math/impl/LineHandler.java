package gc.mc.glowtools.client.util.math.impl;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldInteger;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.wrappers.TextFieldType;
import fi.dy.masa.malilib.util.StringUtils;
import gc.mc.glowtools.client.util.math.ShapeStrategy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import java.util.ArrayList;
import java.util.List;

public class LineHandler implements ShapeStrategy {
    private int length = 10;
    private Direction.Axis axis = Direction.Axis.X;

    @Override
    public List<BlockPos> calculate(BlockPos origin) {
        List<BlockPos> positions = new ArrayList<>();
        for (int i = 0; i < Math.max(1, length); i++) {
            positions.add(origin.offset(
                    axis == Direction.Axis.X ? i : 0,
                    axis == Direction.Axis.Y ? i : 0,
                    axis == Direction.Axis.Z ? i : 0
            ));
        }
        return positions;
    }

    @Override
    public BlockPos[] getBounds(BlockPos origin) {
        BlockPos max = origin.offset(
                axis == Direction.Axis.X ? length - 1 : 0,
                axis == Direction.Axis.Y ? length - 1 : 0,
                axis == Direction.Axis.Z ? length - 1 : 0
        );
        return new BlockPos[]{ origin, max };
    }

    @Override
    public int buildUI(GuiBase gui, int x, int y, int labelWidth, int fieldWidth, int gap, List<Object> registry) {
        String label0 = StringUtils.translate("glowtools.config.schematic_generator.label.axis");
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label0);
        ButtonGeneric btnAxis = new ButtonGeneric(x + labelWidth + gap, y, 60, 20, axis.name());
        gui.addButton(btnAxis, (button, mb) -> {
            axis = Direction.Axis.values()[(axis.ordinal() + 1) % 3];
            btnAxis.setDisplayString(axis.name());
        });
        registry.add(btnAxis);
        y += 24;

        String label1 = StringUtils.translate("glowtools.config.schematic_generator.label.length");
        gui.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label1);
        GuiTextFieldInteger tfLen = new GuiTextFieldInteger(x + labelWidth + gap, y, fieldWidth, 20, gui.font);
        tfLen.setValue(String.valueOf(length));
        gui.addTextField(tfLen, field -> {
            try { length = Integer.parseInt(field.getValue().trim()); } catch (Exception ignored) {}
            return false;
        }, TextFieldType.INTEGER);
        registry.add(tfLen);
        y += 24;

        return y;
    }
}