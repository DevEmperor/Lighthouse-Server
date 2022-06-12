package net.devemperor.lighthouse.misc;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.devemperor.lighthouse.util.Util;
import org.jetbrains.annotations.NotNull;

public class DailyRewards implements CommandExecutor {

    static File rewards = new File("plugins/Lighthouse", "rewards.yml");
    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(rewards);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("reward")) {
                long now = System.currentTimeMillis();
                long lastReward = cfg.getLong(player.getUniqueId() + "." + ".lastReward");

                if ((now - lastReward) > 86400000) {
                    cfg.set(player.getUniqueId() + "." + ".lastReward", now);
                    int exp = new Random().nextInt(100 - 1 + 1) + 1;
                    if (exp != 100) {
                        exp = exp / 10;
                    }
                    
                    player.giveExp(exp);
                    sender.sendMessage(Util.PREFIX + ChatColor.GREEN + "You got " + ChatColor.AQUA + exp + ChatColor.GREEN + " experience!");
                    try {
                        cfg.save(rewards);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    sender.sendMessage(Util.PREFIX + ChatColor.RED + "You can only get a reward once in 24 hours!");
                }
                return true;
            }
        } else {
            sender.sendMessage(Util.CMD_ONLY_PLAYER);
            return true;
        }
        return false;
    }
}
