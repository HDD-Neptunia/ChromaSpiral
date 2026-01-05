package com.ladya.chromaspiral.chroma;

import java.util.List;

import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;

public final class RGBColorBlender {
	
	private int r, g, b;
	
	public int toPackedRGB() {
	    return (r << 16) | (g << 8) | b;
	}

	public static RGBColor blendFromStacks(List<ItemStack> stacks)
 {
        int r = 0, g = 0, b = 0, count = 0;

        for (ItemStack stack : stacks) {
            if (stack.getItem() instanceof DyeItem dye) {
                float[] c = dye.getDyeColor().getTextureDiffuseColors();
                r += (int)(c[0] * 255);
                g += (int)(c[1] * 255);
                b += (int)(c[2] * 255);
                count++;
            }
        }

        if (count == 0) return null;

        

        return new RGBColor(r / count, g / count, b / count);



    }
}

