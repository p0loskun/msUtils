package github.minersStudios.msUtils.commands.other;

import github.minersStudios.msUtils.classes.PlayerID;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class PrivateMessageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtils.sendError(sender, "Только игрок может использовать эту команду!");
            return true;
        } else {
            if (args.length < 2) {
                return false;
            } else {
                PlayerInfo privateMessageSender = new PlayerInfo(player.getUniqueId());
                if (privateMessageSender.isMuted()) {
                    ChatUtils.sendWarning(player, "Вы замучены");
                    return true;
                }
                String message = ChatUtils.extractMessage(args, 1);
                if (args[0].matches("[0-99]+")) {
                    OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
                    if (offlinePlayer != null) {
                        PlayerInfo privateMessageReceiver = new PlayerInfo(offlinePlayer.getUniqueId());
                        ChatUtils.sendPrivateMessage(privateMessageSender, privateMessageReceiver, message);
                    } else {
                        ChatUtils.sendError(player, "Вы ошиблись айди, игрока привязанного к нему не существует");
                    }
                } else if (args[0].length() > 2) {
                    OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
                    if (offlinePlayer == null) {
                        ChatUtils.sendError(player, "Что-то пошло не так...");
                        return true;
                    }
                    PlayerInfo privateMessageReceiver = new PlayerInfo(offlinePlayer.getUniqueId());
                    ChatUtils.sendPrivateMessage(privateMessageSender, privateMessageReceiver, message);
                } else {
                    ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
                }
            }
        }
        return true;
    }
}
