package net.lordprinz.bloodharbourripper.client;

import net.lordprinz.bloodharbourripper.item.ModItems;
import net.lordprinz.bloodharbourripper.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "bloodharbourripper", value = Dist.CLIENT)
public class BoneSkewerChargingSoundHandler {
    private static final Map<UUID, Boolean> wasCharging = new HashMap<>();
    private static final Map<UUID, ChargingSoundInstance> activeSounds = new HashMap<>();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            if (player == null) return;

            boolean isChargingNow = player.isUsingItem() && (
                player.getUseItem().getItem() == ModItems.BONE_SKEWER_SKELETON.get() ||
                player.getUseItem().getItem() == ModItems.BONE_SKEWER_RAW.get() ||
                player.getUseItem().getItem() == ModItems.BONE_SKEWER.get()
            );

            UUID playerUUID = player.getUUID();
            Boolean wasChargingBefore = wasCharging.getOrDefault(playerUUID, false);

            if (isChargingNow && !wasChargingBefore) {
                startChargingSound(player);
            } else if (!isChargingNow && wasChargingBefore) {
                stopChargingSound(playerUUID);
            }

            wasCharging.put(playerUUID, isChargingNow);
        }
    }

    private static void startChargingSound(Player player) {
        Minecraft mc = Minecraft.getInstance();
        UUID playerUUID = player.getUUID();

        stopChargingSound(playerUUID);

        ChargingSoundInstance sound = new ChargingSoundInstance(player);
        activeSounds.put(playerUUID, sound);
        mc.getSoundManager().play(sound);
    }

    private static void stopChargingSound(UUID playerUUID) {
        ChargingSoundInstance sound = activeSounds.remove(playerUUID);
        if (sound != null) {
            sound.stop();
        }
    }

    public static void cleanup() {
        wasCharging.clear();
        for (ChargingSoundInstance sound : activeSounds.values()) {
            sound.stop();
        }
        activeSounds.clear();
    }

    private static class ChargingSoundInstance extends AbstractSoundInstance {
        private boolean stopped = false;

        public ChargingSoundInstance(Player player) {
            super(
                ModSounds.BONE_SKEWER_CHARGING.get(),
                SoundSource.PLAYERS,
                SoundInstance.createUnseededRandom()
            );
            this.looping = true;
            this.delay = 0;
            this.volume = 0.7F;
            this.pitch = 1.0F;
            this.x = player.getX();
            this.y = player.getY();
            this.z = player.getZ();
            this.relative = false;
            this.attenuation = Attenuation.LINEAR;
        }

        public void stop() {
            this.stopped = true;
            this.looping = false;
            Minecraft.getInstance().getSoundManager().stop(this);
        }
    }
}

