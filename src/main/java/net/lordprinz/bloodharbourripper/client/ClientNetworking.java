package net.lordprinz.bloodharbourripper.client;

import net.lordprinz.bloodharbourripper.network.DashPayload;
import net.lordprinz.bloodharbourripper.network.ExecutePayload;

/** Client → server sending for every loader. Client-only class. */
public final class ClientNetworking {
    private ClientNetworking() {}

    public static void sendExecute(int targetId) {
        ExecutePayload payload = new ExecutePayload(targetId);
        //? if fabric && >=1.20.5 {
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(payload);
        //?} elif fabric {
        /*var buf = net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create();
        payload.write(buf);
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(ExecutePayload.ID, buf);
        *///?} elif neoforge && >=1.21.9 {
        /*net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(payload);
        *///?} elif neoforge {
        /*net.neoforged.neoforge.network.PacketDistributor.sendToServer(payload);
        *///?} else {
        /*net.lordprinz.bloodharbourripper.network.ModNetworking.CHANNEL.sendToServer(payload);
        *///?}
    }

    public static void sendDash() {
        DashPayload payload = DashPayload.INSTANCE;
        //? if fabric && >=1.20.5 {
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(payload);
        //?} elif fabric {
        /*net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(DashPayload.ID,
                net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create());
        *///?} elif neoforge && >=1.21.9 {
        /*net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(payload);
        *///?} elif neoforge {
        /*net.neoforged.neoforge.network.PacketDistributor.sendToServer(payload);
        *///?} else {
        /*net.lordprinz.bloodharbourripper.network.ModNetworking.CHANNEL.sendToServer(payload);
        *///?}
    }
}
