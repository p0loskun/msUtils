package github.minersStudios.msUtils.commands;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.commands.ban.BanCommand;
import github.minersStudios.msUtils.commands.ban.UnBanCommand;
import github.minersStudios.msUtils.commands.mute.MuteCommand;
import github.minersStudios.msUtils.commands.mute.UnMuteCommand;
import github.minersStudios.msUtils.commands.other.*;
import github.minersStudios.msUtils.commands.roleplay.*;
import github.minersStudios.msUtils.commands.teleport.TeleportToLastDeathLocationCommand;
import github.minersStudios.msUtils.commands.teleport.WorldTeleportCommand;
import github.minersStudios.msUtils.tabCompleters.AllLocalPlayers;
import github.minersStudios.msUtils.tabCompleters.AllPlayers;
import github.minersStudios.msUtils.tabCompleters.WhiteList;
import github.minersStudios.msUtils.tabCompleters.WorldTeleport;

import javax.annotation.Nonnull;
import java.util.Objects;

public class RegCommands {

	public static void init(@Nonnull Main plugin) {
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
}
