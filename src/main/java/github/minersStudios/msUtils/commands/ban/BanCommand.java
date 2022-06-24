package github.minersStudios.msUtils.commands.ban;

import github.minersStudios.msUtils.classes.PlayerID;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.Date;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length < 2) {
            return false;
        } else {
            if (!args[1].matches("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)")) return false;
            long time = (long) (Float.parseFloat(args[1]) * 86400000 + System.currentTimeMillis());
            String reason = args.length > 2 ? ChatUtils.extractMessage(args, 2) : "неизвестно";
            if (args[0].matches("[0-99]+") ) {
                OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
                if (offlinePlayer == null) return ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
                PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
                if (!playerInfo.isBanned()) {
                    if (playerInfo.setBanned(true, time, reason, sender.getName())) {
                        return ChatUtils.sendFine(sender, "Игрок : \"" + playerInfo.getGrayIDGreenName() + "\" был забанен : " + "\n    - Причина : \"" + reason + "\"\n    - До : " + new Date(time));
                    }
                    return ChatUtils.sendWarning(sender, "Игрок : \"" + playerInfo.getGrayIDGoldName() + "\" ещё ни разу не играл на сервере, используйте пожалуйста никнем");
                }
                ChatUtils.sendWarning(sender, "Игрок : \"" + playerInfo.getGrayIDGoldName() + "\" уже забанен");
            } else if(args[0].length() > 2) {
                OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
                if (offlinePlayer == null) return ChatUtils.sendError(sender, "Что-то пошло не так...");
                PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId(), args[0]);
                if (!playerInfo.isBanned()) {
                    playerInfo.setBanned(true, time, reason, sender.getName());
                    ChatUtils.sendFine(sender, "Игрок : \"" + playerInfo.getGrayIDGreenName() + " (" + args[0] + ")\" был забанен : " + "\n    - Причина : \"" + reason + "\"\n    - До : " + new Date(time));
                } else {
                    ChatUtils.sendWarning(sender, "Игрок : \"" + playerInfo.getGrayIDGoldName() + " (" + args[0] + ")\" уже забанен");
                }
            } else {
                ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
            }
        }
        return true;
    }
}