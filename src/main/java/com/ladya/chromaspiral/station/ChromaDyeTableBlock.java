package com.ladya.chromaspiral.station;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;

import javax.annotation.Nullable;

import com.ladya.chromaspiral.ModBlockEntities;

import net.minecraftforge.network.NetworkHooks;

public class ChromaDyeTableBlock extends Block implements EntityBlock {

    public ChromaDyeTableBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(2.0f)
                .sound(SoundType.METAL)
                .noOcclusion()
                .lightLevel(state -> state.getValue(LIT) ? 7 : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ChromaDyeTableBlockEntity table)) {
            return InteractionResult.PASS;
        }

        ItemStack stack = player.getItemInHand(hand);

        // 1️⃣ Handle water bucket FIRST
        if (stack.is(Items.WATER_BUCKET)) {

        	if (table.getWaterLevel() >= ChromaDyeTableBlockEntity.MAX_WATER) {
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (!level.isClientSide) {
                    table.setWaterLevel(ChromaDyeTableBlockEntity.MAX_WATER);
                    table.setWaterUses(0);
                    table.setChanged();
                    
                    if (!player.isCreative()) {
                        player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                    }

                    level.sendBlockUpdated(pos, state, state, 3);
                
            }

            // IMPORTANT: consume interaction on BOTH sides
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // 2️⃣ Otherwise open GUI
        if (!level.isClientSide) {
            NetworkHooks.openScreen((ServerPlayer) player, table, pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChromaDyeTableBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null :
            (type == ModBlockEntities.CHROMA_DYE_TABLE.get()
                ? (BlockEntityTicker<T>) ChromaDyeTableBlockEntity::tick
                : null);
    }


}
