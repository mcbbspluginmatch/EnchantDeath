package me.arasple.mc.enchantdeath.listeners;

import me.arasple.mc.enchantdeath.EDFiles;
import me.arasple.mc.enchantdeath.EnchantDeath;
import me.arasple.mc.enchantdeath.deathchest.DeathChest;
import me.arasple.mc.enchantdeath.deathchest.DeathChestManager;
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

        if (!EnchantDeath.getServerVersion().startsWith("v1_8") && e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (e.getClickedBlock() == null || e.getClickedBlock().isEmpty()) {
            return;
        }

        if (e.getClickedBlock().getState() instanceof Chest) {
            e.setCancelled(true);
        }

        DeathChest dc = DeathChestManager.getDeathChest(e.getClickedBlock());

        if (dc != null) {
            // 预览查看死亡箱信息的操作
            if (p.isSneaking() && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                DeathChestManager.showInfo(p, dc);
                return;
            }
            // 如果取死亡盒的方式设置的是 Touch 交互, 则执行取箱子相关操作
            if (EDFiles.getSettings().getString("DeathChest.retrieve").equalsIgnoreCase("TOUCH")) {
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

        if (e.getBlock() != null && !e.getBlock().isEmpty() && dc != null) {
            // 匹配到持有者
            if (p.getUniqueId().equals(dc.getOwner())) {
                String retrieve = EDFiles.getSettings().getString("DeathChest.retrieve");
                ItemStack tool = p.getPlayer().getInventory().getItemInMainHand();

                // [BREAK] 索取方式
                if (retrieve.equalsIgnoreCase("BREAK")) {
                    DeathChestManager.retrieve(dc, p);
                    e.setCancelled(true);
                } else if (tool == null || tool.getType() == Material.AIR) {
                    Msger.sendString(p, "DeathChest.not-special-material");
                    e.setCancelled(true);
                    // 自定义物品索取方式
                } else if (retrieve.equalsIgnoreCase("SPECIAL-MATERIAL")) {
                    for (String toolType : EDFiles.getSettings().getStringList("DeathChest.special-materials")) {
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
                } else if (retrieve.equalsIgnoreCase("SPECIAL-LORE")) {
                    if (tool.getItemMeta().getLore() == null || tool.getItemMeta().getLore().size() == 0) {
                        Msger.sendString(p, "DeathChest.no-special-lore");
                        e.setCancelled(true);
                        return;
                    }
                    for (String l : EDFiles.getSettings().getStringList("DeathChest.special-lores")) {
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
