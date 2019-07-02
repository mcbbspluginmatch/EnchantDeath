package me.arasple.mc.enchantdeath.utils;

import me.arasple.mc.enchantdeath.EdFiles;
import me.arasple.mc.enchantdeath.hook.HookPlaceholderApi;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
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

    private static final String JSON_PREFIX = "[JSON]";

    /**
     * 发送一条消息到目标
     *
     * @param target  目标(后台/玩家)
     * @param message 消息内容
     */
    public static void sendTo(CommandSender target, String message) {
        if (message != null) {
            message = color(target instanceof Player ? HookPlaceholderApi.replaceWithPapi((Player) target, message) : message);
            BaseComponent[] msg = message.startsWith(JSON_PREFIX) ? ComponentSerializer.parse(message.substring(6)) : TextComponent.fromLegacyText(message);

            if (target instanceof Player) {
                ((Player) target).spigot().sendMessage(msg);
            } else {
                target.sendMessage(TextComponent.toLegacyText(msg));
            }
        }
    }

    /**
     * 后台执行命令组
     *
     * @param p        玩家
     * @param commands 命令组
     */
    public static void consoleExecute(Player p, List<String> commands) {
        commands.forEach(command -> consoleExecute(p, command));
    }

    /**
     * 后台执行命令一条, 替换 PlaceholderAPI
     *
     * @param p       玩家
     * @param command 命令
     */
    private static void consoleExecute(Player p, String command) {
        command = HookPlaceholderApi.replaceWithPapi(p, command.replace("{P}", p.getName()));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), color(command));
    }

    /**
     * 发送插件消息配置中的字符串到后台
     *
     * @param path 消息路径
     */
    public static void logString(String path) {
        sendString(Bukkit.getConsoleSender(), path);
    }

    /**
     * 发送插件消息配置中的字符串
     *
     * @param target 目标
     * @param path   消息路径
     */
    public static void sendString(CommandSender target, String path) {
        String message = EdFiles.getMessages().getString(path, null);
        sendTo(target, message);
    }

    /**
     * 发送插件消息配置中的字符串
     *
     * @param target 目标
     * @param path   消息路径
     */
    public static void sendStrings(CommandSender target, String path) {
        EdFiles.getMessages().getStringList(path).forEach(str -> sendTo(target, str));
    }

    /**
     * 替换字符串的颜色代码
     *
     * @param str 目标字符串
     * @return 替换后的字符串
     */
    private static String color(String str) {
        if (str == null) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
