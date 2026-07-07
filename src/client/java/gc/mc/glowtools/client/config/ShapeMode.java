package gc.mc.glowtools.client.config;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum ShapeMode implements IConfigOptionListEntry {
    LINE                ("line",
            "glowtools.config.schematic_generator.shape.line"),
    DIAGONAL            ("diagonal",
            "glowtools.config.schematic_generator.shape.diagonal"),
    CUBE                ("cube",
            "glowtools.config.schematic_generator.shape.cube"),
    ELLIPSOID           ("ellipsoid",
            "glowtools.config.schematic_generator.shape.ellipsoid"),
    ELLIPTICAL_CYLINDER ("ellipticalCylinder",
            "glowtools.config.schematic_generator.shape.ellipticalCylinder");

    private final String configString;
    private final String translationKey;

    ShapeMode(String configString, String translationKey) {
        this.configString = configString;
        this.translationKey = translationKey;
    }

    @Override
    public String getStringValue() {
        return this.configString;
    }

    @Override
    public String getDisplayName() {
        return StringUtils.translate(this.translationKey);
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        int id = this.ordinal();
        if (forward) {
            if (++id >= values().length) id = 0;
        } else {
            if (--id < 0) id = values().length - 1;
        }
        return values()[id % values().length];
    }

    @Override
    public ShapeMode fromString(String name) {
        for (ShapeMode mode : values()) {
            if (mode.configString.equalsIgnoreCase(name)) return mode;
        }
        return LINE;
    }
}