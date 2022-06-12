package github.minersStudios.msUtils.commands.roleplay;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.SitPlayer;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtils.sendError(sender, "Только игрок может использовать эту команду!");
            return true;
        } else {
            if (player.getWorld() == Main.worldDark || !Main.authmeApi.isAuthenticated(player)) return true;
            SitPlayer sitPlayer = new SitPlayer(player);
            if (sitPlayer.isSitting()) {
                sitPlayer.setSitting(false);
            } else if (!player.getLocation().subtract(0.0d, 0.2d, 0.0d).getBlock().getType().isSolid()) {
                ChatUtils.sendWarning(player, "Сидеть в воздухе нельзя!");
                return true;
            } else {
                sitPlayer.setSitting(true);
            }
        }
        return true;
    }
}
