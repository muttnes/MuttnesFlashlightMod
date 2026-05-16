package net.muttnes.MuttnesFlashlight.config;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.muttnes.MuttnesFlashlight.MuttnesFlashlight;

import java.util.Set;

@Mod.EventBusSubscriber(modid = MuttnesFlashlight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.EnumValue<LootInjectionMode> LOOT_MODE =
        BUILDER.comment(
                "Controls loot injection behavior:\n" +
                "VANILLA_ONLY = only vanilla Minecraft chests\n" +
                "ALL_CHESTS = vanilla + other mods\n" +
                "DISABLED = no loot injection"
        )
        .defineEnum("lootInjectionMode", LootInjectionMode.VANILLA_ONLY);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

    }
}
