package me.arasple.mc.enchantdeath.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
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
    public static boolean addToInventory(Location location, PlayerInventory inventory, ItemStack[] itemStacks) {
        boolean drop = false;
        if (location == null || location.getWorld() == null){
            return false;
        }
        for (ItemStack item : itemStacks) {
            if (!isInvFull(inventory)) {
                addItemToInv(inventory, item);
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
    public static boolean addToInventoryForce(PlayerInventory inventory, ItemStack[] itemStacks) {
        if (getFreeSlot(inventory) < itemStacks.length) {
            return false;
        } else {
            addItemToInv(inventory, itemStacks);
            return true;
        }
    }

    private static void addItemToInv(PlayerInventory inventory, ItemStack... itemStacks){
        List<ItemStack> armors = new ArrayList<>(inventory.addItem(itemStacks).values());
        armors.forEach(itemStack -> {
            String type = itemStack.getType().name().toLowerCase();
            if (type.endsWith("helmet")){
                inventory.setHelmet(itemStack);
            }else if (type.endsWith("chestplate")){
                inventory.setChestplate(itemStack);
            }else if (type.endsWith("leggings")){
                inventory.setLeggings(itemStack);
            }else if (type.endsWith("boots")){
                inventory.setBoots(itemStack);
            }else if (inventory.getItemInMainHand().getType() == Material.AIR){
                inventory.setItemInMainHand(itemStack);
            }else if (inventory.getItemInOffHand().getType() == Material.AIR) {
                inventory.setItemInOffHand(itemStack);
            }
        });
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
    private static int getFreeSlot(PlayerInventory inventory) {
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
    private static boolean isInvFull(PlayerInventory inventory) {
        return getFreeSlot(inventory) == 0;
    }

}
