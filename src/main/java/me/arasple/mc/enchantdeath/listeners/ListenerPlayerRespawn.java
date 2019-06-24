package me.arasple.mc.enchantdeath.listeners;

import me.arasple.mc.enchantdeath.EdFiles;
import me.arasple.mc.enchantdeath.EnchantDeath;
import me.arasple.mc.enchantdeath.utils.DataHandler;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @author Arasple
 * <p>
 * 监听玩家重生时的事件
 */
public class ListenerPlayerRespawn implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        // * 这里因为似乎在玩家重生瞬间直接执行的话, 玩家实体获取不到
        // * 所以延时执行
        Bukkit.getScheduler().scheduleSyncDelayedTask(EnchantDeath.getInstance(), () -> Msger.consoleExecute(p, EdFiles.getSettings().getStringList("CommandsAfterRespawn")), 10);

        // 给予玩家死亡前的饥饿值, 药水效果

        if (EdFiles.getSettings().getBoolean("General.keep-foodlevel", false)){
            p.setFoodLevel(DataHandler.getFoodLevel().getOrDefault(p.getUniqueId(), 20));
        }
        if (EdFiles.getSettings().getBoolean("General.keep-potion-effects", false)){
            p.addPotionEffects(DataHandler.getPotionEffects().getOrDefault(p.getUniqueId(), null));
        }
    }

}
