package ca.q0r.madvanced.events;

import ca.q0r.madvanced.MAdvanced;
import ca.q0r.madvanced.types.LocaleType;
import ca.q0r.mchat.api.Parser;
import ca.q0r.mchat.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

import java.util.Date;

public class ChatListener implements Listener {
    private MAdvanced plugin;

    public ChatListener(MAdvanced instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        String pName = player.getName();

        String world = player.getWorld().getName();
        String msg = event.getMessage();

        if (msg == null) {
            return;
        }

        if (plugin.isAFK.get(pName) != null) {
            if (plugin.isAFK.get(pName)) {
                player.performCommand("mchatafk");
            }
        }

        plugin.lastMove.put(pName, new Date().getTime());

        if (plugin.spoutB) {
            SpoutPlayer sPlayer = (SpoutPlayer) player;
            final String sPName = pName;

            sPlayer.setTitle(ChatColor.valueOf(LocaleType.MESSAGE_SPOUT_COLOUR.getRaw().toUpperCase())
                    + "- " + MessageUtil.addColour(msg) + ChatColor.valueOf(LocaleType.MESSAGE_SPOUT_COLOUR.getRaw().toUpperCase())
                    + " -" + '\n' + Parser.parsePlayerName(pName, world));

            plugin.isChatting.put(pName, false);

            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    SpoutPlayer sPlayer = (SpoutPlayer) plugin.getServer().getPlayer(sPName);

                    if (sPlayer != null) {
                        sPlayer.setTitle(Parser.parsePlayerName(sPName, sPlayer.getWorld().getName()));
                    }
                }
            }, 7 * 20);
        }

    }
}
