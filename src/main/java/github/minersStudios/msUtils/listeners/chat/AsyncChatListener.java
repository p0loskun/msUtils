package github.minersStudios.msUtils.listeners.chat;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.ChatBuffer;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
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
		if (player.getWorld() == Main.worldDark || !Main.authmeApi.isAuthenticated(player)) return;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());

		if (playerInfo.isMuted() && playerInfo.getMutedTo() - System.currentTimeMillis() < 0)
			playerInfo.setMuted(false, null);

		if (playerInfo.isMuted()) {
			ChatUtils.sendWarning(player, Component.text("Вы замучены"));
			return;
		}

		String message = ChatUtils.plainTextSerializeComponent(event.originalMessage());
		if (message.startsWith("!")) {
			message = message.substring(1);
			if (message.length() != 0)
				ChatUtils.sendMessageToChat(playerInfo, null, -1, Component.text(message));
		} else if (message.startsWith("*")) {
			message = message.substring(1);
			if (message.length() != 0)
				ChatUtils.sendRPEventMessage(player, 25, Component.text(message));
		} else {
			ChatUtils.sendMessageToChat(playerInfo, player.getLocation(), 25, Component.text(message));
			ChatBuffer.receiveMessage(player, message);
		}
	}
}