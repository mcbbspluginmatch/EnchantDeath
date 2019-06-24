package me.arasple.mc.enchantdeath.utils;

import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/6/24 21:06
 */
public class DataHandler {

    private static HashMap<UUID, Integer> foodLevel = new HashMap<>();
    private static HashMap<UUID, Collection<PotionEffect>> potionEffects = new HashMap<>();

    public static HashMap<UUID, Integer> getFoodLevel() {
        return foodLevel;
    }

    public static HashMap<UUID, Collection<PotionEffect>> getPotionEffects() {
        return potionEffects;
    }

}
