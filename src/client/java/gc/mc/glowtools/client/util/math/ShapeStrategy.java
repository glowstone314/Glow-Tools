package gc.mc.glowtools.client.util.math;

import fi.dy.masa.malilib.gui.GuiBase;
import net.minecraft.util.math.BlockPos;
import java.util.List;

public interface ShapeStrategy {
    List<BlockPos> calculate(BlockPos origin);

    BlockPos[] getBounds(BlockPos origin);

    int buildUI(GuiBase gui, int x, int y, int labelWidth, int fieldWidth, int gap, List<Object> registry);
}