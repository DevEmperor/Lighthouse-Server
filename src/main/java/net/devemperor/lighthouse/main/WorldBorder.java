package net.devemperor.lighthouse.main;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.event.Listener;

public class WorldBorder implements Listener {

    public static int getBorderSize() {
        int total_size = 0;
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            total_size += ScoreboardWorld.cfg.getLong(player.getUniqueId() + "." + ".exp");
            total_size -= ScoreboardWorld.cfg.getInt(player.getUniqueId() + "." + ".lvl-lost") * 5;
        }
        return total_size / 5 + 15;
    }

    public static void updateWorldBorder(World world) {
        world.getWorldBorder().setCenter(world.getSpawnLocation());
        world.getWorldBorder().setSize(getBorderSize(), 3);

        world.getWorldBorder().setDamageBuffer(0);
        world.getWorldBorder().setDamageAmount(0.02);
    }
}
