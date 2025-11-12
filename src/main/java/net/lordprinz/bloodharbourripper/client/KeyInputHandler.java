package net.lordprinz.bloodharbourripper.client;

import net.lordprinz.bloodharbourripper.item.ModItems;
import net.lordprinz.bloodharbourripper.network.DashPacket;
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
    private static boolean wasDashPressed = false;
    private static final double EXECUTE_RANGE = 12.0;
    private static long currentTick = 0;

    private static boolean isAlly(Player player, LivingEntity target) {
        if (target instanceof Player targetPlayer) {
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
            return player.getTeam().equals(target.getTeam());
        }

        return false;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            currentTick++;

            ExecuteMarkerRenderer.tickMarkers();

            Minecraft mc = Minecraft.getInstance();

            if (mc.player == null || mc.level == null) return;

            Player player = mc.player;

            boolean isPressed = KeyBindings.EXECUTE_KEY.isDown();

            if (isPressed && !wasPressed) {
                if (player.getMainHandItem().getItem() == ModItems.BONE_SKEWER.get() && player.isShiftKeyDown()) {

                    if (!ExecuteCooldownTracker.canExecute(player.getUUID(), currentTick)) {
                        long remainingTicks = ExecuteCooldownTracker.getRemainingCooldown(player.getUUID(), currentTick);
                        double remainingSeconds = remainingTicks / 20.0;
                        player.displayClientMessage(
                            Component.literal(String.format("§cDeath from below on cooldown: %.1fs", remainingSeconds)),
                            true
                        );
                        wasPressed = isPressed;
                        return;
                    }

                    AABB searchBox = player.getBoundingBox().inflate(EXECUTE_RANGE);
                    List<LivingEntity> nearbyEntities = mc.level.getEntitiesOfClass(LivingEntity.class, searchBox,
                        e -> e != player && e.isAlive());

                    LivingEntity closestTarget = null;
                    double closestDistance = EXECUTE_RANGE + 1;

                    for (LivingEntity target : nearbyEntities) {
                        double distance = player.distanceTo(target);

                        if (distance <= EXECUTE_RANGE && !isAlly(player, target)) {
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

            boolean isDashPressed = KeyBindings.DASH_KEY.isDown();

            if (isDashPressed && !wasDashPressed && player.isShiftKeyDown()) {
                if (player.getMainHandItem().getItem() != ModItems.BONE_SKEWER.get()) {
                    player.displayClientMessage(
                        Component.literal("§cYou need to hold Bone Skewer to use Phantom Undertow!"),
                        true
                    );
                    wasDashPressed = isDashPressed;
                    return;
                }

                long gameTime = mc.level.getGameTime();
                long remainingCooldown = DashPacket.getRemainingCooldown(player.getUUID(), gameTime);

                if (remainingCooldown > 0) {
                    double remainingSeconds = remainingCooldown / 20.0;
                    player.displayClientMessage(
                        Component.literal(String.format("§cPhantom Undertow on cooldown: %.1fs", remainingSeconds)),
                        true
                    );
                } else {
                    ModNetworking.sendToServer(new DashPacket());
                    player.displayClientMessage(
                        Component.literal("§b⚡ Phantom Undertow!"),
                        true
                    );
                }
            }

            wasDashPressed = isDashPressed;
        }
    }
}

