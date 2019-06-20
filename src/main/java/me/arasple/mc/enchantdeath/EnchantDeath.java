package me.arasple.mc.enchantdeath;

import me.arasple.mc.enchantdeath.deathchest.DeathChestManager;
import me.arasple.mc.enchantdeath.deathmessage.DeathMessageManager;
import me.arasple.mc.enchantdeath.hook.HookPlaceholderAPI;
import me.arasple.mc.enchantdeath.listeners.ListenerDeathChestProtect;
import me.arasple.mc.enchantdeath.listeners.ListenerDeathChestRetrieve;
import me.arasple.mc.enchantdeath.listeners.ListenerPlayerDeath;
import me.arasple.mc.enchantdeath.listeners.ListenerPlayerRespawn;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 附魔死亡 EnchantDeath
 * <p>
 * hmmm
 * <p>
 * 高度自定义 / 完善的系统 / 强大的拓展性 —— 死亡管理系统
 * <p>
 * * 无依赖, 轻量级
 * * 所有消息均支持 JSON、PlaceholderAPI
 * * 每个世界可独立设置
 * * 自定义随机死亡消息, 支持一切死亡类型
 * * 自定义死亡后背包掉落方式
 * * 自定义死亡盒材质、取回方式、逾期时间, 高度可编辑
 * * 事件支持自定义后台拓展命令, 扣钱、加药水效果、增音效 一气呵成
 * * 死亡统计系统, 死亡排行榜. 提供 PlaceholderAPI变量
 *
 * @author Arasple (垃圾代码, 大佬勿喷)
 */
public final class EnchantDeath extends JavaPlugin {

    private static EnchantDeath instance;

    /*
    Getters
     */
    public static EnchantDeath getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        register();
    }

    @Override
    public void onDisable() {
        DeathChestManager.saveDeathChests();
        EDFiles.saveDataToFile();
    }

    private void register() {
        // Instance
        instance = this;
        // Files
        EDFiles.loadFiles();
        EDFiles.loadConfigurations();
        // Hook
        if (HookPlaceholderAPI.isHooked()) {
            Msger.log("Plugin.hook-papi");
        }
        // Listeners
        getServer().getPluginManager().registerEvents(new ListenerPlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new ListenerDeathChestProtect(), this);
        getServer().getPluginManager().registerEvents(new ListenerDeathChestRetrieve(), this);
        // Command
        getServer().getPluginCommand("enchantdeath").setExecutor(new EDCommands());
        // DeathChests
        DeathChestManager.loadDeathChests();
        Msger.sendTo(Bukkit.getConsoleSender(), EDFiles.getMessages().getString("DeathChest.load").replace("{V}", String.valueOf(DeathChestManager.getDeathChests().size())));
        // Messages
        DeathMessageManager.loadMessages();
        Msger.sendTo(Bukkit.getConsoleSender(), EDFiles.getMessages().getString("DeathMessages.load").replace("{V}", DeathMessageManager.getCount()));
        // PrintLog
        Msger.sendTo(Bukkit.getConsoleSender(), EDFiles.getMessages().getString("Plugin.enable").replace("{V}", getDescription().getVersion()));
    }

}
