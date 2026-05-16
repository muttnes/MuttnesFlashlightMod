package net.muttnes.MuttnesFlashlight.state;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class FlashlightState {

    private static final String ON_KEY = "on";
    private static final String BATTERY_KEY = "batteryLevel";
    private static final String ID_KEY = "flashlightId";

    private static CompoundTag tag(ItemStack stack) {
        return stack.getOrCreateTag();
    }

    public static boolean isOn(ItemStack stack) {
        return tag(stack).getBoolean(ON_KEY);
    }

    public static void setOn(ItemStack stack, boolean value) {
        tag(stack).putBoolean(ON_KEY, value);
    }

    public static void toggle(ItemStack stack) {
        setOn(stack, !isOn(stack));
    }

    public static int getBattery(ItemStack stack) {
        CompoundTag tag = tag(stack);

        if (!tag.contains(BATTERY_KEY)) {
            tag.putInt(BATTERY_KEY, 100);
        }

        return tag.getInt(BATTERY_KEY);
    }

    public static void setBattery(ItemStack stack, int value) {
        tag(stack).putInt(BATTERY_KEY, Math.max(0, Math.min(100, value)));
    }

    public static void drain(ItemStack stack, int amount) {
        setBattery(stack, getBattery(stack) - amount);
    }

    public static void add(ItemStack stack, int amount) {
        setBattery(stack, getBattery(stack) + amount);
    }

    public static boolean isEmpty(ItemStack stack) {
        return getBattery(stack) <= 0;
    }

    public static boolean canCharge(ItemStack stack) {
        return getBattery(stack) < 100;
    }

    public static UUID getId(ItemStack stack) {
        CompoundTag tag = tag(stack);

        if (!tag.contains(ID_KEY)) {
            UUID id = UUID.randomUUID();
            tag.putUUID(ID_KEY, id);
            return id;
        }

        return tag.getUUID(ID_KEY);
    }

    public static void ensureId(ItemStack stack) {
        getId(stack);
    }

    public static void turnOff(ItemStack stack) {
        setOn(stack, false);
    }
}