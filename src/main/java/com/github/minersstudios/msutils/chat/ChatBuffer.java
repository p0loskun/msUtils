package com.github.minersstudios.msutils.chat;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChatBuffer {
	private static final Map<String, Queue<String>> CHAT_QUEUE = new HashMap<>();

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

	private static void queueMessage(@NotNull Player player, @NotNull String message) {
		String UUID = player.getUniqueId().toString();
		if (!CHAT_QUEUE.containsKey(UUID)) {
			CHAT_QUEUE.put(UUID, new LinkedList<>());
			scheduleMessageUpdate(player, UUID, 0);
		}
		CHAT_QUEUE.get(UUID).add(message);
	}

	private static void scheduleMessageUpdate(@NotNull Player player, @NotNull String UUID, int timer) {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
			if (CHAT_QUEUE.get(UUID).isEmpty() || !player.isOnline()) {
				CHAT_QUEUE.remove(UUID);
			} else {
				scheduleMessageUpdate(player, UUID, spawnMessage(player, Objects.requireNonNull(CHAT_QUEUE.get(UUID).poll())) + 5);
			}
		}, timer);
	}

	public static int spawnMessage(@NotNull Player player, @NotNull String chat) {
		String[] chatLines = chat.split("\n");
		int duration = (chat.length() + (17 * chatLines.length)) * 1200 / 800;
		Entity vehicle = player;
		for (int i = chatLines.length - 1; i >= 0; i--) {
			vehicle = spawnNameTag(vehicle, chatLines[i], player.getLocation().add(0.0d, 1.0d, 0.0d), duration, i == 0);
		}
		return duration;
	}

	private static @NotNull AreaEffectCloud spawnNameTag(
			@NotNull Entity vehicle,
			@NotNull String text,
			@NotNull Location spawnPoint,
			int duration,
			boolean firstLine
	) {
		return spawnPoint.getWorld().spawn(spawnPoint, AreaEffectCloud.class, (areaEffectCloud) -> {
			areaEffectCloud.setParticle(Particle.TOWN_AURA);
			areaEffectCloud.setRadius(0);
			areaEffectCloud.customName(
					(firstLine ? ChatUtils.Symbols.SPEECH : Component.text(""))
					.append(Component.text(text))
					.append(Component.text(" "))
					.color(NamedTextColor.WHITE)
			);
			areaEffectCloud.setCustomNameVisible(true);
			areaEffectCloud.setWaitTime(0);
			areaEffectCloud.setDuration(duration);
			vehicle.addPassenger(areaEffectCloud);
		});
	}
}
