package net.lordprinz.bloodharbourripper;

import com.mojang.logging.LogUtils;
import net.lordprinz.bloodharbourripper.item.ModCreativeModTabs;
import net.lordprinz.bloodharbourripper.item.ModItems;
import net.lordprinz.bloodharbourripper.item.custom.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BloodHarbourRipper.MOD_ID)
public class BloodHarbourRipper
{
    public static final String MOD_ID = "bloodharbourripper";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BloodHarbourRipper(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);

        ModEntityTypes.register(modEventBus);

        net.lordprinz.bloodharbourripper.sound.ModSounds.register(modEventBus);

        net.lordprinz.bloodharbourripper.loot.ModLootModifiers.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            net.lordprinz.bloodharbourripper.network.ModNetworking.register();
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void onKeyRegister(net.minecraftforge.client.event.RegisterKeyMappingsEvent event) {
            event.register(net.lordprinz.bloodharbourripper.client.KeyBindings.EXECUTE_KEY);
            event.register(net.lordprinz.bloodharbourripper.client.KeyBindings.DASH_KEY);
        }

        @SubscribeEvent
        public static void registerRenderers(net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(net.lordprinz.bloodharbourripper.item.custom.ModEntityTypes.BONE_SKEWER_SKELETON.get(),
                    net.lordprinz.bloodharbourripper.client.renderer.BoneSkewerSkeletonRenderer::new);
            event.registerEntityRenderer(net.lordprinz.bloodharbourripper.item.custom.ModEntityTypes.BONE_SKEWER_RAW.get(),
                    net.lordprinz.bloodharbourripper.client.renderer.BoneSkewerRawRenderer::new);
            event.registerEntityRenderer(net.lordprinz.bloodharbourripper.item.custom.ModEntityTypes.BONE_SKEWER_ADVANCED.get(),
                    net.lordprinz.bloodharbourripper.client.renderer.BoneSkewerAdvancedRenderer::new);
            event.registerEntityRenderer(net.lordprinz.bloodharbourripper.item.custom.ModEntityTypes.DASH_PHANTOM.get(),
                    net.lordprinz.bloodharbourripper.client.renderer.DashPhantomRenderer::new);
        }
    }
}
