package com.github.MinersStudios.msUtils.commands.roleplay;

import com.github.MinersStudios.msUtils.Main;
import com.github.MinersStudios.msUtils.utils.ChatUtils;
import com.github.MinersStudios.msUtils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		}
		if (player.getWorld() == Main.getWorldDark() || !Main.getAuthmeApi().isAuthenticated(player)) return true;
		if (!player.getLocation().subtract(0.0d, 0.2d, 0.0d).getBlock().getType().isSolid()) {
			return ChatUtils.sendWarning(player, Component.text("Сидеть в воздухе нельзя!"));
		}
		return PlayerUtils.setSitting(player, PlayerUtils.getSeats().containsKey(player) ? null : player.getLocation(), args.length > 0 ? args : null);
	}
}
