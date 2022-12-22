package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerMoveListener implements Listener {

	@EventHandler
	public void onPlayerMove(@NotNull PlayerMoveEvent event) {
		event.setCancelled(event.getPlayer().getWorld() == Main.getWorldDark());
	}
}
