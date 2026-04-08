package gc.mc.glowtools.client;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import gc.mc.glowtools.client.config.Callbacks;
import gc.mc.glowtools.client.config.Configs;
import gc.mc.glowtools.client.event.InputHandler;
import gc.mc.glowtools.client.gui.GuiConfigs;
import gc.mc.glowtools.client.logic.EntityAlertHandler;
import gc.mc.glowtools.client.logic.OthersHandler;
import gc.mc.glowtools.client.logic.TropicalFishHandler;

public class GlowtoolsInitHandler implements IInitializationHandler {

    @Override
    public void registerModHandlers() {

        ConfigManager.getInstance().registerConfigHandler(Reference.MOD_ID, new Configs());
        Registry.CONFIG_SCREEN.registerConfigScreenFactory(
                new ModInfo(Reference.MOD_ID, Reference.MOD_NAME, GuiConfigs::new)
        );

        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());

        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());

        EntityAlertHandler.init();
        TropicalFishHandler.init();
        OthersHandler.init();

        Callbacks.init();
    }

}