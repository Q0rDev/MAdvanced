package ca.q0r.madvanced.events;

import ca.q0r.madvanced.MAdvanced;
import ca.q0r.madvanced.yml.config.ConfigType;
import ca.q0r.madvanced.yml.locale.LocaleType;
import ca.q0r.mchat.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Date;
import java.util.UUID;

public class PlayerListener implements Listener {
    MAdvanced plugin;

    public PlayerListener(MAdvanced instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        plugin.lastMove.put(uuid, new Date().getTime());

        for (String aliases : plugin.getCommand("mchatafk").getAliases()) {
            if (event.getMessage().contains("/" + aliases) ||
                    event.getMessage().contains("/mchatafk")) {
                return;
            }
        }

        if (plugin.isAFK.get(uuid) == null) {
            return;
        }

        if (plugin.isAFK.get(uuid)) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "mchatafkother " + uuid);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.isChatting.put(player.getUniqueId(), false);
        plugin.isAFK.put(player.getUniqueId(), false);
        plugin.lastMove.put(player.getUniqueId(), new Date().getTime());
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

        plugin.lastMove.put(player.getUniqueId(), new Date().getTime());

        if (plugin.isAFK.get(player.getUniqueId()) == null) {
            return;
        }

        if (plugin.isAFK.get(player.getUniqueId())) {
            if (ConfigType.OPTION_HC_AFK.getBoolean()) {
                if (plugin.AFKLoc.get(player.getUniqueId()) != null) {
                    player.teleport(plugin.AFKLoc.get(player.getUniqueId()));
                }

                MessageUtil.sendMessage(player, LocaleType.MESSAGE_PLAYER_STILL_AFK.getVal());
            } else {
                player.performCommand("mchatafk");
            }
        }
    }
}