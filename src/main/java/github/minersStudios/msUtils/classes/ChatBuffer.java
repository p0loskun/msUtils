package github.minersStudios.msUtils.classes;

import github.minersStudios.msUtils.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ChatBuffer {
	private final Map<String, Queue<String>> chatQueue = new HashMap<>();

	public void receiveChat(Player player, String msg) {
		if (msg.length() <= 30) {
			queueChat(player, msg + "\n");
			return;
		}
		msg += " ";
		StringBuilder chat = new StringBuilder();
		int delimPos, lineCount = 0;

		while (msg.length() > 0) {
			delimPos = msg.lastIndexOf(' ', 30);
			if (delimPos < 0) delimPos = msg.indexOf(' ', 30);
			if (delimPos < 0) delimPos = msg.length();

			chat.append(msg, 0, delimPos);
			msg = msg.substring(delimPos + 1);
			lineCount++;

			if (lineCount % 15 == 0 || msg.length() == 0) {
				queueChat(player, chat + (msg.length() == 0 ? "\n" : "...\n"));
				chat = new StringBuilder();
			} else {
				chat.append("\n");
			}
		}
	}

	private void queueChat(Player player, String chat) {
		String UUID = player.getUniqueId().toString();
		if (!this.chatQueue.containsKey(UUID)) {
			this.chatQueue.put(UUID, new LinkedList<>());
			scheduleMessageUpdate(player, UUID, 0);
		}
		this.chatQueue.get(UUID).add(chat);
	}

	private void scheduleMessageUpdate(Player player, String UUID, int timer) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (chatQueue.get(UUID).size() < 1 || !player.isOnline()) {
					chatQueue.remove(UUID);
					return;
				}
				scheduleMessageUpdate(player, UUID, Main.bubbles.receiveMessage(player, Objects.requireNonNull(chatQueue.get(UUID).poll())) + 5);
			}
		}.runTaskLater(Main.plugin, timer);
	}
}
