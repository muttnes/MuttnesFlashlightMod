package net.muttnes.MuttnesFlashlight;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Set;

@Mod.EventBusSubscriber(modid = MuttnesFlashlight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue INJECT_INTO_OTHER_MODS = BUILDER
            .comment("Allow if you want the flashlight to appear in other mods chest loots. May or may not break/crash the game")
            .define("injectIntoOtherMods", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

    }
}
