package github.minersStudios.msUtils.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Random;

public final class CommandUtils {

    @Nonnull public static String extractMessage(@Nonnull String[] args, int start) {
        return String.join(" ", Arrays.copyOfRange(args, start, args.length));
    }

    public static void sendMessage(Player player, int radius, String message) {
        if (radius > -1) {
            player.getWorld().getPlayers().stream().filter((p) -> player.getLocation().distanceSquared(p.getLocation()) <= Math.pow(radius, 2.0D)).forEach((p) -> p.sendMessage(message));
        } else {
            Bukkit.getOnlinePlayers().forEach((p) -> p.sendMessage(message));
        }
    }

    public static String randomResult() {
        return new String[] {ChatColor.GREEN + "Успешно", ChatColor.RED + "Неуспешно"}[new Random().nextInt(2)];
    }
}
