package lam.ladya.chromaspiral.networking;




import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;



public record SetRGBColorPayload(BlockPos pos, int color) {

    // 🔑 CODEC is how Forge serializes/deserializes
    public static final StreamCodec<RegistryFriendlyByteBuf, SetRGBColorPayload> CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeBlockPos(payload.pos());
                        buf.writeInt(payload.color());
                    },
                    buf -> new SetRGBColorPayload(
                            buf.readBlockPos(),
                            buf.readInt()
                    )
            );

}
