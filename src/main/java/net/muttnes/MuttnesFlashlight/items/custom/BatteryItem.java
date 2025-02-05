package net.muttnes.MuttnesFlashlight.items.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BatteryItem extends Item {

    private static final int CHARGE_AMOUNT = 25;

    public BatteryItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide) {
            return InteractionResultHolder.pass(player.getItemInHand(hand)); // Solo proceder en el servidor
        }

        ItemStack batteryStack = player.getItemInHand(hand);
        InteractionHand otherHand = (hand == InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack flashlightStack = player.getItemInHand(otherHand);

        if (flashlightStack.getItem() instanceof FlashlightItem) {
            int flashlightBatteryLevel = FlashlightItem.getBatteryLevel(flashlightStack);

            if (flashlightBatteryLevel < 100) {
                int newBatteryLevel = Math.min(flashlightBatteryLevel + CHARGE_AMOUNT, 100);
                FlashlightItem.setBatteryLevel(flashlightStack, newBatteryLevel);

                batteryStack.shrink(1);

                return InteractionResultHolder.success(batteryStack);
            }
        }

        return InteractionResultHolder.pass(batteryStack);
    }
}
