package com.github.minersstudios.msutils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.minersstudios.msutils.classes.ChatBubbles;
import com.github.minersstudios.msutils.classes.RotateSeatTask;
import com.github.minersstudios.msutils.commands.RegCommands;
import com.github.minersstudios.msutils.listeners.RegEvents;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.utils.ConfigCache;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import fr.xephi.authme.api.v3.AuthMeApi;
import github.scarsz.discordsrv.DiscordSRV;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;
import java.util.logging.Level;

public final class Main extends JavaPlugin {
	private static Main instance;
	private static final ChatBubbles bubbles = new ChatBubbles();
	private static ConfigCache configCache;
	private static AuthMeApi authMeApi;
	private static ProtocolManager protocolManager;
	private static World worldDark;
	private static World overworld;
	private static Scoreboard scoreboardHideTags;
	private static Team scoreboardHideTagsTeam;
	public static boolean
			isProtocolLibEnabled,
			isAuthMeEnabled,
			isDiscordSRVEnabled,
			isMsDecorEnabled,
			isMsBlockEnabled;
	public static String
			PROTOCOLLIB_VERSION = "5.0.0-SNAPSHOT",
			AUTHME_VERSION = "5.6.0-SNAPSHOT",
			DISCORDSRV_VERSION = "1.26.0-SNAPSHOT",
			MSDECOR_VERSION = "0.5.0",
			MSBLOCK_VERSION = "0.5.0";

	@Override
	public void onEnable() {
		long time = System.currentTimeMillis();
		load(this);
		if (this.isEnabled()) {
			ChatUtils.log(Level.INFO, ChatColor.GREEN + "Enabled in " + (System.currentTimeMillis() - time) + "ms");
		}
	}

	@Override
	public void onDisable() {
		long time = System.currentTimeMillis();
		PlayerUtils.getSeats().keySet().forEach((player) ->
				PlayerUtils.setSitting(player, null, (String) null)
		);
		Bukkit.getOnlinePlayers().forEach((player) ->
				PlayerUtils.kickPlayer(player, "Выключение сервера", "Ну шо грифер, запустил свою лаг машину?")
		);
		ChatUtils.log(Level.INFO, ChatColor.GREEN + "Disabled in " + (System.currentTimeMillis() - time) + "ms");
	}

	public static void load(Main main) {
		instance = main;

		reloadConfigs();
		isAuthMeEnabled = configCache.enable_authme_hook;
		isDiscordSRVEnabled = configCache.enable_discordsrv_hook;
		isMsDecorEnabled = configCache.enable_msdecor_hook;
		isMsBlockEnabled = configCache.enable_msblock_hook;

		worldDark = setWorldDark();
		overworld = setOverworld();

		scoreboardHideTags = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboardHideTagsTeam = scoreboardHideTags.registerNewTeam("hide_tags");
		scoreboardHideTagsTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		scoreboardHideTagsTeam.setCanSeeFriendlyInvisibles(false);

		new RotateSeatTask();

		try {
			protocolManager = ProtocolLibrary.getProtocolManager();
			isProtocolLibEnabled = ProtocolLibrary.getPlugin().getDescription().getVersion().contains(PROTOCOLLIB_VERSION);
		} catch (NoClassDefFoundError error) {
			error.printStackTrace();
		} finally {
			if (isProtocolLibEnabled) {
				ChatUtils.log(Level.INFO, "ProtocolLib successfully hooked!");
			} else {
				ChatUtils.log(Level.SEVERE, "Install ProtocolLib (v" + PROTOCOLLIB_VERSION + ")!");
				Bukkit.getPluginManager().disablePlugin(main);
			}
		}
		if (isAuthMeEnabled) {
			try {
				authMeApi = AuthMeApi.getInstance();
				isAuthMeEnabled = AUTHME_VERSION.contains(authMeApi.getPluginVersion());
			} catch (NoClassDefFoundError error) {
				isAuthMeEnabled = false;
				error.printStackTrace();
			} finally {
				if (isAuthMeEnabled) {
					ChatUtils.log(Level.INFO, "AuthMe successfully hooked!");
				} else {
					ChatUtils.log(Level.SEVERE, "Install AuthMe (v" + AUTHME_VERSION + ")!");
					Bukkit.getPluginManager().disablePlugin(main);
				}
			}
		}
		if (isDiscordSRVEnabled) {
			try {
				if (!DiscordSRV.updateIsAvailable) {
					isDiscordSRVEnabled = DiscordSRV.version.contains(DISCORDSRV_VERSION);
				}
			} catch (NoClassDefFoundError error) {
				isDiscordSRVEnabled = false;
			} finally {
				if (isDiscordSRVEnabled) {
					ChatUtils.log(Level.INFO, "DiscordSRV successfully hooked!");
				} else {
					ChatUtils.log(Level.SEVERE, "Install DiscordSRV (v" + DISCORDSRV_VERSION + ") or disable plugin hook in config!");
				}
			}
		}
		if (isMsDecorEnabled) {
			try {
				isMsDecorEnabled = Main.getPlugin(com.github.minersstudios.msDecor.Main.class).getDescription().getVersion().contains(MSDECOR_VERSION);
			} catch (NoClassDefFoundError error) {
				isMsDecorEnabled = false;
			} finally {
				if (isMsDecorEnabled) {
					ChatUtils.log(Level.INFO, "msDecor successfully hooked!");
				} else {
					ChatUtils.log(Level.SEVERE, "Install msDecor (v" + MSDECOR_VERSION + ") or disable plugin hook in config!");
				}
			}
		}
		if (isMsBlockEnabled) {
			try {
				isMsBlockEnabled = Main.getPlugin(com.github.minersstudios.msBlock.Main.class).getDescription().getVersion().contains(MSBLOCK_VERSION);
			} catch (NoClassDefFoundError error) {
				isMsBlockEnabled = false;
			} finally {
				if (isMsBlockEnabled) {
					ChatUtils.log(Level.INFO, "msBlock successfully hooked!");
				} else {
					ChatUtils.log(Level.SEVERE, "Install msBlock (v" + MSBLOCK_VERSION + ") or disable plugin hook in config!");
				}
			}
		}

		RegEvents.init(main);
		RegCommands.init(main);
	}

	@Nullable
	private static World setWorldDark() {
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

	@Nullable
	public static World setOverworld() {
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
		instance.saveDefaultConfig();
		instance.reloadConfig();
		configCache = new ConfigCache();
	}

	public static Main getInstance() {
		return instance;
	}

	public static ConfigCache getConfigCache() {
		return configCache;
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

	public static ChatBubbles getBubbles() {
		return bubbles;
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
