package com.github.minersstudios.msutils.classes;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.utils.ChatUtils;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.util.List;
import java.util.logging.Level;

public record Chat(boolean isEnabled,
                   double getRadius,
                   List<String> getPermissions,
                   String getPrefix,
                   TextColor getPrimaryColor,
                   TextColor getSecondaryColor,
                   boolean isDiscordEnabled,
                   List<String> getDiscordChannelIds,
                   String getSymbol,
                   boolean isEnableChatSymbols,
                   boolean isShowMessagesAboveHead
) {
	public static Chat actionsChat;
	public static Chat joinChat;
	public static Chat quitChat;
	public static Chat deathChat;
	public static Chat privateMessagesChat;

	@Nullable
	public static Chat getChatBySymbol(String message) {
		for (Chat chat : Main.getConfigCache().chats.values()) {
			if (chat.getSymbol() != null) {
				return message.startsWith(chat.getSymbol()) ? chat : null;
			}
		}
		return null;
	}

	public boolean isUtilityChat() {
		return actionsChat.equals(this)
				|| joinChat.equals(this)
				|| quitChat.equals(this)
				|| deathChat.equals(this)
				|| privateMessagesChat.equals(this);
	}

	@Nullable
	public static Chat getChatWithoutSymbol() {
		Chat chat = null;
		for (Chat configChat : Main.getConfigCache().chats.values()) {
			if (
					configChat.getSymbol() == null
							&& !configChat.isUtilityChat()
							&& configChat.isEnabled()
			) {
				chat = configChat;
			}
		}
		if (chat == null) {
			ChatUtils.log(Level.SEVERE, "Chat without symbol in configuration is not found!");
			Bukkit.getPluginManager().disablePlugin(Main.getInstance());
			return null;
		}
		return chat;
	}
}


