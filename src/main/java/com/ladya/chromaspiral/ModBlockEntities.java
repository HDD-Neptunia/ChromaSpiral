package com.ladya.chromaspiral;



import com.ladya.chromaspiral.station.ChromaDyeTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ChromaSpiral.MODID);

    public static final RegistryObject<BlockEntityType<ChromaDyeTableBlockEntity>> CHROMA_DYE_TABLE =
            BLOCK_ENTITIES.register("chroma_dye_table",
                    () -> BlockEntityType.Builder.of(
                            ChromaDyeTableBlockEntity::new,
                            ModBlocks.CHROMA_DYE_TABLE_BLOCK.get()
                    ).build(null));
}
