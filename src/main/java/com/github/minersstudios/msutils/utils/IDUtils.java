package com.github.minersstudios.msutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;

public final class IDUtils {

	private IDUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static int getID(@NotNull UUID uuid, boolean addPlayer, boolean zeroIfNull) {
		return getConfigCache().idMap.containsKey(uuid)
				? getConfigCache().idMap.get(uuid)
				: addPlayer
				? addPlayer(uuid)
				: zeroIfNull ? 0 : -1;
	}

	public static void setID(@NotNull UUID uuid, int id) {
		getConfigCache().idMap.put(uuid, id);
		getConfigCache().idsYaml.set(uuid.toString(), id);
		saveFile();
	}

	public static int nextID() {
		Collection<Integer> ids = getConfigCache().idMap.values();
		return nextID(ids, ids.size());
	}

	private static int nextID(@NotNull Collection<Integer> ids, int id) {
		return ids.contains(id) ? nextID(ids, id + 1) : id;
	}

	public static @Nullable UUID getUUIDByID(int id) {
		return getConfigCache().idMap.entrySet()
				.stream()
				.filter(entry -> entry.getValue().equals(id))
				.map(Map.Entry::getKey)
				.findFirst()
				.orElse(null);
	}

	public static @Nullable OfflinePlayer getPlayerByID(int id) {
		UUID uuid = getUUIDByID(id);
		return uuid == null ? null : Bukkit.getOfflinePlayer(uuid);
	}

	public static int addPlayer(@NotNull UUID uuid) {
		int id = nextID();
		setID(uuid, id);
		return id;
	}

	public static void saveFile() {
		try {
			getConfigCache().idsYaml.save(getConfigCache().idsFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
