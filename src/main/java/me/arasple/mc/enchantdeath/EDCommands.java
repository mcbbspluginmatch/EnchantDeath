package me.arasple.mc.enchantdeath;

import me.arasple.mc.enchantdeath.deathchest.DeathChestManager;
import me.arasple.mc.enchantdeath.deathmessage.DeathMessageManager;
import me.arasple.mc.enchantdeath.utils.Msger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Arasple
 */
public class EDCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            switch (args[0].toUpperCase()) {
                case "RELOAD":
                    EDFiles.loadConfigurations();
                    DeathMessageManager.loadMessages();
                    Msger.sendString(sender, "Plugin.commands.reload");
                    break;
                case "SAVE":
                    DeathChestManager.saveDeathChests();
                    EDFiles.saveDataToFile();
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

    private void displayHelp(CommandSender sender) {
        Msger.sendStrings(sender, "Plugin.commands.help");
    }

}
