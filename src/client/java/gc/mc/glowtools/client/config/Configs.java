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
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.MessageOutputType;
import fi.dy.masa.malilib.util.StringUtils;
import gc.mc.glowtools.client.Reference;

import java.io.File;

public class Configs implements IConfigHandler {

    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static final KeybindSettings ANY = KeybindSettings.create(KeybindSettings.Context.ANY,
            KeyAction.PRESS, false, true, false, true);


    public static class EntityAlerts {

        public static final ConfigBooleanHotkeyed   ENABLE_ALERTS =             new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.entity_alerts.name.enableAlerts"), false, "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.enableAlerts"));
        public static final ConfigBooleanHotkeyed   ENABLE_WANDERING_TRADER =   new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.entity_alerts.name.enableWanderingTrader"), true, "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.enableWanderingTrader"));
        public static final ConfigBooleanHotkeyed   ENABLE_PILLAGER =           new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.entity_alerts.name.enablePillager"), false, "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.enablePillager"));
        public static final ConfigBooleanHotkeyed   ENABLE_FOX_EMERALD =        new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.entity_alerts.name.enableFoxEmerald"), false, "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.enableFoxEmerald"));
        public static final ConfigBooleanHotkeyed   ENABLE_DROWNED_SNIFFER =    new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.entity_alerts.name.enableDrownedSniffer"), false, "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.enableDrownedSniffer"));
        public static final ConfigBooleanHotkeyed   ENABLE_CUSTOM_ENTITY =      new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.entity_alerts.name.enableCustomEntity"), false, "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.enableCustomEntity"));
        public static final ConfigStringList        CUSTOM_ENTITY_ID =          new ConfigStringList(
                StringUtils.translate("glowtools.config.entity_alerts.name.customEntityId"), ImmutableList.of(),
                StringUtils.translate("glowtools.config.entity_alerts.comment.customEntityId"));
        public static final ConfigBooleanHotkeyed   IGNORE_NAMED_ENTITIES =     new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.entity_alerts.name.ignoreNamedEntities"), true, "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.ignoreNamedEntities"));
        public static final ConfigBooleanHotkeyed   ENABLE_SOUND =              new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.entity_alerts.name.enableSound"), true, "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.enableSound"));
        public static final ConfigBooleanHotkeyed   ENABLE_GLOW =               new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.entity_alerts.name.enableGlow"), true, "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.enableGlow"));
        public static final ConfigBooleanHotkeyed   USE_EXTERNAL_SOUND =        new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.entity_alerts.name.useExternalSound"), false, "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.useExternalSound"));
        public static final ConfigBooleanHotkeyed   EXIT_WORLD =                new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.entity_alerts.name.exitWorld"), false, "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.exitWorld"));
        public static final ConfigOptionList        NOTIFY_MODE =               new ConfigOptionList(
                StringUtils.translate("glowtools.config.entity_alerts.name.notifyMode"), MessageOutputType.MESSAGE,
                StringUtils.translate("glowtools.config.entity_alerts.comment.notifyMode"));
        public static final ConfigInteger           SOUND_INTERVAL_MS =         new ConfigInteger(
                StringUtils.translate("glowtools.config.entity_alerts.name.soundIntervalMs"), 300, 1, 600000,
                StringUtils.translate("glowtools.config.entity_alerts.comment.soundIntervalMs"));
        public static final ConfigInteger           SOUND_COUNT =               new ConfigInteger(
                StringUtils.translate("glowtools.config.entity_alerts.name.soundCount"), 3, 1, 32,
                StringUtils.translate("glowtools.config.entity_alerts.comment.soundCount"));
        public static final ConfigString            CUSTOM_SOUND_ID =              new ConfigString(
                StringUtils.translate("glowtools.config.entity_alerts.name.customSoundId"), "entity.generic.explode",
                StringUtils.translate("glowtools.config.entity_alerts.comment.customSoundId"));
        public static final ConfigString            CUSTOM_SOUND_PATH =              new ConfigString(
                StringUtils.translate("glowtools.config.entity_alerts.name.customSoundPath"), "",
                StringUtils.translate("glowtools.config.entity_alerts.comment.customSoundPath"));
        public static final ConfigColor             GLOW_COLOR =                new ConfigColor(
                StringUtils.translate("glowtools.config.entity_alerts.name.glowColor"), "0xFFFFFF",
                StringUtils.translate("glowtools.config.entity_alerts.comment.glowColor"));

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
                CUSTOM_SOUND_ID,
                CUSTOM_SOUND_PATH
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

