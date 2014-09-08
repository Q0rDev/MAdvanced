package ca.q0r.madvanced.events;

import ca.q0r.madvanced.MAdvanced;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.Date;
import java.util.UUID;

public class ChatListener implements Listener {
    private MAdvanced plugin;

    public ChatListener(MAdvanced instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        String msg = event.getMessage();

        if (msg == null) {
            return;
        }

        if (plugin.isAFK.get(uuid) != null) {
            if (plugin.isAFK.get(uuid)) {
                player.performCommand("mchatafk");
            }
        }

        plugin.lastMove.put(uuid, new Date().getTime());
    }
}