package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.mscore.utils.Badges;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.config.ConfigCache;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.PlayerInfoMap;
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
import static com.github.minersstudios.msutils.utils.MessageUtils.Colors.*;
import static github.scarsz.discordsrv.util.DiscordUtil.getTextChannelById;
import static github.scarsz.discordsrv.util.DiscordUtil.sendMessage;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;

public final class MessageUtils {

    private MessageUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Sends message to all players except those in world_dark
     *
     * @param message message
     */
    public static void sendGlobalMessage(@NotNull Component message) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> !player.getWorld().equals(MSUtils.getWorldDark()))
                .forEach(player -> player.sendMessage(message));
    }

    /**
     * Sends message to all players within the specified radius
     *
     * @param message  message
     * @param location center location
     * @param radius   radius
     */
    public static void sendLocalMessage(
            @NotNull Component message,
            @NotNull Location location,
            double radius
    ) {
        Bukkit.getScheduler().runTask(
                MSUtils.getInstance(),
                () -> location.getWorld().getNearbyPlayers(location, radius)
                        .forEach(player -> player.sendMessage(message))
        );
    }

    /**
     * Sends message to chat
     *
     * @param playerInfo player info
     * @param location   sender location
     * @param chat       chat
     * @param message    message
     */
    public static void sendMessageToChat(
            @NotNull PlayerInfo playerInfo,
            @Nullable Location location,
            @NotNull Chat chat,
            @NotNull Component message
    ) {
        ConfigCache configCache = MSUtils.getConfigCache();

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

            sendLocalMessage(localMessage, location, configCache.localChatRadius);
            Bukkit.getScheduler().runTaskAsynchronously(
                    MSUtils.getInstance(),
                    () -> sendMessage(getTextChannelById(configCache.discordLocalChannelId), stringLocalMessage)
            );
            sendInfo(localMessage);
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

        sendGlobalMessage(globalMessage);
        Bukkit.getScheduler().runTaskAsynchronously(DiscordSRV.getPlugin(), () -> {
            sendMessage(getTextChannelById(configCache.discordGlobalChannelId), stringGlobalMessage.replaceFirst("\\[WM]", ""));
            sendMessage(getTextChannelById(configCache.discordLocalChannelId), stringGlobalMessage);
        });
        sendInfo(globalMessage);
    }

    /**
     * Sends private message
     *
     * @param sender   private message sender
     * @param receiver private message receiver
     * @param message  private message
     * @return True if sender or receiver == null
     */
    public static boolean sendPrivateMessage(
            @NotNull PlayerInfo sender,
            @NotNull PlayerInfo receiver,
            @NotNull Component message
    ) {
        CommandSender commandSender = sender == MSUtils.getConsolePlayerInfo()
                ? Bukkit.getConsoleSender()
                : sender.getOnlinePlayer();
        Player receiverPlayer = receiver.getOnlinePlayer();

        if (commandSender != null && receiverPlayer != null) {
            Component privateMessage = space()
                    .append(sender.getDefaultName()
                    .append(text(" -> ")
                    .append(receiver.getDefaultName()
                    .append(text(" : ")))))
                    .color(CHAT_COLOR_PRIMARY)
                    .append(message.color(CHAT_COLOR_SECONDARY));
            String privateMessageString = serializeLegacyComponent(privateMessage);

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
            receiverPlayer.sendMessage(
                    Badges.SPEECH.append(sender.getDefaultName().append(text(" -> Вам : "))
                    .color(CHAT_COLOR_PRIMARY)
                    .hoverEvent(HoverEvent.showText(text("Нажмите, чтобы написать приватное сообщение данному игроку", NamedTextColor.GRAY)))
                    .clickEvent(ClickEvent.suggestCommand("/pm " + sender.getID() + " ")))
                    .append(message.color(CHAT_COLOR_SECONDARY))
            );
            Bukkit.getScheduler().runTaskAsynchronously(
                    DiscordSRV.getPlugin(),
                    () -> sendMessage(getTextChannelById(MSUtils.getConfigCache().discordLocalChannelId), privateMessageString)
            );
            sendInfo(privateMessage);
            return true;
        }
        return false;
    }

    /**
     * Sends rp event message to chat
     *
     * @param sender             player
     * @param speech             speech
     * @param action             action
     * @param rolePlayActionType rp action type
     */
    public static void sendRPEventMessage(
            @NotNull Player sender,
            @NotNull Component speech,
            @NotNull Component action,
            @NotNull RolePlayActionType rolePlayActionType
    ) {
        ConfigCache configCache = MSUtils.getConfigCache();
        PlayerInfoMap playerInfoMap = configCache.playerInfoMap;
        PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(sender);
        Component fullMessage = switch (rolePlayActionType) {
            case DO ->
                    text("* ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
                    .append(action.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
                    .append(text(" * | ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY))
                    .append(playerInfo.getGrayIDGoldName());
            case IT ->
                    text("* ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
                    .append(action.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
                    .append(text(" *", RP_MESSAGE_MESSAGE_COLOR_PRIMARY));
            case TODO ->
                    text("* ")
                    .color(RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
                    .append(speech
                    .color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
                    .append(text(" - ")
                    .append(text(playerInfo.getPlayerFile().getPronouns().getSaidMessage())))
                    .color(RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
                    .append(space())
                    .append(playerInfo.getGrayIDGoldName())
                    .append(text(", ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY))
                    .append(action
                    .color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY))
                    .append(text(" *", RP_MESSAGE_MESSAGE_COLOR_PRIMARY));
            default ->
                    text("* ", RP_MESSAGE_MESSAGE_COLOR_PRIMARY)
                    .append(playerInfo.getGrayIDGoldName())
                    .append(space()
                    .append(action.color(RP_MESSAGE_MESSAGE_COLOR_SECONDARY)))
                    .append(text(" *", RP_MESSAGE_MESSAGE_COLOR_PRIMARY));
        };

        sendLocalMessage(Badges.YELLOW_EXCLAMATION_MARK.append(fullMessage), sender.getLocation(), configCache.localChatRadius);
        Bukkit.getScheduler().runTaskAsynchronously(
                DiscordSRV.getPlugin(),
                () -> sendMessage(getTextChannelById(configCache.discordLocalChannelId), serializeLegacyComponent(fullMessage))
        );
        sendInfo(fullMessage);
    }

    public static void sendRPEventMessage(
            @NotNull Player player,
            @NotNull Component action,
            @NotNull RolePlayActionType rolePlayActionType
    ) {
        sendRPEventMessage(player, Component.empty(), action, rolePlayActionType);
    }

    /**
     * Sends death message
     *
     * @param killed killed player
     * @param killer killer player
     */
    public static void sendDeathMessage(
            @NotNull Player killed,
            @Nullable Player killer
    ) {
        ConfigCache configCache = MSUtils.getConfigCache();
        PlayerInfoMap playerInfoMap = configCache.playerInfoMap;
        Location deathLocation = killed.getLocation();
        PlayerInfo killedInfo = playerInfoMap.getPlayerInfo(killed);
        PlayerInfo killerInfo = killer != null
                        ? playerInfoMap.getPlayerInfo(killer)
                        : null;
        Component deathMessage = killerInfo != null
                ? space()
                .append(killerInfo.getGoldenName()
                .append(space()))
                .append(text(killerInfo.getPlayerFile().getPronouns().getKillMessage())
                .color(JOIN_MESSAGE_COLOR_PRIMARY)
                .append(space()))
                .append(killedInfo.getGoldenName())
                : space()
                .append(killedInfo.getGoldenName()
                .append(space()))
                .append(text(killedInfo.getPlayerFile().getPronouns().getDeathMessage()))
                .color(JOIN_MESSAGE_COLOR_PRIMARY);
        String stringDeathMessage = serializeLegacyComponent(deathMessage);

        killedInfo.setLastDeathLocation(deathLocation);
        sendGlobalMessage(deathMessage);
        Bukkit.getScheduler().runTaskAsynchronously(DiscordSRV.getPlugin(), () -> {
            sendActionMessage(killed, getTextChannelById(configCache.discordGlobalChannelId), stringDeathMessage, 16757024);
            sendActionMessage(killed, getTextChannelById(configCache.discordLocalChannelId), stringDeathMessage, 16757024);
        });
        sendInfo(deathMessage);

        sendInfo(text("Мир и координаты смерти игрока : \"")
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

        if (
                !playerInfo.isOnline(true)
                || player == null
        ) return;

        ConfigCache configCache = MSUtils.getConfigCache();
        Component joinMessage = space()
                .append(playerInfo.getGoldenName()
                .append(space()))
                .append(text(playerInfo.getPlayerFile().getPronouns().getJoinMessage()))
                .color(JOIN_MESSAGE_COLOR_PRIMARY);
        String stringJoinMessage = serializeLegacyComponent(joinMessage);

        sendGlobalMessage(joinMessage);
        Bukkit.getScheduler().runTaskAsynchronously(DiscordSRV.getPlugin(), () -> {
            sendActionMessage(player, getTextChannelById(configCache.discordGlobalChannelId), stringJoinMessage, 65280);
            sendActionMessage(player, getTextChannelById(configCache.discordLocalChannelId), stringJoinMessage, 65280);
        });
        sendInfo(joinMessage);
    }

    /**
     * Sends leave message
     *
     * @param playerInfo playerInfo
     * @param player     player
     */
    public static void sendQuitMessage(
            @NotNull PlayerInfo playerInfo,
            @NotNull Player player
    ) {
        if (!playerInfo.isOnline()) return;
        ConfigCache configCache = MSUtils.getConfigCache();
        Component quitMessage = space()
                .append(playerInfo.getGoldenName()
                .append(space()))
                .append(text(playerInfo.getPlayerFile().getPronouns().getQuitMessage()))
                .color(JOIN_MESSAGE_COLOR_PRIMARY);
        String stringQuitMessage = serializeLegacyComponent(quitMessage);

        sendGlobalMessage(quitMessage);
        Bukkit.getScheduler().runTaskAsynchronously(DiscordSRV.getPlugin(), () -> {
           sendActionMessage(player, getTextChannelById(configCache.discordGlobalChannelId), stringQuitMessage, 16711680);
           sendActionMessage(player, getTextChannelById(configCache.discordLocalChannelId), stringQuitMessage, 16711680);
        });
        sendInfo(quitMessage);
    }

    private static void sendActionMessage(
            @NotNull Player player,
            TextChannel textChannel,
            @NotNull String actionMessage,
            int colorRaw
    ) {
        if (DiscordUtil.getJda() == null) return;
        DiscordUtil.queueMessage(
                textChannel,
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
                                        : DiscordUtil.getJda().getSelfUser().getName()
                        ),
                        (content, needsEscape) -> PlaceholderUtil.replacePlaceholdersToDiscord(content, player)
                ),
                true
        );
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
