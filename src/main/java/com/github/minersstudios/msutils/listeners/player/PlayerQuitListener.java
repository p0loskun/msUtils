package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.classes.PlayerInfo;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.utils.PlayerUtils;
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

		PlayerUtils.setSitting(player, null, (String) null);
		event.quitMessage(null);
		playerInfo.setLastLeaveLocation();
		playerInfo.setHealth(player.getHealth());
		ChatUtils.sendQuitMessage(playerInfo, player);
	}
}
