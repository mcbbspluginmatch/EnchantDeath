package me.arasple.mc.enchantdeath.listeners;

import me.arasple.mc.enchantdeath.EDFiles;
import me.arasple.mc.enchantdeath.EnchantDeath;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ListenerPlayerRespawn implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        // * 这里因为似乎在玩家重生瞬间直接执行的话, 玩家实体获取不到
        // * 所以延时执行
        Bukkit.getScheduler().scheduleSyncDelayedTask(EnchantDeath.getInstance(), () -> Msger.consoleExecute(p, EDFiles.getSettings().getStringList("CommandsAfterRespawn")), 10);
    }

}
