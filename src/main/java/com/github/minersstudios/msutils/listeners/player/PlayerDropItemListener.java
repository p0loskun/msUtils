package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDropItemListener implements Listener {

	@EventHandler
	public void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (player.getWorld() == Main.getWorldDark()) {
			event.setCancelled(true);
			player.updateInventory();
		}
	}
}
