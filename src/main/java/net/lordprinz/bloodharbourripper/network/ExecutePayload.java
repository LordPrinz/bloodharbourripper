package net.lordprinz.bloodharbourripper.network;

import net.lordprinz.bloodharbourripper.BloodHarbourRipper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

//? if >=1.20.5 {
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?}

/** Client → server: "execute this entity". The server re-validates everything. */
public record ExecutePayload(int targetId)
        //? if >=1.20.5
        implements CustomPacketPayload
{
    public static final Identifier ID = BloodHarbourRipper.id("execute");

    //? if >=1.20.5 {
    public static final CustomPacketPayload.Type<ExecutePayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ExecutePayload> CODEC =
            StreamCodec.composite(ByteBufCodecs.VAR_INT, ExecutePayload::targetId, ExecutePayload::new);

    @Override
    public CustomPacketPayload.Type<ExecutePayload> type() {
        return TYPE;
    }
    //?}

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(targetId);
    }

    public static ExecutePayload read(FriendlyByteBuf buf) {
        return new ExecutePayload(buf.readVarInt());
    }
}
