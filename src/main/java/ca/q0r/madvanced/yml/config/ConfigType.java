package ca.q0r.madvanced.yml.config;

import ca.q0r.madvanced.yml.YmlManager;
import ca.q0r.madvanced.yml.YmlType;
import ca.q0r.mchat.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public enum ConfigType {
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
        return YmlManager.getYml(YmlType.CONFIG_YML).getConfig().getBoolean(option, false);
    }

    public String getString() {
        return MessageUtil.addColour(YmlManager.getYml(YmlType.CONFIG_YML).getConfig().getString(option, ""));
    }

    public Integer getInteger() {
        return YmlManager.getYml(YmlType.CONFIG_YML).getConfig().getInt(option, 0);
    }

    public Double getDouble() {
        return YmlManager.getYml(YmlType.CONFIG_YML).getConfig().getDouble(option, 0.0);
    }

    public List<String> getList() {
        List<String> list = YmlManager.getYml(YmlType.CONFIG_YML).getConfig().getStringList(option);
        ArrayList<String> l = new ArrayList<>();

        for (String s : list) {
            l.add(MessageUtil.addColour(s));
        }

        return l;
    }
}
