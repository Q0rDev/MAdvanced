package ca.q0r.madvanced.events;

import ca.q0r.madvanced.yml.YmlManager;
import ca.q0r.madvanced.yml.YmlType;
import ca.q0r.madvanced.yml.config.ConfigYml;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Map;

public class CommandListener implements Listener {
    public CommandListener() {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage();
        String command = msg.split(" ")[0].replace("/", "");

        for (Map.Entry<String, List<String>> entry : ((ConfigYml) YmlManager.getYml(YmlType.CONFIG_YML)).getAliasMap().entrySet()) {
            for (String comm : entry.getValue()) {
                if (comm.equalsIgnoreCase(command)) {
                    event.setMessage(msg.replaceFirst("/" + command, "/" + entry.getKey()));
                    return;
                }
            }
        }
    }
}