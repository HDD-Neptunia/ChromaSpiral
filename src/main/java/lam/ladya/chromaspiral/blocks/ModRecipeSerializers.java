package lam.ladya.chromaspiral.blocks;


import lam.ladya.chromaspiral.ChromaSpiral;
import lam.ladya.chromaspiral.chroma.RGBWoolDyeRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;

import net.neoforged.neoforge.registries.NeoForgeRegistries;


public class ModRecipeSerializers {

    public static final net.neoforged.neoforge.registries.DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            net.neoforged.neoforge.registries.DeferredRegister.create(NeoForgeRegistries.RECIPE_SERIALIZERS, ChromaSpiral.MODID);

    public static final RegistryObject<RecipeSerializer<?>> RGB_WOOL_DYE =
    	    SERIALIZERS.register("rgb_wool_dye", RGBWoolDyeRecipe.Serializer::new);




    	
    public static void register(net.neoforged.bus.api.IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}

