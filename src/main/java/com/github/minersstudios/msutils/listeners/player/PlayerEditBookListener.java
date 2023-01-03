package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.player.PlayerInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

public class PlayerEditBookListener implements Listener {

	@EventHandler
	public void onPlayerEditBook(@NotNull PlayerEditBookEvent event) {
		if (!event.isSigning()) return;
		BookMeta bookMeta = event.getNewBookMeta();
		String title = bookMeta.getTitle();
		boolean isAnon = title != null && title.startsWith("*");
		event.setNewBookMeta(bookMeta
				.author(
						isAnon ? Component.text("Аноним")
						: new PlayerInfo(event.getPlayer().getUniqueId()).getDefaultName()
				).title(
						isAnon ? Component.text(title.substring(1))
						: bookMeta.title()
				)
		);
	}
}
