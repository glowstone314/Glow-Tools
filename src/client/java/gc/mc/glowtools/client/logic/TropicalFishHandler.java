package gc.mc.glowtools.client.logic;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.MessageOutputType;
import gc.mc.glowtools.client.Reference;
import gc.mc.glowtools.client.config.Configs;
import gc.mc.glowtools.client.mixin.TropicalFishAccessor;
import gc.mc.glowtools.client.util.RareFishHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.fish.TropicalFish;

import java.util.*;

import static gc.mc.glowtools.client.util.Tools.formatPos;
import static gc.mc.glowtools.client.util.Tools.getLoadedEntitiesAroundPlayer;

public class TropicalFishHandler {

    private static final LinkedHashMap<UUID, Integer> NOTIFIED = new LinkedHashMap<>();

    private static final Map<UUID, Integer> LAST_SEEN = new HashMap<>();

    private static int clientTick = 0;

    private static final int DESPAWN_TTL = 200;
    private static final int MAX_NOTIFIED_ENTRIES = 512;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!Configs.TropicalFish.ENABLE_TROPICAL_FISH_ALERTS.getBooleanValue()) return;
            if (client.level == null || client.player == null) return;

            clientTick++;

            List<Entity> entities = getLoadedEntitiesAroundPlayer(client);
            if (entities == null) return; //todo
            for (Entity entity : entities) {
                if (entity instanceof TropicalFish fish) {
                    if (isTargetFish(fish)) {
                        UUID uuid = fish.getUUID();

                        LAST_SEEN.put(uuid, clientTick);

                        if (Configs.TropicalFish.NOTIFY_MODE.getOptionListValue() != MessageOutputType.NONE) {
                            if (!NOTIFIED.containsKey(uuid)) {
                                triggerAlert(fish);
                                NOTIFIED.put(uuid, clientTick);
                            }
                        }
                    }
                }
            }

            Iterator<Map.Entry<UUID, Integer>> lastSeenIt = LAST_SEEN.entrySet().iterator();
            while (lastSeenIt.hasNext()) {
                Map.Entry<UUID, Integer> e = lastSeenIt.next();
                if (clientTick - e.getValue() > DESPAWN_TTL) {
                    UUID uuid = e.getKey();
                    lastSeenIt.remove();
                    NOTIFIED.remove(uuid);
                }
            }

            if (NOTIFIED.size() > MAX_NOTIFIED_ENTRIES) {
                Iterator<UUID> it = NOTIFIED.keySet().iterator();
                while (NOTIFIED.size() > MAX_NOTIFIED_ENTRIES && it.hasNext()) {
                    it.next();
                    it.remove();
                }
            }
        });
    }

    private static void triggerAlert(TropicalFish fish) {
        MessageOutputType mode = (MessageOutputType) Configs.TropicalFish.NOTIFY_MODE.getOptionListValue();
        if (mode == MessageOutputType.NONE) return;

        int variantId = ((TropicalFishAccessor) fish).invokeGetPackedVariant();
        String variantName = GuiBase.TXT_GREEN + RareFishHelper.getTropicalFishVariantName(variantId).getString();
        Component msg = Component.translatable("glowtools.chat.tropical_fish.found", Reference.chatPrefix, variantName, formatPos(fish.getX(), fish.getY(), fish.getZ()));
        Minecraft client = Minecraft.getInstance();

        if (client.player != null) {
            if (mode == MessageOutputType.ACTIONBAR)
                client.player.sendOverlayMessage(msg);
            else
                client.player.sendSystemMessage(msg);
        }
    }

    public static boolean isTargetFish(TropicalFish fish) {
        if (!Configs.TropicalFish.ENABLE_TROPICAL_FISH_ALERTS.getBooleanValue()) return false;
        if (Configs.TropicalFish.IGNORE_FROM_BUCKET.getBooleanValue() && fish.fromBucket()) return false;
        int variantId = ((TropicalFishAccessor) fish).invokeGetPackedVariant();
        return RareFishHelper.isRareTropicalFish(variantId);
    }

}
