package gc.mc.glowtools.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Tools {
    public static String formatPos(Vec3 pos) {
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
    public static List<Entity> getLoadedEntitiesAroundPlayer(Minecraft client) {
        if (client.level == null || client.player == null) return null;
        int renderDistance = client.options.renderDistance().get();
        double radius = renderDistance * 16.0;
        Vec3 pos = client.player.position();
        AABB searchArea = AABB.ofSize(pos, radius * 2, radius * 2, radius * 2);
        return client.level.getEntities(null, searchArea);
    }
}
