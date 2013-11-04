package ca.q0r.madvanced.events;

import ca.q0r.madvanced.MAdvanced;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
    }
}
