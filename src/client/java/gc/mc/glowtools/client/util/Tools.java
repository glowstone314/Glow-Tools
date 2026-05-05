package gc.mc.glowtools.client.util;

import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Vec3d;

import java.util.List;

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
    public static boolean containsEntityId(Entity entity, List<String> ids) {
        if (ids.isEmpty()) return false;
        for (String entityId : ids) {
            if (entityId.isEmpty()) continue;
            entityId = addDefaultPrefix(entityId);
            if (Registries.ENTITY_TYPE.getId(entity.getType()).toString().equalsIgnoreCase(entityId))
                return true;
        }
        return false;
    }
}
