package me.arasple.mc.enchantdeath.hook;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 */
public class HookPlaceholderAPI {

    private final static boolean isHooked;

    static {
        isHooked = Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled();
    }

    public static boolean isHooked() {
        return isHooked;
    }

    public static String replaceWithPapi(Player p, String str) {
        return isHooked() ? PlaceholderAPI.setPlaceholders(p, str) : str;
    }

}
