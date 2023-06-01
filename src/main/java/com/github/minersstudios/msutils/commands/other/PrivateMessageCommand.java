package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.tabcompleters.AllLocalPlayers;
import com.github.minersstudios.msutils.utils.IDUtils;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.minersstudios.msutils.utils.MessageUtils.sendPrivateMessage;
import static net.kyori.adventure.text.Component.text;

@MSCommand(
		command = "privatemessage",
		aliases = {
				"pmessage",
				"pm",
				"w",
				"tell",
				"msg"
		},
		usage = " ꀑ §cИспользуй: /<command> [id/никнейм] [сообщение]",
		description = "Отправь другому игроку приватное сообщение"
)
public class PrivateMessageCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length < 2) return false;
		PlayerInfo senderInfo = sender instanceof Player player
				? MSPlayerUtils.getPlayerInfo(player)
				: MSUtils.consolePlayerInfo;
		if (senderInfo.isMuted()) {
			ChatUtils.sendWarning(sender, "Вы замьючены");
			return true;
		}
		String message = ChatUtils.extractMessage(args, 1);
		if (args[0].matches("-?\\d+")) {
			OfflinePlayer offlinePlayer = IDUtils.getPlayerByID(Integer.parseInt(args[0]));
			if (!(offlinePlayer instanceof Player player)) {
				ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
				return true;
			}
			PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(player);
			if (!playerInfo.isOnline()) {
				ChatUtils.sendWarning(sender, "Данный игрок не в сети");
				return true;
			}
			return sendPrivateMessage(senderInfo, playerInfo, text(message));
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (!(offlinePlayer instanceof Player player)) {
				ChatUtils.sendError(sender, "Кажется, что-то пошло не так...");
				return true;
			}
			PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(player);
			if (!playerInfo.isOnline()) {
				ChatUtils.sendWarning(sender, "Данный игрок не в сети");
				return true;
			}
			return sendPrivateMessage(senderInfo, playerInfo, text(message));
		}
		ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		return new AllLocalPlayers().onTabComplete(sender, command, label, args);
	}
}
