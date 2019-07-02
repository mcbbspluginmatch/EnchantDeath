package me.arasple.mc.enchantdeath.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arasple
 */
public class InvItemsUtils {

    /**
     * 将一些物品添加到一个容器中, 如果容器满了则掉落在指定坐标
     *
     * @param location   坐标
     * @param inventory  目标容器
     * @param itemStacks 添加的物品
     * @return 容器是否已满 / 是否有掉落
     */
    public static boolean addToInventory(Location location, Inventory inventory, ItemStack[] itemStacks) {
        boolean drop = false;
        if (location == null || location.getWorld() == null){
            return false;
        }
        for (ItemStack item : itemStacks) {
            if (!isInvFull(inventory)) {
                inventory.addItem(item);
            } else {
                location.getWorld().dropItemNaturally(location, item);
                drop = !drop;
            }
        }
        return drop;
    }

    /**
     * 强制将一些物品添加到容器中, 若容量不足则一个都不操作
     *
     * @param inventory  目标容器
     * @param itemStacks 添加的物品
     * @return 是否成功
     */
    public static boolean addToInventoryForce(Inventory inventory, ItemStack[] itemStacks) {
        if (getFreeSlot(inventory) < itemStacks.length) {
            return false;
        } else {
            inventory.addItem(itemStacks);
            return true;
        }
    }

    /**
     * 剔除一个 ItemStack[] 对象中的 null/空气
     *
     * @param itemStacks 剔除对象
     * @return 整理后的对象
     */
    public static ItemStack[] skipEmpty(ItemStack[] itemStacks) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack stack : itemStacks) {
            if (stack != null && stack.getType() != Material.AIR) {
                items.add(stack);
            }
        }
        return items.toArray(new ItemStack[0]);
    }

    /**
     * 取得一个容器中剩余的空槽位
     *
     * @param inventory 目标容器
     * @return 是否已满
     */
    private static int getFreeSlot(Inventory inventory) {
        int free = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item == null) {
                free++;
            }
        }
        return free;
    }

    /**
     * 判断一个容器是否槽位已满
     *
     * @param inventory 目标容器
     * @return 是否已满
     */
    private static boolean isInvFull(Inventory inventory) {
        return getFreeSlot(inventory) == 0;
    }

}
