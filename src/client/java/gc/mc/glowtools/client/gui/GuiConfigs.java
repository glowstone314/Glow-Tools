package gc.mc.glowtools.client.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import gc.mc.glowtools.client.Reference;
import gc.mc.glowtools.client.config.Configs;

import java.util.Collections;
import java.util.List;

public class GuiConfigs extends GuiConfigsBase {
    private static ConfigGuiTab currentTab = ConfigGuiTab.ENTITY_ALERTS;

    public GuiConfigs() {
        super(10, 50, Reference.MOD_ID, null, "glowtools.gui.title.configs");
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;

        for (ConfigGuiTab tab : ConfigGuiTab.values()) {
            x += this.createTabButton(x, y, tab);
        }
    }

    private int createTabButton(int x, int y, ConfigGuiTab tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, tab.getDisplayName());
        button.setEnabled(currentTab != tab);
        this.addButton(button, new TabButtonListener(tab, this));
        return button.getWidth() + 2;
    }

    @Override
    protected int getConfigWidth() {
        return 250;
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<? extends IConfigBase> configs;
        ConfigGuiTab tab = currentTab;

        if (tab == ConfigGuiTab.ENTITY_ALERTS) {
            configs = Configs.EntityAlerts.OPTIONS;
        } else if (tab == ConfigGuiTab.TROPICAL_FISH) {
            configs = Configs.TropicalFish.OPTIONS;
        } else if (tab == ConfigGuiTab.TRANSFER_ENCHANTED) {
            configs = Configs.TransferEnchanted.OPTIONS;
        } else if (tab == ConfigGuiTab.OTHERS) {
            configs = Configs.Others.OPTIONS;
        } else {
            return Collections.emptyList();
        }

        return ConfigOptionWrapper.createFor(configs);
    }

    private record TabButtonListener(ConfigGuiTab tab, GuiConfigs parent) implements IButtonActionListener {

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (tab == ConfigGuiTab.SCHEMATIC_GENERATOR) {
                GuiBase.openGui(new GuiSchematicGenerator());
            } else {
                GuiConfigs.currentTab = this.tab;
                this.parent.reCreateListWidget();
                this.parent.getListWidget().resetScrollbarPosition();
                this.parent.initGui();
            }
        }
    }

    public enum ConfigGuiTab {
        ENTITY_ALERTS       ("glowtools.gui.button.config_gui.entity_alerts"),
        TROPICAL_FISH       ("glowtools.gui.button.config_gui.tropical_fish"),
        TRANSFER_ENCHANTED  ("glowtools.gui.button.config_gui.transfer_enchanted"),
        OTHERS              ("glowtools.gui.button.config_gui.others"),
        SCHEMATIC_GENERATOR ("glowtools.gui.button.config_gui.schematic_generator");

        private final String translationKey;

        ConfigGuiTab(String translationKey) {
            this.translationKey = translationKey;
        }

        public String getDisplayName() {
            return StringUtils.translate(this.translationKey);
        }
    }
}