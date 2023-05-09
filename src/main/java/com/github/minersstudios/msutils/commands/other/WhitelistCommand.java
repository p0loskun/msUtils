package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerID;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@MSCommand(
		command = "whitelist",
		usage = " ꀑ §cИспользуй: /<command> [add/remove/reload] [ID/Nickname]",
		description = "Удаляет/добавляет игрока в вайтлист, или перезагружает его",
		permission = "msutils.whitelist",
		permissionDefault = PermissionDefault.OP
)
public class WhitelistCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length == 0) return false;
		if (args[0].equalsIgnoreCase("reload")) {
			Bukkit.reloadWhitelist();
			ChatUtils.sendFine(sender, Component.text("Вайт-Лист был перезагружен"));
			return true;
		}
		if (args.length > 1 && args[1].matches("-?\\d+")) {
			if (args[0].equalsIgnoreCase("add")) {
				ChatUtils.sendWarning(sender, Component.text("Для добавления игрока используйте ник!"));
				return true;
			}
			if (args[0].equalsIgnoreCase("remove")) {
				OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[1]));
				if (offlinePlayer == null) {
					ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
					return true;
				}
				PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
				if (offlinePlayer.getName() != null && PlayerUtils.removePlayerFromWhitelist(offlinePlayer, offlinePlayer.getName())) {
					ChatUtils.sendFine(sender,
							Component.text("Игрок : \"")
							.append(playerInfo.getGrayIDGreenName())
							.append(Component.text(" ("))
							.append(Component.text(offlinePlayer.getName()))
							.append(Component.text(")\" был удалён из белого списка"))
					);
					return true;
				}
				ChatUtils.sendWarning(sender,
						Component.text("Игрок : \"")
						.append(playerInfo.getGrayIDGoldName())
						.append(Component.text(" ("))
						.append(Component.text(offlinePlayer.getName()))
						.append(Component.text(")\" не состоит в белом списке"))
				);
				return true;
			}
			return false;
		}
		if (args.length > 1 && args[1].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[1]);
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
				return true;
			}
			PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
			if (args[0].equalsIgnoreCase("add")) {
				if (PlayerUtils.addPlayerToWhitelist(offlinePlayer, args[1])) {
					ChatUtils.sendFine(sender,
							Component.text("Игрок : \"")
							.append(playerInfo.getGrayIDGreenName())
							.append(Component.text(" ("))
							.append(Component.text(args[1]))
							.append(Component.text(")\" был добавлен в белый список"))
					);
					return true;
				}
				ChatUtils.sendWarning(sender,
						Component.text("Игрок : \"")
						.append(playerInfo.getGrayIDGoldName())
						.append(Component.text(" ("))
						.append(Component.text(args[1]))
						.append(Component.text(")\" уже состоит в белом списке"))
				);
				return true;
			}
			if (args[0].equalsIgnoreCase("remove")) {
				if (PlayerUtils.removePlayerFromWhitelist(offlinePlayer, args[1])) {
					ChatUtils.sendFine(sender,
							Component.text("Игрок : \"")
							.append(playerInfo.getGrayIDGreenName())
							.append(Component.text(" ("))
							.append(Component.text(args[1]))
							.append(Component.text(")\" был удалён из белого списка"))
					);
					return true;
				}
				ChatUtils.sendWarning(sender,
						Component.text("Игрок : \"")
						.append(playerInfo.getGrayIDGoldName())
						.append(Component.text(" ("))
						.append(Component.text(args[1]))
						.append(Component.text(")\" не состоит в белом списке"))
				);
				return true;
			}
			return false;
		}
		ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			completions.add("add");
			completions.add("remove");
			completions.add("reload");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
			for (OfflinePlayer offlinePlayer : Bukkit.getWhitelistedPlayers()) {
				PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
				int id = playerInfo.getID(false, false);
				if (id != -1) {
					completions.add(String.valueOf(id));
				}
				completions.add(offlinePlayer.getName());
			}
		}
		return completions;
	}
}
