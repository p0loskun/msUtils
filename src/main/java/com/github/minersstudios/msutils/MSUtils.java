package com.github.minersstudios.msutils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.minersstudios.mscore.MSPlugin;
import com.github.minersstudios.mscore.utils.InventoryUtils;
import com.github.minersstudios.msutils.anomalies.tasks.MainAnomalyActionsTask;
import com.github.minersstudios.msutils.anomalies.tasks.ParticleTask;
import com.github.minersstudios.msutils.inventory.CraftsMenu;
import com.github.minersstudios.msutils.inventory.PronounsMenu;
import com.github.minersstudios.msutils.inventory.ResourcePackMenu;
import com.github.minersstudios.msutils.listeners.chat.DiscordGuildMessagePreProcessListener;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.ResourcePack;
import com.github.minersstudios.msutils.config.ConfigCache;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import fr.xephi.authme.api.v3.AuthMeApi;
import github.scarsz.discordsrv.DiscordSRV;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class MSUtils extends MSPlugin {
	private static MSUtils instance;

	private static World worldDark;
	private static Entity darkEntity;
	private static World overworld;

	private static AuthMeApi authMeApi;
	private static ProtocolManager protocolManager;

	private static ConfigCache configCache;

	public static PlayerInfo consolePlayerInfo;

	public static Scoreboard scoreboardHideTags;
	public static Team scoreboardHideTagsTeam;

	@Override
	public void enable() {
		instance = this;
		protocolManager = ProtocolLibrary.getProtocolManager();
		authMeApi = AuthMeApi.getInstance();

		Bukkit.getScheduler().runTask(this, () -> {
			worldDark = setWorldDark();
			darkEntity = worldDark.getEntitiesByClass(ItemFrame.class).stream().findFirst().orElseGet(() ->
					worldDark.spawn(new Location(worldDark, 0, 13, 0), ItemFrame.class, (entity) -> {
						entity.setGravity(false);
						entity.setFixed(true);
						entity.setVisible(false);
						entity.setInvulnerable(true);
					})
			);
		});
		overworld = ((CraftServer) this.getServer()).getServer().overworld().getWorld();

		scoreboardHideTags = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboardHideTagsTeam = scoreboardHideTags.registerNewTeam("hide_tags");
		scoreboardHideTagsTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		scoreboardHideTagsTeam.setCanSeeFriendlyInvisibles(false);

		reloadConfigs();

		DiscordSRV.api.subscribe(new DiscordGuildMessagePreProcessListener());

		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () ->
				configCache.seats.forEach((key, value) -> value.setRotation(key.getLocation().getYaw(), 0.0f)),
				0L, 1L
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

		configCache.bukkitTasks.forEach(BukkitTask::cancel);
	}

	private static @NotNull World setWorldDark() {
		String name = "world_dark";
		boolean exists = new File(Bukkit.getWorldContainer(), name).exists();
		World world = new WorldCreator(name)
				.type(WorldType.FLAT)
				.environment(World.Environment.NORMAL)
				.biomeProvider(new BiomeProvider() {
					@Override
					public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
						return Biome.FOREST;
					}

					@Override
					public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
						return new ArrayList<>();
					}
				})
				.generator(new ChunkGenerator() {})
				.generateStructures(false)
				.hardcore(false)
				.keepSpawnLoaded(TriState.TRUE)
				.createWorld();

		if (world == null) {
			Bukkit.shutdown();
			throw new NullPointerException("MSUtils#setWorldDark() " + name + " = null");
		}

		if (!exists) {
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
		}
		return world;
	}

	public static void reloadConfigs() {
		instance.saveDefaultConfig();
		instance.reloadConfig();

		if (configCache != null) {
			configCache.bukkitTasks.forEach(BukkitTask::cancel);
		}
		configCache = new ConfigCache();

		instance.saveResource("anomalies/example.yml", true);
		File consoleDataFile = new File(instance.getPluginFolder(), "players/console.yml");
		if (!consoleDataFile.exists()) {
			instance.saveResource("players/console.yml", false);
		}
		consolePlayerInfo = new PlayerInfo(UUID.randomUUID(), "$Console");

		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), ResourcePack::init);

		configCache.bukkitTasks.add(Bukkit.getScheduler().runTaskTimer(instance,
				() -> new MainAnomalyActionsTask().run(), 0L, configCache.anomalyCheckRate
		));

		configCache.bukkitTasks.add(Bukkit.getScheduler().runTaskTimer(instance,
				() -> new ParticleTask().run(), 0L, configCache.anomalyParticlesCheckRate
		));

		InventoryUtils.registerCustomInventory("pronouns", PronounsMenu.create());
		InventoryUtils.registerCustomInventory("resourcepack", ResourcePackMenu.create());
		InventoryUtils.registerCustomInventory("crafts", CraftsMenu.create());
	}

	@Contract(pure = true)
	public static @NotNull MSUtils getInstance() {
		return instance;
	}

	@Contract(pure = true)
	public static @NotNull World getWorldDark() {
		return worldDark;
	}

	@Contract(pure = true)
	public static @NotNull Entity getDarkEntity() {
		return darkEntity;
	}

	@Contract(pure = true)
	public static @NotNull World getOverworld() {
		return overworld;
	}

	@Contract(pure = true)
	public static @NotNull AuthMeApi getAuthMeApi() {
		return authMeApi;
	}

	@Contract(pure = true)
	public static @NotNull ProtocolManager getProtocolManager() {
		return protocolManager;
	}

	@Contract(pure = true)
	public static @NotNull ConfigCache getConfigCache() {
		return configCache;
	}
}
