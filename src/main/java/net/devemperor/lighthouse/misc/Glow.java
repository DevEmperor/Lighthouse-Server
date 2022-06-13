package net.devemperor.lighthouse.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.devemperor.lighthouse.util.Util;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class Glow implements CommandExecutor {

    static File glow = new File("plugins/Lighthouse", "glow.yml");
    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(glow);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("glow")) {
                if (args.length == 1) {
                    Player dest = Bukkit.getPlayerExact(args[0]);
                    if (dest == null) {
                        sender.sendMessage(Util.PREFIX + ChatColor.RED + "This player isn't online!");
                    } else if (cfg.getBoolean(dest.getUniqueId() + "." + ".isGlowing")) {
                        dest.removePotionEffect(PotionEffectType.GLOWING);
                        cfg.set(dest.getUniqueId() + "." + ".isGlowing", false);
                        try { cfg.save(glow); } catch (IOException e) { e.printStackTrace(); }
                        sender.sendMessage(Util.PREFIX + ChatColor.GOLD + dest.getName() + ChatColor.GREEN + " is not glowing anymore!");
                        return true;
                    } else {
                        dest.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1));
                        cfg.set(dest.getUniqueId() + "." + ".isGlowing", true);
                        try { cfg.save(glow); } catch (IOException e) { e.printStackTrace(); }
                        sender.sendMessage(Util.PREFIX + ChatColor.GOLD + dest.getName() + ChatColor.GREEN + " is now glowing!");
                        return true;
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
