package com.ladya.chromaspiral.chroma;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import net.minecraft.world.item.DyeColor;

public class DyeVectorBlender {
	private static final Map<DyeColor, Float> DYE_ANGLE_MAP = Map.ofEntries(
	        Map.entry(DyeColor.RED, 0f),
	        Map.entry(DyeColor.ORANGE, 30f),
	        Map.entry(DyeColor.YELLOW, 60f),
	        Map.entry(DyeColor.LIME, 90f),
	        Map.entry(DyeColor.GREEN, 120f),
	        Map.entry(DyeColor.CYAN, 150f),
	        Map.entry(DyeColor.LIGHT_BLUE, 180f),
	        Map.entry(DyeColor.BLUE, 210f),
	        Map.entry(DyeColor.PURPLE, 240f),
	        Map.entry(DyeColor.MAGENTA, 270f),
	        Map.entry(DyeColor.PINK, 300f),
	        Map.entry(DyeColor.WHITE, 330f)
	        // Optional: BLACK as saturation damper? Could go wild later.
	    );
	
		public static int blendDyes(List<DyeColor> dyes) {
			if (dyes.isEmpty()) return 0xFFFFFF;
			
			double x = 0, y = 0;
			
			for (DyeColor dye : dyes) {
				Float angle = DYE_ANGLE_MAP.get(dye);
				if (angle == null) continue;
				
				double rad = Math.toRadians(angle);
				x += Math.cos(rad);
				y += Math.sin(rad);
			}
			
			double angle = Math.atan2(y, x);
			double mag = Math.sqrt(x * x + y * y);
			
			float hue = (float) ((angle + Math.PI) / (2 * Math.PI));
			float saturation = Math.min(1f, (float) mag * 0.25f);
			float brightness = 1f;
			
			return Color.HSBtoRGB(hue, saturation, brightness);
	}
}
