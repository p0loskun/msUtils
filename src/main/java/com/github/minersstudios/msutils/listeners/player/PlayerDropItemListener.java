package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerDropItemListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (player.getWorld() == MSUtils.getWorldDark()) {
			event.setCancelled(true);
			player.updateInventory();
		}
	}
}
