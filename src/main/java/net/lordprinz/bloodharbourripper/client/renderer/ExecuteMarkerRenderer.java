package net.lordprinz.bloodharbourripper.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.lordprinz.bloodharbourripper.entity.ExecuteMarkerEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

//? if >=1.21.9 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
//? if >=26.1 {
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?} else {
/*import net.minecraft.client.renderer.state.CameraRenderState;
*///?}
//?} else {
/*import net.minecraft.client.renderer.MultiBufferSource;
*///?}

/** The pulsing execution mark drawn flat on the ground under the target. */
public class ExecuteMarkerRenderer extends
        //? if >=1.21.9 {
        EntityRenderer<ExecuteMarkerEntity, ExecuteMarkerRenderer.State>
        //?} else {
        /*EntityRenderer<ExecuteMarkerEntity>
        *///?}
{
    private static final Identifier TEXTURE = BloodHarbourRipper.id("textures/effect/execute_marker.png");
    private static final float SIZE = 1.6F;

    public ExecuteMarkerRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    private static float pulse(float age) {
        return 1.0F + Mth.sin((ExecuteMarkerEntity.DELAY_TICKS - age) * 0.3F) * 0.1F;
    }

    private static void transform(PoseStack poseStack, float scale) {
        poseStack.translate(0.0, 0.01, 0.0);
        RenderCompat.rotate(poseStack, Axis.XP, 90.0F);
        poseStack.scale(scale, scale, 1.0F);
    }

    //? if >=1.21.9 {
    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        transform(poseStack, pulse(state.ageInTicks));
        collector.submitCustomGeometry(poseStack, RenderCompat.entityTranslucent(TEXTURE),
                (pose, consumer) -> RenderCompat.square(pose, consumer, SIZE));
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    public static class State extends EntityRenderState {}
    //?} else {
    /*@Override
    public void render(ExecuteMarkerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        transform(poseStack, pulse(entity.tickCount + partialTicks));
        RenderCompat.square(poseStack.last(), buffer.getBuffer(RenderCompat.entityTranslucent(TEXTURE)), SIZE);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public Identifier getTextureLocation(ExecuteMarkerEntity entity) {
        return TEXTURE;
    }
    *///?}
}
