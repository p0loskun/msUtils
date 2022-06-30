package github.minersStudios.msUtils.commands.other;

import github.minersStudios.msUtils.classes.PlayerID;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class InfoCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (args.length < 1) {
			return false;
		}
		if (args[0].matches("[0-99]+")) {
			OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null)
				return ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
			PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
			return sendInfo(playerInfo, sender);
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null)
				return ChatUtils.sendError(sender, "Что-то пошло не так...");
			PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
			return sendInfo(playerInfo, sender);
		}
		return ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
	}

	private static boolean sendInfo(@Nonnull PlayerInfo playerInfo, @Nonnull CommandSender sender){
		Location lastLeaveLocation = playerInfo.getLastLeaveLocation(),
				lastDeathLocation = playerInfo.getLastDeathLocation();
		return ChatUtils.sendFine(sender, "UUID : "
				+ playerInfo.getUuid() + "\nID : "
				+ playerInfo.getID() + "\nNickname : "
				+ playerInfo.getNickname() + "\nFirstname : "
				+ playerInfo.getFirstname() + "\nLastname : "
				+ playerInfo.getLastname() + "\nPatronymic : "
				+ playerInfo.getPatronymic() + "\nRPtype : "
				+ playerInfo.getResourcePackType() + "\nMuted : "
				+ playerInfo.isMuted() + "\nBanned : "
				+ playerInfo.isBanned() + "\nFirst join : "
				+ playerInfo.getFirstJoin() + "\nMute reason : "
				+ playerInfo.getMuteReason() + "\nMuted to : "
				+ playerInfo.getMutedTo() + "\nBan reason : "
				+ playerInfo.getBanReason() + "\nBanned to : "
				+ playerInfo.getBannedTo() + "\nLast death world : "
				+ lastDeathLocation.getBlock().getWorld().getName() + "\nLast death X : "
				+ lastDeathLocation.getX() + "\nLast death Y : "
				+ lastDeathLocation.getY() + "\nLast death Z : "
				+ lastDeathLocation.getZ() + "\nLast death Yaw : "
				+ lastDeathLocation.getYaw() + "\nLast death Pitch : "
				+ lastDeathLocation.getPitch() + "\nLast leave world : "
				+ lastLeaveLocation.getBlock().getWorld().getName() + "\nLast leave X : "
				+ lastLeaveLocation.getX() + "\nLast leave Y : "
				+ lastLeaveLocation.getY() + "\nLast leave Z : "
				+ lastLeaveLocation.getZ() + "\nLast leave Yaw : "
				+ lastLeaveLocation.getYaw() + "\nLast leave Pitch : "
				+ lastLeaveLocation.getPitch()
		);
	}
}
