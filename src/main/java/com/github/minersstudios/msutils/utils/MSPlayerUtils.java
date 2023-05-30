package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.msutils.player.PlayerInfo;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;

public final class MSPlayerUtils {

	private MSPlayerUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static @NotNull PlayerInfo getPlayerInfo(@NotNull UUID uuid, @NotNull String nickname) {
		PlayerInfo playerInfo = getConfigCache().playerInfoMap.get(uuid);
		if (playerInfo == null) {
			playerInfo = new PlayerInfo(uuid, nickname);
			getConfigCache().playerInfoMap.put(uuid, playerInfo);
		}
		return playerInfo;
	}

	public static @NotNull PlayerInfo getPlayerInfo(@NotNull Player player) {
		UUID uuid = player.getUniqueId();
		PlayerInfo playerInfo = getConfigCache().playerInfoMap.get(uuid);
		if (playerInfo == null) {
			playerInfo = new PlayerInfo(player);
			getConfigCache().playerInfoMap.put(uuid, playerInfo);
		}
		return playerInfo;
	}
}
