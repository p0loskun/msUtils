package github.minersStudios.msUtils.utils;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.enums.Pronouns;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.commons.lang3.StringUtils;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.objects.MessageFormat;
import github.scarsz.discordsrv.util.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiFunction;

import static github.scarsz.discordsrv.DiscordSRV.*;

public final class ChatUtils {

    public static final String discordGlobalChannelID = Main.plugin.getConfig().getString("discord-global-channel-id");
    public static final String discordLocalChannelID = Main.plugin.getConfig().getString("discord-local-channel-id");

    /**
     * Sends fine message to target
     *
     * @param target target
     * @param message warning message
     */
    public static void sendFine(@Nullable Object target, @Nonnull String message){
        if (target instanceof Player player) {
            player.sendMessage(" ꀒ " + ChatColor.GREEN + message);
        } else if (target instanceof CommandSender sender) {
            sender.sendMessage(ChatColor.GREEN + message);
        } else {
            Bukkit.getLogger().info(ChatColor.GREEN + message);
        }
    }

    /**
     * Sends warning message to target
     *
     * @param target target
     * @param message warning message
     */
    public static void sendWarning(@Nullable Object target, @Nonnull String message){
        if (target instanceof Player player) {
            player.sendMessage(" ꀓ " + ChatColor.GOLD + message);
        } else if (target instanceof CommandSender sender) {
            sender.sendMessage(ChatColor.GOLD + message);
        } else {
            Bukkit.getLogger().warning(ChatColor.GOLD + message);
        }
    }

