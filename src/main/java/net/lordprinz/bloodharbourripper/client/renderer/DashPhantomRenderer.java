package net.lordprinz.bloodharbourripper.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lordprinz.bloodharbourripper.entity.DashPhantomEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

//? if >=1.21.9 {
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
//? if >=26.1 {
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?} else {
/*import net.minecraft.client.renderer.state.CameraRenderState;
*///?}
//?} else {
/*import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
*///?}

/** A translucent copy of the dashing player, wearing their skin. */
public class DashPhantomRenderer extends
        //? if >=1.21.9 {
        EntityRenderer<DashPhantomEntity, DashPhantomRenderer.State>
        //?} else {
        /*EntityRenderer<DashPhantomEntity>
        *///?}
{
    /** White at 30% opacity (ARGB). */
    private static final int COLOR = 0x4DFFFFFF;

    //? if >=1.21.9 {
    private final PlayerModel model;
    //?} else {
    /*private final PlayerModel<AbstractClientPlayer> model;
    *///?}

    public DashPhantomRenderer(EntityRendererProvider.Context context) {
        super(context);
        //? if >=1.21.9 {
        this.model = new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false);
        //?} else {
        /*this.model = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false);
        *///?}
    }

    private static Identifier skinOf(Entity owner) {
        if (owner instanceof AbstractClientPlayer player) {
            //? if >=1.21.9 {
            return player.getSkin().body().texturePath();
            //?} elif >=1.20.2 {
            /*return player.getSkin().texture();
            *///?} else {
            /*return player.getSkinTextureLocation();
            *///?}
        }
        //? if >=1.20.2 {
        return DefaultPlayerSkin.getDefaultTexture();
        //?} else {
        /*return DefaultPlayerSkin.getDefaultSkin();
        *///?}
    }

    private static void transform(PoseStack poseStack) {
        poseStack.translate(-0.5, 0.5, 0.0);
        RenderCompat.rotate(poseStack, Axis.ZP, 180.0F);
        poseStack.scale(0.9F, 0.9F, 0.9F);
    }

    //? if >=1.21.9 {
    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(DashPhantomEntity entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.skin = skinOf(entity.getOwner());
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        transform(poseStack);
        //? if >=26.3 {
        collector.submitModel(this.model, state.pose, poseStack, RenderCompat.entityTranslucent(state.skin),
                state.lightCoords, OverlayTexture.NO_OVERLAY, COLOR, null, state.outlineColor);
        //?} else {
        /*collector.submitModel(this.model, state.pose, poseStack, RenderCompat.entityTranslucent(state.skin),
                state.lightCoords, OverlayTexture.NO_OVERLAY, COLOR, null, state.outlineColor, null);
        *///?}
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    public static class State extends EntityRenderState {
        final AvatarRenderState pose = new AvatarRenderState();
        Identifier skin = DefaultPlayerSkin.getDefaultTexture();
    }
    //?} else {
    /*@Override
    public void render(DashPhantomEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        transform(poseStack);
        var consumer = buffer.getBuffer(RenderCompat.entityTranslucent(getTextureLocation(entity)));
        //? if >=1.21 {
        /^this.model.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, COLOR);
        ^///?} else {
        this.model.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.3F);
        //?}
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public Identifier getTextureLocation(DashPhantomEntity entity) {
        return skinOf(entity.getOwner());
    }
    *///?}
}
