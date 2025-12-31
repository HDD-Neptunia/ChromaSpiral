package com.ladya.chromaspiral.chroma;

import com.ladya.chromaspiral.ModBlocks;


import net.minecraft.client.color.block.BlockColors;


public class RGBBlockColors {

    @SuppressWarnings("deprecation")
	public static void registerBlockColors(BlockColors blockColors) {

        blockColors.register(
            (state, world, pos, tintIndex) -> {
                if (world == null || pos == null || tintIndex != 0) {
                    return 0xFFFFFF;
                }

                // 🔥 CLIENT CACHE ONLY
                return RGBClientColorCache.getColor(pos);
            },
            ModBlocks.RGB_WOOL.get()
        );
    }
}



