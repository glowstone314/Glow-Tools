package gc.mc.glowtools.client.logic;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.MessageOutputType;
import gc.mc.glowtools.client.Reference;
import gc.mc.glowtools.client.config.Configs;
import gc.mc.glowtools.client.mixin.TropicalFishEntityAccessor;
import gc.mc.glowtools.client.util.RareFishHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.text.Text;

import java.util.*;

import static gc.mc.glowtools.client.util.Tools.formatPos;

public class TropicalFishHandler {

    private static final LinkedHashMap<UUID, Integer> NOTIFIED = new LinkedHashMap<>();

    private static final Map<UUID, Integer> LAST_SEEN = new HashMap<>();

    private static int clientTick = 0;

    private static final int DESPAWN_TTL = 200;
    private static final int MAX_NOTIFIED_ENTRIES = 512;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!Configs.TropicalFish.ENABLE_TROPICAL_FISH_ALERTS.getBooleanValue()) return;
            if (client.world == null || client.player == null) return;

            clientTick++;

            for (Entity entity : client.world.getEntities()) {
                if (entity instanceof TropicalFishEntity fish) {
                    if (isTargetFish(fish)) {
                        UUID uuid = fish.getUuid();

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

    private static void triggerAlert(TropicalFishEntity fish) {
        MessageOutputType mode = (MessageOutputType) Configs.TropicalFish.NOTIFY_MODE.getOptionListValue();
        if (mode == MessageOutputType.NONE) return;

        int variantId = ((TropicalFishEntityAccessor) fish).invokeGetTropicalFishVariant();
        String variantName = GuiBase.TXT_GREEN + RareFishHelper.getTropicalFishVariantName(variantId).getString();
        Text msg = Text.translatable("glowtools.chat.tropical_fish.found", Reference.chatPrefix, variantName, formatPos(fish.getX(), fish.getY(), fish.getZ()));
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null) {
            client.player.sendMessage(msg, mode == MessageOutputType.ACTIONBAR);
        }
    }

    public static boolean isTargetFish(TropicalFishEntity fish) {
        if (!Configs.TropicalFish.ENABLE_TROPICAL_FISH_ALERTS.getBooleanValue()) return false;
        if (Configs.TropicalFish.IGNORE_FROM_BUCKET.getBooleanValue() && fish.isFromBucket()) return false;
        int variantId = ((TropicalFishEntityAccessor) fish).invokeGetTropicalFishVariant();
        return RareFishHelper.isRareTropicalFish(variantId);
    }

}
