package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerTeleportListener implements Listener {

	@EventHandler
	public void onPlayerTeleport(@NotNull PlayerTeleportEvent event) {
		MSPlayerUtils.getPlayerInfo(event.getPlayer()).unsetSitting();
		if (
				event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE
				&& event.getPlayer().getWorld() == MSUtils.getWorldDark()
		) {
			event.setCancelled(true);
		}
	}
}
