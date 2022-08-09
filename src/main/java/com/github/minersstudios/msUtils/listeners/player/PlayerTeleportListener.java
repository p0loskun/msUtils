package com.github.minersstudios.msUtils.listeners.player;

import com.github.minersstudios.msUtils.utils.PlayerUtils;
import com.github.minersstudios.msUtils.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.annotation.Nonnull;

public class PlayerTeleportListener implements Listener {

	@EventHandler
	public void onPlayerTeleport(@Nonnull PlayerTeleportEvent event) {
		PlayerUtils.setSitting(event.getPlayer(), null, (String) null);
		event.setCancelled(event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE && event.getPlayer().getWorld() == Main.getWorldDark());
	}
}
