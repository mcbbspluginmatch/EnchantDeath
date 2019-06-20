package me.arasple.mc.enchantdeath;

import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author Arasple
 */
public class EdFiles {

    private static File settings_file, message_file, data_file;
    private static FileConfiguration settings, messages, data;

    /**
     * 初始化插件文件
     */
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
                Msger.logString("&c数据文件创建失败. 请上报作者!");
            }
        }
    }

    /**
     * 加载插件配置
     */
    static void loadConfigurations() {
        settings = YamlConfiguration.loadConfiguration(settings_file);
        messages = YamlConfiguration.loadConfiguration(message_file);
        data = YamlConfiguration.loadConfiguration(data_file);
    }

    /**
     * 取得插件设置文件
     *
     * @return settings.yml
     */
    public static FileConfiguration getSettings() {
        return settings;
    }

    /**
     * 取得插件语言文件
     *
     * @return messages.yml
     */
    public static FileConfiguration getMessages() {
        return messages;
    }

    /**
     * 取得插件数据文件
     *
     * @return data.yml
     */
    public static FileConfiguration getData() {
        return data;
    }

    /**
     * 保存插件数据到文件
     */
    static void saveDataToFile() {
        try {
            data.save(data_file);
        } catch (IOException e) {
            Msger.logString("&c数据文件保存失败. 请上报作者!");
        }
    }

}
