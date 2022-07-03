package github.minersStudios.msUtils.listeners.chat;

import github.minersStudios.msUtils.Main;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.*;
import github.scarsz.discordsrv.dependencies.google.common.base.Function;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.util.LangUtil;
import github.scarsz.discordsrv.util.MessageUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class DiscordSRVListener {

	@Subscribe
	public void discordMessageProcessed(@Nonnull DiscordGuildMessagePreProcessEvent event) {
		Message message = event.getMessage(),
				referencedMessage = message.getReferencedMessage();
		String reply = referencedMessage != null ? replaceReplyPlaceholders(LangUtil.Message.CHAT_TO_MINECRAFT_REPLY.toString(), referencedMessage) : "",
				attachment = !message.getAttachments().isEmpty() ? message.getAttachments().size() > 1 ? "(вложения) " : "(вложение) " : "",
				stringMessage = " " + ChatColor.of("#707ddf") + message.getAuthor().getName() + reply + " : " + ChatColor.of("#a5a5ff") + attachment + ChatColor.of("#cacaff") + message.getContentDisplay();
		for (Player player : Bukkit.getOnlinePlayers())
			if (player.getWorld() != Main.worldDark)
				player.sendMessage(" \uA014" + stringMessage);
		Bukkit.getLogger().info(stringMessage);
	}

	@Nonnull
	private static String replaceReplyPlaceholders(@Nonnull String format, @Nonnull Message repliedMessage) {
		Function<String, String> escape = MessageUtil.isLegacy(format) ? str -> str : str -> str.replaceAll("([<>])", "\\\\$1");
		String attachment = !repliedMessage.getAttachments().isEmpty() ? repliedMessage.getAttachments().size() > 1 ? "(вложения) " : "(вложение) " : "",
				message = escape.apply(MessageUtil.strip(repliedMessage.getContentDisplay()));
		return message.isEmpty() && attachment.isEmpty() ? ""
				: ChatColor.of("#98a2f9") + " (отвечая на \"" + attachment + message + "\")" + ChatColor.of("#707ddf");
	}
}
