package lam.ladya.chromaspiral;


import lam.ladya.chromaspiral.blocks.RGBWool;
import lam.ladya.chromaspiral.station.ChromaDyeTableBlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;


public class ModBlocks {

    public static final net.neoforged.neoforge.registries.DeferredRegister<Block> BLOCKS =
            net.neoforged.neoforge.registries.DeferredRegister.create(NeoForgeRegistries.BLOCK, ChromaSpiral.MODID);

    public static final net.neoforged.neoforge.registries.DeferredRegister<Item> ITEMS =
            net.neoforged.neoforge.registries.DeferredRegister.create(NeoForgeRegistries.ITEMS, ChromaSpiral.MODID);

    
    
    public static final net.neoforged.neoforge.registries.DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            net.neoforged.neoforge.registries.DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChromaSpiral.MODID);

    
    
    public static final DeferredHolder<Block, Block> RGB_WOOL =
            BLOCKS.register("rgb_wool", RGBWool::new);
    
    public static final DeferredHolder<Block, Block> CHROMA_DYE_TABLE_BLOCK =
            BLOCKS.register("chroma_dye_table", ChromaDyeTableBlock::new);

    
    
    public static final DeferredHolder<Item, Item> RGB_WOOL_ITEM =
            ITEMS.register("rgb_wool",
                    () -> new BlockItem(RGB_WOOL.value(), new Item.Properties()));
    
    public static final DeferredHolder<Item, Item> CHROMA_DYE_TABLE_ITEM =
            ITEMS.register("chroma_dye_table",
                    () -> new BlockItem(CHROMA_DYE_TABLE_BLOCK.value(), new Item.Properties()));

    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RGB_WOOL_TAB =
            CREATIVE_TABS.register("rgb_wool_tab", () ->
                    CreativeModeTab.builder()
                            .title(Component.literal("RGB Wool Tab"))
                            .icon(() -> new ItemStack(RGB_WOOL_ITEM.value())) // ← SUPPLIER ✔
                            .displayItems((params, output) -> {
                                output.accept(RGB_WOOL_ITEM.value());
                                output.accept(CHROMA_DYE_TABLE_BLOCK.value());
                            })
                            .build()
            );

    public static void register(net.neoforged.bus.api.IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        CREATIVE_TABS.register(bus);
    }
}


