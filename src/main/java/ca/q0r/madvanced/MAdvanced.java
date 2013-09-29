package ca.q0r.madvanced;

import ca.q0r.madvanced.commands.*;
import ca.q0r.madvanced.events.*;
import ca.q0r.madvanced.yml.YmlManager;
import ca.q0r.madvanced.yml.config.ConfigType;
import ca.q0r.madvanced.yml.locale.LocaleType;
import ca.q0r.mchat.api.API;
import ca.q0r.mchat.api.Parser;
import ca.q0r.mchat.util.MessageUtil;
import ca.q0r.mchat.util.Timer;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.player.SpoutPlayer;

import java.util.Date;
import java.util.HashMap;

public class MAdvanced extends JavaPlugin {
    // Default Plugin Data
    private PluginManager pm;
    private PluginDescriptionFile pdfFile;

    // Spout
    public Boolean spoutB = false;

    // Maps
    public HashMap<String, Boolean> isChatting = new HashMap<String, Boolean>();
    public HashMap<String, Boolean> isAFK = new HashMap<String, Boolean>();

    public HashMap<String, Location> AFKLoc = new HashMap<String, Location>();

    public HashMap<String, Long> lastMove = new HashMap<String, Long>();


    public void onEnable() {
        // Initialize Plugin Data
        pm = getServer().getPluginManager();
        pdfFile = getDescription();

        try {
            // Initialize and Start the Timer
            Timer timer = new Timer();

            // Initialize Metrics
            /*getServer().getScheduler().runTaskLater(this, new BukkitRunnable(){
				@Override
				public void run() {
					try {
						Metrics metrics = new Metrics(Bukkit.getPluginManager().getPlugin("MAdvanced"));
			            metrics.start();
			        } catch (IOException ignored) {}
				}
			}, 200);*/

            // Load Yml
            YmlManager.initialize();

            // Setup Plugins
            setupPlugins();

            // Register Events
            registerEvents();

            // Setup Tasks
            setupTasks();

            // Setup Commands
            setupCommands();

            for (Player players : getServer().getOnlinePlayers()) {
                isChatting.put(players.getName(), false);
                isAFK.put(players.getName(), false);
                lastMove.put(players.getName(), new Date().getTime());

                if (spoutB) {
                    SpoutPlayer sPlayers = (SpoutPlayer) players;
                    sPlayers.setTitle(Parser.parsePlayerName(players.getName(), players.getWorld().getName()));
                }
            }

            // Stop the Timer
            timer.stop();

            // Calculate Startup Timer
            long diff = timer.difference();

            MessageUtil.log("[" + pdfFile.getName() + "] " + pdfFile.getName() + " v" + pdfFile.getVersion() + " is enabled! [" + diff + "ms]");
        } catch(NoClassDefFoundError ignored) {
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
        } catch(NoClassDefFoundError ignored) {
            System.err.println("[" + pdfFile.getName() + "] MChat not found disabling!");
            System.out.println("[" + pdfFile.getName() + "] " + pdfFile.getName() + " v" + pdfFile.getVersion() + " is disabled!");
        }
    }

    Boolean setupPlugin(String pluginName) {
        Plugin plugin = pm.getPlugin(pluginName);

        if (plugin != null) {
            MessageUtil.log("[" + pdfFile.getName() + "] <Plugin> " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " hooked!.");
            return true;
        }

        return false;
    }

    void setupPlugins() {
        spoutB = setupPlugin("Spout");

        if (!ConfigType.MCHAT_SPOUT.getBoolean()) {
            spoutB = false;
        }
    }

    void registerEvents() {
        if (spoutB) {
            pm.registerEvents(new CustomListener(this), this);
        }

        pm.registerEvents(new ChatListener(this), this);
        pm.registerEvents(new CommandListener(), this);
        pm.registerEvents(new EntityListener(this), this);
        pm.registerEvents(new PlayerListener(this), this);
    }

    void setupTasks() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if (ConfigType.OPTION_AFK_TIMER.getInteger() < 1) {
                    return;
                }

                for (Player player : getServer().getOnlinePlayers()) {
                    if (isAFK.get(player.getName()) == null) {
                        isAFK.put(player.getName(), false);
                    }

                    if (isAFK.get(player.getName()) || lastMove.get(player.getName()) == null
                            || API.checkPermissions(player.getName(), player.getWorld().getName(), "mchat.bypass.afk")) {
                        continue;
                    }

                    if (new Date().getTime() - (ConfigType.OPTION_AFK_TIMER.getInteger() * 1000) > lastMove.get(player.getName())) {
                        getServer().dispatchCommand(getServer().getConsoleSender(), "mchatafkother " + player.getName() + " " + LocaleType.MESSAGE_AFK_DEFAULT.getVal());
                    } else {
                        isAFK.put(player.getName(), false);
                    }
                }
            }
        }, 20L * 5, 20L * 5);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if (ConfigType.OPTION_AFK_KICK_TIMER.getInteger() < 1) {
                    return;
                }

                for (Player player : getServer().getOnlinePlayers()) {
                    if (API.checkPermissions(player.getName(), player.getWorld().getName(), "mchat.bypass.afkkick")) {
                        continue;
                    }

                    if (!isAFK.get(player.getName())) {
                        continue;
                    }

                    if (new Date().getTime() - (ConfigType.OPTION_AFK_KICK_TIMER.getInteger() * 1000) > lastMove.get(player.getName())) {
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
