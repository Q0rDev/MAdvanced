package ca.q0r.madvanced.types;

import ca.q0r.madvanced.configs.LocaleUtil;
import com.miraclem4n.mchat.util.MessageUtil;

public enum LocaleType {
    MESSAGE_AFK_AFK("message.afk.afk"),
    MESSAGE_AFK_DEFAULT("message.afk.default"),
    MESSAGE_CONFIG_RELOADED("message.config.reloaded"),
    MESSAGE_CONFIG_UPDATED("message.config.updated"),
    MESSAGE_LIST_HEADER("message.list.header"),
    MESSAGE_PLAYER_AFK("message.player.afk"),
    MESSAGE_PLAYER_NOT_AFK("message.player.notAfk"),
    MESSAGE_PLAYER_STILL_AFK("message.player.stillAfk"),
    MESSAGE_SPOUT_COLOUR("message.spout.colour"),
    MESSAGE_SPOUT_PM("message.spout.pmFrom"),
    MESSAGE_SPOUT_TYPING("message.spout.typing");

    private final String option;

    LocaleType(String option) {
        this.option = option;
    }

    public String getVal() {
        if (LocaleUtil.getConfig().isSet(option))
            return MessageUtil.addColour(LocaleUtil.getConfig().getString(option));

        return "Locale Option '" + option + "' not found!";
    }

    public String getRaw() {
        if (LocaleUtil.getConfig().isSet(option))
            return LocaleUtil.getConfig().getString(option);

        return "Locale Option '" + option + "' not found!";
    }
}
