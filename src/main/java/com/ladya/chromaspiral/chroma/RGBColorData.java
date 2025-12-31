package com.ladya.chromaspiral.chroma;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class RGBColorData extends SavedData {
	
	private final Map<BlockPos, Integer> posToColor = new HashMap<>();
	
	public void setColor(BlockPos pos, int rgb) {
		posToColor.put(pos.immutable(), rgb);
		setDirty();
	}
	
	public int getColor(BlockPos pos) {
		return posToColor.getOrDefault(pos.immutable(), 0xFFFFFF);
	}
	
	public void removeColor(BlockPos pos) {
		posToColor.remove(pos.immutable());
		setDirty();
	}

	public Map<BlockPos, Integer> getAll() {
		return posToColor;
	}
	
	//Called automatically when saving
	@Override
	public CompoundTag save(CompoundTag tag) {
		ListTag list = new ListTag();
		for (Map.Entry<BlockPos, Integer> entry : posToColor.entrySet()) {
			CompoundTag e = new CompoundTag();
			e.putLong("pos", entry.getKey().asLong());
			e.putInt("rgb", entry.getValue());
			list.add(e);
		}
		tag.put("colors", list);
		return tag;
	}
	
	public static RGBColorData load(CompoundTag tag) {
		RGBColorData data = new RGBColorData();
		ListTag list = tag.getList("colors", Tag.TAG_COMPOUND);
		for (Tag t : list) {
			CompoundTag e = (CompoundTag) t;
			BlockPos pos = BlockPos.of(e.getLong("pos"));
			int rgb = e.getInt("rgb");
			data.setColor(pos, rgb);
		}
		return data;
	}
	
	public static RGBColorData get(Level level) {
		if (!(level instanceof ServerLevel serverLevel)) {
			throw new IllegalStateException("Tried to access RGBColorData on the client");
		}
		return serverLevel.getDataStorage().computeIfAbsent(RGBColorData::load, RGBColorData::new, "rgb_color_data");
	}
}
