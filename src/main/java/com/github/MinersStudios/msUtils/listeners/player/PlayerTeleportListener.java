package com.github.MinersStudios.msUtils.listeners.player;

import com.github.MinersStudios.msUtils.utils.PlayerUtils;
import com.github.MinersStudios.msUtils.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.annotation.Nonnull;

public class PlayerTeleportListener implements Listener {

	@EventHandler
	public void onPlayerTeleport(@Nonnull PlayerTeleportEvent event) {
		PlayerUtils.setSitting(event.getPlayer(), null, null);
		event.setCancelled(event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE && event.getPlayer().getWorld() == Main.getWorldDark());
	}
}
