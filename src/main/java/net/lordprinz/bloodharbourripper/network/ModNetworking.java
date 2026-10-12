package net.lordprinz.bloodharbourripper.network;

import net.lordprinz.bloodharbourripper.ability.DashAbility;
import net.lordprinz.bloodharbourripper.ability.ExecuteAbility;
import net.minecraft.server.level.ServerPlayer;

/**
 * Server-side packet registration for every loader. Bump {@link #PROTOCOL_VERSION} whenever a
 * payload's wire format changes, so mismatched client/server versions refuse to connect cleanly.
 */
public final class ModNetworking {
    public static final String PROTOCOL_VERSION = "1";

    private ModNetworking() {}

    static void onExecute(ServerPlayer player, ExecutePayload payload) {
        ExecuteAbility.handleRequest(player, payload.targetId());
    }

    static void onDash(ServerPlayer player, DashPayload payload) {
        DashAbility.handleRequest(player);
    }

    //? if fabric && >=1.20.5 {
    public static void register() {
        //? if >=26.1 {
        var serverbound = net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.serverboundPlay();
        //?} else {
        /*var serverbound = net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playC2S();
        *///?}
        serverbound.register(ExecutePayload.TYPE, ExecutePayload.CODEC);
        serverbound.register(DashPayload.TYPE, DashPayload.CODEC);
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(ExecutePayload.TYPE,
                (payload, context) -> onExecute(context.player(), payload));
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(DashPayload.TYPE,
                (payload, context) -> onDash(context.player(), payload));
    }
    //?} elif fabric {
    /*public static void register() {
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(ExecutePayload.ID,
                (server, player, handler, buf, sender) -> {
                    ExecutePayload payload = ExecutePayload.read(buf);
                    server.execute(() -> onExecute(player, payload));
                });
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(DashPayload.ID,
                (server, player, handler, buf, sender) -> server.execute(() -> onDash(player, DashPayload.INSTANCE)));
    }
    *///?} elif neoforge {
    /*public static void register(net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(ExecutePayload.TYPE, ExecutePayload.CODEC,
                (payload, context) -> onExecute((ServerPlayer) context.player(), payload));
        registrar.playToServer(DashPayload.TYPE, DashPayload.CODEC,
                (payload, context) -> onDash((ServerPlayer) context.player(), payload));
    }
    *///?} else {
    /*public static final net.minecraftforge.network.simple.SimpleChannel CHANNEL =
            net.minecraftforge.network.NetworkRegistry.ChannelBuilder
                    .named(net.lordprinz.bloodharbourripper.BloodHarbourRipper.id("main"))
                    .networkProtocolVersion(() -> PROTOCOL_VERSION)
                    .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                    .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                    .simpleChannel();

    public static void register() {
        CHANNEL.messageBuilder(ExecutePayload.class, 0, net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER)
                .encoder(ExecutePayload::write).decoder(ExecutePayload::read)
                .consumerMainThread((payload, context) -> {
                    ServerPlayer player = context.get().getSender();
                    if (player != null) onExecute(player, payload);
                })
                .add();
        CHANNEL.messageBuilder(DashPayload.class, 1, net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER)
                .encoder(DashPayload::write).decoder(DashPayload::read)
                .consumerMainThread((payload, context) -> {
                    ServerPlayer player = context.get().getSender();
                    if (player != null) onDash(player, payload);
                })
                .add();
    }
    *///?}
}
