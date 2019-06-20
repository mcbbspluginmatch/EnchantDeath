package me.arasple.mc.enchantdeath.listeners;

import me.arasple.mc.enchantdeath.EDFiles;
import me.arasple.mc.enchantdeath.deathchest.DeathChest;
import me.arasple.mc.enchantdeath.deathchest.DeathChestManager;
import me.arasple.mc.enchantdeath.deathmessage.DeathMessageManager;
import me.arasple.mc.enchantdeath.hook.HookPlaceholderAPI;
import me.arasple.mc.enchantdeath.utils.InvItemsUtils;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class ListenerPlayerDeath implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        String worldname = p.getWorld().getName();
        String keepInvType = EDFiles.getSettings().getString("Worlds." + worldname + ".keep-inventory-type");

        // 执行死亡后命令
        Msger.consoleExecute(p, EDFiles.getSettings().getStringList("CommandsAfterDeath"));

        // 死亡后对物品的处理
        // 1. 保留
        if (keepInvType.equalsIgnoreCase("KEEP")) {
            e.setKeepInventory(true);
            Msger.sendString(p, "Inventory.keep");
        // 掉入虚空, 特殊情况
        }else if (p.getLocation().getY()<=0){

        // 2. 全部清除
        } else if (keepInvType.equalsIgnoreCase("REMOVE")) {
            p.getInventory().clear();
            e.setKeepInventory(false);
            Msger.sendString(p, "Inventory.remove");
            // 3. 全部掉落 (原版默认)
        } else if (keepInvType.equalsIgnoreCase("DROP_ALL")) {
            e.setKeepInventory(false);
            Msger.sendString(p, "Inventory.drop-all");
            // 4. 随机掉落一定格数的物品
        } else if (keepInvType.toUpperCase().startsWith("DROP_RANDOM_")) {
            String amountStr = keepInvType.toUpperCase().replace("DROP_RANDOM_", "");
            int amount;
            try {
                amount = Integer.parseInt(amountStr);
                e.setKeepInventory(true);
                ItemStack[] removed = InvItemsUtils.removeRandomly(p.getInventory().getContents(), amount);
                if (removed.length < InvItemsUtils.skipEmpty(p.getInventory().getContents()).length) {
                    p.getInventory().setContents(removed);
                    Msger.sendTo(p, EDFiles.getMessages().getString("Inventory.drop-random").replace("{AMOUNT}", amountStr));
                }
            } catch (Exception ex) {
                Msger.log("&8[&6ED&8] &c配置节点 &6Worlds." + worldname + ".keep-inventory-type &c中未提供整数值.");
                return;
            }
            // 5. 生成死亡箱子
        } else if (keepInvType.equalsIgnoreCase("DEATHCHEST")) {
            if (p.getInventory().getContents().length > 0) {
                long lasts = EDFiles.getSettings().getLong("DeathChest.expire", 600);
                e.setKeepInventory(false);
                DeathChest deathChest = new DeathChest(p.getUniqueId(), p.getLocation().getBlock().getLocation(), p.getInventory().getContents(), System.currentTimeMillis(), System.currentTimeMillis() + lasts * 1000);
                deathChest.loadBlock();
                DeathChestManager.getDeathChests().add(deathChest);
                Msger.sendString(p, "Inventory.deathchest");
            }
        }

        // 死亡后对经验的处理
        double expdrop = EDFiles.getSettings().getDouble("Worlds." + worldname + ".exp-drop");
        if (expdrop != -1) {
            e.setDroppedExp((int) (e.getEntity().getExp() * expdrop));
        }

        // 死亡后对消息处理
        e.setDeathMessage(null);
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
                    HookPlaceholderAPI.replaceWithPapi(p, message.replace("{P}", p.getName())),
                    p
            );
        }
    }

}
