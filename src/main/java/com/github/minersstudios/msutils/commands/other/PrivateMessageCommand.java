package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerID;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.tabcompleters.AllLocalPlayers;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.minersstudios.msutils.utils.ChatUtils.sendPrivateMessage;

@MSCommand(command = "privatemessage")
public class PrivateMessageCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length < 2) return false;
		PlayerInfo senderInfo = sender instanceof Player player
				? new PlayerInfo(player.getUniqueId())
				: MSUtils.CONSOLE_PLAYER_INFO;
		if (senderInfo.isMuted()) {
			return ChatUtils.sendWarning(sender, Component.text("Вы замьючены"));
		}
		String message = ChatUtils.extractMessage(args, 1);
		if (args[0].matches("-?\\d+")) {
			OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
			if (!(offlinePlayer instanceof Player player)) {
				return ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
			}
			if (!PlayerUtils.isOnline(player)) {
				return ChatUtils.sendWarning(sender, Component.text("Данный игрок не в сети"));
			}
			return sendPrivateMessage(senderInfo, new PlayerInfo(player.getUniqueId()), Component.text(message));
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (!(offlinePlayer instanceof Player player)) {
				return ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
			}
			if (!PlayerUtils.isOnline(player)) {
				return ChatUtils.sendWarning(sender, Component.text("Данный игрок не в сети"));
			}
			return sendPrivateMessage(senderInfo, new PlayerInfo(player.getUniqueId()), Component.text(message));
		}
		return ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return new AllLocalPlayers().onTabComplete(sender, command, label, args);
	}
}
