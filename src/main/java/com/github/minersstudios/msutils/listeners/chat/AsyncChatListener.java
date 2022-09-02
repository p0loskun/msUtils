package com.github.minersstudios.msutils.listeners.chat;

import com.github.minersstudios.msutils.classes.Chat;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.classes.PlayerInfo;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

public class AsyncChatListener implements Listener {

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onAsyncPlayerChat(@Nonnull AsyncChatEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		if (player.getWorld() == Main.getWorldDark() || !Main.getAuthMeApi().isAuthenticated(player)) return;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());

		if (playerInfo.isMuted()) {
			if (playerInfo.getMutedTo() - System.currentTimeMillis() < 0) {
				playerInfo.setMuted(false, null);
			} else {
				ChatUtils.sendWarning(player, Component.text("Вы замьючены"));
				return;
			}
		}

		String message = ChatUtils.legacyComponentSerialize(event.originalMessage());
		Chat chat = Chat.getChatBySymbol(message);

		if (chat != null && chat.isEnabled()) {
			message = message.substring(chat.getSymbol().length()).trim();
			if (!message.isEmpty()) {
				ChatUtils.sendMessageToChat(player, playerInfo, null, chat, Component.text(message));
			}
		} else if (
				message.startsWith("*")
				&& Chat.actionsChat.isEnableChatSymbols()
				&& Chat.actionsChat.isEnabled()
		) {
			message = message.substring(1).trim();
			if (message.startsWith("*")) {
				message = message.substring(1).trim();
				if (message.startsWith("*")) {
					message = message.substring(1).trim();
					if (!message.isEmpty()) {
						ChatUtils.sendRPEventMessage(player, Component.text(message), ChatUtils.RolePlayActionType.IT);
					}
				} else if (!message.isEmpty()) {
					ChatUtils.sendRPEventMessage(player, Component.text(message), ChatUtils.RolePlayActionType.DO);
				}
			} else if (message.contains("*")) {
				String action = message.substring(message.indexOf('*') + 1).trim(),
						speech = message.substring(0 , message.indexOf('*')).trim();
				if (action.length() == 0 || speech.length() == 0) {
					ChatUtils.sendError(player, Component.text("Используй: * [речь] * [действие]"));
					return;
				}
				ChatUtils.sendRPEventMessage(player, Component.text(speech), Component.text(action), ChatUtils.RolePlayActionType.TODO);
			} else if (!message.isEmpty()) {
				ChatUtils.sendRPEventMessage(player, Component.text(message), ChatUtils.RolePlayActionType.ME);
			}
		} else {
			chat = Chat.getChatWithoutSymbol();
			if (chat == null) return;
			ChatUtils.sendMessageToChat(player, playerInfo, player.getLocation(), chat, Component.text(message));
		}
	}
}
