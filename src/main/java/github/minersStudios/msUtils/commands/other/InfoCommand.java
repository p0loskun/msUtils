package github.minersStudios.msUtils.commands.other;

import github.minersStudios.msUtils.classes.PlayerID;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
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
        } else {
            if(!args[0].matches("[0-99]+")) return false;
            OfflinePlayer player = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
            if (player != null) {
                PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
                Location lastLeaveLocation = playerInfo.getLastLeaveLocation(),
                        lastDeathLocation = playerInfo.getLastDeathLocation();
                ChatUtils.sendFine(sender, "UUID : "
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
            } else {
                ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
            }
        }
        return true;
    }
}
