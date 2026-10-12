package net.lordprinz.bloodharbourripper.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lordprinz.bloodharbourripper.entity.BoneSkewerEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

//? if >=1.21.9 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
//? if >=26.1 {
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?} else {
/*import net.minecraft.client.renderer.state.CameraRenderState;
*///?}
//?} else {
/*import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
*///?}

/** Renders a flying Bone Skewer as its item model, oriented along its flight path. */
public class BoneSkewerRenderer extends
        //? if >=1.21.9 {
        EntityRenderer<BoneSkewerEntity, BoneSkewerRenderer.State>
        //?} else {
        /*EntityRenderer<BoneSkewerEntity>
        *///?}
{
    private static final float SCALE = 1.1F;

    //? if >=1.21.9 {
    private final ItemModelResolver itemModelResolver;

    public BoneSkewerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(BoneSkewerEntity entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        this.itemModelResolver.updateForNonLiving(state.item, entity.displayStack(), ItemDisplayContext.FIXED, entity);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        orient(poseStack, state.yRot, state.xRot);
        state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    public static class State extends EntityRenderState {
        final ItemStackRenderState item = new ItemStackRenderState();
        float yRot;
        float xRot;
    }
    //?} else {
    /*private final ItemRenderer itemRenderer;

    public BoneSkewerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(BoneSkewerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        orient(poseStack, Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()), Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()));
        this.itemRenderer.renderStatic(entity.displayStack(), ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY,
                poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public Identifier getTextureLocation(BoneSkewerEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
    *///?}

    private static void orient(PoseStack poseStack, float yRot, float xRot) {
        RenderCompat.rotate(poseStack, Axis.YP, yRot + 90.0F);
        RenderCompat.rotate(poseStack, Axis.ZP, -xRot + 45.0F);
        poseStack.scale(SCALE, SCALE, SCALE);
    }
}
