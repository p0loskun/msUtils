package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

@MSListener
public class PlayerSpawnLocationListener implements Listener {

	@EventHandler
	public void onPlayerSpawnLocation(@NotNull PlayerSpawnLocationEvent event) {
		if (!event.getPlayer().isDead()) {
			event.setSpawnLocation(new Location(MSUtils.getWorldDark(), 0.0d, 0.0d, 0.0d));
		}
	}
}
