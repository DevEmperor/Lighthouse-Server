package net.devemperor.lighthouse.main;

import net.devemperor.lighthouse.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Chest.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EventListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.sendTitle(ChatColor.GREEN + "Welcome to the " + ChatColor.GOLD + "Lighthouse-Server!",
                ChatColor.RED + "Survival, PvP and more", 20, 20*4, 20);

        player.sendMessage(Util.PREFIX + ChatColor.GREEN + "Available extra commands for this server are:");
        player.sendMessage(ChatColor.YELLOW + "           /sethome " + ChatColor.GREEN + "Set your home location");
        player.sendMessage(ChatColor.YELLOW + "           /home [cancel] " + ChatColor.GREEN + "Teleport to your home location");
        player.sendMessage(ChatColor.YELLOW + "           /loc " + ChatColor.GREEN + "Store and share locations");
        player.sendMessage(ChatColor.YELLOW + "           /reward " + ChatColor.GREEN + "Request some daily experience");
        player.sendMessage(ChatColor.YELLOW + "           /whereis <player> " + ChatColor.GREEN + "Get a compass that leads you to <player>");
        player.sendMessage(ChatColor.YELLOW + "           /donate <player> <amount> " + ChatColor.GREEN + "Give experience to a player");
        player.sendMessage(ChatColor.YELLOW + "           /glow <player> " + ChatColor.GREEN + "Make <player> visible");
        e.setJoinMessage(Util.PREFIX + ChatColor.GREEN + player.getName() + " has joined...");

        WorldBorder.updateWorldBorder(player.getWorld());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(Util.PREFIX + ChatColor.RED + player.getName() + " has left...");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(Util.PREFIX + ChatColor.DARK_RED + e.getEntity().getName() + " has died! R.I.P.");
        Player player = e.getEntity();

        ScoreboardWorld.cfg.set(player.getUniqueId() + "." + ".deaths", ScoreboardWorld.cfg.getInt(player.getUniqueId() + "." + ".deaths") + player.getLevel());

        List <ItemStack> inv = e.getDrops();

        Location next = player.getLocation();
        next.setX(next.getX() + 1);
        Block firstBlock = player.getLocation().getBlock();
        Block secondBlock = next.getBlock();

        firstBlock.setType(Material.CHEST);
        Chest firstBlockState = (Chest) firstBlock.getBlockData();
        firstBlockState.setType(Type.LEFT);
        firstBlock.setBlockData(firstBlockState, true);
        secondBlock.setType(Material.CHEST);
        Chest secondBlockState = (Chest) secondBlock.getBlockData();
        secondBlockState.setType(Type.RIGHT);
        secondBlock.setBlockData(secondBlockState, true);

        org.bukkit.block.Chest chest = (org.bukkit.block.Chest) firstBlock.getState();
        DoubleChest dc = (DoubleChest) chest.getInventory().getHolder();
        for (ItemStack itemStack : inv) {
            if (itemStack != null) {
                assert dc != null;
                dc.getInventory().addItem(itemStack);
            }
        }
        player.sendMessage(Util.PREFIX + ChatColor.GREEN + "Your inventory has been saved to "
                + "X = " + ChatColor.DARK_AQUA + next.getBlockX() + ChatColor.GREEN + ", Y = " + ChatColor.DARK_AQUA + next.getBlockY()
                + ChatColor.GREEN + ", Z = " + ChatColor.DARK_AQUA + next.getBlockZ() + ChatColor.GREEN + " !");
        e.getDrops().clear();
        e.setDroppedExp(0);
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String msg = e.getMessage();
        e.setFormat(ChatColor.GOLD + player.getName() + " [" + String.format("%,d", ScoreboardWorld.cfg.getLong(player.getUniqueId() + "." + ".exp"))
                + "] " + ChatColor.RED + Util.round(player.getHealth() / 2, 1) + "❤" + ChatColor.AQUA + " >> " + ChatColor.WHITE + msg);
    }

    @EventHandler
    public void onWorldTeleport(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        WorldBorder.updateWorldBorder(player.getWorld());
    }
}
