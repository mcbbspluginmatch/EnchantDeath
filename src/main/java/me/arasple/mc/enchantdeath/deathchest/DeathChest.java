package me.arasple.mc.enchantdeath.deathchest;

import me.arasple.mc.enchantdeath.EDFiles;
import me.arasple.mc.enchantdeath.utils.InvItemsUtils;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DeathChest {

    private UUID owner;
    private Location location;
    private long death_time;
    private long expire_time;
    private ItemStack[] items;

    public DeathChest(UUID owner, Location location, ItemStack[] items, long death_time, long expire_time) {
        this.owner = owner;
        this.location = location;
        this.death_time = death_time;
        this.expire_time = expire_time;
        this.items = InvItemsUtils.skipEmpty(items);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getDeathTime() {
        return death_time;
    }

    public void setDeathTime(long death_time) {
        this.death_time = death_time;
    }

    public boolean isExpired() {
        return expire_time <= System.currentTimeMillis();
    }

    public long getExpireTime() {
        return expire_time;
    }

    public void setExpireTime(long expire_time) {
        this.expire_time = expire_time;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public void setItems(ItemStack[] items) {
        this.items = items;
    }

    public void loadBlock() {
        Block block = getBlock();
        String type = EDFiles.getSettings().getString("DeathChest.type");

        if (!type.equalsIgnoreCase("PLAYER_HEAD")) {
            if (Material.valueOf(type) != null && Material.valueOf(type).isBlock()) {
                block.setType(Material.valueOf(type));
                return;
            } else {
                Msger.log("&8[&6ED&8] &7死亡盒的材料: &6" + type + " &7无效. 将默认使用玩家头颅, 请检查配置!");
            }
        }
        block.setType(getSkullMaterial());
        ((Skull) block.getState()).setSkullType(SkullType.PLAYER);
        // 使用过期的方法是为了兼容1.8
        ((Skull) block.getState()).setOwner(Bukkit.getOfflinePlayer(getOwner()).getName());
        block.getState().update();
    }

    private Material getSkullMaterial() {
        Material skull;
        try {
            skull = Material.valueOf("PLAYER_HEAD");
        } catch (IllegalArgumentException e) {
            skull = Material.valueOf("SKULL");
        }
        return skull;
    }

    public Block getBlock() {
        return getLocation().getBlock();
    }

}
