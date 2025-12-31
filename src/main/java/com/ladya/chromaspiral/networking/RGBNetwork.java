package com.ladya.chromaspiral.networking;

import com.ladya.chromaspiral.ChromaSpiral;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class RGBNetwork {

    private static final String PROTOCOL = "1";
    private static SimpleChannel CHANNEL; // NOT final
    private static int id = 0;

    @SuppressWarnings("removal")
	public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ChromaSpiral.MODID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
        );

        CHANNEL.registerMessage(
            id++,
            SetRGBColorPacket.class,
            SetRGBColorPacket::encode,
            SetRGBColorPacket::decode,
            SetRGBColorPacket::handle
        );
    }

    public static void sendToTracking(Level level, BlockPos pos, int color) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (CHANNEL == null) return; // safety net

        CHANNEL.send(
            PacketDistributor.TRACKING_CHUNK.with(() -> serverLevel.getChunkAt(pos)),
            new SetRGBColorPacket(pos, color)
        );
    }
}


