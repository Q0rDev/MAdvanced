package ca.q0r.madvanced.events;

import ca.q0r.madvanced.MAdvanced;
import ca.q0r.madvanced.types.LocaleType;
import ca.q0r.mchat.api.Parser;
import ca.q0r.mchat.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

public class CustomListener implements Listener {
    private MAdvanced plugin;

    public CustomListener(MAdvanced instance) {
        plugin = instance;
    }

    @EventHandler
    public void onKeyPressedEvent(KeyPressedEvent event) {
        SpoutPlayer player = event.getPlayer();

        String pName = player.getName();

        Keyboard key = event.getKey();
        Keyboard chatKey = player.getChatKey();
        Keyboard forwardKey = player.getForwardKey();
        Keyboard backwardKey = player.getBackwardKey();
        Keyboard leftKey = player.getLeftKey();
        Keyboard rightKey = player.getRightKey();

        if (plugin.isChatting.get(pName) == null) {
            plugin.isChatting.put(pName, false);
        }

        if (key == null) return;

        if (key.equals(chatKey)) {
            player.setTitle(ChatColor.valueOf(LocaleType.MESSAGE_SPOUT_COLOUR.getRaw().toUpperCase()) + MessageUtil.addColour(LocaleType.MESSAGE_SPOUT_TYPING.getVal()) + '\n' + Parser.parsePlayerName(pName, player.getWorld().getName()));
            plugin.isChatting.put(pName, true);
        }

        if (plugin.isChatting.get(pName)) {
            if ((key.equals(forwardKey)) ||
                    (key.equals(backwardKey)) ||
                    (key.equals(leftKey)) ||
                    (key.equals(rightKey))) {
                player.setTitle(Parser.parsePlayerName(pName, player.getWorld().getName()));
                plugin.isChatting.put(pName, false);
            }
        }
    }
}
