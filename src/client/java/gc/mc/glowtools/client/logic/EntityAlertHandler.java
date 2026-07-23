package gc.mc.glowtools.client.logic;

import fi.dy.masa.malilib.util.MessageOutputType;
import gc.mc.glowtools.client.Reference;
import gc.mc.glowtools.client.config.Configs;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static gc.mc.glowtools.client.util.Tools.containsEntityId;
import static gc.mc.glowtools.client.util.Tools.formatPos;

public class EntityAlertHandler {

    private static final Set<UUID> ALERTED_ENTITIES = new HashSet<>();

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                for (Entity entity : client.world.getEntities()) {
                    if (isTargetEntity(entity) && !ALERTED_ENTITIES.contains(entity.getUuid())) {
                        triggerAlert(entity);
                        ALERTED_ENTITIES.add(entity.getUuid());
                    }
                }
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ALERTED_ENTITIES.clear();
        });
    }

    public static boolean isTargetEntity(Entity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return false;
        if (!Configs.EntityAlerts.ENABLE_ALERTS.getBooleanValue()) return false;
        if (Configs.EntityAlerts.IGNORE_NAMED_ENTITIES.getBooleanValue() && entity.hasCustomName()) return false;

        if (entity instanceof WanderingTraderEntity && Configs.EntityAlerts.ENABLE_WANDERING_TRADER.getBooleanValue()) {
            return true;
        } else if (entity instanceof PillagerEntity && Configs.EntityAlerts.ENABLE_PILLAGER.getBooleanValue()) {
            return true;
        } else if (entity instanceof FoxEntity fox && Configs.EntityAlerts.ENABLE_FOX_EMERALD.getBooleanValue()) {
            return fox.getEquippedStack(EquipmentSlot.MAINHAND).isOf(Items.EMERALD);
        } else if (entity instanceof DrownedEntity drowned && Configs.EntityAlerts.ENABLE_DROWNED_SNIFFER.getBooleanValue()) {
            return drowned.getEquippedStack(EquipmentSlot.MAINHAND).isOf(Items.SNIFFER_EGG) || drowned.getEquippedStack(EquipmentSlot.OFFHAND).isOf(Items.SNIFFER_EGG);
        } else {
            if (Configs.EntityAlerts.ENABLE_CUSTOM_ENTITY.getBooleanValue()) {
                List<String> entityIds = Configs.EntityAlerts.CUSTOM_ENTITY_ID.getStrings();
                if (containsEntityId(entity, entityIds)) return true;
            }
        }
        return false;
    }

    private static void triggerAlert(Entity entity) {
        if (Configs.EntityAlerts.ENABLE_SOUND.getBooleanValue()) {
            AlertSoundManager.play();
        }
        MessageOutputType mode = (MessageOutputType) Configs.EntityAlerts.NOTIFY_MODE.getOptionListValue();
        if (mode == MessageOutputType.NONE) return;

        Text msg = Text.translatable("glowtools.chat.entity_alerts.found",
                Reference.chatPrefix, entity.getDisplayName(), formatPos(entity.getPos()));
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null) {
            client.player.sendMessage(msg, mode == MessageOutputType.ACTIONBAR);
        }

        if (Configs.EntityAlerts.EXIT_WORLD.getBooleanValue()) {
            client.execute(() -> {
                if (client.world != null) {
                    LocalTime currentTime = LocalTime.now();
                    Text title = Text.translatable("glowtools.chat.entity_alerts.disconnect_title");
                    Text reason =Text.literal("in " + currentTime.format(formatter) +
                            "\nat " + formatPos(entity.getPos()));

                    client.world.disconnect();
                    client.disconnect();
                    client.setScreen(new DisconnectedScreen(new TitleScreen(), title, reason));

                    Configs.EntityAlerts.EXIT_WORLD.setBooleanValue(false);
                    Configs.saveToFile();
                }
            });
        }
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

}