package net.lordprinz.bloodharbourripper.client;

import net.lordprinz.bloodharbourripper.client.renderer.BoneSkewerRenderer;
import net.lordprinz.bloodharbourripper.client.renderer.DashPhantomRenderer;
import net.lordprinz.bloodharbourripper.client.renderer.ExecuteMarkerRenderer;
import net.lordprinz.bloodharbourripper.registry.ModEntityTypes;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

/** The single list of entity renderers; loaders pass in their registration method. */
public final class ModRenderers {
    @FunctionalInterface
    public interface Sink {
        <E extends Entity> void register(EntityType<? extends E> type, EntityRendererProvider<E> provider);
    }

    private ModRenderers() {}

    public static void registerAll(Sink sink) {
        sink.register(ModEntityTypes.BONE_SKEWER_SKELETON.get(), BoneSkewerRenderer::new);
        sink.register(ModEntityTypes.BONE_SKEWER_RAW.get(), BoneSkewerRenderer::new);
        sink.register(ModEntityTypes.BONE_SKEWER.get(), BoneSkewerRenderer::new);
        sink.register(ModEntityTypes.DASH_PHANTOM.get(), DashPhantomRenderer::new);
        sink.register(ModEntityTypes.EXECUTE_MARKER.get(), ExecuteMarkerRenderer::new);
    }
}
