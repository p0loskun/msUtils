package com.github.minersstudios.msutils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.mscore.MSPlugin;
import com.github.minersstudios.mscore.utils.MSPluginUtils;
import com.github.minersstudios.msutils.anomalies.tasks.MainAnomalyActionsTask;
import com.github.minersstudios.msutils.anomalies.tasks.ParticleTask;
import com.github.minersstudios.msutils.config.ConfigCache;
import com.github.minersstudios.msutils.inventory.CraftsMenu;
import com.github.minersstudios.msutils.inventory.PronounsMenu;
import com.github.minersstudios.msutils.inventory.ResourcePackMenu;
import com.github.minersstudios.msutils.listeners.chat.DiscordGuildMessagePreProcessListener;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.ResourcePack;
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
import org.bukkit.inventory.Recipe;
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

import static com.github.minersstudios.mscore.utils.InventoryUtils.registerCustomInventory;

public final class MSUtils extends MSPlugin {
	private static MSUtils instance;

	private static AuthMeApi authMeApi;
	private static ProtocolManager protocolManager;

	private World worldDark;
	private Entity darkEntity;
	private World overworld;
	private ConfigCache configCache;
	private PlayerInfo consolePlayerInfo;
	private Scoreboard scoreboardHideTags;
	private Team scoreboardHideTagsTeam;

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
					public @NotNull Biome getBiome(
							@NotNull WorldInfo worldInfo,
							int x,
							int y,
							int z
					) {
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

		if (instance.configCache != null) {
			instance.configCache.bukkitTasks.forEach(BukkitTask::cancel);
		}
		instance.configCache = new ConfigCache();

		instance.saveResource("anomalies/example.yml", true);
		File consoleDataFile = new File(instance.getPluginFolder(), "players/console.yml");
		if (!consoleDataFile.exists()) {
			instance.saveResource("players/console.yml", false);
		}
		instance.consolePlayerInfo = new PlayerInfo(UUID.randomUUID(), "$Console");

		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), ResourcePack::init);

		instance.configCache.bukkitTasks.add(Bukkit.getScheduler().runTaskTimer(instance,
				() -> new MainAnomalyActionsTask().run(), 0L, instance.configCache.anomalyCheckRate
		));

		instance.configCache.bukkitTasks.add(Bukkit.getScheduler().runTaskTimer(instance,
				() -> new ParticleTask().run(), 0L, instance.configCache.anomalyParticlesCheckRate
		));

		registerCustomInventory("pronouns", PronounsMenu.create());
		registerCustomInventory("resourcepack", ResourcePackMenu.create());
		registerCustomInventory("crafts", CraftsMenu.create());

		List<Recipe> customBlockRecipes = MSCore.getConfigCache().customBlockRecipes;
		List<Recipe> customDecorRecipes = MSCore.getConfigCache().customDecorRecipes;
		List<Recipe> customItemRecipes = MSCore.getConfigCache().customItemRecipes;
		Bukkit.getScheduler().runTaskTimer(instance, (task) -> {
			if (
					MSPluginUtils.isLoadedCustoms()
					&& !customBlockRecipes.isEmpty()
					&& !customDecorRecipes.isEmpty()
					&& !customItemRecipes.isEmpty()
			) {
				registerCustomInventory("crafts_blocks", CraftsMenu.createCraftsInventory(customBlockRecipes));
				registerCustomInventory("crafts_decors", CraftsMenu.createCraftsInventory(customDecorRecipes));
				registerCustomInventory("crafts_items", CraftsMenu.createCraftsInventory(customItemRecipes));
				task.cancel();
			}
		}, 0L, 10L);
	}

	@Contract(pure = true)
	public static @NotNull MSUtils getInstance() {
		return instance;
	}

	@Contract(pure = true)
	public static @NotNull World getWorldDark() {
		return instance.worldDark;
	}

	@Contract(pure = true)
	public static @NotNull Entity getDarkEntity() {
		return instance.darkEntity;
	}

	@Contract(pure = true)
	public static @NotNull World getOverworld() {
		return instance.overworld;
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
		return instance.configCache;
	}

	@Contract(pure = true)
	public static @NotNull PlayerInfo getConsolePlayerInfo() {
		return instance.consolePlayerInfo;
	}

	@Contract(pure = true)
	public static @NotNull Scoreboard getScoreboardHideTags() {
		return instance.scoreboardHideTags;
	}

	@Contract(pure = true)
	public static @NotNull Team getScoreboardHideTagsTeam() {
		return instance.scoreboardHideTagsTeam;
	}
}
