package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.tabcompleters.AllPlayers;
import com.github.minersstudios.msutils.utils.IDUtils;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@MSCommand(
		command = "player",
		usage = " ꀑ §cИспользуй: /<command> [id/никнейм] [параметры]",
		description = "Команды, отвечающие за параметры игрока",
		permission = "msutils.player.*",
		permissionDefault = PermissionDefault.OP
)
public class PlayerCommandHandler implements MSCommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length == 0) return false;
		if (args[0].matches("-?\\d+")) {
			OfflinePlayer offlinePlayer = IDUtils.getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null || offlinePlayer.getName() == null) {
				ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
				return true;
			}
			return runCommand(sender, args, offlinePlayer);
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, "Кажется, что-то пошло не так...");
				return true;
			}
			return runCommand(sender, args, offlinePlayer);
		}
		ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		switch (args.length) {
			case 1 -> {
				return new AllPlayers().onTabComplete(sender, command, label, args);
			}
			case 2 -> {
				return List.of(
						"update",
						"info"
				);
			}
		}
		return new ArrayList<>();
	}

	private static boolean runCommand(@NotNull CommandSender sender, String @NotNull [] args, @NotNull OfflinePlayer offlinePlayer) {
		PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), Objects.requireNonNull(offlinePlayer.getName()));
		return switch (args[1]) {
			case "update" -> UpdatePlayerCommand.runCommand(sender, playerInfo);
			case "info" -> InfoCommand.runCommand(sender, playerInfo);
			default -> false;
		};
	}
}
