package net.muttnes.MuttnesFlashlight.items.custom;

import atomicstryker.dynamiclights.server.DynamicLights;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.muttnes.MuttnesFlashlight.client.FlashlightLightSource;

import java.util.HashMap;
import java.util.UUID;

public class FlashlightItem extends Item {

    private static final HashMap<UUID, FlashlightLightSource> activeLights = new HashMap<>();

    public FlashlightItem(Properties properties) {
        super(properties);
        properties.stacksTo(1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack flashlightStack = player.getItemInHand(hand);

        boolean isOn = flashlightStack.getOrCreateTag().getBoolean("on");
        flashlightStack.getOrCreateTag().putBoolean("on", !isOn);
    
        ensureFlashlightHasId(flashlightStack);
        UUID flashlightId = flashlightStack.getOrCreateTag().getUUID("flashlightId");
    
        if (!world.isClientSide) {
            if (flashlightStack.getOrCreateTag().getBoolean("on")) {
                if (!activeLights.containsKey(flashlightId)) {
                    FlashlightLightSource lightSource = new FlashlightLightSource(world, player, 13);
                    DynamicLights.addLightSource(lightSource);
                    activeLights.put(flashlightId, lightSource);
                }
            } else {
                removeLightSource(flashlightStack);
            }
        }
    
        return InteractionResultHolder.sidedSuccess(flashlightStack, world.isClientSide);
    }     
    
    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && entity instanceof Player player) {
            if (!stack.getOrCreateTag().contains("batteryLevel")) {
                stack.getOrCreateTag().putInt("batteryLevel", 100);
            }
    
            if (!stack.getOrCreateTag().contains("tickCounter")) {
                stack.getOrCreateTag().putInt("tickCounter", 0);
            }
    
            boolean isHoldingFlashlight = player.getItemInHand(InteractionHand.MAIN_HAND).equals(stack) ||
                                          player.getItemInHand(InteractionHand.OFF_HAND).equals(stack);
            boolean isFlashlightOn = isFlashlightOn(stack);
    
            int currentTicks = stack.getOrCreateTag().getInt("tickCounter");
    
            if (!isHoldingFlashlight || !isFlashlightOn) {
                stack.getOrCreateTag().putInt("tickCounter", currentTicks);
                removeLightSource(stack);
                return;
            }
    
            ensureFlashlightHasId(stack);
            UUID flashlightId = stack.getOrCreateTag().getUUID("flashlightId");
    
            if (!activeLights.containsKey(flashlightId)) {
                FlashlightLightSource lightSource = new FlashlightLightSource(world, player, 13);
                DynamicLights.addLightSource(lightSource);
                activeLights.put(flashlightId, lightSource);
            }
    
            if (activeLights.containsKey(flashlightId)) {
                activeLights.get(flashlightId).update();
            }
    
            currentTicks++;
            stack.getOrCreateTag().putInt("tickCounter", currentTicks);

            shouldCauseReequipAnimation(stack, stack, isFlashlightOn);
    
            if (currentTicks >= 240) {
                drainBattery(stack);
                stack.getOrCreateTag().putInt("tickCounter", 0);
    
                if (getBatteryLevel(stack) <= 0) {
                    turnOffFlashlight(stack);
                }
            }
        }
    }    
    
    private void removeLightSource(ItemStack stack) {
        if (stack.getOrCreateTag().contains("flashlightId")) {
            UUID flashlightId = stack.getOrCreateTag().getUUID("flashlightId");
            if (activeLights.containsKey(flashlightId)) {
                activeLights.get(flashlightId).remove();
                activeLights.remove(flashlightId);
            }
            stack.getOrCreateTag().remove("flashlightId");
        }
    }
    
    private void ensureFlashlightHasId(ItemStack stack) {
        if (!stack.getOrCreateTag().contains("flashlightId")) {
            stack.getOrCreateTag().putUUID("flashlightId", UUID.randomUUID());
        }
    }
    
    private void turnOffFlashlight(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("on", false);
        removeLightSource(stack);
    }
    

    public static void drainBattery(ItemStack stack) {
        int batteryLevel = stack.getOrCreateTag().getInt("batteryLevel");
        if (batteryLevel > 0) {
            stack.getOrCreateTag().putInt("batteryLevel", batteryLevel - 1);
            stack.getOrCreateTag().putInt("tickCounter", 0);
        }
    }

    public static void setBatteryLevel(ItemStack stack, int level) {
        stack.getOrCreateTag().putInt("batteryLevel", Math.min(level, 100));
    }
    
    public static int getBatteryLevel(ItemStack stack) {
        return stack.getOrCreateTag().getInt("batteryLevel");
    }

    public static boolean isFlashlightOn(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("on");
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}
