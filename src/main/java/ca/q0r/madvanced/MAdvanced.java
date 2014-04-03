package ca.q0r.madvanced;

import ca.q0r.madvanced.commands.*;
import ca.q0r.madvanced.events.ChatListener;
import ca.q0r.madvanced.events.CommandListener;
import ca.q0r.madvanced.events.EntityListener;
import ca.q0r.madvanced.events.PlayerListener;
import ca.q0r.madvanced.yml.YmlManager;
import ca.q0r.madvanced.yml.config.ConfigType;
import ca.q0r.madvanced.yml.locale.LocaleType;
import ca.q0r.mchat.api.API;
import ca.q0r.mchat.metrics.Metrics;
import ca.q0r.mchat.util.MessageUtil;
import ca.q0r.mchat.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class MAdvanced extends JavaPlugin {
    // Default Plugin Data
    private PluginManager pm;
    private PluginDescriptionFile pdfFile;

    // Maps
    public HashMap<UUID, Boolean> isChatting = new HashMap<>();
    public HashMap<UUID, Boolean> isAFK = new HashMap<>();

    public HashMap<UUID, Location> AFKLoc = new HashMap<>();

    public HashMap<UUID, Long> lastMove = new HashMap<>();


    public void onEnable() {
        // Initialize Plugin Data
        pm = getServer().getPluginManager();
        pdfFile = getDescription();

        try {
            // Initialize and Start the Timer
            Timer timer = new Timer();

            // Initialize Metrics
            getServer().getScheduler().runTaskLater(this, new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        Metrics metrics = new Metrics(Bukkit.getPluginManager().getPlugin("MAdvanced"));
                        metrics.start();
                    } catch (Exception ignored) {
                    }
                }
            }, 200);

            // Load Yml
            YmlManager.initialize();

            // Register Events
            registerEvents();

            // Setup Tasks
            setupTasks();

            // Setup Commands
            setupCommands();

            for (Player players : getServer().getOnlinePlayers()) {
                isChatting.put(players.getUniqueId(), false);
                isAFK.put(players.getUniqueId(), false);
                lastMove.put(players.getUniqueId(), new Date().getTime());
            }

            // Stop the Timer
            timer.stop();

            // Calculate Startup Timer
            long diff = timer.difference();

            MessageUtil.log("[" + pdfFile.getName() + "] " + pdfFile.getName() + " v" + pdfFile.getVersion() + " is enabled! [" + diff + "ms]");
        } catch (NoClassDefFoundError ignored) {
            pm.disablePlugin(this);
        }
    }

    public void onDisable() {
        try {
            // Initialize and Start the Timer
            Timer timer = new Timer();

            getServer().getScheduler().cancelTasks(this);

            // Unload Yml
            YmlManager.unload();

            // Stop the Timer
            timer.stop();

            // Calculate Shutdown Timer
            long diff = timer.difference();

            MessageUtil.log("[" + pdfFile.getName() + "] " + pdfFile.getName() + " v" + pdfFile.getVersion() + " is disabled! [" + diff + "ms]");
        } catch (NoClassDefFoundError ignored) {
            System.err.println("[" + pdfFile.getName() + "] MChat not found disabling!");
            System.out.println("[" + pdfFile.getName() + "] " + pdfFile.getName() + " v" + pdfFile.getVersion() + " is disabled!");
        }
    }

    void registerEvents() {
        pm.registerEvents(new ChatListener(this), this);
        pm.registerEvents(new CommandListener(), this);
        pm.registerEvents(new EntityListener(this), this);
        pm.registerEvents(new PlayerListener(this), this);
    }

    void setupTasks() {
        getServer().getScheduler().runTaskTimer(this, new Runnable() {
            public void run() {
                if (ConfigType.OPTION_AFK_TIMER.getInteger() < 1) {
                    return;
                }

                for (Player player : getServer().getOnlinePlayers()) {
                    if (isAFK.get(player.getUniqueId()) == null) {
                        isAFK.put(player.getUniqueId(), false);
                    }

                    if (isAFK.get(player.getUniqueId()) || lastMove.get(player.getUniqueId()) == null
                            || API.checkPermissions(player.getName(), player.getWorld().getName(), "mchat.bypass.afk")) {
                        continue;
                    }

                    if (new Date().getTime() - (ConfigType.OPTION_AFK_TIMER.getInteger() * 1000) > lastMove.get(player.getUniqueId())) {
                        getServer().dispatchCommand(getServer().getConsoleSender(), "mchatafkother " + player.getName() + " " + LocaleType.MESSAGE_AFK_DEFAULT.getVal());
                    } else {
                        isAFK.put(player.getUniqueId(), false);
                    }
                }
            }
        }, 20L * 5, 20L * 5);

        getServer().getScheduler().runTaskTimer(this, new Runnable() {
            public void run() {
                if (ConfigType.OPTION_AFK_KICK_TIMER.getInteger() < 1) {
                    return;
                }

                for (Player player : getServer().getOnlinePlayers()) {
                    if (API.checkPermissions(player.getName(), player.getWorld().getName(), "mchat.bypass.afkkick")) {
                        continue;
                    }

                    if (!isAFK.get(player.getUniqueId())) {
                        continue;
                    }

                    if (new Date().getTime() - (ConfigType.OPTION_AFK_KICK_TIMER.getInteger() * 1000) > lastMove.get(player.getUniqueId())) {
                        player.kickPlayer(LocaleType.MESSAGE_AFK_DEFAULT.getVal());
                    }
                }
            }
        }, 20L * 10, 20L * 10);
    }

    void setupCommands() {
        regCommands("madvanced", new AdvancedCommand());
        regCommands("mchatafk", new AFKCommand(this));
        regCommands("mchatafkother", new AFKOtherCommand(this));
        regCommands("mchatlist", new ListCommand(this));
        regCommands("mchatwho", new WhoCommand(this));
    }

    void regCommands(String command, CommandExecutor executor) {
        if (getCommand(command) != null)
            getCommand(command).setExecutor(executor);
    }
}