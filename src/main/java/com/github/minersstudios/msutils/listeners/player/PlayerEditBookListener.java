package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

@MSListener
public class PlayerEditBookListener implements Listener {

	@EventHandler
	public void onPlayerEditBook(@NotNull PlayerEditBookEvent event) {
		if (!event.isSigning()) return;
		BookMeta bookMeta = event.getNewBookMeta();
		String title = bookMeta.getTitle();
		boolean isAnon = title != null && title.startsWith("*");
		event.setNewBookMeta(bookMeta
				.author(
						isAnon ? text("Аноним")
						: MSPlayerUtils.getPlayerInfo(event.getPlayer()).createDefaultName()
				).title(
						isAnon ? text(title.substring(1))
						: bookMeta.title()
				)
		);
	}
}
