package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.utils.PlayerUtils;
import com.github.minersstudios.msutils.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerTeleportListener implements Listener {

	@EventHandler
	public void onPlayerTeleport(@NotNull PlayerTeleportEvent event) {
		PlayerUtils.setSitting(event.getPlayer(), null, null);
		event.setCancelled(event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE && event.getPlayer().getWorld() == Main.getWorldDark());
	}
}
