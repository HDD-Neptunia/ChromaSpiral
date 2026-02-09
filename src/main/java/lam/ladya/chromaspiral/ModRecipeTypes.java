package lam.ladya.chromaspiral;


import lam.ladya.chromaspiral.chroma.RGBWoolDyeRecipe;

import net.minecraft.world.item.crafting.RecipeType;

import net.neoforged.neoforge.registries.NeoForgeRegistries;


public class ModRecipeTypes {

    public static final net.neoforged.neoforge.registries.DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            net.neoforged.neoforge.registries.DeferredRegister.create(NeoForgeRegistries.RECIPE_TYPES, ChromaSpiral.MODID);

    public static final DeferredHolder<RecipeType, RecipeType<RGBWoolDyeRecipe>> RGB_WOOL_DYE_RECIPE_TYPE =
            RECIPE_TYPES.register("rgb_wool_dye", () -> new RecipeType<RGBWoolDyeRecipe>() { 
                @Override
                public String toString() {
                    return ChromaSpiral.MODID + ":rgb_wool_dye";
                }
            });
    
    public static class Types {
    	public static final DeferredHolder<RecipeType, RecipeType<RGBWoolDyeRecipe>> RGB_WOOL_DYE_RECIPE_TYPE =
    		    RECIPE_TYPES.register("rgb_wool_dye", () -> new RecipeType<RGBWoolDyeRecipe>() {});


    	}

}


            
