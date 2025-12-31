package com.ladya.chromaspiral.chroma;

import com.ladya.chromaspiral.ModBlocks;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.nbt.CompoundTag;

public class RGBItemColors {

    @SuppressWarnings("deprecation")
	public static void register(ItemColors itemColors) {
        itemColors.register(
            (stack, tintIndex) -> {
                if (tintIndex != 0) return 0xFFFFFF;

                CompoundTag tag = stack.getTag();
                return tag != null && tag.contains("Color")
                        ? tag.getInt("Color")
                        : 0xFFFFFF;
            },
            ModBlocks.RGB_WOOL_ITEM.get()
        );
    }
}

