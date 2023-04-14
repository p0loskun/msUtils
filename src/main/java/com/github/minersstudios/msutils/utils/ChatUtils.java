package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.mscore.utils.Badges;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.objects.MessageFormat;
import github.scarsz.discordsrv.util.DiscordUtil;
import github.scarsz.discordsrv.util.PlaceholderUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.minersstudios.mscore.utils.ChatUtils.sendInfo;
import static com.github.minersstudios.mscore.utils.ChatUtils.serializeLegacyComponent;
import static com.github.minersstudios.msutils.MSUtils.getConfigCache;
import static com.github.minersstudios.msutils.utils.ChatUtils.Colors.*;
import static github.scarsz.discordsrv.util.DiscordUtil.getTextChannelById;
import static github.scarsz.discordsrv.util.DiscordUtil.sendMessage;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;

public final class ChatUtils {

	private ChatUtils() {
		throw new IllegalStateException("Utility class");
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
			Component localMessage = space()
					.append(playerInfo.getDefaultName()
					.append(text(" : "))
					.color(CHAT_COLOR_PRIMARY)
					.hoverEvent(HoverEvent.showText(text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
					.clickEvent(ClickEvent.suggestCommand("/pm " + playerInfo.getID() + " ")))
					.append(message)
					.color(CHAT_COLOR_SECONDARY);
			String stringLocalMessage = serializeLegacyComponent(localMessage);
			location.getBlock().getWorld().getPlayers().stream().filter(
					(player) -> location.distanceSquared(player.getLocation()) <= Math.pow(getConfigCache().localChatRadius, 2.0d)
			).forEach(
					(player) -> player.sendMessage(localMessage)
			);
			sendMessage(getTextChannelById(getConfigCache().discordLocalChannelId), stringLocalMessage);
			Bukkit.getLogger().info(stringLocalMessage);
			return;
		}
		Component globalMessage = space()
				.append(text("[WM] ")
				.append(playerInfo.getDefaultName()
				.append(text(" : ")))
				.color(CHAT_COLOR_PRIMARY)
				.hoverEvent(HoverEvent.showText(text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
				.clickEvent(ClickEvent.suggestCommand("/pm " + playerInfo.getID() + " ")))
				.append(message)
				.color(CHAT_COLOR_SECONDARY);
		String stringGlobalMessage = serializeLegacyComponent(globalMessage);
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getWorld() != MSUtils.getWorldDark()) {
				player.sendMessage(globalMessage);
			}
		}
		sendMessage(getTextChannelById(getConfigCache().discordGlobalChannelId), stringGlobalMessage.replaceFirst("\\[WM]", ""));
		sendMessage(getTextChannelById(getConfigCache().discordLocalChannelId), stringGlobalMessage);
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
		CommandSender commandSender =
				sender == MSUtils.CONSOLE_PLAYER_INFO
				? Bukkit.getConsoleSender()
				: sender.getOnlinePlayer();
		if (commandSender != null && receiver.getOnlinePlayer() != null) {
			String privateMessage = serializeLegacyComponent(
					space()
					.append(sender.getDefaultName()
					.append(text(" -> ")
					.append(receiver.getDefaultName()
					.append(text(" : ")))))
					.color(CHAT_COLOR_PRIMARY)
					.append(message.color(CHAT_COLOR_SECONDARY))
			);
			commandSender.sendMessage(
					Badges.SPEECH.append(text()
					.append(text("Вы -> ")
					.append(receiver.getDefaultName()
					.append(text(" : ")))
					.hoverEvent(HoverEvent.showText(text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
					.clickEvent(ClickEvent.suggestCommand("/pm " + receiver.getID() + " ")))
					.color(CHAT_COLOR_PRIMARY))
					.append(message.color(CHAT_COLOR_SECONDARY))
			);
			receiver.getOnlinePlayer().sendMessage(
					Badges.SPEECH.append(sender.getDefaultName().append(text(" -> Вам : "))
					.color(CHAT_COLOR_PRIMARY)
					.hoverEvent(HoverEvent.showText(text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
					.clickEvent(ClickEvent.suggestCommand("/pm " + sender.getID() + " ")))
					.append(message.color(CHAT_COLOR_SECONDARY))
			);
			sendMessage(getTextChannelById(getConfigCache().discordLocalChannelId), privateMessage);
			return sendInfo(null, text(privateMessage));
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
					text("* ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
					.append(action.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
					.append(text(" * | ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY))
					.append(playerInfo.getGrayIDGoldName());
		} else if (rolePlayActionType == RolePlayActionType.IT) {
			fullMessage =
					text("* ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
					.append(action.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
					.append(text(" *", RP_MESSAGE_MESSAGE_COLOR_PRIMARY));
		} else if (rolePlayActionType == RolePlayActionType.TODO && speech != null) {
			fullMessage =
					text("* ")
					.color(RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
					.append(speech
					.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
					.append(text(" - ")
					.append(text(playerInfo.getPronouns().getSaidMessage())))
					.color(RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
					.append(space())
					.append(playerInfo.getGrayIDGoldName())
					.append(text(", ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY))
					.append(action
					.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
					.append(text(" *", RP_MESSAGE_MESSAGE_COLOR_PRIMARY));
		} else {
			fullMessage =
					text("* ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
					.append(playerInfo.getGrayIDGoldName())
					.append(space()
					.append(action.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY)))
					.append(text(" *", RP_MESSAGE_MESSAGE_COLOR_PRIMARY));
		}
		player.getWorld().getPlayers().stream().filter(
				(p) -> player.getLocation().distanceSquared(p.getLocation()) <= Math.pow(getConfigCache().localChatRadius, 2.0D)
		).forEach(
				(p) -> p.sendMessage(Badges.YELLOW_EXCLAMATION_MARK.append(fullMessage))
		);
		sendMessage(getTextChannelById(getConfigCache().discordLocalChannelId), serializeLegacyComponent(fullMessage));
		Bukkit.getLogger().info(serializeLegacyComponent(fullMessage));
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
				? space()
					.append(killerInfo.getGoldenName()
					.append(space()))
					.append(text(killerInfo.getPronouns().getKillMessage())
					.color(JOIN_MESSAGE_COLOR_PRIMARY)
					.append(space()))
					.append(killedInfo.getGoldenName())
				: space()
					.append(killedInfo.getGoldenName()
					.append(space()))
					.append(text(killedInfo.getPronouns().getDeathMessage()))
					.color(JOIN_MESSAGE_COLOR_PRIMARY);
		String stringDeathMessage = serializeLegacyComponent(deathMessage);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getWorld() != MSUtils.getWorldDark()) {
				onlinePlayer.sendMessage(deathMessage);
			}
		}

		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), () -> {
			sendActionMessage(killed, getTextChannelById(getConfigCache().discordGlobalChannelId), stringDeathMessage, 16757024);
			sendActionMessage(killed, getTextChannelById(getConfigCache().discordLocalChannelId), stringDeathMessage, 16757024);
		});
		Bukkit.getLogger().info(stringDeathMessage);

		Location deathLocation = killed.getLocation();
		sendInfo(null,
				text("Мир и координаты смерти игрока : \"")
				.append(killedInfo.getDefaultName())
				.append(text(" ("))
				.append(text(killed.getName()))
				.append(text(")\" : "))
				.append(text(deathLocation.getBlock().getWorld().getName()))
				.append(space())
				.append(text(deathLocation.getBlockX()))
				.append(space())
				.append(text(deathLocation.getBlockY()))
				.append(space())
				.append(text(deathLocation.getBlockZ()))
		);
	}

	/**
	 * Sends join message
	 *
	 * @param playerInfo playerInfo
	 */
	public static void sendJoinMessage(@NotNull PlayerInfo playerInfo) {
		Player player = playerInfo.getOnlinePlayer();
		if (!PlayerUtils.isOnline(player, true)) return;
		Component joinMessage = space()
						.append(playerInfo.getGoldenName()
						.append(space()))
						.append(text(playerInfo.getPronouns().getJoinMessage()))
						.color(JOIN_MESSAGE_COLOR_PRIMARY);
		String stringJoinMessage = serializeLegacyComponent(joinMessage);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getWorld() != MSUtils.getWorldDark()) {
				onlinePlayer.sendMessage(joinMessage);
			}
		}

		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), () -> {
			ChatUtils.sendActionMessage(player, getTextChannelById(getConfigCache().discordGlobalChannelId), stringJoinMessage, 65280);
			ChatUtils.sendActionMessage(player, getTextChannelById(getConfigCache().discordLocalChannelId), stringJoinMessage, 65280);
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
		if (
				playerInfo.hasNoName()
				|| !PlayerUtils.isOnline(player)
		) return;
		Component quitMessage = space()
						.append(playerInfo.getGoldenName()
						.append(space()))
						.append(text(playerInfo.getPronouns().getQuitMessage()))
						.color(JOIN_MESSAGE_COLOR_PRIMARY);
		String stringQuitMessage = serializeLegacyComponent(quitMessage);

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getWorld() != MSUtils.getWorldDark()) {
				onlinePlayer.sendMessage(quitMessage);
			}
		}

		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), () -> {
			ChatUtils.sendActionMessage(player, getTextChannelById(getConfigCache().discordGlobalChannelId), stringQuitMessage, 16711680);
			ChatUtils.sendActionMessage(player, getTextChannelById(getConfigCache().discordLocalChannelId), stringQuitMessage, 16711680);
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
}
