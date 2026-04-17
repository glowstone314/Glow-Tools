package gc.mc.glowtools.client.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.MessageOutputType;
import fi.dy.masa.malilib.util.data.json.JsonUtils;
import gc.mc.glowtools.client.Reference;

import java.nio.file.Files;
import java.nio.file.Path;

public class Configs implements IConfigHandler {

    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static final KeybindSettings ANY = KeybindSettings.create(KeybindSettings.Context.ANY,
            KeyAction.PRESS, false, true, false, true);

    private static final String ENTITY_ALERT_KEY = Reference.MOD_ID + ".config.entity_alerts";
    private static final String TROPICAL_FISH_KEY = Reference.MOD_ID + ".config.tropical_fish";
    private static final String TRANSFER_ENCHANTED_KEY = Reference.MOD_ID + ".config.transfer_enchanted";
    private static final String OTHERS_KEY = Reference.MOD_ID + ".config.others";

    public static class EntityAlerts {

        public static final ConfigBooleanHotkeyed   ENABLE_ALERTS =             new ConfigBooleanHotkeyed(  "enableAlerts", false, "").apply(ENTITY_ALERT_KEY);
        public static final ConfigBooleanHotkeyed   ENABLE_WANDERING_TRADER =   new ConfigBooleanHotkeyed(  "enableWanderingTrader", true, "").apply(ENTITY_ALERT_KEY);
        public static final ConfigBooleanHotkeyed   ENABLE_PILLAGER =           new ConfigBooleanHotkeyed(  "enablePillager", false, "").apply(ENTITY_ALERT_KEY);
        public static final ConfigBooleanHotkeyed   ENABLE_FOX_EMERALD =        new ConfigBooleanHotkeyed(  "enableFoxEmerald", false, "").apply(ENTITY_ALERT_KEY);
        public static final ConfigBooleanHotkeyed   ENABLE_DROWNED_SNIFFER =    new ConfigBooleanHotkeyed(  "enableDrownedSniffer", false, "").apply(ENTITY_ALERT_KEY);
        public static final ConfigBooleanHotkeyed   ENABLE_CUSTOM_ENTITY =      new ConfigBooleanHotkeyed(  "enableCustomEntity", false, "").apply(ENTITY_ALERT_KEY);
        public static final ConfigString            CUSTOM_ENTITY_ID =          new ConfigString(           "customEntityId", "").apply(ENTITY_ALERT_KEY);
        public static final ConfigBooleanHotkeyed   IGNORE_NAMED_ENTITIES =     new ConfigBooleanHotkeyed(  "ignoreNamedEntities", true, "").apply(ENTITY_ALERT_KEY);
        public static final ConfigBooleanHotkeyed   ENABLE_SOUND =              new ConfigBooleanHotkeyed(  "enableSound", true, "").apply(ENTITY_ALERT_KEY);
        public static final ConfigBooleanHotkeyed   ENABLE_GLOW =               new ConfigBooleanHotkeyed(  "enableGlow", true, "").apply(ENTITY_ALERT_KEY);
        public static final ConfigBooleanHotkeyed   USE_EXTERNAL_SOUND =        new ConfigBooleanHotkeyed(  "useExternalSound", false, "").apply(ENTITY_ALERT_KEY);
        public static final ConfigBooleanHotkeyed   EXIT_WORLD =                new ConfigBooleanHotkeyed(  "exitWorld", false, "").apply(ENTITY_ALERT_KEY);
        public static final ConfigOptionList        NOTIFY_MODE =               new ConfigOptionList(       "notifyMode", MessageOutputType.MESSAGE).apply(ENTITY_ALERT_KEY);
        public static final ConfigInteger           SOUND_INTERVAL_MS =         new ConfigInteger(          "soundIntervalMs", 300, 1, 600000).apply(ENTITY_ALERT_KEY);
        public static final ConfigInteger           SOUND_COUNT =               new ConfigInteger(          "soundCount", 3, 1, 32).apply(ENTITY_ALERT_KEY);
        public static final ConfigString            CUSTOM_SOUND =              new ConfigString(           "customSound", "entity.generic.explode").apply(ENTITY_ALERT_KEY);
        public static final ConfigColor             GLOW_COLOR =                new ConfigColor(            "glowColor", "0xFFFFFF").apply(ENTITY_ALERT_KEY);

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                ENABLE_ALERTS,
                ENABLE_WANDERING_TRADER,
                ENABLE_PILLAGER,
                ENABLE_FOX_EMERALD,
                ENABLE_DROWNED_SNIFFER,
                ENABLE_CUSTOM_ENTITY,
                CUSTOM_ENTITY_ID,
                IGNORE_NAMED_ENTITIES,
                NOTIFY_MODE,
                ENABLE_SOUND,
                USE_EXTERNAL_SOUND,
                EXIT_WORLD,
                ENABLE_GLOW,
                GLOW_COLOR,
                SOUND_INTERVAL_MS,
                SOUND_COUNT,
                CUSTOM_SOUND
        );

