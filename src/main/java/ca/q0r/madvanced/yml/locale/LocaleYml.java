package ca.q0r.madvanced.yml.locale;

import ca.q0r.mchat.yml.Yml;

import java.io.File;

public class LocaleYml extends Yml {
    public LocaleYml() {
        super(new File("plugins/MAdvanced/locale.yml"), "MAdvanced Locale");

        loadDefaults();
    }

    public void loadDefaults() {
        checkOption("message.afk.afk", "AFK");
        checkOption("message.afk.default", "Away From Keyboard");
        checkOption("message.list.header", "&6-- There are &8%players&6 out of the maximum of &8%max&6 Players online. --");
        checkOption("message.player.afk", "%player is now AFK. [ %reason ]");
        checkOption("message.player.notAfk", "%player is no longer AFK.");
        checkOption("message.player.stillAfk", "You are still AFK.");

        save();
    }
}
