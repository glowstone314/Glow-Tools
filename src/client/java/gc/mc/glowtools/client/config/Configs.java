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
import gc.mc.glowtools.client.Reference;

import java.io.File;

public class Configs implements IConfigHandler {

    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static final KeybindSettings ANY = KeybindSettings.create(KeybindSettings.Context.ANY,
            KeyAction.PRESS, false, true, false, true);


    public static class EntityAlerts {

        public static final ConfigBooleanHotkeyed   ENABLE_ALERTS =             new ConfigBooleanHotkeyed(
                "Enable Entity Spawn Alerts", false, "",
                "Alert when specified entities spawn.");
        public static final ConfigBooleanHotkeyed   ENABLE_WANDERING_TRADER =   new ConfigBooleanHotkeyed(
                "Wandering Trader", true, "",
                "Alert when a Wandering Trader spawns.");
        public static final ConfigBooleanHotkeyed   ENABLE_PILLAGER =           new ConfigBooleanHotkeyed(
                "Pillager", false, "",
                "Alert when a Pillager spawns.");
        public static final ConfigBooleanHotkeyed   ENABLE_FOX_EMERALD =        new ConfigBooleanHotkeyed(
                "Fox with Emerald", false, "",
                "Alert when a Fox holding an Emerald spawns.");
        public static final ConfigBooleanHotkeyed   ENABLE_DROWNED_SNIFFER =    new ConfigBooleanHotkeyed(
                "Drowned with Sniffer Egg", false, "",
                "Alert when a Drowned holding a Sniffer Egg spawns.");
        public static final ConfigBooleanHotkeyed   ENABLE_CUSTOM_ENTITY =      new ConfigBooleanHotkeyed(
                "Custom Entity", false, "",
                "Alert when a custom entity spawns.");
        public static final ConfigStringList        CUSTOM_ENTITY_ID =          new ConfigStringList(
                "Custom Entity ID", ImmutableList.of(),
                "The ID of the custom entity to alert for.");
        public static final ConfigBooleanHotkeyed   IGNORE_NAMED_ENTITIES =     new ConfigBooleanHotkeyed(
                "Ignore Named Entities", true, "",
                "Do not alert if the entity has a custom name.");
        public static final ConfigBooleanHotkeyed   ENABLE_SOUND =              new ConfigBooleanHotkeyed(
                "Enable Sound Alert", true, "",
                "Play a sound alert when an entity spawns.");
        public static final ConfigBooleanHotkeyed   ENABLE_GLOW =               new ConfigBooleanHotkeyed(
                "Enable Glow Effect", true, "",
                "Give a glowing effect to entities that trigger the alert.");
        public static final ConfigBooleanHotkeyed   USE_EXTERNAL_SOUND =        new ConfigBooleanHotkeyed(
                "Use External Sound", false, "",
                "Whether to use an external sound file (bypasses Minecraft's volume sliders).");
        public static final ConfigBooleanHotkeyed   EXIT_WORLD =                new ConfigBooleanHotkeyed(
                "Auto Exit World", false, "",
                "Automatically disconnect/exit the world when a target entity spawns.\nAutomatically turn off this feature after triggering");
        public static final ConfigOptionList        NOTIFY_MODE =               new ConfigOptionList(
                "Notification Position", MessageOutputType.MESSAGE,
                "Where the entity spawn alerts should be displayed.");
        public static final ConfigInteger           SOUND_INTERVAL_MS =         new ConfigInteger(
                "Sound Interval (ms)", 300, 1, 600000,
                "Interval between multiple sound playbacks (in milliseconds).");
        public static final ConfigInteger           SOUND_COUNT =               new ConfigInteger(
                "Sound Count", 3, 1, 32,
                "How many times to play the sound when an entity spawns.");
        public static final ConfigString            CUSTOM_SOUND_ID =              new ConfigString(
                "Custom sound id", "entity.generic.explode",
                "Custom sound id when using internal sound");
        public static final ConfigString            CUSTOM_SOUND_PATH =              new ConfigString(
                "Custom sound path", "",
                "Custom .wav sound file path when using external sound");
        public static final ConfigColor             GLOW_COLOR =                new ConfigColor(
                "Glow Color", "0xFFFFFF",
                "The color used for the entity glow effect.");

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
                "Enable Rare Tropical Fish Alerts", false, "",
                "Alert when a rare tropical fish spawns.");
        public static final ConfigOptionList        NOTIFY_MODE =                   new ConfigOptionList(
                "Notification Position", MessageOutputType.MESSAGE,
                "Where the rare tropical fish alerts should be displayed.");
        public static final ConfigBooleanHotkeyed   IGNORE_FROM_BUCKET =            new ConfigBooleanHotkeyed(
                "Ignore Bucket Release", true, "",
                "Do not alert for tropical fish released from buckets.");
        public static final ConfigBooleanHotkeyed   ENABLE_GLOW =                   new ConfigBooleanHotkeyed(
                "Enable Glow Effect", true, "",
                "Give a glowing effect to rare tropical fish.");
        public static final ConfigColor             GLOW_COLOR =                    new ConfigColor(
                "Glow Color", "0xFFFFFF",
                "The color used for the rare tropical fish glow.");

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
                "Transfer Hotkey", "", ANY,
                "Hotkey to transfer matching items from container to player inventory.");
        public static final ConfigOptionList        FILTER_MODE =           new ConfigOptionList(
                "Filter Mode", EnchantmentsFilterMode.ALL,
                "The criteria used for filtering enchanted items.");
        public static final ConfigHotkey            CYCLE_FILTER_MODE =     new ConfigHotkey(
                "Cycle Filter Mode", "", ANY,
                "Hotkey to cycle through different enchantment filter modes.");
        public static final ConfigBooleanHotkeyed   ONLY_MAX_LEVEL =        new ConfigBooleanHotkeyed(
                "Max Level Only", true, "", ANY,
                "Only transfer items containing max-level enchantments (excluding Swift Sneak).",null);
        public static final ConfigBooleanHotkeyed   EXCLUDE_CURSED =        new ConfigBooleanHotkeyed(
                "Exclude Curses", true, "", ANY,
                "Do not transfer items with any cursed enchantments.", null);
        public static final ConfigBooleanHotkeyed   INVERT_FILTER =         new ConfigBooleanHotkeyed(
                "Invert Filter", false, "", ANY,
                "Inverts the selection criteria for item transfer.", null);
        public static final ConfigBooleanHotkeyed   TRANSFER_BOOK =         new ConfigBooleanHotkeyed(
                "Transfer Books", true, "", ANY,
                "Whether to transfer Enchanted Books.", null);
        public static final ConfigBooleanHotkeyed   TRANSFER_BOW =          new ConfigBooleanHotkeyed(
                "Transfer Bows", false, "", ANY,
                "Whether to transfer enchanted Bows.", null);
        public static final ConfigBooleanHotkeyed   TRANSFER_FISHING_ROD =  new ConfigBooleanHotkeyed(
                "Transfer Fishing Rods", false, "", ANY,
                "Whether to transfer enchanted Fishing Rods.", null);
        public static final ConfigBooleanHotkeyed   TRANSFER_ALL =          new ConfigBooleanHotkeyed(
                "Transfer All Types", false, "", ANY,
                "Transfer all enchanted items (Overrides specific item type settings).", null);

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
                "Open Config GUI", "G,T",
                "The key open the in-game config GUI");
        public static final ConfigBooleanHotkeyed   REMIND_AFTER_PLAYER_RESPAWN =   new ConfigBooleanHotkeyed(
                "Remind After Respawn", false, "",
                "Remind player to restock Ender Pearls after dying.");
        public static final ConfigOptionList        PLAYER_RESPAWN_REMIND_MODE =    new ConfigOptionList(
                "Respawn Notification Position", MessageOutputType.MESSAGE,
                "Where the respawn reminder should be displayed.");
        public static final ConfigBooleanHotkeyed   TWEAKEROO_FAKE_SNEAK_MODIFY =   new ConfigBooleanHotkeyed(
                "Modified Fake Sneak", false, "",
                "When the drop does not exceed 1.25 blocks, players are allowed to move forward.");

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
