package com.ladya.chromaspiral.station;

import com.ladya.chromaspiral.ModBlocks;
import com.ladya.chromaspiral.ModMenuTypes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ChromaDyeTableMenu extends AbstractContainerMenu {

    private final ChromaDyeTableBlockEntity blockEntity;
    private final ContainerLevelAccess access;

    public ChromaDyeTableMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(id, playerInventory, (ChromaDyeTableBlockEntity) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public ChromaDyeTableMenu(int id, Inventory playerInventory, ChromaDyeTableBlockEntity blockEntity) {
        super(ModMenuTypes.CHROMA_DYE_TABLE_MENU.get(), id);
        this.blockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        IItemHandler handler = blockEntity.getItemHandler();

        // Wool input slot
        this.addSlot(new SlotItemHandler(handler, 0, 80, 17));

        // Dye slots (1-8)
        for (int i = 0; i < 8; i++) {
            int x = 8 + (i % 4) * 18;
            int y = 50 + (i / 4) * 18;
            this.addSlot(new SlotItemHandler(handler, 1 + i, x, y));
        }

        // Output slot
        this.addSlot(new SlotItemHandler(handler, 9, 134, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

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

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.CHROMA_DYE_TABLE_BLOCK.get());
    }

    public ChromaDyeTableBlockEntity getBlockEntity() {
        return blockEntity;
    }
} 