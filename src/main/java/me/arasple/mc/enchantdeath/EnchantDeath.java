package me.arasple.mc.enchantdeath;

import me.arasple.mc.enchantdeath.bstats.Metrics;
import me.arasple.mc.enchantdeath.hook.HookPlaceholderApi;
import me.arasple.mc.enchantdeath.listeners.ListenerDeathChestProtect;
import me.arasple.mc.enchantdeath.listeners.ListenerDeathChestRetrieve;
import me.arasple.mc.enchantdeath.listeners.ListenerPlayerDeath;
import me.arasple.mc.enchantdeath.listeners.ListenerPlayerRespawn;
import me.arasple.mc.enchantdeath.modules.deathchest.DeathChestManager;
import me.arasple.mc.enchantdeath.modules.deathmessage.DeathMessageManager;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
// 有javadoc注释标明作者好评 —— yinyangshi
/**
 * 附魔死亡 EnchantDeath
 *
 * @author Arasple
 */
public final class EnchantDeath extends JavaPlugin {

    private static EnchantDeath instance;
    private static String serverVersion;

    /**
     * 取得插件主体实例
     *
     * @return EnchantDeath
     */
    public static EnchantDeath getInstance() {
        return instance;
    }

    /**
     * 取得服务器版本
     *
     * @return nms版本号
     */
    public static String getServerVersion() {
        return serverVersion;
    }

    @Override
    public void onEnable() {
        register();
    }

    @Override
    public void onDisable() {
        DeathChestManager.saveDeathChests();
        EdFiles.saveDataToFile();
    }

    private void register() {
        // Instance
        instance = this;
        // Server Version
        serverVersion = getServer().getClass().getPackage().getName().split("\\.")[3];
        // Metrics (bStats.org)
        new Metrics(this);
        // Files
        EdFiles.loadFiles();
        EdFiles.loadConfigurations();
        // Hook
        if (HookPlaceholderApi.isHooked()) {
            Msger.logString("Plugin.hook-papi");
        }
        // Listeners
        getServer().getPluginManager().registerEvents(new ListenerPlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new ListenerDeathChestProtect(), this);
        getServer().getPluginManager().registerEvents(new ListenerDeathChestRetrieve(), this);
        // Command
        getServer().getPluginCommand("enchantdeath").setExecutor(new EdCommands());
        // DeathChests
        DeathChestManager.loadDeathChests();
        Msger.sendTo(Bukkit.getConsoleSender(), EdFiles.getMessages().getString("DeathChest.load").replace("{V}", String.valueOf(DeathChestManager.getDeathChests().size())));
        // Messages
        DeathMessageManager.loadMessages();
        Msger.sendTo(Bukkit.getConsoleSender(), EdFiles.getMessages().getString("DeathMessages.load").replace("{V}", DeathMessageManager.getCount()));
        // PrintLog
        Msger.sendTo(Bukkit.getConsoleSender(), EdFiles.getMessages().getString("Plugin.enable").replace("{V}", getDescription().getVersion()));
    }

}
