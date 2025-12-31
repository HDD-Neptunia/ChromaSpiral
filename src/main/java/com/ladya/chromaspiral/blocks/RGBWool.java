package com.ladya.chromaspiral.blocks;

import javax.annotation.Nullable;

import com.ladya.chromaspiral.chroma.RGBColorData;
import com.ladya.chromaspiral.networking.RGBNetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import net.minecraft.nbt.CompoundTag;

import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;


public class RGBWool extends Block {

    public RGBWool() {
        super(BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.WOOL)
                .strength(0.8f)
                .sound(SoundType.WOOL)
                .ignitedByLava()
                .pushReaction(PushReaction.NORMAL));
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean isMoving) {

        if (!level.isClientSide && state.getBlock() != newState.getBlock()) {
        	RGBColorData.get(level).removeColor(pos);
            RGBNetwork.sendToTracking(level, pos, -1);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }


    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("Color")) {
            	int color = tag.getInt("Color");
            	
                RGBColorData.get(level).setColor(pos, color);
                RGBNetwork.sendToTracking(level, pos, color);
            }
        }
    }
}

