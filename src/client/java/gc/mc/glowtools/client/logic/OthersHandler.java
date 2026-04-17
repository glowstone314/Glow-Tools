package gc.mc.glowtools.client.logic;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.MessageOutputType;
import gc.mc.glowtools.client.Reference;
import gc.mc.glowtools.client.config.Configs;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

public class OthersHandler {

    private static boolean pendingPearlReminder = false;

    public static void init() {
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (!Configs.Others.REMIND_AFTER_PLAYER_RESPAWN.getBooleanValue()) return;

            Minecraft client = Minecraft.getInstance();
            if (client.player == null) return;

            if (message.getContents() instanceof TranslatableContents translatable) {
                String key = translatable.getKey();

                if (key.startsWith("death.") && message.getString().contains(client.player.getName().getString())) {
                    pendingPearlReminder = true;
                }
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (pendingPearlReminder && client.player != null && client.player.isAlive() && client.player.getHealth() > 0) {
                pendingPearlReminder = false;
                IConfigOptionListEntry remindMode = Configs.Others.PLAYER_RESPAWN_REMIND_MODE.getOptionListValue();
                if (!Configs.Others.REMIND_AFTER_PLAYER_RESPAWN.getBooleanValue() ||
                        remindMode.equals(MessageOutputType.NONE)) return;
                if (remindMode.equals(MessageOutputType.ACTIONBAR))
                    client.player.sendOverlayMessage(Component.translatable(
                            "glowtools.chat.others.remindAfterPlayerRespawn", Reference.chatPrefix));
                else
                    client.player.sendSystemMessage(Component.translatable(
                            "glowtools.chat.others.remindAfterPlayerRespawn", Reference.chatPrefix));
            }
        });
    }

}
