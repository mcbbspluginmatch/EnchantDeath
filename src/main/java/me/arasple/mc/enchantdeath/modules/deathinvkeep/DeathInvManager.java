package me.arasple.mc.enchantdeath.modules.deathinvkeep;

import me.arasple.mc.enchantdeath.EdFiles;
import me.arasple.mc.enchantdeath.modules.deathchest.DeathChest;
import me.arasple.mc.enchantdeath.modules.deathchest.DeathChestManager;
import me.arasple.mc.enchantdeath.utils.InvItemsUtils;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Arasple
 */
public class DeathInvManager {

    private static final String TYPE_KEEP = "KEEP";
    private static final String TYPE_REMOVE = "REMOVE";
    private static final String TYPE_DROP_ALL = "DROP_ALL";
    private static final String TYPE_DROP_RANDOM = "DROP_RANDOM_";
    private static final String TYPE_DEATHCHEST = "DEATHCHEST";


    public static void process(Player p, String type) {
        if (TYPE_KEEP.equals(type)) {
            // 保留所有物品
            Msger.sendString(p, "Inventory.keep");
        } else if (TYPE_REMOVE.equalsIgnoreCase(type)) {
            // 清空所有物品
            p.getInventory().clear();
            Msger.sendString(p, "Inventory.remove");
        } else if (TYPE_DROP_ALL.equalsIgnoreCase(type)) {
            // 掉落所有物品 (原版默认)
            for (ItemStack itemStack : p.getInventory().getContents()) {
                if (itemStack != null && itemStack.getType() != Material.AIR) {
                    p.getLocation().getWorld().dropItemNaturally(p.getLocation(), itemStack);
                }
            }
            Msger.sendString(p, "Inventory.drop-all");
        } else if (type.toUpperCase().startsWith(TYPE_DROP_RANDOM)) {
            // 随机掉落一定槽位的物品
            String amountStr = type.replace(TYPE_DROP_RANDOM, "");
            int amount;
            try {
                amount = Integer.parseInt(amountStr);

                ItemStack[] invContents = InvItemsUtils.skipEmpty(p.getInventory().getContents());
                List<ItemStack> removedItems = new ArrayList<>();
                while (amount > 0) {
                    int index = new Random().nextInt(invContents.length);
                    removedItems.add(invContents[index]);
                    invContents[index] = null;
                    amount--;
                }
                invContents = InvItemsUtils.skipEmpty(invContents);

                if (invContents.length < InvItemsUtils.skipEmpty(p.getInventory().getContents()).length) {
                    p.getInventory().setContents(invContents);
                    removedItems.forEach(i -> p.getLocation().getWorld().dropItemNaturally(p.getLocation(), i));
                    Msger.sendTo(p, EdFiles.getMessages().getString("Inventory.drop-random").replace("{AMOUNT}", amountStr));
                }
            } catch (Exception ex) {
                Msger.logString("&8[&6ED&8] &c配置节点 &6Worlds." + p.getWorld().getName() + ".keep-inventory-type &c中未提供整数值.");
            }
        } else if (TYPE_DEATHCHEST.equalsIgnoreCase(type)) {
            // 生成死亡盒
            if (InvItemsUtils.skipEmpty(p.getInventory().getContents()).length > 0) {
                long lasts = EdFiles.getSettings().getLong("DeathChest.expire", 600);
                DeathChest dc = new DeathChest(p.getUniqueId(), p.getLocation().getBlock().getLocation(), p.getInventory().getContents(), System.currentTimeMillis(), System.currentTimeMillis() + lasts * 1000);
                dc.loadBlock();
                DeathChestManager.getDeathChests().add(dc);
                p.getInventory().clear(); //清空背包
                Msger.sendString(p, "Inventory.deathchest");
            }
        }
    }

}
