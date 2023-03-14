package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerArmorStandManipulateListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerArmorStandManipulate(@NotNull PlayerArmorStandManipulateEvent event) {
		if (MSUtils.getConfigCache().seats.containsValue(event.getRightClicked())) {
			event.setCancelled(true);
		}
	}
}
