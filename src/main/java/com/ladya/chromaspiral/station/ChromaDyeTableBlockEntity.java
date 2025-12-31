package com.ladya.chromaspiral.station;

import com.ladya.chromaspiral.ModBlockEntities;
import com.ladya.chromaspiral.ModBlocks;
import com.ladya.chromaspiral.ModBlockEntities;
import com.ladya.chromaspiral.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;

import net.minecraftforge.common.util.LazyOptional;

import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChromaDyeTableBlockEntity extends BlockEntity implements MenuProvider {

    public static final int INPUT_WOOL_SLOT = 0;
    public static final int INPUT_DYE_START = 1;
    public static final int INPUT_DYE_END = 8;
    public static final int OUTPUT_SLOT = 9;

    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public ChromaDyeTableBlockEntity(BlockPos pos, BlockState state) {
    	super(ModBlockEntities.CHROMA_DYE_TABLE.get(), pos, state);

    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (blockEntity instanceof ChromaDyeTableBlockEntity entity && !level.isClientSide) {
            // Actual logic goes here
        }
    }


    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("Inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.chromaspiral.chroma_dye_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new ChromaDyeTableMenu(id, playerInventory, this);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public LazyOptional<net.minecraftforge.items.IItemHandler> getCapability() {
        return LazyOptional.of(() -> itemHandler);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }
}
