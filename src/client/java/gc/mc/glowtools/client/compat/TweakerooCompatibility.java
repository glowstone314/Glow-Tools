package gc.mc.glowtools.client.compat;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
import net.fabricmc.loader.api.FabricLoader;

public class TweakerooCompatibility {

    private static final boolean TWEAKEROO_INSTALLED = FabricLoader.getInstance().isModLoaded("tweakeroo");

    public static boolean isTweakerooFakeSneakEnabled() {
        if (TWEAKEROO_INSTALLED) {
            try {
                return FeatureToggle.TWEAK_FAKE_SNEAKING.getBooleanValue();
            } catch (Throwable ignored) {
                return false;
            }
        }
        return false;
    }

}