package net.devemperor.lighthouse.misc;

import net.devemperor.lighthouse.main.ScoreboardWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.devemperor.lighthouse.util.Util;
import org.jetbrains.annotations.NotNull;

public class Donate implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("donate")) {
                if (args.length == 2) {
                    Player dest = Bukkit.getPlayerExact(args[0]);
                    int amount;
                    try {
                        amount = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(Util.PREFIX + ChatColor.RED + "The amount isn't a valid number!");
                        return true;
                    }
                    if (dest == null) {
                        sender.sendMessage(Util.PREFIX + ChatColor.RED + "This player isn't online!");
                    } else {
                        if (player.getName().equals(dest.getName())) {
                            sender.sendMessage(Util.PREFIX + ChatColor.RED + "You can't give yourself experience! ;-)");
                        } else {
                            if (amount > player.getTotalExperience()) {
                                sender.sendMessage(Util.PREFIX + ChatColor.RED + "You haven't got enough experience!");
                            } else {
                                dest.giveExp(amount);
                                player.giveExp(-amount);
                                ScoreboardWorld.cfg.set(player.getUniqueId() + "." + ".exp", ScoreboardWorld.cfg.getLong(player.getUniqueId() + "." + ".exp") - amount);
                                sender.sendMessage(Util.PREFIX + ChatColor.GREEN + "You donated " + ChatColor.AQUA + amount
                                        + ChatColor.GREEN + " experience to " + ChatColor.YELLOW + dest.getName());
                                dest.sendMessage(Util.PREFIX + ChatColor.GREEN + "You received " + ChatColor.AQUA + amount
                                        + ChatColor.GREEN + " experience from " + ChatColor.YELLOW + player.getName());
                            }
                        }
                    }
                } else {
                    return false;
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
