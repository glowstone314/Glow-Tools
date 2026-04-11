package gc.mc.glowtools.client.config;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.InfoUtils;
import gc.mc.glowtools.client.Reference;
import gc.mc.glowtools.client.gui.GuiConfigs;
import gc.mc.glowtools.client.logic.TransferEnchantedHandler;

public class Callbacks {
    public static void init() {
        Configs.TransferEnchanted.TRANSFER_HOTKEY.getKeybind().setCallback(
                (a, k) -> TransferEnchantedHandler.runTransfer());
        Configs.TransferEnchanted.CYCLE_FILTER_MODE.getKeybind().setCallback((a, k) -> {
            Configs.TransferEnchanted.FILTER_MODE.setOptionListValue(
                    Configs.TransferEnchanted.FILTER_MODE.getOptionListValue().cycle(true));
            String modeName = GuiBase.TXT_GREEN + Configs.TransferEnchanted.FILTER_MODE.getOptionListValue().getDisplayName();
            InfoUtils.printActionbarMessage("glowtools.chat.transfer_enchanted.currentFilterMode", Reference.chatPrefix, modeName);
            return true;
        });
        Configs.Others.OPEN_CONFIG_GUI.getKeybind().setCallback((a, k) -> {
            GuiBase.openGui(new GuiConfigs());
            return true;
        });
    }
}
