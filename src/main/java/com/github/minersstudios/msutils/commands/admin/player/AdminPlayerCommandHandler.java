package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.CommandUtils;
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
import java.util.Locale;
import java.util.Objects;

@MSCommand(
		command = "player",
		usage = " ꀑ §cИспользуй: /<command> [id/никнейм] [параметры]",
		description = "Команды, отвечающие за параметры игрока",
		permission = "msutils.player.*",
		permissionDefault = PermissionDefault.OP
)
public class AdminPlayerCommandHandler implements MSCommandExecutor {
	@Override
	public boolean onCommand(
			@NotNull CommandSender sender,
			@NotNull Command command,
			@NotNull String label,
			String @NotNull ... args
	) {
		if (args.length < 2) return false;
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
	public @Nullable List<String> onTabComplete(
			@NotNull CommandSender sender,
			@NotNull Command command,
			@NotNull String label,
			String @NotNull ... args
	) {
		switch (args.length) {
			case 1 -> {
				return new AllPlayers().onTabComplete(sender, command, label, args);
			}
			case 2 -> {
				return List.of(
						"update",
						"info",
						"first-join",
						"pronouns",
						"game-params",
						"settings",
						"ban-info",
						"mute-info"
				);
			}
			case 3 -> {
				switch (args[1].toLowerCase(Locale.ENGLISH)) {
					case "pronouns" -> {
						return List.of(
								"he",
								"she",
								"they"
						);
					}
					case "game-params" -> {
						return List.of(
								"game-mode",
								"health",
								"air"
						);
					}
					case "settings" -> {
						return List.of(
								"resourcepack-type"
						);
					}
					case "ban-info", "mute-info" -> {
						return List.of(
								"reason",
								"time"
						);
					}
				}
			}
			case 4 -> {
				switch (args[1].toLowerCase(Locale.ENGLISH)) {
					case "game-params" -> {
						switch (args[2].toLowerCase(Locale.ENGLISH)) {
							case "game-mode" -> {
								return List.of(
										"survival",
										"creative",
										"spectator",
										"adventure"
								);
							}
							case "air" -> {
								return List.of(
										"0",
										"300"
								);
							}
							case "health" -> {
								return List.of(
										"0.0",
										"20.0"
								);
							}
						}
					}
					case "settings" -> {
						switch (args[2].toLowerCase(Locale.ENGLISH)) {
							case "resourcepack-type" -> {
								return List.of(
										"full",
										"lite",
										"none",
										"null"
								);
							}
						}
					}
					case "ban-info", "mute-info" -> {
						switch (args[2].toLowerCase(Locale.ENGLISH)) {
							case "time" -> {
								return CommandUtils.getTimeSuggestions(args[3]);
							}
							case "reason" -> {
								return List.of("неизвестно");
							}
						}
					}
				}
			}
		}
		return new ArrayList<>();
	}

	private static boolean runCommand(
			@NotNull CommandSender sender,
			String @NotNull [] args,
			@NotNull OfflinePlayer offlinePlayer
	) {
		PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), Objects.requireNonNull(offlinePlayer.getName()));
		return switch (args[1].toLowerCase(Locale.ENGLISH)) {
			case "update" -> AdminUpdateCommand.runCommand(sender, playerInfo);
			case "info" -> AdminInfoCommand.runCommand(sender, playerInfo);
			case "pronouns" -> AdminPronounsCommand.runCommand(sender, args, playerInfo);
			case "game-params" -> AdminGameParamsCommand.runCommand(sender, args, offlinePlayer, playerInfo);
			case "first-join" -> AdminFirstJoinCommand.runCommand(sender, playerInfo);
			case "settings" -> AdminSettingsCommand.runCommand(sender, args, playerInfo);
			case "ban-info" -> AdminBanInfoCommand.runCommand(sender, args, playerInfo);
			case "mute-info" -> AdminMuteInfoCommand.runCommand(sender, args, playerInfo);
			default -> false;
		};
	}
}
