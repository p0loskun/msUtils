package github.minersStudios.msUtils.commands.mute;

import github.minersStudios.msUtils.classes.PlayerID;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.CommandUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.Date;

public class MuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length < 2) {
            return false;
        } else {
            if (!args[0].matches("[0-99]+") || !args[1].matches("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)")) return false;
            long time = (long) (Float.parseFloat(args[1]) * 86400000 + System.currentTimeMillis());
            String reason = args.length > 2 ? CommandUtils.extractMessage(args, 2) : "неизвестно";
            OfflinePlayer player = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
            if (player != null) {
                PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
                if(!playerInfo.isBanned()){
                    playerInfo.setMuted(true, time, reason);
                    ChatUtils.sendFine(sender, "Игрок : \"" + ChatColor.GRAY + "[" + playerInfo.getID() + "] " + ChatColor.GREEN + playerInfo.getFirstname() + " " + playerInfo.getLastname() + "\" был замучен : " + "\n    - Причина : \"" + reason + "\"\n    - До : " + new Date(time));
                } else {
                    ChatUtils.sendWarning(sender, "Игрок : \"" + ChatColor.GRAY + "[" + playerInfo.getID() + "] " + ChatColor.GOLD + playerInfo.getFirstname() + " " + playerInfo.getLastname() + "\" уже замучен");
                }
            } else {
                ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
            }
        }
        return true;
    }

}