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
import github.minersStudios.msUtils.tabComplete.*;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
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
		this.generateWorld();
		this.registerCommands();

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
		for (Player player : Bukkit.getOnlinePlayers()) {
			new PlayerInfo(player.getUniqueId()).setLastLeaveLocation(player);
			player.kickPlayer("Ну шо грифер, запустил свою лаг машину?");
		}
	}

	private void registerCommands() {
		Objects.requireNonNull(this.getCommand("ban")).setExecutor(new BanCommand());
		Objects.requireNonNull(this.getCommand("ban")).setTabCompleter(new AllPlayers());
		Objects.requireNonNull(this.getCommand("unban")).setExecutor(new UnBanCommand());
		Objects.requireNonNull(this.getCommand("unban")).setTabCompleter(new AllPlayers());

		Objects.requireNonNull(this.getCommand("mute")).setExecutor(new MuteCommand());
		Objects.requireNonNull(this.getCommand("mute")).setTabCompleter(new AllPlayers());
		Objects.requireNonNull(this.getCommand("unmute")).setExecutor(new UnMuteCommand());
		Objects.requireNonNull(this.getCommand("unmute")).setTabCompleter(new AllPlayers());

		Objects.requireNonNull(this.getCommand("kick")).setExecutor(new KickCommand());
		Objects.requireNonNull(this.getCommand("kick")).setTabCompleter(new AllLocalPlayers());

		Objects.requireNonNull(this.getCommand("teleporttolastdeathlocation")).setExecutor(new TeleportToLastDeathLocationCommand());
		Objects.requireNonNull(this.getCommand("teleporttolastdeathlocation")).setTabCompleter(new AllLocalPlayers());
		Objects.requireNonNull(this.getCommand("worldteleport")).setExecutor(new WorldTeleportCommand());
		Objects.requireNonNull(this.getCommand("worldteleport")).setTabCompleter(new WorldTeleport());

		Objects.requireNonNull(this.getCommand("getmaploc")).setExecutor(new GetMapLocationCommand());
		Objects.requireNonNull(this.getCommand("crafts")).setExecutor(new CraftsCommand());
		Objects.requireNonNull(this.getCommand("resourcepack")).setExecutor(new ResourcePackCommand());
		Objects.requireNonNull(this.getCommand("info")).setExecutor(new InfoCommand());
		Objects.requireNonNull(this.getCommand("info")).setTabCompleter(new AllPlayers());
		Objects.requireNonNull(this.getCommand("whitelist")).setExecutor(new WhitelistCommand());
		Objects.requireNonNull(this.getCommand("whitelist")).setTabCompleter(new WhiteList());
		Objects.requireNonNull(this.getCommand("privatemessage")).setExecutor(new PrivateMessageCommand());
		Objects.requireNonNull(this.getCommand("privatemessage")).setTabCompleter(new AllLocalPlayers());

		Objects.requireNonNull(this.getCommand("sit")).setExecutor(new SitCommand());
		Objects.requireNonNull(this.getCommand("spit")).setExecutor(new SpitCommand());
		Objects.requireNonNull(this.getCommand("fart")).setExecutor(new FartCommand());
		Objects.requireNonNull(this.getCommand("me")).setExecutor(new MeCommand());
		Objects.requireNonNull(this.getCommand("try")).setExecutor(new TryCommand());
	}

	private void generateWorld() {
		if (worldDark != null) return;
		if ((worldDark = new WorldCreator("world_dark")
				.generator(new ChunkGenerator() {})
				.generateStructures(false)
				.type(WorldType.NORMAL)
				.environment(World.Environment.NORMAL).createWorld()) != null
		) {
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
		} else {
			ChatUtils.sendError(null, "Main#generateWorld() world_dark = null");
			plugin.getServer().savePlayers();
			plugin.getServer().shutdown();
		}
	}
}
