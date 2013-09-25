package ca.q0r.madvanced.commands;

import ca.q0r.madvanced.MAdvanced;
import ca.q0r.mchat.api.API;
import ca.q0r.mchat.api.Parser;
import ca.q0r.mchat.util.CommandUtil;
import ca.q0r.mchat.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhoCommand implements CommandExecutor {
    MAdvanced plugin;

    public WhoCommand(MAdvanced instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("mchatwho")
                || !CommandUtil.hasCommandPerm(sender, "mchat.who")) {
            return true;
        }

        if (args.length > 0) {
            if (CommandUtil.isOnlineForCommand(sender, args[0])) {
                formatWho(sender, plugin.getServer().getPlayer(args[0]));
            }

            return true;
        }

        return false;
    }

    void formatWho(CommandSender sender, Player recipient) {
        String recipientName = Parser.parsePlayerName(recipient.getName(), recipient.getWorld().getName());
        Integer locX = (int) recipient.getLocation().getX();
        Integer locY = (int) recipient.getLocation().getY();
        Integer locZ = (int) recipient.getLocation().getZ();
        String loc = ("X: " + locX + ", " + "Y: " + locY + ", " + "Z: " + locZ);
        String world = recipient.getWorld().getName();
        String i = recipient.getAddress().toString();
        String p = i.replace("/", "");
        String ip = p.split(":")[0];
        Integer exp = recipient.getLevel();

        MessageUtil.sendColouredMessage(sender, "Player Name: " + recipient.getName());
        MessageUtil.sendColouredMessage(sender, "Display Name: " + recipient.getDisplayName());
        MessageUtil.sendColouredMessage(sender, "Formatted Name: " + recipientName);
        MessageUtil.sendColouredMessage(sender, "Player's Location: [ " + loc + " ]");
        MessageUtil.sendColouredMessage(sender, "Player's World: " + world);
        MessageUtil.sendColouredMessage(sender, "Player's Health: " + API.createHealthBar(recipient) + " " + recipient.getHealth() + "/20");
        MessageUtil.sendColouredMessage(sender, "Player's Food: " + API.createFoodBar(recipient) + " " + recipient.getFoodLevel() + "/20");
        MessageUtil.sendColouredMessage(sender, "Player's Level: " + exp);
        MessageUtil.sendColouredMessage(sender, "Player's IP: " + ip);
        MessageUtil.sendColouredMessage(sender, "Current Item: " + recipient.getItemInHand().getType().name());
        MessageUtil.sendColouredMessage(sender, "Entity ID: #" + recipient.getEntityId());
    }
}
