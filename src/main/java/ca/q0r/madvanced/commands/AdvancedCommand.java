package ca.q0r.madvanced.commands;

import ca.q0r.madvanced.MAdvanced;
import ca.q0r.madvanced.configs.ConfigUtil;
import com.miraclem4n.mchat.util.MessageUtil;
import com.miraclem4n.mchat.util.MiscUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdvancedCommand implements CommandExecutor {
    MAdvanced plugin;

    public AdvancedCommand(MAdvanced instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("madvanced"))
            return true;

        if (args.length == 0)
            return false;

        if (args[0].equalsIgnoreCase("reload")
                || args[0].equalsIgnoreCase("r")) {
            if (!MiscUtil.hasCommandPerm(sender, "madvanced.reload"))
                return true;

            ConfigUtil.initialize();
            MessageUtil.sendMessage(sender, "Config Reloaded.");
            return true;
        }

        return false;
    }
}
