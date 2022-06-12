package net.devemperor.lighthouse.main;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.devemperor.lighthouse.misc.Compass;
import net.devemperor.lighthouse.misc.DailyRewards;
import net.devemperor.lighthouse.misc.Donate;
import net.devemperor.lighthouse.misc.Glow;
import net.devemperor.lighthouse.misc.Homes;
import net.devemperor.lighthouse.misc.Locations;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);

        ScoreboardWorld board = new ScoreboardWorld(this);
        Bukkit.getPluginManager().registerEvents(board, this);
        board.startTimer();

        WorldBorder border = new WorldBorder();
        Bukkit.getPluginManager().registerEvents(border, this);

        Compass compass = new Compass(this);
        Bukkit.getPluginManager().registerEvents(compass, this);
        getCommand("whereis").setExecutor(compass);

        Homes teleporter = new Homes(this);
        getCommand("sethome").setExecutor(teleporter);
        getCommand("home").setExecutor(teleporter);
        getCommand("home").setTabCompleter(teleporter);

        Locations locations = new Locations();
        getCommand("loc").setExecutor(locations);
        getCommand("loc").setTabCompleter(locations);

        getCommand("reward").setExecutor(new DailyRewards());

        getCommand("donate").setExecutor(new Donate());

        getCommand("glow").setExecutor(new Glow());
    }
}
