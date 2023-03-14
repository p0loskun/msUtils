package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerChangedWorldListener implements Listener {

	@EventHandler
	public void onPlayerChangedWorld(@NotNull PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		MSUtils.getScoreboardHideTagsTeam().addEntry(player.getName());
		player.setScoreboard(MSUtils.getScoreboardHideTags());
	}
}
