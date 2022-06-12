package net.devemperor.lighthouse.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.ChatColor;

public class Util {

    public static final String CMD_ONLY_PLAYER = "This command can only be run by a player!";
    public static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.GOLD + "Server" + ChatColor.GRAY + "] ";

    public static double round(double value, int places) {
        if (places < 0) { throw new IllegalArgumentException(); }

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double getLevelFromExp(long exp) {
        if (exp > 1395) {
            return (Math.sqrt(72 * exp - 54215) + 325) / 18;
        }
        if (exp > 315) {
            return Math.sqrt(40 * exp - 7839) / 10 + 8.1;
        }
        if (exp > 0) {
            return Math.sqrt(exp + 9) - 3;
        }
        return 0;
    }
}
