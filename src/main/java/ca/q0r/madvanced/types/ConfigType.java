package ca.q0r.madvanced.types;

import ca.q0r.madvanced.configs.ConfigUtil;
import com.miraclem4n.mchat.util.MessageUtil;

import java.util.ArrayList;

public enum ConfigType {
    MCHAT_SPOUT("plugin.spout"),

    OPTION_HC_AFK("option.eHQAFK"),
    OPTION_USE_GROUPED_LIST("option.useGroupedList"),
    OPTION_LIST_VAR("option.listVar"),
    OPTION_COLLAPSED_LIST_VAR("option.collapsedListVars"),
    OPTION_AFK_TIMER("option.AFKTimer"),
    OPTION_AFK_KICK_TIMER("option.AFKKickTimer"),
    OPTION_USE_AFK_LIST("option.useAFKList"),

    ALIASES_WHO("aliases.mchatwho"),
    ALIASES_LIST("aliases.mchatlist"),
    ALIASES_AFK("aliases.mchatafk"),
    ALIASES_AFK_OTHER("aliases.mchatafkother");

    private final String option;
    private final Object object;

    ConfigType(String option) {
        this.option = option;
        this.object = getObject();
    }

    private Object getObject() {
        Object value = ConfigUtil.getConfig().get(option);

        if (value instanceof String) {
            String val = (String) value;

            value = MessageUtil.addColour(val);
        }

        return value;
    }

    public Boolean getBoolean() {
        return object instanceof Boolean ? (Boolean) object : false;
    }

    public String getString() {
        return object != null ? object.toString() : null;
    }

    public Integer getInteger() {
        return object instanceof Number ? (Integer) object : 0;
    }

    public Double getDouble() {
        return object instanceof Number ? (Double) object : 0;
    }

    public ArrayList<String> getList() {
        ArrayList<String> list = new ArrayList<String>();

        if (object instanceof ArrayList) {
            ArrayList aList = (ArrayList) object;

            for (Object obj : aList)
                list.add((String) obj);

        }

        return list;
    }
}