        public static final ConfigBooleanHotkeyed   ENABLE_TROPICAL_FISH_ALERTS =   new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.tropical_fish.name.enableTropicalFishAlerts"), false, "",
                StringUtils.translate("glowtools.config.tropical_fish.comment.enableTropicalFishAlerts"));
        public static final ConfigOptionList        NOTIFY_MODE =                   new ConfigOptionList(
                StringUtils.translate("glowtools.config.tropical_fish.name.notifyMode"), MessageOutputType.MESSAGE,
                StringUtils.translate("glowtools.config.tropical_fish.comment.notifyMode"));
        public static final ConfigBooleanHotkeyed   IGNORE_FROM_BUCKET =            new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.tropical_fish.name.ignoreFromBucket"), true, "",
                StringUtils.translate("glowtools.config.tropical_fish.comment.ignoreFromBucket"));
        public static final ConfigBooleanHotkeyed   ENABLE_GLOW =                   new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.tropical_fish.name.enableGlow"), true, "",
                StringUtils.translate("glowtools.config.tropical_fish.comment.enableGlow"));
        public static final ConfigColor             GLOW_COLOR =                    new ConfigColor(
                StringUtils.translate("glowtools.config.tropical_fish.name.glowColor"), "0xFFFFFF",
                StringUtils.translate("glowtools.config.tropical_fish.comment.glowColor"));

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

        public static final ConfigHotkey            TRANSFER_HOTKEY =       new ConfigHotkey(
                StringUtils.translate("glowtools.config.transfer_enchanted.name.transferHotkey"), "", ANY,
                StringUtils.translate("glowtools.config.transfer_enchanted.comment.transferHotkey"));
        public static final ConfigOptionList        FILTER_MODE =           new ConfigOptionList(
                StringUtils.translate("glowtools.config.transfer_enchanted.name.filterMode"), EnchantmentsFilterMode.ALL,
                StringUtils.translate("glowtools.config.transfer_enchanted.comment.filterMode"));
        public static final ConfigHotkey            CYCLE_FILTER_MODE =     new ConfigHotkey(
                StringUtils.translate("glowtools.config.transfer_enchanted.name.cycleFilterMode"), "", ANY,
                StringUtils.translate("glowtools.config.transfer_enchanted.comment.cycleFilterMode"));
        public static final ConfigBooleanHotkeyed   ONLY_MAX_LEVEL =        new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.transfer_enchanted.name.onlyMaxLevel"), true, "", ANY, null,
                StringUtils.translate("glowtools.config.transfer_enchanted.comment.onlyMaxLevel"));
        public static final ConfigBooleanHotkeyed   EXCLUDE_CURSED =        new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.transfer_enchanted.name.excludeCursed"), true, "", ANY, null,
                StringUtils.translate("glowtools.config.transfer_enchanted.comment.excludeCursed"));
        public static final ConfigBooleanHotkeyed   INVERT_FILTER =         new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.transfer_enchanted.name.invertFilter"), false, "", ANY, null,
                StringUtils.translate("glowtools.config.transfer_enchanted.comment.invertFilter"));
        public static final ConfigBooleanHotkeyed   TRANSFER_BOOK =         new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.transfer_enchanted.name.transferBook"), true, "", ANY, null,
                StringUtils.translate("glowtools.config.transfer_enchanted.comment.transferBook"));
        public static final ConfigBooleanHotkeyed   TRANSFER_BOW =          new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.transfer_enchanted.name.transferBow"), false, "", ANY, null,
                StringUtils.translate("glowtools.config.transfer_enchanted.comment.transferBow"));
        public static final ConfigBooleanHotkeyed   TRANSFER_FISHING_ROD =  new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.transfer_enchanted.name.transferFishingRod"), false, "", ANY, null,
                StringUtils.translate("glowtools.config.transfer_enchanted.comment.transferFishingRod"));
        public static final ConfigBooleanHotkeyed   TRANSFER_ALL =          new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.transfer_enchanted.name.transferAll"), false, "", ANY, null,
                StringUtils.translate("glowtools.config.transfer_enchanted.comment.transferAll"));

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

        public static final ConfigHotkey            OPEN_CONFIG_GUI =              new ConfigHotkey(
                StringUtils.translate("glowtools.config.others.name.openConfigGui"), "G,T",
                StringUtils.translate("glowtools.config.others.comment.openConfigGui"));
        public static final ConfigBooleanHotkeyed   REMIND_AFTER_PLAYER_RESPAWN =   new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.others.name.remindAfterPlayerRespawn"), false, "",
                StringUtils.translate("glowtools.config.others.comment.remindAfterPlayerRespawn"));
        public static final ConfigOptionList        PLAYER_RESPAWN_REMIND_MODE =    new ConfigOptionList(
                StringUtils.translate("glowtools.config.others.name.playerRespawnRemindMode"), MessageOutputType.MESSAGE,
                StringUtils.translate("glowtools.config.others.comment.playerRespawnRemindMode"));
        public static final ConfigBooleanHotkeyed   TWEAKEROO_FAKE_SNEAK_MODIFY =   new ConfigBooleanHotkeyed(
                StringUtils.translate("glowtools.config.others.name.tweakerooFakeSneakModify"), false, "",
                StringUtils.translate("glowtools.config.others.comment.tweakerooFakeSneakModify"));

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
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
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
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "EntityAlert", Configs.EntityAlerts.OPTIONS);
            ConfigUtils.writeConfigBase(root, "TropicalFish", Configs.TropicalFish.OPTIONS);
            ConfigUtils.writeConfigBase(root, "TransferEnchanted", Configs.TransferEnchanted.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Others", Configs.Others.OPTIONS);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
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
