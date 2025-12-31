package com.ladya.chromaspiral.blocks;

import com.ladya.chromaspiral.ChromaSpiral;
import com.ladya.chromaspiral.chroma.RGBWoolDyeRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;



import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ChromaSpiral.MODID);

    public static final RegistryObject<RecipeSerializer<RGBWoolDyeRecipe>> RGB_WOOL_DYE =
            SERIALIZERS.register(
                    "rgb_wool_dye",
                    () -> new SimpleCraftingRecipeSerializer<>(
                            (id, category) -> new RGBWoolDyeRecipe(id)
                    )

            );

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}

