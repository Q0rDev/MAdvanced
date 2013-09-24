package ca.q0r.madvanced.commands;

import ca.q0r.madvanced.MAdvanced;
import ca.q0r.madvanced.types.ConfigType;
import ca.q0r.madvanced.types.LocaleType;
import com.miraclem4n.mchat.api.API;
import com.miraclem4n.mchat.api.Parser;
import com.miraclem4n.mchat.api.Reader;
import com.miraclem4n.mchat.types.IndicatorType;
import com.miraclem4n.mchat.types.InfoType;
import com.miraclem4n.mchat.util.MessageUtil;
import com.miraclem4n.mchat.util.CommandUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ListCommand implements CommandExecutor {
    private MAdvanced plugin;

    public ListCommand(MAdvanced instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("mchatlist")
                || !CommandUtil.hasCommandPerm(sender, "mchat.list"))
            return true;

        TreeMap<String, String> rMap = new TreeMap<String, String>();

        rMap.put("max", String.valueOf(plugin.getServer().getMaxPlayers()));
        rMap.put("players", String.valueOf(plugin.getServer().getOnlinePlayers().length));

        sender.sendMessage(MessageUtil.addColour(API.replace(LocaleType.MESSAGE_LIST_HEADER.getVal(), rMap, IndicatorType.LOCALE_VAR)));

        HashMap<String, Integer> cLMap = new HashMap<String, Integer>();

        String msg = "";
        String line = "";
        String[] msgS;

        for (Player players : plugin.getServer().getOnlinePlayers()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (!player.canSee(players))
                    continue;
            }

            String iVar = Reader.getInfo(players.getName(), InfoType.USER, players.getWorld().getName(), ConfigType.OPTION_LIST_VAR.getString());
            String mName = Parser.parseListCmd(players.getName(), players.getWorld().getName());

            Boolean collapsed = false;

            if (iVar.isEmpty())
                iVar = "Default";

            for (String string : ConfigType.OPTION_COLLAPSED_LIST_VAR.getString().split(",")) {
                if (!iVar.equals(string))
                    continue;

                collapsed = true;

                if (cLMap.get(string) != null) {
                    Integer sVal = cLMap.get(string);

                    cLMap.put(string, sVal+1);
                } else
                    cLMap.put(string, 1);
            }

            if (collapsed)
                continue;

            if (plugin.isAFK.get(players.getName()) != null && plugin.isAFK.get(players.getName()))
                if (msg.contains(iVar + ": &f"))
                    msg = msg.replace(iVar + ": &f", iVar + ": &f&4[" + LocaleType.MESSAGE_AFK_AFK.getVal() + "]" + mName + "&f, &f");
                else
                    msg += (iVar + ": &f&4[" + LocaleType.MESSAGE_AFK_AFK.getVal() + "]" + mName + "&f, &f" + '\n');
            else
            if (msg.contains(iVar + ": &f"))
                msg = msg.replace(iVar + ": &f", iVar + ": &f" + mName + "&f, &f");
            else
                msg += (iVar + ": &f" + mName + "&f, &f" + '\n');

            if (!msg.contains("" + '\n'))
                msg += '\n';
        }

        for (Map.Entry<String, Integer> entry : cLMap.entrySet())
            msg += (entry.getKey() + ": &f" + entry.getValue() + '\n');

        if (msg.contains("" + '\n')) {
            if (ConfigType.OPTION_USE_GROUPED_LIST.getBoolean()) {
                msgS = msg.split("" + '\n');


                for (String arg : msgS)
                    sender.sendMessage(MessageUtil.addColour(arg));
            } else {
                msg = MessageUtil.addColour(msg.replace("" + '\n', "&5 | &f"));

                sender.sendMessage(msg);
            }
        } else
            sender.sendMessage(MessageUtil.addColour(msg));

        for (int i = 20; i < MessageUtil.addColour(API.replace(LocaleType.MESSAGE_LIST_HEADER.getVal(), rMap, IndicatorType.LOCALE_VAR)).length(); i++)
            line += "-";

        sender.sendMessage(MessageUtil.addColour("&6" + line));

        return true;
    }
}
