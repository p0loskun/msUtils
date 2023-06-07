package com.github.minersstudios.msutils.listeners.chat;

import com.github.minersstudios.mscore.listener.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.chat.ChatBuffer;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import com.github.minersstudios.msutils.utils.MessageUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import static com.github.minersstudios.mscore.utils.ChatUtils.*;
import static net.kyori.adventure.text.Component.text;

@MSListener
public class AsyncChatListener implements Listener {

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onAsyncChat(@NotNull AsyncChatEvent event) {
		event.setCancelled(true);

		Player player = event.getPlayer();
		PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(player);

		if (player.getWorld() == MSUtils.getWorldDark() || !MSUtils.getAuthMeApi().isAuthenticated(player)) return;

		if (playerInfo.isMuted() && playerInfo.getMutedTo() - System.currentTimeMillis() < 0) {
			playerInfo.setMuted(false, null);
		}

		if (playerInfo.isMuted()) {
			sendWarning(player, "Вы замьючены");
			return;
		}

		String message = serializeLegacyComponent(event.originalMessage());

		if (message.startsWith("!")) {
			message = message.substring(1).trim();

			if (!message.isEmpty()) {
				MessageUtils.sendMessageToChat(playerInfo, null, MessageUtils.Chat.GLOBAL, text(message));
			}
		} else if (message.startsWith("*")) {
			message = message.substring(1).trim();

			if (message.startsWith("*")) {
				message = message.substring(1).trim();

				if (message.startsWith("*")) {
					message = message.substring(1).trim();

					if (!message.isEmpty()) {
						MessageUtils.sendRPEventMessage(player, text(message), MessageUtils.RolePlayActionType.IT);
					}
				} else if (!message.isEmpty()) {
					MessageUtils.sendRPEventMessage(player, text(message), MessageUtils.RolePlayActionType.DO);
				}
			} else if (message.contains("*")) {
				String action = message.substring(message.indexOf('*') + 1).trim(),
						speech = message.substring(0 , message.indexOf('*')).trim();

				if (action.length() == 0 || speech.length() == 0) {
					sendError(player, "Используй: * [речь] * [действие]");
					return;
				}

				MessageUtils.sendRPEventMessage(player, text(speech), text(action), MessageUtils.RolePlayActionType.TODO);
			} else if (!message.isEmpty()) {
				MessageUtils.sendRPEventMessage(player, text(message), MessageUtils.RolePlayActionType.ME);
			}
		} else {
			MessageUtils.sendMessageToChat(playerInfo, player.getLocation(), MessageUtils.Chat.LOCAL, text(message));
			ChatBuffer.receiveMessage(player, message + " ");
		}
	}
}
