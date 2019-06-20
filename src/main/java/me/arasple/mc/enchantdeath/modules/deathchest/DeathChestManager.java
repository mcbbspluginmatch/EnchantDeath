package me.arasple.mc.enchantdeath.modules.deathchest;

import me.arasple.mc.enchantdeath.EdFiles;
import me.arasple.mc.enchantdeath.utils.InvItemsUtils;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 */
public class DeathChestManager {

    private static final String PLAYER_SELF_HEAD_TAG = "pHEAD";
    private static List<DeathChest> deathChests = new ArrayList<>();

    public static List<DeathChest> getDeathChests() {
        return deathChests;
    }

    static String getPlayerSelfHeadTag() {
        return PLAYER_SELF_HEAD_TAG;
    }

    /**
     * 保存死亡盒数据到文件
     */
    public static void saveDeathChests() {
        EdFiles.getData().set("DeathChests", null);

        deathChests.forEach(d -> {
            EdFiles.getData().set("DeathChests." + d.getDeathTime() + ".owner", d.getOwner().toString());
            EdFiles.getData().set("DeathChests." + d.getDeathTime() + ".expire-time", d.getExpireTime());
            EdFiles.getData().set("DeathChests." + d.getDeathTime() + ".location", d.getLocation());
            EdFiles.getData().set("DeathChests." + d.getDeathTime() + ".items", d.getItems());
        });
    }

    /**
     * 从文件中加载死亡盒数据
     */
    public static void loadDeathChests() {
        deathChests.clear();

        if (EdFiles.getData().getConfigurationSection("DeathChests") == null) {
            return;
        }
        EdFiles.getData().getConfigurationSection("DeathChests").getKeys(false).forEach(
                timeStr -> {
                    long deathTime = Long.parseLong(timeStr);
                    long expireTime = EdFiles.getData().getLong("DeathChests." + timeStr + ".expire-time");
                    UUID owner = UUID.fromString(EdFiles.getData().getString("DeathChests." + timeStr + ".owner"));
                    Location location = (Location) EdFiles.getData().get("DeathChests." + timeStr + ".location");
                    List<ItemStack> items = (List<ItemStack>) EdFiles.getData().getList("DeathChests." + timeStr + ".items");

                    if (expireTime > System.currentTimeMillis()) {
                        deathChests.add(new DeathChest(owner, location, items.toArray(new ItemStack[0]), deathTime, expireTime));
                    }
                }
        );
    }

    /**
     * 取得当前方块所对应的死亡盒
     *
     * @param block 方块
     * @return 死亡盒, 如果没有则为 null
     */
    public static DeathChest getDeathChest(Block block) {
        for (DeathChest dc : deathChests) {
            if (dc.isExpired()) {
                dc.delete();
                deathChests.remove(dc);
            } else if (dc.getLocation().distance(block.getLocation()) == 0) {
                return dc;
            }
        }
        return null;
    }

    public static boolean retrieve(DeathChest deathChest, Player p) {
        String dropType = EdFiles.getSettings().getString("DeathChest.drop");
        boolean success = false;
        switch (dropType.toUpperCase()) {
            case "INV":
                boolean drop = InvItemsUtils.addToInventory(p.getLocation(), p.getInventory(), deathChest.getItems());
                if (drop) {
                    Msger.sendString(p, "DeathChest.inv-drop");
                }
                success = !success;
                break;
            case "INV_FORCE":
                boolean enough = InvItemsUtils.addToInventoryForce(p.getInventory(), deathChest.getItems());
                if (!enough) {
                    Msger.sendString(p, "DeathChest.no-enough-space");
                } else {
                    success = !success;
                }
                break;
            case "DROP":
                for (ItemStack item : deathChest.getItems()) {
                    p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item);
                }
                Msger.sendString(p, "DeathChest.drop");
                success = !success;
                break;
            default:
                break;
        }

        if (success) {
            deathChest.getBlock().setType(Material.AIR);
            DeathChestManager.getDeathChests().remove(deathChest);
            Msger.consoleExecute(p, EdFiles.getSettings().getStringList("CommandsAfterRetrieveDeathChest"));
        }

        return success;
    }

    public static boolean isDcBlock(Block block) {
        return getDeathChest(block) != null;
    }

    public static void showInfo(Player p, DeathChest dc) {
        for (String s : EdFiles.getMessages().getStringList("DeathChest.info-preview")) {
            s = s
                    .replace("{OWNER_UUID}", dc.getOwner().toString())
                    .replace("{OWNER}", Bukkit.getOfflinePlayer(dc.getOwner()).getName())
                    .replace("{ITEMS}", String.valueOf(dc.getItems().length))
                    .replace("{TIME_LEFT}", String.valueOf((dc.getExpireTime() - System.currentTimeMillis()) / 1000)
                    );
            Msger.sendTo(p, s);
        }
    }

    private static Material SKULL_MATERIAL;

    static Material getSkullMaterial() {
        return SKULL_MATERIAL != null ? SKULL_MATERIAL : loadSkullMaterial();
    }

    private static Material loadSkullMaterial() {
        try {
            SKULL_MATERIAL = Material.valueOf("PLAYER_HEAD");
        } catch (IllegalArgumentException e) {
            SKULL_MATERIAL = Material.valueOf("SKULL");
        }
        return SKULL_MATERIAL;
    }

}
