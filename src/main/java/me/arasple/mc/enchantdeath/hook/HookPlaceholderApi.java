package me.arasple.mc.enchantdeath.hook;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 */
public class HookPlaceholderApi {

    private final static boolean IS_HOOKED;

    static {
        IS_HOOKED = Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled();
    }

    public static boolean isHooked() {
        return IS_HOOKED;
    }

    public static String replaceWithPapi(Player p, String str) {
        return isHooked() ? PlaceholderAPI.setPlaceholders(p, str) : str;
    }

}
