package net.muttnes.MuttnesFlashlight.server;

import java.util.HashMap;
import java.util.UUID;

public class FlashlightLightManager {

    private static final HashMap<UUID, FlashlightLightSource> activeLights = new HashMap<>();

    public static FlashlightLightSource get(UUID id) {
        return activeLights.get(id);
    }

    public static void put(UUID id, FlashlightLightSource source) {
        activeLights.put(id, source);
    }

    public static FlashlightLightSource remove(UUID id) {
        return activeLights.remove(id);
    }

    public static boolean contains(UUID id) {
        return activeLights.containsKey(id);
    }

    public static void clearAll() {
        for (FlashlightLightSource source : activeLights.values()) {
            source.remove();
        }
        activeLights.clear();
    }
}