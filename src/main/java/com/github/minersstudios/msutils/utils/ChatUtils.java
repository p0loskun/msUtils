package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.classes.Chat;
import com.github.minersstudios.msutils.classes.ChatBuffer;
import com.github.minersstudios.msutils.classes.PlayerInfo;
import com.github.minersstudios.msutils.configs.ConfigCache;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.objects.MessageFormat;
import github.scarsz.discordsrv.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatUtils {

	/**
	 * Sends info message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendInfo(@Nullable Object target, @Nonnull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(Component.text(" ").append(message));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(Component.text(" ").append(message));
		} else {
			Bukkit.getLogger().info(legacyComponentSerialize(message));
		}
		return true;
	}

	/**
	 * Sends fine message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendFine(@Nullable Object target, @Nonnull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(ConfigCache.Symbols.GREEN_EXCLAMATION_MARK.append(message.color(NamedTextColor.GREEN)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.GREEN));
		} else {
			Bukkit.getLogger().info(legacyComponentSerialize(message.color(NamedTextColor.GREEN)));
		}
		return true;
	}

	/**
	 * Sends warning message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendWarning(@Nullable Object target, @Nonnull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(ConfigCache.Symbols.YELLOW_EXCLAMATION_MARK.append(message.color(NamedTextColor.GOLD)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.GOLD));
		} else {
			Bukkit.getLogger().warning(legacyComponentSerialize(message.color(NamedTextColor.GOLD)));
		}
		return true;
	}

	/**
	 * Sends error message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendError(@Nullable Object target, @Nonnull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(ConfigCache.Symbols.RED_EXCLAMATION_MARK.append(message.color(NamedTextColor.RED)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.RED));
		} else {
			Bukkit.getLogger().severe(legacyComponentSerialize(message.color(NamedTextColor.RED)));
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
	public static void sendMessageToChat(@Nonnull Player sender, @Nonnull PlayerInfo playerInfo, @Nullable Location location, @Nonnull Chat chat, @Nonnull Component message) {
		Component fullMessage =
				Component.text(" ")
						.append(Component.text(chat.getPrefix())
						.append(playerInfo.getDefaultName()
						.append(Component.text(" : ")))
						.color(chat.getPrimaryColor())
						.hoverEvent(HoverEvent.showText(Component.text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
						.clickEvent(ClickEvent.suggestCommand("/msg " + playerInfo.getID() + " ")))
						.append(message)
						.color(chat.getSecondaryColor());
		String stringFullMessage = legacyComponentSerialize(fullMessage);

		if (chat.getRadius() == -1.0d) {
			Bukkit.getOnlinePlayers().forEach((player) -> {
				if (player.getWorld() != Main.getWorldDark()) {
					for (String permission : chat.getPermissions()) {
						if (player.hasPermission(permission)) {
							player.sendMessage(fullMessage);
						}
					}
				}
			});
		} else if (location != null) {
			location.getBlock().getWorld().getPlayers().stream().filter(
					(player) -> location.distanceSquared(player.getLocation()) <= Math.pow(chat.getRadius(), 2.0d)
			).forEach(
					(player) -> {
						for (String permission : chat.getPermissions()) {
							if (player.hasPermission(permission)) {
								player.sendMessage(fullMessage);
							}
						}
					}
			);
		}

		if (chat.isShowMessagesAboveHead()) {
			ChatBuffer.receiveMessage(sender, legacyComponentSerialize(ConfigCache.Symbols.SPEECH.append(message)) + " ");
		}

		if (Main.isDiscordSRVEnabled && chat.isDiscordEnabled()) {
			for (String discordChannelId : chat.getDiscordChannelIds()) {
				DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(discordChannelId), stringFullMessage);
			}
		}
		Bukkit.getLogger().info(stringFullMessage);
	}

	/**
	 * Sends private message
	 *
	 * @param sender   private message sender
	 * @param receiver private message receiver
	 * @param message  private message
	 * @return True if sender or receiver == null
	 */
	public static boolean sendPrivateMessage(@Nonnull PlayerInfo sender, @Nonnull PlayerInfo receiver, @Nonnull Component message) {
		if (sender.getOnlinePlayer() != null && receiver.getOnlinePlayer() != null) {
			String privateMessage = legacyComponentSerialize(
					Component.text(" ")
							.append(sender.getDefaultName()
							.append(Component.text(" -> ")
							.append(receiver.getDefaultName()
							.append(Component.text(" : ")))))
							.color(Chat.privateMessagesChat.getPrimaryColor())
							.append(message.color(Chat.privateMessagesChat.getSecondaryColor()))
			);
			sender.getOnlinePlayer().sendMessage(
					ConfigCache.Symbols.SPEECH
							.append(Component.text()
							.append(Component.text("Вы -> ")
							.append(receiver.getDefaultName()
							.append(Component.text(" : ")))
							.hoverEvent(HoverEvent.showText(Component.text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
							.clickEvent(ClickEvent.suggestCommand("/msg " + receiver.getID() + " ")))
							.color(Chat.privateMessagesChat.getPrimaryColor()))
							.append(message.color(Chat.privateMessagesChat.getSecondaryColor()))
			);
			receiver.getOnlinePlayer().sendMessage(
					ConfigCache.Symbols.SPEECH
							.append(sender.getDefaultName().append(Component.text(" -> Вам : "))
							.color(Chat.privateMessagesChat.getPrimaryColor())
							.hoverEvent(HoverEvent.showText(Component.text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
							.clickEvent(ClickEvent.suggestCommand("/msg " + sender.getID() + " ")))
							.append(message.color(Chat.privateMessagesChat.getSecondaryColor()))
			);
			if (Main.isDiscordSRVEnabled && Chat.privateMessagesChat.isDiscordEnabled()) {
				for (String discordChannelId : Chat.privateMessagesChat.getDiscordChannelIds()) {
					DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(discordChannelId), privateMessage);
				}
			}
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
	public static boolean sendRPEventMessage(@Nonnull Player player, @Nullable Component speech, @Nonnull Component action, @Nonnull RolePlayActionType rolePlayActionType) {
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		Component fullMessage;
		if (rolePlayActionType == RolePlayActionType.DO) {
			fullMessage =
					Component.text("* ", Chat.actionsChat.getPrimaryColor())
							.append(action.color(Chat.actionsChat.getSecondaryColor()))
							.append(Component.text(" * | ", Chat.actionsChat.getPrimaryColor()))
							.append(playerInfo.getGrayIDGoldName());
		} else if (rolePlayActionType == RolePlayActionType.IT) {
			fullMessage =
					Component.text("* ", Chat.actionsChat.getPrimaryColor())
							.append(action.color(Chat.actionsChat.getSecondaryColor()))
							.append(Component.text(" *", Chat.actionsChat.getPrimaryColor()));
		} else if (rolePlayActionType == RolePlayActionType.TODO && speech != null) {
			fullMessage =
					Component.text("* ")
							.color(Chat.actionsChat.getPrimaryColor())
							.append(speech
							.color(Chat.actionsChat.getSecondaryColor()))
							.append(Component.text(" - ")
							.append(Component.text(playerInfo.getPronouns().getSaidMessage())))
							.color(Chat.actionsChat.getPrimaryColor())
							.append(Component.text(" "))
							.append(playerInfo.getGrayIDGoldName())
							.append(Component.text(", ", Chat.actionsChat.getPrimaryColor()))
							.append(action
							.color(Chat.actionsChat.getSecondaryColor()))
							.append(Component.text(" *", Chat.actionsChat.getPrimaryColor()));
		} else {
			fullMessage =
					Component.text("* ", Chat.actionsChat.getPrimaryColor())
							.append(playerInfo.getGrayIDGoldName())
							.append(Component.text(" ")
							.append(action.color(Chat.actionsChat.getSecondaryColor())))
							.append(Component.text(" *", Chat.actionsChat.getPrimaryColor()));
		}
		String stringFullMessage = legacyComponentSerialize(fullMessage);
		player.getWorld().getPlayers().stream().filter(
				(p) -> player.getLocation().distanceSquared(p.getLocation()) <= Math.pow(Main.getConfigCache().local_chat_radius, 2.0D)
		).forEach(
				(p) -> p.sendMessage(ConfigCache.Symbols.YELLOW_EXCLAMATION_MARK.append(fullMessage))
		);
		if (Main.isDiscordSRVEnabled && Chat.actionsChat.isDiscordEnabled()) {
			for (String discordChannelId : Chat.actionsChat.getDiscordChannelIds()) {
				DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(discordChannelId), stringFullMessage);
			}
		}
		Bukkit.getLogger().info(stringFullMessage);
		return true;
	}

	public static boolean sendRPEventMessage(@Nonnull Player player, @Nonnull Component action, @Nonnull RolePlayActionType rolePlayActionType) {
		return sendRPEventMessage(player, null, action, rolePlayActionType);
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
						.color(Chat.deathChat.getPrimaryColor())
						.append(Component.text(" ")))
						.append(killedInfo.getGoldenName())
						: Component.text(" ")
						.append(killedInfo.getGoldenName()
						.append(Component.text(" ")))
						.append(Component.text(killedInfo.getPronouns().getDeathMessage()))
						.color(Chat.deathChat.getPrimaryColor());
		String stringDeathMessage = legacyComponentSerialize(deathMessage);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getWorld() != Main.getWorldDark()) {
				onlinePlayer.sendMessage(deathMessage);
			}
		}

		Bukkit.getLogger().info(stringDeathMessage);

		if (Main.isDiscordSRVEnabled && Chat.deathChat.isDiscordEnabled()) {
			for (String discordChannelId : Chat.deathChat.getDiscordChannelIds()) {
				sendActionMessage(killed, DiscordUtil.getTextChannelById(discordChannelId), stringDeathMessage, 16757024);
			}
		}

		Location deathLocation = killed.getLocation();
		sendInfo(null,
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
	public static void sendJoinMessage(@Nonnull PlayerInfo playerInfo, @Nonnull Player player) {
		Component joinMessage =
				Component.text(" ")
						.append(playerInfo.getGoldenName()
						.append(Component.text(" ")))
						.append(Component.text(playerInfo.getPronouns().getJoinMessage()))
						.color(Chat.joinChat.getPrimaryColor());
		String stringJoinMessage = legacyComponentSerialize(joinMessage);

		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				if (onlinePlayer.getWorld() != Main.getWorldDark()) {
					onlinePlayer.sendMessage(joinMessage);
				}
			}
		});
		if (Main.isDiscordSRVEnabled && Chat.joinChat.isDiscordEnabled()) {
			for (String discordChannelId : Chat.joinChat.getDiscordChannelIds()) {
				sendActionMessage(player, DiscordUtil.getTextChannelById(discordChannelId), stringJoinMessage, 65280);
			}
		}
		Bukkit.getLogger().info(stringJoinMessage);
	}

	/**
	 * Sends leave message
	 *
	 * @param playerInfo playerInfo
	 * @param player     player
	 */
	public static void sendQuitMessage(@Nonnull PlayerInfo playerInfo, @Nonnull Player player) {
		if (playerInfo.hasNoName() || player.getWorld() == Main.getWorldDark()) return;
		Component quitMessage =
				Component.text(" ")
						.append(playerInfo.getGoldenName()
						.append(Component.text(" ")))
						.append(Component.text(playerInfo.getPronouns().getQuitMessage()))
						.color(Chat.quitChat.getPrimaryColor());
		String stringQuitMessage = legacyComponentSerialize(quitMessage);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getWorld() != Main.getWorldDark()) {
				onlinePlayer.sendMessage(quitMessage);
			}
		}

		if (Main.isDiscordSRVEnabled && Chat.quitChat.isDiscordEnabled()) {
			for (String discordChannelId : Chat.quitChat.getDiscordChannelIds()) {
				sendActionMessage(player, DiscordUtil.getTextChannelById(discordChannelId), stringQuitMessage, 16711680);
			}
		}
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
	public static String extractMessage(int start, @Nonnull String... args) {
		return String.join(" ", Arrays.copyOfRange(args, start, args.length));
	}

	@Nonnull
	public static String legacyComponentSerialize(@Nonnull Component component) {
		return LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build().serialize(component);
	}

	public enum RolePlayActionType {DO, IT, ME, TODO}
}
