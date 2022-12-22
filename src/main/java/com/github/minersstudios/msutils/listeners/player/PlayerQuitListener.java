package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());

		PlayerUtils.setSitting(player, null, null);
		event.quitMessage(null);
		playerInfo.setLastLeaveLocation();
		playerInfo.setHealth(player.getHealth());
		ChatUtils.sendQuitMessage(playerInfo, player);
	}
}
