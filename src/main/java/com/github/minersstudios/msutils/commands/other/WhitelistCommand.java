package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.msutils.player.PlayerID;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WhitelistCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length == 0) return false;
		if (args[0].equalsIgnoreCase("reload")) {
			Bukkit.reloadWhitelist();
			return ChatUtils.sendFine(sender, Component.text("Вайт-Лист был перезагружен"));
		}
		if (args.length > 1 && args[1].matches("[0-99]+")) {
			if (args[0].equalsIgnoreCase("add")) {
				return ChatUtils.sendWarning(sender, Component.text("Для добавления игрока используйте ник!"));
			}
			if (args[0].equalsIgnoreCase("remove")) {
				OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[1]));
				if (offlinePlayer == null) {
					return ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
				}
				PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
				if (offlinePlayer.getName() != null && PlayerUtils.removePlayerFromWhitelist(offlinePlayer, offlinePlayer.getName())) {
					return ChatUtils.sendFine(sender,
							Component.text("Игрок : \"")
									.append(playerInfo.getGrayIDGreenName())
									.append(Component.text(" ("))
									.append(Component.text(offlinePlayer.getName()))
									.append(Component.text(")\" был удалён из белого списка"))
					);
				}
				return ChatUtils.sendWarning(sender,
						Component.text("Игрок : \"")
								.append(playerInfo.getGrayIDGoldName())
								.append(Component.text(" ("))
								.append(Component.text(offlinePlayer.getName()))
								.append(Component.text(")\" не состоит в белом списке"))
				);
			}
			return false;
		}
		if (args.length > 1 && args[1].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[1]);
			if (offlinePlayer == null) {
				return ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
			}
			PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
			if (args[0].equalsIgnoreCase("add")) {
				if (PlayerUtils.addPlayerToWhitelist(offlinePlayer, args[1])) {
					return ChatUtils.sendFine(sender,
							Component.text("Игрок : \"")
									.append(playerInfo.getGrayIDGreenName())
									.append(Component.text(" ("))
									.append(Component.text(args[1]))
									.append(Component.text(")\" был добавлен в белый список"))
					);
				}
				return ChatUtils.sendWarning(sender,
						Component.text("Игрок : \"")
								.append(playerInfo.getGrayIDGoldName())
								.append(Component.text(" ("))
								.append(Component.text(args[1]))
								.append(Component.text(")\" уже состоит в белом списке"))
				);
			}
			if (args[0].equalsIgnoreCase("remove")) {
				if (PlayerUtils.removePlayerFromWhitelist(offlinePlayer, args[1])) {
					return ChatUtils.sendFine(sender,
							Component.text("Игрок : \"")
									.append(playerInfo.getGrayIDGreenName())
									.append(Component.text(" ("))
									.append(Component.text(args[1]))
									.append(Component.text(")\" был удалён из белого списка"))
					);
				}
				return ChatUtils.sendWarning(sender,
						Component.text("Игрок : \"")
								.append(playerInfo.getGrayIDGoldName())
								.append(Component.text(" ("))
								.append(Component.text(args[1]))
								.append(Component.text(")\" не состоит в белом списке"))
				);
			}
			return false;
		}
		return ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
	}
}
