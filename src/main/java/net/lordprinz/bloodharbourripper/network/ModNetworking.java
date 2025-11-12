package net.lordprinz.bloodharbourripper.network;

import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworking {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(BloodHarbourRipper.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ExecuteEntityPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ExecuteEntityPacket::new)
                .encoder(ExecuteEntityPacket::toBytes)
                .consumerMainThread(ExecuteEntityPacket::handle)
                .add();

        net.messageBuilder(DashPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(DashPacket::new)
                .encoder(DashPacket::toBytes)
                .consumerMainThread(DashPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}

