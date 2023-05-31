package com.github.minersstudios.msutils.commands.admin;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerFile;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.PlayerName;
import com.github.minersstudios.msutils.player.PlayerSettings;
import com.github.minersstudios.msutils.tabcompleters.AllPlayers;
import com.github.minersstudios.msutils.utils.IDUtils;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@MSCommand(
		command = "info",
		usage = " ꀑ §cИспользуй: /<command> [ID]",
		description = "Выводит информацию про игрока",
		permission = "msutils.info",
		permissionDefault = PermissionDefault.OP
)
public class InfoCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length == 0) return false;
		if (args[0].matches("-?\\d+")) {
			OfflinePlayer offlinePlayer = IDUtils.getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null || offlinePlayer.getName() == null) {
				ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
				return true;
			}
			sendInfo(MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), offlinePlayer.getName()), sender);
			return true;
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, "Кажется, что-то пошло не так...");
				return true;
			}
			sendInfo(MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), args[0]), sender);
			return true;
		}
		ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		return new AllPlayers().onTabComplete(sender, command, label, args);
	}

	private static void sendInfo(@NotNull PlayerInfo playerInfo, @NotNull CommandSender sender) {
		PlayerFile playerFile = playerInfo.getPlayerFile();
		PlayerName playerName = playerFile.getPlayerName();
		PlayerSettings playerSettings = playerFile.getPlayerSettings();
		Location
				lastLeaveLocation = playerFile.getLastLeaveLocation(),
				lastDeathLocation = playerFile.getLastDeathLocation();

		if (lastLeaveLocation == null) {
			lastLeaveLocation = new Location(MSUtils.getOverworld(), 0, 0, 0);
		}

		if (lastDeathLocation == null) {
			lastDeathLocation = new Location(MSUtils.getOverworld(), 0, 0, 0);
		}

		ChatUtils.sendInfo(sender,
				"UUID : " + playerInfo.getOfflinePlayer().getUniqueId()
				+ "\n ID : " + playerInfo.getID(false, false)
				+ "\n Nickname : " + playerName.getNickname()
				+ "\n Firstname : " + playerName.getFirstName()
				+ "\n Lastname : " + playerName.getLastName()
				+ "\n Patronymic : " + playerName.getPatronymic()
				+ "\n RP-type : " + playerSettings.getResourcePackType()
				+ "\n Muted : " + playerInfo.isMuted()
				+ "\n Banned : " + playerInfo.isBanned()
				+ "\n First join : " + playerFile.getFirstJoin()
				+ "\n Mute reason : " + playerInfo.getMuteReason()
				+ "\n Muted to : " + playerInfo.getMutedTo()
				+ "\n Ban reason : " + playerInfo.getBanReason()
				+ "\n Banned to : " + playerInfo.getBannedTo()
				+ "\n Last death world : " + lastDeathLocation.getWorld().getName()
				+ "\n Last death X : " + lastDeathLocation.getX()
				+ "\n Last death Y : " + lastDeathLocation.getY()
				+ "\n Last death Z : " + lastDeathLocation.getZ()
				+ "\n Last death Yaw : " + lastDeathLocation.getYaw()
				+ "\n Last death Pitch : " + lastDeathLocation.getPitch()
				+ "\n Last leave world : " + lastLeaveLocation.getWorld().getName()
				+ "\n Last leave X : " + lastLeaveLocation.getX()
				+ "\n Last leave Y : " + lastLeaveLocation.getY()
				+ "\n Last leave Z : " + lastLeaveLocation.getZ()
				+ "\n Last leave Yaw : " + lastLeaveLocation.getYaw()
				+ "\n Last leave Pitch : " + lastLeaveLocation.getPitch()
		);
	}
}
