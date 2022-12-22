package com.github.minersstudios.msutils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.minersstudios.msutils.commands.RegCommands;
import com.github.minersstudios.msutils.listeners.RegEvents;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.utils.ConfigCache;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import fr.xephi.authme.api.v3.AuthMeApi;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public final class Main extends JavaPlugin {
	private static Plugin instance;
	private static ConfigCache cachedConfig;
	private static AuthMeApi authMeApi;
	private static World worldDark;
	private static World overworld;
	private static Scoreboard scoreboardHideTags;
	private static Team scoreboardHideTagsTeam;
	private static ProtocolManager protocolManager;

	@Override
	public void onEnable() {
		long time = System.currentTimeMillis();
		this.load();
		if (this.isEnabled()) {
			ChatUtils.log(Level.INFO, ChatColor.GREEN + "Enabled in " + (System.currentTimeMillis() - time) + "ms");
		}
	}

	@Override
	public void onDisable() {
		for (Player player : PlayerUtils.getSeats().keySet()) {
			PlayerUtils.setSitting(player, null, (String) null);
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerUtils.kickPlayer(player, "Выключение сервера", "Ну шо грифер, запустил свою лаг машину?");
		}
	}

	public void load() {
		instance = this;
		authMeApi = AuthMeApi.getInstance();
		worldDark = setWorldDark();
		overworld = setOverworld();
		protocolManager = ProtocolLibrary.getProtocolManager();
		scoreboardHideTags = (Objects.requireNonNull(Bukkit.getScoreboardManager())).getNewScoreboard();
		scoreboardHideTagsTeam = scoreboardHideTags.registerNewTeam("HideTags");
		scoreboardHideTagsTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		scoreboardHideTagsTeam.setCanSeeFriendlyInvisibles(false);
		RegEvents.init(this);
		RegCommands.init(this);
		reloadConfigs();

		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () ->
				PlayerUtils.getSeats().values().forEach((armorStand) ->
						armorStand.setRotation(armorStand.getPassengers().get(0).getLocation().getYaw(), 0.0f)
				), 0L, 1L
		);
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
			ChatUtils.sendError(null, Component.text("Main#generateWorld() world_dark = null"));
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
			return Bukkit.getWorld(properties.getProperty("level-name"));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public static void reloadConfigs() {
		getInstance().saveDefaultConfig();
		getInstance().reloadConfig();
		cachedConfig = new ConfigCache();
	}

	public static Plugin getInstance() {
		return instance;
	}

	public static ConfigCache getCachedConfig() {
		return cachedConfig;
	}

	public static AuthMeApi getAuthMeApi() {
		return authMeApi;
	}

	public static World getWorldDark() {
		return worldDark;
	}

	public static World getOverworld() {
		return overworld;
	}

	public static Scoreboard getScoreboardHideTags() {
		return scoreboardHideTags;
	}

	public static Team getScoreboardHideTagsTeam() {
		return scoreboardHideTagsTeam;
	}

	public static ProtocolManager getProtocolManager() {
		return protocolManager;
	}
}
