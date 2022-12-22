package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerID;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PrivateMessageCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		}
		if (args.length < 2) return false;
		PlayerInfo privateMessageSender = new PlayerInfo(player.getUniqueId());
		if (privateMessageSender.isMuted()) {
			return ChatUtils.sendWarning(player, Component.text("Вы замьючены"));
		}
		String message = ChatUtils.extractMessage(args, 1);
		if (args[0].matches("[0-99]+")) {
			OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null) {
				return ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
			}
			if (offlinePlayer.getPlayer() == null) {
				return ChatUtils.sendWarning(player, Component.text("Данный игрок не в сети"));
			}
			if (offlinePlayer.getPlayer().getWorld() == Main.getWorldDark()) {
				return ChatUtils.sendWarning(player, Component.text("Данный игрок сейчас в червоточине!"));
			}
			return ChatUtils.sendPrivateMessage(privateMessageSender, new PlayerInfo(offlinePlayer.getUniqueId()), Component.text(message));
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				return ChatUtils.sendError(player, Component.text("Что-то пошло не так..."));
			}
			if (offlinePlayer.getPlayer() == null) {
				return ChatUtils.sendWarning(player, Component.text("Данный игрок не в сети"));
			}
			if (offlinePlayer.getPlayer().getWorld() == Main.getWorldDark()) {
				return ChatUtils.sendWarning(player, Component.text("Данный игрок сейчас в червоточине!"));
			}
			return ChatUtils.sendPrivateMessage(privateMessageSender, new PlayerInfo(offlinePlayer.getUniqueId()), Component.text(message));
		}
		return ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
	}
}
