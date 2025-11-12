package net.lordprinz.bloodharbourripper.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lordprinz.bloodharbourripper.entity.DashPhantomEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class DashPhantomRenderer extends EntityRenderer<DashPhantomEntity> {
    private final PlayerModel<AbstractClientPlayer> playerModel;

    public DashPhantomRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.playerModel = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false);
    }

    @Override
    public void render(DashPhantomEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(-0.5, 0.5, 0);
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180));
        poseStack.scale(0.9f, 0.9f, 0.9f);

        this.playerModel.young = false;
        this.playerModel.crouching = false;
        this.playerModel.attackTime = 0.0F;

        this.playerModel.head.xRot = 0.0F;
        this.playerModel.head.yRot = 0.0F;
        this.playerModel.body.xRot = 0.0F;
        this.playerModel.body.yRot = 0.0F;
        this.playerModel.rightArm.xRot = 0.0F;
        this.playerModel.leftArm.xRot = 0.0F;
        this.playerModel.rightLeg.xRot = 0.0F;
        this.playerModel.leftLeg.xRot = 0.0F;

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));

        this.playerModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.3F);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DashPhantomEntity entity) {
        Minecraft mc = Minecraft.getInstance();
        java.util.UUID ownerUUID = entity.getOwnerUUID();

        if (ownerUUID != null) {
            if (mc.player != null && mc.player.getUUID().equals(ownerUUID)) {
                return mc.player.getSkinTextureLocation();
            }

            if (mc.level != null) {
                for (Player player : mc.level.players()) {
                    if (player.getUUID().equals(ownerUUID) && player instanceof AbstractClientPlayer abstractClientPlayer) {
                        return abstractClientPlayer.getSkinTextureLocation();
                    }
                }
            }
        }

        return ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");
    }
}

