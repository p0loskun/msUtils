package com.github.MinersStudios.msUtils.commands.other;

import com.github.MinersStudios.msUtils.classes.PlayerID;
import com.github.MinersStudios.msUtils.utils.ChatUtils;
import com.github.MinersStudios.msUtils.classes.PlayerInfo;
import com.github.MinersStudios.msUtils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class InfoCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (args.length < 1) return false;
		if (args[0].matches("[0-99]+")) {
			OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null) {
				return ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
			}
			return sendInfo(new PlayerInfo(offlinePlayer.getUniqueId()), sender);
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				return ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
			}
			return sendInfo(new PlayerInfo(offlinePlayer.getUniqueId()), sender);
		}
		return ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
	}

	private static boolean sendInfo(@Nonnull PlayerInfo playerInfo, @Nonnull CommandSender sender) {
		Location
				lastLeaveLocation = playerInfo.getLastLeaveLocation(),
				lastDeathLocation = playerInfo.getLastDeathLocation();
		return ChatUtils.sendInfo(sender,
				Component.text("UUID : " + playerInfo.getUuid()
						+ "\n ID : " + playerInfo.getID(false, false)
						+ "\n Nickname : " + playerInfo.getNickname()
						+ "\n Firstname : " + playerInfo.getFirstname()
						+ "\n Lastname : " + playerInfo.getLastname()
						+ "\n Patronymic : " + playerInfo.getPatronymic()
						+ "\n RPtype : " + playerInfo.getResourcePackType()
						+ "\n Muted : " + playerInfo.isMuted()
						+ "\n Banned : " + playerInfo.isBanned()
						+ "\n First join : " + playerInfo.getFirstJoin()
						+ "\n Mute reason : " + playerInfo.getMuteReason()
						+ "\n Muted to : " + playerInfo.getMutedTo()
						+ "\n Ban reason : " + playerInfo.getBanReason()
						+ "\n Banned to : " + playerInfo.getBannedTo()
						+ "\n Last death world : " + lastDeathLocation.getBlock().getWorld().getName()
						+ "\n Last death X : " + lastDeathLocation.getX()
						+ "\n Last death Y : " + lastDeathLocation.getY()
						+ "\n Last death Z : " + lastDeathLocation.getZ()
						+ "\n Last death Yaw : " + lastDeathLocation.getYaw()
						+ "\n Last death Pitch : " + lastDeathLocation.getPitch()
						+ "\n Last leave world : " + lastLeaveLocation.getBlock().getWorld().getName()
						+ "\n Last leave X : " + lastLeaveLocation.getX()
						+ "\n Last leave Y : " + lastLeaveLocation.getY()
						+ "\n Last leave Z : " + lastLeaveLocation.getZ()
						+ "\n Last leave Yaw : " + lastLeaveLocation.getYaw()
						+ "\n Last leave Pitch : " + lastLeaveLocation.getPitch())
		);
	}
}
