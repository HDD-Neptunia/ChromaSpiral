package com.ladya.chromaspiral;

import com.ladya.chromaspiral.blocks.RGBWool;
import com.ladya.chromaspiral.station.ChromaDyeTableBlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;




import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, ChromaSpiral.MODID);

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ChromaSpiral.MODID);

    
    
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChromaSpiral.MODID);

    
    
    public static final RegistryObject<Block> RGB_WOOL =
            BLOCKS.register("rgb_wool", RGBWool::new);
    
    public static final RegistryObject<Block> CHROMA_DYE_TABLE_BLOCK =
            BLOCKS.register("chroma_dye_table_block", ChromaDyeTableBlock::new);

    
    
    public static final RegistryObject<Item> RGB_WOOL_ITEM =
            ITEMS.register("rgb_wool",
                    () -> new BlockItem(RGB_WOOL.get(), new Item.Properties()));
    
    public static final RegistryObject<Item> CHROMA_DYE_TABLE_ITEM =
            ITEMS.register("chroma_dye_table",
                    () -> new BlockItem(CHROMA_DYE_TABLE_BLOCK.get(), new Item.Properties()));

    
    public static final RegistryObject<CreativeModeTab> RGB_WOOL_TAB =
            CREATIVE_TABS.register("rgb_wool_tab", () ->
                    CreativeModeTab.builder()
                            .title(Component.literal("RGB Wool Tab"))
                            .icon(() -> new ItemStack(RGB_WOOL_ITEM.get())) // ← SUPPLIER ✔
                            .displayItems((params, output) -> {
                                output.accept(RGB_WOOL_ITEM.get());
                            })
                            .build()
            );

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        CREATIVE_TABS.register(bus);
    }
}


