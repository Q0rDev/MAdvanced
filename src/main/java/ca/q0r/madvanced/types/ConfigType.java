package ca.q0r.madvanced.types;

import ca.q0r.madvanced.configs.ConfigUtil;
import com.miraclem4n.mchat.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public enum ConfigType {
    MCHAT_SPOUT("plugin.spout"),

    OPTION_HC_AFK("option.eHQAFK"),
    OPTION_USE_GROUPED_LIST("option.useGroupedList"),
    OPTION_LIST_VAR("option.listVar"),
    OPTION_COLLAPSED_LIST_VAR("option.collapsedListVars"),
    OPTION_AFK_TIMER("option.AFKTimer"),
    OPTION_AFK_KICK_TIMER("option.AFKKickTimer"),
    OPTION_USE_AFK_LIST("option.useAFKList");

    private final String option;

    ConfigType(String option) {
        this.option = option;
    }

    public Boolean getBoolean() {
        Boolean b = ConfigUtil.getConfig().getBoolean(option);

        if (b != null) {
            return b;
        }

        return false;
    }

    public String getString() {
        String s = ConfigUtil.getConfig().getString(option);

        if (s != null) {
            return MessageUtil.addColour(s);
        }

        return "";
    }

    public Integer getInteger() {
        Integer i = ConfigUtil.getConfig().getInt(option);

        if (i != null) {
            return i;
        }

        return 0;
    }

    public Double getDouble() {
        Double d = ConfigUtil.getConfig().getDouble(option);

        if (d != null) {
            return d;
        }

        return 0.0;
    }

    public List<String> getList() {
        List<String> list = ConfigUtil.getConfig().getStringList(option);

        if (list != null) {
            ArrayList<String> l = new ArrayList<String>();

            for (String s : list) {
                l.add(MessageUtil.addColour(s));
            }

            return l;
        }

        return list;
    }
}
