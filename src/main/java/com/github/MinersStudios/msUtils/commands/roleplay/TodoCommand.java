package com.github.MinersStudios.msUtils.commands.roleplay;

import com.github.MinersStudios.msUtils.Main;
import com.github.MinersStudios.msUtils.classes.PlayerInfo;
import com.github.MinersStudios.msUtils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class TodoCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		}
		if (player.getWorld() == Main.getWorldDark() || !Main.getAuthmeApi().isAuthenticated(player)) return true;
		String message = ChatUtils.extractMessage(args, 0);
		if (args.length < 3 || !message.contains("*")) return false;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted()) {
			return ChatUtils.sendWarning(player, Component.text("Вы замучены"));
		}
		String action = message.substring(message.indexOf('*') + 1).trim(),
				speech = message.substring(0 , message.indexOf('*')).trim();
		if (action.equalsIgnoreCase("") || speech.equalsIgnoreCase("")) return false;
		return ChatUtils.sendRPEventMessage(player, Component.text(speech), Component.text(action));
	}
}
