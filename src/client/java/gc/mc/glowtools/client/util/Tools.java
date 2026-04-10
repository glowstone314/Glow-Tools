package gc.mc.glowtools.client.util;

import net.minecraft.util.math.Vec3d;

public class Tools {
    public static String formatPos(Vec3d pos) {
        return String.format("%.1f, %.1f, %.1f", pos.x, pos.y, pos.z);
    }
    public static String formatPos(double x, double y, double z) {
        return String.format("%.1f, %.1f, %.1f", x, y, z);
    }
    public static String addDefaultPrefix(String id) {
        if (id.isEmpty()) return id;
        if (!id.contains(":")) id = "minecraft:" + id;
        return id;
    }
}
