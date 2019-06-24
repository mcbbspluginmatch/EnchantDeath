package me.arasple.mc.enchantdeath.listeners;

import me.arasple.mc.enchantdeath.EdFiles;
import me.arasple.mc.enchantdeath.hook.HookPlaceholderApi;
import me.arasple.mc.enchantdeath.modules.deathinvkeep.DeathInvManager;
import me.arasple.mc.enchantdeath.modules.deathmessage.DeathMessageManager;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author Arasple
 * <p>
 * 监听玩家死亡时的事件
 */
public class ListenerPlayerDeath implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        // 掉入虚空, 特殊情况不处理
        if (p.getLocation().getY() <= 0) {
            return;
        }

        String worldname = p.getWorld().getName();
        String keepInvType = EdFiles.getSettings().getString("Worlds." + worldname + ".keep-inventory-type").toUpperCase();

        // 执行死亡后命令
        Msger.consoleExecute(p, EdFiles.getSettings().getStringList("CommandsAfterDeath"));

        // 死亡后对物品的处理
        if (!(EdFiles.getSettings().getBoolean("General.keep-inv-permission.enable", false) && p.hasPermission(EdFiles.getSettings().getString("General.keep-inv-permission.node", "echantdeath.keepinv")))){
            DeathInvManager.process(p, keepInvType);
        }
        e.setKeepInventory(true);
        e.setDeathMessage(null);

        // 死亡后对经验的处理
        double expdrop = EdFiles.getSettings().getDouble("Worlds." + worldname + ".exp-drop");
        if (expdrop != -1) {
            e.setDroppedExp((int) (e.getEntity().getExp() * expdrop));
        }

        // 死亡后对消息处理
        if (p.getLastDamageCause() != null && p.getLastDamageCause().getCause() != null) {
            EntityDamageEvent.DamageCause cause = p.getLastDamageCause().getCause();
            String message;
            if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager();
                message = DeathMessageManager.getByEntity(damager.getType()).replace("{D}", damager.getName());
            } else {
                message = DeathMessageManager.getByCauses(cause);
            }

            DeathMessageManager.sendDeathMessage(
                    HookPlaceholderApi.replaceWithPapi(p, message.replace("{P}", p.getName())),
                    p
            );
        }
    }

}
