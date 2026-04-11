package gc.mc.glowtools.client.logic;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.MessageOutputType;
import gc.mc.glowtools.client.Reference;
import gc.mc.glowtools.client.config.Configs;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public class OthersHandler {

    private static boolean pendingPearlReminder = false;

    public static void init() {
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (!Configs.Others.REMIND_AFTER_PLAYER_RESPAWN.getBooleanValue()) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            if (message.getContent() instanceof TranslatableTextContent translatable) {
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
                client.player.sendMessage(Text.translatable("glowtools.chat.others.remindAfterPlayerRespawn",
                        Reference.chatPrefix), remindMode.equals(MessageOutputType.ACTIONBAR));
            }
        });
    }

}
