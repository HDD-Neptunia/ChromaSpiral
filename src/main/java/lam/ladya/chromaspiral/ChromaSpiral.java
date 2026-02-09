package lam.ladya.chromaspiral;


import com.mojang.logging.LogUtils;

import lam.ladya.chromaspiral.Config;
import lam.ladya.chromaspiral.ModBlocks;

import lam.ladya.chromaspiral.blocks.ModRecipeSerializers;

import lam.ladya.chromaspiral.chroma.RGBBlockColors;
import lam.ladya.chromaspiral.chroma.RGBItemColors;

import lam.ladya.chromaspiral.networking.RGBNetwork;

import lam.ladya.chromaspiral.station.ChromaDyeTableRenderer;
import lam.ladya.chromaspiral.station.ChromaDyeTableScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import org.slf4j.Logger;


@SuppressWarnings("unused")
@Mod(ChromaSpiral.MODID)
public class ChromaSpiral {
    public static final String MODID = "chromaspiral";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(NeoForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(NeoForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    @SuppressWarnings("removal")
	public ChromaSpiral() {
        net.neoforged.bus.api.IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);

        CREATIVE_MODE_TABS.register(modEventBus);

        ModBlocks.register(modEventBus);  // Your custom block registration class
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModRecipeSerializers.register(modEventBus); // Your recipe serializers
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    	event.enqueueWork(RGBNetwork::register);
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", NeoForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
        Config.items.forEach(item -> LOGGER.info("ITEM >> {}", item));
    }

    @net.neoforged.bus.api.SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @net.neoforged.bus.api.SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        	MenuScreens.register(ModMenuTypes.CHROMA_DYE_TABLE_MENU.value(), ChromaDyeTableScreen::new);
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            // Register block colors etc.
            event.enqueueWork(() -> {
            	RGBItemColors.register(Minecraft.getInstance().getItemColors());
                RGBBlockColors.registerBlockColors(Minecraft.getInstance().getBlockColors());
                BlockEntityRenderers.register(
                        ModBlockEntities.CHROMA_DYE_TABLE.get(),
                        ChromaDyeTableRenderer::new
                    );
               
            });
        }
    }
}

