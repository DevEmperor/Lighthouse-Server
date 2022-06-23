package net.devemperor.lighthouse.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.IOException;

public class ScoreboardWorld implements Listener {

    private final Main plugin;

    static File stats = new File("plugins/Lighthouse", "stats.yml");
    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(stats);
    int seconds = 0;


    public ScoreboardWorld(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        updateScoreboard(player);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Player player = e.getEntity().getKiller();
        if (player != null) {
            cfg.set(player.getUniqueId() + "." + ".kills", cfg.getInt(player.getUniqueId() + "." + ".kills") + 1);
        }
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent e) {
        if (e.getAmount() > 0) {
            Player player = e.getPlayer();
            cfg.set(player.getUniqueId() + "." + ".exp", cfg.getLong(player.getUniqueId() + "." + ".exp") + e.getAmount());

            WorldBorder.updateWorldBorder(player.getWorld());
        }
    }

    public void startTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                int hours = cfg.getInt(player.getUniqueId() + "." + ".hours");
                int minutes = cfg.getInt(player.getUniqueId() + "." + ".minutes");

                if (seconds == 58) {
                    seconds = 0;
                    if (minutes == 59) {
                        minutes = 0;
                        hours++;
                    } else {
                        minutes++;
                    }

                    cfg.set(player.getUniqueId() + "." + ".hours", hours);
                    cfg.set(player.getUniqueId() + "." + ".minutes", minutes);

                    try {
                        cfg.save(stats);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    seconds+=2;
                }

                updateScoreboard(player);
            }
        }, 20*2, 20*2);
    }

    private void updateScoreboard(Player player) {
        long total_exp = cfg.getLong(player.getUniqueId() + "." + ".exp");
        int kills = cfg.getInt(player.getUniqueId() + "." + ".kills");
        int deaths = cfg.getInt(player.getUniqueId() + "." + ".deaths");
        String time = getTime(player);
        
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = board.registerNewObjective("scoreboard", "criteria", "Lighthouse-Server");
        obj.setDisplayName("§6§n§lLighthouse-Server");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score fourteen = obj.getScore(" ");
        Score thirteen = obj.getScore("§a§lTotal experience:");
        Score twelve = obj.getScore(String.format("%,d", total_exp) + " experience");
        Score eleven = obj.getScore("§a§lCurrent experience:");
        Score ten = obj.getScore(String.format("%,d", player.getTotalExperience()) + " experience");
        Score nine = obj.getScore("  ");
        Score eight = obj.getScore("§a§lKills / Deaths:");
        Score seven = obj.getScore(kills + " / " + deaths);
        Score six = obj.getScore("   ");
        Score five = obj.getScore("§a§lBorder size:");
        Score four = obj.getScore(WorldBorder.getBorderSize() + " blocks");
        Score three = obj.getScore("    ");
        Score two = obj.getScore("§a§lPlaytime:");
        Score one = obj.getScore(cfg.getInt(player.getUniqueId() + "." + ".hours") + " hours, "
                + cfg.getInt(player.getUniqueId() + "." + ".minutes") + " minutes");
        Score zero = obj.getScore("§eIt is " + time);

        fourteen.setScore(14);
        thirteen.setScore(13);
        twelve.setScore(12);
        eleven.setScore(11);
        ten.setScore(10);
        nine.setScore(9);
        eight.setScore(8);
        seven.setScore(7);
        six.setScore(6);
        five.setScore(5);
        four.setScore(4);
        three.setScore(3);
        two.setScore(2);
        one.setScore(1);
        zero.setScore(0);

        player.setScoreboard(board);
    }

    private String getTime(Player player) {
        long current = player.getWorld().getTime();
        int hours = (int) (current / 1000) + 6;
        int minutes = (int) (current % 1000) / 50 * 3;
        if (hours >= 24) { hours = hours - 24; }
        String day = ChatColor.GREEN + String.valueOf(hours) + ":" + String.format("%02d", minutes);
        if (current >= 0 && current <= 12000) {
            return day;
        }
        if (current > 23000 || (current > 12000 && current < 13000)) {
            return ChatColor.GOLD + String.valueOf(hours) + ":" + String.format("%02d", minutes);
        }
        if (current >= 13000) {
            return ChatColor.RED + String.valueOf(hours) + ":" + String.format("%02d", minutes);
        }
        return day;
    }
}
