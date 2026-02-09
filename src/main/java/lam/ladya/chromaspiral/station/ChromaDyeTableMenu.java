package lam.ladya.chromaspiral.station;


import lam.ladya.chromaspiral.ModBlocks;
import lam.ladya.chromaspiral.ModMenuTypes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;


public class ChromaDyeTableMenu extends AbstractContainerMenu {

    private final ChromaDyeTableBlockEntity blockEntity;
    private final ContainerLevelAccess access;
    private final ContainerData data;
    
    

    
    public int getWaterLevel() {
        return data.get(0);
    }

    public int getWaterUses() {
        return data.get(1);
    }
    
    public int getWaterColor() {
        return data.get(2);
    }


    
    public ChromaDyeTableMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(id, playerInventory, (ChromaDyeTableBlockEntity) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public ChromaDyeTableMenu(int id, Inventory playerInventory, ChromaDyeTableBlockEntity blockEntity) {
        super(ModMenuTypes.CHROMA_DYE_TABLE_MENU.get(), id);
        this.blockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        IItemHandler handler = blockEntity.getItemHandler();

        

        // Wool input slot
        this.addSlot(new SlotItemHandler(handler, ChromaDyeTableBlockEntity.WOOL_SLOT, 148, 16) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModBlocks.RGB_WOOL_ITEM.get());
            }
        });

        int[][] dyeSlots = {
        	    {37, 18}, {39, 41}, {65, 52}, {90, 52},
        	    {115, 42}, {118, 18}, {78, 28}
        	};

        	for (int i = 0; i < 7; i++) {
        	    int slotIndex = ChromaDyeTableBlockEntity.FIRST_DYE_SLOT + i;
        	    this.addSlot(new net.neoforged.neoforge.items.SlotItemHandler(handler, slotIndex,
        	        dyeSlots[i][0],
        	        dyeSlots[i][1]
        	    ) {
        	        @Override
        	        public boolean mayPlace(ItemStack stack) {
        	            return stack.getItem() instanceof DyeItem;
        	        }
        	    });
        	}

        
	        // Output slot
	    this.addSlot(new ChromaDyeOutputSlot(
	    	    handler,
	    	    blockEntity,
	    	    ChromaDyeTableBlockEntity.OUTPUT_SLOT,
	    	    148,
	    	    56
	    	));
	    
	    this.data = new ContainerData() {
	        @Override
	        public int get(int index) {
	            return switch (index) {
	                case 0 -> blockEntity.getWaterLevel();
	                case 1 -> blockEntity.getWaterUses();
	                case 2 -> blockEntity.getWaterColor();
	                default -> 0;
	            };
	        }

	        @Override
	        public void set(int index, int value) {
	            switch (index) {
	                case 0 -> blockEntity.setWaterLevel(value);
	                case 1 -> blockEntity.setWaterUses(value);
	                case 2 -> blockEntity.setWaterColor(value);
	            }
	        }

	        @Override
	        public int getCount() {
	            return 3;
	        }
	    };

	    this.addDataSlots(data);

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Leave unimplemented for now
        return ItemStack.EMPTY;
    }

    

    //@SuppressWarnings("resource")
    //@Override
    //public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        //if (slotId == ChromaDyeTableBlockEntity.OUTPUT_SLOT) {
            //if (!player.level().isClientSide && blockEntity != null) {
                //blockEntity.applyDyeFromPreview();
            //}
            //super.clicked(slotId, dragType, clickType, player);
            //return;
        //}
        //super.clicked(slotId, dragType, clickType, player);
    //}


    public void consumeWaterUse() {
        int uses = data.get(1) + 1;
        int level = data.get(0);

        if (uses >= 2) {
            uses = 0;
            level = Math.max(0, level - 1);
        }

        data.set(0, level); // 🔥 THIS triggers sync
        data.set(1, uses);

        broadcastChanges(); // 🔥 forces client update
    }


    @Override
    public boolean stillValid(Player player) {
        return this.access.evaluate((level, pos) -> {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            boolean isCorrectBlock = blockEntity instanceof ChromaDyeTableBlockEntity;
            boolean inRange = player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
            return isCorrectBlock && inRange;
        }).orElse(false);
    }






    public ChromaDyeTableBlockEntity getBlockEntity() {
        return blockEntity;
    	}
	}
