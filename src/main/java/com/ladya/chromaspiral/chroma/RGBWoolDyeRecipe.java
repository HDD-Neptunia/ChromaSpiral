package com.ladya.chromaspiral.chroma;

import com.google.gson.JsonObject;
import com.ladya.chromaspiral.ModBlocks;
import com.ladya.chromaspiral.ModRecipeTypes;
import com.ladya.chromaspiral.blocks.ModRecipeSerializers;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;

import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;



public class RGBWoolDyeRecipe implements Recipe<SimpleContainer> {

	private final ResourceLocation id;

	public RGBWoolDyeRecipe(ResourceLocation id) {
	    this.id = id;
	}



    @Override
    public boolean matches(SimpleContainer container, Level level) {
        boolean foundWool = false;
        boolean foundDye = false;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModBlocks.RGB_WOOL_ITEM.get())) {
                foundWool = true; // ✅ allow many
            } else if (stack.getItem() instanceof DyeItem) {
                foundDye = true;
            } else {
                return false;
            }
        }

        return foundWool && foundDye;
    }


    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess access) {
        int r = 0, g = 0, b = 0, dyeCount = 0;
        int woolCount = 0;
        ItemStack inputWool = ItemStack.EMPTY;


        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModBlocks.RGB_WOOL_ITEM.get())) {
            	if (inputWool.isEmpty()) {
            		inputWool = stack;
            	}
                woolCount += stack.getCount();
            }
            else if (stack.getItem() instanceof DyeItem dye) {
                float[] c = dye.getDyeColor().getTextureDiffuseColors();
                r += (int)(c[0] * 255);
                g += (int)(c[1] * 255);
                b += (int)(c[2] * 255);
                dyeCount++;
            }
        }

        if (woolCount == 0 || dyeCount == 0) return ItemStack.EMPTY;

        r /= dyeCount;
        g /= dyeCount;
        b /= dyeCount;

        int color = (r << 16) | (g << 8) | b;

        ItemStack preview = inputWool.copy();
        preview.getOrCreateTag().putInt("Color", color);
        preview.setCount(inputWool.getCount());

        preview.getOrCreateTag().putInt("Color", color);
        return preview;
    }


    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return new ItemStack(ModBlocks.RGB_WOOL_ITEM.get());
    }

    @SuppressWarnings("unused")
	private boolean isRGBWool(ItemStack stack) {
        return stack.is(ModBlocks.RGB_WOOL_ITEM.get());
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return w * h >= 2;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(SimpleContainer inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        boolean dyeUsed = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack item = inv.getItem(i);

            if (item.isEmpty()) continue;

            // Remove ALL RGB Wool entirely
            if (item.is(ModBlocks.RGB_WOOL_ITEM.get())) {
                // Don’t return anything, it gets eaten
                continue;
            }

            // Remove ONE dye
            else if (!dyeUsed && item.getItem() instanceof DyeItem) {
                dyeUsed = true;
                if (item.getCount() > 1) {
                    ItemStack leftover = item.copy();
                    leftover.setCount(item.getCount() - 1);
                    remaining.set(i, leftover);
                }
                // else default is EMPTY
            }

            // Return other items as their default remainder (e.g., bowls)
            else {
                remaining.set(i, net.minecraftforge.common.ForgeHooks.getCraftingRemainingItem(item));
            }
        }

        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.RGB_WOOL_DYE.get();
    }
    
    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.RGB_WOOL_DYE_RECIPE_TYPE.get();
    }


	@Override
	public ResourceLocation getId() {
		return this.id;
	}
	
	public static class Serializer implements RecipeSerializer<RGBWoolDyeRecipe> {

	    @Override
	    public RGBWoolDyeRecipe fromJson(ResourceLocation id, JsonObject json) {
	        return new RGBWoolDyeRecipe(id);
	    }

	    @Override
	    public RGBWoolDyeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
	        return new RGBWoolDyeRecipe(id);
	    }

	    @Override
	    public void toNetwork(FriendlyByteBuf buf, RGBWoolDyeRecipe recipe) {
	        // no data to sync
	    }
	}

}

