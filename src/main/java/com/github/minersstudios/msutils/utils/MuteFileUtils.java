package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;

public final class MuteFileUtils {

	private MuteFileUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static @NotNull Map<OfflinePlayer, Long> getMap() {
		return getConfigCache().mutedPlayers;
	}

	@Contract("null -> false")
	public static boolean isMuted(@Nullable OfflinePlayer offlinePlayer) {
		return offlinePlayer != null && getMap().containsKey(offlinePlayer);
	}

	public static void addPlayer(
			@NotNull OfflinePlayer player,
			long time
	) {
		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), () ->
				getMap().put(player, time)
		);
		getConfigCache().mutedPlayersYaml.set(player.getUniqueId().toString(), time);
		saveFile();
	}

	public static void removeMutedPlayer(@Nullable OfflinePlayer player) {
		if (player == null) return;
		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), () ->
				getMap().remove(player)
		);
		getConfigCache().mutedPlayersYaml.set(player.getUniqueId().toString(), null);
		saveFile();
	}

	private static void saveFile() {
		try {
			getConfigCache().mutedPlayersYaml.save(getConfigCache().mutedPlayersFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
