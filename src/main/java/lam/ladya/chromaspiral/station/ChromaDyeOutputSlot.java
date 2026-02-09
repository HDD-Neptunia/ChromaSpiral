package lam.ladya.chromaspiral.station;


import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeItem;


public class ChromaDyeOutputSlot extends net.neoforged.neoforge.items.SlotItemHandler {

    @SuppressWarnings("unused")
	private final net.neoforged.neoforge.items.IItemHandler handler;
    private final ChromaDyeTableBlockEntity blockEntity;

    public ChromaDyeOutputSlot(net.neoforged.neoforge.items.IItemHandler handler, ChromaDyeTableBlockEntity blockEntity, int index, int x, int y) {
        super(handler, index, x, y);
        this.handler = handler;
        this.blockEntity = blockEntity;
    }


    @Override
    public boolean mayPickup(Player player) {
        return blockEntity.getWaterLevel() > 0;
    }
    
    @SuppressWarnings("resource")
	@Override
    public void onTake(Player player, ItemStack stack) {
        if (player.level().isClientSide) {
            super.onTake(player, stack);
            return;
        }

        if (blockEntity.getWaterLevel() <= 0) return;

        net.neoforged.neoforge.items.ItemStackHandler stacks = blockEntity.getItemHandler();

        blockEntity.consumeWaterUse();

        for (int i = ChromaDyeTableBlockEntity.FIRST_DYE_SLOT; i <= ChromaDyeTableBlockEntity.LAST_DYE_SLOT; i++) {
            ItemStack dye = stacks.getStackInSlot(i);
            if (dye.getItem() instanceof DyeItem) {
                dye.shrink(1);
                stacks.setStackInSlot(i, dye.isEmpty() ? ItemStack.EMPTY : dye);
            }
        }

	stacks.setStackInSlot(
            ChromaDyeTableBlockEntity.WOOL_SLOT,
            ItemStack.EMPTY
            );

        
        stacks.setStackInSlot(ChromaDyeTableBlockEntity.OUTPUT_SLOT, ItemStack.EMPTY);

        blockEntity.setChanged();
        super.onTake(player, stack);
    }



    

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public void onQuickCraft(ItemStack oldStack, ItemStack newStack) {
        // no-op
    }


}
