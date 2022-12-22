package com.github.minersstudios.msutils.chat;

import com.github.minersstudios.msutils.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChatBuffer {
	private static final Map<String, Queue<String>> chatQueue = new HashMap<>();

	public static void receiveMessage(@NotNull Player player, @NotNull String message) {
		if (message.length() <= 30) {
			queueMessage(player, message + "\n");
			return;
		}
		StringBuilder stringBuilder = new StringBuilder();
		int delimPos, lineCount = 0;

		while (message.length() > 0) {
			delimPos = message.lastIndexOf(' ', 30);
			if (delimPos < 0) {
				delimPos = message.indexOf(' ', 30);
				if (delimPos < 0) {
					delimPos = message.length();
				}
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

	private static void queueMessage(@NotNull Player player, @NotNull String message) {
		String UUID = player.getUniqueId().toString();
		if (!chatQueue.containsKey(UUID)) {
			chatQueue.put(UUID, new LinkedList<>());
			scheduleMessageUpdate(player, UUID, 0);
		}
		chatQueue.get(UUID).add(message);
	}

	public static int spawnMessage(@NotNull Player player, @NotNull String chat) {
		String[] chatLines = chat.split("\n");
		int duration = (chat.length() + (17 * chatLines.length)) * 1200 / 800;
		Entity vehicle = player;
		for (int chatLine = chatLines.length - 1 ; chatLine >= 0 ; chatLine--) {
			int finalChatLine = chatLine;
			Entity finalVehicle = vehicle;
			vehicle = player.getWorld().spawn(player.getLocation().clone().add(0.0d, 1.0d, 0.0d), AreaEffectCloud.class, (areaEffectCloud) -> {
				areaEffectCloud.setParticle(Particle.TOWN_AURA);
				areaEffectCloud.setRadius(0);
				areaEffectCloud.customName(
						Component.text("")
								.append(Component.text(chatLines[finalChatLine]))
								.append(Component.text(" "))
								.color(NamedTextColor.WHITE)
				);
				areaEffectCloud.setCustomNameVisible(true);
				areaEffectCloud.setWaitTime(0);
				areaEffectCloud.setDuration(duration);
				finalVehicle.addPassenger(areaEffectCloud);
			});
		}
		return duration;
	}

	private static void scheduleMessageUpdate(@NotNull Player player, @NotNull String UUID, int timer) {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
			if (chatQueue.get(UUID).isEmpty() || !player.isOnline()) {
				chatQueue.remove(UUID);
			} else {
				scheduleMessageUpdate(player, UUID, spawnMessage(player, Objects.requireNonNull(chatQueue.get(UUID).poll())) + 5);
			}
		}, timer);
	}
}
