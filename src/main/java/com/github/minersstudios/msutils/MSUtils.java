package com.github.minersstudios.msutils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.minersstudios.mscore.MSPlugin;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.InventoryUtils;
import com.github.minersstudios.msutils.anomalies.tasks.MainAnomalyActionsTask;
import com.github.minersstudios.msutils.anomalies.tasks.ParticleTask;
import com.github.minersstudios.msutils.inventory.CraftsMenu;
import com.github.minersstudios.msutils.listeners.chat.DiscordGuildMessagePreProcessListener;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.Pronouns;
import com.github.minersstudios.msutils.player.ResourcePack;
import com.github.minersstudios.msutils.utils.ConfigCache;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import fr.xephi.authme.api.v3.AuthMeApi;
import github.scarsz.discordsrv.DiscordSRV;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

public final class MSUtils extends MSPlugin {
	private static MSUtils instance;

	private static Scoreboard scoreboardHideTags;
	private static Team scoreboardHideTagsTeam;

	private static World worldDark;
	private static World overworld;

	private static AuthMeApi authMeApi;
	private static ProtocolManager protocolManager;

	private static ConfigCache configCache;

	public static PlayerInfo CONSOLE_PLAYER_INFO;

	@Override
	public void enable() {
		instance = this;
		scoreboardHideTags = (Objects.requireNonNull(Bukkit.getScoreboardManager())).getNewScoreboard();
		scoreboardHideTagsTeam = scoreboardHideTags.registerNewTeam("HideTags");
		scoreboardHideTagsTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		scoreboardHideTagsTeam.setCanSeeFriendlyInvisibles(false);
		Bukkit.getScheduler().runTask(instance, () -> worldDark = setWorldDark());
		overworld = setOverworld();
		protocolManager = ProtocolLibrary.getProtocolManager();
		authMeApi = AuthMeApi.getInstance();

		reloadConfigs();

		DiscordSRV.api.subscribe(new DiscordGuildMessagePreProcessListener());

		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () ->
				configCache.seats.values().forEach(
						(armorStand) -> armorStand.setRotation(armorStand.getPassengers().get(0).getLocation().getYaw(), 0.0f)
				), 0L, 1L
		);

		Bukkit.getScheduler().runTaskTimer(this, () -> {
			if (configCache.mutedPlayers.isEmpty()) return;
			configCache.mutedPlayers.keySet().stream()
					.filter((player) -> configCache.mutedPlayers.get(player) - System.currentTimeMillis() < 0)
					.forEach((player) -> MSPlayerUtils.getPlayerInfo(player.getUniqueId(), Objects.requireNonNull(player.getName())).setMuted(false, null));
		}, 0L, 50L);
	}

	@Override
	public void disable() {
		for (Player player : configCache.seats.keySet()) {
			MSPlayerUtils.getPlayerInfo(player).unsetSitting();
			Entity vehicle = player.getVehicle();
			if (vehicle != null) {
				vehicle.eject();
			}
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			MSPlayerUtils.getPlayerInfo(player).kickPlayer("Выключение сервера", "Ну шо грифер, запустил свою лаг машину?");
		}
	}

	private static @Nullable World setWorldDark() {
		World world = Bukkit.getWorld("world_dark");
		if (world != null) return world;
		world = new WorldCreator("world_dark")
				.generator(new ChunkGenerator() {})
				.generateStructures(false)
				.type(WorldType.NORMAL)
				.environment(World.Environment.NORMAL)
				.createWorld();
		if (world == null) {
			ChatUtils.sendError(null, Component.text("MSUtils#generateWorld() world_dark = null"));
			Bukkit.shutdown();
			return null;
		}
		world.setTime(18000L);
		world.setDifficulty(Difficulty.PEACEFUL);
		world.setGameRule(GameRule.FALL_DAMAGE, false);
		world.setGameRule(GameRule.FIRE_DAMAGE, false);
		world.setGameRule(GameRule.DROWNING_DAMAGE, false);
		world.setGameRule(GameRule.FREEZE_DAMAGE, false);
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
		world.setGameRule(GameRule.KEEP_INVENTORY, true);
		world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		return world;
	}

	public @Nullable World setOverworld() {
		try (InputStream input = new FileInputStream("server.properties")) {
			Properties properties = new Properties();
			properties.load(input);
			input.close();
			return Bukkit.getWorld(properties.getProperty("level-name"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void reloadConfigs() {
		instance.saveDefaultConfig();
		instance.reloadConfig();

		if (configCache != null) {
			configCache.bukkitTasks.forEach(BukkitTask::cancel);
		}

		instance.saveResource("anomalies/example.yml", true);
		File consoleDataFile = new File(instance.getPluginFolder(), "players/console.yml");
		if (!consoleDataFile.exists()) {
			instance.saveResource("players/console.yml", false);
		}
		CONSOLE_PLAYER_INFO = new PlayerInfo(UUID.randomUUID(), "$Console");

		configCache = new ConfigCache();
		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), ResourcePack::init);

		configCache.bukkitTasks.add(Bukkit.getScheduler().runTaskTimer(instance,
				() -> new MainAnomalyActionsTask().run(), 0L, configCache.anomalyCheckRate
		));

		configCache.bukkitTasks.add(Bukkit.getScheduler().runTaskTimer(instance,
				() -> new ParticleTask().run(), 0L, configCache.anomalyParticlesCheckRate
		));

		InventoryUtils.registerCustomInventory("pronouns", Pronouns.Menu.create());
		InventoryUtils.registerCustomInventory("resourcepack", ResourcePack.Menu.create());
		InventoryUtils.registerCustomInventory("crafts", CraftsMenu.create());
	}

	public static MSUtils getInstance() {
		return instance;
	}

	public static Scoreboard getScoreboardHideTags() {
		return scoreboardHideTags;
	}

	public static Team getScoreboardHideTagsTeam() {
		return scoreboardHideTagsTeam;
	}

	public static World getWorldDark() {
		return worldDark;
	}

	public static World getOverworld() {
		return overworld;
	}

	public static AuthMeApi getAuthMeApi() {
		return authMeApi;
	}

	public static ProtocolManager getProtocolManager() {
		return protocolManager;
	}

	public static ConfigCache getConfigCache() {
		return configCache;
	}
}
