package com.github.MinersStudios.msUtils.listeners.chat;

import com.github.MinersStudios.msUtils.classes.ChatBuffer;
import com.github.MinersStudios.msUtils.utils.ChatUtils;
import com.github.MinersStudios.msUtils.Main;
import com.github.MinersStudios.msUtils.classes.PlayerInfo;
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
		if (player.getWorld() == Main.getWorldDark() || !Main.getAuthmeApi().isAuthenticated(player)) return;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());

		if (playerInfo.isMuted() && playerInfo.getMutedTo() - System.currentTimeMillis() < 0) {
			playerInfo.setMuted(false, null);
		}

		if (playerInfo.isMuted()) {
			ChatUtils.sendWarning(player, Component.text("Вы замучены"));
			return;
		}

		String message = ChatUtils.legacyComponentSerialize(event.originalMessage());
		if (message.startsWith("!")) {
			message = message.substring(1);
			if (message.length() != 0) {
				ChatUtils.sendMessageToChat(playerInfo, null, ChatUtils.Chat.GLOBAL, Component.text(message));
			}
		} else if (message.startsWith("*")) {
			message = message.substring(1);
			if (message.length() != 0) {
				ChatUtils.sendRPEventMessage(player, Component.text(message));
			}
		} else {
			ChatUtils.sendMessageToChat(playerInfo, player.getLocation(), ChatUtils.Chat.LOCAL, Component.text(message));
			ChatBuffer.receiveMessage(player, message);
		}
	}
}
