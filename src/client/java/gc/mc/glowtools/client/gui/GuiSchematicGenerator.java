package gc.mc.glowtools.client.gui;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.Message.MessageType;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import gc.mc.glowtools.client.config.ShapeMode;
import gc.mc.glowtools.client.compat.LitematicaBridge;
import gc.mc.glowtools.client.util.math.ShapeStrategy;
import gc.mc.glowtools.client.util.math.impl.*;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiSchematicGenerator extends GuiBase {
    private static final boolean IS_LITEMATICA_LOADED =
            net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("litematica");

    private static final Map<ShapeMode, ShapeStrategy> STRATEGIES = new HashMap<>();
    private static ShapeMode currentMode = ShapeMode.LINE;

    static {
        STRATEGIES.put(ShapeMode.LINE, new LineHandler());
        STRATEGIES.put(ShapeMode.DIAGONAL, new DiagonalHandler());
        STRATEGIES.put(ShapeMode.CUBE, new CubeHandler());
        STRATEGIES.put(ShapeMode.ELLIPSOID, new EllipsoidHandler());
        STRATEGIES.put(ShapeMode.ELLIPTICAL_CYLINDER, new CylinderHandler());
    }

    private static String currentBlockString = "minecraft:stone";
    private static String currentSchematicName = "Generated schematic";

    private GuiTextFieldGeneric textFieldBlock;
    private GuiTextFieldGeneric textFieldName;
    private final List<Object> dynamicControls = new ArrayList<>();

    public GuiSchematicGenerator() {
        super();
        this.title = StringUtils.translate("glowtools.gui.button.config_gui.schematic_generator");
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearDynamicControls();

        if (!IS_LITEMATICA_LOADED) {
            int centerX = this.getScreenWidth() / 2;
            int centerY = this.getScreenHeight() / 2;

            String warningText = StringUtils.translate("glowtools.warning.schematic_generator.requireLitematica");
            int textWidth = this.textRenderer.getWidth(warningText);
            this.addLabel(centerX - textWidth / 2, centerY - 25, textWidth, 20, 0xFFFFFFFF, warningText);

            String downloadText = StringUtils.translate("glowtools.warning.schematic_generator.downloadLitematica");
            ButtonGeneric downloadButton = new ButtonGeneric(centerX - 90, centerY, 180, 20, downloadText);
            this.addButton(downloadButton, (button, mouseButton) ->
                    ConfirmLinkScreen.open(this, "https://modrinth.com/mod/litematica", true));

            ButtonGeneric backButton = new ButtonGeneric(this.getScreenWidth() - 120, this.getScreenHeight() - 30,
                    100, 20, StringUtils.translate("gui.back"));
            this.addButton(backButton, (button, mouseButton) -> GuiBase.openGui(new GuiConfigs()));
            return;
        }

        Version v = FabricLoader.getInstance().getModContainer("litematica").get().getMetadata().getVersion();
        try {
            int compare = v.compareTo(Version.parse("0.20.9"));
            if (compare < 0) {
                this.addMessage(MessageType.WARNING, "glowtools.warning.schematic_generator.litematicaVersion");
            }
        } catch (VersionParsingException ignored) {}

        int x = 12;
        int y = 30;
        int labelWidth = 120;
        int fieldWidth = 80;
        int gap = 4;

        String label0 = StringUtils.translate("glowtools.config.schematic_generator.label.block");
        this.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label0);
        this.textFieldBlock = new GuiTextFieldGeneric(x + labelWidth + gap, y, 200, 20, this.textRenderer);
        this.textFieldBlock.setText(currentBlockString);
        this.addTextField(this.textFieldBlock, field -> {
            currentBlockString = field.getText().trim();
            return false;
        });
        y += 24;

        String label1 = StringUtils.translate("glowtools.config.schematic_generator.label.shape");
        this.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label1);

        String shapeButtonText = currentMode.getDisplayName();
        ButtonGeneric shapeButton = new ButtonGeneric(x + labelWidth + gap, y, 140, 20, shapeButtonText);
        this.addButton(shapeButton, (button, mouseButton) -> {
            currentMode = (ShapeMode) currentMode.cycle(mouseButton == 0);
            this.initGui();
        });
        y += 24;

        ShapeStrategy strategy = STRATEGIES.get(currentMode);
        if (strategy != null) {
            y = strategy.buildUI(this, x, y, labelWidth, fieldWidth, gap, dynamicControls);
        }

        String label2 = StringUtils.translate("glowtools.config.schematic_generator.label.schematicName");
        this.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label2);
        this.textFieldName = new GuiTextFieldGeneric(x + labelWidth + gap, y, 200, 20, this.textRenderer);
        this.textFieldName.setText(currentSchematicName);
        this.addTextField(this.textFieldName, field -> {
            currentSchematicName = field.getText().trim();
            return false;
        });
        y += 24;

        String label3 = StringUtils.translate("mco.create.world");
        ButtonGeneric createButton = new ButtonGeneric(x, y, 120, 20, label3);
        this.addButton(createButton, (button, mouseButton) -> createSchematic());

        String label4 = StringUtils.translate("gui.back");
        ButtonGeneric backButton = new ButtonGeneric(this.getScreenWidth() - 120, y, 100, 20, label4);
        this.addButton(backButton, (button, mouseButton) -> GuiBase.openGui(new GuiConfigs()));
    }

    private void clearDynamicControls() {
        dynamicControls.clear();
    }

    private void createSchematic() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) {
            this.addMessage(MessageType.ERROR, "glowtools.error.schematic_generator.notInWorld");
            return;
        }

        if (!IS_LITEMATICA_LOADED) {
            this.addMessage(MessageType.ERROR, "Litematica is not installed!");
            return;
        }

        String blockStr = this.textFieldBlock.getText().trim();
        if (blockStr.isEmpty()) {
            this.addMessage(MessageType.ERROR, "glowtools.error.schematic_generator.blockEmpty");
            return;
        }

        RegistryWrapper<Block> wrapper;
        try {
            wrapper = mc.getNetworkHandler().getRegistryManager().getOrThrow(RegistryKeys.BLOCK);
        } catch (Exception e) {
            this.addMessage(MessageType.ERROR, "glowtools.error.schematic_generator.noRegistry");
            return;
        }

        BlockState blockState;
        try {
            var result = BlockArgumentParser.block(wrapper, blockStr, true);
            blockState = result.blockState();
            if (blockState == null) {
                this.addMessage(MessageType.ERROR, "glowtools.error.schematic_generator.invalidBlock");
                return;
            }
        } catch (Exception e) {
            this.addMessage(MessageType.ERROR, "glowtools.error.schematic_generator.parseBlock", e.getMessage());
            return;
        }

        String name = this.textFieldName.getText().trim();
        if (name.isEmpty()) name = "Generated schematic";

        ShapeStrategy strategy = STRATEGIES.get(currentMode);
        if (strategy == null) {
            this.addMessage(MessageType.ERROR, "glowtools.error.schematic_generator.unknownShape");
            return;
        }

        BlockPos origin = mc.player.getBlockPos();
        List<BlockPos> positions = strategy.calculate(origin);
        BlockPos[] bounds = strategy.getBounds(origin);

        if (positions.isEmpty() || bounds.length < 2) {
            this.addMessage(MessageType.ERROR, "glowtools.error.schematic_generator.noBlocks");
            return;
        }

        try {
            LitematicaBridge.buildAndPlace(this, mc, name, blockState, positions, bounds[0], bounds[1], origin);
            this.addMessage(MessageType.SUCCESS, "glowtools.chat.schematic_generator.created", name, positions.size());
        } catch (Throwable t) {
            this.addMessage(MessageType.ERROR, "Failed to delegate to Litematica: " + t.getMessage());
        }
    }
}
