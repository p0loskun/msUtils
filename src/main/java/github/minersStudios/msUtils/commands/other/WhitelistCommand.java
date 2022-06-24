package github.minersStudios.msUtils.commands.other;

import github.minersStudios.msUtils.classes.PlayerID;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class WhitelistCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length < 1) {
            return false;
        } else {
            if(args[0].equalsIgnoreCase("reload")) {
                Bukkit.reloadWhitelist();
                return true;
            }
            if (args.length > 1 && args[1].matches("[0-99]+")) {
                if(args[0].equalsIgnoreCase("add")){
                    return ChatUtils.sendWarning(sender, "Для добавления игрока используйте ник!");
                } else if(args[0].equalsIgnoreCase("remove")){
                    OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[1]));
                    if (offlinePlayer == null) return ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
                    PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
                    if(PlayerUtils.removePlayerFromWhitelist(offlinePlayer, null)){
                        ChatUtils.sendFine(sender, "Игрок : \"" + playerInfo.getGrayIDGreenName() + " (" + playerInfo.getNickname() + ")\" был удалён из белого списка");
                    } else {
                        ChatUtils.sendWarning(sender, "Игрок : \"" + playerInfo.getGrayIDGoldName() + " (" + playerInfo.getNickname() + ")\" не состоит в белом списке");
                    }
                } else {
                    return false;
                }
            } else if(args.length > 1 && args[1].length() > 2) {
                OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[1]);
                if(offlinePlayer == null) return ChatUtils.sendError(sender, "Что-то пошло не так...");
                PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
                if(args[0].equalsIgnoreCase("add")) {
                    if (PlayerUtils.addPlayerToWhitelist(offlinePlayer, args[1])) {
                        ChatUtils.sendFine(sender, "Игрок : \"" + playerInfo.getGrayIDGreenName() + " (" + args[1] + ")\" был добавлен в белый список");
                    } else {
                        ChatUtils.sendWarning(sender, "Игрок : \"" + playerInfo.getGrayIDGoldName() + " (" + args[1] + ")\" уже состоит в белом списке");
                    }
                } else if(args[0].equalsIgnoreCase("remove")) {
                    if (PlayerUtils.removePlayerFromWhitelist(offlinePlayer, args[1])) {
                        ChatUtils.sendFine(sender, "Игрок : \"" + playerInfo.getGrayIDGreenName() + " (" + args[1] + ")\" был удалён из белого списка");
                    } else {
                        ChatUtils.sendWarning(sender, "Игрок : \"" + playerInfo.getGrayIDGoldName() + " (" + args[1] + ")\" не состоит в белом списке");
                    }
                } else {
                    return false;
                }
            } else {
                ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
            }
        }
        return true;
    }
}