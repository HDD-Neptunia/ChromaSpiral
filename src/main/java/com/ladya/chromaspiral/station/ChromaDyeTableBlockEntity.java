package com.ladya.chromaspiral.station;

import com.ladya.chromaspiral.ModBlockEntities;
import com.ladya.chromaspiral.chroma.RGBColor;
import com.ladya.chromaspiral.chroma.RGBColorBlender;
import com.ladya.chromaspiral.chroma.RGBWoolDyeRecipe;
import com.ladya.chromaspiral.ModRecipeTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChromaDyeTableBlockEntity extends BlockEntity implements MenuProvider {
	public static final int WOOL_SLOT = 0;

	public static final int FIRST_DYE_SLOT = 1;
	public static final int LAST_DYE_SLOT = 7;

	public static final int OUTPUT_SLOT = 8;

	private int waterLevel = 0;
	private int waterUses = 0;
	
	private int waterColor = 0x3F76E4; // default MC water blue


	public static final int MAX_WATER = 8;

	public boolean hasWater() {
        return this.getWaterLevel() > 0;
    }
	
	
	private boolean suppressPreview = false;

    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            if (slot != OUTPUT_SLOT) {
                setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
            }
            setChanged();
        }
    };
    
    public boolean tryFillWater() {
        if (this.waterLevel >= MAX_WATER) return false;

        this.waterLevel = MAX_WATER;
        this.waterUses = 0;
        return true;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int level) {
        this.waterLevel = Mth.clamp(level, 0, MAX_WATER);
        setChanged();
    }

    public int getWaterColor() {
        return waterColor;
    }

    public void setWaterColor(int color) {
        this.waterColor = color & 0xFFFFFF; // safety
        setChanged();
    }

    public int getWaterUses() {
        return waterUses;
    }

    public void setWaterUses(int uses) {
        this.waterUses = uses;
        setChanged();
    }
    
    public void setSuppressPreview(boolean suppress) {
        this.suppressPreview = suppress;
    }

    public boolean isPreviewSuppressed() {
        return suppressPreview;
    }


    public ChromaDyeTableBlockEntity(BlockPos pos, BlockState state) {
    	super(ModBlockEntities.CHROMA_DYE_TABLE.get(), pos, state);

    }

    
    public static <T extends BlockEntity> void tick(
            Level level, BlockPos pos, BlockState state, T blockEntity
    ) {

        if (!(blockEntity instanceof ChromaDyeTableBlockEntity entity) || level.isClientSide) {
            return;
        }
        
        if (entity.isPreviewSuppressed()) return;


        if (!entity.hasWater()) {
            entity.itemHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
            return;
        }

        List<ItemStack> dyeStacks = new ArrayList<>();

        for (int i = FIRST_DYE_SLOT; i <= LAST_DYE_SLOT; i++) {
            dyeStacks.add(entity.itemHandler.getStackInSlot(i));
        }

        RGBColor color = RGBColorBlender.blendFromStacks(dyeStacks);
        if (color != null) {
            entity.setWaterColor(color.toPackedRGB());
        }


        
        // Build container ONCE from the handler
        SimpleContainer container = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            container.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        
        Optional<RGBWoolDyeRecipe> recipeOptional =
                level.getRecipeManager().getRecipeFor(
                        ModRecipeTypes.RGB_WOOL_DYE_RECIPE_TYPE.get(),
                        container,
                        level
                );

        ItemStack result = ItemStack.EMPTY;

        if (recipeOptional.isPresent()) {
            RGBWoolDyeRecipe recipe = recipeOptional.get();
            result = recipe.assemble(container, level.registryAccess());
        }



        // Preview-only: update output slot if changed
        ItemStack current =
                entity.itemHandler.getStackInSlot(ChromaDyeTableBlockEntity.OUTPUT_SLOT);

        if (!ItemStack.matches(result, current)) {
            entity.itemHandler.setStackInSlot(
                    ChromaDyeTableBlockEntity.OUTPUT_SLOT,
                    result
            );
        }
        
        entity.suppressPreview = false;

    }


    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        // Inventory
        itemHandler.deserializeNBT(tag.getCompound("Inventory"));

        // Water
        waterLevel = tag.getInt("Water");
        
        waterUses = tag.getInt("WaterUses");
        
        waterColor = tag.getInt("WaterColor");

    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        // Inventory
        tag.put("Inventory", itemHandler.serializeNBT());

        // Water
        tag.putInt("Water", waterLevel);
        
        tag.putInt("WaterUses", waterUses);

        tag.putInt("WaterColor", waterColor);
    }


    public void consumeWaterUse() {
        if (waterLevel <= 0) return;

        waterUses++;

        if (waterUses >= 2) {
            waterUses = 0;
            waterLevel--;
        }
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.chromaspiral.chroma_dye_table");
    }


    public void applyDyeFromPreview() {
        if (!hasWater()) return;

        ItemStackHandler handler = this.itemHandler;

        // consume ONE dye
        for (int i = FIRST_DYE_SLOT; i <= LAST_DYE_SLOT; i++) {
            ItemStack dye = handler.getStackInSlot(i);
            if (dye.getItem() instanceof DyeItem) {
                dye.shrink(1);
                handler.setStackInSlot(i, dye.isEmpty() ? ItemStack.EMPTY : dye);
                break;
            }
        }

        // apply colour to wool
        ItemStack wool = handler.getStackInSlot(WOOL_SLOT);
        ItemStack preview = handler.getStackInSlot(OUTPUT_SLOT);

        if (!wool.isEmpty() && preview.hasTag()) {
            wool.getOrCreateTag().putInt(
                "Color",
                preview.getTag().getInt("Color")
            );
            handler.setStackInSlot(WOOL_SLOT, wool);
        }

        // clear preview
        handler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);

        // 🔥 THIS is where water MUST drain
        consumeWaterUse();

        setChanged();
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
