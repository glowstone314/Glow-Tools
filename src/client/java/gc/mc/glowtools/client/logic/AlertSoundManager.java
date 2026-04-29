package gc.mc.glowtools.client.logic;

import gc.mc.glowtools.client.config.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

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
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        Identifier soundId = Identifier.parse(id);
        SoundEvent event = client.level.registryAccess().lookupOrThrow(Registries.SOUND_EVENT).getValue(soundId);
        if (event != null) {
            client.execute(() -> client.getSoundManager().play(SimpleSoundInstance.forUI(event, 1.0f)));
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