        public static final ImmutableList<IHotkeyTogglable> HOTKEYS = ImmutableList.of(
                ENABLE_ALERTS,
                ENABLE_WANDERING_TRADER,
                ENABLE_PILLAGER,
                ENABLE_FOX_EMERALD,
                ENABLE_DROWNED_SNIFFER,
                ENABLE_CUSTOM_ENTITY,
                IGNORE_NAMED_ENTITIES,
                ENABLE_SOUND,
                USE_EXTERNAL_SOUND,
                EXIT_WORLD,
                ENABLE_GLOW
        );

    }

    public static class TropicalFish {

        public static final ConfigBooleanHotkeyed   ENABLE_TROPICAL_FISH_ALERTS =   new ConfigBooleanHotkeyed(  "enableTropicalFishAlerts", false, "").apply(TROPICAL_FISH_KEY);
        public static final ConfigOptionList        NOTIFY_MODE =                   new ConfigOptionList(       "notifyMode", MessageOutputType.MESSAGE).apply(TROPICAL_FISH_KEY);
        public static final ConfigBooleanHotkeyed   IGNORE_FROM_BUCKET =            new ConfigBooleanHotkeyed(  "ignoreFromBucket", true, "").apply(TROPICAL_FISH_KEY);
        public static final ConfigBooleanHotkeyed   ENABLE_GLOW =                   new ConfigBooleanHotkeyed(  "enableGlow", true, "").apply(TROPICAL_FISH_KEY);
        public static final ConfigColor             GLOW_COLOR =                    new ConfigColor(            "glowColor", "0xFFFFFF").apply(TROPICAL_FISH_KEY);

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                ENABLE_TROPICAL_FISH_ALERTS,
                NOTIFY_MODE,
                IGNORE_FROM_BUCKET,
                ENABLE_GLOW,
                GLOW_COLOR
        );

        public static final ImmutableList<IHotkeyTogglable> HOTKEYS = ImmutableList.of(
                ENABLE_TROPICAL_FISH_ALERTS,
                IGNORE_FROM_BUCKET,
                ENABLE_GLOW
        );

    }

    public static class TransferEnchanted {

        public static final ConfigHotkey            TRANSFER_HOTKEY =       new ConfigHotkey(           "transferHotkey", "", ANY).apply(TRANSFER_ENCHANTED_KEY);
        public static final ConfigOptionList        FILTER_MODE =           new ConfigOptionList(       "filterMode", EnchantmentsFilterMode.ALL).apply(TRANSFER_ENCHANTED_KEY);
        public static final ConfigHotkey            CYCLE_FILTER_MODE =     new ConfigHotkey(           "cycleFilterMode", "", ANY).apply(TRANSFER_ENCHANTED_KEY);
        public static final ConfigBooleanHotkeyed   ONLY_MAX_LEVEL =        new ConfigBooleanHotkeyed(  "onlyMaxLevel", true, "", ANY).apply(TRANSFER_ENCHANTED_KEY);
        public static final ConfigBooleanHotkeyed   EXCLUDE_CURSED =        new ConfigBooleanHotkeyed(  "excludeCursed", true, "", ANY).apply(TRANSFER_ENCHANTED_KEY);
        public static final ConfigBooleanHotkeyed   INVERT_FILTER =         new ConfigBooleanHotkeyed(  "invertFilter", false, "", ANY).apply(TRANSFER_ENCHANTED_KEY);
        public static final ConfigBooleanHotkeyed   TRANSFER_BOOK =         new ConfigBooleanHotkeyed(  "transferBook", true, "", ANY).apply(TRANSFER_ENCHANTED_KEY);
        public static final ConfigBooleanHotkeyed   TRANSFER_BOW =          new ConfigBooleanHotkeyed(  "transferBow", false, "", ANY).apply(TRANSFER_ENCHANTED_KEY);
        public static final ConfigBooleanHotkeyed   TRANSFER_FISHING_ROD =  new ConfigBooleanHotkeyed(  "transferFishingRod", false, "", ANY).apply(TRANSFER_ENCHANTED_KEY);
        public static final ConfigBooleanHotkeyed   TRANSFER_ALL =          new ConfigBooleanHotkeyed(  "transferAll", false, "", ANY).apply(TRANSFER_ENCHANTED_KEY);

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                TRANSFER_HOTKEY,
                FILTER_MODE,
                CYCLE_FILTER_MODE,
                ONLY_MAX_LEVEL,
                EXCLUDE_CURSED,
                INVERT_FILTER,
                TRANSFER_BOOK,
                TRANSFER_BOW,
                TRANSFER_FISHING_ROD,
                TRANSFER_ALL
        );

        public static final ImmutableList<IHotkey> HOTKEYS = ImmutableList.of(
                TRANSFER_HOTKEY,
                CYCLE_FILTER_MODE,
                ONLY_MAX_LEVEL,
                EXCLUDE_CURSED,
                INVERT_FILTER,
                TRANSFER_BOOK,
                TRANSFER_BOW,
                TRANSFER_FISHING_ROD,
                TRANSFER_ALL
        );

    }

    public static class Others {

        public static final ConfigHotkey            OPEN_CONFIG_GUI =               new ConfigHotkey(           "openConfigGui", "G,T", "").apply(OTHERS_KEY);
        public static final ConfigBooleanHotkeyed   REMIND_AFTER_PLAYER_RESPAWN =   new ConfigBooleanHotkeyed(  "remindAfterPlayerRespawn", false, "").apply(OTHERS_KEY);
        public static final ConfigOptionList        PLAYER_RESPAWN_REMIND_MODE =    new ConfigOptionList(       "playerRespawnRemindMode", MessageOutputType.MESSAGE).apply(OTHERS_KEY);
        public static final ConfigBooleanHotkeyed   TWEAKEROO_FAKE_SNEAK_MODIFY =   new ConfigBooleanHotkeyed(  "tweakerooFakeSneakModify", false, "").apply(OTHERS_KEY);

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                OPEN_CONFIG_GUI,
                REMIND_AFTER_PLAYER_RESPAWN,
                PLAYER_RESPAWN_REMIND_MODE,
                TWEAKEROO_FAKE_SNEAK_MODIFY
        );

        public static final ImmutableList<IHotkey> HOTKEYS = ImmutableList.of(
                OPEN_CONFIG_GUI,
                REMIND_AFTER_PLAYER_RESPAWN,
                TWEAKEROO_FAKE_SNEAK_MODIFY
        );

    }

    public static void loadFromFile() {
        Path configFile = FileUtils.getConfigDirectory().resolve(CONFIG_FILE_NAME);

        if (Files.exists(configFile) && Files.isReadable(configFile)) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "EntityAlert", Configs.EntityAlerts.OPTIONS);
                ConfigUtils.readConfigBase(root, "TropicalFish", Configs.TropicalFish.OPTIONS);
                ConfigUtils.readConfigBase(root, "TransferEnchanted", Configs.TransferEnchanted.OPTIONS);
                ConfigUtils.readConfigBase(root, "Others", Configs.Others.OPTIONS);
            }
        }
    }

    public static void saveToFile() {
        Path dir = FileUtils.getConfigDirectory();

        if (!Files.exists(dir)) {
            FileUtils.createDirectoriesIfMissing(dir);
        }

        if (Files.isDirectory(dir)) {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "EntityAlert", Configs.EntityAlerts.OPTIONS);
            ConfigUtils.writeConfigBase(root, "TropicalFish", Configs.TropicalFish.OPTIONS);
            ConfigUtils.writeConfigBase(root, "TransferEnchanted", Configs.TransferEnchanted.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Others", Configs.Others.OPTIONS);

            JsonUtils.writeJsonToFile(root, dir.resolve(CONFIG_FILE_NAME));
        }
    }

    @Override
    public void load()
    {
        loadFromFile();
    }

    @Override
    public void save()
    {
        saveToFile();
    }
}
