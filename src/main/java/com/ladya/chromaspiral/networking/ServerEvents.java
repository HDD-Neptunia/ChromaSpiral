package com.ladya.chromaspiral.networking;

import com.ladya.chromaspiral.ChromaSpiral;
import com.ladya.chromaspiral.chroma.RGBColorData;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChromaSpiral.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
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
