package com.ladya.chromaspiral.networking;


import java.util.function.Supplier;

import com.ladya.chromaspiral.chroma.RGBClientColorCache;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SetRGBColorPacket {

    private final BlockPos pos;
    private final int color;

    public SetRGBColorPacket(BlockPos pos, int color) {
        this.pos = pos;
        this.color = color;
    }

    public static void encode(SetRGBColorPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.color);
    }

    public static SetRGBColorPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int color = buf.readInt();
        return new SetRGBColorPacket(pos, color);
    }

    public static void handle(SetRGBColorPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (msg.color == -1) {
                RGBClientColorCache.remove(msg.pos);
            } else {
                RGBClientColorCache.setColor(msg.pos, msg.color);
            }
            
            @SuppressWarnings("resource")
			var level = Minecraft.getInstance().level;
            if(level != null) {
            	level.sendBlockUpdated(
            		msg.pos,
            		level.getBlockState(msg.pos),
            		level.getBlockState(msg.pos),
            		3
            		);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public BlockPos pos() {
        return pos;
    }

    public int color() {
        return color;
    }
}
