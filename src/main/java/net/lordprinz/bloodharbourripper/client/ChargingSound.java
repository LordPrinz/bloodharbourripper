package net.lordprinz.bloodharbourripper.client;

import net.lordprinz.bloodharbourripper.item.BoneSkewerItem;
import net.lordprinz.bloodharbourripper.registry.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

/** Looping "charging" sound that follows the player and stops by itself when they stop charging. */
final class ChargingSound extends AbstractTickableSoundInstance {
    private final Player player;

    ChargingSound(Player player) {
        super(ModSounds.BONE_SKEWER_CHARGING.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.7F;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    static boolean isCharging(Player player) {
        return player.isUsingItem() && player.getUseItem().getItem() instanceof BoneSkewerItem;
    }

    @Override
    public void tick() {
        if (player.isRemoved() || !isCharging(player)) {
            this.stop();
            return;
        }
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }
}
