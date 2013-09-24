package ca.q0r.madvanced.events;

import ca.q0r.madvanced.MAdvanced;
import ca.q0r.madvanced.types.ConfigType;
import ca.q0r.madvanced.types.LocaleType;
import com.miraclem4n.mchat.api.Parser;
import com.miraclem4n.mchat.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

import java.util.Date;

public class PlayerListener implements Listener {
    MAdvanced plugin;

    public PlayerListener(MAdvanced instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String pName = player.getName();

        plugin.lastMove.put(pName, new Date().getTime());

        for (String aliases : plugin.getCommand("mchatafk").getAliases()) {
            if (event.getMessage().contains("/" + aliases) ||
                    event.getMessage().contains("/mchatafk")) {
                return;
            }
        }

        if (plugin.isAFK.get(pName) == null) {
            return;
        }

        if (plugin.isAFK.get(pName)) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "mchatafkother " + pName);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String world = player.getWorld().getName();
        String pName = player.getName();

        plugin.isChatting.put(player.getName(), false);
        plugin.isAFK.put(player.getName(), false);
        plugin.lastMove.put(player.getName(), new Date().getTime());

        if (plugin.spoutB) {
            SpoutPlayer sPlayer = (SpoutPlayer) player;
            sPlayer.setTitle(Parser.parsePlayerName(pName, world));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        Integer fromX = from.getBlockX();
        Integer fromY = from.getBlockY();
        Integer fromZ = from.getBlockZ();

        Integer toX = to.getBlockX();
        Integer toY = to.getBlockY();
        Integer toZ = to.getBlockZ();


        String fromLoc = from.getWorld().getName() + "|" + fromX + "|" + fromY + "|" + fromZ;
        String toLoc = to.getWorld().getName() + "|" + toX + "|" + toY + "|" + toZ;

        if (fromLoc.equalsIgnoreCase(toLoc)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        plugin.lastMove.put(player.getName(), new Date().getTime());

        if (plugin.isAFK.get(player.getName()) == null) {
            return;
        }

        if (plugin.isAFK.get(player.getName())) {
            if (ConfigType.OPTION_HC_AFK.getBoolean()) {
                if (plugin.AFKLoc.get(player.getName()) != null) {
                    player.teleport(plugin.AFKLoc.get(player.getName()));
                }

                MessageUtil.sendMessage(player, LocaleType.MESSAGE_PLAYER_STILL_AFK.getVal());
            } else {
                player.performCommand("mchatafk");
            }
        }
    }
}
