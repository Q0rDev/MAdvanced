package ca.q0r.madvanced.types;

import ca.q0r.madvanced.configs.ConfigUtil;
import ca.q0r.mchat.util.MessageUtil;

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
        return ConfigUtil.getConfig().getBoolean(option, false);
    }

    public String getString() {
        return MessageUtil.addColour(ConfigUtil.getConfig().getString(option, ""));
    }

    public Integer getInteger() {
        return ConfigUtil.getConfig().getInt(option, 0);
    }

    public Double getDouble() {
        return ConfigUtil.getConfig().getDouble(option, 0.0);
    }

    public List<String> getList() {
        List<String> list = ConfigUtil.getConfig().getStringList(option);
        ArrayList<String> l = new ArrayList<String>();

        for (String s : list) {
            l.add(MessageUtil.addColour(s));
        }

        return l;
    }
}
