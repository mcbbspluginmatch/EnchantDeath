package me.arasple.mc.enchantdeath.utils;

import me.arasple.mc.enchantdeath.EDFiles;
import me.arasple.mc.enchantdeath.hook.HookPlaceholderAPI;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Arasple
 */
public class Msger {

    public static void consoleExecute(Player p, List<String> commands) {
        commands.forEach(command -> consoleExecute(p, command));
    }

    private static void consoleExecute(Player p, String command) {
        command = HookPlaceholderAPI.replaceWithPapi(p, command.replace("{P}", p.getName()));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public static void log(String path) {
        sendString(Bukkit.getConsoleSender(), path);
    }

    public static void sendTo(CommandSender target, String message) {
        if (message != null) {
            message = color(target instanceof Player ? HookPlaceholderAPI.replaceWithPapi((Player) target, message) : message);
            if (message.startsWith("[JSON]")) {
                target.spigot().sendMessage(ComponentSerializer.parse(message.substring(6)));
            } else {
                target.sendMessage(message);
            }
        }
    }

    public static void sendString(CommandSender target, String path) {
        String message = EDFiles.getMessages().getString(path, null);
        sendTo(target, message);
    }

    public static void sendStrings(CommandSender target, String path) {
        EDFiles.getMessages().getStringList(path).forEach(str -> sendTo(target, str));
    }

    private static String color(String str) {
        if (str == null) return null;
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
