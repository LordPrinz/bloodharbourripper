package net.lordprinz.bloodharbourripper.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lordprinz.bloodharbourripper.item.custom.BoneSkewerRawEntity;
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
public class BoneSkewerRawRenderer extends EntityRenderer<BoneSkewerRawEntity> {
    private final ItemRenderer itemRenderer;

    public BoneSkewerRawRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(BoneSkewerRawEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() + 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getXRot() + 50.0F));

        poseStack.scale(1.1F, 1.1F, 1.1F);

        // Przesunięcie dla lepszego centrowania
        poseStack.translate(0, 0, 0);

        // Render the item - użyj FIXED dla lepszego wyglądu 3D
        this.itemRenderer.renderStatic(entity.getSkewerItem(), ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BoneSkewerRawEntity entity) {
        return null; // Not needed as we render the item directly
    }
}

