package net.muttnes.MuttnesFlashlight.client;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.muttnes.MuttnesFlashlight.entities.custom.LightEntity;

public class LightEntityRenderer extends EntityRenderer<LightEntity> {

    public LightEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(LightEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(LightEntity entity) {
        return null;
    }
}