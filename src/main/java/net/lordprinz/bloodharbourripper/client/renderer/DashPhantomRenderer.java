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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class DashPhantomRenderer extends EntityRenderer<DashPhantomEntity> {
    private final PlayerModel<AbstractClientPlayer> playerModel;

    public DashPhantomRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.playerModel = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false);
    }

    @Override
    public void render(DashPhantomEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ServerPlayer owner = entity.getOwner();
        if (owner == null) return;

        poseStack.pushPose();

        poseStack.scale(0.9f, 0.9f, 0.9f);
        poseStack.translate(0, -1.5, 0);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));

        this.playerModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.4F);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DashPhantomEntity entity) {
        ServerPlayer owner = entity.getOwner();
        if (owner != null && Minecraft.getInstance().level != null) {
            Player clientPlayer = Minecraft.getInstance().level.getPlayerByUUID(owner.getUUID());
            if (clientPlayer instanceof AbstractClientPlayer abstractClientPlayer) {
                return abstractClientPlayer.getSkinTextureLocation();
            }
        }
        return ResourceLocation.withDefaultNamespace("textures/entity/steve.png");
    }
}

