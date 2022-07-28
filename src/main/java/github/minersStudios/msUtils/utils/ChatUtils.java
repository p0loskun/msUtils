package github.minersStudios.msUtils.utils;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.objects.MessageFormat;
import github.scarsz.discordsrv.util.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class ChatUtils {
	public static final String
			discordGlobalChannelID = Main.plugin.getConfig().getString("discord-global-channel-id"),
			discordLocalChannelID = Main.plugin.getConfig().getString("discord-local-channel-id");

	/**
	 * Sends info message to target
	 *
	 * @param target target
	 * @param message warning message
	 */
	public static boolean sendInfo(@Nullable Object target, @Nonnull String message) {
		if (target instanceof Player player) {
			player.sendMessage(" " + message);
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(" " + message);
		} else {
			Bukkit.getLogger().info(message);
		}
		return true;
	}

	/**
	 * Sends fine message to target
	 *
	 * @param target target
	 * @param message warning message
	 */
	public static boolean sendFine(@Nullable Object target, @Nonnull String message) {
		if (target instanceof Player player) {
			player.sendMessage(" ꀒ " + ChatColor.GREEN + message);
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(ChatColor.GREEN + message);
		} else {
			Bukkit.getLogger().info(ChatColor.GREEN + message);
		}
		return true;
	}

	/**
	 * Sends warning message to target
	 *
	 * @param target target
	 * @param message warning message
	 */
	public static boolean sendWarning(@Nullable Object target, @Nonnull String message) {
		if (target instanceof Player player) {
			player.sendMessage(" ꀓ " + ChatColor.GOLD + message);
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(ChatColor.GOLD + message);
		} else {
			Bukkit.getLogger().warning(message);
		}
		return true;
	}

	/**
	 * Sends error message to target
	 *
	 * @param target target
	 * @param message warning message
	 */
	public static boolean sendError(@Nullable Object target, @Nonnull String message) {
		if (target instanceof Player player) {
			player.sendMessage(" ꀑ " + ChatColor.RED + message);
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(ChatColor.RED + message);
		} else {
			Bukkit.getLogger().severe(message);
		}
		return true;
	}

	/**
	 * Sends message to chat
	 *
	 * @param playerInfo player info
	 * @param location sender location
	 * @param radius message radius
	 * @param message message
	 */
	public static void sendMessageToChat(@Nonnull PlayerInfo playerInfo, @Nullable Location location, int radius, @Nonnull String message) {
		if (radius > -1 && location != null) {
			String localMessage = " " + ChatColor.of("#aba494") + playerInfo.getDefaultName() + " : " + ChatColor.of("#f2f0e3") + message;
			location.getBlock().getWorld().getPlayers().stream().filter(
					(player) -> location.distanceSquared(player.getLocation()) <= Math.pow(radius, 2.0D)
			).forEach(
					(player) -> player.sendMessage(localMessage)
			);
			DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), localMessage);
			Bukkit.getLogger().info(localMessage);
			return;
		}
		String globalMessage = playerInfo.getDefaultName() + " : " + ChatColor.of("#f2f0e3") + message;
		Bukkit.getOnlinePlayers().forEach((player) -> {
			if (player.getWorld() != Main.worldDark)
				player.sendMessage(ChatColor.of("#aba494") + " [WM] " + globalMessage);
		});
		DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID), globalMessage);
		DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), " [WM] " + globalMessage);
		Bukkit.getLogger().info(ChatColor.of("#aba494") + " [WM] " + globalMessage);
	}

	/**
	 * Sends private message
	 *
	 * @param sender private message sender
	 * @param receiver private message receiver
	 * @param message private message
	 *
	 * @return True if sender or receiver == null
	 */
	public static boolean sendPrivateMessage(@Nonnull PlayerInfo sender, @Nonnull PlayerInfo receiver, @Nonnull String message) {
		if (sender.getOnlinePlayer() != null && receiver.getOnlinePlayer() != null) {
			sender.getOnlinePlayer().sendMessage(" \uA015 " + ChatColor.of("#aba494") + "Вы" + " -> " + receiver.getDefaultName() + " : " + ChatColor.of("#f2f0e3") + message);
			receiver.getOnlinePlayer().sendMessage(" \uA015 " + ChatColor.of("#aba494") + sender.getDefaultName() + " -> " + "Вам" + " : " + ChatColor.of("#f2f0e3") + message);
			DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), sender.getDefaultName() + " -> " + receiver.getDefaultName() + " : " + message);
		}
		return true;
	}

	/**
	 * Sends rp event message to chat
	 *
	 * @param player player
	 * @param radius message radius
	 * @param message message
	 */
	public static boolean sendRPEventMessage(@Nonnull Player player, int radius, @Nonnull String message) {
		player.getWorld().getPlayers().stream().filter((p) -> player.getLocation().distanceSquared(p.getLocation()) <= Math.pow(radius, 2.0D)).forEach((p) -> p.sendMessage(" ꀓ " + ChatColor.GOLD + message));
		DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), message);
		Bukkit.getLogger().info(ChatColor.GOLD + message);
		return true;
	}

	/**
	 * Removes first chat from message
	 *
	 * @param message message
	 *
	 * @return message without first char
	 */
	public static String removeFirstChar(String message) {
		return message == null || message.length() == 0 ? message : message.substring(1);
	}

	/**
	 * Sends death message
	 *
	 * @param killed killed player
	 * @param killer killer player
	 */
	public static void sendDeathMessage(@Nonnull Player killed, @Nullable Player killer) {
		PlayerInfo killedInfo = new PlayerInfo(killed.getUniqueId()), killerInfo = killer != null ? new PlayerInfo(killer.getUniqueId()) : null;
		killedInfo.setLastDeathLocation();
		String deathMessage =
				killerInfo != null
				? " " + killerInfo.getGoldenName() + ChatColor.of("#ffee93") + " " + killerInfo.getPronouns().getKillMessage() + " " + killedInfo.getGoldenName()
				: " " + killedInfo.getGoldenName() + ChatColor.of("#ffee93") + " " + killedInfo.getPronouns().getDeathMessage();

		for (Player onlinePlayer : Bukkit.getOnlinePlayers())
			if (onlinePlayer.getWorld() != Main.worldDark)
				onlinePlayer.sendMessage(deathMessage);

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			ChatUtils.sendActionMessage(killed, DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID), deathMessage, 16757024);
			ChatUtils.sendActionMessage(killed, DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), deathMessage, 16757024);
		});
		Bukkit.getLogger().info(deathMessage);

		Location deathLocation = killed.getLocation();
		ChatUtils.sendInfo(null,
				"Мир и координаты смерти игрока : \"" + killedInfo.getDefaultName() + " (" + killed.getName() + ")\" : "
				+ deathLocation.getBlock().getWorld().getName() + " "
				+ deathLocation.getBlockX() + " "
				+ deathLocation.getBlockY() + " "
				+ deathLocation.getBlockZ()
		);
	}

	/**
	 * Sends join message
	 *
	 * @param playerInfo playerInfo
	 * @param player player
	 */
	public static void sendJoinMessage(@Nonnull PlayerInfo playerInfo, @Nonnull Player player) {
		String joinMessage =
				" " + playerInfo.getGoldenName() + " "
				+ ChatColor.of("#ffee93") + playerInfo.getPronouns().getJoinMessage();

		Bukkit.getScheduler().runTask(Main.plugin, () -> {
			for (Player onlinePlayer : Bukkit.getOnlinePlayers())
				if (onlinePlayer.getWorld() != Main.worldDark)
					onlinePlayer.sendMessage(joinMessage);
		});

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID), joinMessage, 65280);
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), joinMessage, 65280);
		});
		Bukkit.getLogger().info(joinMessage);
	}

	/**
	 * Sends leave message
	 *
	 * @param playerInfo playerInfo
	 * @param player player
	 */
	public static void sendLeaveMessage(@Nonnull PlayerInfo playerInfo, @Nonnull Player player) {
		if (playerInfo.hasNoName() || player.getWorld() == Main.worldDark) return;
		String leaveMessage =
				" " + playerInfo.getGoldenName() + " "
				+ ChatColor.of("#ffee93") + playerInfo.getPronouns().getQuitMessage();

		for (Player onlinePlayer : Bukkit.getOnlinePlayers())
			if (onlinePlayer.getWorld() != Main.worldDark)
				onlinePlayer.sendMessage(leaveMessage);

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID), leaveMessage, 16711680);
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), leaveMessage, 16711680);
		});
		Bukkit.getLogger().info(leaveMessage);
	}

	private static void sendActionMessage(@Nonnull Player player, @Nonnull TextChannel textChannel, @Nonnull String actionMessage, int colorRaw) {
		if (DiscordUtil.getJda() == null) return;
		DiscordUtil.queueMessage(textChannel,
				DiscordSRV.translateMessage(
						new MessageFormat(
								"",
								actionMessage,
								"",
								DiscordSRV.getAvatarUrl(player),
								"",
								"",
								"",
								"",
								"",
								"",
								"",
								null,
								colorRaw,
								null,
								false,
								DiscordUtil.getJda().getSelfUser().getEffectiveAvatarUrl(),
								DiscordSRV.getPlugin().getMainGuild() != null
										? DiscordSRV.getPlugin().getMainGuild().getSelfMember().getEffectiveName()
										: DiscordUtil.getJda().getSelfUser().getName()),
						(content, needsEscape) -> PlaceholderUtil.replacePlaceholdersToDiscord(content, player)
				), true);
	}

	@Nonnull
	public static String extractMessage(@Nonnull String[] args, int start) {
		return String.join(" ", Arrays.copyOfRange(args, start, args.length));
	}
}