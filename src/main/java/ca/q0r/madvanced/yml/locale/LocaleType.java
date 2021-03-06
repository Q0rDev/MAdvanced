package ca.q0r.madvanced.yml.locale;

import ca.q0r.madvanced.yml.YmlManager;
import ca.q0r.madvanced.yml.YmlType;
import ca.q0r.mchat.util.MessageUtil;

public enum LocaleType {
    MESSAGE_AFK_AFK("message.afk.afk"),
    MESSAGE_AFK_DEFAULT("message.afk.default"),
    MESSAGE_LIST_HEADER("message.list.header"),
    MESSAGE_PLAYER_AFK("message.player.afk"),
    MESSAGE_PLAYER_NOT_AFK("message.player.notAfk"),
    MESSAGE_PLAYER_STILL_AFK("message.player.stillAfk");

    private final String option;

    LocaleType(String option) {
        this.option = option;
    }

    public String getVal() {
        if (YmlManager.getYml(YmlType.LOCALE_YML).getConfig().isSet(option))
            return MessageUtil.addColour(YmlManager.getYml(YmlType.LOCALE_YML).getConfig().getString(option));

        return "Locale Option '" + option + "' not found!";
    }

    public String getRaw() {
        if (YmlManager.getYml(YmlType.LOCALE_YML).getConfig().isSet(option)) {
            return YmlManager.getYml(YmlType.LOCALE_YML).getConfig().getString(option);
        }

        return "Locale Option '" + option + "' not found!";
    }
}
