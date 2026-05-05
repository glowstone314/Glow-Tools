package gc.mc.glowtools.client.util;

import gc.mc.glowtools.client.config.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

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
    public static boolean containsEntityId(Entity entity, List<String> ids, Minecraft client) {
        if (ids.isEmpty()) return false;
        for (String entityId : ids) {
            entityId = addDefaultPrefix(entityId);
            Identifier identifier = Identifier.parse(entityId);
            if (Objects.equals(client.level.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE).getValue(identifier), entity.getType()))
                return true;
        }
        return false;
    }
}
