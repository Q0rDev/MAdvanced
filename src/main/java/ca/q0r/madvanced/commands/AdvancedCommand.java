package ca.q0r.madvanced.commands;

import ca.q0r.madvanced.yml.YmlManager;
import ca.q0r.madvanced.yml.YmlType;
import ca.q0r.madvanced.yml.config.ConfigYml;
import ca.q0r.madvanced.yml.locale.LocaleYml;
import ca.q0r.mchat.util.CommandUtil;
import ca.q0r.mchat.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdvancedCommand implements CommandExecutor {
    public AdvancedCommand() { }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("madvanced")) {
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")
                || args[0].equalsIgnoreCase("r")) {
            if (!CommandUtil.hasCommandPerm(sender, "madvanced.reload")) {
                return true;
            }

            YmlManager.reloadYml(YmlType.CONFIG_YML);
            YmlManager.reloadYml(YmlType.LOCALE_YML);

            MessageUtil.sendMessage(sender, "Config Reloaded.");
            return true;
        }

        return false;
    }
}