    /**
     * Sends error message to target
     *
     * @param target target
     * @param message warning message
     */
    public static void sendError(@Nullable Object target, @Nonnull String message){
        if (target instanceof Player player) {
            player.sendMessage(" ꀑ " + ChatColor.RED + message);
        } else if (target instanceof CommandSender sender) {
            sender.sendMessage(ChatColor.RED + message);
        } else {
            Bukkit.getLogger().warning(ChatColor.RED + message);
        }
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
        String senderName = " [" + playerInfo.getID() + "] " + playerInfo.getFirstname() + " " + playerInfo.getLastname();
        if (radius > -1 && location != null) {
            String localMessage = ChatColor.of("#aba494") + senderName + " : " + ChatColor.of("#f2f0e3") + message;
            location.getBlock().getWorld().getPlayers().stream().filter(
                    (player) -> location.distanceSquared(player.getLocation()) <= Math.pow(radius, 2.0D)
            ).forEach(
                    (player) -> player.sendMessage(localMessage)
            );
            DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), localMessage);
            Bukkit.getLogger().info(localMessage);
        } else {
            String globalMessage = senderName + " : " + ChatColor.of("#f2f0e3") + message;
            Bukkit.getOnlinePlayers().forEach((player) -> {
                if(player.getWorld() != Main.worldDark) player.sendMessage(ChatColor.of("#aba494") + " [CTD]" + globalMessage);
            });
            DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID), globalMessage);
            DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), " [CTD]" + globalMessage);
            Bukkit.getLogger().info(globalMessage);
        }
    }

    /**
     * Sends private message
     *
     * @param sender private message sender
     * @param receiver private message receiver
     * @param message private message
     */
    public static void sendPrivateMessage(@Nonnull PlayerInfo sender, @Nonnull PlayerInfo receiver, @Nonnull String message){
        if(sender.getOnlinePlayer() == null || receiver.getOnlinePlayer() == null) return;
        String senderName = " [" + sender.getID() + "] " + sender.getFirstname() + " " + sender.getLastname(),
                receiverName = " [" + receiver.getID() + "] " + receiver.getFirstname() + " " + receiver.getLastname();
        sender.getOnlinePlayer().sendMessage(" \uA015" + ChatColor.of("#aba494") + " Вы" + " ->" + receiverName + " : " + ChatColor.of("#f2f0e3") + message);
        receiver.getOnlinePlayer().sendMessage(" \uA015" + ChatColor.of("#aba494") + senderName + " -> " + "Вам" + " : " + ChatColor.of("#f2f0e3") + message);
    }

    /**
     * Sends rp event message to chat
     *
     * @param player player
     * @param radius message radius
     * @param message message
     */
    public static void sendRPEventMessage(@Nonnull Player player, int radius, @Nonnull String message) {
        player.getWorld().getPlayers().stream().filter((p) -> player.getLocation().distanceSquared(p.getLocation()) <= Math.pow(radius, 2.0D)).forEach((p) -> p.sendMessage(" ꀓ " +message));
        DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), message);
        Bukkit.getLogger().info(message);
    }

    /**
     * Removes first chat from message
     *
     * @param message message
     * @return message without first char
     */
    public static String removeFirstChar(String message) {
        if (message == null || message.length() == 0) {
            return message;
        }
        return message.substring(1);
    }

    /**
     * Sends join message
     *
     * @param playerInfo playerInfo
     */
    public static void sendJoinMessage(@Nonnull PlayerInfo playerInfo, @Nonnull Player player){
        String joinMessage =
                ChatColor.of("#fcf5c7") + " [" + playerInfo.getID() + "] "
                + ChatColor.of("#ffee93") + playerInfo.getFirstname() + " "
                + playerInfo.getLastname() + " "
                + (playerInfo.getPronouns() != null ? playerInfo.getPronouns().getJoinMessage()
                : Pronouns.HE.getJoinMessage());
        Bukkit.getOnlinePlayers().forEach((onlinePlayer) -> {
            if(player.getWorld() != Main.worldDark) onlinePlayer.sendMessage(joinMessage);
        });
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            ChatUtils.sendJoinMessage(player, playerInfo, DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID));
            ChatUtils.sendJoinMessage(player, playerInfo, DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID));
        });
        Bukkit.getLogger().info(joinMessage);
    }

    /**
     * Sends leave message
     *
     * @param playerInfo playerInfo
     */
    public static void sendLeaveMessage(@Nonnull PlayerInfo playerInfo, @Nonnull Player player){
        String leaveMessage =
                ChatColor.of("#fcf5c7") + " [" + playerInfo.getID() + "] "
                        + ChatColor.of("#ffee93") + playerInfo.getFirstname() + " "
                        + playerInfo.getLastname() + " "
                        + (playerInfo.getPronouns() != null ? playerInfo.getPronouns().getQuitMessage()
                        : Pronouns.HE.getQuitMessage());
        Bukkit.getOnlinePlayers().forEach((onlinePlayer) -> {
            if(player.getWorld() != Main.worldDark) onlinePlayer.sendMessage(leaveMessage);
        });
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            ChatUtils.sendLeaveMessage(player, playerInfo, DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID));
            ChatUtils.sendLeaveMessage(player, playerInfo, DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID));
        });
        Bukkit.getLogger().info(leaveMessage);
    }

    /**
     * Sends dialogue message to player
     *
     * @param message message
     */
    public static void sendDialogueMessage(@Nonnull Player player, @Nonnull String message, long delay){
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            player.sendMessage(ChatColor.of("#aba494") + " [0] Незнакомец : " + ChatColor.of("#f2f0e3") + message);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 0.5f, 1.5f);
        }, delay);
    }

    private static void sendJoinMessage(@Nonnull Player player, @Nonnull PlayerInfo playerInfo, @Nonnull TextChannel textChannel) {
        MessageFormat messageFormat = new MessageFormat("", "%displayname% " + (playerInfo.getPronouns() != null ? playerInfo.getPronouns().getJoinMessage() : Pronouns.HE.getJoinMessage()), "", "%embedavatarurl%", "", "", "", "", "", "", "", null, 65280, null, false, "", "");
        if (messageFormat.isAnyContent()) {
            String displayName = StringUtils.isNotBlank(player.getDisplayName()) ? MessageUtil.strip(player.getDisplayName()) : "";
            StringUtils.isNotBlank(null);
            String message = "";
            String name = player.getName();
            String avatarUrl = getAvatarUrl(player);
            String botAvatarUrl = DiscordUtil.getJda().getSelfUser().getEffectiveAvatarUrl();
            String botName = DiscordSRV.getPlugin().getMainGuild() != null ? DiscordSRV.getPlugin().getMainGuild().getSelfMember().getEffectiveName() : DiscordUtil.getJda().getSelfUser().getName();
            BiFunction<String, Boolean, String> translator = (content, needsEscape) -> {
                if (content == null) {
                    return null;
                } else {
                    content = content.replaceAll("%time%|%date%", TimeUtil.timeStamp()).replace("%message%", MessageUtil.strip(needsEscape ? DiscordUtil.escapeMarkdown(message) : message)).replace("%username%", needsEscape ? DiscordUtil.escapeMarkdown(name) : name).replace("%displayname%", needsEscape ? DiscordUtil.escapeMarkdown(displayName) : displayName).replace("%usernamenoescapes%", name).replace("%displaynamenoescapes%", displayName).replace("%embedavatarurl%", avatarUrl).replace("%botavatarurl%", botAvatarUrl).replace("%botname%", botName);
                    content = DiscordUtil.translateEmotes(content, textChannel.getGuild());
                    content = PlaceholderUtil.replacePlaceholdersToDiscord(content, player);
                    return content;
                }
            };
            github.scarsz.discordsrv.dependencies.jda.api.entities.Message discordMessage = translateMessage(messageFormat, translator);
            if (discordMessage != null) {
                String webhookName = translator.apply(messageFormat.getWebhookName(), false);
                String webhookAvatarUrl = translator.apply(messageFormat.getWebhookAvatarUrl(), false);
                if (messageFormat.isUseWebhooks()) {
                    WebhookUtil.deliverMessage(textChannel, webhookName, webhookAvatarUrl, discordMessage.getContentRaw(), discordMessage.getEmbeds().stream().findFirst().orElse(null));
                } else {
                    DiscordUtil.queueMessage(textChannel, discordMessage, true);
                }

            }
        } else {
            debug("Not sending join message due to it being disabled");
        }
    }

    private static void sendLeaveMessage(@Nonnull Player player, @Nonnull PlayerInfo playerInfo, @Nonnull TextChannel textChannel) {
        MessageFormat messageFormat = new MessageFormat("", "%displayname% " + (playerInfo.getPronouns() != null ? playerInfo.getPronouns().getQuitMessage() : Pronouns.HE.getQuitMessage()), "", "%embedavatarurl%", "", "", "", "", "", "", "", null, 16711680, null, false, "", "");
        if (messageFormat.isAnyContent()) {
            String displayName = StringUtils.isNotBlank(player.getDisplayName()) ? MessageUtil.strip(player.getDisplayName()) : "";
            StringUtils.isNotBlank(null);
            String message = "";
            String name = player.getName();
            String avatarUrl = getAvatarUrl(player);
            String botAvatarUrl = DiscordUtil.getJda().getSelfUser().getEffectiveAvatarUrl();
            String botName = DiscordSRV.getPlugin().getMainGuild() != null ? DiscordSRV.getPlugin().getMainGuild().getSelfMember().getEffectiveName() : DiscordUtil.getJda().getSelfUser().getName();
            BiFunction<String, Boolean, String> translator = (content, needsEscape) -> {
                if (content == null) {
                    return null;
                } else {
                    content = content.replaceAll("%time%|%date%", TimeUtil.timeStamp()).replace("%message%", MessageUtil.strip(needsEscape ? DiscordUtil.escapeMarkdown(message) : message)).replace("%username%", MessageUtil.strip(needsEscape ? DiscordUtil.escapeMarkdown(name) : name)).replace("%displayname%", needsEscape ? DiscordUtil.escapeMarkdown(displayName) : displayName).replace("%usernamenoescapes%", name).replace("%displaynamenoescapes%", displayName).replace("%embedavatarurl%", avatarUrl).replace("%botavatarurl%", botAvatarUrl).replace("%botname%", botName);
                    content = DiscordUtil.translateEmotes(content, textChannel.getGuild());
                    content = PlaceholderUtil.replacePlaceholdersToDiscord(content, player);
                    return content;
                }
            };
            github.scarsz.discordsrv.dependencies.jda.api.entities.Message discordMessage = translateMessage(messageFormat, translator);
            if (discordMessage != null) {
                String webhookName = translator.apply(messageFormat.getWebhookName(), false);
                String webhookAvatarUrl = translator.apply(messageFormat.getWebhookAvatarUrl(), false);
                if (messageFormat.isUseWebhooks()) {
                    WebhookUtil.deliverMessage(textChannel, webhookName, webhookAvatarUrl, discordMessage.getContentRaw(), discordMessage.getEmbeds().stream().findFirst().orElse(null));
                } else {
                    DiscordUtil.queueMessage(textChannel, discordMessage, true);
                }

            }
        } else {
            debug("Not sending leave message due to it being disabled");
        }
    }

    @Nonnull
    public static String extractMessage(@Nonnull String[] args, int start) {
        return String.join(" ", Arrays.copyOfRange(args, start, args.length));
    }
}
