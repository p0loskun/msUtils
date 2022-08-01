package github.minersStudios.msUtils.classes;

import github.minersStudios.msUtils.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

public class ChatBuffer {
	private static final Map<String, Queue<String>> chatQueue = new HashMap<>();

	public static void receiveMessage(@Nonnull Player player, @Nonnull String message) {
		if (message.length() <= 30) {
			queueMessage(player, message + "\n");
			return;
		}
		message += " ";
		StringBuilder stringBuilder = new StringBuilder();
		int delimPos, lineCount = 0;

		while (message.length() > 0) {
			delimPos = message.lastIndexOf(' ', 30);
			if (delimPos < 0) {
				delimPos = message.indexOf(' ', 30);
			}
			if (delimPos < 0) {
				delimPos = message.length();
			}

			stringBuilder.append(message, 0, delimPos);
			message = message.substring(delimPos + 1);
			lineCount++;

			if (lineCount % 15 == 0 || message.length() == 0) {
				queueMessage(player, stringBuilder + (message.length() == 0 ? "\n" : "...\n"));
				stringBuilder = new StringBuilder();
			} else {
				stringBuilder.append("\n");
			}
		}
	}

	private static void queueMessage(@Nonnull Player player, @Nonnull String message) {
		String UUID = player.getUniqueId().toString();
		if (!chatQueue.containsKey(UUID)) {
			chatQueue.put(UUID, new LinkedList<>());
			scheduleMessageUpdate(player, UUID, 0);
		}
		chatQueue.get(UUID).add(message);
	}

	private static void scheduleMessageUpdate(@Nonnull Player player, @Nonnull String UUID, int timer) {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
			if (chatQueue.get(UUID).size() < 1 || !player.isOnline()) {
				chatQueue.remove(UUID);
			} else {
				scheduleMessageUpdate(player, UUID, Main.getBubbles().receiveMessage(player, Objects.requireNonNull(chatQueue.get(UUID).poll())) + 5);
			}
		}, timer);
	}
}
