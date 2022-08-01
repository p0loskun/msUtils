package com.github.MinersStudios.msUtils.listeners.player;

import com.github.MinersStudios.msUtils.classes.PlayerInfo;
import com.github.MinersStudios.msUtils.utils.ChatUtils;
import com.github.MinersStudios.msUtils.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());

		PlayerUtils.setSitting(player, null);
		event.quitMessage(null);
		playerInfo.setLastLeaveLocation();
		ChatUtils.sendQuitMessage(playerInfo, player);
	}
}
