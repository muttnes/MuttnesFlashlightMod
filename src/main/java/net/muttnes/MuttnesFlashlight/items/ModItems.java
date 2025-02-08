package net.muttnes.MuttnesFlashlight.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.muttnes.MuttnesFlashlight.MuttnesFlashlight;
import net.muttnes.MuttnesFlashlight.items.custom.BatteryItem;
import net.muttnes.MuttnesFlashlight.items.custom.FlashlightItem;
import net.muttnes.MuttnesFlashlight.network.ModNetwork;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, MuttnesFlashlight.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    } 
    
    public static final RegistryObject<Item> FLASHLIGHT = ITEMS.register("flashlight",
    () -> new FlashlightItem(new Item.Properties(), ModNetwork.CHANNEL));

    public static final RegistryObject<Item> BATTERY = ITEMS.register("battery",
        () -> new BatteryItem(new Item.Properties()));

}
