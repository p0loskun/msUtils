package com.github.MinersStudios.msUtils.commands;

import com.github.MinersStudios.msUtils.commands.ban.BanCommand;
import com.github.MinersStudios.msUtils.commands.ban.UnBanCommand;
import com.github.MinersStudios.msUtils.commands.mute.MuteCommand;
import com.github.MinersStudios.msUtils.commands.mute.UnMuteCommand;
import com.github.MinersStudios.msUtils.commands.other.*;
import com.github.MinersStudios.msUtils.commands.roleplay.*;
import com.github.MinersStudios.msUtils.tabCompleters.AllLocalPlayers;
import com.github.MinersStudios.msUtils.Main;
import com.github.MinersStudios.msUtils.commands.teleport.TeleportToLastDeathLocationCommand;
import com.github.MinersStudios.msUtils.commands.teleport.WorldTeleportCommand;
import com.github.MinersStudios.msUtils.tabCompleters.AllPlayers;
import com.github.MinersStudios.msUtils.tabCompleters.WhiteList;
import com.github.MinersStudios.msUtils.tabCompleters.WorldTeleport;

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
