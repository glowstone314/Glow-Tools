package gc.mc.glowtools.client.event;

import fi.dy.masa.malilib.hotkeys.*;
import gc.mc.glowtools.client.Reference;
import gc.mc.glowtools.client.config.Configs;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {
    private static final InputHandler INSTANCE = new InputHandler();

    public static InputHandler getInstance() { return INSTANCE; }

    @Override
    public void addKeysToMap(IKeybindManager manager) {

        for (IHotkey hotkey : Configs.EntityAlerts.HOTKEYS) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
        for (IHotkey hotkey : Configs.TropicalFish.HOTKEYS) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
        for (IHotkey hotkey : Configs.TransferEnchanted.HOTKEYS) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
        for (IHotkey hotkey : Configs.Others.HOTKEYS) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }

    }

    @Override
    public void addHotkeys(IKeybindManager manager) {

        manager.addHotkeysForCategory(Reference.MOD_NAME, "glowtools.hotkeys.category.entity_alerts_hotkeys", Configs.EntityAlerts.HOTKEYS);
        manager.addHotkeysForCategory(Reference.MOD_NAME, "glowtools.hotkeys.category.tropical_fish_hotkeys", Configs.TropicalFish.HOTKEYS);
        manager.addHotkeysForCategory(Reference.MOD_NAME, "glowtools.hotkeys.category.transfer_enchanted_hotkeys", Configs.TransferEnchanted.HOTKEYS);
        manager.addHotkeysForCategory(Reference.MOD_NAME, "glowtools.hotkeys.category.others_hotkeys", Configs.Others.HOTKEYS);

    }
}