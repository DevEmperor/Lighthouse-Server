package net.devemperor.lighthouse.misc;

import net.devemperor.lighthouse.main.Main;
import net.devemperor.lighthouse.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Homes implements CommandExecutor, TabCompleter {

    private final Main plugin;
    private int scheduler;

    File homes = new File("plugins/Lighthouse", "homes.yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(homes);


    public Homes(Main plugin) {
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("sethome")) {

                Location loc = player.getLocation();

                cfg.set(player.getUniqueId() + "." + ".x", loc.getX());
                cfg.set(player.getUniqueId() + "." + ".y", loc.getY());
                cfg.set(player.getUniqueId() + "." + ".z", loc.getZ());
                cfg.set(player.getUniqueId() + "." + ".yaw", loc.getYaw());
                cfg.set(player.getUniqueId() + "." + ".pitch", loc.getPitch());
                try {
                    cfg.save(homes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                sender.sendMessage(Util.PREFIX + ChatColor.GREEN + "Your home location was set to: " + loc.getBlockX()
                        + " | " + loc.getBlockY() + " | " + loc.getBlockZ() + " !");
                return true;
            } else if (command.getName().equalsIgnoreCase("home")) {
                if (args.length == 1 && args[0].equalsIgnoreCase("cancel")) {
                    if (cfg.getBoolean(player.getUniqueId() + "." + ".tel")) {
                        Bukkit.getScheduler().cancelTask(scheduler);
                        player.stopSound(Sound.BLOCK_PORTAL_TRAVEL);
                        player.removePotionEffect(PotionEffectType.CONFUSION);
                        cfg.set(player.getUniqueId() + "." + ".tel", "false");
                        sender.sendMessage(Util.PREFIX + ChatColor.GREEN + "Teleport canceled!");
                    } else {
                        sender.sendMessage(Util.PREFIX + ChatColor.RED + "There is nothing to cancel!");
                    }
                    return true;
                } else if (args.length == 0) {
                    if (cfg.contains(player.getUniqueId().toString())) {
                        double x = cfg.getDouble(player.getUniqueId() + "." + ".x");
                        double y = cfg.getDouble(player.getUniqueId() + "." + ".y");
                        double z = cfg.getDouble(player.getUniqueId() + "." + ".z");
                        double yaw = cfg.getDouble(player.getUniqueId() + "." + ".yaw");
                        double pitch = cfg.getDouble(player.getUniqueId() + "." + ".pitch");

                        Location loc = new Location(player.getWorld(), x, y, z);
                        loc.setYaw((float) yaw);
                        loc.setPitch((float) pitch);

                        cfg.set(player.getUniqueId() + "." + ".tel", true);
                        scheduler = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            player.teleport(loc);
                            sender.sendMessage(Util.PREFIX + ChatColor.GREEN + "You got teleported to your home!");
                            player.stopSound(Sound.BLOCK_PORTAL_TRAVEL);
                            cfg.set(player.getUniqueId() + "." + ".tel", "false");
                        }, 20*6);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*9, 3));
                        player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.2F, 0);
                    } else {
                        sender.sendMessage(Util.PREFIX + ChatColor.RED + "You must set a home first using the /sethome-command!");
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            sender.sendMessage(Util.CMD_ONLY_PLAYER);
            return true;
        }
        return false;
    }



    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("home")) {
            return Collections.singletonList("cancel");
        }
        return null;
    }
}
