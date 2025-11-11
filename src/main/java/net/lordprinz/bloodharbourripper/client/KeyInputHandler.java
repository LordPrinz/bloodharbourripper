package net.lordprinz.bloodharbourripper.client;

import net.lordprinz.bloodharbourripper.item.ModItems;
import net.lordprinz.bloodharbourripper.network.ExecuteEntityPacket;
import net.lordprinz.bloodharbourripper.network.ModNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "bloodharbourripper", value = Dist.CLIENT)
public class KeyInputHandler {
    private static boolean wasPressed = false;
    private static final double EXECUTE_RANGE = 12.0;
    private static long currentTick = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            currentTick++;

            ExecuteMarkerRenderer.tickMarkers();

            Minecraft mc = Minecraft.getInstance();

            if (mc.player == null || mc.level == null) return;

            boolean isPressed = KeyBindings.EXECUTE_KEY.isDown();

            if (isPressed && !wasPressed) {
                Player player = mc.player;

                if (player.getMainHandItem().getItem() == ModItems.BONE_SKEWER.get() && player.isShiftKeyDown()) {

                    if (!ExecuteCooldownTracker.canExecute(player.getUUID(), currentTick)) {
                        long remainingTicks = ExecuteCooldownTracker.getRemainingCooldown(player.getUUID(), currentTick);
                        double remainingSeconds = remainingTicks / 20.0;
                        player.displayClientMessage(
                            Component.literal(String.format("§cExecute on cooldown: %.1fs", remainingSeconds)),
                            true
                        );
                        wasPressed = isPressed;
                        return;
                    }

                    // Znajdź najbliższego moba z <33% HP w zasięgu
                    AABB searchBox = player.getBoundingBox().inflate(EXECUTE_RANGE);
                    List<LivingEntity> nearbyEntities = mc.level.getEntitiesOfClass(LivingEntity.class, searchBox,
                        e -> e != player && e.isAlive());

                    LivingEntity closestTarget = null;
                    double closestDistance = EXECUTE_RANGE + 1;

                    for (LivingEntity target : nearbyEntities) {
                        double distance = player.distanceTo(target);

                        if (distance <= EXECUTE_RANGE) {
                            float healthPercentage = target.getHealth() / target.getMaxHealth();

                            if (healthPercentage < 0.33f && distance < closestDistance) {
                                closestTarget = target;
                                closestDistance = distance;
                            }
                        }
                    }

                    if (closestTarget != null) {
                        ExecuteMarkerRenderer.addExecutionMarker(closestTarget.getUUID(), 13);

                        ModNetworking.sendToServer(new ExecuteEntityPacket(closestTarget.getId()));

                        ExecuteCooldownTracker.markExecuted(player.getUUID(), currentTick);

                        player.displayClientMessage(
                            Component.literal("§6⚔ DEATH FROM BELOW..."),
                            true
                        );
                    } else {
                        player.displayClientMessage(
                            Component.literal("§cNo valid target for execution!"),
                            true
                        );
                    }
                }
            }

            wasPressed = isPressed;
        }
    }
}

