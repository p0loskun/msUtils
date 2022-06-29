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
		assert (args.length > 1);
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, "Только игрок может использовать эту команду!");
		}
		PlayerInfo privateMessageSender = new PlayerInfo(player.getUniqueId());
		if (privateMessageSender.isMuted()) return ChatUtils.sendWarning(player, "Вы замьючены");	// Замучався бідний(( не уютьнінька йому
		String message = ChatUtils.extractMessage(args, 1);
		if (args[0].matches("[0-99]+")) {
			OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null) return ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
			if (offlinePlayer.getPlayer() == null) return ChatUtils.sendWarning(player, "Данный игрок не в сети");
			ChatUtils.sendPrivateMessage(privateMessageSender, new PlayerInfo(offlinePlayer.getUniqueId()), message);
		} if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) return ChatUtils.sendError(player, "Что-то пошло не так...");
			if (offlinePlayer.getPlayer() == null) return ChatUtils.sendWarning(player, "Данный игрок не в сети");
			return ChatUtils.sendPrivateMessage(privateMessageSender, new PlayerInfo(offlinePlayer.getUniqueId()), message);
		}
		return ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
	}
}
