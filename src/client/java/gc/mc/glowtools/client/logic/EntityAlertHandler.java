package gc.mc.glowtools.client.logic;

import fi.dy.masa.malilib.util.MessageOutputType;
import gc.mc.glowtools.client.Reference;
import gc.mc.glowtools.client.config.Configs;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.Items;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static gc.mc.glowtools.client.util.Tools.*;

public class EntityAlertHandler {

    private static final Set<UUID> ALERTED_ENTITIES = new HashSet<>();

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (Configs.EntityAlerts.ENABLE_ALERTS.getBooleanValue() && client.player != null && client.level != null) {
                List<Entity> entities = getLoadedEntitiesAroundPlayer(client);
                if (entities == null) return;
                for (Entity entity : entities) {
                    if (isTargetEntity(entity) && !ALERTED_ENTITIES.contains(entity.getUUID())) {
                        triggerAlert(entity);
                        ALERTED_ENTITIES.add(entity.getUUID());
                    }
                }
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ALERTED_ENTITIES.clear());
    }

    public static boolean isTargetEntity(Entity entity) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return false;
        if (Configs.EntityAlerts.IGNORE_NAMED_ENTITIES.getBooleanValue() && entity.hasCustomName()) return false;

        switch (entity) {
            case WanderingTrader ignored when Configs.EntityAlerts.ENABLE_WANDERING_TRADER.getBooleanValue() -> {
                return true;
            }
            case Pillager ignored when Configs.EntityAlerts.ENABLE_PILLAGER.getBooleanValue() -> {
                return true;
            }
            case Fox fox when Configs.EntityAlerts.ENABLE_FOX_EMERALD.getBooleanValue() -> {
                return fox.getMainHandItem().is(Items.EMERALD);
            }
            case Drowned drowned when Configs.EntityAlerts.ENABLE_DROWNED_SNIFFER.getBooleanValue() -> {
                return drowned.getMainHandItem().is(Items.SNIFFER_EGG) || drowned.getOffhandItem().is(Items.SNIFFER_EGG);
            }
            default -> {
                if (Configs.EntityAlerts.ENABLE_CUSTOM_ENTITY.getBooleanValue()) {
                    String entityId = Configs.EntityAlerts.CUSTOM_ENTITY_ID.getStringValue();
                    if (entityId.isEmpty()) return false;
                    entityId = addDefaultPrefix(entityId);
                    Identifier identifier = Identifier.parse(entityId);
                    return Objects.equals(client.level.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE).getValue(identifier), entity.getType());
                }
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

        Component msg = Component.translatable("glowtools.chat.entity_alerts.found",
                Reference.chatPrefix, entity.getDisplayName(), formatPos(entity.getX(), entity.getY(), entity.getZ()));
        Minecraft client = Minecraft.getInstance();

        if (client.player != null) {
            if (mode == MessageOutputType.ACTIONBAR)
                client.player.sendOverlayMessage(msg);
            else
                client.player.sendSystemMessage(msg);
        }

        if (Configs.EntityAlerts.EXIT_WORLD.getBooleanValue()) {
            client.execute(() -> {
                if (client.level != null) {
                    LocalTime currentTime = LocalTime.now();
                    Component title = Component.translatable("glowtools.chat.entity_alerts.disconnect_title");
                    Component reason = Component.literal("in " + currentTime.format(formatter) + "\nat " + formatPos(entity.getX(), entity.getY(), entity.getZ()));

                    client.level.disconnect(Component.empty());
                    client.disconnect(new DisconnectedScreen(new TitleScreen(), title, reason), true);
                    //client.setScreen();
                }
            });
        }
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

}