package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.classes.PlayerInfo;
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
	public static void sendMessageToChat(@Nonnull PlayerInfo playerInfo, @Nullable Location location, Chat chat, @Nonnull Component message) {
		if (chat == Chat.LOCAL && location != null) {
			Component localMessage =
					Component.text(" ")
							.append(playerInfo.getDefaultName()
							.append(Component.text(" : "))
							.color(ConfigCache.Colors.CHAT_COLOR_PRIMARY)
							.hoverEvent(HoverEvent.showText(Component.text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
							.clickEvent(ClickEvent.suggestCommand("/pm " + playerInfo.getID() + " ")))
							.append(message)
							.color(ConfigCache.Colors.CHAT_COLOR_SECONDARY);
			String stringLocalMessage = legacyComponentSerialize(localMessage);
			location.getBlock().getWorld().getPlayers().stream().filter(
					(player) -> location.distanceSquared(player.getLocation()) <= Math.pow(Main.getConfigCache().local_chat_radius, 2.0d)
			).forEach(
					(player) -> player.sendMessage(localMessage)
			);
			Bukkit.getLogger().info(stringLocalMessage);
			if (Main.isDiscordSRVEnabled && Main.getConfigCache().send_local_messages_to_discord) {
				DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(Main.getConfigCache().discord_local_channel_id), stringLocalMessage);
			}
			return;
		}
		Component globalMessage =
				Component.text(" ")
						.append(Component.text("[WM] ")
						.append(playerInfo.getDefaultName()
						.append(Component.text(" : ")))
						.color(ConfigCache.Colors.CHAT_COLOR_PRIMARY)
						.hoverEvent(HoverEvent.showText(Component.text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
						.clickEvent(ClickEvent.suggestCommand("/pm " + playerInfo.getID() + " ")))
						.append(message)
						.color(ConfigCache.Colors.CHAT_COLOR_SECONDARY);
		String stringGlobalMessage = legacyComponentSerialize(globalMessage);
		Bukkit.getOnlinePlayers().forEach((player) -> {
			if (player.getWorld() != Main.getWorldDark()) {
				player.sendMessage(globalMessage);
			}
		});
		Bukkit.getLogger().info(stringGlobalMessage);
		if (Main.isDiscordSRVEnabled) {
			if (Main.getConfigCache().send_global_messages_to_discord) {
				DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(Main.getConfigCache().discord_global_channel_id), stringGlobalMessage.replaceFirst("\\[WM]", ""));
			}
			if (Main.getConfigCache().send_local_messages_to_discord) {
				DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(Main.getConfigCache().discord_local_channel_id), stringGlobalMessage);
			}
		}
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
			String privateMessage = ChatUtils.legacyComponentSerialize(
					Component.text(" ")
							.append(sender.getDefaultName()
							.append(Component.text(" -> ")
							.append(receiver.getDefaultName()
							.append(Component.text(" : ")))))
							.color(ConfigCache.Colors.CHAT_COLOR_PRIMARY)
							.append(message.color(ConfigCache.Colors.CHAT_COLOR_SECONDARY))
			);
			sender.getOnlinePlayer().sendMessage(
					ConfigCache.Symbols.SPEECH
							.append(Component.text()
							.append(Component.text("Вы -> ")
							.append(receiver.getDefaultName()
							.append(Component.text(" : ")))
							.hoverEvent(HoverEvent.showText(Component.text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
							.clickEvent(ClickEvent.suggestCommand("/pm " + receiver.getID() + " ")))
							.color(ConfigCache.Colors.CHAT_COLOR_PRIMARY))
							.append(message.color(ConfigCache.Colors.CHAT_COLOR_SECONDARY))
			);
			receiver.getOnlinePlayer().sendMessage(
					ConfigCache.Symbols.SPEECH
							.append(sender.getDefaultName().append(Component.text(" -> Вам : "))
							.color(ConfigCache.Colors.CHAT_COLOR_PRIMARY)
							.hoverEvent(HoverEvent.showText(Component.text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
							.clickEvent(ClickEvent.suggestCommand("/pm " + sender.getID() + " ")))
							.append(message.color(ConfigCache.Colors.CHAT_COLOR_SECONDARY))
			);
			if (Main.isDiscordSRVEnabled && Main.getConfigCache().send_local_messages_to_discord) {
				DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(Main.getConfigCache().discord_local_channel_id), privateMessage);
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
					Component.text("* ", ConfigCache.Colors.RP_MESSAGE_COLOR_PRIMARY)
							.append(action.color(ConfigCache.Colors.RP_MESSAGE_COLOR_SECONDARY))
							.append(Component.text(" * | ", ConfigCache.Colors.RP_MESSAGE_COLOR_PRIMARY))
							.append(playerInfo.getGrayIDGoldName());
		} else if (rolePlayActionType == RolePlayActionType.IT) {
			fullMessage =
					Component.text("* ", ConfigCache.Colors.RP_MESSAGE_COLOR_PRIMARY)
							.append(action.color(ConfigCache.Colors.RP_MESSAGE_COLOR_SECONDARY))
							.append(Component.text(" *", ConfigCache.Colors.RP_MESSAGE_COLOR_PRIMARY));
		} else if (rolePlayActionType == RolePlayActionType.TODO && speech != null) {
			fullMessage =
					Component.text("* ")
							.color(ConfigCache.Colors.RP_MESSAGE_COLOR_PRIMARY)
							.append(speech
							.color(ConfigCache.Colors.RP_MESSAGE_COLOR_SECONDARY))
							.append(Component.text(" - ")
							.append(Component.text(playerInfo.getPronouns().getSaidMessage())))
							.color(ConfigCache.Colors.RP_MESSAGE_COLOR_PRIMARY)
							.append(Component.text(" "))
							.append(playerInfo.getGrayIDGoldName())
							.append(Component.text(", ", ConfigCache.Colors.RP_MESSAGE_COLOR_PRIMARY))
							.append(action
							.color(ConfigCache.Colors.RP_MESSAGE_COLOR_SECONDARY))
							.append(Component.text(" *", ConfigCache.Colors.RP_MESSAGE_COLOR_PRIMARY));
		} else {
			fullMessage =
					Component.text("* ", ConfigCache.Colors.RP_MESSAGE_COLOR_PRIMARY)
							.append(playerInfo.getGrayIDGoldName())
							.append(Component.text(" ")
							.append(action.color(ConfigCache.Colors.RP_MESSAGE_COLOR_SECONDARY)))
							.append(Component.text(" *", ConfigCache.Colors.RP_MESSAGE_COLOR_PRIMARY));
		}
		player.getWorld().getPlayers().stream().filter(
				(p) -> player.getLocation().distanceSquared(p.getLocation()) <= Math.pow(Main.getConfigCache().local_chat_radius, 2.0D)
		).forEach(
				(p) -> p.sendMessage(ConfigCache.Symbols.YELLOW_EXCLAMATION_MARK.append(fullMessage))
		);
		if (Main.isDiscordSRVEnabled && Main.getConfigCache().send_local_messages_to_discord) {
			DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(Main.getConfigCache().discord_local_channel_id), legacyComponentSerialize(fullMessage));
		}
		Bukkit.getLogger().info(legacyComponentSerialize(fullMessage));
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
						.color(ConfigCache.Colors.JOIN_MESSAGE_COLOR_PRIMARY)
						.append(Component.text(" ")))
						.append(killedInfo.getGoldenName())
						: Component.text(" ")
						.append(killedInfo.getGoldenName()
						.append(Component.text(" ")))
						.append(Component.text(killedInfo.getPronouns().getDeathMessage()))
						.color(ConfigCache.Colors.JOIN_MESSAGE_COLOR_PRIMARY);
		String stringDeathMessage = legacyComponentSerialize(deathMessage);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getWorld() != Main.getWorldDark()) {
				onlinePlayer.sendMessage(deathMessage);
			}
		}

		Bukkit.getLogger().info(stringDeathMessage);
		if (Main.isDiscordSRVEnabled) {
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
				if (Main.getConfigCache().send_global_messages_to_discord) {
					ChatUtils.sendActionMessage(killed, DiscordUtil.getTextChannelById(Main.getConfigCache().discord_global_channel_id), stringDeathMessage, 16757024);
				}
				if (Main.getConfigCache().send_local_messages_to_discord) {
					ChatUtils.sendActionMessage(killed, DiscordUtil.getTextChannelById(Main.getConfigCache().discord_local_channel_id), stringDeathMessage, 16757024);
				}
			});
		}

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
	public static void sendJoinMessage(@Nonnull PlayerInfo playerInfo, @Nonnull Player player) {
		Component joinMessage =
				Component.text(" ")
						.append(playerInfo.getGoldenName()
						.append(Component.text(" ")))
						.append(Component.text(playerInfo.getPronouns().getJoinMessage()))
						.color(ConfigCache.Colors.JOIN_MESSAGE_COLOR_PRIMARY);
		String stringJoinMessage = legacyComponentSerialize(joinMessage);

		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				if (onlinePlayer.getWorld() != Main.getWorldDark()) {
					onlinePlayer.sendMessage(joinMessage);
				}
			}
		});

		if (Main.isDiscordSRVEnabled) {
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
				if (Main.getConfigCache().send_global_messages_to_discord) {
					ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(Main.getConfigCache().discord_global_channel_id), stringJoinMessage, 65280);
				}
				if (Main.getConfigCache().send_local_messages_to_discord) {
					ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(Main.getConfigCache().discord_local_channel_id), stringJoinMessage, 65280);
				}
			});
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
						.color(ConfigCache.Colors.JOIN_MESSAGE_COLOR_PRIMARY);
		String stringQuitMessage = legacyComponentSerialize(quitMessage);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getWorld() != Main.getWorldDark()) {
				onlinePlayer.sendMessage(quitMessage);
			}
		}
		if (Main.isDiscordSRVEnabled) {
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
				if (Main.getConfigCache().send_global_messages_to_discord) {
					ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(Main.getConfigCache().discord_global_channel_id), stringQuitMessage, 16711680);
				}
				if (Main.getConfigCache().send_local_messages_to_discord) {
					ChatUtils.sendActionMessage(player, DiscordUtil.getTextChannelById(Main.getConfigCache().discord_local_channel_id), stringQuitMessage, 16711680);
				}
			});
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

	public enum Chat {GLOBAL, LOCAL}

	public enum RolePlayActionType {DO, IT, ME, TODO}
}
