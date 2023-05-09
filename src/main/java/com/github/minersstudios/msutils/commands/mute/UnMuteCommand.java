package com.github.minersstudios.msutils.commands.mute;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerID;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.tabcompleters.AllPlayers;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@MSCommand(
		command = "unmute",
		usage = " ꀑ §cИспользуй: /<command> [ID/Nickname]",
		description = "Размьютить игрока",
		permission = "msutils.mute",
		permissionDefault = PermissionDefault.OP
)
public class UnMuteCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length == 0) return false;
		if (args[0].matches("-?\\d+")) {
			OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
				return true;
			}
			PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
			if (!playerInfo.isMuted()) {
				ChatUtils.sendWarning(sender,
						Component.text("Игрок : \"")
						.append(playerInfo.getGrayIDGoldName())
						.append(Component.text("\" не замьючен"))
				);
				return true;
			}
			playerInfo.setMuted(false, sender);
			return true;
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
				return true;
			}
			PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId(), args[0]);
			if (!playerInfo.isMuted()) {
				ChatUtils.sendWarning(sender,
						Component.text("Игрок : \"")
						.append(playerInfo.getGrayIDGoldName())
						.append(Component.text(" ("))
						.append(Component.text(args[0]))
						.append(Component.text(")\" не замьючен"))
				);
				return true;
			}
			playerInfo.setMuted(false, sender);
			return true;
		}
		ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		return new AllPlayers().onTabComplete(sender, command, label, args);
	}
}
