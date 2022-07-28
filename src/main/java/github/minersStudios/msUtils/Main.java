package github.minersStudios.msUtils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import fr.xephi.authme.api.v3.AuthMeApi;
import github.minersStudios.msUtils.classes.*;
import github.minersStudios.msUtils.commands.ban.*;
import github.minersStudios.msUtils.commands.mute.*;
import github.minersStudios.msUtils.commands.other.*;
import github.minersStudios.msUtils.commands.roleplay.*;
import github.minersStudios.msUtils.commands.teleport.TeleportToLastDeathLocationCommand;
import github.minersStudios.msUtils.commands.teleport.WorldTeleportCommand;
import github.minersStudios.msUtils.listeners.RegEvents;
import github.minersStudios.msUtils.tabCompleters.*;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
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
	public static Main plugin;
	public static AuthMeApi authmeApi;
	public static World worldDark, overworld;
	public static final ChatBubbles bubbles = new ChatBubbles();
	public static Scoreboard scoreboardHideTags;
	public static Team scoreboardHideTagsTeam;
	public static ProtocolManager protocolManager;

	@Override
	public void onEnable() {
		plugin = this;
		authmeApi = AuthMeApi.getInstance();
		worldDark = this.getServer().getWorld("world_dark");
		protocolManager = ProtocolLibrary.getProtocolManager();
		scoreboardHideTags = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
		scoreboardHideTagsTeam = scoreboardHideTags.registerNewTeam("HideTags");
		scoreboardHideTagsTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		scoreboardHideTagsTeam.setCanSeeFriendlyInvisibles(false);

		try {
			BufferedReader is = new BufferedReader(new FileReader("server.properties"));
			Properties properties = new Properties();
			properties.load(is);
			is.close();
			overworld = Bukkit.getWorld(properties.getProperty("level-name"));
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		RegEvents.init();
		new RotateSeatTask();
		generateWorld();
		registerCommands();

		if (!new File(plugin.getDataFolder(), "config.yml").exists())
			this.saveResource("config.yml", false);

		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				scoreboardHideTagsTeam.addEntry(player.getName());
				player.setScoreboard(scoreboardHideTags);
			}
		}
	}

	@Override
	public void onDisable() {
		Bukkit.savePlayers();
		for (Player player : PlayerUtils.getSeats().keySet())
			new SitPlayer(player).setSitting(null);
		for (Player player : Bukkit.getOnlinePlayers())
			PlayerUtils.kickPlayer(player, "Выключение сервера", "Ну шо грифер, запустил свою лаг машину?");
	}

	private static void registerCommands() {
		Objects.requireNonNull(plugin.getCommand("ban")).setExecutor(new BanCommand());
		Objects.requireNonNull(plugin.getCommand("ban")).setTabCompleter(new AllPlayers());
		Objects.requireNonNull(plugin.getCommand("unban")).setExecutor(new UnBanCommand());
		Objects.requireNonNull(plugin.getCommand("unban")).setTabCompleter(new AllPlayers());

		Objects.requireNonNull(plugin.getCommand("mute")).setExecutor(new MuteCommand());
		Objects.requireNonNull(plugin.getCommand("mute")).setTabCompleter(new AllPlayers());
		Objects.requireNonNull(plugin.getCommand("unmute")).setExecutor(new UnMuteCommand());
		Objects.requireNonNull(plugin.getCommand("unmute")).setTabCompleter(new AllPlayers());

		Objects.requireNonNull(plugin.getCommand("kick")).setExecutor(new KickCommand());
		Objects.requireNonNull(plugin.getCommand("kick")).setTabCompleter(new AllLocalPlayers());

		Objects.requireNonNull(plugin.getCommand("teleporttolastdeathlocation")).setExecutor(new TeleportToLastDeathLocationCommand());
		Objects.requireNonNull(plugin.getCommand("teleporttolastdeathlocation")).setTabCompleter(new AllLocalPlayers());
		Objects.requireNonNull(plugin.getCommand("worldteleport")).setExecutor(new WorldTeleportCommand());
		Objects.requireNonNull(plugin.getCommand("worldteleport")).setTabCompleter(new WorldTeleport());

		Objects.requireNonNull(plugin.getCommand("getmaploc")).setExecutor(new GetMapLocationCommand());
		Objects.requireNonNull(plugin.getCommand("crafts")).setExecutor(new CraftsCommand());
		Objects.requireNonNull(plugin.getCommand("resourcepack")).setExecutor(new ResourcePackCommand());
		Objects.requireNonNull(plugin.getCommand("info")).setExecutor(new InfoCommand());
		Objects.requireNonNull(plugin.getCommand("info")).setTabCompleter(new AllPlayers());
		Objects.requireNonNull(plugin.getCommand("whitelist")).setExecutor(new WhitelistCommand());
		Objects.requireNonNull(plugin.getCommand("whitelist")).setTabCompleter(new WhiteList());
		Objects.requireNonNull(plugin.getCommand("privatemessage")).setExecutor(new PrivateMessageCommand());
		Objects.requireNonNull(plugin.getCommand("privatemessage")).setTabCompleter(new AllLocalPlayers());

		Objects.requireNonNull(plugin.getCommand("sit")).setExecutor(new SitCommand());
		Objects.requireNonNull(plugin.getCommand("spit")).setExecutor(new SpitCommand());
		Objects.requireNonNull(plugin.getCommand("fart")).setExecutor(new FartCommand());
		Objects.requireNonNull(plugin.getCommand("me")).setExecutor(new MeCommand());
		Objects.requireNonNull(plugin.getCommand("try")).setExecutor(new TryCommand());
	}

	private static void generateWorld() {
		if (worldDark != null) return;
		worldDark = new WorldCreator("world_dark")
				.generator(new ChunkGenerator() {})
				.generateStructures(false)
				.type(WorldType.NORMAL)
				.environment(World.Environment.NORMAL).createWorld();
		if (worldDark == null) {
			ChatUtils.sendError(null, Component.text("Main#generateWorld() world_dark = null"));
			plugin.getServer().savePlayers();
			plugin.getServer().shutdown();
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
}
