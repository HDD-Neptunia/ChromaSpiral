package com.ladya.chromaspiral;

import com.ladya.chromaspiral.station.ChromaDyeTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ChromaSpiral.MODID);

    public static final RegistryObject<MenuType<ChromaDyeTableMenu>> CHROMA_DYE_TABLE_MENU =
    	    MENU_TYPES.register("chroma_dye_table_menu",
    	        () -> IForgeMenuType.create(ChromaDyeTableMenu::new));

}

