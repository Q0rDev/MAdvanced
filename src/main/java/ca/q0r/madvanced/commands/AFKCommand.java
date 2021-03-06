package ca.q0r.madvanced.commands;

import ca.q0r.madvanced.MAdvanced;
import ca.q0r.madvanced.yml.locale.LocaleType;
import ca.q0r.mchat.util.CommandUtil;
import ca.q0r.mchat.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AFKCommand implements CommandExecutor {
    private MAdvanced plugin;

    public AFKCommand(MAdvanced instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("mchatafk")
                || !CommandUtil.hasCommandPerm(sender, "mchat.afk.self")) {
            return true;
        }

        if (!(sender instanceof Player)) {
            MessageUtil.sendMessage(sender, "Console's can't be AFK.");
            return true;
        }

        String message = "";

        if (args.length > 0) {
            for (String arg : args) {
                message += " " + arg;
            }

            message = message.trim();
        } else {
            message = LocaleType.MESSAGE_AFK_DEFAULT.getVal();
        }

        Player player = (Player) sender;

        if (isAfk(player.getUniqueId())) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "mchatafkother " + player.getName());
            return true;
        }

        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "mchatafkother " + sender.getName() + " " + message);
        return true;
    }

    Boolean isAfk(UUID uuid) {
        return plugin.isAFK.get(uuid) != null && plugin.isAFK.get(uuid);
    }
}