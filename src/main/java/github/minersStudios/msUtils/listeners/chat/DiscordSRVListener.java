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

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class DiscordSRVListener {

	@Subscribe
	public void discordMessageProcessed(DiscordGuildMessagePostProcessEvent event) {
		Bukkit.getOnlinePlayers().forEach((player) -> {
			String reply = event.getMessage().getReferencedMessage() != null ? replaceReplyPlaceholders(LangUtil.Message.CHAT_TO_MINECRAFT_REPLY.toString(), event.getMessage().getReferencedMessage()) : "";
			if(player.getWorld() != Main.worldDark) player.sendMessage(" \uA014 " + ChatColor.of("#707ddf") + event.getMessage().getAuthor().getName() + reply + " : " + ChatColor.of("#cacaff") + event.getMessage().getContentDisplay());
		});
	}

	private String replaceReplyPlaceholders(@Nonnull String format, @Nonnull Message repliedMessage) {
		Function<String, String> escape = MessageUtil.isLegacy(format)
				? str -> str
				: str -> str.replaceAll("([<>])", "\\\\$1");

		final String repliedUserName = repliedMessage.getMember() != null ? repliedMessage.getMember().getEffectiveName() : repliedMessage.getAuthor().getName();

		return format.replace("%name%", escape.apply(MessageUtil.strip(repliedUserName)))
				.replace("%username%", escape.apply(MessageUtil.strip(repliedMessage.getAuthor().getName())))
				.replace("%message%", escape.apply(MessageUtil.strip(repliedMessage.getContentDisplay())));
	}
}
