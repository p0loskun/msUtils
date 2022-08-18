package com.github.minersstudios.msutils.commands;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.commands.ban.BanCommand;
import com.github.minersstudios.msutils.commands.ban.UnBanCommand;
import com.github.minersstudios.msutils.commands.mute.MuteCommand;
import com.github.minersstudios.msutils.commands.mute.UnMuteCommand;
import com.github.minersstudios.msutils.commands.other.*;
import com.github.minersstudios.msutils.commands.roleplay.*;
import com.github.minersstudios.msutils.tabCompleters.*;
import com.github.minersstudios.msutils.commands.teleport.TeleportToLastDeathLocationCommand;
import com.github.minersstudios.msutils.commands.teleport.WorldTeleportCommand;

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

		Objects.requireNonNull(plugin.getCommand("msutils")).setExecutor(new CommandHandler());
		Objects.requireNonNull(plugin.getCommand("msutils")).setTabCompleter(new TabCommandHandler());
		Objects.requireNonNull(plugin.getCommand("getmaploc")).setExecutor(new GetMapLocationCommand());
		Objects.requireNonNull(plugin.getCommand("getmaploc")).setTabCompleter(new Empty());
		Objects.requireNonNull(plugin.getCommand("crafts")).setExecutor(new CraftsCommand());
		Objects.requireNonNull(plugin.getCommand("crafts")).setTabCompleter(new Empty());
		Objects.requireNonNull(plugin.getCommand("resourcepack")).setExecutor(new ResourcePackCommand());
		Objects.requireNonNull(plugin.getCommand("resourcepack")).setTabCompleter(new Empty());
		Objects.requireNonNull(plugin.getCommand("info")).setExecutor(new InfoCommand());
		Objects.requireNonNull(plugin.getCommand("info")).setTabCompleter(new AllPlayers());
		Objects.requireNonNull(plugin.getCommand("whitelist")).setExecutor(new WhitelistCommand());
		Objects.requireNonNull(plugin.getCommand("whitelist")).setTabCompleter(new WhiteList());
		Objects.requireNonNull(plugin.getCommand("privatemessage")).setExecutor(new PrivateMessageCommand());
		Objects.requireNonNull(plugin.getCommand("privatemessage")).setTabCompleter(new AllLocalPlayers());

		Objects.requireNonNull(plugin.getCommand("sit")).setExecutor(new SitCommand());
		Objects.requireNonNull(plugin.getCommand("sit")).setTabCompleter(new Empty());
		Objects.requireNonNull(plugin.getCommand("spit")).setExecutor(new SpitCommand());
		Objects.requireNonNull(plugin.getCommand("spit")).setTabCompleter(new Empty());
		Objects.requireNonNull(plugin.getCommand("fart")).setExecutor(new FartCommand());
		Objects.requireNonNull(plugin.getCommand("fart")).setTabCompleter(new Empty());
		Objects.requireNonNull(plugin.getCommand("me")).setExecutor(new MeCommand());
		Objects.requireNonNull(plugin.getCommand("me")).setTabCompleter(new Empty());
		Objects.requireNonNull(plugin.getCommand("try")).setExecutor(new TryCommand());
		Objects.requireNonNull(plugin.getCommand("try")).setTabCompleter(new Empty());
		Objects.requireNonNull(plugin.getCommand("todo")).setExecutor(new TodoCommand());
		Objects.requireNonNull(plugin.getCommand("todo")).setTabCompleter(new Empty());
		Objects.requireNonNull(plugin.getCommand("it")).setExecutor(new ItCommand());
		Objects.requireNonNull(plugin.getCommand("it")).setTabCompleter(new Empty());
		Objects.requireNonNull(plugin.getCommand("do")).setExecutor(new DoCommand());
		Objects.requireNonNull(plugin.getCommand("do")).setTabCompleter(new Empty());
	}
}
