package net.lordprinz.bloodharbourripper.network;

import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

//? if >=1.20.5 {
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?}

/** Client → server: "dash now". The server re-validates everything. */
public record DashPayload()
        //? if >=1.20.5
        implements CustomPacketPayload
{
    public static final DashPayload INSTANCE = new DashPayload();
    public static final Identifier ID = BloodHarbourRipper.id("dash");

    //? if >=1.20.5 {
    public static final CustomPacketPayload.Type<DashPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, DashPayload> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public CustomPacketPayload.Type<DashPayload> type() {
        return TYPE;
    }
    //?}

    public void write(FriendlyByteBuf buf) {}

    public static DashPayload read(FriendlyByteBuf buf) {
        return INSTANCE;
    }
}
