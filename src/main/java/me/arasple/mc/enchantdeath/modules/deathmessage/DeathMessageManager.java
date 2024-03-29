package me.arasple.mc.enchantdeath.modules.deathmessage;

import me.arasple.mc.enchantdeath.EdFiles;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author Arasple
 */
public class DeathMessageManager {

    private static boolean print_unset_entity;
    private static List<String> unknow;
    private static HashMap<String, List<String>> byCauses, byEntity;

    public static void loadMessages() {
        byCauses = new HashMap<>();
        byEntity = new HashMap<>();
        print_unset_entity = EdFiles.getSettings().getBoolean("DeathMessages.print-unset-entity");
        unknow = EdFiles.getSettings().getStringList("DeathMessages.unknow");
        EdFiles.getSettings().getConfigurationSection("DeathMessages.causes").getKeys(false).forEach(cause -> {
            cause = cause.toUpperCase();
            if (ArrayUtils.contains(EntityDamageEvent.DamageCause.values(), cause)) {
                byCauses.put(cause, EdFiles.getSettings().getStringList("DeathMessages.causes." + cause));
            }
        });
        EdFiles.getSettings().getConfigurationSection("DeathMessages.entity").getKeys(false).forEach(entity -> {
            if (ArrayUtils.contains(EntityType.values(), entity)) {
                byEntity.put(entity, EdFiles.getSettings().getStringList("DeathMessages.entity." + entity));
            }
        });
    }

    /**
     * 通过死亡原因取得已配置的消息
     *
     * @param cause 原因
     * @return 死亡消息
     */
    public static String getByCauses(EntityDamageEvent.DamageCause cause) {
        return byCauses.containsKey(cause.name()) ? getRandomFromListString(byCauses.get(cause.name())) : getUnknow();
    }

    /**
     * 通过攻击者取得已配置的消息
     *
     * @param entityType 实体类型
     * @return 死亡消息
     */
    public static String getByEntity(EntityType entityType) {
        if (print_unset_entity && byEntity.get(entityType.name()) == null) {
            Msger.sendTo(Bukkit.getConsoleSender(), EdFiles.getMessages().getString("DeathMessages.print-unset-entity")
                    .replace("{E}", entityType.name()));
        }

        return byEntity.containsKey(entityType.name()) ? getRandomFromListString(byEntity.get(entityType.name())) : getUnknow();
    }

    /**
     * 发送一个死亡消息
     *
     * @param message 消息
     * @param p       玩家
     */
    public static void sendDeathMessage(String message, Player p) {
        String sendType = EdFiles.getSettings().getString("Worlds." + p.getWorld().getName() + ".deathmessage-type", "NONE");
        if (sendType.startsWith("RANGE_")) {
            int range = Integer.parseInt(sendType.replace("RANGE_", ""));
            for (Entity entity : p.getNearbyEntities(range, range, range)) {
                if (entity instanceof Player) {
                    Msger.sendTo(entity, message);
                }
            }
        } else if ("WORLD".equalsIgnoreCase(sendType)) {
            for (Player player : p.getWorld().getPlayers()) {
                Msger.sendTo(player, message);
            }
        } else if ("SERVER".equalsIgnoreCase(sendType)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Msger.sendTo(player, message);

            }
        }
        Msger.sendTo(Bukkit.getConsoleSender(), message);
    }

    private static String getRandomFromListString(List<String> strings) {
        return strings.get(new Random().nextInt(strings.size()));
    }

    private static String getUnknow() {
        return unknow != null && unknow.size() > 0 ? unknow.get(new Random().nextInt(unknow.size())) : null;
    }

    public static String getCount() {
        return String.valueOf(unknow.size() + byCauses.values().size() + byEntity.values().size());
    }

}
