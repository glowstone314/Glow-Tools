package gc.mc.glowtools.client.event;

import fi.dy.masa.malilib.hotkeys.*;
import gc.mc.glowtools.client.Reference;
import gc.mc.glowtools.client.config.Configs;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {
    private static final InputHandler INSTANCE = new InputHandler();

    public static InputHandler getInstance() { return INSTANCE; }

    @Override
    public void addKeysToMap(IKeybindManager manager) {

        Configs.EntityAlerts.HOTKEYS.forEach(config -> {
            if (config instanceof IHotkey hotkey) {
                manager.addKeybindToMap(hotkey.getKeybind());
            }
        });
        Configs.TropicalFish.HOTKEYS.forEach(config -> {
            if (config instanceof IHotkey hotkey) {
                manager.addKeybindToMap(hotkey.getKeybind());
            }
        });
        Configs.TransferEnchanted.HOTKEYS.forEach(config -> {
            if (config instanceof IHotkey hotkey) {
                manager.addKeybindToMap(hotkey.getKeybind());
            }
        });
        Configs.Others.HOTKEYS.forEach(config -> {
            if (config instanceof IHotkey hotkey) {
                manager.addKeybindToMap(hotkey.getKeybind());
            }
        });

    }

    @Override
    public void addHotkeys(IKeybindManager manager) {

        manager.addHotkeysForCategory(Reference.MOD_NAME, "glowtools.hotkeys.category.entity_alerts_hotkeys", Configs.EntityAlerts.HOTKEYS);
        manager.addHotkeysForCategory(Reference.MOD_NAME, "glowtools.hotkeys.category.tropical_fish_hotkeys", Configs.TropicalFish.HOTKEYS);
        manager.addHotkeysForCategory(Reference.MOD_NAME, "glowtools.hotkeys.category.transfer_enchanted_hotkeys", Configs.TransferEnchanted.HOTKEYS);
        manager.addHotkeysForCategory(Reference.MOD_NAME, "glowtools.hotkeys.category.others_hotkeys", Configs.Others.HOTKEYS);

    }
}