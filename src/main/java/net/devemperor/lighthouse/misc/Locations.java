package net.devemperor.lighthouse.misc;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.devemperor.lighthouse.util.Util;
import org.jetbrains.annotations.NotNull;

public class Locations implements CommandExecutor, TabCompleter {

    File locs = new File("plugins/Lighthouse", "locations.yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(locs);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("loc")) {
                if (args.length != 0) {

                    Player player = (Player) sender;

                    if (args[0].equalsIgnoreCase("add")) {
                        if (args.length == 2) {
                            Location loc = player.getLocation();

                            if (cfg.contains(player.getUniqueId() + "." + args[1])) {
                                player.sendMessage(Util.PREFIX + ChatColor.RED + "There is already a location with this name.\n"
                                        + "Please do /loc remove " + args[1] + " first!");
                            } else {
                                cfg.set(player.getUniqueId() + "." + args[1] + "." + ".x", loc.getBlockX());
                                cfg.set(player.getUniqueId() + "." + args[1] + "." + ".y", loc.getBlockY());
                                cfg.set(player.getUniqueId() + "." + args[1] + "." + ".z", loc.getBlockZ());
                                try {
                                    cfg.save(locs);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                player.sendMessage(Util.PREFIX + ChatColor.GREEN + "Added location " + ChatColor.ITALIC + args[1] + ChatColor.GREEN
                                        + " (" + ChatColor.DARK_AQUA + loc.getBlockX() + ChatColor.GREEN + " | "
                                        + ChatColor.DARK_AQUA + loc.getBlockY() + ChatColor.GREEN + " | "
                                        + ChatColor.DARK_AQUA + loc.getBlockZ() + ChatColor.GREEN + ")" + ChatColor.GREEN + " !");
                            }
                            return true;
                        } else {
                            return false;
                        }
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length == 2) {
                            if (cfg.contains(player.getUniqueId() + "." + args[1])) {
                                cfg.set(player.getUniqueId() + "." + args[1], null);
                                try {
                                    cfg.save(locs);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(Util.PREFIX + ChatColor.GREEN + "Removed location " + ChatColor.ITALIC
                                        + args[1] + ChatColor.GREEN + " !");
                            } else {
                                player.sendMessage(Util.PREFIX + ChatColor.RED + "There isn't a location with this name!");
                            }
                            return true;
                        } else {
                            return false;
                        }
                    } else if (args[0].equalsIgnoreCase("list")) {
                        if (args.length == 1) {
                            Object[] names;
                            try {
                                names = cfg.getConfigurationSection(player.getUniqueId().toString()).getKeys(false).toArray();
                            } catch (NullPointerException e) {
                                player.sendMessage(Util.PREFIX + ChatColor.RED + "You haven't saved any locations yet!");
                                return true;
                            }
                            if (names.length != 0) {
                                for (int i = 0; i < names.length; i++) {
                                    int x = cfg.getInt(player.getUniqueId() + "." + names[i] + "." + ".x");
                                    int y = cfg.getInt(player.getUniqueId() + "." + names[i] + "." + ".y");
                                    int z = cfg.getInt(player.getUniqueId() + "." + names[i] + "." + ".z");

                                    if (i != 0) {
                                        player.sendMessage(ChatColor.GREEN + "           Location " + ChatColor.ITALIC + names[i] + ChatColor.GREEN
                                                + " is: X = " + ChatColor.DARK_AQUA + x + ChatColor.GREEN + ", Y = " + ChatColor.DARK_AQUA + y
                                                + ChatColor.GREEN + ", Z = " + ChatColor.DARK_AQUA + z + ChatColor.GREEN + " !");
                                    } else {
                                        player.sendMessage(Util.PREFIX + ChatColor.GREEN + "Location " + ChatColor.ITALIC + names[i] + ChatColor.GREEN
                                                + " is: X = " + ChatColor.DARK_AQUA + x + ChatColor.GREEN + ", Y = " + ChatColor.DARK_AQUA + y
                                                + ChatColor.GREEN + ", Z = " + ChatColor.DARK_AQUA + z + ChatColor.GREEN + " !");
                                    }
                                }
                            } else {
                                player.sendMessage(Util.PREFIX + ChatColor.RED + "You haven't saved any locations yet!");
                            }
                            return true;
                        } else if (args.length == 2) {
                            if (cfg.contains(player.getUniqueId() + "." + args[1])) {
                                int x = cfg.getInt(player.getUniqueId() + "." + args[1] + "." + ".x");
                                int y = cfg.getInt(player.getUniqueId() + "." + args[1] + "." + ".y");
                                int z = cfg.getInt(player.getUniqueId() + "." + args[1] + "." + ".z");

                                player.sendMessage(Util.PREFIX + ChatColor.GREEN + "Location " + ChatColor.ITALIC + args[1] + ChatColor.GREEN
                                        + " is: X = " + ChatColor.DARK_AQUA + x + ChatColor.GREEN + ", Y = " + ChatColor.DARK_AQUA + y
                                        + ChatColor.GREEN + ", Z = " + ChatColor.DARK_AQUA + z + ChatColor.GREEN + " !");
                            } else {
                                player.sendMessage(Util.PREFIX + ChatColor.RED + "There isn't a location with this name!");
                            }
                            return true;
                        } else {
                            return false;
                        }
                    } else if (args[0].equalsIgnoreCase("share")) {
                        if (args.length == 1) {
                            Bukkit.broadcastMessage(Util.PREFIX + ChatColor.GREEN + "Location of " + ChatColor.ITALIC + player.getName() + ChatColor.RESET + ChatColor.GREEN
                                    + " is: " + "X = " + ChatColor.DARK_AQUA + player.getLocation().getBlockX() + ChatColor.GREEN + ", Y = "
                                    + ChatColor.DARK_AQUA + player.getLocation().getBlockY() + ChatColor.GREEN + ", Z = " + ChatColor.DARK_AQUA + player.getLocation().getBlockZ()
                                    + ChatColor.GREEN + " !");
                            return true;
                        }

                        if (cfg.contains(player.getUniqueId() + "." + args[1])) {
                            int x = cfg.getInt(player.getUniqueId() + "." + args[1] + "." + ".x");
                            int y = cfg.getInt(player.getUniqueId() + "." + args[1] + "." + ".y");
                            int z = cfg.getInt(player.getUniqueId() + "." + args[1] + "." + ".z");

                            if (args.length == 2) {
                                Bukkit.broadcastMessage(Util.PREFIX + ChatColor.GREEN + "Location " + ChatColor.ITALIC + args[1] + ChatColor.RESET + ChatColor.GREEN
                                        + " is: " + "X = " + ChatColor.DARK_AQUA + x + ChatColor.GREEN + ", Y = "
                                        + ChatColor.DARK_AQUA + y + ChatColor.GREEN + ", Z = " + ChatColor.DARK_AQUA + z
                                        + ChatColor.GREEN + " ! (sent by " + ChatColor.YELLOW + player.getName() + ChatColor.GREEN + ")");
                                return true;
                            } else if (args.length == 3) {
                                Player receiver = Bukkit.getPlayerExact(args[2]);
                                if (receiver != null) {
                                    receiver.sendMessage(Util.PREFIX + ChatColor.GREEN + "Location " + ChatColor.ITALIC + args[1] + ChatColor.RESET + ChatColor.GREEN
                                            + " is: " + "X = " + ChatColor.DARK_AQUA + x + ChatColor.GREEN + ", Y = "
                                            + ChatColor.DARK_AQUA + y + ChatColor.GREEN + ", Z = " + ChatColor.DARK_AQUA + z
                                            + ChatColor.GREEN + " ! (sent by " + ChatColor.YELLOW + player.getName() + ChatColor.GREEN + ")");
                                    player.sendMessage(Util.PREFIX + ChatColor.GREEN + "Location " + ChatColor.ITALIC + args[1] + ChatColor.RESET + ChatColor.GREEN
                                            + " sent to " + ChatColor.YELLOW + receiver.getName());
                                } else {
                                    player.sendMessage(Util.PREFIX + ChatColor.RED + "This player isn't online!");
                                }
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            player.sendMessage(Util.PREFIX + ChatColor.RED + "There isn't a location with this name!");
                            return true;
                        }
                    }
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
        if (command.getName().equalsIgnoreCase("loc")) {
            return Arrays.asList("add", "remove", "list", "share");
        }
        return null;
    }
}
