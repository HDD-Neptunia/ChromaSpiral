package com.ladya.chromaspiral.chroma;

import com.ladya.chromaspiral.ModBlocks;
import com.ladya.chromaspiral.blocks.ModRecipeSerializers;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;



public class RGBWoolDyeRecipe extends CustomRecipe {

    public RGBWoolDyeRecipe(ResourceLocation id) {
        super(id, CraftingBookCategory.MISC);
    }
    
    private static final ThreadLocal<Integer> WOOL_USED = new ThreadLocal<>();

    private static final ThreadLocal<Integer> LAST_WOOL_USED = new ThreadLocal<>();


    @Override
    public boolean matches(CraftingContainer container, Level level) {
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
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        int r = 0, g = 0, b = 0, dyeCount = 0;
        int woolCount = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModBlocks.RGB_WOOL_ITEM.get())) {
                woolCount += stack.getCount();
            } else if (stack.getItem() instanceof DyeItem dye) {
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

        int woolToUse = Math.min(woolCount, 64);
        WOOL_USED.set(woolToUse); // 🧠 Pass it to getRemainingItems

        ItemStack result = new ItemStack(ModBlocks.RGB_WOOL_ITEM.get(), woolToUse);
        result.getOrCreateTag().putInt("Color", color);
        return result;
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
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
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
}

