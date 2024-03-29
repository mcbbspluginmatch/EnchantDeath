package me.arasple.mc.enchantdeath.listeners;

import me.arasple.mc.enchantdeath.EdFiles;
import me.arasple.mc.enchantdeath.EnchantDeath;
import me.arasple.mc.enchantdeath.modules.deathchest.DeathChest;
import me.arasple.mc.enchantdeath.modules.deathchest.DeathChestManager;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * @author Arasple
 * <p>
 * 监听取回死亡物品的不同方式
 */
public class ListenerDeathChestRetrieve implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTouch(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!EnchantDeath.getServerVersion().startsWith("v1_8_R1") && e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (e.getClickedBlock() == null || e.getClickedBlock().isEmpty()) {
            return;
        }

        DeathChest dc = DeathChestManager.getDeathChest(e.getClickedBlock());

        if (dc != null) {
            if (e.getClickedBlock().getState() instanceof Chest) {
                e.setCancelled(true);
            }
            // 预览查看死亡箱信息的操作
            if (p.isSneaking() && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                DeathChestManager.showInfo(p, dc);
                return;
            }
            // 如果取死亡盒的方式设置的是 Touch 交互, 则执行取箱子相关操作
            if ("TOUCH".equalsIgnoreCase(EdFiles.getSettings().getString("DeathChest.retrieve"))) {
                // 匹配到持有者
                if (dc.getOwner().equals(p.getUniqueId())) {
                    DeathChestManager.retrieve(dc, p);
                } else {
                    Msger.sendString(p, "DeathChest.not-yours");
                }
                e.setCancelled(true);
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        DeathChest dc = DeathChestManager.getDeathChest(e.getBlock());

        if (!e.getBlock().isEmpty() && dc != null) {
            // 匹配到持有者
            if (p.getUniqueId().equals(dc.getOwner())) {
                String retrieve = EdFiles.getSettings().getString("DeathChest.retrieve");
                ItemStack tool = p.getPlayer().getInventory().getItemInMainHand();

                // [BREAK] 索取方式
                if ("BREAK".equalsIgnoreCase(retrieve)) {
                    DeathChestManager.retrieve(dc, p);
                    e.setCancelled(true);
                } else if (tool.getType() == Material.AIR) {
                    Msger.sendString(p, "DeathChest.not-special-material");
                    e.setCancelled(true);
                    // 自定义物品索取方式
                } else if ("SPECIAL-MATERIAL".equalsIgnoreCase(retrieve)) {
                    for (String toolType : EdFiles.getSettings().getStringList("DeathChest.special-materials")) {
                        if (tool.getType().name().equalsIgnoreCase(toolType)) {
                            if (!DeathChestManager.retrieve(dc, p)) {
                                e.setCancelled(true);
                            }
                            return;
                        }
                    }
                    e.setCancelled(true);
                    Msger.sendString(p, "DeathChest.not-special-material");
                    // 限定 Lore 物品索取方式
                } else if ("SPECIAL-LORE".equalsIgnoreCase(retrieve)) {
                    if (tool.getItemMeta()==null || tool.getItemMeta().getLore() == null || tool.getItemMeta().getLore().size() == 0) {
                        Msger.sendString(p, "DeathChest.no-special-lore");
                        e.setCancelled(true);
                        return;
                    }
                    for (String l : EdFiles.getSettings().getStringList("DeathChest.special-lores")) {
                        for (String lore : tool.getItemMeta().getLore()) {
                            if (lore.equalsIgnoreCase(l)) {
                                DeathChestManager.retrieve(dc, p);
                                e.setCancelled(true);
                                return;
                            }
                        }
                    }
                    Msger.sendString(p, "DeathChest.no-special-lore");
                }
            } else {
                e.setCancelled(true);
                Msger.sendString(p, "DeathChest.not-yours");
            }
        }
    }

}
