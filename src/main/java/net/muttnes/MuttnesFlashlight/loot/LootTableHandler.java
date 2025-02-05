package net.muttnes.MuttnesFlashlight.loot;

import net.muttnes.MuttnesFlashlight.MuttnesFlashlight;
import net.muttnes.MuttnesFlashlight.Config;
import net.muttnes.MuttnesFlashlight.items.ModItems;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

import java.util.List;

@Mod.EventBusSubscriber(modid = MuttnesFlashlight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LootTableHandler {

    private static final List<String> EXCLUDED_PATHS = List.of(
        "nether",
        "end",
        "stronghold", 
        "bastion", 
        "pyramid",
        "monument",
        "portal",
        "fortress",
        "temple",
        "dungeon",
        "buried_treasure",
        "bonus"
    );
    
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        String lootTablePath = event.getName().toString();
        boolean isChestLootTable = lootTablePath.contains(":chests/");
        boolean isMinecraftLootTable = lootTablePath.startsWith("minecraft:chests/");
        boolean isExcluded = EXCLUDED_PATHS.stream().anyMatch(lootTablePath::contains);
    
        if (isChestLootTable && ((isMinecraftLootTable && !isExcluded) || (!isMinecraftLootTable && Config.INJECT_INTO_OTHER_MODS.get()))) {
            event.getTable().addPool(
                LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.FLASHLIGHT.get())
                        .setWeight(1)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)))
                    )
                    .when(LootItemRandomChanceCondition.randomChance(0.2F))
                    .build()
            );
    
            event.getTable().addPool(
                LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.BATTERY.get())
                        .setWeight(1)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                    )
                    .when(LootItemRandomChanceCondition.randomChance(0.4F))
                    .build()
            );
        }
    }
}
