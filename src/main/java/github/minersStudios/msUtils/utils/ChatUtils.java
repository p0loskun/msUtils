package github.minersStudios.msUtils.utils;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.objects.MessageFormat;
import github.scarsz.discordsrv.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
	public static boolean sendInfo(@Nullable Object target, @Nonnull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(Component.text(" ").append(message));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(Component.text(" ").append(message));
		} else {
			Bukkit.getLogger().info(plainTextSerializeComponent(message));
		}
		return true;
	}

	/**
	 * Sends fine message to target
	 *
	 * @param target target
	 * @param message warning message
	 */
	public static boolean sendFine(@Nullable Object target, @Nonnull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(Symbols.greenExclamationMark.append(message.color(NamedTextColor.GREEN)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.GREEN));
		} else {
			Bukkit.getLogger().info(plainTextSerializeComponent(message.color(NamedTextColor.GREEN)));
		}
		return true;
	}

	/**
	 * Sends warning message to target
	 *
	 * @param target target
	 * @param message warning message
	 */
	public static boolean sendWarning(@Nullable Object target, @Nonnull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(Symbols.yellowExclamationMark.append(message.color(NamedTextColor.GOLD)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.GOLD));
		} else {
			Bukkit.getLogger().warning(plainTextSerializeComponent(message));
		}
		return true;
	}

	/**
	 * Sends error message to target
	 *
	 * @param target target
	 * @param message warning message
	 */
	public static boolean sendError(@Nullable Object target, @Nonnull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(Symbols.redExclamationMark.append(message.color(NamedTextColor.RED)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.RED));
		} else {
			Bukkit.getLogger().severe(plainTextSerializeComponent(message));
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
	public static void sendMessageToChat(@Nonnull PlayerInfo playerInfo, @Nullable Location location, int radius, @Nonnull Component message) {
		if (radius > -1 && location != null) {
			Component localMessage =
					Component.text(" ")
					.append(playerInfo.getDefaultName()
					.append(Component.text(" : "))
					.color(Colors.chatColorPrimary))
					.append(message)
					.color(Colors.chatColorSecondary);
			String stringLocalMessage = plainTextSerializeComponent(localMessage);
			location.getBlock().getWorld().getPlayers().stream().filter(
					(player) -> location.distanceSquared(player.getLocation()) <= Math.pow(radius, 2.0D)
			).forEach(
					(player) -> player.sendMessage(localMessage)
			);
			DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), stringLocalMessage);
			Bukkit.getLogger().info(stringLocalMessage);
			return;
		}
		Component globalMessage =
				Component.text(" ")
				.append(Component.text("[WM] ")
				.append(playerInfo.getDefaultName()
				.append(Component.text(" : ")))
				.color(Colors.chatColorPrimary))
				.append(message)
				.color(Colors.chatColorSecondary
		);
		String stringGlobalMessage = plainTextSerializeComponent(globalMessage);
		Bukkit.getOnlinePlayers().forEach((player) -> {
			if (player.getWorld() != Main.worldDark)
				player.sendMessage(globalMessage);
		});
		DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID), stringGlobalMessage.substring(6));
		DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), stringGlobalMessage);
		Bukkit.getLogger().info(stringGlobalMessage);
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
	public static boolean sendPrivateMessage(@Nonnull PlayerInfo sender, @Nonnull PlayerInfo receiver, @Nonnull Component message) {
		if (sender.getOnlinePlayer() != null && receiver.getOnlinePlayer() != null) {
			sender.getOnlinePlayer().sendMessage(
					Symbols.speech
					.append(Component.text()
					.append(Component.text("Вы -> ")
					.append(receiver.getDefaultName()
					.append(Component.text(" : "))))
					.color(Colors.chatColorPrimary))
					.append(message.color(Colors.chatColorSecondary)));
			receiver.getOnlinePlayer().sendMessage(
					Symbols.speech
					.append(sender.getDefaultName().append(Component.text(" -> Вам : "))
					.color(Colors.chatColorPrimary))
					.append(message.color(Colors.chatColorSecondary)));
			DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID),
					ChatUtils.plainTextSerializeComponent(
						sender.getDefaultName()
						.append(Component.text(" -> "))
						.append(receiver.getDefaultName())
						.append(Component.text(" : "))
						.append(message)
					));
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
	public static boolean sendRPEventMessage(@Nonnull Player player, int radius, @Nonnull Component message) {
		Component fullMessage =
				Component.text("* ")
				.color(NamedTextColor.GOLD)
				.append(new PlayerInfo(player.getUniqueId()).getGrayIDGoldName())
				.append(Component.text(" ")
				.append(message
				.append(Component.text("*"))
				.color(NamedTextColor.GOLD)));
		player.getWorld().getPlayers().stream().filter(
				(p) -> player.getLocation().distanceSquared(p.getLocation()) <= Math.pow(radius, 2.0D))
				.forEach((p) -> p.sendMessage(Symbols.yellowExclamationMark.append(fullMessage)));
		DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), plainTextSerializeComponent(fullMessage));
		Bukkit.getLogger().warning(plainTextSerializeComponent(fullMessage));
		return true;
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
		Component deathMessage =
				killerInfo != null
				? Component.text(" ")
				.append(killerInfo.getGoldenName()
				.append(Component.text(" ")))
				.append(Component.text(killerInfo.getPronouns().getKillMessage())
				.color(Colors.joinMessageColorPrimary)
				.append(Component.text(" ")))
				.append(killedInfo.getGoldenName())
				: Component.text(" ")
				.append(killedInfo.getGoldenName()
				.append(Component.text(" ")))
				.append(Component.text(killedInfo.getPronouns().getDeathMessage()))
				.color(Colors.joinMessageColorPrimary);
		String stringDeathMessage = plainTextSerializeComponent(deathMessage);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers())
			if (onlinePlayer.getWorld() != Main.worldDark)
				onlinePlayer.sendMessage(deathMessage);

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			ChatUtils.sendActionMessage(killed, DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID), stringDeathMessage, 16757024);
			ChatUtils.sendActionMessage(killed, DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), stringDeathMessage, 16757024);
		});
		Bukkit.getLogger().info(stringDeathMessage);

		Location deathLocation = killed.getLocation();
		ChatUtils.sendInfo(null,
				Component.text("Мир и координаты смерти игрока : \"")
				.append(killedInfo.getDefaultName())
				.append(Component.text(" ("))
				.append(Component.text(killed.getName()))
				.append(Component.text(")\" : "))
				.append(Component.text(deathLocation.getBlock().getWorld().getName()))
				.append(Component.text(" "))
				.append(Component.text(deathLocation.getBlockX()))
				.append(Component.text(" "))
				.append(Component.text(deathLocation.getBlockY()))
				.append(Component.text(" "))
				.append(Component.text(deathLocation.getBlockZ())));
	}

	/**
	 * Sends join message
	 *
	 * @param playerInfo playerInfo
	 * @param player player
	 */
	public static void sendJoinMessage(@Nonnull PlayerInfo playerInfo, @Nonnull Player player) {
		Component joinMessage =
				Component.text(" ")
				.append(playerInfo.getGoldenName()
				.append(Component.text(" ")))
				.append(Component.text(playerInfo.getPronouns().getJoinMessage()))
				.color(Colors.joinMessageColorPrimary);
		String stringJoinMessage = plainTextSerializeComponent(joinMessage);

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			for (Player onlinePlayer : Bukkit.getOnlinePlayers())
				if (onlinePlayer.getWorld() != Main.worldDark)
					onlinePlayer.sendMessage(joinMessage);
		});

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID), stringJoinMessage, 65280);
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), stringJoinMessage, 65280);
		});
		Bukkit.getLogger().info(stringJoinMessage);
	}

	/**
	 * Sends leave message
	 *
	 * @param playerInfo playerInfo
	 * @param player player
	 */
	public static void sendQuitMessage(@Nonnull PlayerInfo playerInfo, @Nonnull Player player) {
		if (playerInfo.hasNoName() || player.getWorld() == Main.worldDark) return;
		Component quitMessage =
				Component.text(" ")
				.append(playerInfo.getGoldenName()
				.append(Component.text(" ")))
				.append(Component.text(playerInfo.getPronouns().getQuitMessage()))
				.color(Colors.joinMessageColorPrimary
		);
		String stringQuitMessage = plainTextSerializeComponent(quitMessage);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers())
			if (onlinePlayer.getWorld() != Main.worldDark)
				onlinePlayer.sendMessage(quitMessage);

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID), stringQuitMessage, 16711680);
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), stringQuitMessage, 16711680);
		});
		Bukkit.getLogger().info(stringQuitMessage);
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

	@Nonnull
	public static String plainTextSerializeComponent(@Nonnull Component component) {
		return PlainTextComponentSerializer.plainText().serialize(component);
	}
}