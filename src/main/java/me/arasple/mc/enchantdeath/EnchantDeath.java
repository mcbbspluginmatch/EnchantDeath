package me.arasple.mc.enchantdeath;

import me.arasple.mc.enchantdeath.bstats.Metrics;
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
 *
 * @author Arasple (垃圾代码, 大佬勿喷)
 */
public final class EnchantDeath extends JavaPlugin {

    private static EnchantDeath instance;
    private static String serverVersion;

    /*
    Getters
     */
    public static EnchantDeath getInstance() {
        return instance;
    }

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
        EDFiles.saveDataToFile();
    }

    private void register() {
        // Instance
        instance = this;
        // Server Version
        serverVersion = getServer().getClass().getPackage().getName().split("\\.")[3];
        // Metrics (bStats.org)
        new Metrics(this);
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
