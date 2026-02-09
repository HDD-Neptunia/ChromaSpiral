package lam.ladya.chromaspiral;


import lam.ladya.chromaspiral.station.ChromaDyeTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.neoforged.neoforge.registries.NeoForgeRegistries;


public class ModBlockEntities {
    public static final net.neoforged.neoforge.registries.DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            net.neoforged.neoforge.registries.DeferredRegister.create(NeoForgeRegistries.BLOCK_ENTITY_TYPES, ChromaSpiral.MODID);

    public static final DeferredHolder<BlockEntityType<ChromaDyeTableBlockEntity>> CHROMA_DYE_TABLE =
            BLOCK_ENTITIES.register("chroma_dye_table",
                    () -> BlockEntityType.Builder.of(
                            ChromaDyeTableBlockEntity::new,
                            ModBlocks.CHROMA_DYE_TABLE_BLOCK.get()
                    ).build(null));
}
