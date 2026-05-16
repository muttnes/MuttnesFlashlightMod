package net.muttnes.MuttnesFlashlight.items.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.muttnes.MuttnesFlashlight.state.FlashlightState;
import net.muttnes.MuttnesFlashlight.service.FlashlightController;

import java.util.UUID;

public class FlashlightItem extends Item {

    private static final int TICK_BATTERY_INTERVAL = 240;

    public FlashlightItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        FlashlightState.ensureId(stack);

        if (!level.isClientSide) {
            toggleState(stack);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private void toggleState(ItemStack stack) {

        if (!FlashlightState.isOn(stack) && FlashlightState.isEmpty(stack)) {
            return;
        }

        FlashlightState.toggle(stack);

        if (FlashlightState.isEmpty(stack)) {
            FlashlightState.turnOff(stack);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide || !(entity instanceof ServerPlayer player)) return;

        init(stack);

        if (FlashlightState.isEmpty(stack)) {
            FlashlightState.turnOff(stack);
            FlashlightController.removeLight(getId(stack));
            return;
        }

        if (!isHolding(player, stack) || !FlashlightState.isOn(stack)) {
            FlashlightController.removeLight(getId(stack));
            return;
        }

        UUID id = getId(stack);

        FlashlightController.ensureLight(
            (ServerLevel) level,
            player,
            stack,
            id
        );
        FlashlightController.updateLight(id);

        handleBattery(stack);
    }

    private void init(ItemStack stack) {
        if (!stack.getOrCreateTag().contains("tickCounter")) {
            stack.getOrCreateTag().putInt("tickCounter", 0);
        }
    }

    private boolean isHolding(ServerPlayer player, ItemStack stack) {
        return player.getMainHandItem().equals(stack)
                || player.getOffhandItem().equals(stack);
    }

    private void handleBattery(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        int ticks = tag.getInt("tickCounter") + 1;
        tag.putInt("tickCounter", ticks);

        if (ticks < TICK_BATTERY_INTERVAL) {
            return;
        }

        FlashlightState.drain(stack, 1);
        tag.putInt("tickCounter", 0);

        if (FlashlightState.isEmpty(stack)) {
            FlashlightState.turnOff(stack);
            FlashlightController.removeLight(getId(stack));
        }
    }

    private UUID getId(ItemStack stack) {
        return stack.getOrCreateTag().getUUID("flashlightId");
    }

    @Override
    public boolean shouldCauseReequipAnimation(
            ItemStack oldStack,
            ItemStack newStack,
            boolean slotChanged
    ) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }
}