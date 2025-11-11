package net.lordprinz.bloodharbourripper.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lordprinz.bloodharbourripper.item.custom.BoneSkewerSkeletonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoneSkewerSkeletonRenderer extends EntityRenderer<BoneSkewerSkeletonEntity> {
    private final ItemRenderer itemRenderer;

    public BoneSkewerSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(BoneSkewerSkeletonEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() + 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-entity.getXRot() + 45.0F));

        poseStack.scale(1.1F, 1.1F, 1.1F);

        poseStack.translate(0, 0, 0);

        this.itemRenderer.renderStatic(entity.getSkewerItem(), ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BoneSkewerSkeletonEntity entity) {
        return null;
    }
}


