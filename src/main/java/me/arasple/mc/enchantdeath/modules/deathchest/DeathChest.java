package me.arasple.mc.enchantdeath.modules.deathchest;

import me.arasple.mc.enchantdeath.EdFiles;
import me.arasple.mc.enchantdeath.utils.InvItemsUtils;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Arasple
 */
public class DeathChest {

    private UUID owner;
    private Location location;
    private long deathTime;
    private long expireTime;
    private ItemStack[] items;

    public DeathChest(UUID owner, Location location, ItemStack[] items, long deathTime, long expireTime) {
        this.owner = owner;
        this.location = location;
        this.deathTime = deathTime;
        this.expireTime = expireTime;
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
        return deathTime;
    }

    public void setDeathTime(long deathTime) {
        this.deathTime = deathTime;
    }

    public boolean isExpired() {
        return expireTime <= System.currentTimeMillis();
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public void setItems(ItemStack[] items) {
        this.items = items;
    }

    public void loadBlock() {
        Block block = getBlock();
        String type = EdFiles.getSettings().getString("DeathChest.type");

        if (!DeathChestManager.getPlayerSelfHeadTag().equalsIgnoreCase(type)) {
            if (Material.matchMaterial(type) != null && Material.matchMaterial(type).isBlock()) {
                block.setType(Material.valueOf(type));
                return;
            } else {
                Msger.logString("&8[&6ED&8] &7死亡盒的材料: &6" + type + " &7无效. 将默认使用玩家头颅, 请检查配置!");
            }
        }
        block.setType(DeathChestManager.getSkullMaterial());
        Skull skull = (Skull) block.getState();
        skull.setOwner(Bukkit.getOfflinePlayer(getOwner()).getName());
        skull.update();
        block.getState().update();
    }

    public Block getBlock() {
        return getLocation().getBlock();
    }

    public void delete() {
        getBlock().setType(Material.AIR);
    }

}
