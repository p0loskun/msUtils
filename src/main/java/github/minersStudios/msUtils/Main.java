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
import github.minersStudios.msUtils.utils.Config;
import github.minersStudios.msUtils.utils.PlayerUtils;
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
		generateWorld();
		registerCommands();
		reloadConfigs();
		setOverworld();
		new RotateSeatTask();
	}

	@Override
	public void onDisable() {
		Bukkit.savePlayers();
		for (Player player : PlayerUtils.getSeats().keySet())
			PlayerUtils.setSitting(player, null);
		for (Player player : Bukkit.getOnlinePlayers())
			PlayerUtils.kickPlayer(player, "Выключение сервера", "Ну шо грифер, запустил свою лаг машину?");
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
		saveDefaultConfig();
		this.reloadConfig();
		cachedConfig = new Config();
	}
}
