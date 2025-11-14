package net.lordprinz.bloodharbourripper.client;

import net.lordprinz.bloodharbourripper.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "bloodharbourripper", value = Dist.CLIENT)
public class ExecuteHighlightRenderer {
    private static final double EXECUTE_RANGE = 12.0;
    private static java.util.Set<java.util.UUID> previouslyGlowing = new java.util.HashSet<>();

    private static boolean isAlly(Player player, LivingEntity target) {
        if (!(target instanceof Player targetPlayer)) {
        } else {
            if (player.getTeam() != null && player.getTeam().equals(targetPlayer.getTeam())) {
                return true;
            }
        }

        if (target instanceof net.minecraft.world.entity.TamableAnimal tamable) {
            if (tamable.isTame() && tamable.getOwner() != null) {
                if (tamable.getOwner().equals(player)) {
                    return true;
                }
                if (player.getTeam() != null && tamable.getOwner() instanceof Player owner) {
                    if (player.getTeam().equals(owner.getTeam())) {
                        return true;
                    }
                }
            }
        }

        if (player.getTeam() != null && target.getTeam() != null) {
            if (player.getTeam().equals(target.getTeam())) {
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        Player player = mc.player;

        if (player.getMainHandItem().getItem() != ModItems.BONE_SKEWER.get()) {
            for (java.util.UUID uuid : previouslyGlowing) {
                for (var entity : mc.level.entitiesForRendering()) {
                    if (entity.getUUID().equals(uuid) && entity instanceof LivingEntity living) {
                        if (living.hasGlowingTag()) {
                            living.setGlowingTag(false);
                        }
                        break;
                    }
                }
            }
            previouslyGlowing.clear();
            return;
        }

        AABB searchBox = player.getBoundingBox().inflate(EXECUTE_RANGE);
        List<LivingEntity> nearbyEntities = mc.level.getEntitiesOfClass(LivingEntity.class, searchBox,
            e -> e != player && e.isAlive());

        java.util.Set<java.util.UUID> currentGlowing = new java.util.HashSet<>();

        for (LivingEntity target : nearbyEntities) {
            double distance = player.distanceTo(target);

            if (distance <= EXECUTE_RANGE && !isAlly(player, target)) {
                float healthPercentage = target.getHealth() / target.getMaxHealth();

                if (healthPercentage < 0.33f) {
                    if (!target.hasGlowingTag()) {
                        target.setGlowingTag(true);
                    }
                    currentGlowing.add(target.getUUID());
                } else {
                    if (target.hasGlowingTag()) {
                        target.setGlowingTag(false);
                    }
                }
            } else {
                if (target.hasGlowingTag()) {
                    target.setGlowingTag(false);
                }
            }
        }

        for (java.util.UUID uuid : previouslyGlowing) {
            if (!currentGlowing.contains(uuid)) {
                for (var entity : mc.level.entitiesForRendering()) {
                    if (entity.getUUID().equals(uuid) && entity instanceof LivingEntity living) {
                        if (living.hasGlowingTag()) {
                            living.setGlowingTag(false);
                        }
                        break;
                    }
                }
            }
        }

        previouslyGlowing = currentGlowing;
    }
}

