package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());

		event.quitMessage(null);

		Entity vehicle = player.getVehicle();
		if (vehicle != null) {
			vehicle.eject();
		}

		PlayerUtils.setSitting(player, null, null);
		MSUtils.getConfigCache().playerAnomalyActionMap.remove(player);
		playerInfo.setLastLeaveLocation();
		playerInfo.setHealth(player.getHealth());
		ChatUtils.sendQuitMessage(playerInfo, player);
	}
}
