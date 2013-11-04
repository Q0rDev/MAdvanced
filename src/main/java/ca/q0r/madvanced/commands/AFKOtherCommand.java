package ca.q0r.madvanced.commands;

import ca.q0r.madvanced.MAdvanced;
import ca.q0r.madvanced.yml.config.ConfigType;
import ca.q0r.madvanced.yml.locale.LocaleType;
import ca.q0r.mchat.api.API;
import ca.q0r.mchat.api.Parser;
import ca.q0r.mchat.types.IndicatorType;
import ca.q0r.mchat.util.CommandUtil;
import ca.q0r.mchat.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.TreeMap;

public class AFKOtherCommand implements CommandExecutor {
    private MAdvanced plugin;

    public AFKOtherCommand(MAdvanced instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("mchatafkother")
                || !CommandUtil.hasCommandPerm(sender, "mchat.afk.other")) {
            return true;
        }

        Player afkTarget = plugin.getServer().getPlayer(args[0]);

        if (!CommandUtil.isOnlineForCommand(sender, afkTarget)) {
            return true;
        }

        Boolean isAfk = plugin.isAFK.get(afkTarget.getName()) != null &&
                plugin.isAFK.get(afkTarget.getName());

        String notification = LocaleType.MESSAGE_PLAYER_NOT_AFK.getVal();

        String message = "";

        String title = Parser.parsePlayerName(afkTarget.getName(), afkTarget.getWorld().getName());


        if (!isAfk) {
            if (args.length > 1) {
                for (int i = 1; i < args.length; ++i) {
                    message += " " + args[i];
                }

                message = message.trim();
            } else {
                message = "Away From Keyboard";
            }

            notification = LocaleType.MESSAGE_PLAYER_AFK.getVal();
        }

        TreeMap<String, String> rMap = new TreeMap<String, String>();

        rMap.put("player", Parser.parsePlayerName(afkTarget.getName(), afkTarget.getWorld().getName()));
        rMap.put("reason", message);
        rMap.put("r", message);

        plugin.getServer().broadcastMessage(API.replace(notification, rMap, IndicatorType.LOCALE_VAR));

        afkTarget.setSleepingIgnored(!isAfk);
        plugin.isAFK.put(afkTarget.getName(), !isAfk);

        String pLName = Parser.parseTabbedList(afkTarget.getName(), afkTarget.getWorld().getName());

        if (!isAfk) {
            plugin.AFKLoc.put(afkTarget.getName(), afkTarget.getLocation());

            pLName = MessageUtil.addColour("<gold>[" + LocaleType.MESSAGE_AFK_AFK.getVal() + "] ") + pLName;
        }

        if (ConfigType.OPTION_USE_AFK_LIST.getBoolean()) {
            if (pLName.length() > 15) {
                pLName = pLName.substring(0, 16);
                afkTarget.setPlayerListName(pLName);
            } else {
                afkTarget.setPlayerListName(pLName);
            }
        }

        return true;
    }
}
