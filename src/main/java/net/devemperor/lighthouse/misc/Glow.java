package net.devemperor.lighthouse.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.devemperor.lighthouse.util.Util;
import org.jetbrains.annotations.NotNull;

public class Glow implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("glow")) {
                if (args.length == 2) {
                    Player dest = Bukkit.getPlayerExact(args[0]);
                    if (dest == null) {
                        sender.sendMessage(Util.PREFIX + ChatColor.RED + "This player isn't online!");
                    } else {
                        try {
                            dest.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20*Integer.parseInt(args[1]), 1));
                            sender.sendMessage(Util.PREFIX + ChatColor.GOLD + dest.getName() + ChatColor.GREEN + " is now glowing!");
                            return true;
                        } catch (NumberFormatException e) {
                            return false;
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
