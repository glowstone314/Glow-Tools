package gc.mc.glowtools.client.logic;

import gc.mc.glowtools.client.config.Configs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import javax.sound.sampled.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlertSoundManager {

    private static final ExecutorService SOUND_EXECUTOR = Executors.newSingleThreadExecutor();

    public static void play() {
        SOUND_EXECUTOR.submit(() -> {
            int count = Configs.EntityAlerts.SOUND_COUNT.getIntegerValue();
            int interval = Configs.EntityAlerts.SOUND_INTERVAL_MS.getIntegerValue();
            boolean isExternal = Configs.EntityAlerts.USE_EXTERNAL_SOUND.getBooleanValue();

            for (int i = 0; i < count; i++) {
                if (isExternal) {
                    playExternal(Configs.EntityAlerts.CUSTOM_SOUND_PATH.getStringValue());
                } else {
                    playInternal(Configs.EntityAlerts.CUSTOM_SOUND_ID.getStringValue());
                }

                if (i < count - 1 && interval > 0) {
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
    }

    private static void playInternal(String id) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        Identifier soundId = Identifier.of(id);
        SoundEvent event = Registries.SOUND_EVENT.get(soundId);
        if (event != null) {
            client.execute(() -> client.getSoundManager().play(PositionedSoundInstance.master(event, 1.0f)));
        }
    }

    private static void playExternal(String path) {
        if (path.startsWith("\"") && path.endsWith("\"")) {
            path = path.substring(1, path.length() - 1);
        }
        try {
            File soundFile = new File(path);
            if (!soundFile.exists()) return;

            try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("Failed to play external sound: " + e.getMessage());
        }
    }
}