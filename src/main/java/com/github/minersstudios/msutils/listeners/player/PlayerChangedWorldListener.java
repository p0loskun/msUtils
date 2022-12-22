package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerChangedWorldListener implements Listener {

	@EventHandler
	public void onPlayerChangedWorld(@NotNull PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		Main.getScoreboardHideTagsTeam().addEntry(player.getName());
		player.setScoreboard(Main.getScoreboardHideTags());
	}
}
