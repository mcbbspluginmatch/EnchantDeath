package me.arasple.mc.enchantdeath.listeners;

import me.arasple.mc.enchantdeath.deathchest.DeathChestManager;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * @author Arasple
 *
 * 监听一些额外的方块事件以防止死亡箱子被破坏.
 */
public class ListenerDeathChestProtect implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplodeBlock(BlockExplodeEvent e) {
        e.blockList().removeIf(DeathChestManager::isDCBlock);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplodeEntity(EntityExplodeEvent e) {
        e.blockList().removeIf(DeathChestManager::isDCBlock);
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent e) {
        if (DeathChestManager.isDCBlock(e.getBlock())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockMove(BlockFromToEvent e) {
        if (DeathChestManager.isDCBlock(e.getToBlock())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e) {
        for (Block b : e.getBlocks()) {
            if (DeathChestManager.isDCBlock(e.getBlock())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        for (Block b : e.getBlocks()) {
            if (DeathChestManager.isDCBlock(e.getBlock())) {
                e.setCancelled(true);
            }
        }
    }

}
