package gc.mc.glowtools.client.config;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum EnchantmentsFilterMode implements IConfigOptionListEntry {
    ALL             ("all",             "glowtools.label.enchantments_filter_mode.all"),
    SWORD           ("sword",           "glowtools.label.enchantments_filter_mode.sword"),
    ARMOR           ("armor",           "glowtools.label.enchantments_filter_mode.armor"),
    SPECIAL_ARMOR   ("special_armor",   "glowtools.label.enchantments_filter_mode.special_armor"),
    BOW             ("bow",             "glowtools.label.enchantments_filter_mode.bow"),
    TOOL            ("tool",            "glowtools.label.enchantments_filter_mode.tool"),
    CROSSBOW        ("crossbow",        "glowtools.label.enchantments_filter_mode.crossbow"),
    TRIDENT         ("trident",         "glowtools.label.enchantments_filter_mode.trident"),
    FISHING         ("fishing",         "glowtools.label.enchantments_filter_mode.fishing"),
    MACE            ("mace",            "glowtools.label.enchantments_filter_mode.mace"),;

    private final String configString;
    private final String translationKey;

    EnchantmentsFilterMode(String configString, String translationKey) {
        this.configString = configString;
        this.translationKey = translationKey;
    }

    @Override
    public String getStringValue() { return this.configString; }

    @Override
    public String getDisplayName() { return StringUtils.translate(this.translationKey); }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        int id = this.ordinal();
        if (forward) {
            if (++id >= values().length) {
                id = 0;
            }
        } else {
            if (--id < 0) {
                id = values().length - 1;
            }
        }

        return values()[id % values().length];
    }

    @Override
    public IConfigOptionListEntry fromString(String value) {
        for (EnchantmentsFilterMode mode : values()) {
            if (mode.configString.equalsIgnoreCase(value)) return mode;
        }
        return ALL;
    }
}