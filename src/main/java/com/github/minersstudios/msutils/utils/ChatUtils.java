package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.player.PlayerInfo;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.objects.MessageFormat;
import github.scarsz.discordsrv.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.minersstudios.msutils.utils.ChatUtils.Colors.*;
import static com.github.minersstudios.msutils.utils.ChatUtils.Symbols.*;

public final class ChatUtils {
	public static final Style DEFAULT_STYLE = Style.style(
			NamedTextColor.WHITE,
			TextDecoration.OBFUSCATED.withState(false),
			TextDecoration.BOLD.withState(false),
			TextDecoration.ITALIC.withState(false),
			TextDecoration.STRIKETHROUGH.withState(false),
			TextDecoration.UNDERLINED.withState(false)
	);

	private ChatUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Sends info message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendInfo(@Nullable Object target, @NotNull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(Component.text(" ").append(message));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(Component.text(" ").append(message));
		} else {
			Bukkit.getLogger().info(convertComponentToString(message));
		}
		return true;
	}

	/**
	 * Sends fine message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendFine(@Nullable Object target, @NotNull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(GREEN_EXCLAMATION_MARK.append(message.color(NamedTextColor.GREEN)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.GREEN));
		} else {
			Bukkit.getLogger().info(convertComponentToString(message.color(NamedTextColor.GREEN)));
		}
		return true;
	}

	/**
	 * Sends warning message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendWarning(@Nullable Object target, @NotNull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(YELLOW_EXCLAMATION_MARK.append(message.color(NamedTextColor.GOLD)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.GOLD));
		} else {
			Bukkit.getLogger().warning(convertComponentToString(message.color(NamedTextColor.GOLD)));
		}
		return true;
	}

	/**
	 * Sends error message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendError(@Nullable Object target, @NotNull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(RED_EXCLAMATION_MARK.append(message.color(NamedTextColor.RED)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.RED));
		} else {
			Bukkit.getLogger().severe(convertComponentToString(message.color(NamedTextColor.RED)));
		}
		return true;
	}

	/**
	 * Sends message to console with plugin name
	 *
	 * @param level log level
	 * @param message message
	 */
	public static void log(@Nonnull Level level, @Nonnull String message) {
		Logger.getLogger(Main.getInstance().getName()).log(level, message);
	}

	/**
	 * Sends message to chat
	 *
	 * @param playerInfo player info
	 * @param location   sender location
	 * @param chat       chat
	 * @param message    message
	 */
	public static void sendMessageToChat(@NotNull PlayerInfo playerInfo, @Nullable Location location, Chat chat, @NotNull Component message) {
		if (chat == Chat.LOCAL && location != null) {
			Component localMessage =
					Component.text(" ")
							.append(playerInfo.getDefaultName()
							.append(Component.text(" : "))
							.color(CHAT_COLOR_PRIMARY)
							.hoverEvent(HoverEvent.showText(Component.text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
							.clickEvent(ClickEvent.suggestCommand("/pm " + playerInfo.getID() + " ")))
							.append(message)
							.color(CHAT_COLOR_SECONDARY);
			String stringLocalMessage = convertComponentToString(localMessage);
			location.getBlock().getWorld().getPlayers().stream().filter(
					(player) -> location.distanceSquared(player.getLocation()) <= Math.pow(Main.getCachedConfig().local_chat_radius, 2.0d)
			).forEach(
					(player) -> player.sendMessage(localMessage)
			);
			DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(Main.getCachedConfig().discordLocalChannelId), stringLocalMessage);
			Bukkit.getLogger().info(stringLocalMessage);
			return;
		}
		Component globalMessage =
				Component.text(" ")
						.append(Component.text("[WM] ")
						.append(playerInfo.getDefaultName()
						.append(Component.text(" : ")))
						.color(CHAT_COLOR_PRIMARY)
						.hoverEvent(HoverEvent.showText(Component.text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
						.clickEvent(ClickEvent.suggestCommand("/pm " + playerInfo.getID() + " ")))
						.append(message)
						.color(CHAT_COLOR_SECONDARY);
		String stringGlobalMessage = convertComponentToString(globalMessage);
		Bukkit.getOnlinePlayers().forEach((player) -> {
			if (player.getWorld() != Main.getWorldDark()) {
				player.sendMessage(globalMessage);
			}
		});
		DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(Main.getCachedConfig().discordGlobalChannelId), stringGlobalMessage.replaceFirst("\\[WM]", ""));
		DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(Main.getCachedConfig().discordLocalChannelId), stringGlobalMessage);
		Bukkit.getLogger().info(stringGlobalMessage);
	}

	/**
	 * Sends private message
	 *
	 * @param sender   private message sender
	 * @param receiver private message receiver
	 * @param message  private message
	 * @return True if sender or receiver == null
	 */
	public static boolean sendPrivateMessage(@NotNull PlayerInfo sender, @NotNull PlayerInfo receiver, @NotNull Component message) {
		if (sender.getOnlinePlayer() != null && receiver.getOnlinePlayer() != null) {
			String privateMessage = ChatUtils.convertComponentToString(
					Component.text(" ")
							.append(sender.getDefaultName()
							.append(Component.text(" -> ")
							.append(receiver.getDefaultName()
							.append(Component.text(" : ")))))
							.color(CHAT_COLOR_PRIMARY)
							.append(message.color(CHAT_COLOR_SECONDARY))
			);
			sender.getOnlinePlayer().sendMessage(
					SPEECH.append(Component.text()
							.append(Component.text("Вы -> ")
							.append(receiver.getDefaultName()
							.append(Component.text(" : ")))
							.hoverEvent(HoverEvent.showText(Component.text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
							.clickEvent(ClickEvent.suggestCommand("/pm " + receiver.getID() + " ")))
							.color(CHAT_COLOR_PRIMARY))
							.append(message.color(CHAT_COLOR_SECONDARY))
			);
			receiver.getOnlinePlayer().sendMessage(
					SPEECH.append(sender.getDefaultName().append(Component.text(" -> Вам : "))
							.color(CHAT_COLOR_PRIMARY)
							.hoverEvent(HoverEvent.showText(Component.text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
							.clickEvent(ClickEvent.suggestCommand("/pm " + sender.getID() + " ")))
							.append(message.color(CHAT_COLOR_SECONDARY))
			);
			DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(Main.getCachedConfig().discordLocalChannelId), privateMessage);
			return sendInfo(null, Component.text(privateMessage));
		}
		return false;
	}

	/**
	 * Sends rp event message to chat
	 *
	 * @param player  player
	 * @param speech speech
	 * @param action action
	 * @param rolePlayActionType rp action type
	 */
	public static boolean sendRPEventMessage(@NotNull Player player, @Nullable Component speech, @NotNull Component action, @NotNull RolePlayActionType rolePlayActionType) {
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		Component fullMessage;
		if (rolePlayActionType == RolePlayActionType.DO) {
			fullMessage =
					Component.text("* ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
							.append(action.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
							.append(Component.text(" * | ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY))
							.append(playerInfo.getGrayIDGoldName());
		} else if (rolePlayActionType == RolePlayActionType.IT) {
			fullMessage =
					Component.text("* ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
							.append(action.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
							.append(Component.text(" *", RP_MESSAGE_MESSAGE_COLOR_PRIMARY));
		} else if (rolePlayActionType == RolePlayActionType.TODO && speech != null) {
			fullMessage =
					Component.text("* ")
							.color(RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
							.append(speech
							.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
							.append(Component.text(" - ")
							.append(Component.text(playerInfo.getPronouns().getSaidMessage())))
							.color(RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
							.append(Component.text(" "))
							.append(playerInfo.getGrayIDGoldName())
							.append(Component.text(", ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY))
							.append(action
							.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
							.append(Component.text(" *", RP_MESSAGE_MESSAGE_COLOR_PRIMARY));
		} else {
			fullMessage =
					Component.text("* ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
							.append(playerInfo.getGrayIDGoldName())
							.append(Component.text(" ")
							.append(action.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY)))
							.append(Component.text(" *", RP_MESSAGE_MESSAGE_COLOR_PRIMARY));
		}
		player.getWorld().getPlayers().stream().filter(
				(p) -> player.getLocation().distanceSquared(p.getLocation()) <= Math.pow(Main.getCachedConfig().local_chat_radius, 2.0D)
		).forEach(
				(p) -> p.sendMessage(YELLOW_EXCLAMATION_MARK.append(fullMessage))
		);
		DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(Main.getCachedConfig().discordLocalChannelId), convertComponentToString(fullMessage));
		Bukkit.getLogger().info(convertComponentToString(fullMessage));
		return true;
	}

	public static boolean sendRPEventMessage(@NotNull Player player, @NotNull Component action, @NotNull RolePlayActionType rolePlayActionType) {
		return sendRPEventMessage(player, null, action, rolePlayActionType);
	}

	/**
	 * Sends death message
	 *
	 * @param killed killed player
	 * @param killer killer player
	 */
	public static void sendDeathMessage(@NotNull Player killed, @Nullable Player killer) {
		PlayerInfo killedInfo = new PlayerInfo(killed.getUniqueId()), killerInfo = killer != null ? new PlayerInfo(killer.getUniqueId()) : null;
		killedInfo.setLastDeathLocation();
		Component deathMessage =
				killerInfo != null
						? Component.text(" ")
						.append(killerInfo.getGoldenName()
						.append(Component.text(" ")))
						.append(Component.text(killerInfo.getPronouns().getKillMessage())
						.color(JOIN_MESSAGE_COLOR_PRIMARY)
						.append(Component.text(" ")))
						.append(killedInfo.getGoldenName())
						: Component.text(" ")
						.append(killedInfo.getGoldenName()
						.append(Component.text(" ")))
						.append(Component.text(killedInfo.getPronouns().getDeathMessage()))
						.color(JOIN_MESSAGE_COLOR_PRIMARY);
		String stringDeathMessage = convertComponentToString(deathMessage);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getWorld() != Main.getWorldDark()) {
				onlinePlayer.sendMessage(deathMessage);
			}
		}

		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			ChatUtils.sendActionMessage(killed, DiscordUtil.getTextChannelById(Main.getCachedConfig().discordGlobalChannelId), stringDeathMessage, 16757024);
			ChatUtils.sendActionMessage(killed, DiscordUtil.getTextChannelById(Main.getCachedConfig().discordLocalChannelId), stringDeathMessage, 16757024);
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
						.append(Component.text(deathLocation.getBlockZ()))
		);
	}

	/**
	 * Sends join message
	 *
	 * @param playerInfo playerInfo
	 * @param player     player
	 */
	public static void sendJoinMessage(@NotNull PlayerInfo playerInfo, @NotNull Player player) {
		Component joinMessage =
				Component.text(" ")
						.append(playerInfo.getGoldenName()
						.append(Component.text(" ")))
						.append(Component.text(playerInfo.getPronouns().getJoinMessage()))
						.color(JOIN_MESSAGE_COLOR_PRIMARY);
		String stringJoinMessage = convertComponentToString(joinMessage);

		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				if (onlinePlayer.getWorld() != Main.getWorldDark()) {
					onlinePlayer.sendMessage(joinMessage);
				}
			}
		});

		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(Main.getCachedConfig().discordGlobalChannelId), stringJoinMessage, 65280);
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(Main.getCachedConfig().discordLocalChannelId), stringJoinMessage, 65280);
		});
		Bukkit.getLogger().info(stringJoinMessage);
	}

	/**
	 * Sends leave message
	 *
	 * @param playerInfo playerInfo
	 * @param player     player
	 */
	public static void sendQuitMessage(@NotNull PlayerInfo playerInfo, @NotNull Player player) {
		if (playerInfo.hasNoName() || player.getWorld() == Main.getWorldDark()) return;
		Component quitMessage =
				Component.text(" ")
						.append(playerInfo.getGoldenName()
						.append(Component.text(" ")))
						.append(Component.text(playerInfo.getPronouns().getQuitMessage()))
						.color(JOIN_MESSAGE_COLOR_PRIMARY);
		String stringQuitMessage = convertComponentToString(quitMessage);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getWorld() != Main.getWorldDark()) {
				onlinePlayer.sendMessage(quitMessage);
			}
		}

		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(Main.getCachedConfig().discordGlobalChannelId), stringQuitMessage, 16711680);
			ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(Main.getCachedConfig().discordLocalChannelId), stringQuitMessage, 16711680);
		});
		Bukkit.getLogger().info(stringQuitMessage);
	}

	private static void sendActionMessage(@NotNull Player player, @NotNull TextChannel textChannel, @NotNull String actionMessage, int colorRaw) {
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

	public static @NotNull String extractMessage(@NotNull String[] args, int start) {
		return String.join(" ", Arrays.copyOfRange(args, start, args.length));
	}

	public static @NotNull String convertComponentToString(@NotNull Component component) {
		return LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build().serialize(component);
	}

	public static @NotNull Component createDefaultStyledName(@NotNull String name) {
		return Component.text().append(Component.text(name).style(ChatUtils.DEFAULT_STYLE)).build();
	}

	public enum Chat {GLOBAL, LOCAL}

	public enum RolePlayActionType {DO, IT, ME, TODO}

	public static class Colors {
		public static final TextColor
				CHAT_COLOR_PRIMARY = TextColor.color(171, 164, 148),
				CHAT_COLOR_SECONDARY = TextColor.color(241, 240, 227),
				JOIN_MESSAGE_COLOR_PRIMARY = TextColor.color(255, 238, 147),
				JOIN_MESSAGE_COLOR_SECONDARY = TextColor.color(252, 245, 199),
				RP_MESSAGE_MESSAGE_COLOR_PRIMARY = TextColor.color(255, 170, 0),
				RP_MESSAGE_MESSAGE_COLOR_SECONDARY = TextColor.color(255, 195, 105);
	}

	public static class Symbols {
		public static final Component
				GREEN_EXCLAMATION_MARK = Component.text(" ꀒ "),
				YELLOW_EXCLAMATION_MARK = Component.text(" ꀓ "),
				RED_EXCLAMATION_MARK = Component.text(" ꀑ "),
				SPEECH = Component.text(" ꀕ "),
				DISCORD = Component.text(" ꀔ ");
	}
}
