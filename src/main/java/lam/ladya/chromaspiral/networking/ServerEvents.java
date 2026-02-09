package lam.ladya.chromaspiral.networking;


import lam.ladya.chromaspiral.ChromaSpiral;
import lam.ladya.chromaspiral.chroma.RGBColorData;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;


@Mod.EventBusSubscriber(modid = ChromaSpiral.MODID)
public class ServerEvents {

    @net.neoforged.bus.api.SubscribeEvent
    public static void onStartTracking(net.neoforged.neoforge.event.entity.player.PlayerEvent.StartTracking event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        Entity target = event.getTarget();


        ServerLevel level = player.serverLevel();
        RGBColorData data = RGBColorData.get(level);

        ChunkPos chunkPos = new ChunkPos(target.blockPosition());
        
        data.getAll().forEach((pos, color) -> {
            if (new ChunkPos(pos).equals(chunkPos)) {
                RGBNetwork.sendToTracking(level, pos, color);
            }
        });
    }
}
