package ca.q0r.madvanced.yml.config;

import ca.q0r.mchat.yml.Yml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ConfigYml extends Yml {
    private ArrayList<String> whoAliases;
    private ArrayList<String> listAliases;
    private ArrayList<String> afkAliases;
    private ArrayList<String> afkOtherAliases;

    private HashMap<String, List<String>> aliasMap;

    public ConfigYml() {
        super(new File("plugins/MAdvanced/config.yml"), "MAdvanced Config");

        whoAliases = new ArrayList<String>();
        listAliases = new ArrayList<String>();
        afkAliases = new ArrayList<String>();
        afkOtherAliases = new ArrayList<String>();

        aliasMap = new HashMap<String, List<String>>();

        loadDefaults();
    }

    public void loadDefaults() {
        checkOption("option.eHQAFK", true);
        checkOption("option.useGroupedList", true);
        checkOption("option.listVar", "group");
        checkOption("option.collapsedListVars", "default,Default");
        checkOption("option.AFKTimer", 30);
        checkOption("option.AFKKickTimer", 120);
        checkOption("option.useAFKList", false);

        loadAliases();

        checkOption("aliases.mchatwho", whoAliases);
        checkOption("aliases.mchatlist", listAliases);
        checkOption("aliases.mchatafk", afkAliases);
        checkOption("aliases.mchatafkother", afkOtherAliases);

        setupAliasMap();

        save();
    }

    public HashMap<String, List<String>> getAliasMap() {
        return aliasMap;
    }

    private void loadAliases() {
        whoAliases.add("who");

        listAliases.add("list");
        listAliases.add("online");
        listAliases.add("playerlist");

        afkAliases.add("afk");
        afkAliases.add("away");

        afkOtherAliases.add("afko");
        afkOtherAliases.add("awayother");
        afkOtherAliases.add("awayo");
    }

    private void setupAliasMap() {
        Set<String> keys = config.getConfigurationSection("aliases").getKeys(false);

        for (String key : keys) {
            aliasMap.put(key, config.getStringList("aliases." + key));
        }
    }
}
