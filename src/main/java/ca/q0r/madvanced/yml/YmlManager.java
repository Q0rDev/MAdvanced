package ca.q0r.madvanced.yml;

import ca.q0r.madvanced.yml.config.ConfigYml;
import ca.q0r.madvanced.yml.locale.LocaleYml;
import ca.q0r.mchat.yml.Yml;

/**
 * Used to Manage all YML Configs.
 */
public class YmlManager {
    static ConfigYml configYml;
    static LocaleYml localeYml;

    /**
     * Class Initializer.
     */
    public static void initialize() {
        configYml = new ConfigYml();
        configYml.loadDefaults();

        localeYml = new LocaleYml();
        localeYml.loadDefaults();
    }

    /**
     * YML retriever.
     *
     * @param type Type of Config to get.
     * @return YML Config.
     */
    public static Yml getYml(YmlType type) {
        if (type == YmlType.CONFIG_YML) {
            return configYml;
        } else if (type == YmlType.LOCALE_YML) {
            return localeYml;
        }

        return null;
    }

    /**
     * YML Reloader.
     *
     * @param type Type of Config to reload.
     */
    public static void reloadYml(YmlType type) {
        if (type == YmlType.CONFIG_YML) {
            configYml = new ConfigYml();
            configYml.loadDefaults();
        } else if (type == YmlType.LOCALE_YML) {
            localeYml = new LocaleYml();
            localeYml.loadDefaults();
        }
    }

    /**
     * YML Unloader. Unloads all configs.
     */
    public static void unload() {
        configYml = null;
        localeYml = null;
    }
}
