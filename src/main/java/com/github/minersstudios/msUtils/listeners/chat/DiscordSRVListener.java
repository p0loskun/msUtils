package com.github.minersstudios.msUtils.listeners.chat;

import com.github.minersstudios.msUtils.utils.ChatUtils;
import com.github.minersstudios.msUtils.Main;
import com.github.minersstudios.msUtils.utils.Config;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.*;
import github.scarsz.discordsrv.dependencies.google.common.base.Function;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.util.LangUtil;
import github.scarsz.discordsrv.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class DiscordSRVListener {

	@Subscribe
	public void discordMessageProcessed(@Nonnull DiscordGuildMessagePreProcessEvent event) {
		Message message = event.getMessage(),
				referencedMessage = message.getReferencedMessage();
		String reply = referencedMessage != null
				? replaceReplyPlaceholders(LangUtil.Message.CHAT_TO_MINECRAFT_REPLY.toString(), referencedMessage)
				: "",
				attachment = !message.getAttachments().isEmpty()
						? message.getAttachments().size() > 1
						? "(вложения) "
						: "(вложение) "
						: "";
		Component messageComponent =
				Config.Symbols.discord
						.color(NamedTextColor.WHITE)
						.append(Component.text(message.getAuthor().getName(), TextColor.color(112, 125, 223)))
						.append(Component.text(reply, TextColor.color(152, 162, 249)))
						.append(Component.text(" : ", TextColor.color(112, 125, 223)))
						.append(Component.text(attachment, TextColor.color(165, 165, 255)))
						.append(Component.text(message.getContentDisplay(), TextColor.color(202, 202, 255)));

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getWorld() != Main.getWorldDark()) {
				player.sendMessage(messageComponent);
			}
		}
		Bukkit.getLogger().info(ChatUtils.legacyComponentSerialize(messageComponent).substring(2));
	}

	@Nonnull
	private static String replaceReplyPlaceholders(@Nonnull String format, @Nonnull Message repliedMessage) {
		Function<String, String> escape = MessageUtil.isLegacy(format)
				? str -> str
				: str -> str.replaceAll("([<>])", "\\\\$1");
		String attachment = !repliedMessage.getAttachments().isEmpty()
				? repliedMessage.getAttachments().size() > 1
				? "(вложения) "
				: "(вложение) "
				: "",
				message = escape.apply(MessageUtil.strip(repliedMessage.getContentDisplay()));
		return message.isEmpty() && attachment.isEmpty() ? ""
				: " (отвечая на \"" + attachment + message + "\")";
	}
}
