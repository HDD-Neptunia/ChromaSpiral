package lam.ladya.chromaspiral.networking;


import lam.ladya.chromaspiral.chroma.RGBClientColorCache;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;


import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class RGBNetwork {



    public static void register(RegisterPayloadHandlersEvent event) {
    	PayloadRegistrar registrar = event.registrar("1");
    	
        	registrar.playToClient(
        		"chromaspiral:set_rgb_color",
    	        SetRGBColorPayload.CODEC,
    	        (SetRGBColorPayload payload, PayloadContext ctx) -> {
    	            ctx.enqueueWork(() -> {
    	                if (payload.color() == -1) {
    	                    RGBClientColorCache.remove(payload.pos());
    	                } else {
    	                    RGBClientColorCache.setColor(payload.pos(), payload.color());
    	                }

    	                var level = Minecraft.getInstance().level;
    	                if (level != null) {
    	                    level.sendBlockUpdated(
    	                            payload.pos(),
    	                            level.getBlockState(payload.pos()),
    	                            level.getBlockState(payload.pos()),
    	                            3
    	                    );
    	                }
    	            });
    	        }
    	        
        	);
        	
    }

    

    @SuppressWarnings("resource")
	public static void sendToTracking(Level level, BlockPos pos, int color) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        SetRGBColorPayload payload = new SetRGBColorPayload(pos, color);

        serverLevel.getChunkSource()
                .chunkMap
                .getPlayers(new ChunkPos(pos), false)
                .forEach(player ->
                        player.connection.send(
                                new ClientboundCustomPayloadPacket(payload)
                        )
                );
    }
}


