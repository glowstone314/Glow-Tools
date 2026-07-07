package gc.mc.glowtools.client.compat;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.SchematicHolder;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.malilib.gui.Message;
import gc.mc.glowtools.client.gui.GuiSchematicGenerator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import java.util.List;

public class LitematicaBridge {
    public static void buildAndPlace(GuiSchematicGenerator gui, Minecraft mc, String name, BlockState blockState,
                                     List<BlockPos> positions, BlockPos minCorner, BlockPos maxCorner, BlockPos origin) {
        AreaSelection area = new AreaSelection();
        area.setName(name);
        Box box = new Box(minCorner, maxCorner, "main");
        area.addSubRegionBox(box, true);
        area.setSelectedSubRegionBox("main");

        String author = mc.player.getName().getString();
        LitematicaSchematic schematic = LitematicaSchematic.createEmptySchematic(area, author);
        if (schematic == null) {
            gui.addMessage(Message.MessageType.ERROR, "glowtools.error.schematic_generator.createFailed");
            return;
        }

        var container = schematic.getSubRegionContainer("main");
        if (container == null) {
            gui.addMessage(Message.MessageType.ERROR, "glowtools.error.schematic_generator.noContainer");
            return;
        }
        BlockPos size = box.getSize();
        int w = Math.abs(size.getX());
        int h = Math.abs(size.getY());
        int d = Math.abs(size.getZ());
        BlockPos min = box.getPos1();
        for (BlockPos p : positions) {
            int relX = p.getX() - min.getX();
            int relY = p.getY() - min.getY();
            int relZ = p.getZ() - min.getZ();
            if (relX >= 0 && relX < w && relY >= 0 && relY < h && relZ >= 0 && relZ < d) {
                container.set(relX, relY, relZ, blockState);
            }
        }

        schematic.getMetadata().setTotalBlocks(positions.size());
        schematic.getMetadata().setTimeModifiedToNow();

        SchematicHolder.getInstance().addSchematic(schematic, true);
        SchematicPlacement placement = SchematicPlacement.createFor(schematic, origin, name, true, true);
        DataManager.getSchematicPlacementManager().addSchematicPlacement(placement, true);
        DataManager.getSchematicPlacementManager().setSelectedSchematicPlacement(placement);
    }
}