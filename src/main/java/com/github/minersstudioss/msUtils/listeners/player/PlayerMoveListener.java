package com.github.minersstudios.msUtils.listeners.player;

import com.github.minersstudios.msUtils.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;

public class PlayerMoveListener implements Listener {

	@EventHandler
	public void onPlayerMove(@Nonnull PlayerMoveEvent event) {
		event.setCancelled(event.getPlayer().getWorld() == Main.getWorldDark());
	}
}
