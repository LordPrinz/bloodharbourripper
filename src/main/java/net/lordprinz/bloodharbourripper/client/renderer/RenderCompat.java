package net.lordprinz.bloodharbourripper.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

//? if >=1.21.11 {
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
//?} else {
/*import net.minecraft.client.renderer.RenderType;
*///?}

/** Rendering calls that changed between Minecraft versions. */
final class RenderCompat {
    static final int FULL_BRIGHT = 0xF000F0;

    private RenderCompat() {}

    static void rotate(PoseStack poseStack, Axis axis, float degrees) {
        //? if >=26.3 {
        poseStack.rotate(axis.rotationDegrees(degrees));
        //?} else {
        /*poseStack.mulPose(axis.rotationDegrees(degrees));
        *///?}
    }

    static RenderType entityTranslucent(Identifier texture) {
        //? if >=1.21.11 {
        return RenderTypes.entityTranslucent(texture);
        //?} else {
        /*return RenderType.entityTranslucent(texture);
        *///?}
    }

    /** A full-bright, double-sided square of {@code 2 * size} in the XY plane, centred on the origin. */
    static void square(PoseStack.Pose pose, VertexConsumer consumer, float size) {
        vertex(pose, consumer, -size, -size, 0, 1);
        vertex(pose, consumer, size, -size, 1, 1);
        vertex(pose, consumer, size, size, 1, 0);
        vertex(pose, consumer, -size, size, 0, 0);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer consumer, float x, float y, float u, float v) {
        //? if >=1.21 {
        consumer.addVertex(pose, x, y, 0).setColor(255, 255, 255, 255).setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(FULL_BRIGHT).setNormal(pose, 0, 1, 0);
        //?} else {
        /*consumer.vertex(pose.pose(), x, y, 0).color(255, 255, 255, 255).uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(FULL_BRIGHT).normal(pose.normal(), 0, 1, 0).endVertex();
        *///?}
    }
}
