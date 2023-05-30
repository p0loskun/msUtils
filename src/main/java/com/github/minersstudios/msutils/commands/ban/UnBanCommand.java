package com.github.minersstudios.msutils.commands.ban;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.IDUtils;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
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
import java.util.Objects;

@MSCommand(
		command = "unban",
		usage = " ꀑ §cИспользуй: /<command> [ID/Nickname]",
		description = "Разбанить игрока",
		permission = "msutils.ban",
		permissionDefault = PermissionDefault.OP
)
public class UnBanCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length == 0) return false;
		if (args[0].matches("-?\\d+")) {
			OfflinePlayer offlinePlayer = IDUtils.getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null || offlinePlayer.getName() == null) {
				ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
				return true;
			}
			PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), offlinePlayer.getName());
			if (!playerInfo.isBanned()) {
				ChatUtils.sendWarning(sender,
						Component.text("Игрок : \"")
						.append(playerInfo.createGrayIDGoldName())
						.append(Component.text("\" не забанен"))
				);
				return true;
			}
			playerInfo.setBanned(false, sender);
			return true;
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
				return true;
			}
			PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), args[0]);
			if (!playerInfo.isBanned()) {
				ChatUtils.sendWarning(sender,
						Component.text("Игрок : \"")
						.append(playerInfo.createGrayIDGoldName())
						.append(Component.text(" ("))
						.append(Component.text(args[0]))
						.append(Component.text(")\" не забанен"))
				);
				return true;
			}
			playerInfo.setBanned(false, sender);
			return true;
		}
		ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			for (OfflinePlayer offlinePlayer : Bukkit.getBannedPlayers()) {
				if (offlinePlayer != null) {
					PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), Objects.requireNonNull(offlinePlayer.getName()));
					int id = playerInfo.getID(false, false);
					if (id != -1) {
						completions.add(String.valueOf(id));
					}
					completions.add(offlinePlayer.getName());
				}
			}
			return completions;
		}
		return completions;
	}
}
