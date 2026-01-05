package com.ladya.chromaspiral;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.ladya.chromaspiral.chroma.RGBWoolDyeRecipe;

public class ModRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ChromaSpiral.MODID);

    public static final RegistryObject<RecipeType<RGBWoolDyeRecipe>> RGB_WOOL_DYE_RECIPE_TYPE =
            RECIPE_TYPES.register("rgb_wool_dye", () -> new RecipeType<RGBWoolDyeRecipe>() { 
                @Override
                public String toString() {
                    return ChromaSpiral.MODID + ":rgb_wool_dye";
                }
            });
    
    public static class Types {
    	public static final RegistryObject<RecipeType<RGBWoolDyeRecipe>> RGB_WOOL_DYE_RECIPE_TYPE =
    		    RECIPE_TYPES.register("rgb_wool_dye", () -> new RecipeType<RGBWoolDyeRecipe>() {});


    	}

}


            
