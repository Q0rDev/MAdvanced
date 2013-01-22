package ca.q0r.madvanced.configs;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LocaleUtil {
    static YamlConfiguration config;
    static File file;
    static Boolean changed;

    public static void initialize() {
        load();
    }

    public static void dispose() {
        config = null;
        file = null;
        changed = null;
    }

    public static void load() {
        file = new File("plugins/MAdvanced/locale.yml");

        config = YamlConfiguration.loadConfiguration(file);

        config.options().indent(4);
        config.options().header("MAdvanced Locale");

        changed = false;

        loadDefaults();
    }

    private static void loadDefaults() {
        checkOption("message.afk.afk", "AFK");
        checkOption("message.afk.default", "Away From Keyboard");
        checkOption("message.config.reloaded", "%config Reloaded.");
        checkOption("message.config.updated", "%config has been updated.");
        checkOption("message.list.header", "&6-- There are &8%players&6 out of the maximum of &8%max&6 Players online. --");
        checkOption("message.player.afk", "%player is now AFK. [ %reason ]");
        checkOption("message.player.notAfk", "%player is no longer AFK.");
        checkOption("message.player.stillAfk", "You are still AFK.");
        checkOption("message.spout.colour", "dark_red");
        checkOption("message.spout.typing", "*Typing*");

        save();
    }

    public static void set(String key, Object obj) {
        config.set(key, obj);

        changed = true;
    }

    public static Boolean save() {
        if (!changed)
            return false;

        try {
            config.save(file);
            changed = false;
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static YamlConfiguration getConfig() {
        return config;
    }

    private static void checkOption(String option, Object defValue) {
        if (!config.isSet(option))
            set(option, defValue);
    }
}
