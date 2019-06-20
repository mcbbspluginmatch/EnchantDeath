package me.arasple.mc.enchantdeath.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Arasple
 */
public class LocationUtils {

    /**
     * 转 String 为 Location
     */
    public static Location getLocationFromString(String str) {
        String[] locArgs = str.split(",");
        if (Bukkit.getWorld(locArgs[0]) == null) {
            return null;
        }
        try {
            World world = Bukkit.getWorld(locArgs[0]);
            double
                    x = Double.parseDouble(locArgs[1]),
                    y = Double.parseDouble(locArgs[2]),
                    z = Double.parseDouble(locArgs[3]);
            return new Location(world, x, y, z, 0, 0);
        } catch (Exception e) {
            Msger.log("转换 Location 时出错, 请上报作者");
            return null;
        }
    }

    /**
     * 转换 Location 到 String
     */
    public static String getStringFromLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() +
                "," + loc.getY() +
                "," + loc.getZ();
    }

}
