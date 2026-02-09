package lam.ladya.chromaspiral;


import lam.ladya.chromaspiral.station.ChromaDyeTableMenu;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;


public class ModMenuTypes {
    public static final net.neoforged.neoforge.registries.DeferredRegister<MenuType<?>> MENU_TYPES =
            net.neoforged.neoforge.registries.DeferredRegister.create(NeoForgeRegistries.MENU_TYPES, ChromaSpiral.MODID);

    public static final DeferredHolder<MenuType, MenuType<ChromaDyeTableMenu>> CHROMA_DYE_TABLE_MENU =
    	    MENU_TYPES.register("chroma_dye_table_menu",
    	        () -> IForgeMenuType.create(ChromaDyeTableMenu::new));

}

