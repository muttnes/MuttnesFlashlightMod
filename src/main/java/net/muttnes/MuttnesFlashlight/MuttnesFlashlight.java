package net.muttnes.MuttnesFlashlight;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
//import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.muttnes.MuttnesFlashlight.client.FlashlightHUD;
import net.muttnes.MuttnesFlashlight.items.ModItems;
import net.muttnes.MuttnesFlashlight.items.custom.FlashlightItem;

//import org.slf4j.Logger;

@Mod(MuttnesFlashlight.MODID)
public class MuttnesFlashlight
{
    public static final String MODID = "muttnesflashlight";
    //private static final Logger LOGGER = LogUtils.getLogger();

    public MuttnesFlashlight()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.FLASHLIGHT);
            event.accept(ModItems.BATTERY);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        
        {
            ItemProperties.register(ModItems.FLASHLIGHT.get(), new ResourceLocation("on"), 
            (stack, world, entity, seed) -> FlashlightItem.isFlashlightOn(stack) ? 1.0F : 0.0F);

            MinecraftForge.EVENT_BUS.register(FlashlightHUD.class);

        }

        @SubscribeEvent
        public static void onModelRegistryEvent(ModelEvent.RegisterAdditional event) {
            event.register(new ResourceLocation("muttnesflashlight", "item/flashlight"));

        }

    }
}
