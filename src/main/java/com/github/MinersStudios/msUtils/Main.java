package com.github.MinersStudios.msUtils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.MinersStudios.msUtils.classes.ChatBubbles;
import com.github.MinersStudios.msUtils.classes.RotateSeatTask;
import com.github.MinersStudios.msUtils.commands.RegCommands;
import com.github.MinersStudios.msUtils.listeners.RegEvents;
import com.github.MinersStudios.msUtils.utils.ChatUtils;
import com.github.MinersStudios.msUtils.utils.Config;
import com.github.MinersStudios.msUtils.utils.PlayerUtils;
import fr.xephi.authme.api.v3.AuthMeApi;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.*;
import java.util.*;

public final class Main extends JavaPlugin {
	@Getter private static Main instance;
	@Getter private static Config cachedConfig;
	@Getter private static AuthMeApi authmeApi;
	@Getter private static World worldDark, overworld;
	@Getter private static final ChatBubbles bubbles = new ChatBubbles();
	@Getter private static Scoreboard scoreboardHideTags;
	@Getter private static Team scoreboardHideTagsTeam;
	@Getter private static ProtocolManager protocolManager;

	@Override
	public void onEnable() {
		instance = this;
		authmeApi = AuthMeApi.getInstance();
		worldDark = this.getServer().getWorld("world_dark");
		protocolManager = ProtocolLibrary.getProtocolManager();
		scoreboardHideTags = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
		scoreboardHideTagsTeam = scoreboardHideTags.registerNewTeam("HideTags");
		scoreboardHideTagsTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		scoreboardHideTagsTeam.setCanSeeFriendlyInvisibles(false);
		RegEvents.init(this);
		RegCommands.init(this);
		this.generateWorld();
		this.reloadConfigs();
		this.setOverworld();
		new RotateSeatTask();
	}

	@Override
	public void onDisable() {
		Bukkit.savePlayers();
		for (Player player : PlayerUtils.getSeats().keySet()) {
			PlayerUtils.setSitting(player, null, null);
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerUtils.kickPlayer(player, "Выключение сервера", "Ну шо грифер, запустил свою лаг машину?");
		}
	}

	private void generateWorld() {
		if (worldDark != null) return;
		worldDark = new WorldCreator("world_dark")
				.generator(new ChunkGenerator() {})
				.generateStructures(false)
				.type(WorldType.NORMAL)
				.environment(World.Environment.NORMAL).createWorld();
		if (worldDark == null) {
			ChatUtils.sendError(null, Component.text("Main#generateWorld() world_dark = null"));
			Bukkit.savePlayers();
			Bukkit.shutdown();
		}
		worldDark.setTime(18000);
		worldDark.setDifficulty(Difficulty.PEACEFUL);
		worldDark.setGameRule(GameRule.FALL_DAMAGE, false);
		worldDark.setGameRule(GameRule.FIRE_DAMAGE, false);
		worldDark.setGameRule(GameRule.DROWNING_DAMAGE, false);
		worldDark.setGameRule(GameRule.FREEZE_DAMAGE, false);
		worldDark.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		worldDark.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
		worldDark.setGameRule(GameRule.KEEP_INVENTORY, true);
		worldDark.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
	}

	public void setOverworld() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("server.properties"));
			Properties properties = new Properties();
			properties.load(bufferedReader);
			bufferedReader.close();
			overworld = Bukkit.getWorld(properties.getProperty("level-name"));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void reloadConfigs() {
		this.saveDefaultConfig();
		this.reloadConfig();
		cachedConfig = new Config();
	}
}
