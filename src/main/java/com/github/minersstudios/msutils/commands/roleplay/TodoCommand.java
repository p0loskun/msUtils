package com.github.minersstudios.msutils.commands.roleplay;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.classes.PlayerInfo;
import com.github.minersstudios.msutils.utils.ChatUtils;
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
		if (player.getWorld() == Main.getWorldDark() || !Main.getAuthMeApi().isAuthenticated(player)) return true;
		String message = ChatUtils.extractMessage(0, args);
		if (args.length < 3 || !message.contains("*")) return false;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted()) {
			return ChatUtils.sendWarning(player, Component.text("Вы замьючены"));
		}
		String action = message.substring(message.indexOf('*') + 1).trim(),
				speech = message.substring(0 , message.indexOf('*')).trim();
		if (action.isEmpty() || speech.isEmpty()) return false;
		return ChatUtils.sendRPEventMessage(player, Component.text(speech), Component.text(action), ChatUtils.RolePlayActionType.TODO);
	}
}
