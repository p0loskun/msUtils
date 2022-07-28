package github.minersStudios.msUtils.commands.roleplay;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.SitPlayer;
import github.minersStudios.msUtils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player))
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		if (player.getWorld() == Main.worldDark || !Main.authmeApi.isAuthenticated(player))
			return true;
		SitPlayer sitPlayer = new SitPlayer(player);
		if (!player.getLocation().subtract(0.0d, 0.2d, 0.0d).getBlock().getType().isSolid())
			return ChatUtils.sendWarning(player, Component.text("Сидеть в воздухе нельзя!"));
		return sitPlayer.setSitting(sitPlayer.isSitting() ? null : player.getLocation());
	}
}