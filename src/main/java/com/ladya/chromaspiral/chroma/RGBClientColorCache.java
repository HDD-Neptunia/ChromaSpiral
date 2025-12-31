package com.ladya.chromaspiral.chroma;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;

public class RGBClientColorCache {

    private static final Map<BlockPos, Integer> COLORS = new HashMap<>();

    public static void setColor(BlockPos pos, int color) {
        COLORS.put(pos, color);
    }

    public static int getColor(BlockPos pos) {
        return COLORS.getOrDefault(pos, 0xFFFFFF);
    }

    public static void remove(BlockPos pos) {
        COLORS.remove(pos);
    }
}
