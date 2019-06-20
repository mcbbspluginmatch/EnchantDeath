package me.arasple.mc.enchantdeath.listeners;

import me.arasple.mc.enchantdeath.modules.deathchest.DeathChestManager;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * @author Arasple
 * <p>
 * 监听一些额外的方块事件以防止死亡箱子被破坏.
 */
public class ListenerDeathChestProtect implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplodeBlock(BlockExplodeEvent e) {
        e.blockList().removeIf(DeathChestManager::isDcBlock);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplodeEntity(EntityExplodeEvent e) {
        e.blockList().removeIf(DeathChestManager::isDcBlock);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockMove(BlockFromToEvent e) {
        if (DeathChestManager.isDcBlock(e.getToBlock())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPistonExtend(BlockPistonExtendEvent e) {
        for (Block b : e.getBlocks()) {
            if (DeathChestManager.isDcBlock(b)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPistonRetract(BlockPistonRetractEvent e) {
        for (Block b : e.getBlocks()) {
            if (DeathChestManager.isDcBlock(b)) {
                e.setCancelled(true);
            }
        }
    }

}
