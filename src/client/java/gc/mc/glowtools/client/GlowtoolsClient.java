package gc.mc.glowtools.client;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;

public class GlowtoolsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        InitializationHandler.getInstance().registerInitializationHandler(new GlowtoolsInitHandler());
    }

}
