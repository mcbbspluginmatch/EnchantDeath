package me.arasple.mc.enchantdeath.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Arasple
 */
public class InvItemsUtils {

    public static ItemStack[] removeRandomly(ItemStack[] items, int amount) {
        items = skipEmpty(items);
        if (items.length < amount) {
            return items;
        } else if (items.length == amount) {
            return new ItemStack[]{};
        } else {
            while (amount>0){
                items[new Random().nextInt(items.length)] = null;
                amount--;
            }
        }
        return skipEmpty(items);
    }

    public static boolean addToInventory(Location location, Inventory inventory, ItemStack[] itemStacks) {
        boolean drop = false;
        for (ItemStack itemStack : itemStacks) {
            if (!isInvFull(inventory)) {
                inventory.addItem(itemStacks);
            } else {
                location.getWorld().dropItemNaturally(location, itemStack);
                drop = !drop;
            }
        }
        return drop;
    }

    public static boolean addToInventoryForce(Inventory inventory, ItemStack[] itemStacks) {
        if (getFreeSlot(inventory) < itemStacks.length) {
            return false;
        } else {
            inventory.addItem(itemStacks);
            return true;
        }
    }

    public static ItemStack[] skipEmpty(ItemStack[] itemStacks){
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack stack : itemStacks) {
            if (stack!=null && stack.getType()!= Material.AIR){
                items.add(stack);
            }
        }
        return items.toArray(new ItemStack[0]);
    }

    private static int getFreeSlot(Inventory inv) {
        int free = 0;
        for (ItemStack item : inv.getStorageContents()) {
            if (item == null) {
                free++;
            }
        }
        return free;
    }

    private static boolean isInvFull(Inventory inv) {
        return getFreeSlot(inv) == 0;
    }

}
