package me.arasple.mc.enchantdeath;

import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class EDFiles {

    private static File settings_file, message_file, data_file;
    private static FileConfiguration settings, messages, data;

    static void loadFiles() {
        settings_file = new File(EnchantDeath.getInstance().getDataFolder(), "settings.yml");
        message_file = new File(EnchantDeath.getInstance().getDataFolder(), "messages.yml");
        data_file = new File(EnchantDeath.getInstance().getDataFolder(), "data.yml");

        if (!settings_file.exists()) {
            EnchantDeath.getInstance().saveResource("settings.yml", true);
        }
        if (!message_file.exists()) {
            EnchantDeath.getInstance().saveResource("messages.yml", true);
        }
        if (!data_file.exists()) {
            try {
                data_file.createNewFile();
            } catch (IOException e) {
                Msger.log("&c数据文件 创建失败. 请上报作者!");
            }
        }
    }

    public static void loadConfigurations() {
        settings = YamlConfiguration.loadConfiguration(settings_file);
        messages = YamlConfiguration.loadConfiguration(message_file);
        data = YamlConfiguration.loadConfiguration(data_file);
    }

    public static FileConfiguration getSettings() {
        return settings;
    }

    public static FileConfiguration getMessages() {
        return messages;
    }

    public static FileConfiguration getData() {
        return data;
    }

    public static void saveDataToFile() {
        try {
            data.save(data_file);
        } catch (IOException e) {
            Msger.log("&c数据文件 保存失败. 请上报作者!");
        }
    }

}
