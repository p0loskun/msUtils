package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.anomalies.Anomaly;
import com.github.minersstudios.msutils.anomalies.AnomalyAction;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.github.minersstudios.msutils.MSUtils.getInstance;

public final class ConfigCache {
	public final @NotNull File configFile;
	public final @NotNull YamlConfiguration configYaml;
	public final long anomalyCheckRate;
	public final long anomalyParticlesCheckRate;

	public final boolean developerMode;

	public final @NotNull File mutedPlayersFile;
	public final @NotNull YamlConfiguration mutedPlayersYaml;

	public final @NotNull Map<OfflinePlayer, Long> mutedPlayers = new HashMap<>();

	public final @NotNull Map<Player, ArmorStand> seats = new HashMap<>();
	public final @NotNull Map<NamespacedKey, Anomaly> anomalies = new HashMap<>();
	public final @NotNull Map<Player, Map<AnomalyAction, Long>> playerAnomalyActionMap = new ConcurrentHashMap<>();

	public final String
			discordGlobalChannelId,
			discordLocalChannelId;

	public final String version,
			user,
			repo,
			fullFileName,
			fullHash,
			liteFileName,
			liteHash;
	public final double localChatRadius;

	public final List<BukkitTask> bukkitTasks = new ArrayList<>();

	public ConfigCache() {
		this.configFile = getInstance().getConfigFile();
		this.configYaml = YamlConfiguration.loadConfiguration(this.configFile);

		this.developerMode = this.configYaml.getBoolean("developer-mode");

		this.anomalyCheckRate = this.configYaml.getLong("anomaly-check-rate");
		this.anomalyParticlesCheckRate = this.configYaml.getLong("anomaly-particles-check-rate");

		this.localChatRadius = this.configYaml.getDouble("chat.local.radius");
		this.discordGlobalChannelId = this.configYaml.getString("chat.global.discord-channel-id");
		this.discordLocalChannelId = this.configYaml.getString("chat.local.discord-channel-id");

		this.version = this.configYaml.getString("resource-pack.version");
		this.user = this.configYaml.getString("resource-pack.user");
		this.repo = this.configYaml.getString("resource-pack.repo");
		this.fullFileName = this.configYaml.getString("resource-pack.full.file-name");
		this.fullHash = this.configYaml.getString("resource-pack.full.hash");
		this.liteFileName = this.configYaml.getString("resource-pack.lite.file-name");
		this.liteHash = this.configYaml.getString("resource-pack.lite.hash");

		this.mutedPlayersFile = new File(getInstance().getPluginFolder(), "muted_players.yml");
		this.mutedPlayersYaml = YamlConfiguration.loadConfiguration(this.mutedPlayersFile);

		this.mutedPlayers.putAll(this.getMutedPlayers());

		this.loadAnomalies();
	}

	public @NotNull Map<OfflinePlayer, Long> getMutedPlayers() {
		Map<OfflinePlayer, Long> players = new HashMap<>();
		for (Map.Entry<String, Object> uuid : this.mutedPlayersYaml.getValues(true).entrySet()) {
			players.put(Bukkit.getOfflinePlayer(UUID.fromString(uuid.getKey())), (Long) uuid.getValue());
		}
		return players;
	}

	public void addMutedPlayer(@Nullable OfflinePlayer player, long time) {
		if (player == null) return;
		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), () -> this.mutedPlayers.put(player, time));
		this.mutedPlayersYaml.set(player.getUniqueId().toString(), time);
		try {
			this.mutedPlayersYaml.save(this.mutedPlayersFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void removeMutedPlayer(@Nullable OfflinePlayer player) {
		if (player == null) return;
		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), () -> this.mutedPlayers.remove(player));
		this.mutedPlayersYaml.set(player.getUniqueId().toString(), null);
		try {
			this.mutedPlayersYaml.save(this.mutedPlayersFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void loadAnomalies() {
		try (Stream<Path> path = Files.walk(Paths.get(MSUtils.getInstance().getPluginFolder() + "/anomalies"))) {
			path
			.filter(Files::isRegularFile)
			.map(Path::toFile)
			.forEach((file) -> {
				if (file.getName().equals("example.yml")) return;
				Anomaly anomaly = Anomaly.fromConfig(file, YamlConfiguration.loadConfiguration(file));
				this.anomalies.put(anomaly.getNamespacedKey(), anomaly);
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
