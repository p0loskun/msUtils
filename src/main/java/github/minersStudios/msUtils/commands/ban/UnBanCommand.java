package github.minersStudios.msUtils.commands.ban;

import github.minersStudios.msUtils.classes.PlayerID;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class UnBanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length < 1) {
            return false;
        } else {
            if (!args[0].matches("[0-99]+")) return false;
            OfflinePlayer player = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
            if (player != null) {
                PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
                if(playerInfo.isBanned()){
                    playerInfo.setBanned(false, 0, null, null);
                    ChatUtils.sendFine(sender, "Игрок : \"" + ChatColor.GRAY + "[" + playerInfo.getID() + "] " + ChatColor.GREEN + playerInfo.getFirstname() + " " + playerInfo.getLastname() + "\" был разбанен");
                } else {
                    ChatUtils.sendWarning(sender, "Игрок : \"" + ChatColor.GRAY + "[" + playerInfo.getID() + "] " + ChatColor.GOLD + playerInfo.getFirstname() + " " + playerInfo.getLastname() + "\" не забанен");
                }
            } else {
                ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
            }
        }
        return true;
    }

}