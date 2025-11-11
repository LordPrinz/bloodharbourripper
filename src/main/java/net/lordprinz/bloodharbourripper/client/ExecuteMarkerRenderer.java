package net.lordprinz.bloodharbourripper.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "bloodharbourripper", value = Dist.CLIENT)
public class ExecuteMarkerRenderer {
    private static final Map<UUID, Integer> executionMarkers = new HashMap<>();
    private static final ResourceLocation EXECUTE_MARKER_TEXTURE =
        ResourceLocation.fromNamespaceAndPath("bloodharbourripper", "textures/effect/execute_marker.png");

    public static void addExecutionMarker(UUID mobUUID, int durationTicks) {
        executionMarkers.put(mobUUID, durationTicks);
    }

    public static void tickMarkers() {
        executionMarkers.entrySet().removeIf(entry -> {
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                return true;
            }
            entry.setValue(remaining);
            return false;
        });
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        Vec3 cameraPos = event.getCamera().getPosition();

        for (Map.Entry<UUID, Integer> entry : executionMarkers.entrySet()) {
            UUID mobUUID = entry.getKey();
            int remainingTicks = entry.getValue();

            for (var entity : mc.level.entitiesForRendering()) {
                if (entity.getUUID().equals(mobUUID) && entity instanceof LivingEntity living) {
                    renderExecuteMarker(poseStack, bufferSource, living, cameraPos, remainingTicks);
                    break;
                }
            }
        }
    }

    private static void renderExecuteMarker(PoseStack poseStack, MultiBufferSource bufferSource,
                                           LivingEntity entity, Vec3 cameraPos, int remainingTicks) {
        poseStack.pushPose();

        Vec3 entityPos = entity.position();
        double x = entityPos.x - cameraPos.x;
        double y = entityPos.y - cameraPos.y + 0.01;
        double z = entityPos.z - cameraPos.z;

        poseStack.translate(x, y, z);
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90));

        float scale = 1.0f + (float) Math.sin(remainingTicks * 0.3f) * 0.1f;
        poseStack.scale(scale, scale, 1.0f);

        float size = 1.6f;

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(EXECUTE_MARKER_TEXTURE));
        Matrix4f matrix = poseStack.last().pose();

        vertexConsumer.vertex(matrix, -size, -size, 0).color(255, 255, 255, 255)
            .uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(matrix, size, -size, 0).color(255, 255, 255, 255)
            .uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(matrix, size, size, 0).color(255, 255, 255, 255)
            .uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(matrix, -size, size, 0).color(255, 255, 255, 255)
            .uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 1, 0).endVertex();

        poseStack.popPose();
    }
}

