package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerFile;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import com.github.minersstudios.msutils.utils.MessageUtils;
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
		PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(player);
		PlayerFile playerFile = playerInfo.getPlayerFile();

		event.quitMessage(null);

		Entity vehicle = player.getVehicle();
		if (vehicle != null) {
			vehicle.eject();
		}

		playerInfo.unsetSitting();
		MSUtils.getConfigCache().playerAnomalyActionMap.remove(player);
		if (player.getWorld() != MSUtils.getWorldDark()) {
			playerInfo.setLastLeaveLocation();
			playerFile.setGameMode(player.getGameMode());
			playerFile.setHealth(player.getHealth());
			playerFile.setAir(player.getRemainingAir());
			playerFile.save();
			MessageUtils.sendQuitMessage(playerInfo, player);
		}
	}
}
