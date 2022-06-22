package net.devemperor.lighthouse.misc;

import net.devemperor.lighthouse.main.Main;
import net.devemperor.lighthouse.util.Util;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Compass implements CommandExecutor, Listener {

    private final Main plugin;
    private int id;
    File tracking = new File("plugins/Lighthouse", "tracking.yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(tracking);

    public Compass(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("whereis")) {
                if (args.length == 1) {
                    Player dest = Bukkit.getPlayerExact(args[0]);
                    if (dest == null) {
                        sender.sendMessage(Util.PREFIX + ChatColor.RED + "This player isn't online!");
                    } else if (dest.getName().equals(player.getName())) {
                        sender.sendMessage(Util.PREFIX + ChatColor.GREEN + "You are in the " + ChatColor.YELLOW + player.getWorld().getBiome(player.getLocation().getBlockX(),
                                player.getLocation().getBlockY(), player.getLocation().getBlockZ()).toString().toLowerCase());
                    } else {
                        ItemStack compass = new ItemStack(Material.COMPASS);
                        ItemMeta meta = compass.getItemMeta();
                        meta.setDisplayName("§bTracker for §e" + dest.getName());
                        compass.setItemMeta(meta);
                        if (player.getInventory().firstEmpty() == -1) {
                            sender.sendMessage(Util.PREFIX + ChatColor.RED + "You need an empty slot in your inventory to receive the tracker!");
                        } else if (cfg.getBoolean(player.getUniqueId() + "." + ".tracking")) {
                            sender.sendMessage(Util.PREFIX + ChatColor.RED + "You need to cancel your first tracking to start a new one!");
                        } else {
                            cfg.set(player.getUniqueId() + "." + ".tracking", true);
                            try {
                                cfg.save(tracking);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.getInventory().remove(Material.COMPASS);
                            player.getInventory().addItem(compass);
                            sender.sendMessage(Util.PREFIX + ChatColor.GREEN + "You received your tracker for " + ChatColor.GOLD + dest.getName());
                            id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                                player.setCompassTarget(dest.getLocation());
                                double distance = player.getLocation().distance(dest.getLocation());
                                if (player.getInventory().getItemInMainHand().equals(compass)) {
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§b" + (int) distance + " §eblocks"));
                                }
                                if (!dest.isOnline()) {
                                    player.sendMessage(Util.PREFIX + ChatColor.RED + "Your target has left!");
                                    stop(id, player);
                                } else if (!player.getWorld().getName().equals(dest.getWorld().getName())) {
                                    player.sendMessage(Util.PREFIX + ChatColor.RED + "Your target is in another world!");
                                    stop(id, player);
                                } else if ((int) distance <= 5) {
                                    player.sendMessage(Util.PREFIX + ChatColor.GREEN + "Yay! You found your friend!");
                                    stop(id, player);
                                } else if (!player.getInventory().contains(compass) && !player.getItemOnCursor().equals(compass)) {
                                    player.sendMessage(Util.PREFIX + ChatColor.RED + "Tracking canceled!");
                                    stop(id, player);
                                }
                            }, 10, 10);
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

    private void stop(int id, Player player) {
        Bukkit.getScheduler().cancelTask(id);
        try {
            player.setCompassTarget(Objects.requireNonNull(player.getBedSpawnLocation()));
        } catch (NullPointerException e) { // if player hasn't slept yet
            player.setCompassTarget(Objects.requireNonNull(player.getWorld().getSpawnLocation()));
        }
        player.getInventory().remove(Material.COMPASS);
        cfg.set(player.getUniqueId() + "." + ".tracking", false);
        try {
            cfg.save(tracking);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
