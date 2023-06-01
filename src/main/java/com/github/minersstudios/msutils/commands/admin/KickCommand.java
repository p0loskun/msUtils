package com.github.minersstudios.msutils.commands.admin;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.tabcompleters.AllLocalPlayers;
import com.github.minersstudios.msutils.utils.IDUtils;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

@MSCommand(
		command = "kick",
		usage = " ꀑ §cИспользуй: /<command> [id/никнейм] [причина]",
		description = "Кикнуть игрока",
		permission = "msutils.kick",
		permissionDefault = PermissionDefault.OP
)
public class KickCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length == 0) return false;
		String reason = args.length > 1
				? ChatUtils.extractMessage(args, 1)
				: "неизвестно";
		if (args[0].matches("-?\\d+")) {
			OfflinePlayer offlinePlayer = IDUtils.getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null || offlinePlayer.getName() == null) {
				ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
				return true;
			}
			PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), offlinePlayer.getName());
			if (playerInfo.kickPlayer("Вы были кикнуты", reason)) {
				ChatUtils.sendFine(sender,
						text("Игрок : \"")
						.append(playerInfo.createGrayIDGreenName())
						.append(text("\" был кикнут :\n    - Причина : \""))
						.append(text(reason))
				);
				return true;
			}
			ChatUtils.sendWarning(sender,
					text("Игрок : \"")
					.append(playerInfo.createGrayIDGoldName())
					.append(text("\" не в сети!"))
			);
			return true;
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, "Кажется, что-то пошло не так...");
				return true;
			}
			PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), args[0]);
			if (playerInfo.kickPlayer("Вы были кикнуты", reason)) {
				ChatUtils.sendFine(sender,
						text("Игрок : \"")
						.append(playerInfo.createGrayIDGreenName())
						.append(text(" ("))
						.append(text(args[0]))
						.append(text(")\" был кикнут :\n    - Причина : \""))
						.append(text(reason))
				);
				return true;
			}
			ChatUtils.sendWarning(sender,
					text("Игрок : \"")
					.append(playerInfo.createGrayIDGoldName())
					.append(text(" ("))
					.append(text(args[0]))
					.append(text(")\" не в сети!"))
			);
			return true;
		}
		ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		return new AllLocalPlayers().onTabComplete(sender, command, label, args);
	}
}