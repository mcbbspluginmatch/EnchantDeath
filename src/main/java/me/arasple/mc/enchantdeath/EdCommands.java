package me.arasple.mc.enchantdeath;

import me.arasple.mc.enchantdeath.modules.deathchest.DeathChestManager;
import me.arasple.mc.enchantdeath.modules.deathmessage.DeathMessageManager;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Arasple
 */
public class EdCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            switch (args[0].toUpperCase()) {
                // 重载命令
                case "RELOAD":
                    EdFiles.loadConfigurations();
                    DeathMessageManager.loadMessages();
                    Msger.sendString(sender, "Plugin.commands.reload");
                    break;
                // 保存命令
                case "SAVE":
                    DeathChestManager.saveDeathChests();
                    EdFiles.saveDataToFile();
                    Msger.sendString(sender, "Plugin.commands.save");
                    break;
                default:
                    displayHelp(sender);
                    break;
            }
            return true;
        }
        displayHelp(sender);
        return false;
    }

    /**
     * 显示命令帮助
     *
     * @param receiver 接收者
     */
    private void displayHelp(CommandSender receiver) {
        Msger.sendStrings(receiver, "Plugin.commands.help");
    }

}
