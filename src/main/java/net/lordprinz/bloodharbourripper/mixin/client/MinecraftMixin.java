package net.lordprinz.bloodharbourripper.mixin.client;

import net.lordprinz.bloodharbourripper.client.ExecuteHighlight;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Outlines valid execution targets for the local player only, without touching the entity's real
 * glowing flag (which is server-owned and visible to everyone).
 */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "shouldEntityAppearGlowing", at = @At("HEAD"), cancellable = true)
    private void bhr$highlightExecuteTargets(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (ExecuteHighlight.shouldHighlight(entity)) {
            cir.setReturnValue(true);
        }
    }
}
